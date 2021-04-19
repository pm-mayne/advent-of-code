import kotlin.math.abs

fun getDay12Instance(fileName: String): List<Action> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.toFerryMove() }
}

val waypoint = Waypoint(Position("N", lat = 1, long = 10))
val ferry = Ferry(Position("N", lat = 0, long = 0))

fun getDay12_2Instance(fileName: String): List<Action> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.toFerryWaypointMove() }
}

private fun String.toFerryMove(): Action {
    val moveType = substring(0, 1)
    val moveValue = substring(1, length).toInt()
    return when (moveType) {
        "N" -> createMove("N", moveValue)
        "S" -> createMove("S", moveValue)
        "E" -> createMove("E", moveValue)
        "W" -> createMove("W", moveValue)
        "L" -> RotateLeft(moveValue)
        "R" -> RotateRight(moveValue)
        else -> ForwardMove(moveValue)
    }
}

private fun String.toFerryWaypointMove(): Action {
    val moveType = substring(0, 1)
    val moveValue = substring(1, length).toInt()
    return when (moveType) {
        "N" -> createMove("N", moveValue)
        "S" -> createMove("S", moveValue)
        "E" -> createMove("E", moveValue)
        "W" -> createMove("W", moveValue)
        "L" -> WayPointRotateLeft(moveValue)
        "R" -> WayPointRotateRight(moveValue)
        else -> ForwardToWaypointMove(moveValue, waypoint)
    }
}

fun day12() {
    val ferryMoves = getDay12Instance("input_day_12")

    var ferryPosition = Position("E", 0, 0)

    ferryMoves
        .map {
            val newPosition = it.move(ferryPosition)
            println("" + ferryPosition + ", move " + it + ", new pos: " + newPosition)
            ferryPosition = newPosition
        }

    println("End position is: " + ferryPosition)
    println("Manhattan distance is: " + (abs(ferryPosition.lat) + abs(ferryPosition.long)))
}

fun day12_2() {
    val ferryMoves = getDay12_2Instance("input_day_12")


    ferryMoves
        .map {
            if (it.isFerryAction) {
                val newPosition = it.move(ferry.pos)
                println("MOVE " + ferry.pos + ", move " + it + ", new pos: " + newPosition)
                ferry.pos = newPosition
            } else {
                val newPosition = it.move(waypoint.pos)
                println("ADJUST WAYPOINT " + waypoint.pos + ", move " + it + ", new pos: " + newPosition)
                waypoint.pos = newPosition
            }
        }

    println("End position is: " + ferry.pos)
    println("Manhattan distance is: " + (abs(ferry.pos.lat) + abs(ferry.pos.long)))
}

data class Waypoint(var pos: Position)
data class Ferry(var pos: Position)

val headings = listOf("N", "E", "S", "W")

abstract class Action(val isFerryAction: Boolean = false) {
    abstract fun move(pos: Position): Position
}

data class Position(val heading: String, val lat: Int, val long: Int)

fun createMove(heading: String, moveValue: Int): Action {
    return when (heading) {
        "N" -> NorthMove(moveValue)
        "S" -> SouthMove(moveValue)
        "E" -> EastMove(moveValue)
        else -> WestMove(moveValue)
    }
}

data class WestMove(val length: Int) : Action() {
    override fun move(pos: Position): Position {
        return Position(pos.heading, pos.lat, pos.long - length)
    }
}


data class NorthMove(val length: Int) : Action() {
    override fun move(pos: Position): Position {
        return Position(pos.heading, pos.lat + length, pos.long)
    }
}


data class SouthMove(val length: Int) : Action() {
    override fun move(pos: Position): Position {
        return Position(pos.heading, pos.lat - length, pos.long)
    }
}


data class EastMove(val length: Int) : Action() {
    override fun move(pos: Position): Position {
        return Position(pos.heading, pos.lat, pos.long + length)
    }
}

data class ForwardMove(val length: Int) : Action(true) {
    override fun move(pos: Position): Position {
        return createMove(pos.heading, length).move(pos)
    }
}

data class ForwardToWaypointMove(val length: Int, val waypoint: Waypoint) : Action(true) {
    override fun move(pos: Position): Position {
        return Position(
            lat = pos.lat + length * waypoint.pos.lat,
            long = pos.long + length * waypoint.pos.long,
            heading = pos.heading
        )
    }
}

data class RotateLeft(val degree: Int) : Action() {
    override fun move(pos: Position): Position {
        return RotateRight(360 - (degree % 360)).move(pos)
    }
}

data class RotateRight(val degree: Int) : Action() {
    override fun move(pos: Position): Position {
        return Position(
            headings[(headings.indexOf(pos.heading) + degree / 90) % headings.size],
            lat = pos.lat,
            long = pos.long
        )
    }
}

data class WayPointRotateLeft(val degree: Int) : Action() {
    override fun move(pos: Position): Position {
        return when (degree % 360) {
            90 -> Position(
                lat = pos.long,
                long = - pos.lat,
                heading = pos.heading
            )
            180 -> Position(
                lat = - pos.lat,
                long = - pos.long,
                heading = pos.heading
            )
            270 -> Position(
                lat = - pos.long,
                long =  pos.lat,
                heading = pos.heading
            )
            else -> Position(
                lat = pos.lat,
                long =  pos.long,
                heading = pos.heading
            )
        }
    }
}

data class WayPointRotateRight(val degree: Int) : Action() {
    override fun move(pos: Position): Position {
        return WayPointRotateLeft(360 - (degree % 360)).move(pos)
    }
}