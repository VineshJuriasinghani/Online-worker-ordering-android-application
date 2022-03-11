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
	$UID = $_POST["UID"];
	$result['requests'] = array();
	
	$select="select w.Fname , w.Phno , w.rating , cr.status ,w.W_ID, cr.lat, cr.lng from connect_to_requests as cr inner join requests as r on r.ID = cr.ID inner join workers as w on r.W_ID = w.W_ID where cr.UID = '$UID'";
	$response = mysqli_query($conn,$select);
	
	while($row = mysqli_fetch_array($response))
		{
			$index['Fname']  = $row['0'];
			$index['Phno']  = $row['1'];
			$index['rating']  = $row['2'];
			$index['status']  = $row['3'];
			$index['W_ID'] =$row['4'];
			$index['lat'] = $row['5'];
			$index['lng']=$row['6'];
	

			array_push($result['requests'], $index);
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
