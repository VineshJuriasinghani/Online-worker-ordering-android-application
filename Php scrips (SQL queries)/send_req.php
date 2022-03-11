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

$UID = $_POST["UID"];
$lat = $_POST["lat"];
$lng = $_POST["lng"];
$W_ID = $_POST["W_ID"];


$select= "select * from connect_to_requests as cr inner join requests r on cr.ID = r.ID where r.W_ID = '$W_ID' and 
	cr.UID = '$UID'";
	$response = mysqli_query($conn,$select);
		
		if($response-> num_rows>0)
		{
		    echo "request already sent";
		}
		else
		{
		   
			$select = "select * from connect_to_requests where UID = '$UID' and lat = '$lat' and lng = '$lng'";
			$response = mysqli_query($conn,$select);
			
			if($response-> num_rows>0)
		    {
		      
		    }
		    else
		    {
		        $sql = "INSERT INTO connect_to_requests (UID,lat,lng) VALUES
                ('$UID','$lat','$lng')"; 
                $result = mysqli_query($conn,$sql);
                

		    }
		        $sql2 = "select ID from connect_to_requests where UID = '$UID' and lat = '$lat' and lng = '$lng'";
                $result2 = mysqli_query($conn,$sql2);
                $row = mysqli_fetch_array($result2);
                
                $sql3 = "insert into requests (ID , W_ID) values ('$row[0]','$W_ID')";
                $result3 = mysqli_query($conn,$sql3);
                if($result3)
                {
                    echo "request sent";
                }
                else
                {
                    echo "Some error occured";
                }
		}
















?>