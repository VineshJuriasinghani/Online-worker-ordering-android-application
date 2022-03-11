
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
	$result['workers'] = array();
	
	$select= "select w.Fname,w.Lname,w.Phno,w.rating,w.status,cat.CName from workers as w inner JOIN connection as c on w.W_ID = c.W_ID INNER join categories as cat on cat.CID = c.CID
	where w.W_ID = '$W_ID'";
	
	$response = mysqli_query($conn,$select);
	
	while($row = mysqli_fetch_array($response))
		{
			$index['Fname']     =$row['0'];
			$index['Lname']     =$row['1'];
			$index['Phno']      =$row['2'];
			$index['rating']    =$row['3'];
            $index['status']    =$row['4'];
            $index['CName']     =$row['5'];
		
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