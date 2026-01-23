import org.junit.Before
import org.junit.Test
import ru.netology.Attachment
import ru.netology.Attachment.Post
import ru.netology.Attachment.WallService
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import ru.netology.Attachment.Comment
import ru.netology.Attachment.WallService.createComment
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class WallServiceTest {


    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }
    @Test
    fun add() {val post = Post(
        1, 1, 1, 1,
        "a", 1, true, true,
        true, "A"
    )

        //val savedPost =
        WallService.add(post)
        //assertTrue(savedPost.id > 0)
        val comment = Comment(
            1,
            1,
            1,
            "AAA",
            0,
            false,
            null,
            null,
            1
        )
        val addedComment = createComment(1, comment)

        assertEquals(1, addedComment.id)
        assertEquals(1,addedComment.postId)
        assertEquals(1, addedComment.fromId)
        assertEquals("AAA", addedComment.text)
        assertTrue (addedComment.date > 0)
    }


    @Test
    fun updateExisting() {
        val originalPost = Post(
            1,
            1,
            1,
            1,
            "a",
            1,true,true,true,
            "a"
        )
        val savedPost = WallService.add(originalPost)
        val updatedPost = savedPost.copy(
            1,
            1,
            1,
            1,
            "AA",
            1,true,true,true,
            "aaa"
        )
        val result = WallService.update(updatedPost)
        assertTrue(result)
    }
    @Test
    fun updateNotExisting(){
        val fakePost = Post(
            999,
            300,
            300,
            300,
            "bbb",
            1,
            true,
            true,
            true,
            "bbb"
        )
        val result = WallService.update(fakePost)
        assertFalse (result)
    }

    @Test
    fun  updateNotExisting1(){
        val comment = Comment(
            11,
            11,
            11,
            "ADC",
            1,
            false,
            null,
            null,
            1
        )
        assertFailsWith(Attachment.PostNoFoundException::class) {
            createComment(1, comment)
        }
    }
}












