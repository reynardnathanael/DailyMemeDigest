<?php
    error_reporting(0);
    $c = new mysqli("localhost", "root", "", "native_160720034");

    if($c->connect_errno) {
        // terjadi error
        $arr = array(
            "result" => "error",
            "message" => "failed to connect to db"
        );

        echo json_encode($arr);
        die();
    }

    if(isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['username']) && isset($_POST['password'])) {
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $username = $_POST['username'];
        $password = $_POST['password'];

        $c->set_charset("UTF8");
        if ($lastname == "") {
            $sql = "INSERT INTO users(username,firstname,password,registration_date,avatar_img,privacy_setting) VALUE(?,?,?,now(),'https://ubaya.fun/native/160720034/memes_api/img_profile/blankprofile.jpg',0)";
            $stmt = $c->prepare($sql);
            $stmt->bind_param("sss", $username, $firstname, $password);
        }
        else {
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

    else {
        $arr = array(
            "result" => "error",
            "message" => "please check your input data!"
        );

        echo json_encode($arr);
        die();
    }
?>