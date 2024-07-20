package com.donatasd.wixhomework.store;

import com.donatasd.wixhomework.WixHomeworkApplication;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = WixHomeworkApplication.class)
@AutoConfigureMockMvc
public class StoreControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void shouldCreateNewStore() throws Exception {
        var store = generateTestStore();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        var serializedStore =  mapper.writeValueAsBytes(store);

        mvc.perform(post("/store").contentType(MediaType.APPLICATION_JSON).content(serializedStore))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindStoresWithoutQuery() throws Exception {
        mvc.perform(get("/store").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/store?query=EQUAL(id,\"first-post\")",
            "/store?query=EQUAL(views,100)",
            "/store?query=AND(EQUAL(id,\"first-post\"),EQUAL(views,100))",
            "/store?query=OR(EQUAL(id,\"first-post\"),EQUAL(id,\"second-post\"))",
            "/store?query=NOT(EQUAL(id,\"first-post\"))",
            "/store?query=GREATER_THAN(views,100)",
            "/store?query=LESS_THAN(views,100)"
    })
    void shouldFindStoresWithQuery(String url) throws Exception {
        mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFindSpecificStore() throws Exception {
        var store = generateTestStore();
        storeRepository.save(store);

        MvcResult result = mvc.perform(get("/store?query=EQUAL(id,\""+ store.getId()+ "\")").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        var stores =  mapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Store>>(){});

        assertEquals(1, stores.size());
        assertEquals(store.getId(), stores.get(0).getId());
    }

    private static Store generateTestStore() {
        var store = new Store();
        store.setId("my-store");
        store.setContent("Hello World!");
        store.setTitle("My First Post");
        store.setViews(1);
        store.setTimestamp(1555832341);
        return store;
    }
}
