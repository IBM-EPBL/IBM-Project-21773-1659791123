#------------ IMPORT STATEMENTS ------------

from flask import *
from turtle import st
from flask import Flask, render_template, request, redirect, url_for, session
from markupsafe import escape
import sqlite3 as sql
from flask_mysqldb import MySQL
import ibm_db
import sendgrid
import os
from sendgrid.helpers.mail import *
import json
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail

#------------ IBM DATABASE (DB2) CONNECTION ------------
conn = ibm_db.connect("DATABASE=bludb; HOSTNAME=6dszf465df-659s-9856-9865s-321asd65465sd.63as4d6as4d4as6d48rcx.databases.appdomain.cloud; PORT=31125; SECURITY=SSL; SSLServerCertificate=DigiCertGlobalRootCA.crt; UID=qfs89561; PWD=QFS5s896s63", '', '')

#------------ SQL CONNECTION ------------
app = Flask(__name__)
app.secret_key = "zalertconf"
app.config['MYSQL_HOST'] = 'remotemysql.com'
app.config['MYSQL_USER'] = 'eSe8s6a4f'
app.config['MYSQL_PASSWORD'] = 'Sdfs58w2'
app.config['MYSQL_DB'] = 'eSe8s6a4f'
mysql = MySQL(app)


#------------ SENDGRID MAIL FUNCTION ------------
def send_mail(email):
    print(email)
    message = Mail(from_email='vishnuv.cse19@veltechmultitech.org',
                   to_emails=email,
                   subject='Warning',
                   html_content='<strong>You have entered a contaminated zone. Please approach safety as soon as possible</strong><img src= "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmjYta_0j7SzJFylnDT0RWiELr1kI921lwtBw_L-gkubYq8mqJy4nS7WPpyt_WWg_KL5A&usqp=CAU" alt="warning">')

    try:
        sg = sendgrid.SendGridAPIClient(
            api_key='SG.EiX_as6dffh56dsf8sg41.dsz6fjygjg7hjkgj78d9ftye6786dfh464fgx6fg45')
        response = sg.send(message)
        print(response.status_code)
        print(response.body)
        print(response.headers)
    except Exception as e:
        print(e)


#------------ LOGIN PAGE FUNCTION ------------
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


#------------ REGISTER PAGE FUNCTION ------------
@app.route('/reg', methods=['POST', 'GET'])
def reg():
    msg = ''
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        sql = "SELECT * FROM admincreds WHERE email =?"
        stmt = ibm_db.prepare(conn, sql)
        ibm_db.bind_param(stmt, 1, email)
        ibm_db.execute(stmt)
        account = ibm_db.fetch_assoc(stmt)
        if account:
            msg = 'Account already exists !'
        elif not password or not email:
            msg = 'Please fill the Details !'

        else:
            insert_sql = "INSERT INTO admincreds VALUES (?,?)"
            prep_stmt = ibm_db.prepare(conn, insert_sql)
            ibm_db.bind_param(prep_stmt, 1, email)
            ibm_db.bind_param(prep_stmt, 2, password)
            ibm_db.execute(prep_stmt)
            msg = 'Account created successfully '
            return render_template('index.html', msg=msg)
    return render_template('register.html', msg=msg)



#------------ HOME PAGE FUNCTION ------------
@app.route("/home", methods=["POST", "GET"])
def home():
    if (session['id'] == None):
        return redirect(url_for('login'))

    if (request.method == "POST"):
        
        lat = request.form["lat"]
        lon = request.form["lon"]

        vis = 0
        if (lat == "" or lon == ""):
            return render_template('home.html', email=session['email'], id=session['id'], success=0)
        location_cursor = mysql.connection.cursor()
        location_cursor.execute(
            'INSERT INTO location(location_lat,location_long,location_visited) VALUES(%s,%s,%s)', (
                lat, lon, vis
            )
        )
        mysql.connection.commit()
        location_cursor.close()
        return render_template('home.html', email=session['email'], id=session['id'], success=True)
    return render_template('home.html', email=session['email'], id=session['id'])


#------------ LOG OUT PAGE FUNCTION ------------
@app.route("/logout")
def logout():
    session['id'] = None
    session['name'] = None
    session['email'] = None
    return redirect(url_for('login'))


#------------ CONTAINMENT ZONES LIST PAGE FUNCTION ------------
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

# ------------Redirect to Register Page-------------
@app.route("/gotoreg")
def gotoreg():
    return redirect(url_for('reg'))




# ------------------------ ANDROID RELATED FUNCTIONS -------------------------

# ------------ANDROID SIGN UP PAGE -------------
@app.route("/android_sign_up", methods=['GET', 'POST'])
def android_sign_up():
    if (request.method == "POST"):

        name = request.json['name']
        email = request.json['email']
        password = request.json['password']


        signup_cursor = mysql.connection.cursor()
        user_result = signup_cursor.execute(
            "SELECT * FROM users WHERE email=%s", [email]
        )
        if (user_result < 0):
            signup_cursor.close()
            return {'status': 'failuree'}
        else:
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


@app.route("/get_all_users")
def getusers():
    signup_cursor = mysql.connection.cursor()

    # check whether user already exists
    user_result = signup_cursor.execute(
        "SELECT * FROM users"
    )
    if (user_result > 0):
        rv = signup_cursor.fetchall()
        row_headers = [x[0] for x in signup_cursor.description]
        json_data = []
        for result in rv:
            json_data.append(dict(zip(row_headers, result)))
        return json.dumps(json_data)



# ------------SENDING CO-ORDINATES OF THE USER'S LOCATION ON THE ANDROID APP TO THE DATABASE-------------
@app.route("/post_user_location_data", methods=['GET', 'POST'])
def post_user_location_data():
    if (request.method == "POST"):

        id = request.json['id']
        lat = request.json['lat']
        lon = request.json['long']
        ts = request.json['timestamp']

        user_location_cursor = mysql.connection.cursor()

        user_location_cursor.execute(
            'INSERT INTO usersloc(latitude,longitude,user_id,timestamp) VALUES(%s,%s,%s,%s)', (
                lat, lon, id, ts
            )
        )

        mysql.connection.commit()

        return {"response": "success"}


# ------------ SENDS CONTAINMENT ZONES LOCATION REQUESTED BY ANDROID APP API-------------
@app.route("/location_data")
def location_data():
    location_cursor = mysql.connection.cursor()

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


# ------------ TRIGGERS THE SEND EMAIL FUCNTION IF THE USER'S LOCATION MATCHES WITH LOCATION OF ANY CONTAINMENT ZONE-------------
@app.route("/send_trigger", methods=["POST"])
def send_trigger():
    if (request.method == "POST"):

        email = request.json['email']
        location_id = request.json['id']
        location_cursor = mysql.connection.cursor()

        user_result = location_cursor.execute(
            "SELECT location_visited FROM location WHERE location_id=%s", [
                location_id]
        )
        if (user_result == 0):
            return {"response": "failure"}
        else:
            res = location_cursor.fetchone()
            print(res[0])
            visited = res[0]
            visited = visited+1
            location_cursor.execute(
                "UPDATE location SET location_visited = %s WHERE location_id=%s",
                (visited, location_id)
            )
            mysql.connection.commit()

        send_mail(email)
        return {"response": "success"}


# ------------ MAIN -------------
if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=5000)
