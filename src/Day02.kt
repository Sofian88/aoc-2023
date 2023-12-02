import kotlin.math.max

fun main() {
    fun mapGames(input: List<String>): List<Game> {
        val games = input.map { line ->
            with(line.split(": ")) {
                val id = this[0].split(" ")[1].toInt()
                val rgb = mutableListOf<Triple<Int, Int, Int>>()

                this[1].split("; ").forEach { gameItem ->
                    var r = 0
                    var g = 0
                    var b = 0
                    gameItem.split(", ").forEach {
                        val record = it.split(" ")
                        when (record[1]) {
                            "red" -> r = record[0].toInt()
                            "green" -> g = record[0].toInt()
                            "blue" -> b = record[0].toInt()
                        }
                    }
                    rgb.add(Triple(r, g, b))
                }
                Game(id, rgb)
            }

        }
        return games
    }

    fun part1(input: List<String>): Int {
        val games = mapGames(input)
        return games.sumOf { if (it.isValid()) it.id else 0 }
    }

    fun part2(input: List<String>): Long {
        val games = mapGames(input)
        return games.sumOf { it.powerOfItems() }
    }

    val testInput = readInput("Day02_test")
    part1(testInput).println()
    check(part1(testInput) == 8)
    part2(testInput).println()
    check(part2(testInput) == 2286L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class Game(val id: Int, private val rgb: List<Triple<Int, Int, Int>>) {
    fun isValid(): Boolean = rgb.none { it.first > 12 || it.second > 13 || it.third > 14 }

    fun powerOfItems(): Long {
        val required = getMinNecessary()
        return required.first.toLong() * required.second.toLong() * required.third.toLong()
    }

    private fun getMinNecessary() = rgb.reduce { acc, triple ->
        Triple(max(acc.first, triple.first), max(acc.second, triple.second), max(acc.third, triple.third))
    }
}

