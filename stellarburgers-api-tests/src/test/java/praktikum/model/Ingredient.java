package praktikum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    public String _id;
    public String name;
    public IngredientType type;
    public float price;
    public String image;
    public String image_mobile;
    public String image_large;
    public int __v;
}