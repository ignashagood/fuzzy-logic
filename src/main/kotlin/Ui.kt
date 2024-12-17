import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.awt.Frame

@Composable
@Preview
fun App() {
    var inputFilePath by remember { mutableStateOf<String?>(null) }
    var fileContent by remember { mutableStateOf<List<Student>?>(null) }
    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Загрузка данных из Excel", modifier = Modifier.align(Alignment.CenterHorizontally))
        Button(onClick = {
            val frame = Frame()
            val file = FileDialog(frame, "Выберите файл").apply {
                isVisible = true
                isMultipleMode = false
            }.files.firstOrNull()

            inputFilePath = file?.absolutePath
            file?.let {
                fileContent = readExcelFile(it)
            }
        }) {
            Text("Загрузить файл")
        }

        inputFilePath?.let {
            Text("Выбран файл: $it", modifier = Modifier.padding(top = 8.dp))
        }

        fileContent?.let { content ->
            Button(onClick = {
                val filePath = chooseDirectoryWithAWT()
                val success = saveExcelFile(filePath, content)
                statusMessage =
                    if (success) {
                        "Файл успешно сохранён в: $filePath"
                    } else {
                        "Ошибка при сохранении файла"
                    }
            }) {
                Text("Сохранить файл")
            }

            if (statusMessage.isNotEmpty()) {
                Text(statusMessage)
            }
            PrettyTable(content)
        }
    }
}

@Composable
fun PrettyTable(students: List<Student>) {

    LazyColumn {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF)) // Чередование цвета строк
                    .padding(8.dp)
            ) {
                Text(
                    text = "Фамилия",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Имя",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "КР 1",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "КР 2",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "КР 3",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "КР 4",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Посещаемость в %",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Работа на парах",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Автомат",
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        items(students.size) { rowIndex ->
            val student = students[rowIndex]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (rowIndex % 2 == 0) Color.White else Color(0xFFEFEFEF)) // Чередование цвета строк
                    .padding(8.dp)
            ) {
                Text(
                    text = student.surname,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.firstWork.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.secondWork.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.thirdWork.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.fourthWork.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.omissionsNumber.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.pairWork.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = student.result.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}