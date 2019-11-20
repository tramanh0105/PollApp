package com.pollapp.pollapp.payload.request;

import javax.validation.constraints.NotBlank;

public class ChoiceRequest {
    @NotBlank
    private String text;

    public ChoiceRequest(@NotBlank String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
