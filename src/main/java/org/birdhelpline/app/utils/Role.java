package org.birdhelpline.app.utils;

public enum Role {
    ADMIN(1), Volunteer(5), Doctor(6), Driver(7), Fosterer(8), Receptionist(9);
    int id;

    Role(int id) {
        this.id = id;
    }
}
