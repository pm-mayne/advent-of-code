fun getDay7Instance(fileName: String): List<Rule> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .flatMap { it.toRule() }
}

data class Rule(val container: String, val contained: String, val number: Int = 0)

fun day7() {
    val rootBag = "shiny gold"
    val containers: MutableSet<String> = mutableSetOf()
    val day7Instance = getDay7Instance("input_day_7")
    trackContainers(rootBag, day7Instance, containers)

    println("Bags containing a Shiny Gold: " + containers.size)
}

fun day7_2() {
    val rootBag = "shiny gold"
    val day7Instance = getDay7Instance("input_day_7")
    val countContainers = countContainers(rootBag, day7Instance)

    println("Bags contained in a Shiny Gold: " + countContainers)
}

fun countContainers(rootBag: String, rules: List<Rule>): Int {
    return rules
        .filter { it.container == rootBag }
        .map {
            it.number + it.number * countContainers(it.contained, rules)
        }
        .sumBy { it }
}

fun trackContainers(rootBag: String, rules: List<Rule>, containers: MutableSet<String>) {
    rules
        .filter { it.contained == rootBag }
        .map {
            containers.add(it.container)
            trackContainers(it.container, rules, containers)
        }
}

private fun String.toRule(): List<Rule> {
    val container = this.split("bags contain")[0].trim()

    val containedRawStr = this.split("bags contain")[1]
    return containedRawStr.split(",")
        .map { it.trim() }
        .filter { !it.contains("no other bags") }
        .map {
            it.filter { it.isDigit() }.toInt() to
                    it.split(Regex("[0-9]"))[1]
        }
        .map { it.first to it.second.split("bag")[0] }
        .map { it.first to it.second.trim() }
        .map { Rule(container, it.second, it.first) }
}

