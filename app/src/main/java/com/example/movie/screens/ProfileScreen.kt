package com.example.movie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
// Note: Ensure Coil is added to your dependencies if loading network images:
// implementation("io.coil-kt:coil-compose:2.6.0")
import coil.compose.AsyncImage
import com.example.movie.auth.SessionManager
import com.example.movie.community.CommRepo
import com.example.movie.community.CommVMFac
import com.example.movie.community.CommViewModel
import com.example.movie.community.GetMyPostsState
import com.example.movie.components.MoviePostCard

// Sample data classes matching typical user & post structures
data class UserProfile(
    val name: String,
    val username: String,
    val email: String,
    val profileImageUrl: String?
)

val sampleUser = UserProfile(
    name = "Aarav Sharma",
    username = "cinephile_aarav",
    email = "aarav.sharma@cinesphere.app",
    profileImageUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=400&q=80"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  ProfileScreen(
    user: UserProfile,

    onSettingsClick: () -> Unit,
    // Pass your existing MoviePostCard composable or invoke it directly inside items()
   mainNavController: NavHostController,
) {

    val context= LocalContext.current

    val viewModel: CommViewModel = viewModel(
        factory = CommVMFac(
            repository = CommRepo(SessionManager(context))
        )
    )


    val getMyPostsState by viewModel.getMyPostsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.get_my_posts(category = null)
    }

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFE0E0E0)
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFFE0E0E0)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212)
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Section: User Details
            item {
                UserHeaderSection(user = user)
            }



            // Scrollable User Posts Area


            when (val state = getMyPostsState) {
                is GetMyPostsState.Idle -> {

                }

                is GetMyPostsState.Loading -> {

                }

                is GetMyPostsState.Success -> {

                    val posts = state.data

                    // Section Divider & Feed Header
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            HorizontalDivider(
                                color = Color(0xFF2A2A2A),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text = "My Posts (${posts.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFFE0E0E0),
                                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                            )
                        }
                    }

                    if (posts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No movie posts yet.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }

                        items(
                            items = posts,
                            key = { post -> post.id }
                        ) { post ->
                            MoviePostCard(
                                post = post,
                                //mainNavController = mainNavController
                            )
                        }


                    }
                }

                    is GetMyPostsState.Error -> {

                    }


                }
            }
        }
    }



@Composable
private fun UserHeaderSection(user: UserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1E1E))
                .border(2.dp, Color(0xFFE50914), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!user.profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Avatar",
                    tint = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Full Name
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Username Handle
        Text(
            text = "@${user.username.removePrefix("@")}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFFE50914) // Cinematic red accent
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Email Address
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFE0E0E0).copy(alpha = 0.65f)
        )
    }
}