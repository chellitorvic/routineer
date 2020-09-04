package com.github.routineer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class RoutineerApiTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getRoutines() throws Exception {
        mvc.perform(get("/routines"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getNonExistentRoutine() throws Exception {
        mvc.perform(get("/routines/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistentRoutine() throws Exception {
        mvc.perform(delete("/routines/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNonExistentRoutine() throws Exception {
        mvc.perform(put("/routines/1")
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRoutine() throws Exception {
        create()
                .andExpect(jsonPath("id", is(not(nullValue()))))
                .andExpect(jsonPath("schedule.interval", is("1m")))
                .andExpect(jsonPath("action.webhook.url", is("http://google.com")));
    }

    @Test
    void getCreatedRoutine() throws Exception {
        var content = create().andReturn().getResponse().getContentAsString();

        var routine = objectMapper.readValue(content, RoutinesController.Routine.class);
        mvc.perform(get("/routines/{id}", routine.getId()))
                .andExpect(status().isOk());
    }


    private ResultActions create() throws Exception {
        return mvc.perform(post("/routines")
                .contentType(APPLICATION_JSON)
                .content("{\n" +
                        "  \"schedule\": {\n" +
                        "    \"interval\": \"1m\"\n" +
                        "  },\n" +
                        "  \"action\": {\n" +
                        "    \"webhook\": {\n" +
                        "      \"url\": \"http://google.com\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"))
                .andExpect(status().isCreated());
    }

}
