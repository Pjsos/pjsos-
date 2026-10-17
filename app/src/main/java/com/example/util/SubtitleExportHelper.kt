package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.SubtitleLine
import java.io.OutputStream

object SubtitleExportHelper {

    enum class ExportFormat(val titleFa: String, val extension: String, val mimeType: String) {
        SRT("فایل زیرنویس استاندارد (SRT)", "srt", "application/x-subrip"),
        TXT_PERSIAN("فقط متن ترجمه فارسی (TXT)", "txt", "text/plain"),
        TXT_DUAL("متن دو زبانه انگلیسی و فارسی (TXT)", "txt", "text/plain")
    }

    /**
     * Converts seconds to SRT timestamp format: 00:01:20,000
     */
    fun formatSrtTime(seconds: Float): String {
        val totalMillis = (seconds * 1000).toLong().coerceAtLeast(0L)
        val hours = totalMillis / (3600 * 1000)
        val remAfterHours = totalMillis % (3600 * 1000)
        val minutes = remAfterHours / (60 * 1000)
        val remAfterMinutes = remAfterHours % (60 * 1000)
        val secs = remAfterMinutes / 1000
        val millis = remAfterMinutes % 1000
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, secs, millis)
    }

    /**
     * Builds standard SubRip (.srt) content from subtitles.
     */
    fun buildSrtContent(subtitles: List<SubtitleLine>, includeDual: Boolean = false): String {
        val sb = StringBuilder()
        subtitles.forEachIndexed { index, line ->
            sb.append("${index + 1}\n")
            sb.append("${formatSrtTime(line.startSeconds)} --> ${formatSrtTime(line.endSeconds)}\n")
            if (includeDual) {
                sb.append("${line.englishText}\n")
                sb.append("${line.persianText}\n\n")
            } else {
                sb.append("${line.persianText}\n\n")
            }
        }
        return sb.toString()
    }

    /**
     * Builds plain text (.txt) content containing only Persian translated subtitles.
     */
    fun buildPersianTxtContent(videoTitle: String, subtitles: List<SubtitleLine>): String {
        val sb = StringBuilder()
        sb.append("ترجمه فارسی زیرنویس ویدیو: $videoTitle\n")
        sb.append("تولید شده توسط مترجم هوشمند یوتیوب\n")
        sb.append("=====================================\n\n")

        subtitles.forEach { line ->
            sb.append("[${line.formattedTimestamp}] ${line.persianText}\n\n")
        }
        return sb.toString()
    }

    /**
     * Builds plain text (.txt) content containing dual English and Persian subtitles.
     */
    fun buildDualTxtContent(videoTitle: String, subtitles: List<SubtitleLine>): String {
        val sb = StringBuilder()
        sb.append("زیرنویس دو زبانه (انگلیسی و فارسی): $videoTitle\n")
        sb.append("=====================================\n\n")

        subtitles.forEach { line ->
            sb.append("[${line.formattedTimestamp}]\n")
            sb.append("EN: ${line.englishText}\n")
            sb.append("FA: ${line.persianText}\n\n")
        }
        return sb.toString()
    }

    /**
     * Generates export text according to the selected format.
     */
    fun generateContent(
        format: ExportFormat,
        videoTitle: String,
        subtitles: List<SubtitleLine>
    ): String {
        return when (format) {
            ExportFormat.SRT -> buildSrtContent(subtitles, includeDual = false)
            ExportFormat.TXT_PERSIAN -> buildPersianTxtContent(videoTitle, subtitles)
            ExportFormat.TXT_DUAL -> buildDualTxtContent(videoTitle, subtitles)
        }
    }

    /**
     * Shares the translated subtitles to other apps (Telegram, WhatsApp, Notes, etc.) via Android Sharesheet.
     */
    fun shareSubtitles(
        context: Context,
        format: ExportFormat,
        videoTitle: String,
        subtitles: List<SubtitleLine>
    ) {
        val textToShare = generateContent(format, videoTitle, subtitles)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "زیرنویس ترجمه شده: $videoTitle")
            putExtra(Intent.EXTRA_TEXT, textToShare)
        }
        val chooser = Intent.createChooser(shareIntent, "اشتراک‌گذاری زیرنویس در...")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    /**
     * Writes content directly into a file Uri obtained via CreateDocument SAF contract.
     */
    fun writeToFileUri(context: Context, uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
                outputStream.write(content.toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
