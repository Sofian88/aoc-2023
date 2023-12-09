fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf { row ->
            val measurements = row.split(" ").map { it.toLong() }
            val diffLists = buildDiffLists(listOf(measurements))
            var lastNewNumber = 0L
            diffLists.filterNot { it.isEmpty() }.indices.forEach { index ->
                lastNewNumber += (diffLists.getOrNull(index + 1)?.lastOrNull() ?: 0L)
            }
            lastNewNumber
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { row ->
            val measurements = row.split(" ").map { it.toLong() }
            val diffLists = buildDiffLists(listOf(measurements))
            var lastNewNumber = 0L
            diffLists.filterNot { it.isEmpty() }.indices.forEach { index ->
                diffLists.getOrNull(index + 1)?.let {
                    lastNewNumber = (it.firstOrNull() ?: 0L) - lastNewNumber
                }
            }
            lastNewNumber
        }
    }

    val testInput = readInput("Day09_test")
    part1(testInput).println()
    check(part1(testInput) == 114L)
    part2(testInput).println()
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

tailrec fun buildDiffLists(
    measurements: List<List<Long>>,
): List<List<Long>> {
    val diffValues = mutableListOf<Long>()
    measurements.first().forEachIndexed { index, l ->
        if (measurements.first().size - 1 != index) {
            val first = l
            val second = measurements.first()[index + 1]
            diffValues += second - first
        }
    }
    val newList = mutableListOf(diffValues.toList())
    newList += measurements
    return if ((diffValues.all { it == 0L }))
        newList
    else
        buildDiffLists(newList)
}