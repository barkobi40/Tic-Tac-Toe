package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {

    private lateinit var board: Array<Array<TextView>>
    private lateinit var gameStatus: TextView
    private lateinit var playAgainButton: Button

    private var currentPlayer = "X"
    private var winner: String? = null
    private var boardState = Array(3) { Array(3) { "" } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        gameStatus = findViewById(R.id.game_status)
        playAgainButton = findViewById(R.id.play_again_button)
        val gridLayout: GridLayout = findViewById(R.id.board)

        // Map board cells
        board = Array(3) { row ->
            Array(3) { col ->
                val cellId = resources.getIdentifier("cell_${row}${col}", "id", packageName)
                gridLayout.findViewById(cellId)
            }
        }

        // Set up click listeners for each cell
        for (row in 0..2) {
            for (col in 0..2) {
                board[row][col].setOnClickListener {
                    if (boardState[row][col].isEmpty() && winner == null) {
                        boardState[row][col] = currentPlayer
                        board[row][col].text = currentPlayer
                        winner = checkWinner(boardState)
                        if (winner != null) {
                            if (winner != "Draw") {
                                try {
                                    MediaPlayer.create(this, R.raw.win_sound)?.apply {
                                        setOnCompletionListener { release() }
                                        start()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                gameStatus.text = "Player $winner Wins!"
                            } else {
                                gameStatus.text = "It's a Draw!"
                            }
                        } else {
                            currentPlayer = if (currentPlayer == "X") "O" else "X"
                            gameStatus.text = "Player $currentPlayer's Turn"
                        }
                    }
                }
            }
        }

        // Set up the Play Again button
        playAgainButton.setOnClickListener {
            resetGame()
        }
    }

    private fun resetGame() {
        boardState = Array(3) { Array(3) { "" } }
        for (row in 0..2) {
            for (col in 0..2) {
                board[row][col].text = ""
            }
        }
        currentPlayer = "X"
        winner = null
        gameStatus.text = "Player X's Turn"
    }

    private fun checkWinner(board: Array<Array<String>>): String? {
        for (i in 0..2) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0].isNotEmpty())
                return board[i][0]
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i].isNotEmpty())
                return board[0][i]
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0].isNotEmpty())
            return board[0][0]
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2].isNotEmpty())
            return board[0][2]

        if (board.all { row -> row.all { it.isNotEmpty() } }) return "Draw"

        return null
    }
}
