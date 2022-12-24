package com.reynard.dailymemedigest

data class Meme(val meme_id: Int, val image_url: String, val top_text: String, val bottom_text: String, var num_likes: Int, val username: String, val avatar_img: String)
