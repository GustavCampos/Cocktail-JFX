/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jorge.Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gustavo
 */
public class DrinkModel {
    private String idDrink;
    private String strDrink;
    private String strDrinkThumb;
    private String strInstructions;

    // The raw 1–15 fields:
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;

    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;

    // Convert raw fields → usable list:
    public List<String> getIngredientsFormatted() {
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            try {
                String ing = (String) this.getClass().getDeclaredField("strIngredient" + i).get(this);
                String mea = (String) this.getClass().getDeclaredField("strMeasure" + i).get(this);

                if (ing != null && !ing.isBlank()) {
                    if (mea == null) mea = "";
                    list.add(ing + " - " + mea);
                }
            } catch (Exception ignored) {}
        }
        return list;
    }

    public String getIdDrink() {
        return idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public String getStrInstructions() {
        return strInstructions;
    }
}
