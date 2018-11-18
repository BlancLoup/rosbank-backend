package com.rxproject.rosbank.views.ViewFabric.ViewModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentViewModel extends ViewModel {
    final Type type = Type.DOC;
    private Long fileId;
    private String imgUrl;
    private String text;
}
