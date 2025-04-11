package com.capstonebe.capstonebe.inquiry.entity;

public enum QuestionType {
    ITEM("분실물"), USER("회원"), ETC("기타");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }
}