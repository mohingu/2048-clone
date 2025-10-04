package com.game.twenty48

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var scoreText: TextView
    private lateinit var bestScoreText: TextView
    private lateinit var newGameButton: Button
    private lateinit var gameManager: GameManager

    private var bestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)
        scoreText = findViewById(R.id.scoreText)
        bestScoreText = findViewById(R.id.bestScoreText)
        newGameButton = findViewById(R.id.newGameButton)

        gameManager = GameManager(onStateChanged = {
            updateUI()
            checkGameState()
        })

        gameView.setGameManager(gameManager)
        newGameButton.setOnClickListener {
            gameManager.restart()
            updateUI()
        }

        updateUI()
    }

    private fun updateUI() {
        scoreText.text = gameManager.score.toString()
        
        if (gameManager.score > bestScore) {
            bestScore = gameManager.score
            bestScoreText.text = bestScore.toString()
        }
        
        gameView.invalidate()
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

