var map;
var mapLat = -1.473590;
var mapLng = -48.451329;
var mapDefaultZoom = 17;


function initialize_map() {
    map = L.map('mapid').setView([mapLat, mapLng], mapDefaultZoom);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 20,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.streets'
        }).addTo(map);

    var popup = L.popup();
    function onMapClick(e) {
    popup
        .setLatLng(e.latlng)
        .setContent("You clicked the map at " + e.latlng.toString())
        .openOn(map);
    }

    map.on('click', onMapClick);
    set_stops(map);

   var track = new L.KML("assets/data/rota.kml", {async: true});
	track.on("loaded", function(e) {
			map.fitBounds(e.target.getBounds());
		});
	map.addLayer(track);
	
		
}

