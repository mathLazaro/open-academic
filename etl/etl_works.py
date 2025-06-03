from concurrent.futures import ThreadPoolExecutor, as_completed
import time

from dao import DatabaseConnection, insert_data
from etl.api_request import fetch_data


def pipeline(item, db_conn):
    try:
        with db_conn.cursor() as cursor:
            print(f'Processing work: {item.get("id")}')

            # oficial schema
            if item.get('id') is None or item.get('publication_date') is None:
                return

            work_data = {
                'id': item.get('id'),
                'title': item.get('title', ''),
                'is_open': item.get('open_acess', {}).get('is_oa', True),
                'referenced_works_count': int(item.get('referenced_works_count', 0)),
                'cited_by_count': int(item.get('cited_by_count', 0)),
                'fwci': float(item.get('fwci', 0.0)),
                'publish_date': item.get('publication_date'),
                'type': item.get('type')
            }

            insert_data(cursor, 'tb_works', work_data)

            # staging schema
            schema = 'staging'

            authorships = item.get('authorships', [])
            if len(authorships) > 0:
                for authorship in authorships:
                    author = authorship.get('author')
                    institutions = authorship.get('institutions', [])

                    if author is None or len(institutions) == 0:
                        continue

                    author_id = authorship.get('author').get('id')
                    instituition_id = institutions[0].get(
                        'id') if institutions else None

                    authorship_ids = {
                        'work_id': item.get('id'),
                        'author_id': author_id,
                        'instituition_id': instituition_id
                    }
                    insert_data(
                        cursor, f'{schema}.authorship_work_ids', authorship_ids)

            topics = item.get('topics', [])
            if len(topics) > 0:
                for topic in topics:
                    topic_id = topic.get('id')
                    topic_data = {
                        'work_id': item.get('id'),
                        'topic_id': topic_id
                    }
                    insert_data(cursor, f'{schema}.topic_work_ids', topic_data)

            primary_location = item.get('primary_location')
            if primary_location is not None:
                if primary_location.get('source') is not None:
                    publisher_id = primary_location.get(
                        'source').get('host_organization')
                    if publisher_id is not None:
                        publisher_data = {
                            'work_id': item.get('id'),
                            'publisher_id': publisher_id
                        }
                        insert_data(
                            cursor, f'{schema}.publisher_work_ids', publisher_data)

            print(f'Processing grants for work: {item.get("id")}')

            grants = item.get('grants', [])
            if len(grants) > 0:
                for grant in grants:
                    funder_id = grant.get('funder')
                    if funder_id is not None:
                        funder_data = {
                            'work_id': item.get('id'),
                            'funder_id': funder_id
                        }
                        insert_data(
                            cursor, f'{schema}.funder_work_ids', funder_data)
            print(f'End processing work: {item.get("id")}')
    except Exception as e:
        print(f'Error: {e}')


def etl(next, db_conn) -> str:
    endpoint = 'works'
    filter = 'institutions.country_code:BR|AR|MX|CL|CO|PE|VE|EC|GT|CU|BO|DO|HN|PY|SV|NI|CR|PA|UY|PR,type:article|book|book-chapter|dissertation|review'
    params = {
        'filter': filter,
        'per-page': 200,
        'cursor': next,
        'mailto': 'seuemail@gmail.com'
    }

    response = fetch_data(endpoint, params)

    cursor = response.get('meta', {}).get('next_cursor', None)
    data = response.get('results', [])

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = [executor.submit(pipeline, item, db_conn)
                   for item in data]

        for future in as_completed(futures):
            try:
                future.result()
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    return cursor


if __name__ == '__main__':
    db = DatabaseConnection()
    try:
        db.open_connection()
        iterations = 15  # definir numero de iterações
        wait = 20  # tempo de espera entre as requisições
        seed = 5  # definir seed para o cursor inicial

        next_cursor = '*'
        for _ in range(iterations):
            next_cursor = etl(next_cursor, db.connection)

            time.sleep(wait)
            if not next_cursor:
                break
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        if db.is_open():
            db.close_connection()
