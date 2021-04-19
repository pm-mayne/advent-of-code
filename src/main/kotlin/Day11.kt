fun getDay11Instance(fileName: String): List<List<String>> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.split("") }
}

private fun List<List<String>>.createSeatings(): List<Seat> {
    val seats = this.withIndex().map { l ->
        l.value
            .withIndex()
            .map { x -> Seat(value = x.value, i = l.index, j = x.index) }
    }
    for (i in seats.indices) {
        for (j in seats[i].indices) {
            if (j < seats[i].size - 1) {
                seats[i][j].adjacentSeats.add(seats[i][j + 1])
                if (i < seats.size - 1) {
                    seats[i][j].adjacentSeats.add(seats[i + 1][j + 1])
                }
                if (i > 0) {
                    seats[i][j].adjacentSeats.add(seats[i - 1][j + 1])
                }
            }
            if (i < seats.size - 1) {
                seats[i][j].adjacentSeats.add(seats[i + 1][j])
            }
            if (j > 0) {
                seats[i][j].adjacentSeats.add(seats[i][j - 1])
                if (i < seats.size - 1) {
                    seats[i][j].adjacentSeats.add(seats[i + 1][j - 1])
                }
                if (i > 0) {
                    seats[i][j].adjacentSeats.add(seats[i - 1][j - 1])
                }
            }
            if (i > 0) {
                seats[i][j].adjacentSeats.add(seats[i - 1][j])
            }
        }
    }
    return seats.flatten()
}

data class Seat(
    val adjacentSeats: MutableSet<Seat> = mutableSetOf(),
    var value: String,
    var nextValue: String = "",
    val i: Int,
    val j: Int
) {
    fun next() {
        value = nextValue
        nextValue = ""
    }

    fun mutate() {
        /*
        If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
        If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
         */
        if (value == "L" && adjacentSeats.none { it.value == "#" }) {
            nextValue = "#"
        } else if (value == "#" && (adjacentSeats.filter { it.value == "#" }.count() > 4)) {
            nextValue = "L"
        } else {
            nextValue = value
        }
    }

    fun isUnchanged(): Boolean {
        return nextValue == value
    }

    override fun hashCode(): Int {
        return i * 1000 + j
    }
}

fun day11() {
    val seatings = getDay11Instance("input_day_11")
        .createSeatings()

    var isSystemInvariant = false
    while (!isSystemInvariant) {
        isSystemInvariant = seatings.map {
            it.mutate()
            it
        }
            .all {
                it.isUnchanged()
            }

        seatings.map {
            it.next()
            it
        }
    }
    val count = seatings.filter { it.value == "#" }
        .count()

    println("Number of occupied seats: " + count)
}

fun day11_2() {
    val seatings = getDay11Instance("input_day_11")
        .createSeatingsForPart2()

    var isSystemInvariant = false
    while (!isSystemInvariant) {
        isSystemInvariant = seatings.map {
            it.mutate()
            it
        }
            .all {
                it.isUnchanged()
            }

        seatings.map {
            it.next()
            it
        }
    }
    val count = seatings.filter { it.value == "#" }
        .count()

    println("Number of occupied seats: " + count)
}

private fun List<List<String>>.createSeatingsForPart2(): List<Seat> {
    val seats = this.withIndex()
        .map { l ->
        l.value
            .withIndex()
            .filter { x -> x.value.isNotEmpty() }
            .map { x -> Seat(value = x.value, i = l.index, j = x.index) }
    }
    for (i in seats.indices) {
        for (j in seats[i].indices) {
            val curr = seats[i][j]
            if (j < seats[i].size - 1) {
                getFirstRightSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                if (i < seats.size - 1) {
                    getFirstBRDiagSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                }
                if (i > 0) {
                    getFirstTRDiagSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                }
            }
            if (i < seats.size - 1) {
                getFirstBottomSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
            }
            if (j > 0) {
                getFirstLeftSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                if (i < seats.size - 1) {
                    getFirstBLDiagSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                }
                if (i > 0) {
                    getFirstTLDiagSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
                }
            }
            if (i > 0) {
                getFirstTopSeat(i, j, seats)?.let { seats[i][j].adjacentSeats.add(it) }
            }

            //println("Seat[" + i + "][" + j + "](" + seats[i][j].value + ")")
            //println("Adjacent Seats: " + seats[i][j].adjacentSeats.map { "s[" + it.i + "][" + it.j + "](" + it.value + ")" })
        }
    }
    return seats.flatten()
}

fun getFirstBRDiagSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    var verticalIndex = i + 1
    for (lateralIndex in j + 1 until seats[i].size) {
        if (verticalIndex > seats.size - 1) {
            return null
        }
        if (seats[verticalIndex][lateralIndex].value != ".") {
            return seats[verticalIndex][lateralIndex]
        }
        verticalIndex++
    }
    return null
}

fun getFirstTRDiagSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    var verticalIndex = i - 1
    for (lateralIndex in j + 1 until seats[i].size) {
        if (verticalIndex < 0) {
            return null
        }
        if (seats[verticalIndex][lateralIndex].value != ".") {
            return seats[verticalIndex][lateralIndex]
        }
        verticalIndex--
    }
    return null
}

fun getFirstBLDiagSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    var verticalIndex = i + 1
    for (lateralIndex in j - 1 downTo 0) {
        if (verticalIndex > seats.size - 1) {
            return null
        }
        if (seats[verticalIndex][lateralIndex].value != ".") {
            return seats[verticalIndex][lateralIndex]
        }
        verticalIndex++
    }
    return null
}

fun getFirstTLDiagSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    var verticalIndex = i - 1
    for (lateralIndex in j - 1 downTo 0) {
        if (verticalIndex < 0) {
            return null
        }
        if (seats[verticalIndex][lateralIndex].value != ".") {
            return seats[verticalIndex][lateralIndex]
        }
        verticalIndex--
    }
    return null
}

fun getFirstLeftSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    for (lateralIndex in j - 1 downTo 0) {
        if (seats[i][lateralIndex].value != ".") {
            return seats[i][lateralIndex]
        }
    }
    return null
}

fun getFirstRightSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    for (lateralIndex in j + 1 until seats[i].size) {
        if (seats[i][lateralIndex].value != ".") {
            return seats[i][lateralIndex]
        }
    }
    return null
}

fun getFirstTopSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    for (verticalIndex in i - 1 downTo 0) {
        if (seats[verticalIndex][j].value != ".") {
            return seats[verticalIndex][j]
        }
    }
    return null
}

fun getFirstBottomSeat(i: Int, j: Int, seats: List<List<Seat>>): Seat? {
    for (verticalIndex in i + 1 until seats.size) {
        if (seats[verticalIndex][j].value != ".") {
            return seats[verticalIndex][j]
        }
    }
    return null
}