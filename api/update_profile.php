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

    if(isset($_POST['user_id']) && isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['privacy_setting'])) {
        $user_id = $_POST['user_id'];
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $privacy_setting = $_POST['privacy_setting'];

        $c->set_charset("UTF8");
        $sql = "UPDATE users SET firstname=?, lastname=?, privacy_setting=? WHERE user_id=?";
        $stmt = $c->prepare($sql);
        $stmt->bind_param("ssss", $firstname, $lastname, $privacy_setting, $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $arr = array(
            "result" => "success",
            "message" => "Update successful!"
        );

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