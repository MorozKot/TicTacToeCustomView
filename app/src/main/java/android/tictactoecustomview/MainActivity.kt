package android.tictactoecustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.tictactoecustomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isFirstPlayer = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ticTacToeField.ticTacToeField = TicTacToeField(10, 10)

        binding.ticTacToeField.actionListener = { row, column, field ->
            val cell = field.getCell(row, column)
            if (cell == Cell.EMPTY) {
                if (isFirstPlayer) {
                    field.setCell(row, column, Cell.PLAYER1)
                } else {
                    field.setCell(row, column, Cell.PLAYER2)
                }
                isFirstPlayer = !isFirstPlayer
            }
        }
    }
}