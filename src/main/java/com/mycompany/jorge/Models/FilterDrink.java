/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jorge.Models;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author gustavo
 */
public class FilterDrink {
    @SerializedName("strDrink")
    public String name;
    
    @SerializedName("strDrinkThumb")
    public String image;
    
    @SerializedName("idDrink")
    public String id;
}
