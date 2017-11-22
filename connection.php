<?php
$username="id12345_studoob";  
$password="12345678";  //account password
$host = "localhost";  
$dbname="id12345_studoob";

    $connection = mysqli_connect($host,$username,$password,$dbname) or die("Error " . mysqli_error($connection));
  if(! $connection ) {
            die('Could not connect: ' . mysql_error());
    }
    echo 'Connected successfully';
     mysqli_close($connection);
?>