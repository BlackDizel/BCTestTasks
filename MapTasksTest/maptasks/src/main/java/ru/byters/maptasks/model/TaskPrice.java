package ru.byters.maptasks.model;

import java.io.Serializable;

/**
 * Created by AlexMoto on 04.07.2015.
 */
public class TaskPrice implements Serializable {
    private int price;
    private String description;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
