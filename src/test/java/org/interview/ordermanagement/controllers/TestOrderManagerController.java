package org.interview.ordermanagement.controllers;

import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TestOrderManagerController {
    //Mock RestAPI Invocations
    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderRepository orderRepository;

    //@Test
    void shouldAddNewOrder() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode" , is(200)));
    }

    //@Test
    void shouldRemoveOrder() throws Exception {
        mockMvc.perform(delete("/orders/1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode" , is(200)));
    }

    //@Test
    void shouldModifyOrder() throws Exception {
        mockMvc.perform(put("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode" , is(200)));
    }

    //@Test
    void shouldCalculatePrice() throws Exception {
        mockMvc.perform(post("/orders/JPM/price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode" , is(200)));
    }

    //@Test
    void shouldPlaceTrade() throws Exception {
        mockMvc.perform(post("/orders/JPM/trade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("statusCode" , is(200)));
    }
}
