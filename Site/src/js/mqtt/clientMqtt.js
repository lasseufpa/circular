//  <Qualidade do sinal>,<Temperatura>,<UTC date & Time>,<Latitude>,<Longitude>,<Velocidade>,<Curso>

var client = mqtt.connect('wss://iot.eclipse.org:443/ws')
client.on('connect', function () {
    console.log('client connected')
    client.subscribe('/ufpa/circular/loc/+');
    client.on('message', function (topic, payload) {
        var circular = topic.split('/')
        var message  = (payload.toString()).split(',')

        busText          = "Circular" + circular[4];    // Nome do circular + circular[4] que é o numero do circular
        busSignalQuality = message[0]                   // Qualidade de Sinal
        busTemperature   = message[1]                   // Temperatura
        busTimer         = message[2]                   // Hora e Data
        busLat           = message[3]                   // Latitude
        busLng           = message[4]                   // Longitude
        busSpeed         = message[5]                   // Velocidade
        busDirection     = message[6]                   // Direção (Curso)

        set_bus(busText, busSignalQuality, busTemperature, busTimer, busLat, busLng, busSpeed, busDirection, map);
    });
});
