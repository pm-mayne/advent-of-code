fun getDay5Instance(fileName: String): List<String> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .filter { it.isNotEmpty() }
}

fun day5() {

    val day5Instance = getDay5Instance("input_day_5")

    val maxSeatID = day5Instance.map { it.toRow() to it.toColumn() }
        .map { it.first * 8 + it.second }
        .maxByOrNull { it }

    println("Max Seat ID: " + maxSeatID)

}

fun day5_2() {

    val day5Instance = getDay5Instance("input_day_5")

    val seatIDs = day5Instance.map { it.toRow() to it.toColumn() }
        .map { it.first * 8 + it.second}
        .sortedBy { it }

    val missingSeatID = getMissingSeat(seatIDs)

    println("My Seat is: " + missingSeatID)

}

fun getMissingSeat(seatIDs: List<Int>): Int {
    for (i in 1 until seatIDs.size) {
        if(seatIDs[i] > seatIDs[i-1] + 1) {
            return seatIDs[i] - 1
        }
    }
    return -1
}

private fun String.toRow(): Int {
    return binaryMap(this, 'F', 'B')
}

private fun String.toColumn(): Int {
    return binaryMap(this, 'L', 'R')
}

fun binaryMap(input: String, zero: Char, one: Char): Int {
    return input.map { when (it) {
        zero -> '0'
        one -> '1'
        else -> ""
    } }.joinToString("")
        .toByte(2)
        .toInt()
}
