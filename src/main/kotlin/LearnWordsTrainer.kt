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

class LearnWordsTrainer {

    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learned = dictionary.filter { it.correctAnswerCount >= REQUIRED_LEARNED_WORDS }.size
        val total = dictionary.size
        val percent = (learned.toDouble() / total.toDouble()) * 100
        return Statistics(learned, total, percent)
    }

    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < REQUIRED_LEARNED_WORDS }
        if (notLearnedList.isEmpty()) return null
        val questionWords = notLearnedList.shuffled().take(NUMBER_OF_UNLEARNED_WORDS)
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


