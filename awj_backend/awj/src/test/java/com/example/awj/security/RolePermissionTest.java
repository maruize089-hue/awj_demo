package com.example.awj.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RolePermissionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;
    private String merchantToken;

    @BeforeEach
    void setUp() throws Exception {
        adminToken = extractToken(login("admin", "123456"));
        userToken = extractToken(login("test", "123456"));
        merchantToken = extractToken(login("merchant1", "123456"));
    }

    private String login(String username, String password) throws Exception {
        return mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String extractToken(String response) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("data").get("token").asText();
    }

    @Test
    void testUserCanAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/cart/list")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/favorite/list")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/message/list")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/wallet/info")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    @Test
    void testUserCannotAccessMerchantEndpoints() throws Exception {
        mockMvc.perform(get("/api/merchant/info")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/product")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/service")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testMerchantCanAccessMerchantEndpoints() throws Exception {
        mockMvc.perform(get("/api/merchant/info")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isOk());
    }

    @Test
    void testMerchantCanAccessProductCRUD() throws Exception {
        mockMvc.perform(get("/api/product/list")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/product/1")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isOk());
    }

    @Test
    void testMerchantCannotAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/cart/list")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/favorite/list")
                        .header("Authorization", "Bearer " + merchantToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnAuthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/user/info"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/merchant/info"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/product/list"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/service/list"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/banner/list"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/category/list"))
                .andExpect(status().isOk());
    }
}