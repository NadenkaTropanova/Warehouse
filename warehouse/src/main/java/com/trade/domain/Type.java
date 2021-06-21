package com.trade.domain;

public enum Type {
    SOLD("Продано"), DELIVERED("Поставлено"), ORDERED("Заказано"), CANCELLED("Списано");

    private String name;

    Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
