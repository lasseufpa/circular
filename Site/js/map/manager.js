//variaveis auxiliadoras
var valor="ocultar";
var Cmap="com rota";
var animation="sim";
var logoPosition="nao mostar";

//variaveis usadas para os carros
var iconCarStart='images/StartEnd/convertibleStart.png';
var iconCarEnd='images/StartEnd/convertibleEnd.png';

//variaveis usadas pelo onibus
var iconBusStart = 'images/StartEnd/busStart.png';
var iconBusEnd='images/StartEnd/busEnd.png';

//variaveis usadas pelo usuario
var iconUserEnd='images/StartEnd/userEnd.png';
var userEndLat=-1.466533;
var userEndLng=-48.444693;
var iconUserStart = 'images/StartEnd/userStart.png';
var userStartLat=-1.474856;
var userStartLng=-48.457181;

var infowindowA;
var infowindowB;

//array onde os pontos de subida e descida ficam salvos
var PositionsStopsUser = [];
var PositionsStopsBus = [];

//modo de viagem
var selectedMode='';

//menor distancia entre dois pontos
var menorDistanciaA
var menorDistanciaB;

//posicao do array que tem a menor distancia
var menorDistanciaIndexA=0;
var menorDistanciaIndexB=0;

//array onde as distancias estao salvas
var distancesA = [];
var distancesB = [];

var idMenu = new Array(
[ "menu-1","desce"],  
[ "menu-2","desce" ],
[ "menu-3","desce"],    
[ "menu-4","desce"],  
[ "menu-5","desce"],
[ "menu-6","desce"]
);  

//funcoes iniciais  
mudarEstado('img');
mudarEstado('imgMap2');
mudarEstado('circle1');
mudarEstado('circle2');
mudarEstado('mode-selector');
logoOptions();

	
//funcao que move o logo para fora da tela
function logoBack(){

	 // Standard syntax
    document.getElementById("logo").style.transform = "translateX(0px)"; 
	document.getElementById("circle1").style.transform = "translateX(0px)"; 
	document.getElementById("circle2").style.transform = "translateX(0px)"; 
	document.getElementById("img").style.transform = "translateX(0px)"; 
	document.getElementById("imgMap2").style.transform = "translateX(0px)"; 
	document.getElementById("mode-selector").style.transform = "translateX(0px)"; 	   
	
	// Code for Safari
    document.getElementById("logo").style.WebkitTransform = "translateX(0px)"; 
	document.getElementById("circle1").style.WebkitTransform = "translateX(0px)"; 
	document.getElementById("circle2").style.WebkitTransform = "translateX(0px)"; 
	document.getElementById("img").style.WebkitTransform = "translateX(0px)"; 
	document.getElementById("imgMap2").style.WebkitTransform = "translateX(0px)"; 
	document.getElementById("mode-selector").style.WebkitTransform = "translateX(0px)"; 
   
	// Code for IE9
    document.getElementById("logo").style.msTransform = "translateX(0px)"; 
    document.getElementById("circle1").style.msTransform = "translateX(0px)"; 
    document.getElementById("circle2").style.msTransform = "translateX(0px)"; 
	document.getElementById("img").style.msTransform = "translateX(0px)"; 
	document.getElementById("imgMap2").style.msTransform = "translateX(0px)"; 
	document.getElementById("mode-selector").style.msTransform = "translateX(0px)"; 
}

//funcao que move o logo para a tela
function logoGo(){
// Standard syntax
    document.getElementById("logo").style.transform = "translateX(300px)"; 
	document.getElementById("circle1").style.transform = "translateX(300px)"; 
	document.getElementById("circle2").style.transform = "translateX(300px)"; 
	document.getElementById("img").style.transform = "translateX(300px)"; 
	document.getElementById("imgMap2").style.transform = "translateX(300px)"; 
	document.getElementById("mode-selector").style.transform = "translateX(300px)"; 	   
	
	// Code for Safari
    document.getElementById("logo").style.WebkitTransform = "translateX(300px)"; 
	document.getElementById("circle1").style.WebkitTransform = "translateX(300px)"; 
	document.getElementById("circle2").style.WebkitTransform = "translateX(300px)"; 
	document.getElementById("img").style.WebkitTransform = "translateX(300px)"; 
	document.getElementById("imgMap2").style.WebkitTransform = "translateX(300px)"; 
	document.getElementById("mode-selector").style.WebkitTransform = "translateX(300px)"; 
   
	// Code for IE9
    document.getElementById("logo").style.msTransform = "translateX(300px)"; 
    document.getElementById("circle1").style.msTransform = "translateX(300px)"; 
    document.getElementById("circle2").style.msTransform = "translateX(300px)"; 
	document.getElementById("img").style.msTransform = "translateX(300px)"; 
	document.getElementById("imgMap2").style.msTransform = "translateX(300px)"; 
	document.getElementById("mode-selector").style.msTransform = "translateX(300px)"; 
}
	

