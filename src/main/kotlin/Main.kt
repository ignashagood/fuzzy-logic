import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFWorkbook


fun main() = application {
    Window(state = rememberWindowState(width = 1400.dp, height = 800.dp), onCloseRequest = ::exitApplication) {
        App()
    }
}

fun readExcelFile(file: File): List<Student> {
    val data = mutableListOf<Student>()
    val fis = FileInputStream(file)
    val workbook = XSSFWorkbook(fis)
    val sheet = workbook.getSheetAt(0)

    for (row in sheet) {
        row.takeIf { row.rowNum > 2 && row.getCell(0).toString().isNotEmpty() }
            ?.let {
                val id = row.getCell(0).toString().replace(".0", "").toIntOrNull() ?: row.hashCode()
                val surname = row.getCell(1).toString().takeIf { it.isNotEmpty() }
                    ?: throw Exception("Заполните столбец фамилий правильно")
                val name = row.getCell(2).toString().takeIf { it.isNotEmpty() }
                    ?: throw Exception("Заполните столбец имен правильно")
                val firstWork = row.getCell(5).format("В столбце оценок за первую работу укажите число или 'неявка'")
                val secondWork = row.getCell(6).format("В столбце оценок за вторую работу укажите число или 'неявка'")
                val thirdWork = row.getCell(7).format("В столбце оценок за третью работу укажите число или 'неявка'")
                val fourthWork =
                    row.getCell(8).format("В столбце оценок за четвертую работу укажите число или 'неявка'")
                val omissions =
                    ((16f - row.getCell(9).format("В столбце пропусков укажите целое число").toFloat()) / 16 * 100)
                        .toInt()
                val pairWork = row.getCell(10).format("В столбце работы на парах укажите целое число")
                val student =
                    Student(id, surname, name, firstWork, secondWork, thirdWork, fourthWork, omissions, pairWork)
                data.add(student)
            }
    }

    workbook.close()
    fis.close()
    return data
}

fun saveExcelFile(fileName: String, students: List<Student>): Boolean {
    return try {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Example Sheet")
        val headerRow = sheet.createRow(0)

        val headers = listOf("№", "Фамилия", "Имя", "Результат")

        headers.forEachIndexed { index, header ->
            val cell = headerRow.createCell(index)
            cell.setCellValue(header)
        }

        students.forEachIndexed { rowIndex, rowData ->
            val row = sheet.createRow(rowIndex + 1)
            row.createCell(0).apply { setCellValue(rowData.id.toString()) }
            row.createCell(1).apply { setCellValue(rowData.name) }
            row.createCell(2).apply { setCellValue(rowData.surname) }
            row.createCell(3).apply { setCellValue(if (rowData.result > 0.5) "Зачет" else "Не зачет") }
        }

        val file = File(fileName)
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Cell.format(errorMessage: String): Int =
    toString().replace(".0", "").replace("неявка", "0").replace("-", "").toIntOrNull() ?: throw Exception(errorMessage)

fun chooseDirectoryWithAWT(): String {
    val fileDialog = FileDialog(null as Frame?, "Выберите директорию", FileDialog.LOAD).apply {
        isVisible = true
    }
    val selectedFile = fileDialog.file
    return if (selectedFile.endsWith(".xlsx")) {
        "${fileDialog.directory}${selectedFile}"
    } else {
        "${fileDialog.directory}${selectedFile}.xlsx"
    }
}