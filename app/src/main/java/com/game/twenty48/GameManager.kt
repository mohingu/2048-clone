package com.game.twenty48

class GameManager(
    val size: Int = 4,
    private val onStateChanged: () -> Unit,
    private val soundManager: SoundManager? = null
) {
    private val grid = Grid(size)
    var score = 0
        private set
    var over = false
        private set
    var won = false
        private set

    init {
        setup()
    }

    private fun setup() {
        grid.clear()
        score = 0
        over = false
        won = false
        addStartTiles()
    }

    fun restart() {
        setup()
        onStateChanged()
    }

    private fun addStartTiles() {
        repeat(2) {
            addRandomTile()
        }
    }

    private fun addRandomTile() {
        val availableCells = grid.availableCells()
        if (availableCells.isNotEmpty()) {
            val cell = availableCells.random()
            val tile = Tile.create(cell.first, cell.second)
            grid.insertTile(tile)
        }
    }

    fun move(direction: Direction): Boolean {
        if (over || won) return false

        val vector = getVector(direction)
        val traversals = buildTraversals(vector)
        var moved = false
        var merged = false

        prepareTiles()

        traversals.row.forEach { row ->
            traversals.col.forEach { col ->
                val tile = grid.cellContent(row, col)
                tile?.let {
                    val positions = findFarthestPosition(it, vector)
                    val next = grid.cellContent(positions.second.first, positions.second.second)

                    if (next != null && next.value == tile.value && next.mergedFrom == null) {
                        // Merge tiles
                        val mergedTile = Tile(tile.value * 2, positions.second.first, positions.second.second)
                        mergedTile.mergedFrom = Pair(tile, next)

                        grid.insertTile(mergedTile)
                        grid.removeTile(tile)

                        tile.updatePosition(positions.second.first, positions.second.second)

                        score += mergedTile.value

                        // Check for milestones
                        if (mergedTile.value in listOf(256, 512, 1024, 2048)) {
                            soundManager?.playMilestoneSound(mergedTile.value)
                            soundManager?.vibrateMilestone(mergedTile.value)
                        }

                        if (mergedTile.value == 2048) {
                            won = true
                        }

                        moved = true
                        merged = true
                    } else {
                        moveTile(tile, positions.first.first, positions.first.second)
                        if (tile.row != row || tile.col != col) {
                            moved = true
                        }
                    }
                }
            }
        }

        if (moved) {
            // Play appropriate sound
            if (merged) {
                soundManager?.playMergeSound()
            } else {
                soundManager?.playMoveSound()
            }
            
            addRandomTile()

            if (!movesAvailable()) {
                over = true
                soundManager?.playGameOverSound()
            } else if (won) {
                soundManager?.playWinSound()
            }

            onStateChanged()
        }

        return moved
    }

    private fun prepareTiles() {
        grid.eachCell { _, _, tile ->
            tile?.let {
                it.mergedFrom = null
                it.isNew = false
            }
        }
    }

    private fun moveTile(tile: Tile, row: Int, col: Int) {
        grid.removeTile(tile)
        tile.updatePosition(row, col)
        grid.insertTile(tile)
    }

    private fun getVector(direction: Direction): Pair<Int, Int> {
        return when (direction) {
            Direction.UP -> Pair(-1, 0)
            Direction.DOWN -> Pair(1, 0)
            Direction.LEFT -> Pair(0, -1)
            Direction.RIGHT -> Pair(0, 1)
        }
    }

    private fun buildTraversals(vector: Pair<Int, Int>): Traversals {
        val traversals = Traversals(
            row = (0 until size).toMutableList(),
            col = (0 until size).toMutableList()
        )

        if (vector.first == 1) traversals.row.reverse()
        if (vector.second == 1) traversals.col.reverse()

        return traversals
    }

    private fun findFarthestPosition(
        tile: Tile,
        vector: Pair<Int, Int>
    ): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        var previous: Pair<Int, Int>
        var cell = Pair(tile.row, tile.col)

        do {
            previous = cell
            cell = Pair(previous.first + vector.first, previous.second + vector.second)
        } while (grid.withinBounds(cell.first, cell.second) && grid.cellAvailable(cell.first, cell.second))

        return Pair(previous, cell)
    }

    private fun movesAvailable(): Boolean {
        return grid.availableCells().isNotEmpty() || tileMatchesAvailable()
    }

    private fun tileMatchesAvailable(): Boolean {
        for (row in 0 until size) {
            for (col in 0 until size) {
                val tile = grid.cellContent(row, col)
                tile?.let {
                    for (direction in Direction.values()) {
                        val vector = getVector(direction)
                        val cell = Pair(row + vector.first, col + vector.second)
                        val other = grid.cellContent(cell.first, cell.second)

                        if (other != null && other.value == tile.value) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun getTiles(): List<Tile> {
        return grid.getAllTiles()
    }

    private data class Traversals(
        val row: MutableList<Int>,
        val col: MutableList<Int>
    )
}