//veriifica se o logo deve ir ou voltar
function logoOptions(){

	if(logoPosition=="mostrar"){
		logoPosition="nao mostrar";
		logoGo();
	}else{
		logoPosition="mostrar";
		logoBack();
	}
} 

var menuManager ="desce";
var menuIndex=0;

function closeMenu(){
	for(var a=1; a<6;a++){
		// Standard syntax
    document.getElementById(idMenu[a][0]).style.transform = "translateY(0px)"; 
	
	// Code for Safari
    document.getElementById(idMenu[a][0]).style.WebkitTransform = "translateY(0px)"; 
   
	// Code for IE9
    document.getElementById(idMenu[a][0]).style.msTransform = "translateY(0px)"; 
	}
}

function openMenu(){
	for(var a=1; a<6;a++){
		// Standard syntax
    document.getElementById(idMenu[a][0]).style.transform = "translateY(110px)"; 
	
	// Code for Safari
    document.getElementById(idMenu[a][0]).style.WebkitTransform = "translateY(110px)"; 
   
	// Code for IE9
    document.getElementById(idMenu[a][0]).style.msTransform = "translateY(110px)"; 
	}
}

function moveElement(){
	mudarVisualizacao();
	if(menuManager=="desce"){
		menuManager="sobe";
		openMenu();
	}else{
		menuManager="desce";
		closeMenu();
	}
}	


//funcao que remove e insere os marcadores no mapa, mudar a cor de elemento
function atualizaMarkes(){
	if(valor=="mostrar"){
		valor="ocultar";
		showMarkers();
		if(selectedMode=='TRANSIT'){
			markers[menorDistanciaIndexA].setMap(null); 
			markers[menorDistanciaIndexB].setMap(null);
		}
		document.getElementById("circle1").style.backgroundColor = "#4285F4";
		}else{
			valor="mostrar";
			clearMarkers();
			document.getElementById("circle1").style.backgroundColor = "#FF8F1C";
		}
}

var visualizacaoOpcoes="mostrar"
//funcao usada para alternar a visualizacao dos icones
function mudarVisualizacao() {
mudarEstado('img');
mudarEstado('imgMap2');
mudarEstado('circle1');
mudarEstado('circle2');
}

//funcao usada para ocultar ou mostrar alguma DIV na tela
function mudarEstado(el) {
        var display = document.getElementById(el).style.display;
        if(display == "none")
            document.getElementById(el).style.display = 'block';
        else
            document.getElementById(el).style.display = 'none';
}
	
//mostra no mapa a rota para quem vai andando
function routeWalking(){
	distancesA.length = 0;
	distancesB.length = 0;
	selectedMode = 'WALKING';
	aplicarMudancas();
	verificaAnimation();
}

//mostra no mapa a rota para quem vai dirigendo
function routeDriving(){
	distancesA.length = 0;
	distancesB.length = 0;
	selectedMode = 'DRIVING';
	aplicarMudancas();
	verificaAnimation();
	}
	
//mostra no mapa a rota para quem vai de Circular
function routeBus(){
	distancesA.length = 0;
	distancesB.length = 0;
	selectedMode = 'TRANSIT';
	removeProx="sim";
	aplicarMudancas();
	verificaAnimation();
	}

