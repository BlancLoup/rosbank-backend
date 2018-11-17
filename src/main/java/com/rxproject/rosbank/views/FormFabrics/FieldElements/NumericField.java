package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NumericField extends FormField {
    Type type = Type.NUMERIC;
    String regexp;
    String placeholder;
    Float min;
    Float max;
    Float value;
}
