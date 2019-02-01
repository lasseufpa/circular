<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>

    <!--CSS Styles-->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css" integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA==" crossorigin=""/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.css" />
    <!--JS Scripts-->
    <script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js" integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg==" crossorigin=""></script>
    <script src="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.js"></script>
    
    <script type="text/javascript" src="assets/js/stop.js"></script>
    <script type="text/javascript" src="assets/js/bus.js"></script>
    <script type="text/javascript" src="assets/js/map.js"></script>

    <script type="text/javascript" src="assets/js/mqtt/browserMqtt.js"></script>
    <script type="text/javascript" src="assets/js/mqtt/clientMqtt.js"></script>

</head>
<body>
<div id="mapid" style="width: 100vw; height: 100vh;"></div>
<script> 
    initialize_map();

</script>
</body>
</html>