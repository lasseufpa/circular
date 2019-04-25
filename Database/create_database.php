<?php
/**
 * Created by PhpStorm.
 * User: Izidio Carvalho
 * Date: 05/03/2019
 * Time: 20:24
 */

$SQL = "

-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 05, 2019 at 11:17 PM
-- Server version: 5.7.19
-- PHP Version: 7.1.9

SET AUTOCOMMIT = 0;
START TRANSACTION;

--
-- Database: circular
--
DROP DATABASE IF EXISTS circular;
CREATE DATABASE circular;
-- --------------------------------------------------------

--
-- Table structure for table circular
--

DROP TABLE IF EXISTS circular.circular;
CREATE TABLE IF NOT EXISTS circular.circular (
id bigint(20) NOT NULL AUTO_INCREMENT,
  descricao varchar(255) DEFAULT NULL,
  ativado bit(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'circular'
--

INSERT INTO circular.circular (id, ativado, descricao) VALUES
(1,b'1','Circular 1'),
(2,b'1','Circular 2');

-- --------------------------------------------------------

--
-- Table structure for table localizacao
--

DROP TABLE IF EXISTS circular.localizacao;
CREATE TABLE IF NOT EXISTS circular.localizacao (
id bigint(20) NOT NULL AUTO_INCREMENT,
  data datetime DEFAULT NULL,
  circular_id bigint(20) DEFAULT NULL,
  parada_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_circular_id (circular_id),
  KEY FK_parada_id (parada_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table parada
--

DROP TABLE IF EXISTS circular.parada;
CREATE TABLE IF NOT EXISTS circular.parada (
id bigint(20) NOT NULL AUTO_INCREMENT,
  descricao varchar(255) DEFAULT NULL,
  latitude varchar(255) DEFAULT NULL,
  longitude varchar(255) DEFAULT NULL,
  ativado bit(1) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

--
-- Dumping data for table 'parada'
--

INSERT INTO circular.parada (id, ativado, descricao, latitude, longitude) VALUES
(1, b'1', 'Espaço Inovação', '-1.464255', '-48.444712'),
(2, b'1', 'Betina', '-1.469727', '-48.446557'),
(3, b'1', 'Portão 03 (Volta)', '-1.472745', '-48.451378'),
(4, b'1', 'Portão 03 (Ida)', '-1.472965', '-48.451668'),
(5, b'1', 'Vadião', '-1.476600', '-48.454826'),
(6, b'1', 'ICEN', '-1.474466', '-48.455782'),
(7, b'1', 'Ginasio', '-1.473141', '-48.455911'),
(8, b'1', 'Reitoria e Biblioteca', '-1.475790', '-48.455654'),
(9, b'1', 'Reitoria', '-1.476433', '-48.454813'),
(10, b'1', 'RU e Instituto de Geociência', '-1.475053', '-48.458483'),
(11, b'1', '', '-1.473459', '-48.458701'),
(12, b'1', '', '-1.472336', '-48.457703'),
(13, b'1', 'PGITEC', '-1.474918', '-48.454406'),
(14, b'1', 'PGITEC', '-1.474969', '-48.454264'),
(15, b'1', '', '-1.477671', '-48.456557'),
(16, b'1', 'latitude', '-1.477092', '-48.458086'),
(17, b'1', 'Dona Gina', '-1.473297', '-48.453620'),
(18, b'1', '', '-1.472667', '-48.448853'),
(19, b'1', 'Odontologia e Laboratorio de Analises Clinicas', '-1.471116', '-48.448037'),
(20, b'1', 'Odontologia e Laboratorio de Analises Clinicas', '-1.470950', '-48.447908'),
(21, b'1', 'Nutricao e Betina', '-1.470126', '-48.446759'),
(22, b'1', 'Portão 04', '-1.467505', '-48.447569'),
(23, b'1', 'Portão 04', '-1.467192', '-48.446933'),
(24, b'1', 'Biomedicina', '-1.468420', '-48.448159'),
(25, b'1', '', '-1.468937', '-48.448218'),
(26, b'1', 'PCT', '-1.463631', '-48.444562'),
(27, b'1', 'PCT', '-1.463215', '-48.444622'),
(28, b'1', 'Instituto de Pesquisas Espaciais', '-1.461523', '-48.442213');
";


$servername = "localhost";
$username = "root";
$password = "";

// Create connection
$conn = new mysqli($servername, $username, $password);
$conn->set_charset("utf8");
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Execute SQL
if ($conn->multi_query($SQL)) {
    echo "Database created successfully";
} else {
    echo "Error creating database: " . $conn->error;
}

$conn->close();

echo "<br><br><br>SQL:";
echo nl2br($SQL);
