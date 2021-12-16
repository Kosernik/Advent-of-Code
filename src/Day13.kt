import kotlin.math.max

fun main() {
    val input: List<String> = readInput("Day13")

    val dots: MutableList<Pair<Int, Int>> = mutableListOf()
    val folds: MutableList<Pair<Int, Int>> = mutableListOf()
    val dimensions: Pair<Int, Int> = parseInput(input, dots, folds)

    val table: MutableList<MutableList<Byte>> = fillTable(dimensions, dots)

    // Puzzle 1
//    foldOneAxis(table, folds[0])
//    println(countDots(table))

    // Puzzle 2
    for (fold in folds) {
        foldOneAxis(table, fold)
    }
    printTableSpecialMode(table)

}

// Counts the number of dots on a table
fun countDots(table: MutableList<MutableList<Byte>>): Int {
    var count = 0
    val one: Byte = 1
    for (i in 0 until table.size) {
        for (j in 0 until table[0].size) {
            if (table[i][j] == one) {
                count++
            }
        }
    }
    return count
}

fun foldOneAxis(table: MutableList<MutableList<Byte>>, fold: Pair<Int, Int>) {
    if (fold.first == 0) {
        foldX(table, fold)
    } else {
        foldY(table, fold)
    }
}

fun foldY(table: MutableList<MutableList<Byte>>, fold: Pair<Int, Int>) {
    for (i in 0 until table.size) {
        for (j in 0 until fold.second) {
            val secondJ = table[0].size - j - 1
            table[i][j] = if (table[i][j] + table[i][secondJ] >= 1) 1 else 0
            table[i][secondJ] = 0
        }
    }

    for (i in 0 until table.size) {
        for (j in 0 until (table[0].size - fold.second)) {
            table[i].removeAt(table[i].size-1)
        }
    }
}
fun foldX(table: MutableList<MutableList<Byte>>, fold: Pair<Int, Int>) {
    for (i in 0 until fold.second) {
        for (j in 0 until table[0].size) {
            val secondI = table.size - i - 1
            table[i][j] = if (table[i][j] + table[secondI][j] >= 1) 1 else 0
            table[secondI][j] = 0
        }
    }

    for (i in 0 until (table.size - fold.second)) {
        table.removeAt(table.size-1)
    }
}

fun fillTable(dimensions: Pair<Int, Int>, dots: MutableList<Pair<Int, Int>>): MutableList<MutableList<Byte>> {
    val result: MutableList<MutableList<Byte>> = mutableListOf()

    // Инициализация 2d листа в Хуётлине :)
    for (i in 0 until dimensions.first) {
        result.add(mutableListOf())
        for (j in 0 until dimensions.second) {
            result[i].add(0)
        }
    }

    for (dot in dots) {
        result[dot.first][dot.second] = 1
    }

    return result
}

fun parseInput(input: List<String>,
               dots: MutableList<Pair<Int, Int>>, folds: MutableList<Pair<Int, Int>>): Pair<Int, Int> {

    var maxX = 0
    var maxY = 0

    var idx = 0
    while (input[idx] != "") {
        val split = input[idx].split(",")
        val pair: Pair<Int, Int> = Pair(split[0].toInt(), split[1].toInt())
        dots.add(pair)

        maxX = max(maxX, pair.first)
        maxY = max(maxY, pair.second)

        idx++
    }
    idx++

    while (idx < input.size) {
        val splitX: List<String> = input[idx].split("=")

        val pair: Pair<Int, Int> = Pair(if (splitX[0].contains('x')) 0 else 1, splitX[1].toInt())
        folds.add(pair)

        idx ++
    }

    return Pair(maxX+1, maxY+1)
}

fun printTable(table: MutableList<MutableList<Byte>>) {
    for (row in table) {
        println(row)
    }
    println()
}

fun printTableSpecialMode(table: MutableList<MutableList<Byte>>) {
    val one: Byte = 1
    for (i in 0 until table[0].size) {
        for (j in  0 until table.size) {
            print(if (table[j][i] == one) "#" else " ")
            print("")
        }
        println()
    }
    println()
}