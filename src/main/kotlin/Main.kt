import java.io.File

enum class MenuOption(val value: Int) {
    EXIT(0),
    LEARN_WORDS(1),
    STATISTICS(2)
}

data class Word(
    val original: String,
    val translate: String,
    val correctAnswerCount: String = "0"
)

fun main() {
    val dictionary = loadDictionary()

    while (true) {
        println(
            """
            Меню: 
            ${MenuOption.LEARN_WORDS.value} – Учить слова
            ${MenuOption.STATISTICS.value} – Статистика
            ${MenuOption.EXIT.value} – Выход
        """.trimIndent()
        )

        print("Ввод: ")
        val input: Int? = readln().toIntOrNull()
        println()
        when (input) {
            MenuOption.EXIT.value -> break
            MenuOption.LEARN_WORDS.value -> println("Учить слова")
            MenuOption.STATISTICS.value -> println("Статистика")
            else -> println("Введите число 1, 2 или 0")
        }
    }

}

fun loadDictionary(): List<Word> {
    val dictionary = mutableListOf<Word>()

    val wordsFile = File("words.txt")
    wordsFile.createNewFile()
    wordsFile.writeText("hello|привет|0\n")
    wordsFile.appendText("dog|собака|0\n")
    wordsFile.appendText("cat|кошка\n")

    wordsFile.readLines().forEach {
        val line = it.split("|")
        dictionary.add(
            Word(
                original = line[0],
                translate = line[1],
                correctAnswerCount = line.getOrNull(2) ?: "0"
            )
        )
    }

    return dictionary
}