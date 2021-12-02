/**
 * Calculates the product of submarine`s horizontal travel by depth
 */
fun getProductOfCoordinates(commands: List<String>) :Int {
    var horizontal = 0
    var vertical = 0

    for (command in commands) {
        val splitted = command.split(" ")
        if (splitted[0] == "forward") {
            horizontal += splitted[1].toInt()
        } else if (splitted[0] == "down") {
            vertical += splitted[1].toInt()
        } else {
            vertical = Math.max(0, vertical - splitted[1].toInt())
        }
    }

    return horizontal * vertical
}

fun getAim(commands: List<String>) :Int {
    var horizontal = 0
    var vertical = 0
    var aim = 0

    for (command in commands) {
        val splitted = command.split(" ")
        if (splitted[0] == "forward") {
            horizontal += splitted[1].toInt()
            vertical += aim * splitted[1].toInt()
        } else if (splitted[0] == "down") {
            aim += splitted[1].toInt()
        } else {
            aim -= splitted[1].toInt()
        }
    }

    return horizontal * vertical
}

fun main() {
    fun part1(input: List<String>): Int {
        return getProductOfCoordinates(input)
    }

    fun part2(input: List<String>): Int {
        return getAim(input)
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}