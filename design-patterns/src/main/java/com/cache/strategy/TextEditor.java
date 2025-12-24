package com.cache.strategy;

public class TextEditor {

    private TextFormatter textFormatter;

    // We can set the strategy at runtime via the constructor or a setter
    public TextEditor(TextFormatter textFormatter)
    {
        this.textFormatter = textFormatter;
    }

    public void setFormatter(TextFormatter formatter)
    {
        this.textFormatter= formatter;
    }

    public void publishTex(String string)
    {
        String formatted = textFormatter.format(string);
        System.out.println(formatted);
    }


}
