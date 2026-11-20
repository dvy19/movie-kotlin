package com.example.movie.community


data class PostRequest(
    var title:String,
    var content:String,
    var image:String?,
    var category:String

)



data class PostUser(
    var id:Int,
    var email:String
)

/* Movie Post Response
[
  {
    "id": 104,
    "user": 3,
    "image": "/media/post_images/past_lives_poster.jpg",
    "title": "Past Lives is Quietly Devastating",
    "content": "A film about the paths not taken and the versions of ourselves we leave behind in different countries and phases of life.",
    "category": "Indie Gem",
    "likes_count": 320,
    "comments_count": 51
  },
  {
    "id": 103,
    "user": 1,
    "image": null,
    "title": "Mad Max: Fury Road Still Holds the Crown for Practical Stunts",
    "content": "Rewatched it on the big screen last night. The sheer commitment to physical framing, real-world choreography, and minimal CGI...",
    "category": "Action / Thriller",
    "likes_count": 412,
    "comments_count": 65
  },
  {
    "id": 102,
    "user": 4,
    "image": "/media/post_images/boy_and_heron.jpg",
    "title": "The Boy and the Heron: Hayao Miyazaki's Poetic Farewell",
    "content": "A surreal, deeply personal journey through grief and imagination.",
    "category": "Animation",
    "likes_count": 189,
    "comments_count": 27
  },
  {
    "id": 101,
    "user": 2,
    "image": "/media/post_images/dune_two.jpg",
    "title": "Dune: Part Two — A Masterclass in Sound & Scale",
    "content": "Denis Villeneuve delivers one of the most immersive theatrical experiences of the decade.",
    "category": "Sci-Fi Review",
    "likes_count": 248,
    "comments_count": 42
  }
]
 */

data class PostResponse(
    val data:List<Post>
)

data class Post(
    var id:Int,
    var title:String,
    var content:String,
    var image:String?,
    var category:String,
    var likes_count:Int,
    var comments_count:Int,
    var user: PostUser
)