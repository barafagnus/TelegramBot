import java.io.File

data class Statistics(
    val learned: Int,
    val total: Int,
    val percent: Double
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word
)

class LearnWordsTrainer(
    private val learnedAnswerCount: Int = 3,
    private val countOfQuestionWords: Int = 4
) {

    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learned = dictionary.filter { it.correctAnswerCount >= learnedAnswerCount }.size
        val total = dictionary.size
        val percent = (learned.toDouble() / total.toDouble()) * 100
        return Statistics(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < learnedAnswerCount }

        if (notLearnedList.isEmpty()) return null
        val questionWords = if (notLearnedList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswerCount >= learnedAnswerCount }.shuffled()
            notLearnedList.shuffled().take(countOfQuestionWords) +
                    learnedList.take(countOfQuestionWords - notLearnedList.size)
        } else {
            notLearnedList.shuffled().take(countOfQuestionWords)
        }.shuffled()

        val correctAnswer = questionWords.random()
        question = Question(variants = questionWords, correctAnswer = correctAnswer)
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)

            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswerCount++
                saveDictionary(dictionary)
                true
            } else false
        } ?: false
    }

    private fun loadDictionary(): List<Word> {
        try {
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
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }


    private fun saveDictionary(dictionary: List<Word>) {
        val wordsFile = File(FILE_PATHNAME)
        wordsFile.writeText("")
        dictionary.forEach {
            wordsFile.appendText("${it.original}|${it.translate}|${it.correctAnswerCount}\n")
        }
    }

    private fun fillDictionaryFile(wordsFile: File) {
        wordsFile.createNewFile()
        wordsFile.writeText("thank you|спасибо|1\n")
        wordsFile.appendText("dog|собака|2\n")
        wordsFile.appendText("cat|кошка|1\n")
        wordsFile.appendText("hat|шляпа\n")
    }

}


