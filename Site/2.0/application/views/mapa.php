<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?><!DOCTYPE html>
<html lang="en">
<head>
	<meta charset='utf-8'>
	<title>Circular UFPA</title>
    
    <!--Import Google Icon Font-->
    <link href="css/Materialicon.css" rel="stylesheet">
    <!--Import materialize.css-->
      <link type="text/css" rel="stylesheet" href="materialize/css/materialize.min.css"  media="screen,projection"/>
    <!--Import base.css-->
      <link type="text/css" rel="stylesheet" href="css/base.css"  media="screen,projection"/>
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	
</head>
<body>
    <header>
 
    <nav class="navbar-fixed">
        <div>
      <div class="nav-wrapper">
          
         
     <a href="#" data-activates="slide-out" class="button-collapse"><i class="material-icons">menu</i></a>
          
          <a href="#!" class="brand-logo">
              
              <div class="logo-title">
                 
          <div class="bar-title"><b>Circular UFPA</b></div>
            <div class="bar-subtitle">Mapa</div>
          </div>
          </a>
          
        <ul class="right hide-on-med-and-down">
             <li class="active"><a href="#">Mapa</a></li>
          <li><a href="#">Sobre</a></li>
            <li><a href="#">Contato</a></li>
            <li><a href="#">Guia</a></li>
            
          <li><!--<a class="waves-effect waves-light btn red">Baixar app<i class="material-icons right">shop</i></a>-->
            <a class="waves-effect waves-light" style="height:62px;" href='https://play.google.com/store/apps/details?id=org.lasseufpa.Circular&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img  style="width:170px;" alt='Disponível no Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/pt-br_badge_web_generic.png'/></a></li>
        </ul>
      </div>
            </div>
    </nav>
  
       
     
    <ul id="slide-out" class="side-nav fixed">
              <li class="logo">
                  <a id="logo-container" href="" >
            <img style="width: 300px;" src="img/banner.png"/>
                  
                  </a>
              </li>
      
             <li class="no-padding">
        <ul class="collapsible collapsible-accordion">
          <li>
            <a class="collapsible-header">Configurações do Mapa<i class="material-icons">arrow_drop_down</i></a>
            <div class="collapsible-body">
              <ul>
                
                  <li ><a class="switch " ><i class="material-icons left">settings_remote</i>Loc. Circular<label style="float: right;"> <input id="checkbox-enable-bus" type="checkbox" onclick="setBus()" checked>
      <span class="lever "></label></span></a></li>
             <li  ><a class="switch " ><i class="material-icons left">flag</i>Pontos de parada <label style="float: right;"> <input type="checkbox" onclick="atualizaMarkes()"  checked>
      <span class="lever "></label></span></a></li>
             <li  ><a class="switch " ><i class="material-icons left">call_made</i>Rota de ida <label style="float: right;"> <input type="checkbox">
      <span class="lever "></label></span></a></li>
             <li  ><a class="switch " ><i class="material-icons left">call_received</i>Rota de volta <label style="float: right;"> <input type="checkbox">
      <span class="lever "></label></span></a></li>
                  
                  
              </ul>
            </div>
          </li>
            <li><a href="#" ><i class="material-icons left">directions_bus</i>Frota ativa</a></li>
            <li><a href="#" ><i class="material-icons left">flag</i>Paradas</a></li>
            <li><a href="#" ><i class="material-icons left">settings_remote</i>Feedback</a></li>
            <li><a href="#" ><i class="material-icons left">settings_remote</i>Sobre</a></li>
        </ul>
      </li>
             
      
    </ul>       
</header>

<main>
    
    <!--
        <div class="card-panel "  id="card-bus">
            <div class="card-content">
           <ul class="collection">
    <li class="collection-item avatar"><a  href="#" >
      <i class="material-icons circle">location_on</i>
      <span class="title">circular 01</span>
      <p>Location
      </p>
        </a>
    </li>
    <li class="collection-item avatar"><a href="#" >
      <i class="material-icons circle">location_on</i>
      <span class="title">Title</span>
      <p>First Line </p>
        </a>
               </li>
  </ul>
           </div> 
        </div>
     
    -->
     <div id="map">
    
    
    
    </div>
    </main>

    
    
    
    <footer class="page-footer">
          <div class="footer-copyright">
            <div class="row" style="margin-bottom:0px;">
                <div class="col s8">
            © 2017 - Alphamage tech - Page rendered in <strong>{elapsed_time}</strong> seconds.  <?php echo  (ENVIRONMENT === 'development') ?  'CodeIgniter Version <strong>' . CI_VERSION . '</strong>' : '' ?>
                    </div>
                <div class="col s4">
                <a class="grey-text text-lighten-4 right" href="http://alphamage.com"><img style="width:150px;" src="img/logo-menu-inline.png"/></a>
                </div>
            </div>
          </div>
        </footer>
     <script type="text/javascript" src="js/browserMqtt.js"></script>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
      <script type="text/javascript" src="materialize/js/materialize.min.js"></script>
    
    <script type="text/javascript" src="js/setStops.js" charset="utf-8"></script>
    <script type="text/javascript" src="js/manager.js"></script>
	<script type="text/javascript" src="js/setBus.js"></script>
    <script type="text/javascript" src="js/setMap.js"></script>
	
      <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAUlFPt6t42rcjR47eoVL2wVxqyIYGa0xA&callback=initMap"
      async defer></script>
    <div id="map-canvas" />
    <script type="text/javascript" src="js/clientMqtt.js"></script>
    <script>
        
        $( document ).ready(function() {
      $(".button-collapse").sideNav();
    });
    </script>
    
      <!--Import jQuery before materialize.js-->
      
    
</body>
</html>