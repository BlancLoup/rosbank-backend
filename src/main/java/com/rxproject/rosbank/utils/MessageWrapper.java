package com.rxproject.rosbank.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapper {

    public enum Type {
        BUTTON,
        FORM,
        TEXT,
        DOC,
        PHOTO
    }

    private Type type;
    private String value;
}
