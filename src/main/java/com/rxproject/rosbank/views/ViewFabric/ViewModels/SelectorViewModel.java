package com.rxproject.rosbank.views.ViewFabric.ViewModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectorViewModel extends ViewModel {
    final Type type = Type.SELECTOR;
    int id;
    String name;
    String imageUrl;
    String description;
}
