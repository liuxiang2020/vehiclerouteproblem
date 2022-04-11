package com.liuxiang.vrp.element;

import lombok.Data;

@Data
public class Coordinate {
    private int row;
    private int col;
    private int value;

    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinate(int row, int col, int value) {
        this(row, col);
        this.value = value;
    }
}
