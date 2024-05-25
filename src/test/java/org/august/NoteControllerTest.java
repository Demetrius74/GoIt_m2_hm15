package org.august;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    public NoteControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
    }

    @Test
    public void testList() throws Exception {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Title 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Title 2");
        note2.setContent("Content 2");

        when(noteService.listAll()).thenReturn(Arrays.asList(note1, note2));

        mockMvc.perform(get("/note/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notes"))
                .andExpect(view().name("notes"));

        verify(noteService, times(1)).listAll();
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(post("/note/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list"));

        verify(noteService, times(1)).deleteById(1L);
    }

    @Test
    public void testEdit() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Title");
        note.setContent("Content");

        when(noteService.getById(1L)).thenReturn(note);

        mockMvc.perform(get("/note/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("note"))
                .andExpect(view().name("edit"));

        verify(noteService, times(1)).getById(1L);
    }

    @Test
    public void testUpdate() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Updated Title");
        note.setContent("Updated Content");

        mockMvc.perform(post("/note/edit")
                        .flashAttr("note", note))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list"));

        verify(noteService, times(1)).update(note);
    }
}
