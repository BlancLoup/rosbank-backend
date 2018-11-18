package com.rxproject.rosbank.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rxproject.rosbank.model.Document;
import com.rxproject.rosbank.service.DocumentService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/test")
@Log4j
public class TestController {

    @Autowired
    private DocumentService documentService;

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

    //получение документа по id
    @RequestMapping(value = "document", method = RequestMethod.GET)
    public ResponseEntity getDocumentById(@RequestParam("id") Long id){
        Document document = documentService.getById(id);
        if (document == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        File file = new File(document.getPath());
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(resource,headers,HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
