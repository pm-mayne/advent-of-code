fun getDay8Instance(fileName: String): List<Instruction> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n")
        .map { it.toInstruction() }
}

private fun String.toInstruction(): Instruction {
    val typeStr = this.split(" ")[0]
    val value = Integer.parseInt(this.split(" ")[1])
    return when (typeStr) {
        "acc" -> AccInstruction(value)
        "jmp" -> JmpInstruction(value)
        else -> NopInstruction(value)
    }
}

abstract class Instruction(var visited: Boolean = false) {
    abstract fun run(pointer: Int): Int
}

data class JmpInstruction(val value: Int) : Instruction() {
    override fun run(pointer: Int): Int {
        return pointer + value
    }
}

data class NopInstruction(val value: Int) : Instruction() {
    override fun run(pointer: Int): Int {
        return pointer + 1
    }
}

data class AccInstruction(val value: Int) : Instruction() {
    override fun run(pointer: Int): Int {
        accumulator += value
        return pointer + 1
    }
}


var accumulator = 0

fun day8() {
    val instructionList = getDay8Instance("input_day_8")
    runTheThing(instructionList)
}

fun runTheThing(instructionList: List<Instruction>) {

    var pointer = 0
    var currentInstr = instructionList[pointer]
    while (!currentInstr.visited && pointer != instructionList.size - 1) {
        pointer = currentInstr.run(pointer)
        currentInstr.visited = true
        currentInstr = instructionList[pointer]
    }
    println("Pointer: " + pointer)
    if (pointer == instructionList.size - 1) {
        println("Accumulator at the end: " + accumulator)
    }
    //println("Accumulator: " + accumulator)
}

fun day8_2() {
    val instructionList = getDay8Instance("input_day_8")

    for (i in instructionList.indices) {
        accumulator = 0
        val changedInstructionList = instructionList.map {
            it.visited = false
            return@map it
        }.toMutableList()
        if (instructionList[i] is JmpInstruction) {
            changedInstructionList[i] = NopInstruction((instructionList[i] as JmpInstruction).value)
            runTheThing(changedInstructionList)
        }
        if (instructionList[i] is NopInstruction) {
            changedInstructionList[i] = JmpInstruction((instructionList[i] as NopInstruction).value)
            runTheThing(changedInstructionList)
        }
    }

}
