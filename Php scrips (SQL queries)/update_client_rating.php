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
    $rating = $_POST["rating"];
	
	
	        $select = "select rating from users where UID = '$UID'";
	        $response = mysqli_query($conn,$select);
	        $row = mysqli_fetch_array($response);
	        
		    $select= "update users set rating = (('$rating'+'$row[0]')/2) where UID = '$UID'";
	        $response = mysqli_query($conn,$select);
	        
         
	        
	        if($response)
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
