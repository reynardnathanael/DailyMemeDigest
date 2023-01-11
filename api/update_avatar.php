<?php
error_reporting(0);
// $c = new mysqli("localhost", "native_160720034", "ubaya", "native_160720034");
$c = new mysqli("localhost", "root", "", "native_160720034");

if ($c->connect_errno) {
    // terjadi error
    $arr = array(
        "result" => "error",
        "message" => "failed to connect to db"
    );

    echo json_encode($arr);
    die();
}

if (isset($_POST['user_id']) && isset($_POST['avatar_img'])) {
    $user_id = $_POST['user_id'];
    $avatar_img = $_POST['avatar_img'];
    $img = str_replace('data:image/jpeg;base64,', '', $avatar_img);
    $img = str_replace(' ', '+', $img);
    $data = base64_decode($img);
    file_put_contents('img_profile/' . $user_id . '.jpg', $data);
    $avatar_img = 'https://ubaya.fun/native/160720034/memes_api/img_profile/' . $user_id . ".jpg";

    $c->set_charset("UTF8");
    $sql = "UPDATE users SET avatar_img=? WHERE user_id=?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("ss", $avatar_img, $user_id);
    $stmt->execute();

    $arr = array(
        "result" => "success",
        "message" => "Update successful!",
        "avatar" => $avatar_img,
    );

    echo json_encode($arr);
    die();
} else {
    $arr = array(
        "result" => "error",
        "message" => "Please check your input data!"
    );

    echo json_encode($arr);
    die();
}
