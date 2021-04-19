fun main(args: Array<String>) {
    day13_2()

}

class Puzzle {
    fun day1() {
        val list = getInstance("input_day_1")
            .map { Integer.parseInt(it) }

        val newList = list.toMutableList()
        newList
            .retainAll { list.contains(2020 - it) }

        val value = newList[0] * newList[1]
        print("Day One: " + value)
    }

    fun day1_2() {
        val list = getInstance("input_day_1")
            .map { Integer.parseInt(it) }

        val newList = list.toMutableList()
        newList
            .retainAll {
                val y = 2020 - it
                val tmp = list.toMutableList()
                tmp.retainAll { z ->
                    list.contains(y - z)
                }
                tmp.size == 2
            }
        println(newList)
        println("Day one part 2: " + newList[0] * newList[1] * newList[2])
    }

    fun day1_2_lisible() {
        val list = getInstance("input_day_1")
            .map { Integer.parseInt(it) }

        list
            .map { i ->
                list.map { j ->
                    list.map { k ->
                        if (i + j + k == 2020) {
                            println("" + i + " " + j + " " + k)
                            println(i * j * k)
                            return
                        }
                    }
                }
            }
    }

    fun day2() {
        val validPasswords = getInstance("input_day_2")
            .map { it.toPassword() }
            .filter{ it.isValid() }
            .count()
        
        println("Valid password count: " + validPasswords)
    }

    fun day2_2() {
        val validPasswords =  getInstance(("input_day_2"))
            .map { it.toPassword() }
            .filter{ it.isValid2() }
            .count()

        println("Valid password count: " + validPasswords)
    }
}

private fun Password.isValid2(): Boolean {
    return (password[minOcc-1] == letter) != (password[maxOcc-1] == letter)
}

private fun Password.isValid(): Boolean {
    return password.split(letter).size - 1 in minOcc..maxOcc
}

private fun String.toPassword(): Password {
    val split = this.split(": ")
    val ruleParams = split[0].split(" ")
    val boundParams = ruleParams[0].split("-")
    val minOcc = Integer.parseInt(boundParams[0])
    val maxOcc = Integer.parseInt(boundParams[1])
    val letter = ruleParams[1][0]
    val password = split[1]
    return Password(minOcc, maxOcc, letter, password)
}

data class Password(val minOcc: Int, val maxOcc: Int, val letter: Char, val password: String)

fun getInstance(fileName : String) : List<String> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .filter { it.isNotEmpty() }
}