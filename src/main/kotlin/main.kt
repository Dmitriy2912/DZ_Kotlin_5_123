package ru.netology

import ru.netology.Attachment.WallService.createComment
import kotlin.collections.mutableListOf




interface Attachment {

    val type: String

    data class Post(
        val id: Int,
        val ownerId: Int,
        val fromId: Int,
        val likes: Int,
        val text: String,
        val count: Int,
        val userLikes: Boolean,
        val canLike: Boolean,
        val canPublish: Boolean,
        val postType: String,

        val attachments: List<Attachment>? = null
    )

    data class Photo(
        val id: Int,
        val ownerId: Int,
        val photo130: String,
        val photo604: String,
        val width: Int? = null,
        val height: Int? = null,
        val text: String? = null
    )

    data class Video(
        val id: Int,
        val ownerId: Int,
        val title: String,
        val duration: Int,
        val description: String? = null,
        val photo130: String? = null,
        val photo320: String? = null,
        val photo640: String? = null
    )

    data class Document(
        val id: Int,
        val title: String,
        val size: Long,
        val ext: String,
        val url: String,
        val date: Int? = null,
        val type: Int? = null
    )

    data class Link(
        val url: String,
        val title: String? = null,
        val description: String? = null,
        val photo100: String? = null,
        val photo130: String? = null
    )

    data class Note(
        val id: Int,
        var text: String,
        var isDeleted: Boolean = false
    )

    data class Comment(
        val id: Int,
        val postId: Int,
        val fromId: Int,
        var text: String,
        val date: Int,
        var isDeleted: Boolean = false,
        val replyToComment: Int? = null,
        val attachment: List<Attachment>? = null,
        val noteId: Int
    )
    class PostNoFoundException(postId: Int):
        RuntimeException("Post s ID=$postId ne nauden")



//    class WallServiceTo{
//        private var post = emptyArray<Post>()
//        private var comment = emptyArray<Comment>()
//        private var nextCommentId = 1
//
////            fun createComment(postId: Int, comment: Comment): Comment {
////            val post = post.find{ it.id == postId} ?:
////            throw PostNoFoundException(postId)
////
////            val newComment = comment.copy(id = nextCommentId++,
////                postId = postId,
////                date = (System.currentTimeMillis() / 1000).toInt()
////            )
//
//
//
//            return newComment
//        }
//    }




    data class Audio(
        val id: Int,
        val ownerId: Int,
        val artist: String,
        val title: String,
        val duration: Int,
        val url: String? = null,
        val lyricsId: Int? = null
    )


    data class PhotoAttachment(
        override val type: String = "photo",
        val photo: Photo
    ) : Attachment

    data class VideoAttachment(
        override val type: String = "video",
        val video: Video
    ) : Attachment

    data class DocumentAttachment(
        override val type: String = "doc",
        val document: Document
    ) : Attachment

    data class LinkAttachment(
        override val type: String = "link",
        val link: Link
    ) : Attachment

    data class AudioAttachment(
        override val type: String = "audio",
        val audio: Audio
    ) : Attachment

    data class Message (
        val id: Int,
        val chatId: Int,
        val senderId: Int,
        var text: String,
        var chetator: Boolean = false,
        val timestamp: Long = System.currentTimeMillis()
    )



    data class Chat(
        val id: Int,
        val senderUserId: Int,
        val recipientUserId: Int,
        var lastMessage: String = "no messages",
        var unreadCount: Int = 0,
        var lastActivity: Long = System.currentTimeMillis()
    )

    class ChatService{
        val chats = mutableMapOf<Int, Chat>()
        val messages = mutableMapOf<Int, MutableList<Message>>()


        fun createChat(senderUserId: Int, recipientUserId: Int): Chat{
            val id = generateId()
            val chat = Chat(id, senderUserId, recipientUserId)
            chats[id] = chat
            messages[id] = mutableListOf()
            return chat
        }
        fun getChat(userId: Int): List<Chat> = chats.values.filter()
        { it.senderUserId == userId || it.recipientUserId == userId }
            .sortedByDescending { it.lastActivity }

        fun getUnreadChatsCount(userId: Int): Int = getChat(userId).count{ it.unreadCount > 0}

        fun deleteChat(chatId: Int): Boolean{
            if (chats.remove(chatId) == null) return false
            messages.remove(chatId)
            return true
        }

        private fun generateId() =
            (chats.keys.maxOrNull() ?: 0) + 1
    }

    class MessageService(private val chatService: ChatService){

        fun sendMessage(senderId: Int, receiverId: Int, text: String): Message? {
            val chat = findOrCreateChat(senderId, receiverId)
            val message = Message(
                generateMessageId(),
                chat.id,
                senderId,
                text
            )
            chatService.messages[chat.id]?.add(message)

            chat.lastMessage = text
            chat.unreadCount++
            chat.lastActivity = message.timestamp

            return message
        }

