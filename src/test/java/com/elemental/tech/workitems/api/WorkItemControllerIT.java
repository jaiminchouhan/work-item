package com.elemental.tech.workitems.api;

// TODO: 2. Left imports as a hint.
import com.elemental.tech.workitems.api.dto.UpdateWorkItemStatusRequest;
import com.elemental.tech.workitems.api.dto.WorkItemResponse;
import com.elemental.tech.workitems.domain.WorkItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.elemental.tech.workitems.api.dto.CreateWorkItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WorkItemControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createNewWorkItem() throws Exception {

        //Given
        var req = new CreateWorkItemRequest(
                "Hello",
                "World",
                CreateWorkItemRequest.Priority.MEDIUM
        );

        //When
        String response = mvc.perform(post("/api/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

//Then
        assert response.contains("Hello");
    }

    @Test
    void shouldListWorkItems() throws Exception {

        CreateWorkItemRequest request =
                new CreateWorkItemRequest("Task A", "Desc",
                        CreateWorkItemRequest.Priority.HIGH);

        mvc.perform(post("/api/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/work-items"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetWorkItemById() throws Exception {

        CreateWorkItemRequest request =
                new CreateWorkItemRequest("Task X", "Desc",
                        CreateWorkItemRequest.Priority.LOW);

        String response = mvc.perform(post("/api/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkItemResponse created =
                objectMapper.readValue(response, WorkItemResponse.class);

        mvc.perform(get("/api/work-items/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task X"));
    }

    @Test
    void shouldRejectInvalidStatusTransition() throws Exception {

        CreateWorkItemRequest request =
                new CreateWorkItemRequest("Task Y", "Desc",
                        CreateWorkItemRequest.Priority.MEDIUM);

        String response = mvc.perform(post("/api/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        WorkItemResponse created =
                objectMapper.readValue(response, WorkItemResponse.class);

        UpdateWorkItemStatusRequest invalid =
                new UpdateWorkItemStatusRequest(
                        WorkItem.Status.DONE, 2L
                );

        mvc.perform(patch("/api/work-items/" + created.id() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
