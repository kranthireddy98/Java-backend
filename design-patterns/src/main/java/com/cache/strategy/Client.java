package com.cache.strategy;

public class Client {
    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor(new UpperCaseStrategy());

        textEditor.publishTex("Hello Strategy Pattern");

        textEditor.setFormatter(new LowerCaseStrategy());

        textEditor.publishTex("Hello Strategy Pattern");

        TextEditor editor = new TextEditor(text -> text.replace(" ", "_"));
        editor.publishTex("Snake Case Example");
    }
}
