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

if (isset($_POST['username'])) {

    $username = $_POST['username'];

    $c->set_charset("UTF8");
    $sql = "SELECT * from memes as m inner join users as u on m.user_id=u.user_id where username = ? order by meme_id desc";
    $stmt = $c->prepare($sql);
    $stmt->bind_param('s', $username);
    $stmt->execute();
    $result = $stmt->get_result();

    $data = [];
    while ($r = $result->fetch_object()) {
        array_push($data, $r);
    }

    echo json_encode(["result" => "success", "data" => $data]);
    die();
} else {
    echo json_encode(["result" => "error", "message" => "Error sql: $sql"]);
    die();
}
