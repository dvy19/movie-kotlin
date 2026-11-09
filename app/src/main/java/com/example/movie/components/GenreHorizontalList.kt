package com.example.movie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


data class Genre(
    val id: Int,
    val name: String,
    val imageUrl: String // Image representing the genre
)

val sampleGenres = listOf(
    Genre(
        id = 28,
        name = "Action",
        imageUrl = "https://image.tmdb.org/t/p/w500/3V4kSt8aL9DODvhwIQLD me standard/9FE5eD92WfVCiivM9Pq9GVSrlWk.jpg" // Dark Knight backdrop
    ),
    Genre(
        id = 12,
        name = "Adventure",
        imageUrl = "https://image.tmdb.org/t/p/w500/8Y4PKa4aySuvhqebzqPRpE1sBl7.jpg"
    ),
    Genre(
        id = 16,
        name = "Animation",
        imageUrl = "https://image.tmdb.org/t/p/w500/sl5NJ1vtyhP9Vr88bB4blULsJYp.jpg"
    ),
    Genre(
        id = 35,
        name = "Comedy",
        imageUrl = "https://image.tmdb.org/t/p/w500/2Lqa15A8G2y8w0a88ld2wL4.jpg"
    ),
    Genre(
        id = 80,
        name = "Crime",
        imageUrl = "https://image.tmdb.org/t/p/w500/hZkgoQY85KGdfW28qW2vjf.jpg"
    ),
    Genre(
        id = 18,
        name = "Drama",
        imageUrl = "https://image.tmdb.org/t/p/w500/9fqk2w423J41l3A87K.jpg"
    ),
    Genre(
        id = 27,
        name = "Horror",
        imageUrl = "https://image.tmdb.org/t/p/w500/7I928f5A93K7S421L8.jpg"
    ),
    Genre(
        id = 878,
        name = "Sci-Fi",
        imageUrl = "https://image.tmdb.org/t/p/w500/xJHokMbljvjADYdit5fK5VvG3io.jpg"
    ),
    Genre(
        id = 53,
        name = "Thriller",
        imageUrl = "https://image.tmdb.org/t/p/w500/vL5LR6VwqZFCrT0m2n82p.jpg"
    )
)

@Composable
fun GenreHorizontalList(
    genres: List<Genre>,
    onGenreClick: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        items(genres, key = { it.id }) { genre ->
            GenreCard(
                genre = genre,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}

@Composable
fun GenreCard(
    genre: Genre,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(Color(0xFF1E1E24)) // Dark surface background fallback
    ) {
        // 1. Genre Background Image
        AsyncImage(
            model = genre.imageUrl,
            contentDescription = genre.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // 2. Dark Gradient Overlay (ensures text readability over bright images)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        ),
                        startY = 30f
                    )
                )
        )

        // 3. Genre Name Text
        Text(
            text = genre.name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        )
    }
}