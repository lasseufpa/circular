var bus;

var busIcon = L.icon({
    iconUrl: 'assets/img/bus/iconCircular.png',
    iconAnchor:   [0, 32, 0, 0]
});

function set_bus(lat, lng, map){
    if (bus != null){
        remove_bus();
    }
    bus = L.marker([lat, lng], {icon: busIcon}).addTo(map);
}

function remove_bus(){
    bus.remove();
}