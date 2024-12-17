import fuzzy4j.sets.LinearFunction
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class FuzzyLogic {

    // Функции принадлежности
    // Посещаемость высокая
    val attendanceHigh = LinearFunction(2.0, -1.0)
    // Посещаемость нормальная
    val attendanceLow = LinearFunction(1.0, 0.0)
    // Контрольные работы хорошо
    val worksGood = LinearFunction(0.8, -2.2)
    // Контрольные работы нормально
    val worksNormal = LinearFunction(1.0, -2.0)
    // Работа на парах хорошо
    val pairWorkGood = LinearFunction(0.5, 0.0)

    fun evaluateRules(attendance: Double, works: List<Double>, pairWork: Double): Double {
        val studentAttendanceHigh = attendanceHigh.apply(attendance)
        val studentAttendanceLow = attendanceLow.apply(attendance)
        val studentWorksGood: List<Double> = works.map { worksGood.apply(it) }
        val studentWorksNormal: List<Double> = works.map { worksNormal.apply(it) }
        val studentPairWorkGood = pairWorkGood.apply(pairWork)

        // Первое нечеткое правило
        // Если П = «нормальная» и КР1 ≥ «нормально» и КР2 ≥ «нормально» и КР3 ≥ «нормально» и КР4 ≥ «нормально», то автомат
        val firstRule =
            if (studentAttendanceLow > 0 && studentWorksNormal.all { it > 0 }) {
                min(studentAttendanceLow, studentWorksNormal.minOrNull() ?: 0.0)
            } else {
                0.0
            }

        val worksGoodCondition =
            (studentWorksGood[0] > 0 && studentWorksGood[1] > 0 && studentWorksGood[2] > 0) ||
                (studentWorksGood[0] > 0 && studentWorksGood[1] > 0 && studentWorksGood[3] > 0) ||
                (studentWorksGood[1] > 0 && studentWorksGood[2] > 0 && studentWorksGood[3] > 0) ||
                (studentWorksGood[0] > 0 && studentWorksGood[2] > 0 && studentWorksGood[3] > 0)
        val worksGoodLists =
            listOf(
                studentWorksGood.subList(0, 3),
                studentWorksGood.subList(1, 4),
                listOf(studentWorksGood[0], studentWorksGood[1], studentWorksGood[3]),
                listOf(studentWorksGood[0], studentWorksGood[2], studentWorksGood[3]),
            )

        // Второе нечеткое правило
        // Если П = «высокая» и Р = «хорошо» и ((КР1 ≥ «хорошо» и КР2 ≥ «хорошо» и КР3 ≥ «хорошо»)
        // или (КР1 ≥ «хорошо» и КР2 ≥ «хорошо» и КР4 ≥ «хорошо»)
        // или (КР2 ≥ «хорошо» и КР3 ≥ «хорошо» и КР4 ≥ «хорошо»)), то автомат
        val secondRule =
            if (studentAttendanceHigh > 0 && studentPairWorkGood > 0 && worksGoodCondition) {
                min(min(studentAttendanceHigh, studentPairWorkGood), worksGoodLists.maxOfOrNull { it.min() } ?: 0.0)
            } else {
                0.0
            }

        return max(firstRule, secondRule).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble()
    }
}