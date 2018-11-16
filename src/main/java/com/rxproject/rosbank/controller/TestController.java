package com.rxproject.rosbank.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Log4j
public class TestController {

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public ResponseEntity testGet(){
        JsonNodeFactory jnf = JsonNodeFactory.instance;
        ObjectNode res = jnf.objectNode();
        res.put("message", "Hello, I am rosbank backend!");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public ResponseEntity testPost(@RequestBody JsonNode json){
        log.info("POST with body " + json.toString());
        return new ResponseEntity(HttpStatus.OK);
    }
}
