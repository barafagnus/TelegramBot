import java.io.File
import kotlin.math.roundToInt

enum class MenuOption(val value: Int) {
    EXIT(0),
    LEARN_WORDS(1),
    STATISTICS(2)
}

const val REQUIRED_LEARNED_WORDS = 3
const val NUMBER_OF_UNLEARNED_WORDS = 4
const val FILE_PATHNAME = "words.txt"

data class Word(
    val original: String,
    val translate: String,
    var correctAnswerCount: Int = 0
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
            MenuOption.LEARN_WORDS.value -> {
                println("Учить слова")
                learnWords(dictionary)
            }

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
    val wordsFile = File(FILE_PATHNAME)

    if (!wordsFile.exists()) {
        fillDictionaryFile(wordsFile)
    }

    wordsFile.readLines().forEach {
        val line = it.split("|")
        dictionary.add(
            Word(
                original = line[0],
                translate = line[1],
                correctAnswerCount = line.getOrNull(2)?.toInt() ?: 0
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
            it.correctAnswerCount >= REQUIRED_LEARNED_WORDS
        }.size
        val learnedWordsPercentage = (learnedWordsAmount.toDouble() / wordsAmount.toDouble()) * 100

        println("Выучено $learnedWordsAmount из $wordsAmount | ${learnedWordsPercentage.roundToInt()}%")
    }
}

fun learnWords(dictionary: List<Word>) {
    while (true) {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < REQUIRED_LEARNED_WORDS }

        if (notLearnedList.isEmpty()) {
            println("Все слова в словаре выучены")
            break
        } else {
            val questionWords = notLearnedList.shuffled().take(NUMBER_OF_UNLEARNED_WORDS)
            val correctAnswer = questionWords.random()

            println()
            println("${correctAnswer.original}:")
            questionWords.forEachIndexed { index, word ->
                println(" ${index + 1} - ${word.translate}")
            }
            println("--------")
            println(" ${MenuOption.EXIT.value} - Меню")


            print("Ввод: ")
            val userAnswerInput = readln().toIntOrNull()
            val correctAnswerId = questionWords.indexOf(correctAnswer) + 1

            when (userAnswerInput) {
                in 1..questionWords.size -> {
                    if (userAnswerInput == correctAnswerId) {
                        println("Правильно!")
                        val answerIndex = dictionary.indexOf(correctAnswer)
                        correctAnswer.correctAnswerCount += 1
                        saveDictionary(dictionary)
                    } else println("Неправильно! ${correctAnswer.original} - это ${correctAnswer.translate}")
                }
                MenuOption.EXIT.value -> break
                else -> println("Некорректный ввод. Введите число от 0 до ${questionWords.size}")
            }
        }
    }
}

fun saveDictionary(dictionary: List<Word>) {
    val wordsFile = File(FILE_PATHNAME)
    wordsFile.writeText("")
    dictionary.forEach {
        wordsFile.appendText("${it.original}|${it.translate}|${it.correctAnswerCount}\n")
    }
}

fun fillDictionaryFile(wordsFile: File) {
    wordsFile.createNewFile()
    wordsFile.writeText("thank you|спасибо|1\n")
    wordsFile.appendText("dog|собака|2\n")
    wordsFile.appendText("cat|кошка|1\n")
    wordsFile.appendText("hat|шляпа\n")
}
