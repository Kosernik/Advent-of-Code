package year2021

import readInput
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashSet

fun main() {
    val input: List<String> = readInput("Day15")

    val maze: List<List<Int>> = convertInput(input)

    println("First puzzle A*: " + aStar(maze))

    val scaledMaze: Array<Array<Int>> = scaleUpTheMaze(maze, 5)
    val convertedScaledMaze = convertArraysToLists(scaledMaze)
    println("Second puzzle A*: " + aStar(convertedScaledMaze))
}

fun aStar(maze: List<List<Int>>): Int {
    val height = maze.size
    val width = maze[0].size
    val hw = height + width

    // an array to get coordinates of 4 vertically and horizontally adjacent neighbours
    val coordinatesShifter = listOf(-1,0,1,0,-1)

    //              Pair<row, column> - already visited cells
    val visited: HashSet<Pair<Int, Int>> = HashSet()

    // comparing cells by potential distance to the target.
    val myComparator: Comparator<Pair<Pair<Int,Int>,Int>>
                        = Comparator {
            a: Pair<Pair<Int, Int>, Int>,
            b: Pair<Pair<Int, Int>, Int>
            -> (hw - a.first.first - a.first.second + a.second) -  (hw - b.first.first - b.first.second + b.second)}

    //                              Pair<Pair<row, column>, curLength>
    val priorityQueue: PriorityQueue<Pair<Pair<Int, Int>, Int>> = PriorityQueue(myComparator)

    // Finish cell.
    val target: Pair<Int, Int> = Pair(height-1, width-1)

    priorityQueue.offer(Pair(Pair(0, 0), 0))

    while (!priorityQueue.isEmpty()) {
        val curCell: Pair<Pair<Int, Int>, Int> = priorityQueue.poll()

        if (curCell.first == target) return curCell.second
        else if (visited.contains(curCell.first)) continue
        visited.add(curCell.first)

        val row = curCell.first.first
        val col = curCell.first.second
        val curPath = curCell.second

        for (i in 0 until 4) {
            val neighbourRow = row + coordinatesShifter[i]
            val neighbourCol = col + coordinatesShifter[i+1]

            if (0 <= neighbourRow && neighbourRow < height && 0 <= neighbourCol && neighbourCol < width) {
                val neighPair: Pair<Int, Int> = Pair(neighbourRow, neighbourCol)
                if (!visited.contains(neighPair)) {
                    priorityQueue.offer(Pair(neighPair, curPath + maze[neighbourRow][neighbourCol]))
                }
            }
        }
    }

    return -1
}

fun scaleUpTheMaze(maze: List<List<Int>>, scaleSize: Int): Array<Array<Int>> {
    val height = maze.size
    val width = maze[0].size
    val twoDArray: Array<Array<Int>> = Array(height * scaleSize) { Array(width * scaleSize) { 0 } }

    for (i in 0 until height) {
        for (j in 0 until width) {
            twoDArray[i][j] = maze[i][j]

            for (row in 1 until scaleSize) {
                twoDArray[i+height*row][j] = twoDArray[i+height*row - height][j] + 1
                if (twoDArray[i+height*row][j] == 10) twoDArray[i+height*row][j] = 1
            }
        }
    }

    for (countRows in 0 until scaleSize) {
            for (row in 0 until height) {
                for (col in 0 until width) {
                    for (countCol in 1 until scaleSize) {
                        twoDArray[row + height*countRows][col + width*countCol] = twoDArray[row + height*countRows][col + width*(countCol-1)] + 1
                        if (twoDArray[row + height*countRows][col + width*countCol] == 10) {
                            twoDArray[row + height*countRows][col + width*countCol] = 1
                        }
                    }
                }
            }
    }

    return twoDArray
}

fun convertInput(input: List<String>): List<List<Int>> {
    val maze: MutableList<List<Int>> = mutableListOf()

    for (line in input) {
        val curLine: MutableList<Int> = mutableListOf()

        for (ch in line) {
            curLine.add(ch-'0')
        }

        maze.add(curLine.toList())
    }

    return maze.toList()
}

fun convertArraysToLists(maze: Array<Array<Int>>): List<List<Int>> {
    val convertedMaze: MutableList<List<Int>> = mutableListOf()

    for (row in maze) {
        val curLine: MutableList<Int> = mutableListOf()
        for (cell in row) {
            curLine.add(cell)
        }
        convertedMaze.add(curLine.toList())
    }

    return convertedMaze.toList()
}