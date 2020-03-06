package com.example.demo.domain;

public class Data {

    private String creatorId;
    private String creatorPlatform;
    private String creatoredSource;
    private String creatoredType;
    private String deckId;
    private boolean isExpert;
    private Question question;
    private String text;
    private String title;
    private String userId;


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorPlatform() {
        return creatorPlatform;
    }

    public void setCreatorPlatform(String creatorPlatform) {
        this.creatorPlatform = creatorPlatform;
    }

    public String getCreatoredSource() {
        return creatoredSource;
    }

    public void setCreatoredSource(String creatoredSource) {
        this.creatoredSource = creatoredSource;
    }

    public String getCreatoredType() {
        return creatoredType;
    }

    public void setCreatoredType(String creatoredType) {
        this.creatoredType = creatoredType;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public void setExpert(boolean expert) {
        isExpert = expert;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
