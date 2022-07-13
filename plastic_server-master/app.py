from flask import Flask, render_template, request, jsonify, g
import flask
import db_work
import json
import psycopg2

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
def get_conn():
    
    db = getattr(g,'_database', None)
    if db is None:
        db = g._database = db_work.init_db()
    return db



@app.route('/getPlaces', methods=['POST'])
def get_places():
    conn = get_conn()
    return jsonify(db_work.get_places(conn, flask.request.data))

@app.route('/getPlacesByCode', methods=['POST'])
def get_places_by_code():
    conn = get_conn()
    return jsonify(db_work.get_places_by_code(conn, flask.request.data))

@app.route('/getCodes', methods = ['POST'])
def get_codes():
    conn = get_conn()
    return jsonify(db_work.get_codes(conn, flask.request.data))

@app.route('/getPlacesByCodeList', methods=['POST'])
def get_places_by_codelist():
    conn = get_conn()
    return jsonify(db_work.get_places_by_codelist(conn, flask.request.data))

@app.route('/getPlacesByPlaceList', methods=['POST'])
def get_places_by_placelist():
    conn = get_conn()
    return jsonify(db_work.get_places_by_placelist(conn, flask.request.data))

@app.route('/test', methods=['GET'])
def test():
    conn = get_conn()
    res = db_work.get_place_test
    print(res)

if __name__ == "__main__":

    with app.app_context():
        g._database = db_work.init_db()
    app.run(debug=True)