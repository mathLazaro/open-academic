from concurrent.futures import ThreadPoolExecutor, as_completed
from api_request import fetch_data
from dao import DatabaseConnection, insert_batch_data, select_data

CHUNK_SIZE = 50


def get_params(ids):
    id_param = '|'.join(ids)
    return {
        'filter': 'id:' + id_param,
        'per-page': 200,
        'mailto': 'matheus.lazaro@outlook.com'
    }


def get_chunk_list(lst, chunk_size):
    for i in range(0, len(lst), chunk_size):
        yield lst[i:i + chunk_size]


def pipeline(conn, author_ids):
    try:
        authors = []
        params = get_params(author_ids)

        data = fetch_data('authors', params).get('results', [])

        if not data:
            print("No more author data.")
            return

        authors.extend([{
            'id': item.get('id'),
            'name': item.get('display_name'),
            'works_count': item.get('works_count'),
            'cited_by_count': item.get('cited_by_count')
        } for item in data if item.get('id') and item.get('display_name')])

        with conn.get_cursor() as cursor:
            if authors:
                insert_batch_data(cursor, 'tb_authors', authors)
    except Exception as e:
        print(f"Error fetching author data: {e}")


def etl(conn: DatabaseConnection):
    with conn.get_cursor() as cursor:
        authorship_staging = select_data(
            cursor,
            'author_id',
            'staging.authorship_work_ids',
            'processed = FALSE'
        )

        author_id_list = list({item['author_id']
                              for item in authorship_staging})

        with ThreadPoolExecutor(max_workers=10) as executor:
            futures = []
            for chunk in get_chunk_list(author_id_list, CHUNK_SIZE):
                futures.append(executor.submit(pipeline, conn, chunk))
                print(f'Processing author chunk: {chunk}')

            for future in as_completed(futures):
                try:
                    future.result()
                except Exception as exc:
                    print(f'Erro na thread: {exc}')


if __name__ == "__main__":
    try:
        conn = DatabaseConnection()
        conn.open_connection()
        if not conn.is_open():
            Exception("Database connection is not open.")

        etl(conn)

        conn.close_connection()
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
