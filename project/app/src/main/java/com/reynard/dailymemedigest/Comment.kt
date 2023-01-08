package com.reynard.dailymemedigest

data class Comment(val comment_id: Int, val user_id: Int, val meme_id: Int, val content: String, var comment_date: String, val username: String, val avatar_img: String)
