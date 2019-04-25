<?php
/**
 * Created by PhpStorm.
 * User: Izidio Carvalho
 * Date: 05/03/2019
 * Time: 22:01
 */
include_once("genericDAO.php");

#TODOS MEDIDORES ATIVADOS
$all = genericDAO::getAll(TRUE);
var_dump($all);