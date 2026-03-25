package com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.helper

enum class AnimationStepper {
    IDLE,
    SLIDING_IN,      // avatar + gun sliding
    GUN_FIRING,      // sound + fire effect
    TEXT_SHOWING,    // spoken text visible
    BULLETS_UPDATE,  // bullets decrease
    DONE             // navigate back
}