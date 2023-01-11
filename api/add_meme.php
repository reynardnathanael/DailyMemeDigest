<?php
error_reporting(0);
// $c = new mysqli("localhost", "native_160720034", "ubaya", "native_160720034");
$c = new mysqli("localhost", "root", "", "native_160720034");

// check if connection success or not
if ($c->connect_errno) {
    // terjadi error
    $arr = array(
        "result" => "error",
        "message" => "failed to connect to db"
    );

    echo json_encode($arr);
    die();
}

$c->set_charset("UTF8");

// check if there's any data posted from the application
if (isset($_POST['image_url']) && isset($_POST['top_text']) && isset($_POST['bottom_text']) && isset($_POST['user_id'])) {
    $image_url = $_POST['image_url'];
    $top_text = $_POST['top_text'];
    $bottom_text = $_POST['bottom_text'];
    $user_id = $_POST['user_id'];

    $sql = "INSERT INTO memes(image_url,top_text,bottom_text,user_id) VALUE(?,?,?,?)";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("ssss", $image_url, $top_text, $bottom_text, $user_id);

    $stmt->execute();

    $arr = array(
        "result" => "success",
        "message" => "Adding meme successful!"
    );

    echo json_encode($arr);
    die();
} else {
    $arr = array(
        "result" => "error",
        "message" => "please check your input data!"
    );

    echo json_encode($arr);
    die();
}
