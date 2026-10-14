package com.example.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.US
        } else {
            Log.e("TtsManager", "TextToSpeech init failed with status: $status")
        }
    }

    fun speakEnglish(text: String) {
        if (!isInitialized) return
        tts?.language = Locale.US
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "eng_tts_${System.currentTimeMillis()}")
    }

    fun speakPersian(text: String) {
        if (!isInitialized) return
        // Try Persian locale if installed, otherwise fallback
        val persianLocale = Locale.forLanguageTag("fa-IR")
        val result = tts?.setLanguage(persianLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Persian voice not installed, fallback gracefully
            tts?.language = Locale.getDefault()
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "fa_tts_${System.currentTimeMillis()}")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
