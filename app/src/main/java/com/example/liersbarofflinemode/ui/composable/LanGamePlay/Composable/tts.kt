package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale


fun createTts(context: Context): TextToSpeech {
    var tts : TextToSpeech? = null
     tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("en", "UK")
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