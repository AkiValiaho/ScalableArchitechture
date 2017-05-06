package com.akivaliaho.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by akivv on 5.5.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(value = ControllerTest.class)
public class CalculationsControllerTest {
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
        ResultActions perform = mockMvc.perform(post("/api/doSuperHardCalculation").params(numberMap));
    }

    @Test
    public void doSuperHardCalculation() throws Exception {
    }

}