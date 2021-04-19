fun getDay4Instance(fileName: String): List<String> {
    return Puzzle::class.java.getResource(fileName).readText()
        .split("\n\n")
        .filter { it.isNotEmpty() }
}

fun day4() {
    val day4Instance = getDay4Instance("input_day_4")
        .map {
            it.split(" ")
                .flatMap { s -> s.split("\n") }

        }
        .map {
            it
                .filter { x -> x.isNotEmpty() }
                .map { s ->
                    return@map s.split(":")[0] to s.split(":")[1]
                }
                .toMap()
        }
        .map { x -> x.toPassport() }
        .filter { p -> p.isValid() }
        .filter { p -> p.areFieldsValid() }
        .count()



    println(day4Instance)
}


private fun Map<String, String>.toPassport(): Passport {
    return Passport(
        get("byr"),
        get("iyr"),
        get("eyr"),
        get("hgt"),
        get("hcl"),
        get("ecl"),
        get("pid"),
        get("cid")
    )
}

data class Passport(
    val byr: String?,
    val iyr: String?,
    val eyr: String?,
    val hgt: String?,
    val hcl: String?,
    val ecl: String?,
    val pid: String?,
    val cid: String?
) {
    fun isValid(): Boolean {
        return byr != null
        && iyr != null
        && eyr != null
        && hgt != null
        && hcl != null
        && ecl != null
        && pid != null
    }

    fun areFieldsValid(): Boolean {
        return isByrValid(byr)
                && isIyrValid(iyr)
                && isEyrValid(eyr)
                && isHgtValid(hgt)
                && isHclValid(hcl)
                && isEclValid(ecl)
                && isPidValid(pid)
    }

    private fun isPidValid(pid: String?): Boolean {
        //pid (Passport ID) - a nine-digit number, including leading zeroes.
        return pid!!.matches(Regex("^[0-9]{9}$"))
    }

    private fun isEclValid(ecl: String?): Boolean {
        //ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        return ecl == "amb"
                || ecl == "blu"
                || ecl == "brn"
                || ecl == "gry"
                || ecl == "grn"
                || ecl == "hzl"
                || ecl == "oth"
    }

    private fun isHclValid(hcl: String?): Boolean {
        //    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        return hcl!!.matches(Regex("^#([0-9]|[a-f]|[A-F]){6}$"))
    }

    private fun isHgtValid(hgt: String?): Boolean {
/*        hgt (Height) - a number followed by either cm or in:
        If cm, the number must be at least 150 and at most 193.
        If in, the number must be at least 59 and at most 76.
 */
        val hgtValue = hgt!!.filter { it.isDigit() }.toInt()
        return when (hgt.filter { it.isLetter() }) {
            "cm" -> hgtValue in 150..193
            "in" -> hgtValue in 59..76
            else -> false
        }
    }

    private fun isEyrValid(eyr: String?): Boolean {
        //eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        return eyr!!.toInt() in 2020..2030
    }

    private fun isIyrValid(iyr: String?): Boolean {
        //    iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        return iyr!!.toInt() in 2010..2020
    }

    private fun isByrValid(byr: String?): Boolean {
        //byr (Birth Year) - four digits; at least 1920 and at most 2002.
        return byr!!.toInt() in 1920..2002
    }
}

