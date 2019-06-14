<html>
  <head>
    <title> CIRCULAR UFPA</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!--CSS Styles-->
    <link rel="stylesheet" href="../../assets/css/w3.css">
    <link rel="stylesheet" href="../../assets/css/css?family=Raleway">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="../../assets/css/leaflet.css" integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="crossorigin=""/>
    <link rel="stylesheet" href="../../assets/css/leaflet-routing-machine.css" />

    <link rel="stylesheet" type="text/css" href="../../assets/css/base.css"/>

    <!--JS Scripts-->
    <script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js" integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg==" crossorigin=""></script>
    <script src="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.js"></script>
    
    <script type="text/javascript" src="assets/js/stop.js"></script>
    <script type="text/javascript" src="assets/js/bus.js"></script>
    <script type="text/javascript" src="assets/js/map.js"></script>

    <script type="text/javascript" src="assets/js/mqtt/browserMqtt.js"></script>
    <script type="text/javascript" src="assets/js/mqtt/clientMqtt.js"></script>

    <script type="text/javascript" src="assets/js/layer/vector/KML.js"></script>
    <script type="text/javascript" src="assets/js/layer/MovingMarker.js"></script>

<body>
<!-- Navigation Bar -->
<div class="w3-bar background w3-large"> 
  <a class="nav-header w3-bar-item w3-mobile"><img src="../../assets/img/sobre/logo_sidebar_2019.jpg" height="32.5" width="100"></a>
  <a  onclick = "document.getElementById('modal-wrapper4').style.display='block'" class="w3-bar-item w3-button w3-hide-small w3-hover-blue-gray">Frota ativa</a>  
  <a  onclick = "document.getElementById('modal-wrapper3').style.display='block'" class="w3-bar-item w3-button w3-hide-small w3-hover-blue-gray">Paradas</a>
  <a onclick = "document.getElementById('modal-wrapper2').style.display='block'" class="w3-bar-item w3-button w3-hide-small w3-hover-blue-gray">Feedback</a>
  <a onclick = "document.getElementById('modal-wrapper').style.display='block'" class="w3-large w3-bar-item w3-button w3-hide-small w3-hover-blue-gray">Sobre</a>
  <a href="#work" class="w3-bar-item w3-button w3-right w3-hide-small w3-medium w3-hover-blue-gray w3-large"><i class="fa fa-facebook-official w3-hover-opacity fa-2x"></i></a>
  <a href="#work" class="w3-bar-item w3-button w3-right w3-large w3-hide-small w3-hover-blue-gray"> <i class="fa fa-twitter w3-hover-opacity fa-2x"></i></a>    
  <a href="myfile.htm" class="w3-bar-item w3-button w3-right w3-hide-small w3-hover-blue-gray w3-large"><img src="../../assets/img/sobre/playstore_icon.svg" width="30" height="30"></a> 
</div>
<div class="w3-bar background w3-large"> 
<a href="javascript:void(0)" class="w3-bar-item w3-right w3-hide-large w3-hide-medium" onclick="w3_open()">
      <i class="fa fa-bars"style="color:white"></i>
    </a>
  </div>
<div class="sidebar">
<!-- Sidebar on small screens when clicking the menu icon -->
	<nav class="w3-sidebar w3-bar-block w3-collapse w3-top background2 w3-hide-large" style="width:250px;display:none" id="mySidebar">
        <a class="nav-header w3-bar-item w3-mobile"><img src="../../assets/img/sobre/logo_sidebar_2019.jpg" height="45" width="100"></a>
	<a onclick = "document.getElementById('modal-wrapper4').style.display='block'" class="w3-bar-item w3-button">Frota ativa</a>  
	<a onclick = "document.getElementById('modal-wrapper3').style.display='block'" class="w3-bar-item w3-button">Paradas</a>
	<a onclick = "document.getElementById('modal-wrapper2').style.display='block'" class="w3-bar-item w3-button">Feedback</a>
	<a onclick = "document.getElementById('modal-wrapper').style.display='block'" class="w3-bar-item w3-button">Sobre</a>
	<a href="#facebook" class="w3-bar-item w3-button w3-right"><i class="fa fa-facebook-official w3-hover-opacity"></i></a>
	<a href="#twitter" class="w3-bar-item w3-button w3-right"> <i class="fa fa-twitter w3-hover-opacity"></i></a>    
	<a href="#playstore" class="w3-bar-item w3-button w3-right"><img src="../../assets/img/sobre/playstore_icon.svg" width="15" height="15"></a>   
        <a class="nav-header w3-bar-item w3-mobile" style="position:absolute;bottom:0px"><img src="../../assets/img/sobre/logo.png" height="30" width="30"></a>	
</nav>
</div>

<!-- Header -->
<header class="w3-display-container w3-content" style="max-width:1500px;">
<link rel="shortcut icon" href="../../assets/img/sobre/playstore_icon.svg" type="image/x-icon">  
<link rel="icon" href="https://image.flaticon.com/icons/svg/214/214320.svg" type="image/x-icon">
<meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
</header>


