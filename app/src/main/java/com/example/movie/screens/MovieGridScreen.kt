package com.example.movie.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie.movie.Movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movie.movie.MovieVM
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.movie.movie.GetPopularMoviesState
import com.example.movie.movie.PopularMovie

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

data class FilterOption(
    val label: String,
    val code: String?
)

data class MovieFilterState(
    val language: FilterOption = FilterOption("All", null),
    val releaseYear: FilterOption = FilterOption("All", null),
    val includeAdult: Boolean = false
)

@Composable
fun MovieGridScreen(
    mainNavController: NavController,
    currentFilter: MovieFilterState,
    onApplyFilters: (MovieFilterState) -> Unit,
    onResetFilters: () -> Unit,

    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var submittedQuery by rememberSaveable { mutableStateOf("") }

    val vm: MovieVM = viewModel()
    val moviesState by vm.popularMoviesState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        vm.get_popular_movies(
            page = 1,
        )
    }

    val onQueryChange: (String) -> Unit = { newQuery ->
        searchQuery = newQuery
        if (newQuery.isBlank() && submittedQuery.isNotBlank()) {
            submittedQuery = ""
            vm.get_popular_movies(page = 1) // Restore popular movies when cleared
        }
    }

    val triggerSearch: () -> Unit = {
        val trimmed = searchQuery.trim()
        if (trimmed.isNotEmpty()) {
            submittedQuery = trimmed
            vm.search_movies(query = trimmed) // Writes into the same state
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }



    val languages = remember {
        listOf(
            FilterOption("All", null),
            FilterOption("English", "en"),
            FilterOption("Hindi", "hi"),
            FilterOption("Korean", "ko")
        )
    }

    val releaseYears = remember {
        listOf(
            FilterOption("All", null),
            FilterOption("2026", "2026"),
            FilterOption("2025", "2025"),
            FilterOption("2024", "2024")
        )
    }

    var selectedLanguage by remember(currentFilter) { mutableStateOf(currentFilter.language) }
    var selectedYear by remember(currentFilter) { mutableStateOf(currentFilter.releaseYear) }
    var includeAdult by remember(currentFilter) { mutableStateOf(currentFilter.includeAdult) }



    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF121212),
        topBar = {
            Surface(
                color = Color(0xFF121212),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search Bar + Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("Search movies...", color = Color.Gray)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color(0xFFE50914)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            searchQuery = ""
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Search",
                                            tint = Color.Gray
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = { triggerSearch() }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFE50914),
                                unfocusedBorderColor = Color(0xFF333333),
                                focusedContainerColor = Color(0xFF1E1E1E),
                                unfocusedContainerColor = Color(0xFF1E1E1E),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color(0xFFE50914)
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = triggerSearch,
                            enabled = searchQuery.isNotBlank(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE50914),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF2C2C2C),
                                disabledContentColor = Color.Gray
                            ),
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text("Search")
                        }
                    }

                    // Row 1: Language & Release Year Dropdowns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterDropdown(
                            label = "Language: ${selectedLanguage.label}",
                            options = languages,
                            onSelect = { selectedLanguage = it },
                            modifier = Modifier.weight(1f)
                        )

                        FilterDropdown(
                            label = "Year: ${selectedYear.label}",
                            options = releaseYears,
                            onSelect = { selectedYear = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: Adult Toggle + Reset & Apply Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = includeAdult,
                            onClick = { includeAdult = !includeAdult },
                            label = { Text("Include Adult") },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1E1E1E),
                                labelColor = Color.LightGray,
                                selectedContainerColor = Color(0xFFE50914).copy(alpha = 0.22f),
                                selectedLabelColor = Color.White
                            )
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val cleared = MovieFilterState()
                                    selectedLanguage = cleared.language
                                    selectedYear = cleared.releaseYear
                                    includeAdult = cleared.includeAdult
                                    onResetFilters()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.LightGray
                                ),
                                border = BorderStroke(1.dp, Color(0xFF444444))
                            ) {
                                Text("Reset")
                            }

                            Button(
                                onClick = {
                                    val newState = MovieFilterState(
                                        language = selectedLanguage,
                                        releaseYear = selectedYear,
                                        includeAdult = includeAdult
                                    )
                                    onApplyFilters(newState)

                                    vm.get_filter_movies(
                                        page = 1,
                                        language = "en-US",
                                        sortBy = "popularity.desc",
                                        originalLanguage = selectedLanguage.code,
                                        includeAdult = includeAdult,
                                        releaseYear = selectedYear.code?.toIntOrNull(),
                                        releaseDateGte = null,
                                        releaseDateLte = null
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE50914),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Apply")
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Pass `innerPadding` (e.g. Modifier.padding(innerPadding)) to your content container




        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Forces exactly 2 columns
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            when (val state = moviesState) {

                is GetPopularMoviesState.Error -> {
                    item{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Error: ${state.message}")
                        }
                    }
                }

                is GetPopularMoviesState.Loading -> {
                    item{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is GetPopularMoviesState.Empty -> {
                    item{
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (submittedQuery.isBlank()) "No movies to display"
                                else "No results found for \"$submittedQuery\""
                            )
                        }
                    }
                }

                is GetPopularMoviesState.Success -> {
                    val movies = state.data.results
                    // Render popular movies when submittedQuery.isBlank(),
                    // or matching search result card(s) when searching

                    items(movies, key = { it.id }) { movie ->
                        MovieGridCard(
                            onItemClick = {
                                mainNavController.navigate("movie_detail/${movie.id}")
                            },
                            movie = movie)
                    }
                }
            }

        }
    }
}

@Composable
fun MovieGridCard(
    onItemClick: (Movie) -> Unit = {},
    movie: PopularMovie
) {
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

@Composable
private fun FilterDropdown(
    label: String,
    options: List<FilterOption>,
    onSelect: (FilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, maxLines = 1)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}