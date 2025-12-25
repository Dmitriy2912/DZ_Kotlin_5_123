package ru.netology
data class Post(
    val id: Int ,
    val ownerId: Int,
    val fromId: Int,
    val likes: Int,
    val text: String,
    val count: Boolean,
    val userLikes: Boolean,
    val canLike: Boolean,
    val canPublish: Boolean,
    val postType: String
)
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
fun main (){
    val post = Post (1,1,1,1,"a", true,
        true,true,true,"A")
    val liked = post.copy(likes = post.likes + 1)
    val (id, _, text) = post
    println(liked)
    println("$id, $text")



}