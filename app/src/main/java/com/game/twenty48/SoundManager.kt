package com.game.twenty48

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundManager(private val context: Context) {
    
    private var toneGenerator: ToneGenerator? = null
    private var vibrator: Vibrator? = null
    
    var isSoundEnabled = true
    
    init {
        initToneGenerator()
        initVibrator()
    }
    
    private fun initToneGenerator() {
        try {
            // Create ToneGenerator with music stream and higher volume (80%)
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun initVibrator() {
        try {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun playMoveSound() {
        if (isSoundEnabled) {
            try {
                // Short low beep for move (DTMF tone 1)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_1, 100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun playMergeSound() {
        if (isSoundEnabled) {
            try {
                // Higher pitched beep for merge (DTMF tone 5)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_5, 150)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun playWinSound() {
        if (isSoundEnabled) {
            try {
                // Happy success tone (DTMF tone 9)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_9, 400)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun playGameOverSound() {
        if (isSoundEnabled) {
            try {
                // Lower tone for game over (DTMF tone 0)
                toneGenerator?.startTone(ToneGenerator.TONE_DTMF_0, 300)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun playMilestoneSound(value: Int) {
        if (isSoundEnabled) {
            try {
                // Play a special tone sequence based on milestone value
                when (value) {
                    256 -> {
                        toneGenerator?.startTone(ToneGenerator.TONE_DTMF_4, 100)
                    }
                    512 -> {
                        toneGenerator?.startTone(ToneGenerator.TONE_DTMF_6, 150)
                    }
                    1024 -> {
                        toneGenerator?.startTone(ToneGenerator.TONE_DTMF_8, 200)
                    }
                    2048 -> {
                        // Victory fanfare - play multiple tones
                        toneGenerator?.startTone(ToneGenerator.TONE_DTMF_9, 150)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun vibrateMilestone(value: Int) {
        try {
            val duration = when (value) {
                256 -> 50L
                512 -> 100L
                1024 -> 150L
                2048 -> 300L
                else -> 50L
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(duration)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
    
    /*
     * FUTURE: To use sound files instead of ToneGenerator:
     * 
     * 1. Add sound files to app/src/main/res/raw/:
     *    - move.mp3
     *    - merge.mp3
     *    - win.mp3
     *    - gameover.mp3
     * 
     * 2. Replace ToneGenerator with SoundPool:
     *    private var soundPool: SoundPool? = null
     *    private var moveSound: Int = 0
     *    private var mergeSound: Int = 0
     *    // ... etc
     * 
     * 3. Load sounds in init:
     *    soundPool = SoundPool.Builder().setMaxStreams(4).build()
     *    moveSound = soundPool?.load(context, R.raw.move, 1) ?: 0
     *    mergeSound = soundPool?.load(context, R.raw.merge, 1) ?: 0
     *    // ... etc
     * 
     * 4. Update play methods:
     *    fun playMoveSound() {
     *        if (isSoundEnabled) {
     *            soundPool?.play(moveSound, 0.3f, 0.3f, 1, 0, 1.0f)
     *        }
     *    }
     */
}