        fun getLastMessages(userId: Int): Map<Int, String> =chatService.getChat(userId).associate { it.id to it.lastMessage }

        fun getMessages(chatId: Int, limit: Int): List<Message>{
            val mList = chatService.messages[chatId] ?: return emptyList()

            val result = mList.takeLast(limit).toList()

            result.forEach  { it.chetator = true }

            val chat = chatService.chats[chatId]
            chat?.unreadCount = mList.count{ it.chetator }

            return result
        }

        fun editMessage(messegeId: Int, newText: String): Boolean{
            val message = findMessageById(messegeId) ?: return false
            message.text = newText
            val chat = chatService.chats[message.chatId]
            chat?.lastMessage = newText
            return true
        }

        fun deleteMessage(messegeId: Int): Boolean {
            val message = findMessageById(messegeId) ?: return false
            val mList = chatService.messages[message.chatId] ?: return false
            mList.remove(message)
            return true
        }

        private fun findOrCreateChat (senderId: Int, receiverId: Int): Chat{
            val chat = chatService.chats.values.find {
                (it.senderUserId == senderId && it.recipientUserId == receiverId)  ||
                (it.senderUserId == receiverId && it.recipientUserId == senderId)
            }
            return  chat ?: chatService.createChat(senderId, receiverId)
        }

        private fun findMessageById(messegeId: Int): Message? = chatService.messages.values.flatMap { it }
            .find { it.id == messegeId }

        private fun generateMessageId() = (chatService.messages.values.flatMap {  it.map { it.id }}.maxOrNull() ?: 0) + 1

    }


    interface NoteService<T> {
        fun add(note: T): T
        fun createComment1(noteId: Int, comment: Comment): Comment
        fun delete(noteId: Int)
        fun deleteComment(commentId: Int)
        fun edit(noteId: Int, note: T)
        fun editComment(commentId: Int, comment: Comment)
        fun get(): List<T>
        fun getById(id: Int): T
        fun getComments(noteId: Int): List<Comment>
        fun restoreComment(commentId: Int)

    }
class NoteServiceId : NoteService<Note> {
    private val notes : MutableList<Note> = mutableListOf()
    val comments : MutableList<Comment> = mutableListOf()
    private var nextCommentId = 1
    private var nextNoteId = 1



    override fun add(note: Note): Note {
        val newNote = note.copy(id = nextNoteId++)
            notes.add(newNote)
            return newNote
    }

    override fun createComment1(noteId: Int, comment: Comment): Comment {
        val note = getById(noteId)
        if (note.isDeleted) throw Exception("Cannot comment on deleted note")

        val newComment = comment.copy(id = nextCommentId++, noteId = noteId, isDeleted = false)
        comments.add(newComment)
        return newComment
    }

    override fun delete(noteId: Int) {
        val note = getById(noteId)
        note.isDeleted = true
        comments.filter { it.noteId == noteId }.forEach { it.isDeleted = true }
    }

    override fun deleteComment(commentId: Int) {
        val comment = getCommentById(commentId)
        if (comment.isDeleted) throw Exception("Comment already deleted")
        comment.isDeleted = true
        }

    override fun edit(noteId: Int, note: Note) {
        val currentNote = getById(noteId)
        if (currentNote.isDeleted) throw Exception("Cannot edit deleted note")
        currentNote.text = note.text
    }


    override fun editComment(commentId: Int, comment: Comment) {
        val currentComment = getCommentById(commentId)
        if (currentComment.isDeleted) throw Exception("Cannot edit deleted comment")
        currentComment.text = comment.text
    }


    override fun get(): List<Note> = notes.filter {!it.isDeleted}

    override fun getById(id: Int): Note {
        return notes.find { it.id == id && !it.isDeleted } ?:
        throw Exception("Note not found or already deleted")
    }

    override fun getComments(noteId: Int): List<Comment> =
        comments.filter { it.noteId == noteId && !it.isDeleted }


    override fun restoreComment(commentId: Int) {
        val comment = getCommentById(commentId)
        if (!comment.isDeleted) throw Exception("Comment is not deleted")
        comment.isDeleted = false
    }

    private fun getCommentById(commentId: Int): Comment {
            return comments.find { it.id == commentId } ?:
            throw Exception("Comment not found")
    }


}


    object WallService {
        //: NoteService<Note>{
        private val posts = mutableListOf<Post>()
        private var nextId = 1
        private var post = emptyArray<Post>()
        private var comment = emptyArray<Comment>()
        private var nextCommentId = 1
//        private val notes: MutableList<Note> = mutableListOf()
//        private val comments: MutableList<Comment> = mutableListOf()
//        private var nextNoteId = 1

        fun add(post: Post): Post {
            val newPost = post.copy(id = nextId)
            posts.add(newPost)
            nextId++
            return newPost
        }

