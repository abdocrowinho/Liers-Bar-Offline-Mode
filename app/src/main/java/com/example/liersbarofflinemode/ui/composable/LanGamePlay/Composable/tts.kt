package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale


fun createTts(context: Context): TextToSpeech {
    var tts: TextToSpeech? = null
    tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            // Fix: "GB" is the correct ISO code, not "UK"
            val locale = Locale("en", "GB")
            val result = tts?.setLanguage(locale)

            // Fallback to US if GB not supported on this device
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }

            tts?.setPitch(0.1f)
            tts?.setSpeechRate(1.0f)

            val voice = tts?.voices?.find {
                it.locale.language == "en" &&
                        !it.name.contains("female", true) &&
                        (it.name.contains("en-gb", true) || it.name.contains("en-us", true))
            }
            voice?.let { tts?.voice = it }
        }
    }
    return tts
}