import mysql.connector as mysql

def mysql_select(host, user, pwd, database):
    mydb = mysql.connect(
        host=host,
        user=user,
        passwd=pwd,
        database = database,
        auth_plugin='mysql_native_password'
    )

    mycursor = mydb.cursor()
    mycursor.execute("SELECT * FROM dados")
    myresult = mycursor.fetchall()

    for x in myresult:
        print(x)

def mysql_insert(host, user, pwd, database, timer, lat, lng, speed):
    mydb = mysql.connect(
        host=host,
        user=user,
        passwd=pwd,
        database = database,
        auth_plugin='mysql_native_password'
    )

    mycursor = mydb.cursor()
    sql = "INSERT INTO dados (timer, lat, lng, speed) VALUES (%s, %s, %s, %s)"
    val = (timer, lat, lng, speed)
    mycursor.execute(sql, val)
    mydb.commit()
    print(mycursor.rowcount, "record inserted.")

mysql_select("127.0.0.1", "root", "root", "db")
