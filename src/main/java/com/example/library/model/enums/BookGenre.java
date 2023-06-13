package com.example.library.model.enums;

public enum BookGenre {
    FANTASY("Fantasy"), NONFICTION("Non-fiction"), NOVEL("Novel"), POETRY("Poetry");

    private final String displayName;
    BookGenre(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}