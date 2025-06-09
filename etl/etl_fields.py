from dao import insert_batch_data
from utils import fetch_data


def save_to_database(entities):
    try:
        insert_batch_data(
        table='tb_fields',
        data=entities
        )
        print(f'Saved {len(entities)} entities to the database')
    except Exception as e:
        print(f"An error occurred while saving to the database: {e}")


def pipeline(data):
    try:
        entities = [
            {
                'id': item['id'],
                'title': item['display_name'],
                'description': item.get('description', ''),
                'works_count': item.get('works_count', 0),
                'domain_id': item.get('domain').get('id', '')
            }
            for item in data
        ]

        return entities
    except Exception as e:
        print(f"An error occurred in the pipeline: {e}")
        return []
    

def etl():
    data = fetch_data(endpoint='fields').get('results', [])
    if not data:
        print("No data fetched from the API.")
        return
    entities = pipeline(data)
    if not entities:
        print("No entities processed in the pipeline.")
        return
    save_to_database(entities)


if __name__ == "__main__":
    try:
        etl()
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
