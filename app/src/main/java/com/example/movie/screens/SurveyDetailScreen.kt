package com.example.movie.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// --- UI Models (Replace with your domain/DTO models later) ---
data class SurveyUiModel(
    val id: String,
    val movieName: String,
    val title: String,
    val startDate: String,
    val endDate: String,
    val imageUrl: String,
    val description: String,
    val status: String, // e.g., "ACTIVE", "ENDED"
    val questions: List<SurveyQuestionUiModel>
)

data class SurveyQuestionUiModel(
    val id: String,
    val questionText: String,
    val choices: List<String> // Expected size: 4
)

// --- Parent Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailScreen(
    mainNavController: NavController,
    survey: SurveyUiModel = sampleSurveyData(),
    onBackClick: () -> Unit = {},
    onSubmitSurvey: (Map<Int, Int>) -> Unit = {}
) {
    // Tracks selected choice index (0..3) per question index (0..3)
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    val pagerState = rememberPagerState(pageCount = { survey.questions.size })
    val coroutineScope = rememberCoroutineScope()

    val isSurveyActive = survey.status.equals("ACTIVE", ignoreCase = true)
    val allQuestionsAnswered = selectedAnswers.size == survey.questions.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Community Survey",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Previous Question Button
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        enabled = pagerState.currentPage > 0,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous question",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Next or Submit Button
                    if (pagerState.currentPage < survey.questions.lastIndex) {
                        FilledTonalButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text("Next Question")
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = { onSubmitSurvey(selectedAnswers.toMap()) },
                            enabled = isSurveyActive && allQuestionsAnswered,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = if (isSurveyActive) "Submit Survey" else "Survey Closed",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Top Survey Details Section
            SurveyHeaderSection(survey = survey)

            // 2. Progress Indicator Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${pagerState.currentPage + 1} of ${survey.questions.size}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Step Dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(survey.questions.size) { index ->
                        val isCurrent = pagerState.currentPage == index
                        val isAnswered = selectedAnswers.containsKey(index)
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isCurrent) 22.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> MaterialTheme.colorScheme.primary
                                        isAnswered -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                        )
                    }
                }
            }

            // 3. Horizontal Movable Question Component
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) { pageIndex ->
                val question = survey.questions[pageIndex]
                QuestionCard(
                    questionNumber = pageIndex + 1,
                    question = question,
                    selectedChoiceIndex = selectedAnswers[pageIndex],
                    enabled = isSurveyActive,
                    onChoiceSelected = { choiceIdx ->
                        selectedAnswers[pageIndex] = choiceIdx
                    }
                )
            }
        }
    }
}

@Composable
private fun SurveyHeaderSection(survey: SurveyUiModel) {
    val isActive = survey.status.equals("ACTIVE", ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column {
            // Banner Image with Status Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                AsyncImage(
                    model = survey.imageUrl,
                    contentDescription = survey.movieName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Subtle bottom scrim for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f)),
                                startY = 80f
                            )
                        )
                )

                // Status Badge (Top-End)
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = CircleShape,
                    color = if (isActive) Color(0xFF1B5E20) else MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = survey.status.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Movie Name Tag (Bottom-Start)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = survey.movieName,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Text Details: Title, Dates, Description
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = survey.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${survey.startDate} – ${survey.endDate}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = survey.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuestionCard(
    questionNumber: Int,
    question: SurveyQuestionUiModel,
    selectedChoiceIndex: Int?,
    enabled: Boolean,
    onChoiceSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Q$questionNumber. ${question.questionText}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            question.choices.forEachIndexed { index, choiceText ->
                val isSelected = selectedChoiceIndex == index
                val borderColor = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
                val containerColor = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLowest
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = enabled) { onChoiceSelected(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = containerColor,
                    border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelected) {
                                Icons.Filled.CheckCircle
                            } else {
                                Icons.Outlined.RadioButtonUnchecked
                            },
                            contentDescription = null,
                            tint = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = choiceText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// --- Sample Preview Data ---
private fun sampleSurveyData() = SurveyUiModel(
    id = "survey_101",
    movieName = "Dune: Part Two",
    title = "Audience Verdict & Cinematography Poll",
    startDate = "Nov 18, 2026",
    endDate = "Nov 25, 2026",
    imageUrl = "https://image.tmdb.org/t/p/w780/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
    description = "Share your thoughts on Denis Villeneuve's visual storytelling, sound design, and character arcs with the community.",
    status = "ACTIVE",
    questions = listOf(
        SurveyQuestionUiModel(
            id = "q1",
            questionText = "Which element stood out the most in the film?",
            choices = listOf(
                "IMAX Cinematography & Visuals",
                "Hans Zimmer's Background Score",
                "Paul Atreides' Character Arc",
                "Action Choreography & Scale"
            )
        ),
        SurveyQuestionUiModel(
            id = "q2",
            questionText = "How would you rate the pacing across the 166-minute runtime?",
            choices = listOf(
                "Gripping from start to finish",
                "Slow build-up, explosive second half",
                "Slightly rushed in the final act",
                "Felt too long overall"
            )
        ),
        SurveyQuestionUiModel(
            id = "q3",
            questionText = "Which supporting performance impressed you most?",
            choices = listOf(
                "Rebecca Ferguson (Lady Jessica)",
                "Austin Butler (Feyd-Rautha)",
                "Javier Bardem (Stilgar)",
                "Zendaya (Chani)"
            )
        ),
        SurveyQuestionUiModel(
            id = "q4",
            questionText = "Would you rewatch it in theaters before Part Three?",
            choices = listOf(
                "Definitely in IMAX again",
                "Yes, standard screening",
                "Will rewatch at home on streaming",
                "Once was enough for me"
            )
        )
    )
)