function getVar(message, ident) {           //Função que trata a string e retorna o valor de cada variavel dependendo do seu identificador.
    var data = message.split(',');
    return data[ident];
}
//  <Qualidade do sinal>,<Temperatura>,<UTC date & Time>,<Latitude>,<Longitude>,<Velocidade>,<Curso>
var client = mqtt.connect('ws://iot.eclipse.org:80/ws')
client.on('connect', function () {
    console.log('client connected')
    client.subscribe('/ufpa/circular/loc/+');
    client.on('message', function (topic, payload) {
        var circular = topic.split('/')
        var message = payload.toString()
        busText= "Circular" + circular[4];        // Nome do circular + circular[4] que é o numero do circular
        busSignalQuality = getVar(message, 0)     // Qualidade de Sinal 
        busTemperature = getVar(message, 1)       // Temperatura
        busTimer = getVar(message, 2)             // Hora e Data
        busLat = getVar(message, 3)               // Latitude
        busLng = getVar(message, 4)               // Longitude
        busSpeed = getVar(message, 5)             // Velocidade
        busDirection = getVar(message, 6)         // Direção (Curso)

        set_bus(busLat, busLng, map);
    });
});
