package com.example.movie.community

import com.google.gson.annotations.SerializedName


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



/* Survey creation response
{
    "data": {
        "id": 1,
        "title": "Inception Rewatch Poll",
        "movie": "Inception",
        "rating": 9,
        "movie_image": null,
        "description": "Vote on your favorite elements, scenes, and ending interpretations for Inception.",
        "start_date": "2026-11-21T00:00:00Z",
        "end_date": "2026-11-28T23:59:59Z",
        "status": "published",
        "created_by": 3,
        "created_at": "2026-11-20",
        "survey_questions": [
            {
                "id": 1,
                "question_text": "Which dream level was your favorite?",
                "choice_1": "Level 1: The Rainy City",
                "choice_2": "Level 2: The Hotel Hallway",
                "choice_3": "Level 3: The Snow Fortress",
                "choice_4": "Level 4: Limbo",
                "order": 1
            },
            {
                "id": 2,
                "question_text": "Which score/track stands out the most?",
                "choice_1": "Time",
                "choice_2": "Dream Is Collapsing",
                "choice_3": "Mombasa",
                "choice_4": "528491",
                "order": 2
            },
            {
                "id": 3,
                "question_text": "What do you think happened at the very end?",
                "choice_1": "Cobb woke up in reality",
                "choice_2": "Cobb is still in a dream",
                "choice_3": "The totem doesn't matter—he sees his kids",
                "choice_4": "It's Mal's dream",
                "order": 3
            },
            {
                "id": 4,
                "question_text": "Which character had the strongest arc?",
                "choice_1": "Dom Cobb",
                "choice_2": "Arthur",
                "choice_3": "Ariadne",
                "choice_4": "Eames",
                "order": 4
            }
        ]
    }
}
 */

/* Single Survey Response

{
  "data": {
    "id": 1,
    "title": "Inception Rewatch Poll",
    "movie": "Inception",
    "rating": 9,
    "movie_image": null,
    "description": "Vote on your favorite elements, scenes, and ending interpretations for Inception.",
    "start_date": "2026-11-21T00:00:00Z",
    "end_date": "2026-11-28T23:59:59Z",
    "status": "published",
    "created_by": 1,
    "created_at": "2026-11-20",
    "survey_questions": [
      {
        "id": 1,
        "question_text": "Which dream level was your favorite?",
        "choice_1": "Level 1: The Rainy City",
        "choice_2": "Level 2: The Hotel Hallway",
        "choice_3": "Level 3: The Snow Fortress",
        "choice_4": "Level 4: Limbo",
        "order": 1
      },
      {
        "id": 2,
        "question_text": "Which score/track stands out the most?",
        "choice_1": "Time",
        "choice_2": "Dream Is Collapsing",
        "choice_3": "Mombasa",
        "choice_4": "528491",
        "order": 2
      },
      {
        "id": 3,
        "question_text": "What do you think happened at the very end?",
        "choice_1": "Cobb woke up in reality",
        "choice_2": "Cobb is still in a dream",
        "choice_3": "The totem doesn't matter—he sees his kids",
        "choice_4": "It's Mal's dream",
        "order": 3
      },
      {
        "id": 4,
        "question_text": "Which character had the strongest arc?",
        "choice_1": "Dom Cobb",
        "choice_2": "Arthur",
        "choice_3": "Ariadne",
        "choice_4": "Eames",
        "order": 4
      }
    ]
  }
}
 */

/* All Survey Response

{
  "data": [
    {
      "id": 1,
      "title": "Inception Rewatch Poll",
      "movie": "Inception",
      "rating": 9,
      "movie_image": null,
      "description": "Vote on your favorite elements, scenes, and ending interpretations for Inception.",
      "start_date": "2026-11-21T00:00:00Z",
      "end_date": "2026-11-28T23:59:59Z",
      "status": "published",
      "created_by": 1,
      "created_at": "2026-11-20",
      "survey_questions": [
        {
          "id": 1,
          "question_text": "Which dream level was your favorite?",
          "choice_1": "Level 1: The Rainy City",
          "choice_2": "Level 2: The Hotel Hallway",
          "choice_3": "Level 3: The Snow Fortress",
          "choice_4": "Level 4: Limbo",
          "order": 1
        }
      ]
    }
  ]
}
 */

data class AllSurveyResponse(
    val data:List<Survey>
)

data class Survey(

    var id:Int,
    var title:String,
    var movie:String,
    var rating:Int,
    var movie_image:String?,
    var description:String,
    var start_date:String,
    var end_date:String,
    var status:String,
    var created_by:Int,
    var created_at:String,
    var survey_questions:List<SurveyQuestion>

)

data class SurveyQuestion(
    var id:Int,
    var question_text:String,
    var choice_1:String,
    var choice_2:String,
    var choice_3:String,
    var choice_4:String,
    var order:Int
)

data class UserSurveyResponse(
    @SerializedName("question")
    val question: Int,

    @SerializedName("selected_choice")
    val selectedChoice: String?, // nullable in case not attended

    @SerializedName("has_attended")
    val hasAttended: Boolean
)

data class CreateRoomReq(
    var recipient_id:Int,
)

/*
{
    "message": "Chat room created",
    "created": true,
    "room": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "user1": "divya",
        "user2": "rahul",
        "created_at": "2026-11-26T12:00:00Z",
        "updated_at": "2026-11-26T12:00:00Z"
    }
}
 */
data class CreateRoomRes(

    var message:String,
    var created:Boolean,
    var room:Room


)

data class Room(
    var id:String,
    var user1:String,
    var user2:String,
    var created_at:String,
    var updated_at:String

)

/*
 {
        "room_id": "550e8400-e29b-41d4-a716-446655440000",
        "recipient_id": 5,
        "recipient_username": "rahul",
        "created_at": "2026-11-25T10:00:00Z",
        "updated_at": "2026-11-26T12:00:00Z"
    },
 */
data class InboxRes(
    var room_id:String,
    var recipient_id:Int,
    var recipient_username:String,
    var created_at:String,
    var updated_at:String

)