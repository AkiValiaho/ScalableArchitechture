package com.akivaliaho.rest;

import com.akivaliaho.service.StringToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by akivv on 9.5.2017.
 */
@RestController
@RequestMapping("/api")
public class StringsController {

    @Autowired
    StringToolsService stringToolsService;

    @RequestMapping(value = "/appendStrings", method = RequestMethod.POST)
    public DeferredResult<String> appendTwoStrings(String one, String two) {
        return stringToolsService.callServiceMethod("appendStrings", one, two);
    }

}

