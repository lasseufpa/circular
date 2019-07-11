var locAtual;
var locAnt;
var bus;
var busList = []
var a;
var c;

class Bus {
    constructor(name, signalQuality, temp, timer, lat, lng, speed, direction) {
        this.name          = name;
        this.signalQuality = signalQuality;
        this.temp          = temp;
        this.timer         = Date.now(); //timer;
        this.lat           = lat;
        this.lng           = lng;
        this.speed         = speed;
        this.direction     = direction;
        this.oldLoc        = null;
        this.marker        = null;
        this.crono         = null;  //ls
        this.j             = null;  //ls
    }
  }

var busIcon = L.icon({
    iconUrl: './img/bus/iconCircular.png',
    iconAnchor:   [0, 32, 0, 0], //32 no segundo
    popupAnchor:  [0, -15, 0, 0]
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

function update_bus(name, signalQuality, temp, timer, lat, lng, speed, direction, busList, crono, j){
    for (b of busList){
        if (b.name == name){
            a = b.timer;
            if((b.lng == lng) && (b.lat == lat)){  //ls //mudar para igual dps
                console.log('test1');
                b.j = Date.now();  //ls
                console.log(b.timer);
                console.log(b.j);
                b.crono = b.crono + b.j - b.timer;  //ls
                console.log(b.crono);
                if(b.crono > 60000) {  //ls
                    console.log('test');
                    b.marker.remove();  //ls
                }else {
                    console.log('test4');
                    b.signalQuality = signalQuality;
                    b.temp          = temp;
                    b.timer         = Date.now(); //timer;
                    b.lat           = lat;
                    b.lng           = lng;
                    b.speed         = speed;
                    b.direction     = direction;
                    c = b.timer;
                }
            } else {  //ls
                console.log('test2');
                console.log(b.name);
            b.signalQuality = signalQuality;
            b.temp          = temp;
            b.timer         = timer;
            b.oldLoc        = [b.lat, b.lng];
            b.lat           = lat;
            b.lng           = lng;
            b.speed         = speed;
            b.direction     = direction;
            b.crono         = 0;
            c = b.timer;

            move_bus(b);
            //b.marker.remove();
            //create_bus(b);
        }
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
    b.marker.bindPopup(b.name);  //nome do circular no mapa
}

function conection(a, c, name, busList) {
    for (let b of busList){
        if (b.name == name){
            if(a == c){
                b.marker.remove();
            }
        }
    }
}

 function move_bus(b){
     b.marker.remove();
     b.marker = L.Marker.movingMarker([b.oldLoc, [b.lat, b.lng]], [1000], {icon: busIcon},);//.addTo(map); //marker1
     map.addLayer(b.marker);  //marker1
     b.marker.start(); //marker1
     //b.marker.remove(); //ls
     //b.marker = b.marker1//ls
     b.marker.bindPopup(b.name);  //nome do circular no mapa
     b.oldLoc = [b.lat, b.lng];
 }