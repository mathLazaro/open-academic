from concurrent.futures import ThreadPoolExecutor, as_completed
import sys

from utils import fetch_data, get_chunk_list
from dao import insert_data, select_join


def get_from_database():
    try:
        data = select_join(
            select='distinct(institution_id)',
            table1='tb_authorships a',
            table2='tb_organizations o',
            on='a.institution_id = o.id',
            join_type='LEFT JOIN',
            where='o.id IS NULL'
            # misc='LIMIT 1000'
        )
        return [item['institution_id'] for item in data]
    except Exception as e:
        print(f"Error fetching data from database: {e}")
        sys.exit(1)


def save_data(data, table):
    try:
        insert_data(table=table, data=data)
    except Exception as e:
        print(f"Error saving data: {e}")
        sys.exit(1)


def pipeline(item):
    try:
        organization = None
        roles = []
        organization_domains = []

        if not item.get('id') or not item.get('display_name'):
            return organization, roles, organization_domains

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

        organization = {
            'id': ror_id,
            'name': item.get('display_name'),
            'city': city,
            'country': country,
            'country_code': item.get('country_code'),
            'works_count': item.get('works_count'),
            'cited_by_count': item.get('cited_by_count')
        }

        domains = []
        for topic in item.get('topics', []):
            domain_id = topic.get('domain').get('id')
            if domain_id:
                obj = {
                    'organization_id': ror_id,
                    'domain_id': domain_id
                }
                if obj not in domains:
                    domains.append(obj)
        organization_domains.extend(domains)

        return organization, roles, organization_domains
    except Exception as e:
        print(f"Error processing institution data: {e}")


def extract(ids: str):
    return fetch_data(
        endpoint='institutions',
        filter='ror:' + '|'.join(ids)
    ).get('results', [])


def etl(data):
    fetched_data = []
    print('Fetching data from API')
    with ThreadPoolExecutor(max_workers=4) as executor:
        futures = []
        for chunk in get_chunk_list(data):
            futures.append(executor.submit(extract, chunk))

        for future in as_completed(futures):
            try:
                fetched_data.extend(future.result())
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    if not fetched_data:
        print('No data fetched from API.')
        return

    print('Processing fetched data')
    organization_entities = []
    role_entities = []
    organization_domain_entities = []
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for chunk in fetched_data:
            futures.append(executor.submit(pipeline, chunk))

        for future in as_completed(futures):
            try:
                organization, roles, organization_domain = future.result()
                if organization:
                    organization_entities.append(organization)
                if roles:
                    role_entities.extend(roles)
                if organization_domain:
                    organization_domain_entities.extend(organization_domain)
            except Exception as exc:
                print(f'Erro na thread: {exc}')

    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for organization in organization_entities:
            if organization:
                futures.append(executor.submit(
                    save_data,
                    organization,
                    'tb_organizations'
                ))
        for role in role_entities:
            if role:
                futures.append(executor.submit(
                    save_data,
                    role,
                    'tb_roles'
                ))
        for organization_domain in organization_domain_entities:
            if organization_domain:
                futures.append(executor.submit(
                    save_data,
                    organization_domain,
                    'tb_organization_domains'
                ))

        for future in as_completed(futures):
            try:
                future.result()
            except Exception as exc:
                print(f'Erro na thread ao salvar dados: {exc}')


if __name__ == "__main__":
    try:
        data = get_from_database()
        etl(data)
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
