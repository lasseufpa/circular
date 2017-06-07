		function getVar (message, ident){ //Função que trata a string e retorna o valor de cada variavel dependendo do seu identificador.  
			var n = message.length 
			var stg = message.substr(10, n);//Retira o '+CGNSINF: ' da string final 	
			var res = stg.split(',');
			return res[ident]; 
	  }    

	   var client = mqtt.connect('ws://test.mosca.io:80')
        client.on('connect', function(){
            console.log('client connected')
            client.subscribe('CircularUFPA_Loc');
            client.on('message', function(topic, payload) {
				var mess = payload.toString()
				CircularText = "Circular01" //Nome do circular - Cada circular vai ter seu topico
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
