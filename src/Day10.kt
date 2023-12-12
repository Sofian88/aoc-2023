
fun main() {
    fun part1(input: List<String>): Long {
        var startPosition: Pair<Int, Int> = Pair(0, 0)
        val board =
            input.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    when (c) {
                        'S' -> {
                            startPosition = x to y
                            Animal()
                        }

                        '|' -> VerticalPipe()
                        '-' -> HorizontalPipe()
                        'L' -> Bend1()
                        'J' -> Bend2()
                        '7' -> Bend3()
                        'F' -> Bend4()
                        else -> Floor()
                    }
                }
            }

        return findLoopDistance(board, startPosition)
    }

    fun part2(input: List<String>): Long {
        return 0L
    }

    val testInput = readInput("Day10_test")
    part1(testInput).println()
    check(part1(testInput) == 8L)
    part2(testInput).println()
    check(part2(testInput) == 0L)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

fun findLoopDistance(board: List<List<Tile>>, startPosition: Pair<Int, Int>): Long {
    return findLoop(board, emptySet(), startPosition, 0L)
}

private fun Array<Pair<Pair<Int, Int>, Tile?>>.validate(current: Tile): Array<Pair<Pair<Int, Int>, Tile?>> {
    val newArray = arrayListOf<Pair<Pair<Int, Int>, Tile?>>()
    if (this.isNotEmpty() && (current.north && this[0].second?.south == true)) {
        newArray.add(this[0])
    }
    if (this.size >= 2 && (current.south && this[1].second?.north == true)) {
        newArray.add(this[1])
    }
    if (this.size >= 3 && (current.west && this[2].second?.east == true)) {
        newArray.add(this[2])
    }
    if (this.size == 4 && (current.east && this[3].second?.west == true)) {
        newArray.add(this[3])
    }
    return newArray.filterNot { it.second == null }.toTypedArray()
}


private fun getNeighbours(
    x: Int,
    y: Int,
    board: List<List<Tile>>
) : Array<Pair<Pair<Int, Int>, Tile?>> = arrayOf(
    x to y - 1 to board.getOrNull(y - 1)?.getOrNull(x),
    x to y + 1 to board.getOrNull(y + 1)?.getOrNull(x),
    x - 1 to y to board[y].getOrNull(x - 1),
    x + 1 to y to board[y].getOrNull(x + 1)
).filterNot { it.second == null }.toTypedArray()

tailrec fun findLoop(
    board: List<List<Tile>>,
    visited: Set<Pair<Int, Int>>,
    actualItem: Pair<Int, Int>,
    distance: Long
): Long {
    val current = board[actualItem.first][actualItem.second]
    val newVisited = visited.toMutableSet().also { it.add(actualItem) }
    "Visited ${actualItem}".println()
    val nextNeighbours = getNeighbours(actualItem.first, actualItem.second, board).validate(current)
    return if (nextNeighbours.any { it.second is Animal }) distance + 1
    else if (nextNeighbours.isEmpty() && current is Animal) distance
    else {
        val neighbourToVisit =
            nextNeighbours.filterNot { nextNeighbour -> newVisited.any { it == nextNeighbour.first } }.firstOrNull()
        if (neighbourToVisit == null) {
            "No more item to visit"
            if(distance == 0L) return 0L
            return findLoop(board, newVisited, newVisited.toList()[newVisited.size - 1], distance - 1)
        } else findLoop(board, newVisited, neighbourToVisit.first, distance + 1)
    }
}


sealed class Tile(val west: Boolean, val east: Boolean, val south: Boolean, val north: Boolean)
class Floor : Tile(false, false, false, false)
class Animal : Tile(true, true, true, true)
class VerticalPipe : Tile(false, false, true, true)
class HorizontalPipe : Tile(true, true, false, false)
class Bend1 : Tile(false, true, false, true)
class Bend2 : Tile(true, false, false, true)
class Bend3 : Tile(true, false, true, false)
class Bend4 : Tile(false, true, true, false)


