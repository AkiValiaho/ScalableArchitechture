package com.akivaliaho.rest;

import com.akivaliaho.IntegrationTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by akivv on 27.5.2017.
 */
@RunWith(IntegrationTestRunner.class)
@SpringBootTest
public class StringControllerTestIT {
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;


    @Before
    public void initBeforeTest() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testStringAppender() throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("one", "asdf");
        multiValueMap.add("two", "fdsa");

        MvcResult mvcResult = mockMvc.perform(post("/api/appendStrings").params(multiValueMap)).andReturn();
        MvcResult mvcResult1 = mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult1.getResponse().getContentAsString();
        assertEquals("asdffdsa", contentAsString);
    }
}
