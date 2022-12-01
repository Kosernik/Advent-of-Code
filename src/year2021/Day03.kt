package year2021

import readInput

/**
 * Computes the power consumption.
 */
fun getPowerConsumption(logs: List<String>): Int {
    val countOnes = IntArray(logs[0].length)
    val length = logs.size

    for (log in logs) {
        for (i in log.indices) {
            if (log[i] == '1') countOnes[i]++
        }
    }

    var gamma = ""
    var epsilon = ""

    for (idx in countOnes.indices) {
        if (countOnes[idx] * 2 > length) {
            gamma = gamma.plus("1")
            epsilon = epsilon.plus("0")
        } else {
            gamma = gamma.plus("0")
            epsilon = epsilon.plus("1")
        }
    }

    return gamma.toInt(2) * epsilon.toInt(2)
}

/**
 * Computes the support rating.
 */
fun getLifeSupportRating(logs: List<String>): Int {
    val oxygen = getOxygen(logs, 0)
    val co2 = getCO2(logs, 0)

    return oxygen.toInt(2) * co2.toInt(2)
}

/**
 * Computes the oxygen generator rating.
 */
fun getOxygen(logs: List<String>, idx: Int): String{
    if (logs.size == 1 || idx == logs[0].length-1) return logs[0]
    val mostCommon = getMostCommon(logs, idx)
    val reduced = mutableListOf<String>()

    for (log in logs) {
        if (log[idx] == mostCommon) reduced.add(log)
    }

    return getOxygen(reduced, idx+1)
}

/**
 * Computes the most common value at index 'idx' for each input in 'logs'.
 * If 0 and 1 are equally common, returns 1 in the position being considered.
 */
fun getMostCommon(logs: List<String>, idx: Int): Char {
    var countOnes = 0
    for (log in logs) {
        if (log[idx] == '1') countOnes++
    }
    return if (countOnes*2 >= logs.size) '1'
    else '0'
}

/**
 * Computes CO2 scrubber rating.
 */
fun getCO2(logs: List<String>, idx: Int): String{
    if (logs.size == 1 || idx == logs[0].length-1) return logs[0]
    val leastCommon = getLeastCommon(logs, idx)
    val reduced = mutableListOf<String>()

    for (log in logs) {
        if (log[idx] == leastCommon) reduced.add(log)
    }

    return getCO2(reduced, idx+1)
}

/**
 * Computes the least common value at index 'idx' for each input in 'logs'.
 * If 0 and 1 are equally common, returns 0 in the position being considered.
 */
fun getLeastCommon(logs: List<String>, idx: Int): Char {
    var countOnes = 0
    for (log in logs) {
        if (log[idx] == '1') countOnes++
    }
    return if (countOnes*2 >= logs.size) '0'
    else '1'
}

fun main() {
    fun part1(input: List<String>): Int {
        return getPowerConsumption(input)
    }

    fun part2(input: List<String>): Int {
        return getLifeSupportRating(input)
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}