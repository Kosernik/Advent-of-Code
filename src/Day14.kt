import kotlin.math.max
import kotlin.math.min

fun main() {
    val input: List<String> = readInput("Day14")

    var polymer: String = input[0]
    val mapping: Map<String, Char> = getMapping(input)

//     Puzzle 1
//    for (i in 0 until 10) {
//        polymer = buildPolymer(polymer, mapping)
//    }
//
//    val minMax = getHighAndLowString(polymer)
//    println(minMax.first - minMax.second)

    var polyMap: MutableMap<String, Long> = convertToPairs(input[0])
    for (i in 0 until 40) {
        polyMap = buildPolyPairs(polyMap, mapping)
    }

    val maxMin = getHighAndLowMap(polyMap, input[0])
    println(maxMin.first - maxMin.second)
}

// Returns the counts of the most common and the least common characters.
fun getHighAndLowString(polymer: String): Pair<Long, Long> {
    val count: MutableMap<Char, Long> = mutableMapOf()

    for (letter in polymer) {
        count[letter] = count.getOrDefault(letter, 0) + 1
    }

    var max = 0L
    var min = Long.MAX_VALUE

    for (c in count) {
        max = max(max, c.value)
        min = min(min, c.value)
    }

    return Pair(max, min)
}
// Returns the counts of the most common and the least common characters.
fun getHighAndLowMap(polyMap: MutableMap<String, Long>, originalPoly: String): Pair<Long, Long> {
    val count: MutableMap<Char, Long> = mutableMapOf()
    count[originalPoly[0]] = 1
    count[originalPoly[originalPoly.length-1]] = 1

    for (entry in polyMap) {
        val first = entry.key[0]
        val second = entry.key[1]

        count[first] = count.getOrDefault(first, 0) + entry.value
        count[second] = count.getOrDefault(second, 0) + entry.value
    }

    var max = 0L
    var min = Long.MAX_VALUE

    for (c in count) {
        max = max(max, c.value)
        min = min(min, c.value)
    }

    return Pair(max/2, min/2)
}

// Converts the polymer template into a map of pairs of elements -> count of a pair.
fun convertToPairs(polymer: String): MutableMap<String, Long> {
    val pairs: MutableMap<String, Long> = mutableMapOf()

    for (i in 1 until polymer.length) {
        val pair = polymer.substring(i-1, i+1)
        pairs[pair] = pairs.getOrDefault(pair, 0) + 1
    }

    return pairs
}

fun buildPolyPairs(polyMap: MutableMap<String, Long>, mapping: Map<String, Char>): MutableMap<String, Long> {
    val pairs: MutableMap<String, Long> = mutableMapOf()

    for (polyPair in polyMap) {
        val middleChar = mapping.getOrDefault(polyPair.key, "-")
        if (middleChar == "-") continue

        val firstPair: String = "" + polyPair.key[0] + middleChar
        val secondPair: String = "" + middleChar + polyPair.key[1]

        pairs[firstPair] = pairs.getOrDefault(firstPair, 0) + polyPair.value
        pairs[secondPair] = pairs.getOrDefault(secondPair, 0) + polyPair.value
    }

    return pairs
}

// Bruteforce
fun buildPolymer(polymer: String, mapping: Map<String, Char>): String {
    val builder: StringBuilder = StringBuilder()
    builder.append(polymer[0])

    for (i in 1 until polymer.length) {
        val pair = polymer.substring(i-1, i+1)
        builder.append(mapping.getOrDefault(pair, ""))
        builder.append(polymer[i])
    }

    return builder.toString()
}

fun getMapping(input: List<String>): Map<String, Char> {
    val mapping = mutableMapOf<String, Char>()

    for (i in 2 until input.size) {
        val split = input[i].split(" -> ")

        mapping[split[0].trim()] = split[1].trim()[0]
    }

    return mapping
}