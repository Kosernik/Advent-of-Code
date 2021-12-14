import java.util.*

fun main() {
    val input: List<String> = readInput("Day10")

    val mapOfScoresCorrupted: Map<Char, Int> = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    println(getTotalScore(input, mapOfScoresCorrupted))

    val mapOfScoresIncomplete: Map<Char, Int> = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
    println(getIncompleteScore(input, mapOfScoresIncomplete))
}

// Computes the middle score of an incomplete string.
fun getIncompleteScore(input: List<String>, mapOfScores: Map<Char, Int>): Long {
    val listOfIncompleteScores: MutableList<Long> = mutableListOf()

    for (line in input) {
        val score = getIncompleteLineScore(line, mapOfScores)
        if (score != 0L) listOfIncompleteScores.add(score)
    }

    listOfIncompleteScores.sort()

    return listOfIncompleteScores[listOfIncompleteScores.size/2]
}

// Returns the score of an incomplete string or 0 if the line is valid or corrupted.
fun getIncompleteLineScore(line: String, mapOfScores: Map<Char, Int>): Long {
    val stack: Deque<Char> = ArrayDeque()

    for (letter in line) {
        if (letter == '(' || letter == '[' || letter == '{' || letter == '<') {
            stack.push(letter)
        } else {
            if (letter == ')') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return 0
                } else {
                    stack.pop()
                }
            }
            if (letter == ']') {
                if (stack.isEmpty() || stack.peek() != '[') {
                    return 0
                } else {
                    stack.pop()
                }
            }
            if (letter == '}') {
                if (stack.isEmpty() || stack.peek() != '{') {
                    return 0
                } else {
                    stack.pop()
                }
            }
            if (letter == '>') {
                if (stack.isEmpty() || stack.peek() != '<') {
                    return 0
                } else {
                    stack.pop()
                }
            }
        }
    }

    if (stack.isEmpty()) return 0

    var score = 0L

    while (!stack.isEmpty()) {
        score *= 5
        score += mapOfScores[stack.pop()]!!
    }

    return score
}

// Computes the score of corrupted lines
fun getTotalScore(input: List<String>, mapOfScores: Map<Char, Int>): Int {
    var score = 0

    for (line in input) {
        score += getCorruptedLineScore(line, mapOfScores)
    }

    return score
}

// Returns the score of a corrupted line or returns 0 if the line is valid or incomplete
fun getCorruptedLineScore(line: String, mapOfScores: Map<Char, Int>): Int {
    val stack: Deque<Char> = ArrayDeque()

    for (letter in line) {
        if (letter == '(' || letter == '[' || letter == '{' || letter == '<') {
            stack.push(letter)
        } else {
            if (letter == ')') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return mapOfScores[letter]!!
                } else {
                    stack.pop()
                }
            }
            if (letter == ']') {
                if (stack.isEmpty() || stack.peek() != '[') {
                    return mapOfScores[letter]!!
                } else {
                    stack.pop()
                }
            }
            if (letter == '}') {
                if (stack.isEmpty() || stack.peek() != '{') {
                    return mapOfScores[letter]!!
                } else {
                    stack.pop()
                }
            }
            if (letter == '>') {
                if (stack.isEmpty() || stack.peek() != '<') {
                    return mapOfScores[letter]!!
                } else {
                    stack.pop()
                }
            }
        }
    }

    return 0
}