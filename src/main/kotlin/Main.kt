import kotlin.math.roundToInt

enum class MenuOption(val value: Int) {
    EXIT(0),
    LEARN_WORDS(1),
    STATISTICS(2)
}

const val FILE_PATHNAME = "words.txt"

data class Word(
    val original: String,
    val translate: String,
    var correctAnswerCount: Int = 0
)

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index: Int, word: Word -> "${index + 1} - ${word.translate}" }
        .joinToString(separator = "\n")
    return this.correctAnswer.original + "\n" + variants + "\n--------" + "\n${MenuOption.EXIT.value} - Меню"
}

fun main() {
    val trainer = try {
        LearnWordsTrainer(3, 4)
    } catch (e: Exception) {
        println("Невозможно загрузить словарь")
        return
    }

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

                while (true) {
                    val question = trainer.getNextQuestion()

                    if (question == null) {
                        println("Все слова в словаре выучены")
                        break
                    } else {
                        println()
                        println(question.asConsoleString())

                        print("Ввод: ")
                        val userAnswerInput = readln().toIntOrNull()
                        if (userAnswerInput == MenuOption.EXIT.value) break

                        if (trainer.checkAnswer(userAnswerInput?.minus(1))) {
                            println("Правильно!")
                        } else
                            println("Неправильно! ${question.correctAnswer.original} - это ${question.correctAnswer.translate}")
                    }
                }

            }

            MenuOption.STATISTICS.value -> {
                println("Статистика")
                val statistics = trainer.getStatistics()
                println("Выучено ${statistics.learned} из ${statistics.total} | ${statistics.percent.roundToInt()}%")
            }

            else -> println("Введите число 1, 2 или 0")
        }
    }

}