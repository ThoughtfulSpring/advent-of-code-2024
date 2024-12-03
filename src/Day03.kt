fun main() {

    fun extractValidMultiplications(input: List<String>): List<Pair<Int, Int>> {
        // Regex to match valid mul(X,Y) pattern where X and Y are 1-3 digits
        val pattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()

        return input.flatMap { line ->
            pattern.findAll(line)
                .map { matchResult ->
                    val (x, y) = matchResult.destructured
                    Pair(x.toInt(), y.toInt())
                }
                .toList()
        }
    }

    fun calculateSum(multiplications: List<Pair<Int, Int>>): Int {
        return multiplications.sumOf { (x, y) -> x * y }
    }

    fun processCorruptedMemory(input: List<String>): Int {
        val validMultiplications = extractValidMultiplications(input)
        return calculateSum(validMultiplications)
    }

    fun part1(input: List<String>): Int {
        return processCorruptedMemory(input)
    }

    fun part2(input: List<String>): Int {
        // Regex patterns for different types of instructions
        val mulPattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        val doPattern = """do\(\)""".toRegex()
        val dontPattern = """don't\(\)""".toRegex()

        // Start with multiplications enabled
        var isEnabled = true
        val results = mutableListOf<Int>()

        // Process each line in the input
        input.forEach { line ->
            var currentPosition = 0

            // Process each instruction in the line
            while (currentPosition < line.length) {
                // Find the next occurrence of each instruction type
                val doMatch = doPattern.find(line, currentPosition)
                val dontMatch = dontPattern.find(line, currentPosition)
                val mulMatch = mulPattern.find(line, currentPosition)

                // Get the position of each instruction (MAX_VALUE if not found)
                val nextDoPos = doMatch?.range?.first ?: Int.MAX_VALUE
                val nextDontPos = dontMatch?.range?.first ?: Int.MAX_VALUE
                val nextMulPos = mulMatch?.range?.first ?: Int.MAX_VALUE

                // Process the instruction that appears first
                when {
                    // do() instruction appears first
                    nextDoPos < nextDontPos && nextDoPos < nextMulPos -> {
                        isEnabled = true
                        currentPosition = doMatch!!.range.last + 1
                    }
                    // don't() instruction appears first
                    nextDontPos < nextDoPos && nextDontPos < nextMulPos -> {
                        isEnabled = false
                        currentPosition = dontMatch!!.range.last + 1
                    }
                    // mul() instruction appears first
                    nextMulPos != Int.MAX_VALUE -> {
                        if (isEnabled) {
                            val (x, y) = mulMatch!!.destructured
                            results.add(x.toInt() * y.toInt())
                        }
                        currentPosition = mulMatch!!.range.last + 1
                    }
                    // No more instructions found in this line
                    else -> break
                }
            }
        }

        return results.sum()
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
