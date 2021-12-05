import kotlin.math.max

fun main() {
    val input: List<String> = readInput("Day05")

    val coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>> = parseCoordinates(input)

    val maxDimensions = getMaxSize(coordinates)
    val board: Array<Array<Int>> = Array(maxDimensions.first+1) { Array(maxDimensions.second+1) {0}}

    fillBoardHorizontalAndVertical(board, coordinates)

    println(firstPuzzle(board))

    fillBoardDiagonal(board, coordinates)

    println(firstPuzzle(board))
}

fun printBoard(board: Array<Array<Int>>) {
    for (row in board) {
        for (col in row) {
            print(col)
            print(" ")
        }
        println()
    }
}

fun firstPuzzle(board: Array<Array<Int>>): Int {
    var result = 0

    for (row: Array<Int> in board) {
        for (col: Int in row) {
            if (col >= 2) result++
        }
    }

    return result
}

fun fillBoardDiagonal(board: Array<Array<Int>>, coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
    for (line in coordinates) {

        if (isValidDiagonalLine(line)) {
            var rowCell = line.first.first
            val rowDirection = if ((line.first.first - line.second.first) < 0)  1 else -1

            var colCell = line.first.second
            val  colDirection = if ((line.first.second - line.second.second) < 0)  1 else -1

            for (idx in 0..(Math.abs(line.first.first-line.second.first))) {
                board[rowCell][colCell] += 1
                rowCell += rowDirection
                colCell += colDirection
            }
        }
    }
}

fun fillBoardHorizontalAndVertical(board: Array<Array<Int>>, coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
    for (line: Pair<Pair<Int, Int>, Pair<Int, Int>> in coordinates) {
        if (isValidHorizontalOrVerticalLine(line)) {
            val rowStart = Math.min(line.first.first, line.second.first)
            val rowEnd = Math.max(line.first.first, line.second.first)

            val colStart = Math.min(line.first.second, line.second.second)
            val colEnd = Math.max(line.first.second, line.second.second)

            for (row: Int in rowStart..rowEnd) {
                for (col: Int in colStart..colEnd) {
                    board[row][col] += 1
                }
            }
        }
    }
}

/**
 * Returns true if a line is diagonal. False - otherwise.
 */
fun isValidDiagonalLine(line: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    if (line.first.first+line.first.second == line.second.first+line.second.second) return true
    else if (line.first.first-line.second.first == line.first.second-line.second.second) return true
    return false
}

/**
 * Returns true if a line is horizontal or vertical. False - otherwise.
 */
fun isValidHorizontalOrVerticalLine(line: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    if (line.first.first == line.second.first || line.first.second == line.second.second) return true
    return false
}

/**
 * Returns the pair of maximum 'x' and 'y'.
 */
fun getMaxSize(coordinates: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Pair<Int, Int> {
    var maxX = 0
    var maxY = 0

    for (pair: Pair<Pair<Int, Int>, Pair<Int, Int>> in coordinates) {
        maxX = max(maxX, max(pair.first.first, pair.second.first))
        maxY = max(maxY, max(pair.first.second, pair.second.second))
    }

    return Pair(maxX, maxY)
}

/**
 * Returns a list of pairs of coordinates of lines.
 * Pair< Pair<x1, y1>, Pair<x2, y2> >
 */
fun parseCoordinates(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val listOfCoordinates = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    for (entry: String in input) {
        val split: List<String> = entry.trim().split("->")

        val start: List<String> = split[0].trim().split(",")
        val startPair: Pair<Int, Int> = Pair(start[0].toInt(), start[1].toInt())

        val end: List<String> = split[1].trim().split(",")
        val endPair: Pair<Int, Int> = Pair(end[0].toInt(), end[1].toInt())

        listOfCoordinates.add(Pair(startPair, endPair))
    }

    return listOfCoordinates.toList()
}