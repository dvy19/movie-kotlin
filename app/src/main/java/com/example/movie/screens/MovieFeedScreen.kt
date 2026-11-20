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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.movie.Screens
import com.example.movie.auth.SessionManager
import com.example.movie.community.CommRepo
import com.example.movie.community.CommVMFac
import com.example.movie.community.CommViewModel
import com.example.movie.community.GetAllPostsState
import com.example.movie.components.MoviePostCard

// 1. Parent Feed Screen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieFeedScreen(
    mainNavController: NavHostController,
    onNewPostClick: () -> Unit = {}
) {


    val context= LocalContext.current
    val viewModel: CommViewModel = viewModel(
        factory = CommVMFac(
            repository = CommRepo(SessionManager(context))
        )
    )

    LaunchedEffect(Unit) {
        viewModel.get_all_posts(
            category = null
        )
    }


    val getAllPostsState by viewModel.getAllPostsState.collectAsState()



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
            when (val state = getAllPostsState) {
                is GetAllPostsState.Idle -> {
                    // Optional: show nothing or a subtle prompt while waiting to fetch
                }

                is GetAllPostsState.Loading -> {
                    item(key = "loading") {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is GetAllPostsState.Success -> {
                    val posts = state.data.data // Use `state.data` if you switched Retrofit to List<Post>

                    if (posts.isEmpty()) {
                        item(key = "empty") {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No movie posts found.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(
                            items = posts,
                            key = { post -> post.id }
                        ) { post ->
                            MoviePostCard(
                                post = post,

                            )
                        }
                    }
                }

                is GetAllPostsState.Error -> {
                    item(key = "error") {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = state.message, // Adjust to your Error property name
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {}) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }

    }
}