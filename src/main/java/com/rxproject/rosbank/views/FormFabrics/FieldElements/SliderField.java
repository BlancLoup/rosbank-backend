package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SliderField extends FormField {
    Type type = Type.SLIDER;
    Float min;
    Float max;
    Float value;
}
