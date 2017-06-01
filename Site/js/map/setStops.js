//endereco do icone dos marcadores
var imageStop = 'images/stops/stoppoint.png';

//array dos marcadores da parada
var markers = [];

//Localização das paradas para os marcadores
var pointsMarkers = [
  {lat: -1.464255, lng: -48.444712}, // Espaco Inovacao 0
  {lat: -1.469727, lng: -48.446557}, // Betina 1
  {lat: -1.472745, lng: -48.451378}, // Portao 3 2
  {lat: -1.472965, lng: -48.451668}, // Portao 3 3
  {lat: -1.476600, lng: -48.454826}, // Vadiao 4
  {lat: -1.474466, lng: -48.455782}, // ICEN 5
  {lat: -1.473141, lng: -48.455911}, //Ginasio 6
  {lat: -1.475790, lng: -48.455654}, //Reitoria e Biblioteca 7
  {lat: -1.476433, lng: -48.454813}, //Reitoria e RU 8
  {lat: -1.475053, lng: -48.458483}, //RU e instituto de Geociências 9
  {lat: -1.473459, lng: -48.458701}, //10
  {lat: -1.472336, lng: -48.457703},//11
  {lat: -1.474894, lng: -48.454301}, //PGITEC  12
  {lat: -1.475063, lng: -48.454121}, //PGITEC  13
  {lat: -1.477671, lng: -48.456557}, //14
  {lat: -1.477092, lng: -48.458086},//15
  {lat: -1.473297, lng: -48.453620}, //Dona GINA 16
  {lat: -1.472667, lng: -48.448853},// 17
  {lat: -1.471116, lng: -48.448037}, // Odontologia e Laboratorio de Analises Clinicas 18
  {lat: -1.470950, lng: -48.447908}, // Odontologia e Laboratorio de Analises Clinicas 19
  {lat: -1.470126, lng: -48.446759}, //Nutricao e Betina 20
  {lat: -1.467505, lng: -48.447569}, //Portao 4 21
  {lat: -1.467192, lng: -48.446933}, //Portao 4 22
  {lat: -1.468420, lng: -48.448159}, // Biomedicina 23
  {lat: -1.468937, lng: -48.448218},//24
  {lat: -1.463631, lng: -48.444562}, //PCT 25
  {lat: -1.463215, lng: -48.444622}, //PCT 26
  {lat: -1.461523, lng: -48.442213} //Instituto de Pesquisas Espaciais 26
];

//Informacões das paradas
var informations = new Array(
[ "Espaco Inovacao","Espaco inovacao, PCT-Guama"],        //0
[ "Hospital Betina Ferro","Hospital Universitario Betina ferro de Souza" ],        //1
[ "Portao 3 (Lado Portaria)", "Portao 3 Da UFPA, Terminal Rodoviario"],        //2
[ "Portao 3 (Lado Estacionamento)", "Portao 3 Da UFPA, ARodoviario"],       //3
[ "Vadiao", "Espaco de Recreacao, Bancos, Lojas e Servicos"],        //4
[ "ICEN", "Istituto de Ciencias Exatas e Naturais" ],        //5
[ "Ginasio", "Ginasio de Esportes da UFPA"],        //6
[ "Reitoria e Biblioteca Central", ""],        //7
[ "Reitoria", "Reitoria da Universidade Federal do Para"],        //8
[ "Instituto de Geociencias", "" ],        //9
[ "Parada Circular", ""],        //10
[ "CAPACIT", "Coordenadoria de Capacitacao e Desenvolvimento"],        //11
[ "Bosque", "Espaco verde do ITEC"],        //12
[ "PGITEC/NEWTON", "Bloco de Pos Graduacao do ITEC"],        //13
[ "Mirante do Rio", "Novo Bloco de aulas do Campus basico" ],        //14
[ "RU Basico", "Restaurante universitario do campus basico"],        //15
[ "Dona Gina", "" ],        //16
[ "ICSA", "Instituto de Ciencias Sociais Aplicadas"],        //17
[ "Odontologia e Laboratorio de Analises Clinicas", "" ],        //18
[ "Odontologia e Laboratorio de Analises Clinicas", "" ],        //19
[ "Nutricao e Hospital Betina Ferro", "" ],        //20
[ "Portao 4", "Portao 4 da UFPA" ],        //21
[ "CEAMAZON", "Centro de Exc. em Eficiencia energética da Amazônia "],        //22
[ "Biomedicina", ""],        //23
[ "Parada Circular", ""],        //24
[ "PCT guama", "Parque de ciencia e Tecnologia"],        //25
[ "PCT guama", "Parque de Ciencia e Tecnologia"],        //26
[ "Parada circular", ""] 
);

//funcao que insere no mapa as paradas de uma a uma  			marker
function addMarker(location, index) {
	
	var marker = new google.maps.Marker({
    	position: location,
        icon: imageStop, // define a nova imagem do marcador
        title: informations[index][0],
		map: map
    });
		
    marker.addListener('click', function() {
        infowindow.open(map, marker);
    });
	
	
    var contentString = '<b>' + informations[index][0] + '</b>' +
        '<div id="bodyContent">' +
        '<p>' + informations[index][1];

    var infowindow = new google.maps.InfoWindow({
        content: contentString,
        maxWidth: 200
    });
    markers.push(marker);
    
}

// Seta no mapa as paradas do array
function setMap(map) {
	
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

//funcao que é chamada para apagar as paradas
function clearMarkers() {
    setMap(null);
}

//funcao que e chamada para mostrar as paradas
function showMarkers() {
    setMap(map);
}