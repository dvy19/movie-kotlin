package com.example.movie.components

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


data class ChatUser(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val lastText: String,
    val isSeen: Boolean,
    val timestamp: String
)
@Composable
fun RecentChatsSection(
    chats: List<ChatUser>,
    onChatClick: (ChatUser) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Adds spacing between cards
        ) {
            for (chatUser in chats) {
                UserChatCard(
                    chatUser = chatUser,
                    onClick = { onChatClick(chatUser) }
                )
            }
        }
    }
}


@Composable
fun UserChatCard(
    chatUser: ChatUser,
    onClick: () -> Unit
) {
    // Calculate half of the screen width minus padding offsets (if you want half-width,
    // change fillMaxWidth() on the card to width(cardWidth))
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = (screenWidth - 44.dp) / 2

    Card(
        modifier = Modifier
            .fillMaxWidth() // Change to .width(cardWidth) if you want half-width cards in this column
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Profile Image & Seen Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = chatUser.profileImageUrl,
                    contentDescription = chatUser.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )

                // Seen Status Badge (Gold dot = unread/unseen, Gray = seen)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (chatUser.isSeen) Color.Gray.copy(alpha = 0.5f) else Color(0xFFFFC107))
                )
            }

            // Bottom Content: Name & Last Text
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = chatUser.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = chatUser.lastText,
                    color = Color(0xFFB0B0B0),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}