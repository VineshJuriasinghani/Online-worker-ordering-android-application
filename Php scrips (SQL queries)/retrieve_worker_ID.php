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
    $result = array();
	$phno = $_POST["Phno"];
    $password = $_POST["password"];
	$result['workers'] = array();
	$select= "SELECT * from workers where Phno = '$phno' and password = '$password'";
	$response = mysqli_query($conn,$select);
	
	while($row = mysqli_fetch_array($response))
		{
			$index['W_ID']  = $row['0'];

			array_push($result['workers'], $index);
		}
		
		if($response-> num_rows>0)
		{
			$result["success"]="1";
		}
		else
		{
		   	$result["success"]="0"; 
		}
		echo json_encode($result);
			
			mysqli_close($conn);

 ?>
