import kotlin.math.abs

fun main() {

    fun findTotalDistanceDifference(input: List<String>) : Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input.map {
            val (first, second) = it.split("   ").map { it.toInt() }

            leftList.add(first)
            rightList.add(second)
        }

        leftList.sort()
        rightList.sort()

        return leftList.zip(rightList) { first, second ->
            abs(first - second)
        }.sum()
    }

    fun part1(input: List<String>): Int {
        return findTotalDistanceDifference(input)
    }

    fun totalSimilarityScore(input: List<String>): Int {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input.map {
            val (first, second) = it.split("   ").map { it.toInt() }

            leftList.add(first)
            rightList.add(second)
        }

        leftList.sort()
        rightList.sort()

        val rightListFrequency = rightList.groupBy { it }.mapValues { it.value.size }

        return leftList.sumOf { first ->
            first * (rightListFrequency[first] ?: 0 )
        }
    }


    fun part2(input: List<String>): Int {
        return totalSimilarityScore(input)
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 11)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
