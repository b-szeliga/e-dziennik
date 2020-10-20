package com.bszeliga.gui;

import lombok.Getter;

@Getter
public class TableRow {
    private int id;
    private String name;
    private String lastname;
    private int role;
    private String school;
    private boolean verified;

    public TableRow(int id, String name, String lastname, int role, String school, boolean verified) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        this.school = school;
        this.verified = verified;
    }
}
