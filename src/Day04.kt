fun main() {

    fun countXMAS(grid: List<String>, part1: Boolean): Int {
        val rows = grid.size
        val cols = grid[0].length
        var count = 0

        // All possible directions to search (horizontal, vertical, diagonal)
        val directions = listOf(
            Pair(0, 1),   // right
            Pair(1, 0),   // down
            Pair(1, 1),   // diagonal down-right
            Pair(-1, 1),  // diagonal up-right
            Pair(0, -1),  // left
            Pair(-1, 0),  // up
            Pair(-1, -1), // diagonal up-left
            Pair(1, -1)   // diagonal down-left
        )

        // Check if a position is within grid bounds
        fun isValid(row: Int, col: Int): Boolean =
            row in 0 until rows && col in 0 until cols


        // Search for XMAS starting from each position in each direction
        fun searchFromPosition(startRow: Int, startCol: Int, dir: Pair<Int, Int>): Boolean {
            val target = "XMAS"
            var row = startRow
            var col = startCol

            for (char in target) {
                if (!isValid(row, col) || grid[row][col] != char) {
                    return false
                }
                row += dir.first
                col += dir.second
            }
            return true
        }

        // Function to check one arm of the X pattern
        fun checkArm(startRow: Int, startCol: Int, rowDelta: Int, colDelta: Int, forward: Boolean): Boolean {
            val chars = if (forward) "MAS" else "MAS".reversed()
            for ((index, char) in chars.withIndex()) {
                val currentRow = startRow + (rowDelta * index)
                val currentCol = startCol + (colDelta * index)

                if (!isValid(currentRow, currentCol) || grid[currentRow][currentCol] != char) {
                    return false
                }
            }
            return true
        }

        if (part1) {
            // Iterate through each cell as a starting point
            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    // Only start searching if we find an 'X'
                    if (grid[row][col] == 'X') {
                        // Try each direction
                        for (dir in directions) {
                            if (searchFromPosition(row, col, dir)) {
                                count++
                            }
                        }
                    }
                }
            }
        } else {
            // For each potential 'A' position in the grid
            for (row in 1 until rows - 1) {
                for (col in 1 until cols - 1) {
                    if (grid[row][col] == 'A') {  // This is our center point
                        // Always start from above positions and check both directions
                        // Check all four combinations of forward/backward readings
                        if ((checkArm(row - 1, col - 1, 1, 1, true) &&
                                    checkArm(row - 1, col + 1, 1, -1, true)) ||    // Both forward
                            (checkArm(row - 1, col - 1, 1, 1, false) &&
                                    checkArm(row - 1, col + 1, 1, -1, true)) ||    // Left backward, right forward
                            (checkArm(row - 1, col - 1, 1, 1, true) &&
                                    checkArm(row - 1, col + 1, 1, -1, false)) ||   // Left forward, right backward
                            (checkArm(row - 1, col - 1, 1, 1, false) &&
                                    checkArm(row - 1, col + 1, 1, -1, false))
                        ) {   // Both backward
                            count++
                        }
                    }
                }
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        return countXMAS(input, true)
    }

    fun part2(input: List<String>): Int {
        return countXMAS(input, false)
    }


    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
