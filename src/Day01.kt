fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            Integer.valueOf(String(charArrayOf(line.first { it.isDigit() }, line.last { it.isDigit() })))
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            Integer.valueOf(String(charArrayOf(line.firstNumericValue(), line.lastNumericValue())))
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

fun String.firstNumericValue(): Char {
    var position = this.indexOfFirst { it.isDigit() }.takeIf { it != -1 } ?: Int.MAX_VALUE

    this.findAnyOf(numbers.keys, 0)?.let {
        position = kotlin.math.min(position, it.first)
    }

    return if (this[position].isDigit()) this[position]
    else (numbers[this.findAnyOf(numbers.keys, 0)?.second] ?: 0).toString().first()
}


fun String.lastNumericValue(): Char {
    var position = this.indexOfLast { it.isDigit() }.takeIf { it != -1 } ?: 0

    this.findLastAnyOf(numbers.keys, this.length - 1)?.let {
        position = kotlin.math.max(position, it.first)
    }
    return if (this[position].isDigit()) this[position]
    else (numbers[this.findLastAnyOf(numbers.keys, position)?.second] ?: 0).toString().first()
}

