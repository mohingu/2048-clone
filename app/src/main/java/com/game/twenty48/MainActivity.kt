package com.game.twenty48

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var scoreText: TextView
    private lateinit var bestScoreText: TextView
    private lateinit var newGameButton: Button
    private lateinit var gameManager: GameManager
    private lateinit var soundManager: SoundManager
    private lateinit var milestoneTracker: MilestoneTracker

    private var bestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)
        scoreText = findViewById(R.id.scoreText)
        bestScoreText = findViewById(R.id.bestScoreText)
        newGameButton = findViewById(R.id.newGameButton)

        // Initialize sound manager and milestone tracker
        soundManager = SoundManager(this)
        milestoneTracker = MilestoneTracker(this)

        gameManager = GameManager(
            onStateChanged = {
                updateUI()
                checkMilestones()
                checkGameState()
            },
            soundManager = soundManager
        )

        gameView.setGameManager(gameManager)
        newGameButton.setOnClickListener {
            gameManager.restart()
            milestoneTracker.reset()
            updateUI()
        }

        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }

    private fun updateUI() {
        scoreText.text = gameManager.score.toString()
        
        if (gameManager.score > bestScore) {
            bestScore = gameManager.score
            bestScoreText.text = bestScore.toString()
        }
        
        gameView.invalidate()
    }
    
    private fun checkMilestones() {
        // Check all tiles for milestones
        gameManager.getTiles().forEach { tile ->
            if (tile.value in listOf(256, 512, 1024, 2048)) {
                milestoneTracker.checkMilestone(tile.value)
                // Animate milestone tiles
                if (tile.mergedFrom != null) {
                    gameView.animateMilestoneTile(tile)
                }
            }
        }
    }

    private fun checkGameState() {
        when {
            gameManager.won -> {
                showDialog("You Win!", "Congratulations! You reached 2048!")
            }
            gameManager.over -> {
                showDialog("Game Over", "No more moves available. Final score: ${gameManager.score}")
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("New Game") { _, _ ->
                gameManager.restart()
                updateUI()
            }
            .setCancelable(false)
            .show()
    }
}

