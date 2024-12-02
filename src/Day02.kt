import kotlin.math.abs

fun main() {

    fun isReportSafe(report: List<Int>): Boolean {
        val isIncreasing: Boolean = report[1] > report[0]

        for (i in 1 until report.size) {
            val diff = report[i] - report[i - 1]

            // Check the difference between adjacent levels
            if (abs(diff) < 1 || abs(diff) > 3) {
                return false
            }

            // Check if the sequence is consistently increasing or decreasing
            if (diff > 0 && !isIncreasing) {
                return false // The sequence was expected to decrease, but is increasing
            }

            if (diff < 0 && isIncreasing) {
                return false // The sequence was expected to increase, but is decreasing
            }
        }

        return true
    }

    fun isReportSafeWithOneRemoval(input: List<Int>): Boolean {
        // if the report is already safe there's nothing do here
        if (isReportSafe(input)) return true

        // Try removing each level and check if it becomes safe
        for (i in input.indices) {
            val newReport = input.filterIndexed { index, _ -> index != i }
            if (isReportSafe(newReport)) {
                return true // If removing one level makes the report safe, return true
            }
        }

        return false // If no single removal makes it safe, return false
    }

    fun countSafeReports(input: List<String>): Pair<Int, Int> {
        var safeCount = 0
        var safeCountWithProblemDampener = 0

        val reportList = input.map { report ->
            report.trim().split(" ").map { it.toInt() }
        }

        for (report in reportList) {
            if (isReportSafe(report)) {
                safeCount++
            }
            if (isReportSafeWithOneRemoval(report)) {
                safeCountWithProblemDampener++
            }
        }

        return Pair(safeCount, safeCountWithProblemDampener)
    }



    fun part1(input: List<String>): Int {
        return countSafeReports(input).first
    }

    fun part2(input: List<String>): Int {
        return countSafeReports(input).second
    }

    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
