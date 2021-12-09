fun main() {
    val input: List<String> = readInput("Day09")

    val converted: List<List<Int>> = convertToNumbers(input)

    println(getSumOfLowPoints(converted))

    println(getProductOfLargestLakes(converted))
}

// Computes the product of areas of three larges lakes on a map.
fun getProductOfLargestLakes(map: List<List<Int>>): Int {
    var biggest = -1
    var medium = -1
    var smallest = -1

    val visited: MutableSet<String> = mutableSetOf()

    for (row in 1 until (map.size-1)) {
        for (col in 1 until (map[0].size-1)) {
            if (map[row][col] != 9 && visited.add(("$row*$col"))) {
                val curArea = getArea(map, row, col, visited)

                // Updating three maximum areas
                if (curArea > biggest) {
                    smallest = medium
                    medium = biggest
                    biggest = curArea
                } else if (curArea > medium) {
                    smallest = medium
                    medium = curArea
                } else if (curArea > smallest) {
                    smallest = curArea
                }
            }
        }
    }

    return biggest * medium * smallest
}

fun getArea(map: List<List<Int>>, row: Int, col: Int, visited: MutableSet<String>): Int {
    var result = 1

    // Checking neighbours if they are lower than 9 and they have not been visited yet.
    if (map[row-1][col] != 9 && !visited.contains("" + (row-1) + "*" + col)) {
        visited.add("" + (row-1) + "*" + col)
        result += getArea(map, row-1, col, visited)
    }
    if (map[row+1][col] != 9 && !visited.contains("" + (row+1) + "*" + col)) {
        visited.add("" + (row+1) + "*" + col)
        result += getArea(map, row+1, col, visited)
    }
    if (map[row][col-1] != 9 && !visited.contains("" + row + "*" + (col-1))) {
        visited.add("" + row + "*" + (col-1))
        result += getArea(map, row, col-1, visited)
    }
    if (map[row][col+1] != 9 && !visited.contains("" + row + "*" + (col+1))) {
        visited.add("" + row + "*" + (col+1))
        result += getArea(map, row, col+1, visited)
    }

    return result
}

// Computes the sum of lowest points.
fun getSumOfLowPoints(converted: List<List<Int>>): Int {
    var result = 0

    for (row in 1 until (converted.size-1)) {
        for (col in 1 until (converted[0].size-1)) {
            if (converted[row][col] < converted[row-1][col] && converted[row][col] < converted[row+1][col] &&
                converted[row][col] < converted[row][col-1] && converted[row][col] < converted[row][col+1]) {
                result += converted[row][col]+1
            }
        }
    }

    return result
}

// Converts input to 2d list of integers.
fun convertToNumbers(input: List<String>): List<List<Int>> {
    val converted = mutableListOf<List<Int>>()
    converted.add(getLineOfNines(input[0].length+2))

    for (line in input) {
        val numberLine = mutableListOf<Int>()
        numberLine.add(9)

        for (digit: Char in line) {
            numberLine.add(digit-'0')
        }

        numberLine.add(9)
        converted.add(numberLine.toList())
    }

    converted.add(getLineOfNines(input[0].length+2))
    return converted.toList()
}

fun getLineOfNines(length: Int): List<Int> {
    val line = mutableListOf<Int>()
    for (i in 0 until length) {
        line.add(9)
    }
    return line.toList()
}