from concurrent.futures import ThreadPoolExecutor, as_completed
import sys

from dao import insert_batch_data
from utils import fetch_data


def extract(next):
    try:
        endpoint = 'works'
        filter = 'institutions.country_code:AR|BO|BR|CL|CO|EC|GY|PY|PE|SR|UY|VE,type:article|book|dissertation|review'
        response = fetch_data(
            endpoint,
            filter=filter,
            cursor=next
        )

        cursor = response.get('meta', {}).get('next_cursor', None)
        data = response.get('results', [])

        return data, cursor
    except Exception as e:
        print(f'Error on extract: {e}')
        return [], None


def pipeline(item):
    print(f'Processing item with ID: {item.get("id")}')
    work_entity = None
    authorship_entities = []
    topic_entities = []
    publisher_entities = []
    funder_entities = []
    try:
        if item.get('id') is None or item.get('publication_date') is None or item.get('title') is None:
            print(f'Skipping item due to missing required fields')
            return

        work_entity = {
            'id': item.get('id'),
            'title': item.get('title', ' - '),
            'is_open': item.get('open_acess', {}).get('is_oa', True),
            'referenced_works_count': int(item.get('referenced_works_count', 0)),
            'cited_by_count': int(item.get('cited_by_count', 0)),
            'fwci': float(item.get('fwci', 0.0)),
            'publish_date': item.get('publication_date'),
            'type': item.get('type')
        }

        authorships = item.get('authorships', [])
        if len(authorships) > 0:
            for authorship in authorships:
                author = authorship.get('author')
                institutions = authorship.get('institutions', [])

                if not author or len(institutions) == 0:
                    continue

                obj = {
                    'work_id': item.get('id'),
                    'author_id': author.get('id'),
                    'institution_id': institutions[0].get('ror') if institutions else None
                }
                if obj not in authorship_entities:
                    authorship_entities.append(obj)

        topics = item.get('topics', [])
        if len(topics) > 0:
            for topic in topics:
                topic_id = topic.get('id')
                obj = {
                    'work_id': item.get('id'),
                    'topic_id': topic_id,
                    'score': topic.get('score', 0.0)
                }
                if obj not in topic_entities:
                    topic_entities.append(obj)

        primary_location = item.get('primary_location')
        if primary_location is not None:
            if primary_location.get('source') is not None:
                publisher_id = primary_location.get(
                    'source').get('host_organization')
                if publisher_id is not None:
                    obj = {
                        'work_id': item.get('id'),
                        'organization_id': publisher_id,
                        'role_type': 'PUBLISHER'
                    }
                    if obj not in publisher_entities:
                        publisher_entities.append(obj)

        grants = item.get('grants', [])
        if len(grants) > 0:
            for grant in grants:
                funder_id = grant.get('funder')
                if funder_id is not None:
                    obj = {
                        'work_id': item.get('id'),
                        'organization_id': funder_id,
                        'role_type': 'FUNDER'
                    }
                    if obj not in funder_entities:
                        funder_entities.append(obj)
        return work_entity, authorship_entities, topic_entities, publisher_entities, funder_entities
    except Exception as e:
        print(f'Error on pipeline: {e}')


def save_to_database(work_entities, authorship_entities, topic_entities, publisher_entities, funder_entities):
    try:
        if work_entities:
            insert_batch_data('tb_works', work_entities)
        if authorship_entities:
            insert_batch_data('tb_authorships', authorship_entities)
        if topic_entities:
            insert_batch_data('tb_work_topics', topic_entities)
        if publisher_entities:
            insert_batch_data('tb_work_organizations', publisher_entities)
        if funder_entities:
            insert_batch_data('tb_work_organizations', funder_entities)
        print('Data saved successfully to the database')
    except Exception as e:
        print(f'Error on saving: {e}')


def etl(next) -> str:
    print('Extracting data from API')
    data, cursor = extract(next)

    if not data or cursor is None:
        sys.exit('No data')

    work_data = []
    authorship_data = []
    topic_data = []
    publisher_data = []
    funder_data = []

    print('Processing data')
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(pipeline, item)
                   for item in data]

        for future in as_completed(futures):
            try:
                work_entity, authorship_entities, topic_entities, publisher_entities, funder_entities = future.result()
                if work_entity:
                    work_data.append(work_entity)
                if authorship_entities:
                    authorship_data.extend(authorship_entities)
                if topic_entities:
                    topic_data.extend(topic_entities)
                if publisher_entities:
                    publisher_data.extend(publisher_entities)
                if funder_entities:
                    funder_data.extend(funder_entities)
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    print('Saving data in database')
    save_to_database(work_data, authorship_data, topic_data,
                     publisher_data, funder_data)

    return cursor


def main():
    iterations = 30  # definir numero de iterações

    next_cursor = '*'
    for _ in range(iterations):
        next_cursor = etl(next_cursor)


if __name__ == '__main__':
    try:
        main()
    except Exception as e:
        print(f"An error occurred: {e}")
