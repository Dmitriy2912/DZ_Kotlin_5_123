import org.junit.Assert.*
import org.junit.Test
import ru.netology.Attachment
import ru.netology.Attachment.ChatService

class ChatServiceTest {
    private val service = ChatService()

    @Test
    fun createChat(){
        val chat = service.createChat(1,2)
        assertNotNull(chat)
        assertEquals(1, chat.senderUserId)
        assertEquals(2,chat.recipientUserId)
    }

    @Test
    fun getChat(){
        service.createChat(1,2)
        service.createChat(3,1)

        val chats = service.getChat(1)
        assertEquals(2,chats.size)
    }

    @Test
    fun etUnreadChatsCount(){
        val chat1 = service.createChat(1,2)
        chat1.unreadCount = 2

        val chat2 = service.createChat(1,3)
        chat2.unreadCount = 0

        assertEquals(1, service.getUnreadChatsCount(1))
    }

    @Test
    fun deleteChat(){
        val chat = service.createChat(1,2)
        assertTrue(service.deleteChat(chat.id))
        assertFalse(service.getChat(1).contains(chat))
    }


}