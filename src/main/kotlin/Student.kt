data class Student(
    val id: Int,
    val surname: String,
    val name: String,
    val firstWork: Int,
    val secondWork: Int,
    val thirdWork: Int,
    val fourthWork: Int,
    val omissionsNumber: Int,
    val pairWork: Int,
    val result: Double =
        FuzzyLogic().evaluateRules(
            omissionsNumber.toDouble() / 100,
            listOf(firstWork.toDouble(), secondWork.toDouble(), thirdWork.toDouble(), fourthWork.toDouble()),
            pairWork.toDouble()
        )
)