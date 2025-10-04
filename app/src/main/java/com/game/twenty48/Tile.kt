package com.game.twenty48

data class Tile(
    var value: Int,
    var row: Int,
    var col: Int,
    var mergedFrom: Pair<Tile, Tile>? = null
) {
    val previousPosition: Pair<Int, Int>? = null
    var isNew: Boolean = false

    fun updatePosition(newRow: Int, newCol: Int) {
        row = newRow
        col = newCol
    }

    companion object {
        fun create(row: Int, col: Int): Tile {
            return Tile(if (Math.random() < 0.9) 2 else 4, row, col).apply {
                isNew = true
            }
        }
    }
}

