package com.rxproject.rosbank.views.ViewFabric.ViewModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlViewModel extends ViewModel {
    final ViewModel.Type type = ViewModel.Type.URL;
    private String image;
    private String url;
    private String text;
}
