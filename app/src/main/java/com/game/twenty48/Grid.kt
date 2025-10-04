package com.game.twenty48

class Grid(val size: Int = 4) {
    private val cells: Array<Array<Tile?>> = Array(size) { Array(size) { null } }

    fun insertTile(tile: Tile) {
        cells[tile.row][tile.col] = tile
    }

    fun removeTile(tile: Tile) {
        cells[tile.row][tile.col] = null
    }

    fun cellAvailable(row: Int, col: Int): Boolean {
        return cells[row][col] == null
    }

    fun cellContent(row: Int, col: Int): Tile? {
        return if (withinBounds(row, col)) cells[row][col] else null
    }

    fun withinBounds(row: Int, col: Int): Boolean {
        return row >= 0 && row < size && col >= 0 && col < size
    }

    fun availableCells(): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (cellAvailable(row, col)) {
                    cells.add(Pair(row, col))
                }
            }
        }
        return cells
    }

    fun eachCell(callback: (Int, Int, Tile?) -> Unit) {
        for (row in 0 until size) {
            for (col in 0 until size) {
                callback(row, col, cells[row][col])
            }
        }
    }

    fun getAllTiles(): List<Tile> {
        val tiles = mutableListOf<Tile>()
        eachCell { _, _, tile ->
            tile?.let { tiles.add(it) }
        }
        return tiles
    }

    fun clear() {
        for (row in 0 until size) {
            for (col in 0 until size) {
                cells[row][col] = null
            }
        }
    }
}

