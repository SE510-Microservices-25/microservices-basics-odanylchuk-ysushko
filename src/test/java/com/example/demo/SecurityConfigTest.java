package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.config.SecurityConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    public void contextLoads() {
        assertNotNull(securityConfig);
    }

    @Test
    public void testKeycloakConfigResolver() {
        assertNotNull(securityConfig.keycloakConfigResolver());
    }

    @Test
    public void testAuthorityMapper() {
        assertNotNull(securityConfig.keycloakAuthenticationProvider());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAuthenticatedUserCanAccessSecureEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnauthenticatedUserCannotAccessSecureEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPublicEndpointsAreAccessible() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andExpect(status().isOk());
        
        mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}