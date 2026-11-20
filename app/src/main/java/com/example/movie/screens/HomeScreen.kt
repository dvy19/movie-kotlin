package com.example.movie.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movie.Screens
import com.example.movie.components.GenreHorizontalList
import com.example.movie.components.MovieVerticalList
import com.example.movie.movie.GetGenreState
import com.example.movie.movie.GetPopularMoviesState
import com.example.movie.movie.Movie
import com.example.movie.movie.MovieVM
import com.example.movie.movie.PopularMovie
import com.example.movie.room.CacheRepo
import com.example.movie.room.CacheViewModel
import com.example.movie.room.CacheVmFac
import com.example.movie.room.DatabaseProvider
import com.example.movie.room.GenreEntity
import com.example.movie.room.GetCacheGenreState
import kotlinx.coroutines.launch




val sampleMovies = listOf(
    PopularMovie(
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
        softcore = false,
        video = false,
        voteAverage = 8.5,
        voteCount = 37141
    ),
    PopularMovie(
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
        softcore = false,
        video = false,
        voteAverage = 7.9,
        voteCount = 1420
    ),
    PopularMovie(
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
        softcore = false,
        video = false,
        voteAverage = 6.9,
        voteCount = 3105
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainNavController: NavController
) {


    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val viewModel: MovieVM = viewModel()

    val getGenreState by viewModel.genreState.collectAsState()

    val popularMoviesState by viewModel.popularMoviesState.collectAsState()

    val context = LocalContext.current

    // Now LocalContext.current works because you are inside a Composable!
    val cacheViewModel: CacheViewModel = viewModel(
        factory = CacheVmFac(
            repository = CacheRepo(
                genreDao = DatabaseProvider.getDatabase(context).genreDao()
            )
        )
    )

    val genreState by cacheViewModel.getGenre.collectAsState()


    LaunchedEffect(Unit) {
        cacheViewModel.get_genre()
    }

    when(val state=genreState){
        is GetCacheGenreState.Loading -> {
            CircularProgressIndicator()
        }

        is GetCacheGenreState.Success -> {
            val genres = state.data

            if(genres.isEmpty()){
                LaunchedEffect(Unit) {
                    viewModel.get_genres()
                }
            }
        }
        is GetCacheGenreState.Error -> {
            Text(text = state.message)
        }
        else -> {

        }
    }

    LaunchedEffect(Unit) {



        viewModel.get_popular_movies()
    }





    val scope = rememberCoroutineScope()



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(240.dp),
                drawerContainerColor = Color.White
            ) {
                // Header spacing (or add a DrawerHeader here)
                Spacer(modifier = Modifier.height(16.dp))

                NavigationDrawerItem(
                    label = {
                        Text(
                            "Saved Facts",
                            color = Color.Black
                        )
                    },
                    selected = false,

                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.White,
                        selectedContainerColor = Color(0xFFE0E0E0), // Light gray when selected
                        unselectedTextColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedIconColor = Color.Black
                    ),
                    onClick = {
                        //mainNavController.navigate(Screens.SavedFactsScreen.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )


                NavigationDrawerItem(
                    label = {
                        Text(
                            "Your Plants",
                            color = Color.Black
                        )
                    },
                    selected = false,

                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.White,
                        selectedContainerColor = Color(0xFFE0E0E0), // Light gray when selected
                        unselectedTextColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedIconColor = Color.Black
                    ),
                    onClick = {
                        // mainNavController.navigate(Screens.YourPlantsScreen.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            "Plant Catalog",
                            color = Color.Black
                        )
                    },
                    selected = false,

                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.White,
                        selectedContainerColor = Color(0xFFE0E0E0), // Light gray when selected
                        unselectedTextColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedIconColor = Color.Black
                    ),
                    onClick = {
                        //mainNavController.navigate(Screens.PlantCatalogScreen.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            "Logout",
                            color = Color.Red
                        )
                    },
                    selected = false,

                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.White,
                        selectedContainerColor = Color(0xFFE0E0E0), // Light gray when selected
                        unselectedTextColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedIconColor = Color.Black
                    ),

                    onClick = {
                        //sessionManager.logout()

                        //mainNavController.navigate(Screens.GetStartScreen.route)

                        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                // Push the close button to the bottom of the drawer
                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    label = {
                        Text(
                            "Close",
                            color = Color.Black
                        )
                    },

                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.White,
                        selectedContainerColor = Color(0xFFE0E0E0), // Light gray when selected
                        unselectedTextColor = Color.Black,
                        selectedTextColor = Color.Black,
                        unselectedIconColor = Color.Black,
                        selectedIconColor = Color.Black
                    ),
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.heightIn(min = 56.dp),
                    title = { Text("HomeScreen") },
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

                item {
                    Text("Categories", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                item{

                    when(val state=getGenreState){

                        is GetGenreState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is GetGenreState.Success -> {

                            val genres = state.data

                            val genreEntities = genres.genres.map { movieGenre ->
                                GenreEntity(
                                    id = movieGenre.id,
                                    name = movieGenre.name
                                )
                            }

                            // 2. Pass the converted list to your cache ViewModel
                            cacheViewModel.insertGenres(genreEntities)


                            GenreHorizontalList(
                                genres = genres,

                                onGenreClick = {
                                    // Handle genre click here
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        is GetGenreState.Error -> {
                            Text(text = state.message)
                        }

                        else -> {

                        }



                }
                    }


                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Section Title
                        Text(
                            text = "Popular Movies",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // View All Action Button
                        TextButton(
                            onClick = {mainNavController.navigate(Screens.MovieGridScreen.route)},
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "View All",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    when(val state=popularMoviesState){
                        is GetPopularMoviesState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is GetPopularMoviesState.Success -> {
                            val movies = state.data
                            MovieVerticalList(
                                mainNavController,
                                movies = movies.results.take(6),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is GetPopularMoviesState.Error -> {
                            Text(text = state.message)
                        }
                        else -> {

                        }
                    }
                }



            }


        }


    }

}


