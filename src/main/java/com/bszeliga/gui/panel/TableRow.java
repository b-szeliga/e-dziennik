package com.bszeliga.gui.panel;

import lombok.Getter;

@Getter
public class TableRow {
    private int id;
    private String name;
    private String lastname;
    private int role;
    private String school;

    public TableRow(int id, String name, String lastname, int role, String School) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        school = School;
    }
}
