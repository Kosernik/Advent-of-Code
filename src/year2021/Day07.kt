package year2021

import readInput
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val input: List<String> = readInput("Day07")
    val parsedInput: MutableList<Int> = getIntInput(input)

    println(countAmountOfFuel(parsedInput))

    println(countAmountOfFuelProgression(parsedInput))
}

/**
 * Counts amount of fuel required to tove all drones to a single position.
 * Each step costs 1 more unit of fuel than the last. First step costs 1, second = 2, ...
 */
fun countAmountOfFuelProgression(parsedInput: MutableList<Int>): Long {
    val counts: Array<Long> = getCount(parsedInput)

    val dp: Array<Long> = Array(counts.size) {0}
    var minFuel: Long = Int.MAX_VALUE.toLong()

    for (i in dp.indices) {
        for (j in counts.indices) {
            dp[i] += (counts[j] * getArithmeticProgression(abs(j-i)))
        }
        minFuel = min(minFuel, dp[i])
    }

    return minFuel
}

/**
 * Counts the number of drones on each position.
 */
fun getCount(parsedInput: MutableList<Int>): Array<Long> {
    val counts: Array<Long> = Array(parsedInput[parsedInput.size-1]+1) {0}

    for (value in parsedInput) counts[value]++

    return counts
}

fun getArithmeticProgression(n: Int): Int = n * (n + 1) / 2

/**
 * Counts amount of fuel required to tove all drones to a single position.
 */
fun countAmountOfFuel(parsedInput: MutableList<Int>): Int {
    var result = 0

    parsedInput.sort()

    var endIdx = parsedInput.size-1
    for (idx in 0..(parsedInput.size/2)) {
        result += (parsedInput[endIdx--] - parsedInput[idx])
    }

    return result
}

/**
 * Converts input to a list of integers.
 */
fun getIntInput(input: List<String>): MutableList<Int> {
    val parsed = mutableListOf<Int>()

    for (line in input) {
        for (value in line.trim().split(",")) {
            parsed.add(value.toInt())
        }
    }

    return parsed
}