fun main() {
    val input: List<String> = readInput("Day08")

    println(count1478(input))

    println(getTotalSum(input))
}

//  be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
fun getTotalSum(input: List<String>): Int {
    var result = 0

    for (line in input) {
        val split: List<String> = line.split("|")

        result += getLineNumber(split)
    }

    return result
}

fun getLineNumber(split: List<String>): Int {
    val allNumbers: String = split[0].trim()

    val decoded: Array<Char> = decodeNumbers(allNumbers)

    var result = 0
    val numbers = split[1].trim().split(" ")

    for (number: String in numbers) {
        result *= 10
        result += getNumber(decoded, number)
    }

    return result
}

fun getNumber(decoded: Array<Char>, number: String): Int {
    when (number.length) {
        2 -> return 1
        3 -> return 7
        4 -> return 4
        7 -> return 8
    }
    if (number.length == 5) {
        return if (number.contains(decoded[4])) {
            2
        } else if (number.contains(decoded[1])) {
            5
        } else {
            3
        }
    } else {
        return if (!number.contains(decoded[2])) {
            6
        } else if (!number.contains(decoded[4])) {
            9
        } else {
            0
        }
    }
}

fun decodeNumbers(allNumbers: String): Array<Char> {
    val result: Array<Char> = Array(7) {'z'}

    val split = allNumbers.split(" ")
    val countSegments = mutableMapOf<Char, Int>()
    val countToNumber: MutableMap<Int, String> = mutableMapOf()

    for (word: String in split) {
        val trimmed = word.trim()
        if (trimmed.length == 2 || trimmed.length == 3 || trimmed.length == 4) countToNumber[trimmed.length] = trimmed

        // Count segments
        for (letter in trimmed) {
            countSegments[letter] = countSegments.getOrDefault(letter, 0) + 1
        }
    }

    for (entry in countSegments) {
        when (entry.value) {
            4 -> {
                // e-segment
                result[4] = entry.key
            }
            6 -> {
                // b-segment
                result[1] = entry.key
            }
            9 -> {
                // f-segment
                result[5] = entry.key
            }
        }
    }

    // c-segment
    result[2] = getC(countToNumber[2]!!, result[5])

    // a-segment
    result[0] = getA(countToNumber[3]!!, result[2], result[5])

    // d-segment
    result[3] = getD(countToNumber[4]!!, result[1], result[2], result[5])

    result[6] = getG(result)

    return result
}

fun getG(result: Array<Char>): Char {
    val setOfChars: MutableSet<Char> = mutableSetOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
    for (element in result) {
        setOfChars.remove(element)
    }
    return setOfChars.first()
}

fun getD(four: String, b: Char, c: Char, f: Char): Char {
    for (ch in four) {
        if (ch != b && ch != c && ch != f) return ch
    }
    // Should throw an exception
    return four[0]
}

fun getA(seven: String, c: Char, f: Char): Char {
    for (ch in seven) {
        if (ch != c && ch != f) return ch
    }
    // Should throw an exception
    return seven[0]
}

fun getC(one: String, f: Char): Char {
    return if (one[0] == f) {
        one[1]
    } else {
        one[0]
    }
}

//  Puzzle 1
fun count1478(input: List<String>): Int {
    var result = 0

    for (line in input) {
        val split: List<String> = line.split("|")

        result += helperCount1478(split[1].trim())
    }

    return result
}

fun helperCount1478(s: String): Int {
    var result = 0

    for (word in s.split(" ")) {
        if (word.length == 2 || word.length == 3 || word.length == 4 || word.length == 7) {
            result += 1
        }
    }
    return result
}