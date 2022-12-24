<?php
error_reporting(0);
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
if (isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['username']) && isset($_POST['password'])) {
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $username = $_POST['username'];
    $password = $_POST['password'];

    // check if the username is already taken or not
    $checkSql = "SELECT * FROM users WHERE username=?";
    $chectStmt = $c->prepare($checkSql);
    $chectStmt->bind_param("s", $username);
    $chectStmt->execute();
    $result = $chectStmt->get_result();

    if ($obj = $result->fetch_object()) {
        $arr = array(
            "result" => "error",
            "message" => "Username already taken!"
        );

        echo json_encode($arr);
        die();
    } else {
        // if lastname is empty...
        if ($lastname == "") {
            $sql = "INSERT INTO users(username,firstname,password,registration_date,avatar_img,privacy_setting) VALUE(?,?,?,now(),'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg',0)";
            $stmt = $c->prepare($sql);
            $stmt->bind_param("sss", $username, $firstname, $password);
        } else {
            $sql = "INSERT INTO users(username,firstname,lastname,password,registration_date,avatar_img,privacy_setting) VALUE(?,?,?,?,now(),'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg',0)";
            $stmt = $c->prepare($sql);
            $stmt->bind_param("ssss", $username, $firstname, $lastname, $password);
        }

        $stmt->execute();

        $arr = array(
            "result" => "success",
            "message" => "Registration successful!"
        );

        echo json_encode($arr);
        die();
    }
} else {
    $arr = array(
        "result" => "error",
        "message" => "please check your input data!"
    );

    echo json_encode($arr);
    die();
}
