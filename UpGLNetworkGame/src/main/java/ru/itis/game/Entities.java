package ru.itis.game;

public enum Entities {
    Ball(1), House(500), Sky(3), Rule(4), Ball2(5), House2(6), Camera(7), Camera2(8)
    ;

    public final int id;

    Entities(int id) {
        this.id = id;
    }
}
