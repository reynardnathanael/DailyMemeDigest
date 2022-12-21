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

    if(isset($_POST['username']) && isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['privacy_setting'])) {
        $username = $_POST['username'];
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $privacy_setting = $_POST['privacy_setting'];

        $c->set_charset("UTF8");
        $sql = "UPDATE users SET firstname=?, lastname=?, privacy_setting=? WHERE username=?";
        $stmt = $c->prepare($sql);
        $stmt->bind_param("ssi", $username, $password, $privacy_setting);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($obj = $result->fetch_assoc()) {
            $arr = array(
                "result" => "success",
                "message" => "Update successfull!"
            );
        }
        else {
            $arr = array(
                "result" => "error",
                "message" => "Username not found!"
            );
        }

        echo json_encode($arr);
        die();
    }

    else {
        $arr = array(
            "result" => "error",
            "message" => "Please check your input data!"
        );

        echo json_encode($arr);
        die();
    }
?>