from concurrent.futures import ThreadPoolExecutor, as_completed
from dao import DatabaseConnection, insert_data, select_join


def save_data(authorship_staging):
    conn = DatabaseConnection()
    with conn:
        with conn.get_cursor() as cursor:
            try:
                insert_data(cursor, 'tb_authorships', authorship_staging)
            except Exception as e:
                print(f"Error inserting authorship data: {e}")


def etl():
    conn = DatabaseConnection()
    authorship_staging = []
    with conn:
        with conn.get_cursor() as cursor:
            authorship_staging = select_join(
                cursor,
                'ror_id as instituition_id, author_id, work_id',
                'staging.authorship_work_ids as f',
                'staging.map_organization_ror_id as s',
                'f.instituition_id = s.id'
            )

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for item in authorship_staging:
            futures.append(executor.submit(save_data, item))

        for future in as_completed(futures):
            try:
                future.result()
            except Exception as exc:
                print(f'Erro na thread: {exc}')


if __name__ == "__main__":
    try:
        etl()
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
