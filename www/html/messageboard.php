<?php
$con=mysqli_connect("localhost","root","joojoo","messageboard");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
echo "Think it works? Try again.";
$username = $_POST["username"];
$text = $_POST["text"];
$latitude = $_POST["latitude"];
$longitude = $_POST["longitude"];

$query = "INSERT INTO message VALUES('$text','$username','$latitude','$longitude')";
if(mysqli_query($con,$query))
{
        //do nothing
                //eat a potato or something
}

mysqli_close($con);
?>


