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
 
    $W_ID=$_POST["W_ID"];
    $UID = $_POST["UID"];
    $lng = $_POST["lng"];
    $lat = $_POST["lat"];
	$status= $_POST["status"];
	$wstatus=$_POST["worker_working_status"];
	
	$select= "select * from requests as r inner join connect_to_requests as cr on r.ID = cr.ID where r.W_ID = '$W_ID' and 
	cr.lat = '$lat' and cr.lng = '$lng' and cr.UID = '$UID'" ;
	

	$response = mysqli_query($conn,$select);
	
	
	
		
		if($response-> num_rows>0)
		{
		    $select= "update requests as r inner join connect_to_requests as cr on r.ID = cr.ID 
		    inner join workers as w on w.W_ID = r.W_ID set cr.status = '$status' , w.status = '$wstatus' where r.W_ID = '$W_ID' and 
	        cr.lat = '$lat' and cr.lng = '$lng' and cr.UID = '$UID'" ;
	        $response = mysqli_query($conn,$select);
	        
	        
	        if($response)
	        {
	            $result["success"]="1";
	        }
	        else
		    {
		   	$result["success"]="0"; 
		    }
		}
		else
		{
		   	$result["success"]="0"; 
		}
		echo json_encode($result);
			
			mysqli_close($conn);

 ?>
