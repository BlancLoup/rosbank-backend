package com.rxproject.rosbank.views.FormFabrics.FieldElements;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class DatePickerField extends FormField {
    Type type = Type.DATE_PICKER;
    Date value;
}
