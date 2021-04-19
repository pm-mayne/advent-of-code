fun getDay6Instance(fileName: String): List<List<String>> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n\n")
        .map { it.split("\n") }
}

fun day6() {

    val day6Instance = getDay6Instance("input_day_6")

    val count = day6Instance
        .map {
            it
                .flatMap { s -> s.toList() }
                .toSet()
                .size
        }
        .sum()

    println("Total yes: " + count)

}

fun day6_2() {

    val day6Instance = getDay6Instance("input_day_6")

    val count = day6Instance
        .map {
            it
                .toGroup()
                .countUnanimous()
        }
        .sum()

    println("Total yes: " + count)

}

private fun List<String>.toGroup(): Group {
    return Group(this)
}

class Group(val data: List<String>) {

    fun countUnanimous(): Int {
        val allAnswers = data.flatMap {
            it.toList()
        }

        return allAnswers.toSet()
            .filter { (allAnswers.filter { c -> c == it }.count()) == data.size }
            .count()
    }
}