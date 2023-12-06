import kotlin.math.*

fun main() {

    fun part1(input: List<String>): Long {
        val timeAndDistance = input[0].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() } to
                input[1].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }

        var numberOfWays = 1L
        for (i: Int in 0..<timeAndDistance.first.size) {
            val min = floor(
                ((-timeAndDistance.first[i].toDouble() + sqrt(
                    timeAndDistance.first[i].toDouble().pow(2) - 4 * -1 * -timeAndDistance.second[i].toDouble()
                )) / -2) + 1
            )

            val max = ceil(
                ((-timeAndDistance.first[i].toDouble() - sqrt(
                    timeAndDistance.first[i].toDouble().pow(2) - 4 * -1 * -timeAndDistance.second[i].toDouble()
                )) / -2) - 1
            )

            numberOfWays *= (max.toLong() - min.toLong()) + 1L
        }

        return numberOfWays
    }

    fun part2(input: List<String>): Long {
        val timeAndDistance =
            input[0].split(":")[1].split(" ").filter { it.isNotBlank() }.joinToString("") { it.trim() }.toLong() to
                    input[1].split(":")[1].split(" ").filter { it.isNotBlank() }.joinToString("") { it.trim() }.toLong()


        val min = floor(
            ((-timeAndDistance.first.toDouble() + sqrt(
                timeAndDistance.first.toDouble().pow(2) - 4 * -1 * -timeAndDistance.second.toDouble()
            )) / -2) + 1
        )

        val max = ceil(
            ((-timeAndDistance.first.toDouble() - sqrt(
                timeAndDistance.first.toDouble().pow(2) - 4 * -1 * -timeAndDistance.second.toDouble()
            )) / -2) - 1
        )

        return (max.toLong() - min.toLong()) + 1L
    }

    val testInput = readInput("Day06_test")
    part1(testInput).println()
    check(part1(testInput) == 288L)
    part2(testInput).println()
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}




