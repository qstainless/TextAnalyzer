package com.gce.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Word {
    private final SimpleIntegerProperty wordRankProperty;
    private final SimpleStringProperty wordContentProperty;
    private final SimpleIntegerProperty wordFrequencyProperty;

    public Word(int wordRank, String wordContent, int wordFrequency) {
        this.wordRankProperty = new SimpleIntegerProperty(wordRank);
        this.wordContentProperty = new SimpleStringProperty(wordContent);
        this.wordFrequencyProperty = new SimpleIntegerProperty(wordFrequency);
    }

    public int getWordRank() {
        return wordRankProperty.get();
    }

    public void setWordRank(int wordRank) {
        this.wordRankProperty.set(wordRank);
    }

    public String getWordContent() {
        return wordContentProperty.get();
    }

    public void setWordContent(String wordContent) {
        this.wordContentProperty.set(wordContent);
    }

    public int getWordFrequency() {
        return wordFrequencyProperty.get();
    }

    public void setWordFrequency(int wordFrequency) {
        this.wordFrequencyProperty.set(wordFrequency);
    }
}
