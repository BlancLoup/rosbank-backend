package com.rxproject.rosbank.views.FormFabrics.FieldElements;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoneyField extends NumericField {
    Type type = Type.MONEY;
    String currency;
}
