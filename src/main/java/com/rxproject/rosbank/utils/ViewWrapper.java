package com.rxproject.rosbank.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewWrapper {

    public enum Type {
        PHOTO,
        DOC,
        CARDS
    }

    private Type type;
    private JsonNode content;

}
