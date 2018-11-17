package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SwitchField extends FormField {
    Type type = Type.SWITCH;
    public Boolean value;
}
