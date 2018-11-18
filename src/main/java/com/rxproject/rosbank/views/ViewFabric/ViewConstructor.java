package com.rxproject.rosbank.views.ViewFabric;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rxproject.rosbank.views.ViewFabric.ViewModels.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewConstructor {
    private List<ViewModel> fields = new ArrayList<>();
    public void addElement(ViewModel viewModel){
        fields.add(viewModel);
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
