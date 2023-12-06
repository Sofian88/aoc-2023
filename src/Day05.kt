import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.math.min

fun main() {

    fun rangeBasedCalculation(
        seeds: LongRange, level: Int, tree: List<TreeNode>
    ): Long {
        var minLocation = Long.MAX_VALUE
        seeds.forEach { seed ->
            var lookup = seed
            for (i: Int in 0..level) {
                val node = tree.firstOrNull { it is Node && it.level == i && it.contains(lookup) }
                node?.let {
                    lookup = (it as Node).get(lookup)
                }
            }
            minLocation = min(minLocation, lookup)

        }
        "Found: $minLocation".println()
        return minLocation
    }

    fun minLocation(input: List<String>, seeds: LongRange): Long {
        var level = -1
        val tree = mutableListOf<TreeNode>()

        for (i: Int in 1..<input.size) {
            val row = input[i]
            if (row.contains("map:")) {
                level++
                continue
            }


            if (input[i].isBlank()) continue
            val values = input[i].split(" ").map { it.trim().toLong() }
            tree += Node(
                LongRange(values[1], values[1] + values[2] - 1), level, ChildNode(
                    LongRange(
                        values[0], values[0] + values[2] - 1
                    )
                )
            )
        }
        return rangeBasedCalculation(seeds, level, tree)
    }

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ")[1].split(" ").map { it.trim().toLong() }.map { LongRange(it, it) }
        return seeds.minOf { minLocation(input, it) }
    }

    fun part2(input: List<String>): Long {
        var minLocation = Long.MAX_VALUE
        val seedsInput = input.first().split(": ")[1].split(" ").map { it.trim().toLong() }
        val seeds = mutableListOf<LongRange>()
        for (i: Int in seedsInput.indices step 2) {
            val baseRange = LongRange(seedsInput[i], seedsInput[i] + seedsInput[i + 1] - 1)
            val subRangeSize = (baseRange.last - baseRange.first) / 5
            var first = baseRange.first
            var last = first + subRangeSize
            while (last != baseRange.last) {
                seeds += LongRange(first, last)
                first = last + 1
                last = min(baseRange.last, last + subRangeSize)
            }
        }

        runBlocking {
            val jobs = seeds.map {
                async(Dispatchers.Default) {
                    "Size: ${it.last - it.first}".println()
                    return@async minLocation(input, it)
                }
            }
            minLocation = jobs.awaitAll().minOf { it }
        }

        return minLocation
    }

    val testInput = readInput("Day05_test")
    part1(testInput).println()
    check(part1(testInput) == 35L)
    part2(testInput).println()
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

interface TreeNode {
    val range: LongRange
    fun contains(lookup: Long): Boolean
}

data class Node(override val range: LongRange, val level: Int, val child: ChildNode) : TreeNode {
    override fun contains(lookup: Long) = range.contains(lookup)
    fun get(lookup: Long): Long = child.range.first + (lookup - range.first)
}

data class ChildNode(override val range: LongRange) : TreeNode {
    override fun contains(lookup: Long) = range.contains(lookup)
}