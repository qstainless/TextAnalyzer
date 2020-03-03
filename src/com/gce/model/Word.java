package com.gce.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Word model used to populate the text analysis results
 * in a TableView.
 */
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

    public String getWordContent() {
        return wordContentProperty.get();
    }

    public int getWordFrequency() {
        return wordFrequencyProperty.get();
    }
}
