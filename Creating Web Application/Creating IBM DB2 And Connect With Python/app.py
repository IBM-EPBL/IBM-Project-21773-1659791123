from flask import *
from turtle import st
from flask import Flask, render_template, request, redirect, url_for, session
from markupsafe import escape
import ibm_db

conn = ibm_db.connect("DATABASE=bludb; HOSTNAME=s3ad1s5ad5-01b3832d.f6ads4f6ds4f56d.databases.appdomain.cloud; PORT=30119; SECURITY=SSL; SSLServerCertificate=DigiCertGlobalRootCA.crt; UID=qqq88888; PWD=DFE64654SD", '', '')
app = Flask(__name__)
app.secret_key = "zalertconf"


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


# main
if __name__ == "__main__":
    app.debug = True
    app.run(host='0.0.0.0', port=5000)

