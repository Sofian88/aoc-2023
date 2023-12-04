import kotlin.math.pow

fun main() {

    fun mapGamesAndDraws(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
        val draws = mutableListOf<List<Int>>()
        val myNumbers = mutableListOf<List<Int>>()
        input.map { line ->
            line.split(": ")[1].split(" | ").also {
                myNumbers += it[0].split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
                draws += it[1].split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
            }

        }
        return myNumbers to draws
    }

    fun part1(input: List<String>): Long {
        val games = mapGamesAndDraws(input)
        var sum = 0.0
        games.second.forEachIndexed { index, ints ->
            games.first[index].intersect(ints).size.also {
                if (it > 0) {
                    sum += 2.0.pow(it - 1)
                }
            }
        }
        return sum.toLong()
    }

    fun part2(input: List<String>): Long {
        val games = mapGamesAndDraws(input)
        val cardCount = mutableMapOf<Int, Long>()
        games.second.forEachIndexed { index, ints ->
            games.first[index].intersect(ints).size.also {
                if (cardCount[index] == null) cardCount[index] = 1
                if (it > 0) {
                    for (iterator: Int in 1..it) {
                        cardCount[index + iterator] =
                            ((cardCount[index + iterator] ?: 1) + (1 * (cardCount[index] ?: 1)))
                    }
                }
            }
        }
        return cardCount.values.sum()
    }

    val testInput = readInput("Day04_test")
    part1(testInput).println()
    check(part1(testInput) == 13L)
    part2(testInput).println()
    check(part2(testInput) == 30L)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}




