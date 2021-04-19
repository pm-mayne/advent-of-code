fun getDay10Instance(fileName: String): List<Int> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.toInt() }
}

fun day10() {
    var count1JoltDiffs = 0
    var count3JoltDiffs = 0
    var currentJolt = 0
    getDay10Instance("input_day_10")
        .sorted()
        .withIndex()
        .map {
            val joltDiff = it.value - currentJolt
            if (joltDiff == 3) {
                count3JoltDiffs++
            }
            if (joltDiff == 1) {
                count1JoltDiffs++
            }
            currentJolt = it.value
            return@map it
        }

    count3JoltDiffs++ //That's for your device

    println("3 jolt diff: " + count3JoltDiffs)
    println("1 jolt diff: " + count1JoltDiffs)
    println("Product: " + count1JoltDiffs * count3JoltDiffs)
    println("Total adapters: " + getDay10Instance("input_day_10").size)
}

fun day10_2() {

    val adaptersInst = getDay10Instance("input_day_10")
        .sorted()
        .toMutableList()

    val device: Int = adaptersInst[adaptersInst.size - 1] + 3
    adaptersInst.add(0)
    adaptersInst.add(device)

    val adapters = adaptersInst.sorted()

    val essentialAdapters = adapters.filter { it.isEssential(adapters) }

    val produce = getGroups(adapters, essentialAdapters)
        .filter { it.isNotEmpty() }
        .map { it.getCombinations() }
        .reduce { acc, size -> acc * size }


    println(produce)

}

private fun List<Int>.getCombinations(): Long {
    //println("Combinations for " + this)
    val leftBound = this[0]
    val rightBound = this[this.size - 1]
    val subLists: Set<MutableList<Int>> = getSubLists()
        .map {
            if (it[0] != leftBound) it.add(leftBound)
            if (it[it.size - 1] != rightBound) it.add(rightBound)
            it.sorted().toSet().toMutableList()
        }.toSet()

    //println(subLists)
    val count : Long = subLists
        .filter { it.isValid() }
        .count().toLong()
    println("" + count)
    return count
}

private fun List<Int>.getSubLists(): Set<MutableList<Int>> {
    if (this.size == 2) {
        return setOf(
            this.toMutableList(),
            listOf(this[0]).toMutableList(),
            listOf(this[1]).toMutableList()
        )
    }
    val subs: MutableSet<MutableList<Int>> = mutableSetOf()
    for (i in this) {
        val tmp: MutableList<Int> = this.toMutableList()
        tmp.remove(i)
        subs.add(tmp)
        tmp.getSubLists()
            .map {
                subs.add(it)
            }
    }
    return subs
}

fun getGroups(adapters: List<Int>, essentialAdapters: List<Int>): List<List<Int>> {
    val groups: MutableList<MutableList<Int>> = mutableListOf()
    groups.add(mutableListOf())
    for (i in adapters.indices) {
        val adapter = adapters[i]
        if (essentialAdapters.contains(adapter)) {
            if (groups[groups.size - 1].isNotEmpty()) {
                groups[groups.size - 1].add(adapter)
            }
            groups.add(mutableListOf())
        } else {
            if (essentialAdapters.contains(adapters[i - 1])) {
                groups[groups.size - 1].add(adapters[i - 1])
            }
            groups[groups.size - 1].add(adapter)
        }

    }
    return groups
}

private fun Int.isEssential(adapters: List<Int>): Boolean {
    val index = adapters.indexOf(this)
    if (index >= adapters.size - 2) {
        return true
    }
    if (index == 0) {
        return true
    }
    return adapters[index + 1] - adapters[index - 1] > 3
}

private fun List<Int>.isValid(): Boolean {
    for (i in 1 until this.size) {
        if (this[i - 1] + 3 < this[i]) {
            return false
        }
    }
    return true
}

