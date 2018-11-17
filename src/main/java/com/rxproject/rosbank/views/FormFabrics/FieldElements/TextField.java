package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TextField extends FormField {
    Type type = Type.TEXT;
    public String regexp;
    public String placeholder;
    public String value;
}
