import java.io.File
import kotlin.math.roundToInt

enum class MenuOption(val value: Int) {
    EXIT(0),
    LEARN_WORDS(1),
    STATISTICS(2)
}

const val REQUIRED_LEARNED_WORDS = 3

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
            MenuOption.STATISTICS.value -> {
                println("Статистика")
                showStatistics(dictionary)
            }

            else -> println("Введите число 1, 2 или 0")
        }
    }

}

fun loadDictionary(): List<Word> {
    val dictionary = mutableListOf<Word>()
    val wordsFile = File("words.txt")

    if (!wordsFile.exists()) {
        fillDictionaryFile(wordsFile)
    }

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

fun showStatistics(dictionary: List<Word>) {
    if (dictionary.isEmpty()) {
        println("Словарь пуст")
    } else {
        val wordsAmount = dictionary.size
        val learnedWordsAmount = dictionary.filter {
            it.correctAnswerCount.toInt() >= REQUIRED_LEARNED_WORDS
        }.size
        val learnedWordsPercentage = (learnedWordsAmount.toDouble() / wordsAmount.toDouble()) * 100

        println("Выучено $learnedWordsAmount из $wordsAmount | ${learnedWordsPercentage.roundToInt()}%")
    }
}

fun fillDictionaryFile(wordsFile: File) {
    wordsFile.createNewFile()
    wordsFile.writeText("hello|привет|3\n")
    wordsFile.appendText("dog|собака|2\n")
    wordsFile.appendText("cat|кошка\n")
}