<?php
$servername = "localhost";
$username = "id18079796_mazdoor";
$database = "id18079796_mazdoor1";
$password = "2Z1xVY<|W]>ja[*c";

// Create connection
$conn = new mysqli($servername, $username, $password,$database);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$qry = "Alter table connection add foreign key (W_ID) references workers (W_ID);";
$result = mysqli_query($conn,$qry);
if($result)
echo "success";
else
echo "failed";
?>