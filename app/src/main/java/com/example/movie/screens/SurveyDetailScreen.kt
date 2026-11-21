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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.movie.auth.SessionManager
import com.example.movie.community.CommRepo
import com.example.movie.community.CommVMFac
import com.example.movie.community.CommViewModel
import com.example.movie.community.GetAllSurveysState
import com.example.movie.community.Survey
import com.example.movie.community.SurveyQuestion
import com.example.movie.community.UserSurveyResponse
import kotlinx.coroutines.launch


data class SurveyAnswerPayload(
    val question: Int,
    val selected_choice: String?,
    val has_attended: Boolean
)

private fun buildSurveyPayload(
    questions: List<SurveyQuestion>,
    selectedAnswers: Map<Int, Int>
): List<UserSurveyResponse> {
    return questions.mapIndexed { qIndex, question ->
        val choiceIndex = selectedAnswers[qIndex]
        val choices = listOf(
            question.choice_1,
            question.choice_2,
            question.choice_3,
            question.choice_4
        )
        UserSurveyResponse(
            question = question.id, // or qIndex + 1 if your API expects 1..4 index
            selectedChoice = choiceIndex?.let { choices[it] }, // actual choice text, or null if skipped
            hasAttended = choiceIndex != null
        )
    }
}



@Composable
fun SurveyDetailScreen(
    mainNavController: NavController
){

    val context = LocalContext.current
    val commViewModel: CommViewModel= viewModel(
        factory = CommVMFac(
            repository = CommRepo(
                sessionManager = SessionManager(context)
            )
        )
    )

    val getAllSurveysState by commViewModel.getAllSurveysState.collectAsState()

    LaunchedEffect(Unit) {
        commViewModel.get_all_surveys()
    }

    when(val state=getAllSurveysState){
        is GetAllSurveysState.Loading -> {
            CircularProgressIndicator()
        }
        is GetAllSurveysState.Success -> {
            SurveyDetailLayout(
                survey = state.data.data[0]
            )
        }
        is GetAllSurveysState.Error -> {
            Text(text = state.message)
        }
        else -> {

        }
    }

}
// --- Parent Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyDetailLayout(
    survey: Survey,
    onBackClick: () -> Unit = {},
    onSubmitSurvey: (Map<Int, Int>) -> Unit = {}
) {


    val context = LocalContext.current
    val commViewModel: CommViewModel= viewModel(
        factory = CommVMFac(
            repository = CommRepo(
                sessionManager = SessionManager(context)
            )
        )
    )

    // Tracks selected choice index (0..3) per question index (0..3)
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    val pagerState = rememberPagerState(pageCount = { survey.survey_questions.size })
    val coroutineScope = rememberCoroutineScope()

    val isSurveyActive = survey.status.equals("published", ignoreCase = true)
    val allQuestionsAnswered = selectedAnswers.size == survey.survey_questions.size

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
                    if (pagerState.currentPage < survey.survey_questions.lastIndex) {
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
                            onClick = {

                                    val payload = buildSurveyPayload(
                                        selectedAnswers = selectedAnswers,
                                        questions = survey.survey_questions
                                    )
                                    commViewModel.user_survey_response(payload)

                                      },
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
                    text = "Question ${pagerState.currentPage + 1} of ${survey.survey_questions.size}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Step Dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(survey.survey_questions.size) { index ->
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
                val question = survey.survey_questions[pageIndex]
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
private fun SurveyHeaderSection(survey: Survey) {
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
                    model = survey.movie_image,
                    contentDescription = survey.movie,
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
                        text = survey.movie,
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
                        text = "${survey.start_date} – ${survey.end_date}",
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
    question: SurveyQuestion,
    selectedChoiceIndex: Int?,
    enabled: Boolean,
    onChoiceSelected: (Int) -> Unit
) {

    val choices=listOf(question.choice_1 , question.choice_2 , question.choice_3 , question.choice_4)
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
                text = "Q$questionNumber. ${question.question_text}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )



            choices.forEachIndexed { index, choiceText ->
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
private fun sampleSurveyData() = Survey(
    id = 101,
    movie = "Dune: Part Two",
    title = "Audience Verdict & Cinematography Poll",
    start_date = "Nov 18, 2026",
    end_date = "Nov 25, 2026",
    movie_image = "https://image.tmdb.org/t/p/w780/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg",
    description = "Share your thoughts on Denis Villeneuve's visual storytelling, sound design, and character arcs with the community.",
    status = "ACTIVE",
    survey_questions = listOf(
        SurveyQuestion(
            id = 1,
            question_text = "Which element stood out the most in the film?",

            choice_1 = "IMAX Cinematography & Visuals",
            choice_2 = "Hans Zimmer's Background Score",
            choice_3 = "Paul Atreides' Character Arc",
            choice_4 = "Action Choreography & Scale",
            order = 1

        ),
        SurveyQuestion(
            id =2,
            question_text = "How would you rate the pacing across the 166-minute runtime?",

                choice_1 = "Gripping from start to finish",
            choice_2 ="Slow build-up, explosive second half",
            choice_3 ="Slightly rushed in the final act",
            choice_4 = "Felt too long overall",
            order=2

        ),
        SurveyQuestion(
            id = 3,
            question_text = "Which supporting performance impressed you most?",

                choice_1 = "Rebecca Ferguson (Lady Jessica)",
            choice_2 ="Austin Butler (Feyd-Rautha)",
            choice_3 ="Javier Bardem (Stilgar)",
            choice_4= "Zendaya (Chani)",
            order = 3

        ),
        SurveyQuestion(
            id =4,
            question_text = "Would you rewatch it in theaters before Part Three?",

               choice_1 =  "Definitely in IMAX again",
                choice_2 = "Yes, standard screening",
               choice_3 =  "Will rewatch at home on streaming",
               choice_4 =  "Once was enough for me",
                order=4

        )
    ),
    rating = TODO(),
    created_by = TODO(),
    created_at = TODO()
)