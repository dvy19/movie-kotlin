package com.example.movie.community


data class PostRequest(
    var title:String,
    var content:String,
    var image:String?,

)

data class PostResponse(
    var message:String,
    var data:Post
)

data class Post(
    var id:Int,
    var title:String,
    var content:String,
    var image:String?
)