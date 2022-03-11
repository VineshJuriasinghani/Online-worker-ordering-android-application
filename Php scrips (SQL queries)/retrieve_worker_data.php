

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
	$category = $_POST["category"];
	$result['workers'] = array();
	$select= "select w.W_ID,w.lat,w.lng from workers as w inner JOIN connection as c on w.W_ID = c.W_ID INNER join categories cat on cat.CID = c.CID where cat.CName = '$category'";
	$response = mysqli_query($conn,$select);
	
	while($row = mysqli_fetch_array($response))
		{
			$index['W_ID']      =$row['0'];
            $index['lat']       =$row['1'];
            $index['lng']       =$row['2'];
		
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