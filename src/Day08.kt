import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val directions = input.first()
        val graph = parseGraph(input.subList(2, input.size))

        return findTarget(directions, graph, 0L, "ZZZ", "AAA")
    }

    fun part2(input: List<String>): Long {
        val directions = input.first()
        val graph = parseGraph(input.subList(2, input.size))
        val startDestinations = graph.filter { it.key.endsWith("A") }.keys.toList()
        val results = startDestinations.map {
            findTarget(directions, graph, 0L, "Z", it)
        }
        return results.reduce { acc, l -> lcm(acc, l) }
    }

    val testInput = readInput("Day08_test")
    part1(testInput).println()
    check(part1(testInput) == 2L)
    val testInput2 = readInput("Day08_test2")
    part2(testInput2).println()
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun parseGraph(input: List<String>): TreeMap<String, Pair<String, String>> {
    val graph = TreeMap<String, Pair<String, String>>()
    input.forEach {
        val elements = it.split("=")
        val key = elements.first().trim()
        val value = elements[1].trim().removePrefix("(").removeSuffix(")").split(", ")
        graph[key] = value[0] to value[1]
    }
    return graph
}

tailrec fun findTarget(
    directions: String,
    graph: TreeMap<String, Pair<String, String>>,
    step: Long,
    lookingFor: String,
    current: String
): Long {
    return if (lookingFor == current || (lookingFor.length == 1 && current.endsWith(lookingFor))) {
        "Found $step".println()
        step
    } else {
        val newDirection = when (directions[step.toInt() % directions.length]) {
            'L' -> graph[current]?.first ?: ""
            'R' -> graph[current]?.second ?: ""
            else -> graph[current]?.first ?: ""
        }
        findTarget(directions, graph, step + 1, lookingFor, newDirection)
    }
}

fun lcm(number1: Long, number2: Long): Long {
    if (number1 == 0L || number2 == 0L) {
        return 0
    }
    val abs1 = abs(number1)
    val abs2 = abs(number2)
    val absHigherNumber = max(abs1, abs2)
    val absLowerNumber = min(abs1, abs2)
    var lcm = absHigherNumber
    while (lcm % absLowerNumber != 0L) {
        lcm += absHigherNumber
    }
    return lcm
}