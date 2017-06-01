//endereco do icone dos onibus
var imageBus = 'images/icon circular.png';

//variaveis do circular
var NameBus = [];
var positionBus = [];
var markerBus = [];
var CircularText;
var CircularLat;
var CircularLng;

function SetBus() {
    var cont = 0; //contador para saber se foi inserido uma NOVA POSICAO em algum onibus
    for (var i = 0; i < NameBus.length; i++) {
        if (NameBus[i] == CircularText) { //usa o nome pra saber se o onibus ja esta no aray	
            markerBus[i].setMap(null);
            positionBus[i] = new google.maps.LatLng(CircularLat, CircularLng); //se ja existir, apenas atualiza a posicao
            cont++;
        }
    }
    if (cont == 0) { //se nao houve mudanca
        positionBus.push(new google.maps.LatLng(CircularLat, CircularLng)); //insere a nova localizacao no array
        NameBus.push(CircularText); //insere o novo nomme no array
    }
    if (markerBus.length == 0) {
        addBus(positionBus[0], 0)
    } else {
        clearMarkersBus();
        showMarkersBus();
        for (var i = 0; i < positionBus.length; i++) {
            addBus(positionBus[i], i);
        }
    }
}


//funcao que insere no array os onibus
function addBus(location, index) {
    var infowindowBus = new google.maps.InfoWindow({
        content: NameBus[index]
    });
    var marker = new google.maps.Marker({
        position: location,
        icon: imageBus, // define a imagem do marcador
        map: map
    });
    marker.addListener('click', function() {
        infowindowBus.open(map, marker);
    });
    markerBus.push(marker);
}

// Seta no mapa os onibus do array
function setMapBus(map) {
    for (var i = 0; i < markerBus.length; i++) {
        markerBus[i].setMap(map);
    }
}

//funcao que é chamada para apagar os onibus
function clearMarkersBus() {
    for (var i = 0; i < markerBus.length; i++) {
        markerBus[i].setMap(null);
    }

}

//funcao que e chamada para adcionar os onibus
function showMarkersBus() {
    for (var i = 0; i < positionBus.length; i++) {
        addBus(positionBus[i]);
    }
}