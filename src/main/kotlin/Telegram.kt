fun main(args: Array<String>) {
    var updateId: Int? = 0
    val telegramBotService = TelegramBotService(args[0])

    while (true) {
        Thread.sleep(2000)
        val updatesJson: String = telegramBotService.getUpdates(updateId)
        val updates = telegramBotService.parseUpdates(updatesJson)
        val startUpdateId = updates.updateId
        if (startUpdateId == null) continue

        updates.text?.let { text ->
            val message = when (text.lowercase()) {
                "hello" -> "Hello"
                else -> "I don't undarstand u"
            }
            telegramBotService.sendMessage(updates.chatId, message)
        }

        updateId = startUpdateId.plus(1)
    }
}