//alterna entre mostar ou ocultar a rota, os icones, mudar a cor de elemento
function changeMap(){
	mudarEstado('mode-selector');
	document.getElementById("changemode-transit").checked = true;
	if(Cmap=="com rota"){
		Cmap="sem rota";
		animation="sim";
		PositionsStopsUser.length=0;
		setIconsUser(iconUserStart, userStartLat,userStartLng);
		setIconsUser(iconUserEnd, userEndLat,userEndLng);
		verificaAnimation();
		document.getElementById("circle2").style.backgroundColor = "#FF8F1C";
		}else{
			Cmap="com rota";
			removeIconsBus();
			removeIconsUser();
			document.getElementById("changemode-walking").checked = false;
			document.getElementById("changemode-transit").checked = false;
			document.getElementById("changemode-driving").checked = false;
			directionsDisplay.setMap(null);
			document.getElementById("circle2").style.backgroundColor = "#4285F4";

		}
	
	if(valor=="mostrar"){
		clearMarkers();
	}else{
		showMarkers();
		
	}
}


//funcao usanda para calcular mostrar a parad mais proxima
function calculateAndDisplayRouteBus(iconLat, iconLng) {
	if(valor=="ocultar"){
	markers[menorDistanciaIndexA].setMap(map); 
	markers[menorDistanciaIndexB].setMap(map);
	}
	
	for (var i = 0; i < pointsMarkers.length; i++) {
		if(distancesA[i] == menorDistanciaA){
		menorDistanciaIndexA=i;
			}

		}
	
	for (var i = 0; i < pointsMarkers.length; i++) {
		if(distancesB[i] == menorDistanciaB){
		menorDistanciaIndexB=i;
			}

		}

	setIconsBus(iconBusStart, markers[menorDistanciaIndexA].getPosition().lat(),markers[menorDistanciaIndexA].getPosition().lng(), menorDistanciaIndexA);
	setIconsBus(iconBusEnd, markers[menorDistanciaIndexB].getPosition().lat(),markers[menorDistanciaIndexB].getPosition().lng(), menorDistanciaIndexB);
	

	markers[menorDistanciaIndexA].setMap(null); 
	markers[menorDistanciaIndexB].setMap(null);  
}


//funcao usanda para calcular mostrar a rota de acordo com o modo de viagem
function calculateAndDisplayRoute(directionsService, directionsDisplay, iconLat1, iconLng1, iconLat2, iconLng2) {
	directionsService.route({
		origin:	new google.maps.LatLng(iconLat1,iconLng1),  
        destination: new google.maps.LatLng(iconLat2,iconLng2),
//		waypoints: [{location: new google.maps.LatLng(-1.464255,-48.444712)}, {location: new google.maps.LatLng(-1.469727,-48.446557)}],
        travelMode: google.maps.TravelMode[selectedMode]
        }, function(response, status) {
        if (status == 'OK') {
			directionsDisplay.setDirections(response);
			} else {
            window.alert('Directions request failed due to ' + status);
          }
        });
		
			if(valor=="mostrar"){
				clearMarkers();
			}
			else{
				showMarkers();
			}
}

// funcao que configura o mapa
function aplicarMudancas(){
	userStartLat=PositionsStopsUser[0].getPosition().lat();
	userStartLng=PositionsStopsUser[0].getPosition().lng();
	userEndLat=PositionsStopsUser[1].getPosition().lat();
	userEndLng=PositionsStopsUser[1].getPosition().lng();
	if(document.getElementById("changemode-walking").checked == true ||
			document.getElementById("changemode-transit").checked == true ||
			document.getElementById("changemode-driving").checked == true){
				menorDistanciaA=1000000000;
				menorDistanciaB=1000000000;
				
				distancesA.length = 0;
				distancesB.length = 0;
				
				removeIconsBus();
	
					if(selectedMode=='DRIVING'){
					removeIconsUser();
					PositionsStopsUser.length=0;
					setIconsUser(iconCarStart, userStartLat,userStartLng);
					setIconsUser(iconCarEnd, userEndLat,userEndLng);
					directionsDisplay.setMap(map);
					removeIconsBus();
					calculateAndDisplayRoute(directionsService, directionsDisplay, userStartLat,userStartLng, userEndLat,userEndLng);
					}else{
						if(selectedMode=='TRANSIT'){
							for (var i = 0; i < pointsMarkers.length; i++) {
								menorIndex=i;
								CalculaDistancia(i);
						}
							removeIconsUser();
							PositionsStopsUser.length=0;
							setIconsUser(iconUserStart, userStartLat,userStartLng);
							setIconsUser(iconUserEnd, userEndLat,userEndLng);
							calculateAndDisplayRouteBus(userStartLat,userStartLng);
							directionsDisplay.setMap(null);
							}else{
								directionsDisplay.setMap(map);
								removeIconsUser();
								PositionsStopsUser.length=0;
								setIconsUser(iconUserStart, userStartLat,userStartLng);
								setIconsUser(iconUserEnd, userEndLat,userEndLng);
								calculateAndDisplayRoute(directionsService, directionsDisplay, userStartLat,userStartLng, userEndLat,userEndLng);
						}
					}
	
	}else{
		alert('Escolha um tipo de Rota');
		}

}

