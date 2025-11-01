import java.io.File

data class Word(
    val original: String,
    val translate: String,
    val correctAnswerCount: String = "0"
)

fun main() {
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

    dictionary.forEach {
        println(it)
    }

}