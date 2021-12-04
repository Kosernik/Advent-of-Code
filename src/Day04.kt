// Возвращает список вытянутых номеров в порядке выпадания.
fun getNumbers(input: List<String>): List<Int> {
    val listOfNumbers = mutableListOf<Int>()
    val numbers = input[0].split(",")

    for (num in numbers) listOfNumbers.add(num.toInt())
    return listOfNumbers.toList()
}

// Возвращает все играющие карточки
// Ключ -> (Номар на карточке -> Координаты номера на карточке)
// Значение -> Массив 5*2: [i][0] - х-координата, [i][1] - у-координата
fun getBoards(input: List<String>): MutableMap<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>> {
    val board = mutableMapOf<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>>()
    for (i in 1 until input.size step 6) {
        val valuesToCoordinates = mutableMapOf<Int, Pair<Int, Int>>()
        for (row in 0..4) {
            val curRow = input[i+1+row].trim().split(Regex( "\\s+"))
            for (col in 0..4) {
                valuesToCoordinates[curRow[col].toInt()] = Pair(row, col)
            }
        }
        val marked = Array(5){Array(2) {0}}
        board[valuesToCoordinates] = marked
    }
    return board
}

// Возвращает итоговый результат победившей карточки.
// Модифицирует входные данные.
fun getBingoScore(numbers: List<Int>, boards: Map<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>>): Int {
    for (number in numbers) {
        for (board in boards) {
            val boardNumbers: Map<Int, Pair<Int, Int>> = board.key
            if (boardNumbers.containsKey(number)) {
                val coords: Pair<Int, Int>? = boardNumbers[number]
                val row = coords?.first
                val col = coords?.second
                val marked: Array<Array<Int>> = board.value
                marked[row!!][0] += 1
                marked[col!!][1] += 1

                if (marked[row!!][0] == 5 || marked[col!!][1] == 5) {
                    return calculateScore(board, number, numbers) * number
                }
            }
        }
    }
    return -1
}

// Возвращает сумму значений неотмеченых клеток.
fun calculateScore(board: Map.Entry<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>>, lastNumber: Int, numbers: List<Int>): Int {
    var totalSum = 0
    for (num in board.key) totalSum += num.key

    for (number in numbers) {
        if (board.key.containsKey(number)) totalSum -= number
        if (number == lastNumber) {
            break
        }
    }
    return totalSum
}

// Возвращает итоговый результат последней победившей карточки.
// Модифицирует входные данные.
fun getLastCardScore(numbers: List<Int>, boards: MutableMap<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>>): Int {
    var lastCardScore = -1
    var curBoards: MutableMap<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>> = boards

    for (number in numbers) {
        val nextBoards = mutableMapOf<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>>()
        for (board: MutableMap.MutableEntry<MutableMap<Int, Pair<Int, Int>>, Array<Array<Int>>> in curBoards) {
            val boardNumbers: Map<Int, Pair<Int, Int>> = board.key
            var toAdd = true
            if (boardNumbers.containsKey(number)) {
                val coords: Pair<Int, Int>? = boardNumbers[number]
                val row = coords?.first
                val col = coords?.second
                val marked: Array<Array<Int>> = board.value
                marked[row!!][0] += 1
                marked[col!!][1] += 1

                if (marked[row!!][0] == 5 || marked[col!!][1] == 5) {
                    lastCardScore = calculateScore(board, number, numbers) * number
                    toAdd = false
                }
            }
            if (toAdd) nextBoards[board.key] = board.value
        }
        if (nextBoards.isEmpty()) break
        curBoards = nextBoards
    }
    return lastCardScore
}

fun main() {
    val input = readInput("Day04")
    val numbers = getNumbers(input)

    var boards = getBoards(input)

    println(getBingoScore(numbers, boards))

    boards = getBoards(input)
    println(getLastCardScore(numbers, boards))
}