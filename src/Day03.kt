import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Long {
        val numbers = mutableListOf<String>()

        input.forEachIndexed { index, line ->
            var foundNumber = ""
            line.forEachIndexed { charIndex, character ->
                if (character.isDigit()) {
                    foundNumber += character
                    if (charIndex == line.length - 1) {
                        if (input[max(0, index - 1)].subSequence(max(0, charIndex - (foundNumber.length))..charIndex)
                                .any { it.isSymbol() } ||
                            input[min(
                                input.size - 1,
                                index + 1
                            )].subSequence(max(0, charIndex - (foundNumber.length))..charIndex)
                                .any { it.isSymbol() } ||
                            line[max(0, charIndex - foundNumber.length)].isSymbol()

                        ) {
                            numbers += foundNumber
                            foundNumber = ""
                        } else {
                            foundNumber = ""
                        }
                    }
                } else {
                    if (foundNumber.isNotBlank()) {
                        if (character.isSymbol() ||
                            input[max(0, index - 1)].subSequence(
                                max(
                                    0,
                                    charIndex - (foundNumber.length + 1)
                                )..charIndex
                            )
                                .any { it.isSymbol() } ||
                            input[min(
                                input.size - 1,
                                index + 1
                            )].subSequence(max(0, charIndex - (foundNumber.length + 1))..charIndex)
                                .any { it.isSymbol() } ||
                            line[max(0, charIndex - (foundNumber.length + 1))].isSymbol()

                        ) {
                            numbers += foundNumber
                            foundNumber = ""
                        } else {
                            foundNumber = ""
                        }
                    }
                }
            }
        }
        numbers.println()
        return numbers.sumOf { it.toLong() }
    }

    fun part2(input: List<String>): Long {
        val gearRatios = mutableListOf<Number>()

        input.forEachIndexed { index, line ->
            var foundNumber = ""
            line.forEachIndexed { charIndex, character ->
                var asteriskIndex = -1 to -1
                if (character.isDigit()) {
                    foundNumber += character
                    if (charIndex == line.length - 1) {
                        if (input[max(0, index - 1)].subSequence(max(0, charIndex - (foundNumber.length))..charIndex)
                                .filterIndexed { indexFilter, c ->
                                    if (c.isAsterisk()) {
                                        asteriskIndex =
                                            max(0, charIndex - (foundNumber.length)) + indexFilter to index - 1
                                    }
                                    c.isAsterisk()
                                }.isNotEmpty() ||
                            input[min(
                                input.size - 1,
                                index + 1
                            )].subSequence(max(0, charIndex - (foundNumber.length))..charIndex)
                                .filterIndexed { indexFilter, c ->
                                    if (c.isAsterisk()) {
                                        asteriskIndex =
                                            max(0, charIndex - (foundNumber.length)) + indexFilter to index + 1
                                    }
                                    c.isAsterisk()
                                }.isNotEmpty() ||
                            line[max(0, charIndex - foundNumber.length)].isAsterisk().also {
                                if (it) {
                                    asteriskIndex = max(0, charIndex - foundNumber.length) to index
                                }
                            }
                        ) {
                            gearRatios += Number(foundNumber.toLong(), asteriskIndex)
                            foundNumber = ""
                            asteriskIndex = -1 to -1
                        } else {
                            foundNumber = ""
                            asteriskIndex = -1 to -1
                        }
                    }
                } else {
                    if (foundNumber.isNotBlank()) {
                        if (character.isAsterisk().also {
                                if (it) {
                                    asteriskIndex = charIndex to index
                                }
                            } ||
                            input[max(0, index - 1)].subSequence(
                                max(
                                    0,
                                    charIndex - (foundNumber.length + 1)
                                )..charIndex
                            )
                                .filterIndexed { indexFilter, c ->
                                    if (c.isAsterisk()) {
                                        asteriskIndex = max(
                                            0,
                                            charIndex - (foundNumber.length + 1)
                                        ) + indexFilter to index - 1
                                    }
                                    c.isAsterisk()
                                }.isNotEmpty() ||
                            input[min(
                                input.size - 1,
                                index + 1
                            )].subSequence(max(0, charIndex - (foundNumber.length + 1))..charIndex)
                                .filterIndexed { indexFilter, c ->
                                    if (c.isAsterisk()) {
                                        asteriskIndex = max(
                                            0,
                                            charIndex - (foundNumber.length + 1)
                                        ) + indexFilter to index + 1
                                    }
                                    c.isAsterisk()
                                }.isNotEmpty() ||
                            line[max(0, charIndex - (foundNumber.length + 1))].isAsterisk().also {
                                if (it) {
                                    asteriskIndex = max(0, charIndex - (foundNumber.length + 1)) to index
                                }
                            }
                        ) {
                            gearRatios += Number(foundNumber.toLong(), asteriskIndex)
                            asteriskIndex = -1 to -1
                            foundNumber = ""
                        } else {
                            foundNumber = ""
                            asteriskIndex = -1 to -1
                        }
                    }
                }
            }
        }
        gearRatios.println()
        return gearRatios.filterNot { it.asteriskCoordinates == -1 to -1 }
            .groupBy { it.asteriskCoordinates }.filter { it.value.size != 1 }.entries.sumOf { entry ->
                val numbers = entry.value.map { it.number }
                numbers[0] * numbers[1]
            }
    }

    val testInput = readInput("Day03_test")
    part1(testInput).println()
    check(part1(testInput) == 4361L)
    part2(testInput).println()
    check(part2(testInput) == 467835L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun Char.isSymbol() = !this.isDigit() && this != '.'

fun Char.isAsterisk() = this == '*'

data class Number(val number: Long, val asteriskCoordinates: Pair<Int, Int>)



