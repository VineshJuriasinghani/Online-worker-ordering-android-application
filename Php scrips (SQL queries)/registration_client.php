<?php
$servername = "localhost";
$username = "id18079796_mazdoor";
$database = "id18079796_mazdoor1";
$password = "2Z1xVY<|W]>ja[*c";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$fname = $_POST["Fname"];
$lname = $_POST["Lname"];
$cnic = $_POST["Cnic"];
$phno = $_POST["Phno"];
$password = $_POST["password"];

$sql = "INSERT INTO users (Fname,Lname,Cnic,Phno,password) VALUES
('$fname','$lname','$cnic','$phno','$password')"; 

$result = mysqli_query($conn,$sql);

if($result)
{
echo "registered successfully";
}
else
{
echo "CNIC or Phone No: already exist";
}



?>