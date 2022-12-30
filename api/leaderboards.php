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

$arr = [];

$sql = "SELECT u.firstname, u.lastname, u.avatar_img, sum(m.num_likes) as jumlah, u.privacy_setting FROM users u LEFT JOIN memes m ON u.user_id = m.user_id GROUP BY u.user_id ORDER BY jumlah desc";
$result = $c->query($sql);

$data = [];
$i = 0;
while ($r = $result->fetch_assoc()) {
    $data[$i]['firstname'] = $r['firstname'];
    if ($r['lastname'] == null) {
        $r['lastname'] = "";
    }
	$data[$i]['lastname'] = $r['lastname'];
	$data[$i]['avatar_img'] = $r['avatar_img'];
	if ($r['jumlah'] == null) {
        $r['jumlah'] = 0;
    }
    $data[$i]['jumlah'] = $r['jumlah'];
    $data[$i]['privacy_setting'] = $r['privacy_setting'];
    $i++;
}

echo json_encode(["result" => "success", "data" => $data]);
die();