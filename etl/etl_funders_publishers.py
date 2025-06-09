import sys

from dao import select_join
from etl_institutions import etl


def get_from_database():
    try:
        data = select_join(
            select='distinct(organization_id)',
            table1='tb_work_organizations wo',
            table2='tb_organizations o',
            on='wo.organization_id = o.id',
            join_type='LEFT JOIN',
            where='o.id IS NULL'
            # misc='LIMIT 1000'
        )
        return [item['organization_id'] for item in data]
    except Exception as e:
        print(f"Error fetching data from database: {e}")
        sys.exit(1)


if __name__ == "__main__":
    try:
        data = get_from_database()
        etl(data)
    except Exception as e:
        print(f"An error occurred in the ETL pipeline: {e}")
