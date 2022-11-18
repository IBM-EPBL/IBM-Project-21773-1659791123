from flask import *
from turtle import st
from flask import Flask, render_template, request, redirect, url_for, session
from markupsafe import escape
import bcrypt
import sqlite3 as sql
from flask_mysqldb import MySQL
import ibm_db
import sendgrid
import os
from sendgrid.helpers.mail import *
import json
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail

conn = ibm_db.connect("DATABASE=bluedb; HOSTNAME=askjjdfgajkwhfkjabhwfkn-askfbajksbfhkajwbfk.databases.appdomain.cloud; PORT=21458; SECURITY=SSL; SSLServerCertificate=DigiCertGlobalRootCA.crt; UID=awr8998; PWD=iaoshdfkashaa", '', '')
app = Flask(__name__)
app.secret_key = "zalertconf"
mysql = MySQL(app)

# Api's
@app.route('/', methods=['POST', 'GET'])
def login():
    msg = ''
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']
        sql = "SELECT * FROM admincreds WHERE email =?"
        stmt = ibm_db.prepare(conn, sql)
        ibm_db.bind_param(stmt, 1, email)
        ibm_db.execute(stmt)
        account = ibm_db.fetch_both(stmt)
        accounts = account
        if (account):
            if (password == accounts['PASSWORD']):
                msg = 'Logged in successfully !'
                session['id'] = 1
                session['email'] = email
                session['password'] = password
                return render_template('home.html', msg=msg)
            else:
                msg = 'Wrong Credentials'
                return redirect(url_for("login"))
    return render_template('index.html', msg=msg)


@app.route("/home", methods=["POST", "GET"])
def home():
    if (session['id'] == None):
        return redirect(url_for('login'))

    if (request.method == "POST"):
        # get data
        lat = request.form["lat"]
        lon = request.form["lon"]
        vis = 0
        if (lat == "" or lon == ""):
            return render_template('home.html', email=session['email'], id=session['id'], success=0)

        # create a location cursor
        location_cursor = mysql.connection.cursor()

        # Execute the query
        location_cursor.execute(
            'INSERT INTO location(location_lat,location_long,location_visited) VALUES(%s,%s,%s)', (
                lat, lon, vis
            )
        )
        mysql.connection.commit()
        location_cursor.close()
        return render_template('home.html', email=session['email'], id=session['id'], success=True)
    return render_template('home.html', email=session['email'], id=session['id'])


@app.route("/logout")
def logout():
    # remove the username from the session if it is there
    session['id'] = None
    session['name'] = None
    session['email'] = None
    return redirect(url_for('login'))


@app.route("/data")
def data():
    if (session['id'] == None):
        return redirect(url_for('login'))

    location_cursor = mysql.connection.cursor()

    # check whether user already exists
    user_result = location_cursor.execute(
        "SELECT * FROM location"
    )
    if (user_result == 0):
        return render_template("data.html", responses=0)
    else:
        res = location_cursor.fetchall()
        print(res)
        return render_template("data.html", responses=res)

# ------------Redirect to Home Page-------------


@app.route("/gotohome")
def gotohome():
    return redirect(url_for('home'))


@app.route("/android_sign_up", methods=['GET', 'POST'])
def android_sign_up():
    if (request.method == "POST"):

        # get the data from the form
        name = request.json['name']
        email = request.json['email']
        password = request.json['password']

        # initialize the cursor
        signup_cursor = mysql.connection.cursor()

        # check whether user already exists
        user_result = signup_cursor.execute(
            "SELECT * FROM users WHERE email=%s", [email]
        )
        if (user_result < 0):
            signup_cursor.close()
            return {'status': 'failuree'}
        else:
            # execute the query
            signup_cursor.execute(
                'INSERT INTO users(user_id,name,email,password) VALUES(%s,%s,%s,%s)', (
                    "2", name, email, password,
                )
            )

            mysql.connection.commit()
            id_result = signup_cursor.execute(
                'SELECT user_id FROM users WHERE email = %s', [email]
            )
            if (id_result > 0):
                id = signup_cursor.fetchone()
                return {"id": 1}    
            signup_cursor.close()

    return {"status": "failure"}



@app.route("/post_user_location_data", methods=['GET', 'POST'])
def post_user_location_data():
    if (request.method == "POST"):

        # get the data from the form
        id = request.json['id']
        lat = request.json['lat']
        lon = request.json['long']
        ts = request.json['timestamp']

        # initialize the cursor
        user_location_cursor = mysql.connection.cursor()

        # execute the query
        user_location_cursor.execute(
            'INSERT INTO usersloc(latitude,longitude,user_id,timestamp) VALUES(%s,%s,%s,%s)', (
                lat, lon, id, ts
            )
        )

        mysql.connection.commit()

        return {"response": "success"}


@app.route("/location_data")
def location_data():
    location_cursor = mysql.connection.cursor()

    # check whether user already exists
    user_result = location_cursor.execute(
        "SELECT * FROM location"
    )
    if (user_result != 0):
        res = location_cursor.fetchall()
        print(res)
        row_headers = [x[0] for x in location_cursor.description]
        json_data = []
        for result in res:
            json_data.append(dict(zip(row_headers, result)))
        return json.dumps(json_data)
    else:
        return {"response": "failure"}




# main
if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=5000)

