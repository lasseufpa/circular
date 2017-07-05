		function getVar (message, ident){ //Função que trata a string e retorna o valor de cada variavel dependendo do seu identificador.
			var stg = message.split(': '); //Separa o '+CGNSINF+CGNSINF: ' da string final
			var res = stg[1].split(',');
			return res[ident];
	  }

	   var client = mqtt.connect('ws://test.mosca.io:80')
        client.on('connect', function(){
            console.log('client connected')
            client.subscribe('ufpa/circular/loc/+');
            client.on('message', function(topic, payload) {
							var circular = topic.split('/')
							var mess = payload.toString()
							CircularText = "Circular"+circular[3]; //Nome do circular + circular[3] que é o numero do circular
							CircularTimer = getVar(mess, 2) // Hora e Data
			  			CircularLat = getVar(mess, 3) //Latitude
							CircularLng = getVar(mess, 4) //Longitude
							CircularSpeed = getVar(mess, 6) //Velocidade
							console.log(CircularText)
							console.log(CircularLat)
							console.log(CircularLng)
							console.log(CircularTimer)
							console.log(CircularSpeed)
							SetBus();
						});
        });
