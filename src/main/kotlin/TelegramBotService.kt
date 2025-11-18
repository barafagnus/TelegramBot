import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class Update(
    val updateId: Int?,
    val chatId: Int?,
    val text: String?,
)

class TelegramBotService(
    private val botToken: String
) {
    private val url = "https://api.telegram.org/bot$botToken"

    fun getUpdates(updateId: Int?): String {
        val urlGetUpdates = "$url/getUpdates?offset=$updateId"
        val client: HttpClient = HttpClient.newBuilder().build()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun parseUpdates(updatesJson: String) =
        Update(
            updateId = "\"update_id\":(.+?),".toRegex().find(updatesJson)?.groups?.get(1)?.value?.toInt(),
            chatId = "\"chat\":\\{\"id\":(.+?),".toRegex().find(updatesJson)?.groups?.get(1)?.value?.toInt(),
            text = "\"text\":\"(.+?)\"".toRegex().find(updatesJson)?.groups?.get(1)?.value,
        )

    fun sendMessage(chatId: Int?, text: String) {
        val urlSendMessage = "$url/sendMessage?chat_id=$chatId&text=${
            text.replace(" ", "+")
        }"
        val client: HttpClient = HttpClient.newBuilder().build()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(request, HttpResponse.BodyHandlers.discarding())
    }
}