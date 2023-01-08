package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var ticTacToe: TicTacToe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ticTacToe = TicTacToe()

        // Find the buttons for each cell on the tic tac toe board
        val button00 = findViewById<Button>(R.id.button_00)
        val button01 = findViewById<Button>(R.id.button_01)
        val button02 = findViewById<Button>(R.id.button_02)
        val button10 = findViewById<Button>(R.id.button_10)
        val button11 = findViewById<Button>(R.id.button_11)
        val button12 = findViewById<Button>(R.id.button_12)
        val button20 = findViewById<Button>(R.id.button_20)
        val button21 = findViewById<Button>(R.id.button_21)
        val button22 = findViewById<Button>(R.id.button_22)

        // Set OnClickListeners for each button
        button00.setOnClickListener { handleMove(0, 0) }
        button01.setOnClickListener { handleMove(0, 1) }
        button02.setOnClickListener { handleMove(0, 2) }
        button10.setOnClickListener { handleMove(1, 0) }
        button11.setOnClickListener { handleMove(1, 1) }
        button12.setOnClickListener { handleMove(1, 2) }
        button20.setOnClickListener { handleMove(2, 0) }
        button21.setOnClickListener { handleMove(2, 1) }
        button22.setOnClickListener { handleMove(2, 2) }
    }

    private fun handleMove(row: Int, col: Int) {
        // Make the move and get the result
        val result = ticTacToe.makeMove(row, col)

        // Update the button text
        val button = when (row) {
            0 -> when (col) {
                0 -> findViewById<Button>(R.id.button_00)
                1 -> findViewById<Button>(R.id.button_01)
                2 -> findViewById<Button>(R.id.button_02)
                else -> throw IllegalArgumentException("Invalid column: $col")
            }
            1 -> when (col) {
                0 -> findViewById<Button>(R.id.button_10)
                1 -> findViewById<Button>(R.id.button_11)
                2 -> findViewById<Button>(R.id.button_12)
                else -> throw IllegalArgumentException("Invalid column: $col")
            }
            2 -> when (col) {
                0 -> findViewById<Button>(R.id.button_20)
                1 -> findViewById<Button>(R.id.button_21)
                2 -> findViewById<Button>(R.id.button_22)
                else -> throw IllegalArgumentException("Invalid column: $col")
            }
            else -> throw IllegalArgumentException("Invalid row: $row")
        }
        button.text = ticTacToe.getCurrentPlayer().toString()

        // Check if the game is over
        if (result == TicTacToe.Result.X_WINS) {
            Toast.makeText(this, "X wins!", Toast.LENGTH_SHORT).show()
        } else if (result == TicTacToe.Result.O_WINS) {
            Toast.makeText(this, "O wins!", Toast.LENGTH_SHORT).show()
        } else if (result == TicTacToe.Result.DRAW) {
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        }
    }

    class TicTacToe {

        enum class Player {
            X, O, Empty
        }

        enum class Result {
            X_WINS, O_WINS, DRAW, IN_PROGRESS
        }

        private var board = Array(3) { Array(3) { Player.Empty } }
        private var currentPlayer = Player.X

        fun makeMove(row: Int, col: Int): Result {
            if (row !in 0..2 || col !in 0..2) {
                throw IllegalArgumentException("Invalid move: $row, $col")
            }
            if (board[row][col] != Player.Empty) {
                throw IllegalArgumentException("Position is already occupied: $row, $col")
            }
            board[row][col] = currentPlayer
            val result = getResult()
            if (result == Result.IN_PROGRESS) {
                currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
            }
            return result
        }

        fun getCurrentPlayer(): Player {
            val result = getResult()
            if (result == Result.IN_PROGRESS) {
                return if (currentPlayer == Player.X) Player.O else Player.X
            } else {
                return currentPlayer
            }
        }

        private fun getResult(): Result {
            // Check rows
            for (row in 0..2) {
                if (board[row][0] != Player.Empty && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                    return if (board[row][0] == Player.X) Result.X_WINS else Result.O_WINS
                }
            }

            // Check columns
            for (col in 0..2) {
                if (board[0][col] != Player.Empty && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                    return if (board[0][col] == Player.X) Result.X_WINS else Result.O_WINS
                }
            }

            // Check diagonals
            if (board[0][0] != Player.Empty && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                return if (board[0][0] == Player.X) Result.X_WINS else Result.O_WINS
            }
            if (board[2][0] != Player.Empty && board[2][0] == board[1][1] && board[1][1] == board[0][2]) {
                return if (board[2][0] == Player.X) Result.X_WINS else Result.O_WINS
            }

            // Check for draw
            for (row in 0..2) {
                for (col in 0..2) {
                    if (board[row][col] == Player.Empty) {
                        return Result.IN_PROGRESS
                    }
                }
            }
            return Result.DRAW
        }
    }
}