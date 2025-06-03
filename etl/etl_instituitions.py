from concurrent.futures import ThreadPoolExecutor, as_completed
from api_request import fetch_data
from dao import DatabaseConnection, insert_batch_data, select_data

CHUNK_SIZE = 50


def get_params(ids, param='id'):
    id_param = '|'.join(ids)
    return {
        'filter': param + ':' + id_param,
        'per-page': 200,
        'mailto': 'matheus.lazaro@gmail.com'
    }


def get_chunk_list(lst, chunk_size):
    for i in range(0, len(lst), chunk_size):
        yield lst[i:i + chunk_size]


def save_data(roles, organizations, mapping):
    conn = DatabaseConnection()
    conn.open_connection()
    try:
        with conn.get_cursor() as cursor:
            if organizations:
                insert_batch_data(cursor, 'tb_organizations', organizations)
            if roles:
                insert_batch_data(cursor, 'tb_roles', roles)
            if mapping:
                insert_batch_data(cursor, 'staging.map_organization_ror_id', mapping)
    finally:
        conn.close_connection()


def pipeline(institutions_ids: list, api_endpoint = 'institutions', param='id'):
    try:
        organizations = []
        roles = []
        mappping = []

        params = get_params(institutions_ids, param)
        data = fetch_data(api_endpoint, params).get('results', [])

        if not data:
            print('No more institution avaliable')
            return

        for item in data:
            if not item.get('id') or not item.get('display_name'):
                continue

            geo = item.get('geo')

            city = None
            country = None
            if geo:
                city = geo.get('city')
                country = geo.get('country')

            ror_id = item.get('ror')
            roles_by_ror = item.get('roles', [])
            distinct_roles = {}  # role : works_count

            if roles_by_ror:
                for i in roles_by_ror:
                    if isinstance(i.get('role'), str):
                        mappping.append({'ror_id': ror_id, 'id': i.get('id')})

                        formatted_role = i.get('role').upper()
                        works_count = i.get('works_count')

                        if formatted_role not in distinct_roles.keys():
                            distinct_roles[formatted_role] = works_count
                        else:
                            distinct_roles[formatted_role] = distinct_roles[formatted_role] + works_count

            roles.extend([{
                'organization_id': ror_id,
                'role': r,
                'works_count': w
            } for r, w in distinct_roles.items()
            ])

            organizations.append({
                'id': ror_id,
                'name': item.get('display_name'),
                'city': city,
                'country': country,
                'country_code': item.get('country_code'),
                'works_count': item.get('works_count'),
                'cited_by_count': item.get('cited_by_count')
            })

        save_data(roles, organizations, mappping)
    except Exception as e:
        print(f"Error processing institution data: {e}")


def etl(conn: DatabaseConnection):
    institution_list = []
    with conn:
        with conn.get_cursor() as cursor:
            authorship_staging = select_data(
                cursor,
                'instituition_id',
                'staging.authorship_work_ids',
                'processed = FALSE'
            )

            institution_list.extend(list({
                item['instituition_id']
                for item in authorship_staging
            }))

    if not institution_list:
        print("No institutions to process.")
        return

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for chunk in get_chunk_list(institution_list, CHUNK_SIZE):
            futures.append(executor.submit(
                pipeline, chunk))
            print(f'Processing institution: {chunk}')

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
            raise Exception("Database connection is not open.")
        etl(conn)
        conn.close_connection()
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")