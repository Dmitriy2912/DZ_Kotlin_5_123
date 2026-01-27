import org.junit.Assert.*
import org.junit.Test
import ru.netology.Attachment




class NoteServiceTest {
    private val noteService = Attachment.NoteServiceId()

    @Test
    fun add() {
        val note = Attachment.Note(0, "Test note")
        val addedNote = noteService.add(note)
        assertEquals(note.text,
            noteService.getById(addedNote.id).text)
    }

    @Test
    fun createComment1() {
        val note = Attachment.Note(0, "Test")
        noteService.add(note)
        val comment = Attachment.Comment(1,
            2,
            1,
            "AAA",
            0,
            false,
            null,
            null,
            1)
        val addedComent = noteService.getComments(note.id)
        assertEquals(comment.text, comment.text)
    }

    @Test
    fun deleteComment(){
        val note = Attachment.Note(0, "Test")
        noteService.add(note)
        val comment = Attachment.Comment(1,
            2,
            1,
            "AAA",
            0,
            false,
            null,
            null,
            1)
        val addedComment = noteService.createComment1(note.id, comment)
        noteService.delete(note.id)
        assertTrue(noteService.get().isEmpty())
        assertTrue(noteService.getComments(note.id).isEmpty())

        val delteComment = noteService.comments.find { it.id == addedComment.id }
        assertNotNull(delteComment)
        assertTrue(delteComment!!.isDeleted)
//        noteService.getComments(note.id)
//        noteService.delete(note.id)
//        assertTrue(noteService.getComments(note.id).isEmpty())
    }

    @Test
    fun restoreComment(){
        val note = Attachment.Note(0, "Test")
        noteService.add(note)
        val comment = Attachment.Comment(1,
            2,
            1,
            "AAA",
            0,
            false,
            null,
            null,
            1)
        val addedComment = noteService.createComment1(note.id, comment)
        noteService.deleteComment(addedComment.id)
        noteService.restoreComment(addedComment.id)
        assertFalse(noteService.getComments(note.id).isEmpty())
    }

    @Test
    fun updateExisting () {
        val note = Attachment.Note(0, "Test")
        noteService.add(note)
        val comment = Attachment.Comment(
            1,
            2,
            1,
            "AAA",
            0,
            false,
            null,
            null,
            1
        )
        val addedComment = noteService.createComment1(note.id, comment)
        noteService.deleteComment(addedComment.id)


        assertThrows(Exception::class.java) {
            noteService.editComment(addedComment.id, Attachment.Comment
                (1,
                    2,
                    1,
                    "AAA",
                    0,
                    false,
                    null,
                    null,
                    1))

        }

    }
}