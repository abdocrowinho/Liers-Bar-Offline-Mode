package com.example.liersbarofflinemode.ui.Utltiy

import android.content.Context
import android.media.SoundPool
import com.example.liersbarofflinemode.R
import dagger.hilt.android.qualifiers.ApplicationContext

class SoundPlayer(@ApplicationContext context: Context) {
   private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
private val soundId : Int = soundPool.load(context, R.raw.button_sound,1)
    fun playClickSound() {
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }

}