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
	$W_ID = $_POST["W_ID"];
	$status = $_POST["status"];
	$result['workers'] = array();
	
	$select= "SELECT cr.UID , U.Fname , U.Phno , cr.lat , cr.lng , U.rating ,U.Lname from requests as r inner join connect_to_requests as cr on r.ID = cr.ID  inner join users as U on U.UID = cr.UID where r.W_ID = '$W_ID'
	and cr.status != '$status'" ;
	
	$response = mysqli_query($conn,$select);
	
	while($row = mysqli_fetch_array($response))
		{
			$index['UID']  = $row['0'];
			$index['Fname']  = $row['1'];
			$index['Phno']  = $row['2'];
			$index['lat']  = $row['3'];
			$index['lng']  = $row['4'];
			$index['rating']  = $row['5'];
			$index['Lname']  =$row['6'];

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
