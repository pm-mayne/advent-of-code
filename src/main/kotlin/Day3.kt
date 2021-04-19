fun getDay3Instance(fileName: String): List<String> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .filter { it.isNotEmpty() }
}


fun day3() {
    val day3Instance = getDay3Instance("input_day_3")

    val treeCount = getTreeCountWithSlope(3, 1, day3Instance)

    println("Tree Count: " + treeCount)

}

fun day3_2() {
    val day3Instance = getDay3Instance("input_day_3")

    val treeCount11 = getTreeCountWithSlope(1, 1, day3Instance).toLong()
    val treeCount31 = getTreeCountWithSlope(3, 1, day3Instance).toLong()
    val treeCount51 = getTreeCountWithSlope(5, 1, day3Instance).toLong()
    val treeCount71 = getTreeCountWithSlope(7, 1, day3Instance).toLong()
    val treeCount12 = getTreeCountWithSlope(1, 2, day3Instance).toLong()

    println("Tree counts: " + treeCount11 + " " + treeCount31 + " " + treeCount51 + " " + treeCount71 + " " + treeCount12)
    val prod: Long = treeCount11 * treeCount31 * treeCount51 * treeCount71 * treeCount12
    println("Tree Count: " + prod)

}

fun getTreeCountWithSlope(right: Int, down: Int, instance: List<String>): Int {
    return instance.withIndex()
        .filter { it.index % down == 0 }
        .map { it.value[(it.index * right) % it.value.length] }
        .filter { it == '#' }
        .count()
}
