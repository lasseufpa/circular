<?php

/**
 * Created by PhpStorm.
 * User: Izidio Carvalho
 * Date: 05/03/2019
 * Time: 22:01
 */
class genericDAO
{

    static $servername = "localhost";
    static $username = "root";
    static $password = "";
    static $database = "circular";

    /**
     * genericDAO constructor.
     */
    public function __construct()
    {

    }

    /**
     *
     * @return $list contendo as Paradas Ativas
     */
    public static function getAll($status)
    {
        // Create connection
        $conn = new mysqli(self::$servername, self::$username, self::$password, self::$database);
        $conn->set_charset("utf8");

        $sql = "SELECT * FROM parada WHERE ativado =" . $status;

        $result = $conn->query($sql);

        if (!$result) {
            return null;
        } else {
            $list = [];
            $nome = 1;
            $lat = 2;
            $lon = 3;
            while ($row = $result->fetch_row()) {
                $name = $row[$nome];
                $latitude = $row[$lat];
                $longitude = $row[$lon];

                $item = [
                    "NAME" => $name,
                    "LATITUDE" => $latitude,
                    "LONGITUDE" => $longitude
                ];

                $list[] = $item;
            }
            return $list;
        }

    }

}
