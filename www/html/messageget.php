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

$query = "select text, username, latitude, longitude from message";
if($result = mysqli_query($con,$query))
{
        while($row = mysqli_fetch_row($result))
        {
                echo"<h2><a href='https://www.google.fi/maps/@".$row[3].",".$row[2].",15z\'>$row[0]</a></h2>";
                echo"<h8>".$row[1]."</h3>";
        }
}

mysqli_close($con);
?>



