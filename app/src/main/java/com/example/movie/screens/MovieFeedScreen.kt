package com.example.movie.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movie.Screens
import com.example.movie.components.MoviePost
import com.example.movie.components.MoviePostCard

// 1. Parent Feed Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieFeedScreen(
    mainNavController: NavHostController,
    posts: List<MoviePost> = sampleMoviePosts,
    onCommentClick: (MoviePost) -> Unit = {},
    onLikeToggle: (MoviePost, Boolean) -> Unit = { _, _ -> },
    onNewPostClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CineFeed",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = {mainNavController.navigate(Screens.ChatScreen.route)}) {
                        Icon(
                            imageVector = Icons.Default.MailOutline, // Or Icons.Default.ChatBubbleOutline / Icons.Default.Send
                            contentDescription = "Message Inbox",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewPostClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create New Post"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = posts,
                key = { post -> post.id } // Stable keys for optimal LazyColumn recycling
            ) { post ->
                MoviePostCard(
                    post = post,
                    onCommentClick = { clickedPost -> onCommentClick(clickedPost) },
                    onLikeToggle = { targetPost, isLiked -> onLikeToggle(targetPost, isLiked) }
                )
            }
        }
    }
}

// 2. Sample Data Source containing 4 diverse movie posts
val sampleMoviePosts = listOf(
    MoviePost(
        id = 101,
        authorName = "Vikramaditya Nair",
        timeAgo = "2h ago",
        category = "Sci-Fi Review",
        title = "Dune: Part Two — A Masterclass in Sound & Scale",
        content = "Denis Villeneuve delivers one of the most immersive theatrical experiences of the decade. The sandworm sequence alone justifies watching it on the largest screen possible, backed by Hans Zimmer's thunderous score.",
        initialLikes = 248,
        commentCount = 42
    ),
    MoviePost(
        id = 102,
        authorName = "Ananya Iyer",
        timeAgo = "5h ago",
        category = "Animation",
        title = "The Boy and the Heron: Hayao Miyazaki's Poetic Farewell",
        content = "A surreal, deeply personal journey through grief and imagination. It may lack the strict narrative linearity of Spirited Away, but every single frame feels like a hand-painted watercolor masterpiece.",
        initialLikes = 189,
        commentCount = 27
    ),
    MoviePost(
        id = 103,
        authorName = "Rohan Gupta",
        timeAgo = "1d ago",
        category = "Action / Thriller",
        title = "Mad Max: Fury Road Still Holds the Crown for Practical Stunts",
        content = "Rewatched it on the big screen last night. The sheer commitment to physical framing, real-world choreography, and minimal CGI makes modern blockbuster action look weightless by comparison.",
        initialLikes = 412,
        commentCount = 65
    ),
    MoviePost(
        id = 104,
        authorName = "Priya Verma",
        timeAgo = "2d ago",
        category = "Indie Gem",
        title = "Past Lives is Quietly Devastating",
        content = "A film about the paths not taken and the versions of ourselves we leave behind in different countries and phases of life. Greta Lee and Teo Yoo's chemistry is electric without ever raising their voices.",
        initialLikes = 320,
        commentCount = 51
    )
)