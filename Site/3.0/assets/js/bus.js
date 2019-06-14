var locAtual;
var locAnt;
var bus;

var busIcon = L.icon({
    iconUrl: 'assets/img/bus/iconCircular.png',
    iconAnchor:   [0, 32, 0, 0]
});

function set_bus(lat, lng, map){
    if (bus != null){
        remove_bus();
    }

    locAtual = {lat,lng};

    if(locAtual == locAnt || locAnt == null){
        bus = L.marker(locAtual, {icon: busIcon}).addTo(map);
        locAnt = locAtual;
    }else{
        bus = L.Marker.movingMarker([locAnt, locAtual],  [1000], {icon: busIcon},).addTo(map);
        map.addLayer(bus);
        bus.start();
        locAnt = locAtual;
    }
}

function remove_bus(){
    bus.remove();
}