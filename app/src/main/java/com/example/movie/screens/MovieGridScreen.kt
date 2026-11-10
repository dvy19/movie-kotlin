package com.example.movie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie.movie.Movie


val sampleGridMovies = listOf(
    Movie(
        adult = false,
        backdropPath = "/9FE5eD92WfVCiivM9Pq9GVSrlWk.jpg",
        genreIds = listOf(28, 53, 80),
        id = 155,
        title = "The Dark Knight",
        originalLanguage = "en",
        originalTitle = "The Dark Knight",
        overview = "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets.",
        popularity = 4135.68,
        posterPath = "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
        releaseDate = "2008-07-16",
        //softcore = false,
        video = false,
        voteAverage = 8.5,
        voteCount = 37141
    ),
    Movie(
        adult = false,
        backdropPath = "/sl5NJ1vtyhP9Vr88bB4blULsJYp.jpg",
        genreIds = listOf(16, 12, 14),
        id = 791523,
        title = "Wildwood",
        originalLanguage = "en",
        originalTitle = "Wildwood",
        overview = "Prue McKeel and her best friend Curtis are drawn into a hidden, magical forest on the outskirts of Portland after her infant brother Mac is abducted by a murder of crows.",
        popularity = 1250.45,
        posterPath = "/jN1B3J4lWk9vVqY.jpg",
        releaseDate = "2025-10-08",
        //softcore = false,
        video = false,
        voteAverage = 7.9,
        voteCount = 1420
    ),
    Movie(
        adult = true,
        backdropPath = "/zO1p3v9qX67A2aM7N5u99.jpg",
        genreIds = listOf(18, 53),
        id = 49013,
        title = "Nymphomaniac: Vol. I",
        originalLanguage = "en",
        originalTitle = "Nymphomaniac: Vol. I",
        overview = "A self-diagnosed nymphomaniac recounts her erotic experiences to the man who saved her after a beating.",
        popularity = 890.12,
        posterPath = "/8bF7y.jpg",
        releaseDate = "2013-12-25",
        //softcore = false,
        video = false,
        voteAverage = 6.9,
        voteCount = 3105
    ),
    Movie(
        adult = false,
        backdropPath = "/hZkgoQY85KGdfW28qW2vjf.jpg",
        genreIds = listOf(28, 12, 878),
        id = 27205,
        title = "Inception",
        originalLanguage = "en",
        originalTitle = "Inception",
        overview = "Cobb, a skilled thief who steals corporate secrets through the use of dream-sharing technology, is given the inverse task of planting an idea into the mind of a C.E.O.",
        popularity = 3420.50,
        posterPath = "/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
        releaseDate = "2010-07-15",
        //softcore = false,
        video = false,
        voteAverage = 8.4,
        voteCount = 34500
    )
)

@Composable
fun MovieGridScreen(
    mainNavController: NavController,
    movies: List<Movie>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Forces exactly 2 columns
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Dark movie app background
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sampleGridMovies, key = { it.id }) { movie ->
            MovieGridCard(movie = movie)
        }
    }
}

@Composable
fun MovieGridCard(movie: Movie) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp), // Compact height optimized for a 2-column grid
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Backdrop Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.backdropPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Adult 18+ Badge (Only shows if adult = true)
                if (movie.adult) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Red.copy(alpha = 0.85f)
                    ) {
                        Text(
                            text = "18+",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // 2. Details Section (Title & Popularity)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title (limited to 2 lines to prevent layout distortion)
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Popularity Metric
                Text(
                    text = "🔥 ${String.format("%.1f", movie.popularity)}",
                    color = Color(0xFFFFC107), // Gold/Amber accent
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}