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

    if(isset($_POST['username']) && isset($_POST['password'])) {
        $username = $_POST['username'];
        $password = $_POST['password'];

        $c->set_charset("UTF8");
        $sql = "SELECT * FROM users WHERE username=? AND password=?";
        $stmt = $c->prepare($sql);
        $stmt->bind_param("ss", $username, $password);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($obj = $result->fetch_assoc()) {
            $arr = array(
                "result" => "success",
                "message" => "Login successfull!"
            );
        }
        else {
            $arr = array(
                "result" => "error",
                "message" => "Wrong username or password!"
            );
        }

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