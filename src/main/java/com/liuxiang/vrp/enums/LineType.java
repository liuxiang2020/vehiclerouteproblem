package com.liuxiang.vrp.enums;

public enum LineType {
    ROUTE("route", 1, "路径"),
    BROKEN_LINE("broken_line", 2, "折线图");
    private String name;
    private int id;
    private String desc;

    LineType(String name, int id, String desc) {
        this.name = name;
        this.id = id;
        this.desc = desc;
    }


}
