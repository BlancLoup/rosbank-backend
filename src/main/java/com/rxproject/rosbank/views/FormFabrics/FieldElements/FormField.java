package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormField {
    public String name;
    public String label;
    public String hint;
    public boolean enabled = true;

    public enum Type {
        DATE_PICKER,
        LABEL,
        MONEY,
        NUMERIC,
        PICKER,
        SLIDER,
        SWITCH,
        TEXT
    }
}
