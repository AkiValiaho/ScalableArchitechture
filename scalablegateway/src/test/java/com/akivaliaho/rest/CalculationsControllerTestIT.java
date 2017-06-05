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
 * Created by akivv on 5.5.2017.
 */
@RunWith(IntegrationTestRunner.class)
@SpringBootTest
@ServicesToStart(servicesToStart = {"calculatorService"})
public class CalculationsControllerTestIT {
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Before
    public void initBeforeTest() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void doHardCalculation() throws Exception {
        MultiValueMap<String, String> numberMap = new LinkedMultiValueMap<>();
        numberMap.add("number1", "5");
        numberMap.add("number32", "6");
        MvcResult perform = mockMvc.perform(post("/api/doSuperHardCalculation").params(numberMap))
                .andReturn();
        MvcResult mvcResult = mockMvc.perform(asyncDispatch(perform))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertEquals(contentAsString, "11");
    }


    @Test
    public void doSuperHardCalculation() throws Exception {
    }

}