package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.Spellcheck
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.TranslatorViewModel
import com.example.ui.components.SubtitleView
import com.example.ui.components.VideoSelectorRow
import com.example.ui.components.YouTubePlayerView
import com.example.ui.theme.YouTubeRed

import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Person
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TranslatorViewModel,
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currentVideo by viewModel.currentVideo.collectAsStateWithLifecycle()
    val currentSeconds by viewModel.currentPlaybackSeconds.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val playbackSpeed by viewModel.playbackSpeed.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val subtitleMode by viewModel.subtitleLanguageMode.collectAsStateWithLifecycle()
    val searchUrlInput by viewModel.searchUrlInput.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchError by viewModel.searchError.collectAsStateWithLifecycle()
    val textTranslateInput by viewModel.textTranslateInput.collectAsStateWithLifecycle()
    val aiTranslateOutput by viewModel.aiTranslateOutput.collectAsStateWithLifecycle()
    val isAiTranslating by viewModel.isAiTranslating.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val savedVideos by viewModel.savedVideos.collectAsStateWithLifecycle()
    val savedWords by viewModel.savedWords.collectAsStateWithLifecycle()
    val isSyncingCloud by viewModel.isSyncingCloud.collectAsStateWithLifecycle()

    val currentUser = Firebase.auth.currentUser
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = YouTubeRed,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = "مترجم یوتیوب",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "متصل به فایربیس و ابر گوگل",
                                    fontSize = 11.sp,
                                    color = YouTubeRed,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                },
                actions = {
                    // Cloud Sync Indicator
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isSyncingCloud) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                contentDescription = "وضعیت ابری",
                                tint = if (isSyncingCloud) YouTubeRed else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSyncingCloud) "همگام‌سازی..." else "فایربیس فعال",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Logout Action
                    IconButton(
                        onClick = onSignOut,
                        modifier = Modifier.testTag("btn_logout")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "خروج از حساب",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    val tabs = listOf(
                        Triple(0, "ویدیو و زیرنویس", Icons.Filled.SmartDisplay to Icons.Outlined.SmartDisplay),
                        Triple(1, "ترجمه متن", Icons.Filled.Translate to Icons.Outlined.Translate),
                        Triple(2, "خلاصه و واژگان", Icons.Filled.AutoAwesome to Icons.Filled.AutoAwesome),
                        Triple(3, "تاریخچه من", Icons.Filled.Bookmark to Icons.Outlined.BookmarkBorder)
                    )

                    tabs.forEach { (index, title, iconPair) ->
                        val isSelected = selectedTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(index) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) iconPair.first else iconPair.second,
                                    contentDescription = title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = YouTubeRed,
                                selectedTextColor = YouTubeRed,
                                indicatorColor = YouTubeRed.copy(alpha = 0.15f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_tab_$index")
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    // Video & Subtitles Screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        // Video selector row and YouTube Link Input
                        VideoSelectorRow(
                            sampleVideos = viewModel.sampleVideos,
                            selectedVideoId = currentVideo.id,
                            onSelectVideo = { viewModel.selectVideo(it) },
                            searchUrlInput = searchUrlInput,
                            onSearchUrlChange = { viewModel.setSearchUrlInput(it) },
                            onLoadUrl = { viewModel.loadVideoFromInput() },
                            isSearching = isSearching,
                            searchError = searchError
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Video Player
                        YouTubePlayerView(
                            video = currentVideo,
                            currentSeconds = currentSeconds,
                            isPlaying = isPlaying,
                            playbackSpeed = playbackSpeed,
                            onTogglePlay = { viewModel.togglePlayPause() },
                            onSeek = { viewModel.seekTo(it) },
                            onSpeedChange = { viewModel.changePlaybackSpeed(it) },
                            onSpeakCurrentEnglish = {
                                val activeLine = currentVideo.subtitles.firstOrNull {
                                    currentSeconds >= it.startSeconds && currentSeconds <= it.endSeconds
                                }
                                if (activeLine != null) {
                                    viewModel.speakEnglish(activeLine.englishText)
                                } else if (currentVideo.subtitles.isNotEmpty()) {
                                    viewModel.speakEnglish(currentVideo.subtitles.first().englishText)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Synchronized Dual Subtitles
                        SubtitleView(
                            subtitles = currentVideo.subtitles,
                            currentSeconds = currentSeconds,
                            languageMode = subtitleMode,
                            onLanguageModeChange = { viewModel.setSubtitleLanguageMode(it) },
                            onSeekTo = { viewModel.seekTo(it) },
                            onSpeakEnglish = { viewModel.speakEnglish(it) },
                            onSaveVocabulary = { viewModel.saveVocabulary(it) },
                            videoTitle = currentVideo.title,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                1 -> {
                    // Text Translator Tab
                    TextTranslatorTab(
                        inputText = textTranslateInput,
                        onInputChange = { viewModel.setTextTranslateInput(it) },
                        onTranslate = { viewModel.translateDirectText() },
                        isLoading = isAiTranslating,
                        output = aiTranslateOutput,
                        onSpeakEnglish = { viewModel.speakEnglish(it) },
                        onSpeakPersian = { viewModel.speakPersian(it) },
                        onSaveWord = { viewModel.saveVocabulary(it) }
                    )
                }

                2 -> {
                    // AI Summary & Vocabulary Tab
                    AiSummaryTab(
                        video = currentVideo,
                        onSaveVideo = { viewModel.saveCurrentVideo() },
                        onSaveWord = { viewModel.saveVocabulary(it) },
                        onSpeakEnglish = { viewModel.speakEnglish(it) }
                    )
                }

                3 -> {
                    // History & Wordbook Tab
                    SavedHistoryTab(
                        savedVideos = savedVideos,
                        savedWords = savedWords,
                        onSelectVideoId = { id ->
                            val found = viewModel.sampleVideos.firstOrNull { it.id == id }
                            if (found != null) {
                                viewModel.selectVideo(found)
                                viewModel.selectTab(0)
                            } else {
                                viewModel.setSearchUrlInput("https://www.youtube.com/watch?v=$id")
                                viewModel.loadVideoFromInput()
                                viewModel.selectTab(0)
                            }
                        },
                        onDeleteVideo = { viewModel.deleteSavedVideo(it) },
                        onDeleteWord = { id, word -> viewModel.deleteVocabulary(id, word) },
                        onSpeakWord = { viewModel.speakEnglish(it) }
                    )
                }
            }
        }
    }
}