//calulcula a distancia entre dois pontos
function CalculaDistancia(i) {
	
	var p1 = new google.maps.LatLng(PositionsStopsUser[0].getPosition().lat(), PositionsStopsUser[0].getPosition().lng()); //start
	var p2 = new google.maps.LatLng(PositionsStopsUser[1].getPosition().lat(), PositionsStopsUser[1].getPosition().lng()); //end
	var p3 = new google.maps.LatLng(markers[i].getPosition().lat(), markers[i].getPosition().lng()); //stops
	
	//calcula a menor distancia para o Start
	var distA = google.maps.geometry.spherical.computeDistanceBetween(p1, p3);
	distancesA.push(distA);
	
	if(distA < menorDistanciaA){
		menorDistanciaA=distA;
		
}
	
	//calcula a menor distancia para o End
	var distB = google.maps.geometry.spherical.computeDistanceBetween(p2, p3);
	distancesB.push(distB);
	
	if(distB < menorDistanciaB){
		menorDistanciaB=distB;
	}
	
}

//insere os icones dos usuarios
function setIconsUser(iconImage, iconLat, iconLng){
	infowindowA = new google.maps.InfoWindow({
        content: " Arraste o Icone",
        maxWidth: 200
    });
	infowindowB= new google.maps.InfoWindow({
        content: " Arraste o Icone",
        maxWidth: 200
    });
	var marker = new google.maps.Marker({
    	position: new google.maps.LatLng(iconLat,iconLng),
        icon: iconImage, // define a nova imagem do marcador
		draggable: true,
		map: map
    });
	PositionsStopsUser.push(marker);
	
	
	marker.addListener('dragend', function() {
	animation="nao";
	if(selectedMode == 'WALKING'){
		routeWalking();
	}else{
		if(selectedMode == 'DRIVING'){
			routeDriving();
		}else{
			routeBus();
		}
	}
	});
	
	
}
//insere os icones dos onibus
function setIconsBus(iconImage, iconLat, iconLng, index){
	var marker = new google.maps.Marker({
    	position: new google.maps.LatLng(iconLat,iconLng),
        icon: iconImage, // define a nova imagem do marcador
		title: informations[index][0],
        draggable: false,
		map: map
    });
	PositionsStopsBus.push(marker);
}

//remove os incone dos onibus
function removeIconsBus(){
   for (var i = 0; i < PositionsStopsBus.length; i++) {
        PositionsStopsBus[i].setMap(null);
    }
	
}

//remove os icones dos usuarios
function removeIconsUser(){
   for (var i = 0; i < PositionsStopsUser.length; i++) {
        PositionsStopsUser[i].setMap(null);
    }
	
}

//funcao que verifica se os marcadores devem ou não ficarem animados
function verificaAnimation(){
	if(animation=="sim"){
	infowindowA.open(map,  PositionsStopsUser[0]);
	infowindowB.open(map,  PositionsStopsUser[1]);
	animaMaker();
	}else{
		infowindowA.close();
		infowindowB.close();
		noAnimaMaker();
	}
}

//funcao para os marcadores ficarem animados
function animaMaker(){
	for (var i = 0; i < PositionsStopsUser.length; i++) {
        PositionsStopsUser[i].setAnimation(google.maps.Animation.BOUNCE);
    }
}

//funao para os marcadores ficarem inamiamdos
function noAnimaMaker(){
   for (var i = 0; i < PositionsStopsUser.length; i++) {
        PositionsStopsUser[i].setAnimation(null);
    }
	
}