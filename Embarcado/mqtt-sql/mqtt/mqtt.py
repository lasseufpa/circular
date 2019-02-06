import paho.mqtt.client as mqtt

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("/ufpa/circular/loc/01")

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    # Timer : message[2]
    # Lat : message[3]
    # Lng : message[4]
    # Speed : message[6]
    message = str(msg.payload).split(",")

# Save the mqtt message on mysql database
def save_mqtt(timer, lat, lng, speed):
    



client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("iot.eclipse.org", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()