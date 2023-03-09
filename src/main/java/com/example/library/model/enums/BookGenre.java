package com.example.library.model.enums;

public enum BookGenre {
    FICTION("Fiction"), NONFICTION("Non-fiction"), DRAMA("Drama"), POETRY("Poetry");

    private String displayName;
    private BookGenre(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
