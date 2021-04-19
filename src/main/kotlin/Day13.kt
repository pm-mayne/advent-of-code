fun getDay13Instance(fileName: String): List<List<String>> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.split(",") }
}

fun day13() {
    val day13Instance = getDay13Instance("input_day_13")
    val earliestTimestamp = day13Instance[0][0].toInt()
    val earliestBus = day13Instance[1]
        .filter { it != "x" }
        .map { it.toInt() }
        .map { it to (earliestTimestamp / it + 1) * it }
        .minByOrNull { it.second }!!

    println("Answer: " + earliestBus.first * (earliestBus.second - earliestTimestamp))

}

fun day13_2() {
    val day13Instance = getDay13Instance("input_day_13")[1]
        .map {
            if (it == "x")
                "1"
            else
                it
        }.map { it.toInt() }

    val heuristicStart: Long = 100000000000000L
    val bottleNeck = day13Instance.maxByOrNull { it }!!
    var timestamp: Long = ((heuristicStart / bottleNeck) + 1) * bottleNeck - day13Instance.indexOf(bottleNeck)
    var isRuleSatisfied = isRuleSatisfied(timestamp, day13Instance)

    while (!isRuleSatisfied) {
        timestamp += bottleNeck
        isRuleSatisfied = isRuleSatisfied(timestamp, day13Instance)
    }
    println("First Timestamp: " + timestamp)
}

fun isRuleSatisfied(timestamp: Long, rules: List<Int>): Boolean {
    return rules.withIndex()
        .all { ((timestamp + it.index.toLong()) % it.value.toLong()) == 0L }
}