        fun update(post: Post): Boolean {
            if (post.id <= 0) return false
            val index = posts.indexOfFirst { it.id == post.id }
            if (index == -1) return false
            posts[index] = post
            return true
        }

//        override fun add(note: Note): Note {
//            val newNote = note.copy(id = nextNoteId++)
//            notes.add(newNote)
//            return newNote
//        }
        fun createComment(postId: Int, comment: Comment): Comment {
            val post = posts.find{ it.id == postId} ?:
            throw PostNoFoundException(postId)

            val newComment = comment.copy(id = nextCommentId++,
                postId = postId,
                date = (System.currentTimeMillis() / 1000).toInt()
            )

            return newComment
        }

//        override fun createComment1(noteId: Int, comment: Comment): Comment {
//            val note = getById(noteId)
//            if (note.isDeleted) throw Exception("Cannot comment on deleted note")
//
//            val newComment = comment.copy(id = nextCommentId++, noteId = noteId)
//            comments.add(newComment)
//            return newComment
//        }
//
//        override fun delete(noteId: Int) {
//            val note = getById(noteId)
//            note.isDeleted = true
//            comments.filter { it.noteId == noteId }.forEach { it.isDeleted = true }
//        }
//
//        override fun deleteComment(commentId: Int) {
//            val comment = getCommentById(commentId)
//            if (comment.isDeleted) throw Exception("Comment already deleted")
//            comment.isDeleted = true
//        }
//
//        override fun edit(noteId: Int, note: Note) {
//            val currentNote = getById(noteId)
//            if (currentNote.isDeleted) throw Exception("Cannot edit deleted note")
//            currentNote.text = note.text
//        }
//
//        override fun editComment(commentId: Int, comment: Comment) {
//            val currentComment = getCommentById(commentId)
//            if (currentComment.isDeleted) throw Exception("Cannot edit deleted comment")
//            currentComment.text = comment.text
//        }
//
//        override fun get(): List<Note> = notes.filter {!it.isDeleted}
//
//        override fun getById(id: Int): Note {
//            return notes.find { it.id == id && !it.isDeleted }
//                ?: throw Exception("Note not found or already deleted")
//        }
//
//        override fun getComments(noteId: Int): List<Comment> =
//            comments.filter { it.noteId == noteId && !it.isDeleted }
//
//
//        override fun restoreComment(commentId: Int) {
//            val comment = getCommentById(commentId)
//            if (!comment.isDeleted) throw Exception("Comment is not deleted")
//            comment.isDeleted = false
//        }
//        private fun getCommentById(commentId: Int): Comment {
//            return comments.find { it.id == commentId }
//                ?: throw Exception("Comment not found")
//        }


        fun clear() {
            posts.clear()
            nextId = 1
        }

    }

    fun main() {
        //val wallService = WallServiceTo()

        val post = Post(
            1, 1, 1, 1, "a", 1,
            true, true, true, "A",
        )
        //урок
        val short = ::liked
        val full: (Post) -> Boolean = ::liked
        println(short == full)
        println(short === full)




        val comment = Comment(
            1, 2, 1, "AAA", 0,
            false, null, null,1

        )

        try { val addedComment = createComment(1, comment)
            println("addedComment: $addedComment")
        }catch (e: PostNoFoundException){
            println(e.message)
        }







        val photo = Photo(1, 100, "https://vk.com/photo1_130.jpg", "https://vk.com/photo1_604.jpg")
        val video = Video(2, 100, "Моё видео", 180)
        val document = Document(3, "Документ.pdf", 1024000, "pdf", "https://vk.com/doc3.pdf")
        val link = Link("https://example.com", "Пример сайта")
        val audio = Audio(4, 100, "Исполнитель", "Трек", 240)



        val attachments = listOf(
            PhotoAttachment(photo = photo),
            VideoAttachment(video = video),
            DocumentAttachment(document = document),
            LinkAttachment(link = link),
            AudioAttachment(audio = audio)
        )
        println("Пост ID: ${post.id}")
        println("Текст: ${post.text}")
        println("Вложений: ${post.attachments?.size ?: 0}")

        post.attachments?.forEach { attachment ->
            when (attachment) {
                is PhotoAttachment -> println("Фото: ${attachment.photo.photo130}")
                is VideoAttachment -> println("Видео: ${attachment.video.title}")
                is DocumentAttachment -> println("Документ: ${attachment.document.title}")
                is LinkAttachment -> println("Ссылка: ${attachment.link.url}")
                is AudioAttachment -> println("Аудио: ${attachment.audio.artist} — ${attachment.audio.title}")
            }
        }

        // Урок
        val list = mutableListOf(1, 2, 3)
        println(list)
        list.swap(0,2)
        println(list)


        val strList = mutableListOf("One", "Two", "Three")
        println(strList)
        strList.swap(0,2)
        println(strList)
    }



    fun <E> MutableList<E>.swap(index1: Int, index2: Int){
        val e1 = this[index1]
        val e2 = this[index2]
        this[index1] = e2
        this[index2] = e1
    }
    fun liked(post: Post) = post.likes > 0


}