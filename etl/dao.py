import psycopg2 as psy
import sys


class DatabaseConnection:

    def __init__(self):
        self.connection = None

    def open_connection(self):
        try:
            self.connection = psy.connect(
                host='localhost',
                database='open_academic',
                user='postgres',
                password='postgres',
                port=5432
            )
            print('Database connection established.')
        except psy.Error as e:
            print(f"Error connecting to the database: {e}")
            return None

    def close_connection(self):
        if self.connection:
            self.connection.close()
            print("Database connection closed.")

    def is_open(self):
        return self.connection is not None

    def get_cursor(self):
        return self.connection.cursor() if self.is_open() else None
    
    def __enter__(self):
        self.open_connection()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close_connection()


def insert_data(cursor: psy.extensions.cursor, table, data: dict):
    keys = ', '.join(data.keys())
    values = ', '.join(['%s'] * len(data))
    query = f"INSERT INTO {table} ({keys}) VALUES ({values})"
    try:
        cursor.execute(query, tuple(data.values()))
        cursor.connection.commit()
        print(f"({table}) Data inserted successfully: {data}")
    except Exception as e:
        print(f"Error inserting data into {table}: {e}")

def insert_batch_data(cursor: psy.extensions.cursor, table: str, data: list):
    if not data:
        print("No data to insert.")
        return

    keys = ', '.join(data[0].keys())
    values_placeholder = '(' + ', '.join(['%s'] * len(data[0])) + ')'
    all_placeholders = ', '.join([values_placeholder] * len(data))
    query = f"INSERT INTO {table} ({keys}) VALUES {all_placeholders}"
    values = []
    for item in data:
        values.extend(item.values())

    try:
        cursor.execute(query, values)
        cursor.connection.commit()
        print(f"Batch data inserted successfully into {table}.")
    except Exception as e:
        print(f"Error inserting batch data into {table}: {e}")
        sys.exit(1)


def select_data(cursor: psy.extensions.cursor, select: str, table: str, where: str, misc: str = ''):
    query = f'SELECT {select} FROM {table} WHERE {where} {misc}'
    cursor.execute(query)
    columns = [desc[0] for desc in cursor.description]
    results = cursor.fetchall()
    return [dict(zip(columns, row)) for row in results]


def update_data(cursor: psy.extensions.cursor, table: str, set_clause: str, where: str):
    query = f'UPDATE {table} SET {set_clause} WHERE {where}'
    
    try:
        cursor.execute(query)
        cursor.connection.commit()
        print(f"Data updated successfully in {table}.")
    except Exception as e:
        print(f"Error updating data in {table}: {e}")
        sys.exit(1)


def select_join(cursor: psy.extensions.cursor, select: str, table1: str, table2: str, on: str, where: str = '', misc: str = '', join_type: str = 'JOIN'):
    query = f'SELECT {select} FROM {table1} {join_type} {table2} ON {on}'
    if where:
        query += f' WHERE {where}'
    if misc:
        query += f' {misc}'
    cursor.execute(query)
    columns = [desc[0] for desc in cursor.description]
    results = cursor.fetchall()
    return [dict(zip(columns, row)) for row in results]