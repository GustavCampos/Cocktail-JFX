/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.jorge.Models;

import java.util.List;

/**
 *
 * @author gustavo
 * @param <T>
 */
public interface HasItems<T extends HasValue>{
    List<T> getItems();
}
