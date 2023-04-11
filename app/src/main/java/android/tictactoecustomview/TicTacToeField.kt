package android.tictactoecustomview

enum class Cell {
    PLAYER1,
    PLAYER2,
    EMPTY
}

typealias OnFieldChangedListener = (field: TicTacToeField) -> Unit

class TicTacToeField(
    val rows: Int,
    val columns: Int
) {
    private var cellsMatrix = Array(rows) { Array(columns) { Cell.EMPTY } }

    val listeners = mutableSetOf<OnFieldChangedListener>()

    fun getCell(row: Int, column: Int): Cell {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return Cell.EMPTY
        return cellsMatrix[row][column]
    }

    fun setCell(row: Int, column: Int, cell: Cell) {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return
        if (getCell(row, column) != cell) {
            cellsMatrix[row][column] = cell
            listeners.forEach { it.invoke(this) }
        }
    }
}
