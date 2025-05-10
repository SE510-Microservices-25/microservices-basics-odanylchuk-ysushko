package com.example.demo;

import com.example.demo.event.UserCreated;
import com.example.demo.event.UserDeleted;
import com.example.demo.event.UserUpdated;
import com.example.demo.model.User;
import com.example.demo.service.OutboxPublisherService;
import com.example.demo.service.UserCommandService;
import com.example.demo.service.UserQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
@WithMockUser(username = "testuser", roles = {"USER"})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UserCommandService userCommandService;

    @MockBean
    private UserQueryService userQueryService;

    @MockBean
    private OutboxPublisherService outboxPublisherService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testUser", "testToken");
        testUser = new User("testUser", "testToken");
        // Using reflection to set ID (since it's protected)
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userQueryService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser"))
                .andExpect(jsonPath("$[0].token").value("testToken"));

        verify(userQueryService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        when(userQueryService.getUserByUsername("testUser")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.token").value("testToken"));

        verify(userQueryService, times(1)).getUserByUsername("testUser");
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userCommandService.createUser(anyString(), anyString())).thenReturn(testUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.token").value("testToken"));

        verify(userCommandService, times(1)).createUser(anyString(), anyString());
        verify(outboxPublisherService, times(1)).saveOutboxEvent(any(UserCreated.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User("updatedUser", "updatedToken");
        when(userQueryService.getUserById(1L)).thenReturn(Optional.of(testUser));
        when(userQueryService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk());

        verify(userQueryService, times(1)).getUserById(1L);
        verify(userQueryService, times(1)).saveUser(any(User.class));
        verify(outboxPublisherService, times(1)).saveOutboxEvent(any(UserUpdated.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userQueryService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userQueryService, times(1)).existsById(1L);
        verify(userCommandService, times(1)).deleteUserById(1L);
        verify(outboxPublisherService, times(1)).saveOutboxEvent(any(UserDeleted.class));
    }
}