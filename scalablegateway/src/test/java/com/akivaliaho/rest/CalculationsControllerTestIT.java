package com.akivaliaho.rest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by akivv on 5.5.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculationsControllerTestIT {
    private static IntegrationTestHarness testHarness;
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @BeforeClass
    public static void init() {
        testHarness = new IntegrationTestHarness();
        testHarness.startServices();
    }

    @AfterClass
    public static void removeAllProcesses() {
        testHarness.stopServices();
    }

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