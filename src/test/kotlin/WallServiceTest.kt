import org.junit.Before
import org.junit.Test
import ru.netology.Attachment.Post
import ru.netology.Attachment.WallService
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }
    @Test
    fun add () {
        val post = Post(1,1,1,1,
            "a", 1, true,true,
            true, "a")

        val savedPost = WallService.add(post)

         assertTrue (savedPost.id > 0)
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


}


