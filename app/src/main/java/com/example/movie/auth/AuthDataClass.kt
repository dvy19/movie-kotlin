package com.example.movie.auth


data class RegisterReq(

    val email:String,
    val password:String,
    var role:String="user"
)

/*
{
  "message": "Registration successful",
  "role": "customer",
  "tokens": {
    "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCJ9.example_refresh_sig",
    "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIn0.example_access_sig"
  }
}
 */

data class RegisterResponse(
    val message:String,
    val role:String,
    val tokens:Tokens

)

data class Tokens(
    val refresh:String,
    val access:String
)

data class LoginReq(
    val email:String,
    var password:String
)


data class UserProfileReq(
    var name:String,
    var gender:String,
    var profile:String?,
    var city:String
)

data class USerProfileRes(
    var message:String,
    var data:User
)

data class User(
    var name:String,
    var gender:String,
    var profile:String?,
    var city:String,
    var mail:String
)



/* all user details

[
    {
        "city": "delhi",
        "gender": "male",
        "user": 25,
        "profile": null,
        "name": "divy",
        "mail": "ahana099@gmail.com"
    },
    {
        "city": "delhi",
        "gender": "male",
        "user": 26,
        "profile": null,
        "name": "div dixit eofny",
        "mail": "user67@gmail.com"
    }
]
 */