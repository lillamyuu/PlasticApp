import json
import psycopg2
def init_db():
    conn = psycopg2.connect(
        host = "ec2-52-18-116-67.eu-west-1.compute.amazonaws.com",
        database = "dcqkvkaahfbjo5",
        user = "zbkgfrzggzyklp",
        password="261b7abc6ac83fdeaeb90da504e66e157dfe038f0fc3a63d996371a3feacca1a")
   # cur = conn.cursor()
    print("aaa")
    
   # cur.execute('CREATE TABLE IF NOT EXISTS place ('
   #             'place_id SERIAL PRIMARY KEY,'
   #             'name varchar(50),'
   #             'latitude float,'
   #             'longitude float,'
   #             'timetable varchar(150));'
   #                              )
   # cur.execute("INSERT INTO place (name, latitude, longitude, timetable) VALUES ('test1', 40.516939, 64.539393, '11-12');")
   # cur.execute('CREATE TABLE IF NOT EXISTS code ('
   #             'code_id SERIAL PRIMARY KEY,'
   #             'name varchar(50),'
   #             'info varchar(150));'
   #                              )
   # cur.execute('CREATE TABLE IF NOT EXISTS place_code('
   #                 'place_id INTEGER, '
   #                 'code_id INTEGER, '
   #                 'FOREIGN KEY(place_id) '
   #                 'REFERENCES place(place_id), '
   #                 'FOREIGN KEY(place_id) '
   #                 'REFERENCES place(place_id), '
   #                 'PRIMARY KEY(place_id, code_id));'
   #                 )
   # conn.commit()
   # cur.close()
    return conn
def get_places(conn, data):
    cur = conn.cursor()
    req_data = json.JSONDecoder().decode(data.decode('utf-8'))
    get_data_sql = 'SELECT place_id, name, latitude, longitude, timetable FROM place WHERE place_id='+str(req_data["place_id"])
    cur.execute(get_data_sql)
    res = {"items":[{
        "place_id": data_row[0],
        "name": data_row[1],
        "latitude": data_row[2],
        "longitude": data_row[3],
        "timetable": data_row[4]
        
    } for data_row in cur.fetchall()]}
    cur.close()
    return res    
def get_place_test(conn):
    cur = conn.cursor()
    req_data = [1, 2]
    #req_data = json.JSONDecoder().decode(data.decode('utf-8'))
    get_data_sql = 'SELECT place_id, name, latitude, longitude, timetable FROM place WHERE place_id IN ('+str(req_data)[1:-1]+')'
    cur.execute(get_data_sql)
    res = {"items":[{
        "place_id": data_row[0],
        "name": data_row[1],
        "latitude": data_row[2],
        "longitude": data_row[3],
        "timetable": data_row[4]
        
    } for data_row in cur.fetchall()]}
    cur.close()
    print(res)

def get_places_by_code(conn, data):
    cur = conn.cursor()
    req_data = json.JSONDecoder().decode(data.decode('utf-8'))
    get_data_sql = 'SELECT place_id FROM place_code WHERE code_id='+str(req_data["code_id"])
    cur.execute(get_data_sql)
    res = {"items":[{
        "place_id": data_row[0],
        "name": "",
        "latitude": 0,
        "longitude": 0,
        "timetable": ""
        
    } for data_row in cur.fetchall()]}
    cur.close()
    return res

def get_codes(conn, data):
    cur = conn.cursor()
    req_data = json.JSONDecoder().decode(data.decode('utf-8'))
    get_data_sql = 'SELECT code_id, name, info FROM code WHERE code_id='+str(req_data["code_id"])
    cur.execute(get_data_sql)
    res = {"items":[{
        "code_id": data_row[0],
        "name": data_row[1],
        "info": data_row[2]
    } for data_row in cur.fetchall()]}
    cur.close()
    return res
def get_places_by_codelist(conn, data):
    req_data = json.JSONDecoder().decode(data.decode('utf-8'))["items"]
    cur = conn.cursor()
    placeset = set()
    for row in req_data:
        cur.execute('SELECT place_id FROM place_code WHERE code_id=')+str(req_data["code_id"])
        placeset.intersection_update({data_row[0] for data_row in cur.fetchall()}) 
    cur.execute('SELECT place_id, name, latitude, longitude, timetable FROM place WHERE place_id IN ('+str(placeset)[1:-1]+')')
    res = {"items":[{
        "place_id": data_row[0],
        "name": data_row[1],
        "latitude": data_row[2],
        "longitude": data_row[3],
        "timetable": data_row[4]
        
    } for data_row in cur.fetchall()]}
    cur.close()
    return res

def get_places_by_placelist(conn, data):
    req_data = json.JSONDecoder().decode(data.decode('utf-8'))["items"]
    cur = conn.cursor()
    get_data_sql = 'SELECT place_id, name, latitude, longitude, timetable FROM place WHERE place_id IN ('+str(req_data)[1:-1]+')'
    cur.execute(get_data_sql)
    res = {"items":[{
        "place_id": data_row[0],
        "name": data_row[1],
        "latitude": data_row[2],
        "longitude": data_row[3],
        "timetable": data_row[4]
        
    } for data_row in cur.fetchall()]}
    cur.close()
    print(res)
    return res


