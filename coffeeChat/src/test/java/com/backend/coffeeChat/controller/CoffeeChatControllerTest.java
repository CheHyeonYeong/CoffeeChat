package com.backend.coffeeChat.controller;

import com.backend.coffeeChat.dto.ApplyRequest;
import com.backend.coffeeChat.repository.ApplicationRepository;
import com.backend.coffeeChat.service.AlimtalkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CoffeeChatController.class)
class CoffeeChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationRepository repository;

    @MockBean
    private AlimtalkService alimtalkService;

    @Test
    void applyPostSavesApplication() throws Exception {
        ApplyRequest saved = new ApplyRequest();
        saved.setId(1L);
        when(repository.save(any(ApplyRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/apply")
                .param("name", "John")
                .param("contact", "010-0000-0000")
                .param("preferredTime", "10AM")
                .param("topic", "Spring"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apply"));

        verify(repository).save(any(ApplyRequest.class));
    }

    @Test
    void acceptGetSendsNotification() throws Exception {
        ApplyRequest request = new ApplyRequest();
        request.setId(1L);
        request.setName("John");
        request.setContact("010-0000-0000");
        request.setPreferredTime("10AM");
        request.setTopic("Spring");
        when(repository.findById(1L)).thenReturn(request);

        mockMvc.perform(get("/accept").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("accept"));

        verify(repository).findById(1L);
        verify(alimtalkService).sendAlimtalk(eq("010-0000-0000"), eq("TEMPLATE_USER_ACCEPT"), anyMap());
    }
}
