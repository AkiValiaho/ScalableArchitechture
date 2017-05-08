package com.akivaliaho.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by akivv on 5.5.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculationsControllerTestIT {
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;
    private IntegrationTestHarness testHarness;


    @Before
    public void init() {
        this.testHarness = new IntegrationTestHarness();
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext).build();
        testHarness.startServices();
    }

    @After
    public void stopServices() {
        this.testHarness.stopServices();
    }

    @Test
    public void doHardCalculation() throws Exception {
        MultiValueMap<String, String> numberMap = new LinkedMultiValueMap<>();
        numberMap.add("number1", "1");
        numberMap.add("number32", "2");
        MvcResult perform = mockMvc.perform(post("/api/doSuperHardCalculation").params(numberMap))
                .andReturn();
        mockMvc.perform(asyncDispatch(perform))
                .andExpect(status().isOk());
    }

    @Test
    public void doSuperHardCalculation() throws Exception {
    }

}