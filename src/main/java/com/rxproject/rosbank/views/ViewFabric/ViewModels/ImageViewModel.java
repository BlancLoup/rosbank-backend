package com.rxproject.rosbank.views.ViewFabric.ViewModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageViewModel extends ViewModel {
    final Type type = Type.PHOTO;
    private Long fileId;
    private String text;
}
