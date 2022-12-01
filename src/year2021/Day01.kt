package year2021

import readInput

fun main() {

    fun countIncreases(input: List<String>) :Int {
        var result = 0

        var prev :Int = input[0].toInt()

        for (depth in input) {
            if (depth.toInt() > prev) result++
            prev = depth.toInt()
        }

        return result
    }

    fun countIncreasesThree(input: List<String>) :Int {
        var result = 0

        var prev :Int = input[0].toInt() + input[1].toInt() + input[2].toInt()

        for (i in 2..(input.size-2)) {
            val sum :Int = input[i-1].toInt() + input[i].toInt() + input[i+1].toInt()
            if (sum > prev) result++
            prev = sum
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return countIncreases(input)
    }

    fun part2(input: List<String>): Int {
        return countIncreasesThree(input)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
