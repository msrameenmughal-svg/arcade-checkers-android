package com.arcade.checkers.game

enum class PieceColor { RED, CYAN }
enum class PieceType { MAN, KING }

data class Piece(val color: PieceColor, var type: PieceType = PieceType.MAN) {
    val id get() = "$color-$type"
}

data class Cell(val row: Int, val col: Int)

data class Move(
    val from: Cell,
    val to: Cell,
    val captures: List<Cell> = emptyList()
) {
    val isCapture get() = captures.isNotEmpty()
}

class CheckersGame {
    companion object {
        const val SIZE = 8
    }

    val board: Array<Array<Piece?>> = Array(SIZE) { r ->
        Array(SIZE) { c ->
            if ((r + c) % 2 == 1) {
                when {
                    r < 3 -> Piece(PieceColor.RED)
                    r > 4 -> Piece(PieceColor.CYAN)
                    else -> null
                }
            } else null
        }
    }

    var currentPlayer = PieceColor.CYAN
        private set
    var selected: Cell? = null
    var validMoves: List<Move> = emptyList()
        private set
    var capturedByCyan = 0
        private set
    var capturedByRed = 0
        private set
    var winner: PieceColor? = null
        private set
    var moveHistory = mutableListOf<List<List<Piece?>>>()
        private set

    fun piecesOf(color: PieceColor): Int =
        board.sumOf { row -> row.count { it?.color == color } }

    fun kingsOf(color: PieceColor): Int =
        board.sumOf { row -> row.count { it?.color == color && it.type == PieceType.KING } }

    private fun inside(r: Int, c: Int) = r in 0 until SIZE && c in 0 until SIZE

    private fun directions(piece: Piece): List<Pair<Int, Int>> = when (piece.type) {
        PieceType.KING -> listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
        PieceType.MAN -> if (piece.color == PieceColor.CYAN) listOf(-1 to -1, -1 to 1)
        else listOf(1 to -1, 1 to 1)
    }

    private fun opponent(color: PieceColor) =
        if (color == PieceColor.CYAN) PieceColor.RED else PieceColor.CYAN

    fun legalMovesFor(row: Int, col: Int): List<Move> {
        val piece = board[row][col] ?: return emptyList()
        if (piece.color != currentPlayer) return emptyList()
        val moves = mutableListOf<Move>()
        for ((dr, dc) in directions(piece)) {
            val r1 = row + dr; val c1 = col + dc
            if (!inside(r1, c1)) continue
            if (board[r1][c1] == null) {
                moves += Move(Cell(row, col), Cell(r1, c1))
            } else if (board[r1][c1]?.color == opponent(piece.color)) {
                val r2 = row + 2 * dr; val c2 = col + 2 * dc
                if (inside(r2, c2) && board[r2][c2] == null) {
                    moves += Move(Cell(row, col), Cell(r2, c2), listOf(Cell(r1, c1)))
                }
            }
        }
        // Forced captures: if any capture exists for this piece, only captures are legal
        val captures = moves.filter { it.isCapture }
        return captures.ifEmpty { moves }
    }

    fun allLegalMoves(color: PieceColor = currentPlayer): List<Move> {
        val all = mutableListOf<Move>()
        for (r in 0 until SIZE) for (c in 0 until SIZE) {
            if (board[r][c]?.color == color) all += legalMovesFor(r, c)
        }
        // Forced capture rule across the whole board
        val captures = all.filter { it.isCapture }
        return captures.ifEmpty { all }
    }

    fun select(row: Int, col: Int) {
        val piece = board[row][col]
        if (piece != null && piece.color == currentPlayer) {
            selected = Cell(row, col)
            validMoves = legalMovesFor(row, col)
        } else {
            selected = null
            validMoves = emptyList()
        }
    }

    fun tryMove(to: Cell): Boolean {
        val sel = selected ?: return false
        val move = validMoves.firstOrNull { it.to == to } ?: return false
        makeMove(move)
        return true
    }

    fun makeMove(move: Move) {
        // Snapshot for undo
        moveHistory += board.map { row -> row.map { it } }

        val piece = board[move.from.row][move.from.col] ?: return
        board[move.from.row][move.from.col] = null
        board[move.to.row][move.to.col] = piece

        for (cap in move.captures) {
            board[cap.row][cap.col]?.let {
                if (it.color == PieceColor.CYAN) capturedByRed++ else capturedByCyan++
            }
            board[cap.row][cap.col] = null
        }

        // Promotion
        if (piece.type == PieceType.MAN) {
            if (piece.color == PieceColor.CYAN && move.to.row == 0) piece.type = PieceType.KING
            if (piece.color == PieceColor.RED && move.to.row == SIZE - 1) piece.type = PieceType.KING
        }

        selected = null
        validMoves = emptyList()

        // Multi-jump continuation: if the same piece can jump again, keep turn
        if (move.isCapture) {
            val extra = legalMovesFor(move.to.row, move.to.col).filter { it.isCapture }
            if (extra.isNotEmpty()) {
                selected = move.to
                validMoves = extra
                return
            }
        }

        currentPlayer = opponent(currentPlayer)
        checkWinner()
    }

    private fun checkWinner() {
        val redCount = piecesOf(PieceColor.RED)
        val cyanCount = piecesOf(PieceColor.CYAN)
        val redMoves = allLegalMoves(PieceColor.RED)
        val cyanMoves = allLegalMoves(PieceColor.CYAN)
        winner = when {
            redCount == 0 || redMoves.isEmpty() -> PieceColor.CYAN
            cyanCount == 0 || cyanMoves.isEmpty() -> PieceColor.RED
            else -> null
        }
    }

    fun undo(): Boolean {
        if (moveHistory.isEmpty()) return false
        val prev = moveHistory.removeAt(moveHistory.lastIndex)
        for (r in 0 until SIZE) for (c in 0 until SIZE) board[r][c] = prev[r][c]
        selected = null
        validMoves = emptyList()
        currentPlayer = opponent(currentPlayer)
        winner = null
        return true
    }

    fun reset() {
        for (r in 0 until SIZE) for (c in 0 until SIZE) {
            board[r][c] = when {
                (r + c) % 2 == 1 && r < 3 -> Piece(PieceColor.RED)
                (r + c) % 2 == 1 && r > 4 -> Piece(PieceColor.CYAN)
                else -> null
            }
        }
        currentPlayer = PieceColor.CYAN
        selected = null
        validMoves = emptyList()
        capturedByCyan = 0
        capturedByRed = 0
        winner = null
        moveHistory.clear()
    }
}