<!------------------------->
<!-- Inicio dos Popups --->
<!-- Modal 1 (Popup Sobre) -->
<div id="modal-wrapper" class="modal">
  
  <form class="modal-content animate" action="/action_page.php">
        
    <div class="imgcontainer">
      <span onclick="document.getElementById('modal-wrapper').style.display='none'" class="close" title="Close PopUp">&times;</span>
      <img src="../../assets/img/sobre/Banner_2019.jpeg" alt="Avatar" class="avatar">
      <h1 style="text-align:center">Sobre o Projeto</h1>
    </div>

    <div class="container">

      <p> É uma proposta desenvolvida pelo PETi (projeto dentro LASSE - Núcleo de P&D em Telecomunicações, Automação e Eletrônica) que visa atender a população que está diariamente na UFPA e necessita do transporte coletivo “circular”. A demanda de pessoas é bem maior do que o número de ônibus, no entanto, um dos principais problemas enfrentados pelos usuários é a questão do tempo, pois, mesmo estando dentro do campus, a demora para se locomover de um portão para outro, por exemplo, é muito prejudicial aos que estão a espera do transporte. É a partir dessa problemática que o projeto surge, a intenção é trazer mais conforto para todos. </p>

<p><b>Agradecimento aos que fizeram este projeto acontecer:</b> Ingrid Nascimento, Moacir Neto, Camila Novaes, Carlos Eduardo Dias, Gabriel Couto, Marcos Lude, Virgínia Tavares e Yuri Silva. </p>   </div>

  </form>
  
</div>


<!-- Modal 2 (Popup Feedback) -->

<div id="modal-wrapper2" class="modal">
  
  <form class="modal-content animate" action="/action_page.php">
        
    <div class="imgcontainer">
      <span onclick="document.getElementById('modal-wrapper2').style.display='none'" class="close" title="Close PopUp">&times;</span>
      <img src="../../assets/img/sobre/Banner_2019.jpeg" alt="Avatar" class="avatar">
      <h1 style="text-align:center">Contato</h1>
    </div>

    <div class="container2">
      <h6>Programa Especial de Treinamento Inclusivo do LASSE - <a href="https://peti.lasseufpa.org/" target="_blank">PETi</a> </h6>
      <h6>Núcleo de Pesquisa e Desenvolvimento em Telecomunicações, Automação e Eletrônica - <a href="https://www.lasse.ufpa.br/" target="_blank">LASSE</a> </h6>
    </div>

  </form>
  
</div>

<!-- Modal 3 (Popup Paradas) -->
<div id="modal-wrapper3" class="modal">
  
  <form class="modal-content animate" action="/action_page.php">
        
    <div class="imgcontainer">
      <span onclick="document.getElementById('modal-wrapper3').style.display='none'" class="close" title="Close PopUp">&times;</span>
      <img src="../../assets/img/sobre/Banner_2019.jpeg" alt="Avatar" class="avatar">
      <h1 style="text-align:center">Paradas</h1>
    </div>

    <div class="container2">
      <h6>As paradas realizadas pelo circular são simbolizadas pelas bandeiras azuis no mapa. Para mais informações acerca dos campus da UFPA <a href="http://prefeitura.ufpa.br/index.php/mapas" target="_blank">acesse</a>. </h6>
    </div>

  </form>
  
</div>

<!-- Modal 4 (Popup Frotas Ativas) -->

<div id="modal-wrapper4" class="modal">

	<form class="modal-content animate" action="/action_page.php">

	<div class="imgcontainer">
	      <span onclick="document.getElementById('modal-wrapper4').style.display='none'" class="close" title="Close PopUp">&times;</span>
     		 <img src="../../assets/img/sobre/Banner_2019.jpeg" alt="Avatar" class="avatar">
     	 <h1 style="text-align:center">Frotas ativas</h1>
   	</div>

    <div class="container2">
      <h6>Em breve...</h6>
    </div>

  </form>

</div>
<!-------------------->
<!-- Fim dos Popups -->




<div id="mapid" class="minha-div"></div>
<!-- Footer -->
	<footer class="background footer w3-center">
 	<h3>LASSE
		<img src="../../assets/img/sobre/logo.png" height="30" width="30">
	</h3>
	</footer>
	<script>  
		 initialize_map();
	</script>

<script>

// If user clicks anywhere outside of the modal, Modal will close

var modal = document.getElementById('modal-wrapper');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

var modal = document.getElementById('modal-wrapper2');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
</script>
<script>
// Toggle between showing and hiding the sidebar when clicking the menu icon
var mySidebar = document.getElementById("mySidebar");

function w3_open() {
  if (mySidebar.style.display === 'block') {
    mySidebar.style.display = 'none';
  } else {
    mySidebar.style.display = 'block';
  }
}

// Close the sidebar with the close button
function w3_close() {
    mySidebar.style.display = "none";
}
</script>
</body>
</html>
