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
public class CategoryPojo implements HasItems<Category>{
    @SerializedName("drinks")
    public List<Category> items;

    @Override
    public List<Category> getItems() {
        return items;
    }
}
