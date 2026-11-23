package com.example.movie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie.components.MovieVerticalList
import com.example.movie.movie.PopularMovie
import com.example.movie.room.CacheRepo
import com.example.movie.room.CacheViewModel
import com.example.movie.room.DatabaseProvider
import com.example.movie.room.GetSavedMovieState
import com.example.movie.room.MovieEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineSavedMovieScreen(
    mainNavController: NavController
){

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )


    val scope=rememberCoroutineScope()

    val context = LocalContext.current
    val cacheVm: CacheViewModel = viewModel {
        val db = DatabaseProvider.getDatabase(context)
        CacheViewModel(
            CacheRepo(
                genreDao = db.genreDao(),
                movieDao = db.movieDao()
            )
        )
    }

    val getMovieState by cacheVm.getMovie.collectAsState()

    LaunchedEffect(Unit) {
        val movies=cacheVm.get_movies()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.heightIn(min = 56.dp),
                title = { Text("Saved Movie") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open navigation drawer"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = { mainNavController.navigate("all_users")}) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "Messages"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Default.Cases,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


           when(val state=getMovieState){
               is GetSavedMovieState.Loading -> {
                   item{ CircularProgressIndicator() }
               }
               is GetSavedMovieState.Success -> {
                   items(
                       items = state.data,
                       key = { it.movie_id }
                   ) { movieEntity -> // movieEntity is a single MovieEntity
                       MovieCard(
                           onCardClick = { /* navigate using movieEntity.movie_id */ },
                           movie = movieEntity
                       )
                   }
               }
               is GetSavedMovieState.Error -> {
                   item { Text(text = state.message) }
               }
               else -> {

               }
           }


        }




        }


        }


@Composable
fun MovieCard(
    onCardClick : () -> Unit = {},
    movie: MovieEntity) {
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 1. Backdrop Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${movie.image}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Adult Badge (Only shows if adult = true)
                if (movie.adult) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Red.copy(alpha = 0.8f)
                    ) {
                        Text(
                            text = "18+",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 2. Movie Content Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Metadata Row: Release Date & Language
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Release: ${movie.releaseDate ?: "N/A"}",
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Lang: ${movie.originalLanguage.uppercase()}",
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Popularity & Additional Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔥 Popularity: ${String.format("%.1f", 0)}",
                        color = Color(0xFFFFC107), // Amber/Gold accent for stats
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "⭐ ${movie.voteAverage} (${movie.voteCount} votes)",
                        color = Color(0xFFB0B0B0),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}