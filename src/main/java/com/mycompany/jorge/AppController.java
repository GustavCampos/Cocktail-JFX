package com.mycompany.jorge;

import com.mycompany.jorge.Models.DrinkModel;
import com.mycompany.jorge.Models.ListItem;
import com.mycompany.jorge.Services.CocktailService;
import java.util.List;
import java.util.concurrent.Callable;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AppController {
    private final CocktailService service = new CocktailService();
    
    // Search drinks by name
    @FXML private TextField searchDrinkTF;
    
    // Search items by name
//    @FXML
//    private Button searchIngredient;
//    @FXML
//    private TextArea searchIngredientTA;
    
    // Filters
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ComboBox<String> glassFilter;
    @FXML private ComboBox<String> ingredientFilter;
    @FXML private ComboBox<String> alcoholicFilter;

    // Item list
    @FXML private ListView<ListItem> itemList;
    
    // Drink viewer
    @FXML private ProgressIndicator drinkLoadingIndicator;
    @FXML private Label drinkPlaceholderText;
    @FXML private VBox drinkContentBox;
    @FXML private ImageView drinkImage;
    @FXML private Label drinkName;
    @FXML private VBox drinkIngredientsList;
    @FXML private Text drinkInstructions;
    
    // =========================================================================
    // METHODS
    // =========================================================================
    @FXML
    public void initialize() {
        loadFilter(categoryFilter, service::fetchCategories, "Select a category...");
        loadFilter(glassFilter, service::fetchGlasses, "Select a glass...");
        loadFilter(ingredientFilter, service::fetchIngredients, "Select an ingredient...");
        loadFilter(alcoholicFilter, service::fetchAlcoholic, "Select an alcoholic...");
        
        itemList.setCellFactory(list -> new ListCell<ListItem>() {
            @Override
            protected void updateItem(ListItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); } 
                else { setText(item.label); }
            }
        });
    }
    
    private void loadFilter(
        ComboBox<String> comboBox, 
        Callable<List<String>> fetcher,
        String prompt
    ) {
        Task<List<String>> task = new Task() {
            @Override
            protected List<String> call() throws Exception {
                return fetcher.call();
            }
        };   
        task.setOnSucceeded(e -> {
            List<String> values = task.getValue();
            comboBox.getItems().setAll(values);
            comboBox.getItems().add(0, null);   
            comboBox.setPromptText(prompt);
        });
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }
    
    @FXML
    private void searchDrink() {  
        Task<List<ListItem>> task = new Task<>() {
            @Override
            protected List<ListItem> call() throws Exception {
                return service.fetchDrinks(searchDrinkTF.getText());
            }
        };

        task.setOnSucceeded(e -> {
            List<ListItem> results = task.getValue();
            itemList.getItems().setAll(results);
        });

        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
    
    @FXML
    private void searchDrinkWithFilter() {
        boolean noFiltersSelected =
            categoryFilter.getValue() == null &&
            glassFilter.getValue() == null &&
            ingredientFilter.getValue() == null &&
            alcoholicFilter.getValue() == null;
        
        if (noFiltersSelected) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Filters Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one filter before searching.");
            alert.showAndWait();
            return;
        }
        
        Task<List<ListItem>> task = new Task<>() {
            @Override
            protected List<ListItem> call() throws Exception {
                return service.fetchDrinksFromFilters(
                    categoryFilter.getValue(),
                    glassFilter.getValue(),
                    ingredientFilter.getValue(),
                    alcoholicFilter.getValue()
                );
            }
        };

        task.setOnSucceeded(e -> {
            List<ListItem> results = task.getValue();
            itemList.getItems().setAll(results);
        });

        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }
    
    @FXML
    private void displayListItem(javafx.scene.input.MouseEvent event) {
        ListItem selected = itemList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        drinkLoadingIndicator.setVisible(true);
        drinkContentBox.setVisible(false);
        
        Task<DrinkModel> task = new Task<>() {
            @Override
            protected DrinkModel call() throws Exception {
                return service.fetchDrink(selected.id);
            }
        };
        
        task.setOnSucceeded(e -> {            
            DrinkModel drink = task.getValue();
            drinkName.setText(drink.getStrDrink());
            drinkImage.setImage(new Image(drink.getStrDrinkThumb(), true));           
            drinkIngredientsList.getChildren().clear();
            for (String ing: drink.getIngredientsFormatted()) {
                Label label = new Label(ing);
                label.setWrapText(true);
                drinkIngredientsList.getChildren().add(label);
            }
            drinkInstructions.setText(drink.getStrInstructions());


            drinkContentBox.setVisible(true);
            drinkPlaceholderText.setVisible(false);
            drinkLoadingIndicator.setVisible(false);
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            drinkLoadingIndicator.setVisible(false);
        });

        new Thread(task).start();
    }
    
    @FXML
    private void displayRandomDrink() {
        drinkLoadingIndicator.setVisible(true);
        drinkContentBox.setVisible(false);
        
        Task<DrinkModel> task = new Task<>() {
            @Override
            protected DrinkModel call() throws Exception {
                return service.fetchRandomDrink();
            }
        };
        
        task.setOnSucceeded(e -> {            
            DrinkModel drink = task.getValue();
            drinkName.setText(drink.getStrDrink());
            drinkImage.setImage(new Image(drink.getStrDrinkThumb(), true));           
            drinkIngredientsList.getChildren().clear();
            for (String ing: drink.getIngredientsFormatted()) {
                Label label = new Label(ing);
                label.setWrapText(true);
                drinkIngredientsList.getChildren().add(label);
            }
            drinkInstructions.setText(drink.getStrInstructions());


            drinkContentBox.setVisible(true);
            drinkPlaceholderText.setVisible(false);
            drinkLoadingIndicator.setVisible(false);
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            drinkLoadingIndicator.setVisible(false);
        });

        new Thread(task).start();
    }
}
