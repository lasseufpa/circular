var locAtual;
var locAnt;
var bus;
var busList = []

class Bus {
    constructor(name, signalQuality, temp, timer, lat, lng, speed, direction) {
        this.name          = name;
        this.signalQuality = signalQuality;
        this.temp          = temp;
        this.timer         = timer;
        this.lat           = lat;
        this.lng           = lng;
        this.speed         = speed;
        this.direction     = direction;
        this.oldLoc        = null;
        this.marker        = null;
    }
  }

var busIcon = L.icon({
    iconUrl: './img/bus/iconCircular.png',
    iconAnchor:   [0, 32, 0, 0]
});

function set_bus(name, signalQuality, temp, timer, lat, lng, speed, direction, map){
    if (!(find_bus(name, busList))){
        bus = new Bus(name, signalQuality, temp, timer, lat, lng, speed, direction)
        busList.push(bus)
        create_bus(bus);
    } else {
        update_bus(name, signalQuality, temp, timer, lat, lng, speed, direction, busList);
    }
}

function update_bus(name, signalQuality, temp, timer, lat, lng, speed, direction, busList){
    for (b of busList){
        if (b.name == name){
            b.signalQuality = signalQuality;
            b.temp          = temp;
            b.timer         = timer;
            b.lat           = lat;
            b.lng           = lng;
            b.speed         = speed;
            b.direction     = direction;

            // move_bus(b);
            b.marker.remove();
            create_bus(b);
        }
    }
}

function find_bus(name, busList){
    for (let b of busList){
        if (b.name == name){
            return true;
        }
    }
    return false;
}

function create_bus(b){
    b.marker = L.marker([b.lat, b.lng], {icon: busIcon}).addTo(map);
    b.locAnt = [b.lat, b.lng];
}

// function move_bus(b){
//     b.marker = L.Marker.movingMarker([b.oldLoc, [b.lat, b.lng]], [1000], {icon: busIcon},).addTo(map);
//     map.addLayer(b.marker);
//     b.marker.start();
//     b.oldLoc = [b.lat, b.lng];
// }