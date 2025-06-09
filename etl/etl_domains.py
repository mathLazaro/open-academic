
from dao import insert_batch_data
from utils import fetch_data


CHUNK_SIZE = 50


def save_to_database(entities):
    insert_batch_data(
        table='tb_domains',
        data=entities
    )
    print(f'Saved {len(entities)} entities to the database')


def etl():
    data = fetch_data(endpoint='domains').get('results', [])

    entities = [
        {
            'id': item['id'],
            'title': item['display_name'],
            'description': item.get('description', ''),
            'works_count': item.get('works_count', 0)
        }
        for item in data
    ]
    save_to_database(entities)


if __name__ == "__main__":
    try:
        etl()
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
