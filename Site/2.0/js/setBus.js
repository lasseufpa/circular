//endereco do icone dos onibus
var imageBus = 'img/newPinCirc.png';

//variaveis do circular

var CircularText;
var CircularLat;
var CircularLng;

var circularMarkers = [];


function setBus() {

    var marker = getMarker();

    if (circularMarkers.length != 0) {
        circularMarkers.forEach(function (bus) {

            if (bus.getTitle() == marker.getTitle()) {
                // console.log("BUS FOUND");
                moveBus(bus, marker);
            }

        });
    } else {
        // console.log("ADD BUS");
        marker.setMap(map);
        circularMarkers.push(marker);
    }

}

function getMarker() {
    var infowindowBus = new google.maps.InfoWindow({
        content: CircularText
    });
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(CircularLat, CircularLng), //define a posicao do marcador
        icon: imageBus, // define a imagem do marcador
        map: null
    });
    marker.addListener('click', function () {
        infowindowBus.open(marker);
    });

    return marker;
}

function moveBus(markerStart, markerEnd) {
    var request = {
        origin: markerStart.getPosition(),
        destination: markerEnd.getPosition(),
        travelMode: google.maps.TravelMode.DRIVING
    };

    var directionsService = new google.maps.DirectionsService();
    directionsService.route(request, function (response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            response.routes.forEach(function (rout) {
                var path = (rout.overview_path);

                path.forEach(function (position, p2, p3) {
                    console.log("position -> " + position)
                    markerStart.setPosition(position);
                    sleep(50);
                })
            });
        } else {
            markerStart.setPosition(markerStart.getPosition());
        }
    });

}

function sleep(miliseconds) {
    var currentTime = new Date().getTime();

    while (currentTime + miliseconds >= new Date().getTime()) {
        console.log("TIMER");
    }
}

//funcao que � chamada para apagar os onibus
function clearMarkersBus() {
    circularMarkers.forEach(function (bus) {
        bus.setMap(null);
    });

}

//funcao que e chamada para adcionar os onibus
function showMarkersBus() {
    circularMarkers.forEach(function (bus) {
        bus.setMap(map);
    });
}

