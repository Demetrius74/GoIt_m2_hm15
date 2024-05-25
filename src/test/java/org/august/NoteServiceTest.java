package org.august;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    private NoteService noteService;

    @BeforeEach
    public void setUp() {
        noteService = new NoteService();
    }

    @Test
    public void testAddNote() {
        Note note = new Note();
        note.setTitle("Test Title");
        note.setContent("Test Content");

        Note addedNote = noteService.add(note);

        assertNotNull(addedNote.getId());
        assertEquals("Test Title", addedNote.getTitle());
        assertEquals("Test Content", addedNote.getContent());
    }

    @Test
    public void testListAll() {
        Note note1 = new Note();
        note1.setTitle("Title 1");
        note1.setContent("Content 1");
        noteService.add(note1);

        Note note2 = new Note();
        note2.setTitle("Title 2");
        note2.setContent("Content 2");
        noteService.add(note2);

        List<Note> notes = noteService.listAll();
        assertEquals(2, notes.size());
    }

    @Test
    public void testDeleteById() {
        Note note = new Note();
        note.setTitle("Title");
        note.setContent("Content");
        Note addedNote = noteService.add(note);

        noteService.deleteById(addedNote.getId());

        assertThrows(IllegalArgumentException.class, () -> noteService.getById(addedNote.getId()));
    }

    @Test
    public void testUpdate() {
        Note note = new Note();
        note.setTitle("Title");
        note.setContent("Content");
        Note addedNote = noteService.add(note);

        addedNote.setTitle("Updated Title");
        noteService.update(addedNote);

        Note updatedNote = noteService.getById(addedNote.getId());
        assertEquals("Updated Title", updatedNote.getTitle());
    }

    @Test
    public void testGetById() {
        Note note = new Note();
        note.setTitle("Title");
        note.setContent("Content");
        Note addedNote = noteService.add(note);

        Note fetchedNote = noteService.getById(addedNote.getId());

        assertEquals(addedNote.getId(), fetchedNote.getId());
        assertEquals("Title", fetchedNote.getTitle());
    }

    @Test
    public void testGetByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> noteService.getById(999L));
    }
}
