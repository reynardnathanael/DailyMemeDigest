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

    $sql = "SELECT * from likes l inner join memes m on l.meme_id=m.meme_id inner join users u on l.user_id=u.user_id where l.meme_id = ? and l.user_id = ?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param('ii', $meme_id, $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $hasLike = false;
    if ($result->num_rows == 1) {
        $hasLike = true;
    }

    $message = "like";
    if ($hasLike) {
        $sql2 = "DELETE from likes where meme_id = ? and user_id = ?";
        $stmt2 = $c->prepare($sql2);
        $stmt2->bind_param('ii', $meme_id, $user_id);
        $stmt2->execute();

        $sql3 = "UPDATE memes set num_likes = num_likes - 1 where meme_id = ?";
        $stmt3 = $c->prepare($sql3);
        $stmt3->bind_param('i', $meme_id);
        $stmt3->execute();

        $message = "dislike";

    } else if (!$hasLike) {
        $sql2 = "INSERT into likes(meme_id, user_id) values(?,?)";
        $stmt2 = $c->prepare($sql2);
        $stmt2->bind_param('ii', $meme_id, $user_id);
        $stmt2->execute();

        $sql3 = "UPDATE memes set num_likes = num_likes + 1 where meme_id = ?";
        $stmt3 = $c->prepare($sql3);
        $stmt3->bind_param('i', $meme_id);
        $stmt3->execute();

        $message = "like";
    }
}


echo json_encode(["result" => "success", "message" => $message]);
die();
