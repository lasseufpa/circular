<?php
/**
 * Created by PhpStorm.
 * User: Izidio Carvalho
 * Date: 05/03/2019
 * Time: 22:01
 */

$list = [];

$servername = "localhost";
$username = "root";
$password = "";
$database = "circular";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);
$conn->set_charset("utf8");

$sql = "SELECT * FROM parada";

if (!$result = $conn->query($sql)) {
    echo "Sorry, the website is experiencing problems.";
} else {
    $n = 1;
    $lat = 2;
    $lon = 3;
    while ($row = $result->fetch_row()) {
        $name = $row[$n];
        $latitude = $row[$lat];
        $longitude = $row[$lon];

        $item = [
            "NAME" => $name,
            "LATITUDE" => $latitude,
            "LONGITUDE" => $longitude
        ];

        $list[] = $item;
    }
}

$conn->close();
echo count($list)." itens:<br><br>";
foreach ($list as $item) {
    var_dump($item);
    echo "<br>";

}
