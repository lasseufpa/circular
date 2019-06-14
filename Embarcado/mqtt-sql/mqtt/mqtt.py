import paho.mqtt.client as mqtt
import mysql.connector as mysql

message = " "
message_ant = " "

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("/ufpa/circular/loc/01")

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    # Timer : message[2], Lat : message[3], Lng : message[4], Speed : message[6]
    message = str(msg.payload).split(",")
    print(message)
    with open("Output.txt", "a") as text_file:
        print("{}".format(message), file=text_file)

# Save the mqtt message on mysql database
def save_mqtt(timer, lat, lng, speed):
    mysql_insert("127.0.0.1", "root", "root", "db", timer, lat, lng, speed)

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

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message
client.connect("iot.eclipse.org", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()
