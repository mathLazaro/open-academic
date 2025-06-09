import psycopg2 as psy


def _open_connection():
    try:
        return psy.connect(
            host='localhost',
            database='open_academic',
            user='postgres',
            password='postgres',
            port=5432
        )
    except psy.Error as e:
        print(f"Error connecting to the database: {e}")
        return None


def _close_connection(conn: psy.extensions.connection):
    if conn:
        try:
            conn.close()
        except psy.Error as e:
            print(f"Error closing the database connection: {e}")


def insert_data(table: str, data: dict, conn: psy.extensions.connection = None):
    should_close = False
    if conn is None:
        conn = _open_connection()
        should_close = True

    keys = ', '.join(data.keys())
    values = ', '.join(['%s'] * len(data))
    query = f"INSERT INTO {table} ({keys}) VALUES ({values})"
    try:
        with conn.cursor() as cursor:
            cursor.execute(query, tuple(data.values()))
            cursor.connection.commit()
            print(f"({table}) Data inserted successfully: {data}")
    except Exception as e:
        print(f"Error inserting data into {table}: {e}")
    finally:
        if should_close:
            _close_connection(conn)


def insert_batch_data(table: str, data: list, conn: psy.extensions.connection = None):
    should_close = False
    if conn is None:
        conn = _open_connection()
        should_close = True

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
        with conn.cursor() as cursor:
            cursor.execute(query, values)
            cursor.connection.commit()
            print(f"Batch data inserted successfully into {table}.")
    except Exception as e:
        print(f"Error inserting batch data into {table}: {e}")
    finally:
        if should_close:
            _close_connection(conn)


def select_data(select: str, table: str, where: str = '1 = 1', misc: str = '', conn: psy.extensions.connection = None):
    should_close = False
    if conn is None:
        conn = _open_connection()
        should_close = True

    query = f'SELECT {select} FROM {table} WHERE {where} {misc}'
    try:
        with conn.cursor() as cursor:
            cursor.execute(query)
            columns = [desc[0] for desc in cursor.description]
            results = cursor.fetchall()
        return [dict(zip(columns, row)) for row in results]
    except Exception as e:
        print(f"Error executing select query: {e}")
    finally:
        if should_close:
            _close_connection(conn)


def update_data(table: str, set_clause: str, where: str, conn: psy.extensions.connection = None):
    should_close = False
    if conn is None:
        conn = _open_connection()
        should_close = True

    query = f'UPDATE {table} SET {set_clause} WHERE {where}'

    try:
        with conn.cursor() as cursor:
            cursor.execute(query)
            cursor.connection.commit()
            print(f"Data updated successfully in {table}.")
    except Exception as e:
        print(f"Error updating data in {table}: {e}")
    finally:
        if should_close:
            _close_connection(conn)


def select_join(select: str, table1: str, table2: str, on: str, where: str = '', misc: str = '', join_type: str = 'JOIN', conn: psy.extensions.connection = None):
    should_close = False
    if conn is None:
        conn = _open_connection()
        should_close = True

    query = f'SELECT {select} FROM {table1} {join_type} {table2} ON {on}'
    if where:
        query += f' WHERE {where}'
    if misc:
        query += f' {misc}'
    try:
        with conn.cursor() as cursor:
            cursor.execute(query)
            columns = [desc[0] for desc in cursor.description]
            results = cursor.fetchall()
        return [dict(zip(columns, row)) for row in results]
    except Exception as e:
        print(f"Error executing join query: {e}")
    finally:
        if should_close:
            _close_connection(conn)
