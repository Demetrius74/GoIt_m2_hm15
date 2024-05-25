package org.august;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    public void testList() throws Exception {
        when(noteService.listAll()).thenReturn(Arrays.asList(new Note(), new Note()));

        mockMvc.perform(get("/note/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notes"))
                .andExpect(view().name("notes"));

        verify(noteService, times(1)).listAll();
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(noteService).deleteById(1L);

        mockMvc.perform(post("/note/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list"));

        verify(noteService, times(1)).deleteById(1L);
    }

    @Test
    public void testEdit() throws Exception {
        when(noteService.getById(1L)).thenReturn(new Note());

        mockMvc.perform(get("/note/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("note"))
                .andExpect(view().name("edit"));

        verify(noteService, times(1)).getById(1L);
    }

    @Test
    public void testUpdate() throws Exception {
        doNothing().when(noteService).update(any(Note.class));

        mockMvc.perform(post("/note/edit").flashAttr("note", new Note()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/note/list"));

        verify(noteService, times(1)).update(any(Note.class));
    }
}
