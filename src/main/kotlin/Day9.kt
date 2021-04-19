fun getDay9Instance(fileName: String): List<Long> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.toLong() }
}

fun day9() {
    val instance = getDay9Instance("input_day_9")

    val first = instance
        .withIndex()
        .first { it.index >= 25 && !it.value.hasSumIn(instance.subList(it.index - 25, it.index)) }
        .value

    println("First anomaly number: " + first)
}

private fun Long.hasSumIn(instance: List<Long>): Boolean {
    return instance.any { instance.contains(this - it) }
}

fun day9_2() {
    val instance = getDay9Instance("input_day_9")

    val datNumber : Long = 41682220 //dis son of a bitch number

    val first = instance
        .withIndex()
        .map { getSumForItIn(datNumber, instance.subList(it.index, instance.size - 1)) }
        .first { it.isNotEmpty() }

    val max = first.maxByOrNull { it }
    val min = first.minByOrNull { it }
    println("Consecutive series min and max: " + listOf(min, max))
    println("Consecutive series min and max sum: " + (min!! + max!!))

}

private fun getSumForItIn(datNumber: Long, instance: List<Long>): List<Long> {
    var count : Long = 0
    for (i in instance.indices) {
        count += instance[i]
        if(count > datNumber){
            return emptyList()
        }
        if(count == datNumber) {
            return instance.subList(0, i)
        }
    }
    return emptyList()
}
