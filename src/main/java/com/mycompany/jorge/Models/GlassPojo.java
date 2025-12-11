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
public class GlassPojo implements HasItems<Glass>{
    @SerializedName("drinks")
    public List<Glass> items;

    @Override
    public List<Glass> getItems() {
        return items;
    }
}
