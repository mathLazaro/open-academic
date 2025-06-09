from concurrent.futures import ThreadPoolExecutor, as_completed
import sys

from dao import insert_batch_data, select_data
from utils import fetch_data, get_chunk_list


def extract(data) -> list:
    try:
        fetched_data = fetch_data(
            'topics', f'id:{"|".join(data)}').get('results', [])
        return fetched_data
    except Exception as e:
        print(f'Error in extract function: {e}')
        sys.exit(1)


def get_from_database():
    return [item['topic_id'] for item in select_data(
        select='distinct(topic_id)',
        table='tb_work_topics'
    )]


def save_to_database(topics, keywords):
    try:
        insert_batch_data(
            table='tb_topics',
            data=topics
        )
        print(f'Saved {len(topics)} entities to the database tb_topics')
        insert_batch_data(
            table='tb_topic_keywords',
            data=keywords
        )
        print(
            f'Saved {len(keywords)} keyword entities to the database tb_topic_keywords')
    except Exception as e:
        print(f'Error saving data to database: {e}')
        sys.exit(1)


def pipeline(item):
    topic_entity = None
    keyword_entities = []

    try:
        topic_entity = {
            'id': item['id'],
            'title': item['display_name'],
            'description': item.get('description', ''),
            'works_count': item.get('works_count', 0),
            'field_id': item.get('field').get('id', ''),
            'keywords': item.get('keywords', [])
        }

        if not topic_entity:
            raise Exception("Topic entity is empty")

        keywords = topic_entity.pop('keywords', [])
        for keyword in keywords:
            if keyword not in keyword_entities:
                keyword_entities.append({
                    'topic_id': topic_entity['id'],
                    'word': keyword
                })

        return topic_entity, keyword_entities
    except Exception as e:
        print(f'Error in pipeline function: {e}')
        sys.exit(1)


def etl(data):
    fetched_data = []
    topic_entities = []
    keyword_entities = []

    print('Extracting topics from API')
    with ThreadPoolExecutor(max_workers=5) as executor:
        futures = []
        for chunk in get_chunk_list(data):
            futures.append(executor.submit(extract, chunk))
            print(f'Fetching topic chunk')

        for future in as_completed(futures):
            try:
                fetched_data.extend(future.result())
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    if not fetched_data:
        print('No data fetched from API')
        return

    print('Generating entities')
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for item in fetched_data:
            futures.append(executor.submit(pipeline, item))
            print(f'Processing topic chunk')

        for future in as_completed(futures):
            try:
                topic, keywords = future.result()
                if topic and topic not in topic_entities:
                    topic_entities.append(topic)
                if keywords:
                    keyword_entities.extend(keywords)
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    if not topic_entities:
        print('No topic entities generated')
        return

    print('Saving data to database')
    save_to_database(topic_entities, keyword_entities)


if __name__ == "__main__":
    try:
        data = get_from_database()
        etl(data)
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
