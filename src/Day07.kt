fun main() {

    fun sortHands(input: List<String>, cards: String, handType: String.(Boolean) -> Int, withJoker: Boolean) =
        input.map {
            val hand = it.split(" ")
            Hand(hand[0], hand[1].toLong())
        }.sortedWith(HandComparator(cards, handType, withJoker))

    fun part1(input: List<String>): Long {
        val hands = sortHands(input, cardsNormal, defaultHandType, false)
        return hands.foldIndexed(0) { index, acc, hand ->
            acc + ((index + 1) * hand.rank)
        }
    }

    fun part2(input: List<String>): Long {
        val hands = sortHands(input, cardsExtended, defaultHandType, true)
        hands.map { it.value }.println()
        return hands.foldIndexed(0L) { index, acc, hand ->
            acc + ((index + 1) * hand.rank)
        }
    }

    val testInput = readInput("Day07_test")
    part1(testInput).println()
    check(part1(testInput) == 6440L)
    part2(testInput).println()
    check(part2(testInput) == 5905L)
    val testInput2 = readInput("Day07_test2")
    part2(testInput2).println()
    check(part2(testInput2) == 6839L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

val cardsNormal = "AKQJT98765432".reversed()
val cardsExtended = "AKQT98765432J".reversed()

data class Hand(val value: String, val rank: Long)


class HandComparator(
    private val cards: String,
    val handType: String.(Boolean) -> Int,
    private val withJoker: Boolean = false
) : Comparator<Hand> {
    override fun compare(first: Hand, second: Hand): Int {
        val firstHand = first.value.handType(withJoker)
        val secondHand = second.value.handType(withJoker)
        return if (firstHand == secondHand) {
            first.value.forEachIndexed { index, c ->
                if (c != second.value[index]) {
                    return if (cards.indexOf(c) < cards.indexOf(second.value[index]))
                        -1
                    else 1
                }
            }
            return 0
        } else if (firstHand < secondHand) -1
        else 1
    }
}

val defaultHandType = fun String.(withJoker: Boolean): Int {
    val repetition = this.repetition(withJoker).values
    return if (repetition.any { it == 5 }) {
        6
    } else if (repetition.any { it == 4 }) {
        5
    } else if (repetition.any { it == 3 } && repetition.any { it == 2 }) {
        4
    } else if (repetition.any { it == 3 } && repetition.none { it == 2 }) {
        3
    } else if (repetition.filter { it == 2 }.size == 2) {
        2
    } else if (repetition.any { it == 2 }) {
        1
    } else {
        0
    }
}

fun String.repetition(withJoker: Boolean = false): Map<String, Int> {
    var repetition = mutableMapOf<String, Int>()
    this.forEach {
        repetition[it.toString()] = (repetition[it.toString()] ?: 0) + 1
    }
    repetition = repetition.toList().sortedBy { (_, value) -> value }.reversed().toMap().toMutableMap()
    if (withJoker) {
        val tempRepetition = mutableMapOf<String, Int>()
        val jokerCount = repetition.remove("J") ?: 0
        if (jokerCount != 0 && jokerCount != 5) {
            repetition.entries.first().also { (c, i) ->
                tempRepetition[c] = i + jokerCount
                repetition.remove(c)
            }
            repetition += tempRepetition
        } else if (jokerCount == 5) {
            repetition = mutableMapOf("J" to 5)
        }
    }
    return repetition
}



