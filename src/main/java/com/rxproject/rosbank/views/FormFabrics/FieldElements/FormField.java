package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FormField {
    private String name;
    private String label;
    private String hint;
    private boolean enabled = true;

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
