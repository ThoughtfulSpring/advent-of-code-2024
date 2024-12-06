fun main() {

    data class PageOrderingProblem(
        val rules: List<Pair<Int, Int>>,
        val updates: List<List<Int>>
    )

    fun parseInput(lines: List<String>): PageOrderingProblem {
        val splitIndex = lines.indexOf("")

        val rules = lines.take(splitIndex).map { line ->
            val (before, after) = line.split("|")
            before.toInt() to after.toInt()
        }

        val updates = lines.drop(splitIndex + 1).map { line ->
            line.split(",").map { it.toInt() }
        }

        return PageOrderingProblem(rules, updates)
    }

    fun isValidOrder(pages: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
        val applicableRules = rules.filter { (before, after) ->
            pages.contains(before) && pages.contains(after)
        }

        return applicableRules.all { (before, after) ->
            val beforeIndex = pages.indexOf(before)
            val afterIndex = pages.indexOf(after)
            beforeIndex < afterIndex
        }
    }

    fun findCorrectOrder(pages: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        // Build adjacency list for topological sort
        val graph = mutableMapOf<Int, MutableSet<Int>>()
        val inDegree = mutableMapOf<Int, Int>()

        // Initialize all pages with empty sets and 0 in-degree
        pages.forEach { page ->
            graph[page] = mutableSetOf()
            inDegree[page] = 0
        }

        // Add applicable rules to the graph
        rules.forEach { (before, after) ->
            if (pages.contains(before) && pages.contains(after)) {
                graph[before]?.add(after)
                inDegree[after] = inDegree.getOrDefault(after, 0) + 1
            }
        }

        // Perform topological sort using Kahn's algorithm
        val result = mutableListOf<Int>()
        val queue = ArrayDeque<Int>()

        // Add all nodes with 0 in-degree to queue
        inDegree.entries.filter { it.value == 0 }
            .forEach { queue.add(it.key) }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            result.add(current)

            graph[current]?.forEach { neighbor ->
                inDegree[neighbor] = inDegree[neighbor]!! - 1
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor)
                }
            }
        }

        return result
    }

    fun findMiddlePage(pages: List<Int>): Int {
        return pages[pages.size / 2]
    }


    fun part1(input: List<String>): Int {
        val problem = parseInput(input)

        return problem.updates
            .filter { isValidOrder(it, problem.rules) }
            .sumOf { findMiddlePage(it) }
    }

    fun part2(input: List<String>): Int {
        val problem = parseInput(input)

        return problem.updates
            .filterNot { isValidOrder(it, problem.rules) }
            .map { findCorrectOrder(it, problem.rules) }
            .sumOf { findMiddlePage(it) }
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
