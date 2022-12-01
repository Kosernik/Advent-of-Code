package year2021

import readInput
import java.util.*

fun main() {
    val input: List<String> = readInput("Day11")
    val table: MutableList<MutableList<Int>> = convertToInt(input)

    prntTable(table)

//    println(year2021.getTotalNumberOfFlashes(table, 100))
    println(getFirstAllFlash(table))
}

// Returns the number of a step after which all cells flashed
// Modifies the table!
fun getFirstAllFlash(table: MutableList<MutableList<Int>>): Int {
    var step = 1

    while (true) {
        val curFlashes = getNumberOfFlashes(table)
        if (curFlashes == table.size * table[0].size) return step
        step++
    }
}

// Returns the number of flashes after 'count' steps
// Modifies the table!
fun getTotalNumberOfFlashes(table: MutableList<MutableList<Int>>, count: Int): Int {
    var numberOfFlashes = 0

    for (i in 0 until count) {
        numberOfFlashes += getNumberOfFlashes(table)
    }

    return numberOfFlashes
}

// Counts the number of flashes during 1 step
// Modifies the table!
fun getNumberOfFlashes(table: MutableList<MutableList<Int>>): Int {
    var flashes = 0

    val queue: Deque<Pair<Int, Int>> = ArrayDeque()

    // Incrementing values in all cells by 1 and adding cells to a queue if necessary.
    for (row in 0 until table.size) {
        for (col in 0 until  table[0].size) {
            table[row][col]++
            if (table[row][col] == 10) {
                table[row][col] = 0
                queue.offer(Pair(row, col))
            }
        }
    }

    while (!queue.isEmpty()) {
        val curPair = queue.poll()
        val i = curPair.first
        val j = curPair.second

        flashes++

        /*
        Incrementing values in cell`s neighbours and adding them to the queue if necessary.
         */
        if (i-1 >= 0 && j-1 >= 0 && table[i-1][j-1] != 0) {
            table[i-1][j-1]++
            if (table[i-1][j-1] > 9) {
                table[i-1][j-1] = 0
                queue.offer(Pair(i-1, j-1))
            }
        }
        if (i-1 >= 0 && table[i-1][j] != 0) {
            table[i-1][j]++
            if (table[i-1][j] > 9) {
                table[i-1][j] = 0
                queue.offer(Pair(i-1, j))
            }
        }
        if (j-1 >= 0 && table[i][j-1] != 0) {
            table[i][j-1]++
            if (table[i][j-1] > 9) {
                table[i][j-1] = 0
                queue.offer(Pair(i, j-1))
            }
        }
        if (i+1 < table.size && j-1 >= 0 && table[i+1][j-1] != 0) {
            table[i+1][j-1]++
            if (table[i+1][j-1] > 9) {
                table[i+1][j-1] = 0
                queue.offer(Pair(i+1, j-1))
            }
        }
        if (i+1 < table.size && table[i+1][j] != 0) {
            table[i+1][j]++
            if (table[i+1][j] > 9) {
                table[i+1][j] = 0
                queue.offer(Pair(i+1, j))
            }
        }
        if (i-1 >= 0 && j+1 < table[0].size && table[i-1][j+1] != 0) {
            table[i-1][j+1]++
            if (table[i-1][j+1] > 9) {
                table[i-1][j+1] = 0
                queue.offer(Pair(i-1, j+1))
            }
        }
        if (j+1 < table[0].size && table[i][j+1] != 0) {
            table[i][j+1]++
            if (table[i][j+1] > 9) {
                table[i][j+1] = 0
                queue.offer(Pair(i, j+1))
            }
        }
        if (i+1 < table.size && j+1 < table[0].size && table[i+1][j+1] != 0) {
            table[i+1][j+1]++
            if (table[i+1][j+1] > 9) {
                table[i+1][j+1] = 0
                queue.offer(Pair(i+1, j+1))
            }
        }
    }

    return flashes
}

// Converts input into a 2d list of integers.
fun convertToInt(input: List<String>): MutableList<MutableList<Int>> {
    val result: MutableList<MutableList<Int>> = mutableListOf()

    for (line in input) {
        val list = mutableListOf<Int>()

        for (ch: Char in line) {
            list.add(ch-'0')
        }

        result.add(list)
    }

    return result
}

// Just prints the table
fun prntTable(table: MutableList<MutableList<Int>>) {
    for (lst in table) {
        for (entry in lst) {
            print(entry)
            print(" ")
        }
        println()
    }
    println()
}

