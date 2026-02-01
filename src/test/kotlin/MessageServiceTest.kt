import org.junit.Assert.*
import org.junit.Test
import ru.netology.Attachment

class MessageServiceTest {
    private val chatService = Attachment.ChatService()
    private val messageService = Attachment.MessageService(chatService)

    @Test
    fun sendMessage(){
        val messege = messageService.sendMessage(1,2,"Hello")

        assertNotNull(messege)
        assertEquals(1, messege?.senderId)
        assertEquals(1,messege?.chatId)

        val chats = chatService.getChat(1)
        assertEquals(1, chats.size)
        assertTrue(chats.any { it.senderUserId == 1 && it.recipientUserId == 2 })
    }

    @Test
    fun getLastMessages(){
        messageService.sendMessage(1,2,"SMS 1")
        messageService.sendMessage(1,3,"SMS 2")

        val lastMessage = messageService.getLastMessages(1)

        assertEquals(2, lastMessage.size)
        assertEquals("SMS 1", lastMessage.values.find { it == "SMS 1" })
        assertEquals("SMS 2", lastMessage.values.find { it == "SMS 2" })
    }

    @Test
    fun getMessages(){
        val message1 = messageService.sendMessage(1,2,"first")
        val message2 = messageService.sendMessage(1,1,"second")

        val chat = chatService.getChat(1).first { it.recipientUserId == 2 }
        assertEquals(1, chat.unreadCount)

        val message = messageService.getMessages(chat.id, 2)
        assertEquals(1, message.size)
        assertTrue(message.all { it.chetator })
        assertEquals(1, chat.unreadCount)
    }

    @Test
    fun editMessage(){
        val msg = messageService.sendMessage(1,2, "Text 1")
        assertTrue(messageService.editMessage(msg!!.id, "Text 2"))

        val updatedMsg = findMessageById(msg.id)
        assertEquals("Text 2", updatedMsg?.text)

        val chat = chatService.getChat(1).first { it.recipientUserId == 2 }
        assertEquals("Text 2", chat.lastMessage)
    }

    @Test
    fun deleteMessage(){
        val message = messageService.sendMessage(1,2,"Delete")
        assertTrue(messageService.deleteMessage(message!!.id))
        assertNull(findMessageById(message.id))
    }
//    @Test
//    fun editMessageException(){
//        val message = messageService.sendMessage(1,2,"Text")
//        messageService.deleteMessage(message!!.id)
//
//        assertThrows<Exception>(Exception::class.java){
//            messageService.editMessage(message.id, "Text")
//        }
//    }



    private fun findMessageById(messageId: Int): Attachment.Message? =
        chatService.messages.values
            .flatMap { it }
            .find { it.id == messageId }




}