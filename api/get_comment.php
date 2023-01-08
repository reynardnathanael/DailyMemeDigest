<?php
error_reporting(0);
$c = new mysqli("localhost", "root", "", "native_160720034");
$arr = [];

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

extract($_POST);

if (isset($meme_id)) {
    $sql = "SELECT * from meme_comments mc inner join users u on mc.user_id=u.user_id where mc.meme_id = ?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("i", $meme_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $data = [];
    while($r = $result->fetch_assoc()){
        array_push($data, $r);
    }

    $arr = array(
        "result" => "success",
        "data" => $data,
        "sql" => $sql
    );

    echo json_encode($arr);
    die();
}
else {
    $arr = array(
        "result" => "error",
        "message" => "Error sql = $sql"
    );

    echo json_encode($arr);
    die();
}
