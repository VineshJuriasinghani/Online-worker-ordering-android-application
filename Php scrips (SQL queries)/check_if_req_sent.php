
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

	$W_ID = $_POST["W_ID"];
	$UID = $_POST["UID"];
	 
	
	$select= "select * from connect_to_requests as cr inner join requests r on cr.ID = r.ID where r.W_ID = '$W_ID' and 
	cr.UID = '$UID'";
	$response = mysqli_query($conn,$select);
		
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