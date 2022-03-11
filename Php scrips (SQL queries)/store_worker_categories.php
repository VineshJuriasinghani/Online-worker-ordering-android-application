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

$Phno = $_POST["Phno"];
$skill = $_POST["skill"];


$select = "select W_ID from workers where Phno = '$Phno'";
$result = mysqli_query($conn,$select);
$row = mysqli_fetch_array($result);

if($result)
{
$select = "select CID from categories where CName = '$skill'";
$result1 = mysqli_query($conn,$select);
$row1 = mysqli_fetch_array($result1);

if($result1)
{
$sql = "INSERT INTO connection (CID,W_ID) VALUES
('$row1[0]','$row[0]')"; 
}
else
{
echo "CNIC or Phone No: already exist";
}
}
else
{
echo "CNIC or Phone No: already exist";
}






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