/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jorge.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author gustavo
 */
public class IngredientPojo implements HasItems<Ingredient>{
    @SerializedName("drinks")
    List<Ingredient> items;

    @Override
    public List<Ingredient> getItems() {
        return items;
    }
}
