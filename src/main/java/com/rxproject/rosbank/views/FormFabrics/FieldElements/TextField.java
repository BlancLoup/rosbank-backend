package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TextField extends FormField {
    final Type type = Type.TEXT;
    private String regexp;
    private String placeholder;
    private String value;

}
