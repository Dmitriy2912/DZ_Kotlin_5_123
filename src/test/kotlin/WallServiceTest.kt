import org.junit.Before
import org.junit.Test

import ru.netology.Post
import ru.netology.WallService
import kotlin.test.assertFails
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
            "a", true, true,true,
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
            true,true,true,true,
            "a"
        )
        val savedPost = WallService.add(originalPost)
        val updatedPost = savedPost.copy(
            1,
            1,
            1,
            1,
            "AA",
            true,true,true,true,
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
            true,
            true,
            true,
            true,
            "bbb"
        )
        val result = WallService.update(fakePost)
        assertFalse (result)

        
    }


}


