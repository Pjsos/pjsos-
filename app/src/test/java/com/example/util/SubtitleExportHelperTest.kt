package com.example.util

import com.example.data.model.SubtitleLine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SubtitleExportHelperTest {

    private val sampleSubtitles = listOf(
        SubtitleLine(1, 0f, 3.5f, "Hello everyone and welcome.", "سلام به همگی و خوش آمدید."),
        SubtitleLine(2, 4.0f, 7.8f, "Today we will learn Kotlin.", "امروز زبان کاتلین را یاد خواهیم گرفت.")
    )

    @Test
    fun `formatSrtTime formats correctly`() {
        assertEquals("00:00:03,500", SubtitleExportHelper.formatSrtTime(3.5f))
        assertEquals("00:01:05,250", SubtitleExportHelper.formatSrtTime(65.25f))
        assertEquals("01:00:00,000", SubtitleExportHelper.formatSrtTime(3600f))
    }

    @Test
    fun `buildSrtContent generates valid standard SRT format`() {
        val srt = SubtitleExportHelper.buildSrtContent(sampleSubtitles)
        assertTrue(srt.contains("1\n00:00:00,000 --> 00:00:03,500\nسلام به همگی و خوش آمدید."))
        assertTrue(srt.contains("2\n00:00:04,000 --> 00:00:07,800\nامروز زبان کاتلین را یاد خواهیم گرفت."))
    }

    @Test
    fun `buildPersianTxtContent includes timestamps and title`() {
        val txt = SubtitleExportHelper.buildPersianTxtContent("تست ویدیو", sampleSubtitles)
        assertTrue(txt.contains("ترجمه فارسی زیرنویس ویدیو: تست ویدیو"))
        assertTrue(txt.contains("[00:00] سلام به همگی و خوش آمدید."))
        assertTrue(txt.contains("[00:04] امروز زبان کاتلین را یاد خواهیم گرفت."))
    }

    @Test
    fun `buildDualTxtContent includes english and persian lines`() {
        val dual = SubtitleExportHelper.buildDualTxtContent("آموزش کاتلین", sampleSubtitles)
        assertTrue(dual.contains("EN: Hello everyone and welcome."))
        assertTrue(dual.contains("FA: سلام به همگی و خوش آمدید."))
    }
}
