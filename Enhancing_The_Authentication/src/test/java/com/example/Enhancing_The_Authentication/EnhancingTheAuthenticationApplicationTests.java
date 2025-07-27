package com.example.Enhancing_The_Authentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class EnhancingTheAuthenticationApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testInvalidLogin() throws Exception {
        String loginJson = objectMapper.writeValueAsString(new TestUser("nouser", "wrongpass", null));
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegistrationWithMissingFields() throws Exception {
        // Missing password
        String userJson = objectMapper.writeValueAsString(new TestUser("user2", null, new String[]{"USER"}));
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAdminRoleAccess() throws Exception {
        // Register admin
        String userJson = objectMapper.writeValueAsString(new TestUser("adminuser", "adminpass", new String[]{"ADMIN"}));
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());
        // Login as admin
        String loginJson = objectMapper.writeValueAsString(new TestUser("adminuser", "adminpass", null));
        String token = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readTree(token).get("token").asText();
        // Access protected endpoint as admin
        mockMvc.perform(get("/api/protected")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("You have access to a protected endpoint!"));
    }

    static class TestUser {
        public String username;
        public String password;
        public String[] roles;
        public TestUser(String username, String password, String[] roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }
    }
}
