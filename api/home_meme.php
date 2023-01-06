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
$order = $_POST['order'];
$sql = "SELECT m.meme_id, m.image_url, m.top_text, m.bottom_text, m.num_likes, u.username, u.avatar_img from memes m 
inner join users u on m.user_id=u.user_id left join meme_comments mc on m.meme_id = mc.meme_id 
group by m.meme_id order by $order desc";
$result = $c->query($sql);

$data = [];
while ($r = $result->fetch_assoc()) {
    array_push($data, $r);
}

echo json_encode(["result" => "success", "data" => $data]);
die();