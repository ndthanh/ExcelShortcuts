package com.example.thanhnguyen.excelshortcuts;

import android.database.sqlite.SQLiteDatabase;

public class Shortcut {

    private int _id;
    private String shortcutButton1;
    private String shortcutButton2;
    private String shortcutButton3;
    private String shortcutButton4;
    private String description;
    private String category;

    public Shortcut(){}

    public Shortcut(String description) {
        this.description = description;
    }

    public Shortcut(int _id, String shortcutButton1, String shortcutButton2, String shortcutButton3, String shortcutButton4, String description) {
        this._id = _id;
        this.shortcutButton1 = shortcutButton1;
        this.shortcutButton2 = shortcutButton2;
        this.shortcutButton3 = shortcutButton3;
        this.shortcutButton4 = shortcutButton4;
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public String getShortcutButton1() {
        return shortcutButton1;
    }

    public String getShortcutButton2() {
        return shortcutButton2;
    }

    public String getShortcutButton3() {
        return shortcutButton3;
    }

    public String getShortcutButton4() {
        return shortcutButton4;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setShortcutButton1(String shortcutButton1) {
        this.shortcutButton1 = shortcutButton1;
    }

    public void setShortcutButton2(String shortcutButton2) {
        this.shortcutButton2 = shortcutButton2;
    }

    public void setShortcutButton3(String shortcutButton3) {
        this.shortcutButton3 = shortcutButton3;
    }

    public void setShortcutButton4(String shortcutButton4) {
        this.shortcutButton4 = shortcutButton4;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
