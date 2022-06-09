package com.farmersapp.models;

import com.farmersapp.helpers.Utils;

import java.util.ArrayList;
import java.util.Map;

public class Product extends Model {
    public static final String DB_COLLECTION_NAME = "products";

    private String name, description, imageUrl;
    private Category category = null;
    private User seller = null;
    private Map<String, Double> pricing;
    private ArrayList<String> searchIndex;


    public Product(String name, String description, Map<String, Double> pricing, String imageUrl, Category category, User seller) {
        this(name, description, pricing, imageUrl, category, seller, null, null, null);
    }

    public Product(String name, String description, Map<String, Double> pricing, String imageUrl, Category category, User seller, ArrayList<String> searchIndex, String id, String timestamp) {
        super(id, timestamp);

        this.name = name;
        this.description = description;
        this.pricing = pricing;
        this.imageUrl = imageUrl;
        this.category = category;
        this.seller = seller;

        this.searchIndex = searchIndex == null ?
                Utils.generateSearchIndex(new ArrayList<>(), name, category.getName()) : searchIndex;
    }

    public static Product fromMap(Map<String, Object> data){
        if(data == null) return null;

        return new Product(
            (String) data.get("name"),
            (String) data.get("description"),
            (Map<String, Double>) data.get("pricing"),
            (String) data.get("imageUrl"),
            Category.fromMap((Map<String, Object>) data.get("category")),
            User.fromMap((Map<String, Object>) data.get("seller")),
            (ArrayList<String>) data.get("searchIndex"),
            (String) data.get("id"),
            (String) data.get("timestamp")
        );
    }

    @Override
    public Map<String, Object> toMiniMap(){
        Map<String, Object> data = super.toMiniMap();

        Map<String, Object> seller_map = seller == null ? null : seller.toMiniMap();
        Map<String, Object> category_map = category == null ? null : category.toMiniMap();

        data.put("name", name);
        data.put("description", description);
        data.put("imageUrl", imageUrl);
        data.put("pricing", pricing);
        data.put("seller", seller_map);
        data.put("category", category_map);

        return data;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> data = super.toMap();
        data.put("searchIndex", searchIndex);
        return data;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public User getSeller() {
        return seller;
    }

    public Map<String, Double> getPricing() {
        return pricing;
    }

    public ArrayList<String> getSearchIndex() {
        return searchIndex;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice(){
        if(pricing.keySet().isEmpty()){
            return "Price Unavailable";
        }

        String min_key = (String) pricing.keySet().toArray()[0];
        double min = pricing.get(min_key);

        for(String key: pricing.keySet()){
            if(pricing.get(key) < min){
                min = pricing.get(key);
                min_key = key;
            }
        }

        return "From " + Utils.numberFormat(min) + " / " + min_key;
    }
}
