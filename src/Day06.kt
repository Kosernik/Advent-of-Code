
fun main() {
    val input: List<String> = readInput("Day06")

    val counts: Map<Int, Long> = getCountOfFish(input)

    println(firstPuzzle(counts))
}

fun firstPuzzle(counts: Map<Int, Long>): Long {
    return getTotalNumberOfFish(getCountAfterXDays(counts, 256))
}

fun getTotalNumberOfFish(counts: Map<Int, Long>): Long {
    var totalNumber: Long = 0

    for (fish in counts) totalNumber += fish.value

    return totalNumber
}

fun getCountAfterXDays(counts: Map<Int, Long>, days: Int): Map<Int, Long> {
    var resultCount = counts

    for (day in 1..days) {
        val curCount = mutableMapOf<Int, Long>()

        for (fishCount: Map.Entry<Int, Long> in resultCount) {
            var curDay = fishCount.key

            if (curDay == 0) {
                curCount[8] = fishCount.value
                curDay = 7
            }

            curCount[curDay-1] = curCount.getOrDefault(curDay-1, 0) + fishCount.value
        }

        resultCount = curCount
    }

    return resultCount.toMap()
}

fun getCountOfFish(input: List<String>): Map<Int, Long> {
    val counts: MutableMap<Int, Long> = mutableMapOf()
    val splitInput = input[0].trim().split(",")

    for (fish in splitInput) {
        val fishDay = fish.toInt()
        counts[fishDay] = counts.getOrDefault(fishDay, 0) + 1
    }

    return counts.toMap()
}