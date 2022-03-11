
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
	$lat = $_POST["lat"];
	$lng = $_POST["lng"];
	 
	
	$select= "select cr.ID from connect_to_requests as cr inner join requests as r on cr.ID = r.ID where r.W_ID = '$W_ID' and 
	cr.UID = '$UID' and cr.lat = '$lat' and cr.lng = '$lng'";
	$response = mysqli_query($conn,$select);

		
		if($response-> num_rows>0)
		{
		    	$row = mysqli_fetch_array($response);
            	$select= "select * from requests  where ID = '$row[0]'";
            	$response1 = mysqli_query($conn,$select);
            	
            	$select ="delete r from connect_to_requests as cr inner join requests as r on cr.ID = r.ID where r.W_ID ='$W_ID' and cr.UID = '$UID' and cr.lat = '$lat' and cr.lng = '$lng'";
            	$response = mysqli_query($conn,$select);
            	
            	
            	if($response1-> num_rows <2)
            	{
            	    $select= "delete cr from connect_to_requests as cr where cr.UID = '$UID' and cr.lat = '$lat' and cr.lng = '$lng'";
            	$response = mysqli_query($conn,$select);
            	}
    
		    
		        if($response)
		        {
			        $result["success"]="1";
		        }
		    
		        else
		        {
		   	        $result["success"]="-1"; 
		        }
		    
		}
		else
		{
		   	$result["success"]="0"; 
		}
			echo json_encode($result);
			mysqli_close($conn);

 ?>