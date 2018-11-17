package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PickerField extends FormField{
    final Type type = Type.PICKER;
    String value;
    List<String> variants;
}
