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
        this.oldLoc        = [lat, lng];
        // this.marker        = 0;
    }

    isValid(){
        console.log(Data.now());
        console.log(this.timer);
    }
  }

var busIcon = L.icon({
    iconUrl: './img/bus/iconCircular.png',
    iconAnchor:   [0, 32, 0, 0]
});

function set_bus(name, signalQuality, temp, timer, lat, lng, speed, direction, map){
    bus = new Bus(name, signalQuality, temp, timer, lat, lng, speed, direction)

    if (busList.length < 0 || !(find_bus(bus, busList))){
        busList.push(bus)
        console.log(busList)
    } else {
        update_bus(bus, busList);
        console.log("updated")
        console.log(busList)
    }

    // for (let b of busList){
    //     busLoc = [bus.lat, bus.lng]

    //     if (busLoc == null){
    //         bus.marker = L.marker(busLoc, {icon: busIcon}).addTo(map);
    //     } else {
    //         bus.marker = L.Marker.movingMarker([bus.oldLoc, busLoc],  [1000], {icon: busIcon},).addTo(map);
    //         bus.oldLoc = busLoc
    //     }
    // }

    // if(locAtual == locAnt || locAnt == null){
    //     bus_marker = L.marker(locAtual, {icon: busIcon}).addTo(map);
    //     locAnt = locAtual;
    // } else{
    //     bus = L.Marker.movingMarker([locAnt, locAtual],  [1000], {icon: busIcon},).addTo(map);
    //     map.addLayer(bus);
    //     bus.start();
    //     locAnt = locAtual;
    // }
}

function update_bus(bus, busList){
    busList.signalQuality = bus.signalQuality;
    busList.temp          = bus.temp;
    busList.timer         = bus.timer;
    busList.lat           = bus.lat;
    busList.lng           = bus.lng;
    busList.speed         = bus.speed;
    busList.direction     = bus.direction;
}

function find_bus(bus, busList){
    for (let k of busList){
        if (bus.name == k.name){
            return true;
        }
    }

    return false;
}

// function add_bus(bus, map){
//     busList.push(bus)
//     bus.marker = L.marker(locAtual, {icon: busIcon}).addTo(map);
// }

// function remove_bus(bus){
//     bus.marker.remove();
// }