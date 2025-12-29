package ru.netology

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


    object WallService {
        private val posts = mutableListOf<Post>()
        private var nextId = 1

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

        fun clear() {
            posts.clear()
            nextId = 1
        }

    }

    fun main() {
        val post = Post(
            1, 1, 1, 1, "a", 1,
            true, true, true, "A",
        )
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




    }
}