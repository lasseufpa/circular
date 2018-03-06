
//centro do mapa
var LatLng = {lat: -1.476261, lng:  -48.455639};

//variavel do mapa
var map;

//variavel do arquivo(KML) que gera a rota
var ctaLayer;
var directionsDisplay;
var directionsService;

 //funcao que gera o mapa
function initMap() {
//directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});
directionsDisplay = new google.maps.DirectionsRenderer({draggable:false,suppressMarkers: true});
directionsService = new google.maps.DirectionsService;

  	map = new google.maps.Map(document.getElementById('map'), {
	zoom: 20,
    center: LatLng,
	mapTypeControl: true,
          mapTypeControlOptions: {
              style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
              position: google.maps.ControlPosition.TOP_CENTER
          },
         scaleControl: true,
          streetViewControl: true,
          streetViewControlOptions: {
              position: google.maps.ControlPosition.RIGHT_BOTTOM
          }
         });
    ctaLayer = new google.maps.KmlLayer({
    url: 'http://circularufpa.esy.es/RotaCircularUFPA.kml',
    map: map
	});
 
	//insere as paradas do array no mapa
	for (var i = 0; i < pointsMarkers.length; i++) {
		addMarker(pointsMarkers[i], i);
	}
	directionsDisplay.setMap(map);
	
}


 
