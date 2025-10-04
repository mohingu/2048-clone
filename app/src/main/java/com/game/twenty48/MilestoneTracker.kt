package com.game.twenty48

import android.content.Context
import android.widget.Toast

class MilestoneTracker(private val context: Context) {
    
    private val achievedMilestones = mutableSetOf<Int>()
    
    fun checkMilestone(value: Int) {
        if (value in listOf(256, 512, 1024, 2048) && !achievedMilestones.contains(value)) {
            achievedMilestones.add(value)
            showMilestoneToast(value)
        }
    }
    
    private fun showMilestoneToast(value: Int) {
        val message = when (value) {
            256 -> "🎉 Awesome! You reached 256!"
            512 -> "🔥 Fantastic! You reached 512!"
            1024 -> "⭐ Amazing! You reached 1024!"
            2048 -> "🏆 INCREDIBLE! You reached 2048!"
            else -> "Great job!"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    
    fun reset() {
        achievedMilestones.clear()
    }
}

