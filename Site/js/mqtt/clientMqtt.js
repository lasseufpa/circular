		function getVar (message, ident){ //Função que fará a separação da string recebida dependendo da TAG [#+identificador (Ex.: #N - Nome)] 
			var n = message.length 
			var index
			var res = "";
			
			for(i=0;i<n;i++){ //Percorre a string até achar a TAG (#+identificador)
				if (message.charAt(i)=='#'&&message.charAt(i+1)==ident){
					index = i+2
					
					for(j=index;j<n;j++){
						if (message.charAt(j) == '*'){
							j=i=n
							break
						}
						res = res+message.charAt(j);
					}
				}
			}
			res=res.replace(",",".");
			return res;
	   }

	
		
	   var client = mqtt.connect('ws://test.mosca.io:80')
        client.on('connect', function(){
            console.log('client connected')
            client.subscribe('CircularUFPA_Loc');
            client.on('message', function(topic, payload) {
				var mess = payload.toString()
				CircularText = getVar(mess, 'N') //Nome do circular
				CircularLng = getVar(mess, 'Y') //Longitude 
				CircularLat = getVar(mess, 'X') //Latitude
				console.log(CircularText)
				console.log(CircularLng)
				console.log(CircularLat)
				
				SetBus();
			});
			
        });