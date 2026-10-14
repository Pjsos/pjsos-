package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubtitleLine
import com.example.data.model.VocabularyItem
import com.example.ui.SubtitleLanguageMode
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.YouTubeRed

@Composable
fun SubtitleView(
    subtitles: List<SubtitleLine>,
    currentSeconds: Float,
    languageMode: SubtitleLanguageMode,
    onLanguageModeChange: (SubtitleLanguageMode) -> Unit,
    onSeekTo: (Float) -> Unit,
    onSpeakEnglish: (String) -> Unit,
    onSaveVocabulary: (VocabularyItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // Find active subtitle index
    val activeIndex = subtitles.indexOfFirst {
        currentSeconds >= it.startSeconds && currentSeconds <= it.endSeconds
    }

    // Smooth scroll to active line
    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0) {
            listState.animateScrollToItem(activeIndex)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Language Mode Tabs Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Subtitles,
                    contentDescription = null,
                    tint = YouTubeRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = "زیرنویس و ترجمه همزمان",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Mode Selector
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(2.dp)
            ) {
                SubtitleLanguageMode.values().forEach { mode ->
                    val isSelected = languageMode == mode
                    val label = when (mode) {
                        SubtitleLanguageMode.DUAL -> "دو زبانه"
                        SubtitleLanguageMode.PERSIAN_ONLY -> "فارسی"
                        SubtitleLanguageMode.ENGLISH_ONLY -> "English"
                    }
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) YouTubeRed else Color.Transparent,
                        modifier = Modifier.clickable { onLanguageModeChange(mode) }
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        if (subtitles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = "زیرنویسی برای این ویدیو یافت نشد. می‌توانید با دکمه ترجمه متن اختصاصی اضافه کنید.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subtitles, key = { it.id }) { line ->
                    val isActive = currentSeconds >= line.startSeconds && currentSeconds <= line.endSeconds
                    SubtitleItemCard(
                        line = line,
                        isActive = isActive,
                        languageMode = languageMode,
                        onSeekTo = { onSeekTo(line.startSeconds) },
                        onSpeakEnglish = { onSpeakEnglish(line.englishText) },
                        onCopyText = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Subtitle", "${line.englishText}\n${line.persianText}")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, "زیرنویس کپی شد", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SubtitleItemCard(
    line: SubtitleLine,
    isActive: Boolean,
    languageMode: SubtitleLanguageMode,
    onSeekTo: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onCopyText: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isActive) YouTubeRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        label = "border_color"
    )

    val cardBg = if (isActive) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSeekTo() }
            .border(
                width = if (isActive) 1.5.dp else 0.8.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Timestamp chip and action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timestamp badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isActive) YouTubeRed else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { onSeekTo() }
                ) {
                    Text(
                        text = line.formattedTimestamp,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Action Icons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Audio Pronunciation
                    IconButton(
                        onClick = onSpeakEnglish,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "تلفظ انگلیسی",
                            tint = if (isActive) ElectricCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Copy
                    IconButton(
                        onClick = onCopyText,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "کپی",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // English Text
            if (languageMode == SubtitleLanguageMode.DUAL || languageMode == SubtitleLanguageMode.ENGLISH_ONLY) {
                Text(
                    text = line.englishText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }

            if (languageMode == SubtitleLanguageMode.DUAL) {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Persian Translation (RTL)
            if (languageMode == SubtitleLanguageMode.DUAL || languageMode == SubtitleLanguageMode.PERSIAN_ONLY) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = line.persianText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isActive) YouTubeRed else MaterialTheme.colorScheme.secondary,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
