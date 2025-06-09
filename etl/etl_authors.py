from concurrent.futures import ThreadPoolExecutor, as_completed
import sys

from utils import fetch_data, get_chunk_list
from dao import insert_batch_data, select_data, select_join


def get_from_database():
    try:
        data = select_join(
            select='distinct(author_id)',
            table1='tb_authorships',
            table2='tb_authors',
            on='tb_authorships.author_id = tb_authors.id',
            misc='limit 50000',
            where='tb_authors.id is null',
            join_type='LEFT JOIN'
        )
        return [item.get('author_id') for item in data]
    except Exception as e:
        print(f"Error fetching author IDs from database: {e}")
        sys.exit(1)


def save_to_database(entities):
    try:
        insert_batch_data(
            table='tb_authors',
            data=entities
        )
        print(f'Saved {len(entities)} author entities to the database')
    except Exception as e:
        print(f"An error occurred while saving to the database: {e}")
        sys.exit(1)


def extract(ids) -> list:
    try:
        print(f'Fetching author data from chunk')
        return fetch_data('authors', f'id:{'|'.join(ids)}').get('results', [])
    except Exception as e:
        print(f"Error fetching author data: {e}")
        sys.exit(1)


def pipeline(authors) -> list:
    authors_entities = []
    id_added = set()
    try:
        for item in authors:
            if item.get('id') and item.get('display_name'):
                id = item.get('id')
                if id in id_added:
                    continue
                obj  = {
                    'id': id,
                    'name': item.get('display_name', ' - '),
                    'works_count': item.get('works_count', 0),
                    'cited_by_count': item.get('cited_by_count', 0)
                }
                if obj not in authors_entities:
                    id_added.add(id)
                    authors_entities.append(obj)
        return authors_entities
    except Exception as e:
        print(f"Error fetching author data: {e}")
        sys.exit(1)


def etl(data):
    fetched_data = []
    print('Fetching author from API')
    with ThreadPoolExecutor(max_workers=5) as executor:
        futures = []
        for chunk in get_chunk_list(data):
            futures.append(executor.submit(extract, chunk))

        for future in as_completed(futures):
            try:
                fetched_data.extend(future.result())
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    if not fetched_data:
        print("No author data fetched from the API.")
        sys.exit(1)

    author_entities = []
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for chunk in get_chunk_list(fetched_data):
            futures.append(executor.submit(pipeline, chunk))
            print(f'Processing author chunk')

        for future in as_completed(futures):
            try:
                author_entities.extend(future.result())
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    if not author_entities:
        print("No author entities processed in the pipeline.")
        sys.exit(1)

    save_to_database(author_entities)


if __name__ == "__main__":
    try:
        data = get_from_database()
        if data:
            for chunk in get_chunk_list(data, chunk_size=5000):
                print(f'Processing chunk of {len(chunk)} author IDs')
                etl(chunk)
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
