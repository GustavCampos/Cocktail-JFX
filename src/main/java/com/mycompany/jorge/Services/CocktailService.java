/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jorge.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.jorge.Models.AlcoholicPojo;
import com.mycompany.jorge.Models.CategoryPojo;
import com.mycompany.jorge.Models.DrinkModel;
import com.mycompany.jorge.Models.FilterDrinkPojo;
import com.mycompany.jorge.Models.GlassPojo;
import com.mycompany.jorge.Models.HasItems;
import com.mycompany.jorge.Models.HasValue;
import com.mycompany.jorge.Models.IngredientPojo;
import com.mycompany.jorge.Models.ListItem;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gustavo
 */
public class CocktailService {
    private final HttpClient client =  HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String drinkEndpoint = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php";
    private final String searchEndpoint = "https://www.thecocktaildb.com/api/json/v1/1/search.php";
    private final String filterEndpoint = "https://www.thecocktaildb.com/api/json/v1/1/filter.php";

    private final URI randomEndpoint = URI.create("https://www.thecocktaildb.com/api/json/v1/1/random.php");
    private final URI categoryEndpoint = URI.create("https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");
    private final URI glassEndpoint = URI.create("https://www.thecocktaildb.com/api/json/v1/1/list.php?g=list");
    private final URI ingredientEndpoint = URI.create("https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list");
    private final URI alcoholicEndpoint = URI.create("https://www.thecocktaildb.com/api/json/v1/1/list.php?a=list");
    
    private static String buildUrl(String baseUrl, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(baseUrl);
        sb.append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            sb.append("&");
        }

        sb.deleteCharAt(sb.length() - 1); // remove last &
        return sb.toString();
    }
    
    private HttpResponse<String> simpleRequest(URI endpoint) throws Exception {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(endpoint)
                .build();
        
        return client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );
    }
    
    private <T extends HasItems<? extends HasValue>> List<String> fetchList(
        URI endpoint, Class<T> type
    ) throws Exception {
        HttpResponse<String> response = this.simpleRequest(endpoint);
        T data = gson.fromJson(response.body(), type);
        return data.getItems().stream().map(HasValue::getValue).toList();
    }
    
    public List<String> fetchCategories() throws Exception {
        return fetchList(categoryEndpoint, CategoryPojo.class);
    }
    
    public List<String> fetchGlasses() throws Exception {
        return fetchList(glassEndpoint, GlassPojo.class);
    }
    
    public List<String> fetchIngredients() throws Exception {
        return fetchList(ingredientEndpoint, IngredientPojo.class);
    }
    
    public List<String> fetchAlcoholic() throws Exception {
        return fetchList(alcoholicEndpoint, AlcoholicPojo.class);
    }
    
    public List<ListItem> fetchDrinksFromFilters(
        String category,
        String glass,
        String ingredient,
        String alcoholic
    ) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (category != null)   params.put("c", category);
        if (glass != null)      params.put("g", glass);
        if (ingredient != null) params.put("i", ingredient);
        if (alcoholic != null)  params.put("a", alcoholic);
        
        String uri = buildUrl(filterEndpoint, params);
        
        HttpResponse<String> response = this.simpleRequest(URI.create(uri));
        FilterDrinkPojo data = gson.fromJson(response.body(), FilterDrinkPojo.class);
        
        return data.drinks.stream()
                .map(d -> new ListItem(d.id, d.name, "drink"))
                .toList();
    }
    
    public DrinkModel fetchDrink(String id) throws Exception {
        String uri = buildUrl(drinkEndpoint, Map.of("i", id));
        HttpResponse<String> response = this.simpleRequest(URI.create(uri));
        JsonObject root = gson.fromJson(response.body(), JsonObject.class);
        
        return gson.fromJson(
                root.getAsJsonArray("drinks").get(0), 
                DrinkModel.class
        );
    }
    
    public DrinkModel fetchRandomDrink() throws Exception {
        HttpResponse<String> response = this.simpleRequest(randomEndpoint);
        JsonObject root = gson.fromJson(response.body(), JsonObject.class);
        
        return gson.fromJson(
                root.getAsJsonArray("drinks").get(0), 
                DrinkModel.class
        );
    }
    
    public List<ListItem> fetchDrinks(String query) throws Exception {
        String uri = buildUrl(searchEndpoint, Map.of("s", query));
        HttpResponse<String> response = this.simpleRequest(URI.create(uri));
        
        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonArray drinks = root.getAsJsonArray("drinks");

        List<ListItem> items = new ArrayList<>();
        if (drinks != null) {
            for (int i = 0; i < drinks.size(); i++) {
                JsonObject drink = drinks.get(i).getAsJsonObject();
                String id = drink.get("idDrink").getAsString();
                String name = drink.get("strDrink").getAsString();

                items.add(new ListItem(id, name, "drink"));
            }
        }
        
        return items;
    }
}
