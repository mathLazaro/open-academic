from concurrent.futures import ThreadPoolExecutor, as_completed

from dao import DatabaseConnection, insert_batch_data, insert_data, select_join
import etl_instituitions as institutions

CHUNK_SIZE = 50


def pipeline(institutions_ids: list, api_endpoint = 'institutions', param='id'):
    try:
        organizations = []
        roles = []
        mappping = []

        params = get_params(institutions_ids, param)
        data = institutions.fetch_data(api_endpoint, params).get('results', [])

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

def save_data(data, table):
    conn = DatabaseConnection()
    with conn:
        with conn.get_cursor() as cursor:
            try:
                insert_data(cursor, table, data)
            except Exception as e:
                print(f"Error inserting authorship data: {e}")

def etl(relation_type: str, id:str, table_staging:str):
    conn = DatabaseConnection()
    staging = []
    with conn:
        with conn.get_cursor() as cursor:
            staging = select_join(
                cursor,
                f'work_id, {id} , ror_id',
                f'staging.{table_staging} as f',
                'staging.map_organization_ror_id as s',
                f'f.{id} = s.id',
                where='processed = FALSE',
                join_type='LEFT JOIN'
            )

    # processa as organizações que já estão armazenadas
    stored_staging = [{'organization_id': item.get('ror_id'), 'work_id': item.get(
        'work_id'), 'role_type': relation_type.upper()} for item in staging if item.get('ror_id')]
    
    if stored_staging:
        with ThreadPoolExecutor(max_workers=10) as executor:
            futures = []
            for item in stored_staging:
                futures.append(executor.submit(save_data, item, 'tb_work_organizations'))

            for future in as_completed(futures):
                try:
                    future.result()
                except Exception as exc:
                    print(f'Erro na thread: {exc}')
    

    not_stored_staging = list({item[id] for item in staging if not item.get('ror_id')})
    if not_stored_staging:        
        with ThreadPoolExecutor(max_workers=10) as executor:
            futures = []
            for chunk in get_chunk_list(not_stored_staging, CHUNK_SIZE):
                futures.append(executor.submit(institutions.pipeline, chunk, relation_type.lower() + 's', 'openalex_id'))

            for future in as_completed(futures):
                try:
                    future.result()
                except Exception as exc:
                    print(f'Erro na thread: {exc}')
            
            # passa pelo etl novamento para processar as organizações que foram inseridas
            etl(relation_type, id, table_staging)



if __name__ == "__main__":
    try:
        etl('publisher', 'publisher_id', 'publisher_work_ids')
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
