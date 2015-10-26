package me.annygakh.tamara.model;

import com.plumillonforge.android.chipview.Chip;

/**
 * Created by annygakhokidze on 10/18/15.
 */
public class Ingredient implements Chip {
    private String mName;

    public Ingredient(String name) {
        this.mName = name;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        return mName.equals(that.mName);

    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }
}
