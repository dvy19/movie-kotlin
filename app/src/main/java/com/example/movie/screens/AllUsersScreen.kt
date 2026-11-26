package com.example.movie.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie.auth.AuthVM
import com.example.movie.auth.GetAllUsersState
import com.example.movie.auth.User
import com.example.movie.community.CommViewModel
import com.example.movie.community.CreateChatRoomState
import com.example.movie.community.GetChatRoomState
import com.example.movie.community.InboxRes
import com.example.movie.community.Room


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUsersScreen(
    mainNavController: NavController,


) {

    /*
    val viewModel:AuthVM=viewModel()

    LaunchedEffect(Unit){
        viewModel.getAllUsers()
    }

    val getAllUsersState by viewModel.getAllUsersState.collectAsState()

     */

    val context=LocalContext.current

    val vm: CommViewModel=viewModel{
        CommViewModel(
            repo = com.example.movie.community.CommRepo(
                sessionManager = com.example.movie.auth.SessionManager(context)
            )
        )
    }

    val getChatRoomsState by vm.getChatRoomsState.collectAsState()

    LaunchedEffect(Unit) {
        vm.get_chat_rooms()
    }




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->

        when(val state=getChatRoomsState) {

            /*
            is GetAllUsersState.Idle -> {

            }
            is GetAllUsersState.Loading -> {

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No users available")
                }

            }

            is GetAllUsersState.Success -> {

                val users = state.data


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(users) { user ->
                            UserCard(
                                user = user ,
                                mainNavController

                                )
                        }
                    }


            }

            is GetAllUsersState.Error -> {

            }




        }
        */


            is GetChatRoomState.Idle->{

            }

            is GetChatRoomState.Loading->{

            }

            is GetChatRoomState.Success->{

                val data=state.data

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(data) { user ->
                        SimpleUserPairCard(
                            item= user ,
                            mainNavController

                        )
                    }
                }



            }

            is GetChatRoomState.Error->{

            }



        }
    }
}

@Composable
fun UserCard(
    user: User,
    mainNavController: NavController
) {

    val context= LocalContext.current

    val commViewModel: CommViewModel=viewModel{
        CommViewModel(
            repo = com.example.movie.community.CommRepo(
                sessionManager = com.example.movie.auth.SessionManager(context)
            )
        )
    }

    val createChatRoomState by commViewModel.createChatRoomState.collectAsState()

    when(val state=createChatRoomState){
        is CreateChatRoomState.Idle -> {

        }

        is CreateChatRoomState.Loading -> {

        }

        is CreateChatRoomState.Success -> {

            mainNavController.navigate("user_chat_screen/${user.user}")

        }

        is CreateChatRoomState.Error -> {

        }



    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Profile Avatar Container
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = user.profile,
                        contentDescription = "Profile picture of ${user.name}",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // User Information
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = user.mail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Message Button (Material 3 Icon or Filled Button)
            IconButton(
                onClick = {

                    commViewModel.create_chat_room(
                        com.example.movie.community.CreateRoomReq(
                            recipient_id = user.user
                        )
                    )

                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Message ${user.name}"
                )
            }
        }
    }

}

@Composable
fun SimpleUserPairCard(
    item: InboxRes,
    mainNavController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                mainNavController.navigate("user_chat_screen/${item.recipient_id}")
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${item.recipient_username} ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Updated: ${item.updated_at}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Created: ${item.created_at}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
