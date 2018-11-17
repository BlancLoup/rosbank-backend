package com.rxproject.rosbank.views.FormFabrics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rxproject.rosbank.views.FormFabrics.FieldElements.FormField;

import java.util.ArrayList;
import java.util.List;

public class FormConstructor {
    private List<FormField> fields = new ArrayList<>();

    public void addElement(FormField formField){
        fields.add(formField);
    }

    public String toJSON(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
