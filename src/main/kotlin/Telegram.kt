fun main(args: Array<String>) {
    val botToken = args[0]
    var updateId: Int? = 0
    val telegramBotService = TelegramBotService()

    while (true) {
        Thread.sleep(2000)
        val updatesJson: String = telegramBotService.getUpdates(botToken, updateId)
        val updates = telegramBotService.parseUpdates(updatesJson)
        val startUpdateId = updates.updateId
        if (startUpdateId == null) continue

        updates.text?.let { text ->
            val message = when (text.lowercase()) {
                "hello" -> "Hello"
                else -> "I don't undarstand u"
            }
            telegramBotService.sendMessage(botToken, updates.chatId, message)
        }

        updateId = startUpdateId.plus(1)
    }
}

