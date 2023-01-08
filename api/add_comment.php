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

if (isset($user_id) && isset($meme_id)) {
    $sql = "INSERT into meme_comments (user_id, meme_id, content, comment_date) values (?,?,?,?)";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("iiss", $user_id, $meme_id, $content, $comment_date);
    $stmt->execute();

    $arr = array(
        "result" => "success",
        "message" => "success adding comment"
    );

    echo json_encode($arr);
    die();
} else {
    $arr = array(
        "result" => "error",
        "message" => "error adding comment, Error sql: $sql"
    );

    echo json_encode($arr);
    die();
}
