
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.movie.components.ChatUser
import com.example.movie.components.RecentChatsSection

// 1. Data Model for Communities
data class Community(
    val id: String,
    val name: String,
    val subtitle: String,
    val profileImageUrl: String
)

val sampleChatUsers = listOf(
    ChatUser(
        id = "1",
        name = "Sarah Connor",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/9FE5eD92WfVCiivM9Pq9GVSrlWk.jpg",
        lastText = "Are we still watching Inception tonight?",
        isSeen = false,
        timestamp = "10:42 AM"
    ),
    ChatUser(
        id = "2",
        name = "Movie Geek Dave",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/sl5NJ1vtyhP9Vr88bB4blULsJYp.jpg",
        lastText = "The Dark Knight soundtrack is elite 🎶",
        isSeen = true,
        timestamp = "Yesterday"
    ),
    ChatUser(
        id = "3",
        name = "Cinema Club",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/hZkgoQY85KGdfW28qW2vjf.jpg",
        lastText = "New sci-fi drop next Friday!",
        isSeen = false,
        timestamp = "2m ago"
    )
)
val sampleCommunities = listOf(
    Community(
        id = "c1",
        name = "Sci-Fi Fanatics",
        subtitle = "14.2k members",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/xJHokMbljvjADYdit5fK5VvG3io.jpg"
    ),
    Community(
        id = "c2",
        name = "Marvel Universe",
        subtitle = "32.8k members",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/8Y4PKa4aySuvhqebzqPRpE1sBl7.jpg"
    ),
    Community(
        id = "c3",
        name = "Indie Cinema",
        subtitle = "5.1k members",
        profileImageUrl = "https://image.tmdb.org/t/p/w200/9fqk2w423J41l3A87K.jpg"
    )
)

@Composable
fun ChatScreen(
    mainNavController: NavController,
    communities: List<Community>,
    chats: List<ChatUser>,
    onCommunityClick: (Community) -> Unit,
    onChatClick: (ChatUser) -> Unit,
    onViewAllChatsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Dark movie app background
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- PART 1: COMMUNITIES SECTION ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Section Header
                Text(
                    text = "Communities",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Horizontal LazyRow for Communities
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sampleCommunities, key = { it.id }) { community ->
                        CommunityCard(
                            community = community,
                            onClick = { onCommunityClick(community) }
                        )
                    }
                }
            }
        }

        // --- PART 2: CONVERSATIONS / RECENT CHATS SECTION ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Row with "View All"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Conversations",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "View All",
                        color = Color(0xFFFFC107), // Gold/Amber accent
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(onClick = onViewAllChatsClick)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Vertical Column of Conversations
                RecentChatsSection(
                    chats = sampleChatUsers,
                    onChatClick = onChatClick
                )
            }
        }
    }
}

// --- COMMUNITY CARD COMPONENT ---
@Composable
fun CommunityCard(
    community: Community,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Community Profile Image
            AsyncImage(
                model = community.profileImageUrl,
                contentDescription = community.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Community Name
            Text(
                text = community.name,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Subtitle
            Text(
                text = community.subtitle,
                color = Color(0xFFB0B0B0),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
