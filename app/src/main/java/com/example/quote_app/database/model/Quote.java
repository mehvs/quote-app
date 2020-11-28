package com.example.quote_app.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quotes")
public class Quote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "quote_text")
    private String quoteText;

    @ColumnInfo(name = "author")
    private String author;

    public Quote(String quoteText, String author){
        this.quoteText = quoteText;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
