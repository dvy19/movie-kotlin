package com.example.movie.movie

import com.google.gson.annotations.SerializedName


/* get all  genre response
{
  "genres": [
    {
      "id": 28,
      "name": "Action"
    },
    {
      "id": 12,
      "name": "Adventure"
    },
    {
      "id": 16,
      "name": "Animation"
    },
    {
      "id": 35,
      "name": "Comedy"
    },
    {
      "id": 80,
      "name": "Crime"
    },
    {
      "id": 99,
      "name": "Documentary"
    },
    {
      "id": 18,
      "name": "Drama"
    },
    {
      "id": 10751,
      "name": "Family"
    },
    {
      "id": 14,
      "name": "Fantasy"
    },
    {
      "id": 36,
      "name": "History"
    },
    {
      "id": 27,
      "name": "Horror"
    },
    {
      "id": 10402,
      "name": "Music"
    },
    {
      "id": 9648,
      "name": "Mystery"
    },
    {
      "id": 10749,
      "name": "Romance"
    },
    {
      "id": 878,
      "name": "Science Fiction"
    },
    {
      "id": 10770,
      "name": "TV Movie"
    },
    {
      "id": 53,
      "name": "Thriller"
    },
    {
      "id": 10752,
      "name": "War"
    },
    {
      "id": 37,
      "name": "Western"
    }
  ]
}
 */

data class MovieGenreResponse(
    val genres: List<MovieGenre>
)

data class MovieGenre(
    val id: Int,
    val name: String
)

data class MovieByGenreResponse(
    @SerializedName("genre_id")
    val genreId: Int, // Change to String if your genre_id is passed as a string
    val query: String,
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    val results: List<Movie>
)

data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)

/* popular movie response

{
  "page": 1,
  "results": [
    {
      "adult": false,
      "backdrop_path": "/yE766yhgg98tKwTKd8CyNvAtmn6.jpg",
      "genre_ids": [
        12,
        53
      ],
      "id": 1263337,
      "title": "Heart of the Beast",
      "original_language": "en",
      "original_title": "Heart of the Beast",
      "overview": "After a harrowing plane crash, Special Forces officer James Belmont and his combat dog, Odin, find themselves stranded deep in the Alaskan wilderness. Together, they are forced into a brutal fight for survival against the elements.",
      "popularity": 507.5043,
      "poster_path": "/6sYemVUn79UPKz1ZsSG6kPU4RVy.jpg",
      "release_date": "2026-09-19",
      "softcore": false,
      "video": false,
      "vote_average": 7.696,
      "vote_count": 973
    },
    {
      "adult": false,
      "backdrop_path": "/c7btGYblBnfAaw713WvFM0Kimc7.jpg",
      "genre_ids": [
        12,
        28,
        14
      ],
      "id": 1368337,
      "title": "The Odyssey",
      "original_language": "en",
      "original_title": "The Odyssey",
      "overview": "Odysseus, the legendary King of Ithaca, embarks on a long and perilous journey home following the Trojan War. Throughout his voyage, he is forced to confront the whims of gods, mythological monsters, and trials that stretch both his cunning and his humanity to the breaking point.",
      "popularity": 478.6101,
      "poster_path": "/5rhTDKUhPYvpdQIijFIs5VoWsON.jpg",
      "release_date": "2026-07-15",
      "softcore": false,
      "video": false,
      "vote_average": 8.024,
      "vote_count": 5040
    },
 ]
}

 */

data class PopularMovieResponse(
    val page:Int,
    val results: List<PopularMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class PopularMovie(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    val title: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val softcore: Boolean, // Included from your sample response
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)