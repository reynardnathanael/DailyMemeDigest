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
    $sql = "UPDATE memes SET num_views = num_views + 1 WHERE meme_id = ?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("i", $meme_id);
    $stmt->execute();

    $arr = array(
        "result" => "success",
        "message" => "success adding view"
    );

    echo json_encode($arr);
    die();
}
else {
    $arr = array(
        "result" => "error",
        "message" => "error adding view"
    );

    echo json_encode($arr);
    die();
}
