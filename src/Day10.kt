import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        var startPosition: Pair<Int, Int> = Pair(0, 0)
        val board =
            input.mapIndexed { y, row ->
                row.split("").mapIndexed { x, c ->
                    when (c) {
                        "S" -> {
                            startPosition = x - 1 to y
                            Animal()
                        }

                        "|" -> VerticalPipe()
                        "-" -> HorizontalPipe()
                        "L" -> Bend1()
                        "J" -> Bend2()
                        "7" -> Bend3()
                        "F" -> Bend4()
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
    return findLoop(board, startPosition, 0L)
}

private fun Array<Pair<Pair<Int, Int>, Tile>>.validate(): Array<Pair<Pair<Int, Int>, Tile>> {
    val newArray = arrayListOf<Pair<Pair<Int, Int>, Tile>>()
    if (this[0].second is VerticalPipe || this[0].second is Bend3 || this[0].second is Bend4) {
        newArray.add(this[0])
    } else if (this[1].second is VerticalPipe || this[1].second is Bend1 || this[1].second is Bend2) {
        newArray.add(this[1])
    } else if (this[2].second is HorizontalPipe || this[2].second is Bend1 || this[2].second is Bend4) {
        newArray.add(this[2])
    } else if (this[3].second is HorizontalPipe || this[3].second is Bend2 || this[3].second is Bend3) {
        newArray.add(this[3])
    }
    return newArray.toTypedArray()
}

private fun getNeighbours(
    x: Int,
    y: Int,
    board: List<List<Tile>>
) = arrayOf(
    x to max(0, y - 1) to board[x][max(0, y - 1)],
    x to min(board.size, y + 1) to board[x][min(board.size, y + 1)],
    max(0, x - 1) to y to board[max(0, x - 1)][y],
    min(board[x].size, x + 1) to y to board[min(board[x].size, x + 1)][y]
)

tailrec fun findLoop(
    board: List<List<Tile>>,
    first: Pair<Int, Int>,
    distance: Long
): Long {
    val nextNeighbours = getNeighbours(first.first, first.second, board).validate()
    return if (nextNeighbours.any { it.second is Animal }) distance + 1
    else if (nextNeighbours.isEmpty()) distance
    else {
        nextNeighbours.maxOf { findLoop(board, it.first, distance + 1) }
    }
}


sealed interface Tile
sealed interface North : Tile
sealed interface South : Tile
sealed interface East : Tile
sealed interface West : Tile


class Floor : Tile
class Animal : Tile
class VerticalPipe : North, South
class HorizontalPipe : East, West
class Bend1 : North, East
class Bend2 : North, West
class Bend3 : South, West
class Bend4 : South, East


