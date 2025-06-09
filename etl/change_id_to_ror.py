import sys

from dao import select_data, update_data
from utils import fetch_data, get_chunk_list


def get_from_database(organization_type):
    data = select_data(
        select='distinct(organization_id)',
        table='tb_work_organizations',
        where=f"role_type = '{organization_type}' and organization_id is not null"
    )
    return [item['organization_id'] for item in data if item['organization_id'] is not None]


def update_database(item: dict):
    try:
        update_data(
            table='tb_work_organizations',
            set_clause=f"organization_id = '{item.get('ror')}'",
            where=f"organization_id = '{item.get('id')}'"
        )

    except Exception as e:
        print(f"Error updating database: {e}")
        sys.exit(1)


def get_from_api(endpoint, ids):
    try:
        return fetch_data(
            endpoint=endpoint,
            filter='openalex_id:' + '|'.join(ids)
        ).get('results', [])
    except Exception as e:  #
        print(f"Error fetching data from API: {e}")
        sys.exit(1)


def pipeline(organizations):
    mapping = []
    for item in organizations:
        if item.get('ids').get('ror') is not None:
            mapping.append({
                'id': item.get('id'),
                'ror': item.get('ids').get('ror')
            })
    return mapping


def main(organization_type, endpoint):
    data = get_from_database(organization_type)

    fetched_data = []
    for chunk in get_chunk_list(data):
        fetched_data.extend(get_from_api(endpoint, chunk))

    if fetched_data:
        mapping = pipeline(fetched_data)

        for item in mapping:
            update_database(item)


if __name__ == '__main__':
    try:
        organization_type = 'FUNDER'  # or 'PUBLISHER'
        endpoint = 'funders'  # or 'publishers'
        main(organization_type, endpoint)
    except Exception as e:
        print(f"An error occurred: {e}")
        sys.exit(1)
