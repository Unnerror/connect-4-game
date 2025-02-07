// Danila Sergienko
// 2307887

package org.example

val debug = false

enum class CellState(val symbol: Char) {
    EMPTY(' '),
    PLAYER_X('X'),
    PLAYER_O('O');

    override fun toString(): String = symbol.toString()
}

class Game {
    private val board: Array<Array<CellState>> = Array(6) { Array(7) { CellState.EMPTY } }
    private var currentPlayer: CellState = CellState.PLAYER_X

    //to track and switch player for handeling test testNoWinYet() - parce qui in my logic player switching is outside of makeMove()
    var lastPlayer: CellState? = null

    fun play() {
        var gameRunning = true

        while (gameRunning) {
            displayBoard()

            println("Player ${currentPlayer.symbol}, it's your turn! Enter a column (1-7):")

            // handling user input
            val column = readlnOrNull()?.toIntOrNull()
            if (column == null || column !in 1..7) {
                println("Invalid input. Please enter a number between 1 and 7.")
                continue
            }

            // making move converting to zero based value
            if (!makeMove(column -1)) {
                println("Column $column is full. Try a different column.")
                continue
            }

            // checking if the game can be continued
            if (checkWin()) {
                displayBoard()
                println("Player ${currentPlayer.symbol} wins!")
                gameRunning = false
            } else if (isBoardFull()) {
                displayBoard()
                println("The game is a draw!")
                gameRunning = false
            } else {
                // displayBoard()
                if (debug)
                    println("else displayBoard()")
                switchPlayer()
            }
        }

        // or we have a winner
        println("Game over! Do you want to play again? (yes/no):")
        if (readLine()?.lowercase() == "yes") {
            resetBoard()
            play()
        }
    }

    fun resetBoard() {
        for (row in board) {
            row.fill(CellState.EMPTY)
        }
        currentPlayer = CellState.PLAYER_X
    }


    fun displayBoard() {
        println("  1   2   3   4   5   6   7")
        val rows = board.size
        val columns = board[0].size
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                // populating the board with cell values
                print("| ${board[row][col].symbol} ")
            }
            println("|")
        }
    }

    fun makeMove(column: Int): Boolean {
        // check column index
        if (column !in 0..6) {
            return false // if invalid column
        }

        if (lastPlayer == currentPlayer)
            switchPlayer()

        // placing token from the bottom to top
        for (row in board.size - 1 downTo 0) {
            if (board[row][column] == CellState.EMPTY) {
                board[row][column] = currentPlayer
                // switchPlayer()
                lastPlayer = currentPlayer
                return true // move successful
            }
        }
        return false // if column is full
    }



    fun checkWin(): Boolean {
        val rows = board.size
        val columns = board[0].size

        // checking horizontal
        for (row in 0 until rows) {
            for (col in 0 until columns - 3) {
                if (board[row][col] == currentPlayer &&
                    board[row][col + 1] == currentPlayer &&
                    board[row][col + 2] == currentPlayer &&
                    board[row][col + 3] == currentPlayer
                ) {
                    if (debug)
                        println("horizontal win detected at row $row, starting at column $col")
                    return true
                }
            }
        }

        // checking vertical
        for (col in 0 until columns) {
            for (row in 0 until rows - 3) {
                if (board[row][col] == currentPlayer &&
                    board[row + 1][col] == currentPlayer &&
                    board[row + 2][col] == currentPlayer &&
                    board[row + 3][col] == currentPlayer
                ) {
                    if (debug)
                        println("vertical win detected at row $row, starting at column $col")
                    return true
                }
            }
        }

        // checking diagonal from bottom-left to top-right
        for (row in 3 until rows) {
            for (col in 0 until columns - 3) {
                if (board[row][col] == currentPlayer &&
                    board[row - 1][col + 1] == currentPlayer &&
                    board[row - 2][col + 2] == currentPlayer &&
                    board[row - 3][col + 3] == currentPlayer
                ) {
                    if (debug)
                        println("diagonal win from bottom-left to top-right detected at row $row, starting at column $col")
                    return true
                }
            }
        }

        // checking diagonal from top-left to bottom-right
        for (row in 0 until rows - 3) {
            for (col in 0 until columns - 3) {
                if (board[row][col] == currentPlayer &&
                    board[row + 1][col + 1] == currentPlayer &&
                    board[row + 2][col + 2] == currentPlayer &&
                    board[row + 3][col + 3] == currentPlayer
                ) {
                    if (debug)
                        println("diagonal win from top-left to bottom-right detected at row $row, starting at column $col")
                    return true
                }
            }
        }
        if (debug)
            println("no winner so far")
        return false
    }


    fun isBoardFull(): Boolean {
        for (row in board) {
            if (row.contains(CellState.EMPTY)) {
                return false
            }
        }
        return true
    }


    fun switchPlayer() {
        currentPlayer = if (currentPlayer == CellState.PLAYER_X) CellState.PLAYER_O else CellState.PLAYER_X
    }

    fun getBoardCell(row: Int, col: Int): CellState {
        return board[row][col]
    }

    fun getCurrentPlayer(): CellState {
        return currentPlayer
    }
}