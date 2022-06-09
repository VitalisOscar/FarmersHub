package com.farmersapp.network.requests.market;

import android.content.Context;

import com.farmersapp.network.Request;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.helpers.Utils;
import com.farmersapp.models.Category;
import com.farmersapp.models.Product;
import com.farmersapp.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeedDbRequest extends Request {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        FirebaseFirestore db = getFirestore();
        WriteBatch batch = db.batch();

        for (Category c:categories()){
            batch.set(db.collection(Category.DB_COLLECTION_NAME).document(c.getId()), c.toMap());
        }

        for (Product p:products()){
            DocumentReference ref = db.collection(Product.DB_COLLECTION_NAME).document();
            p.setId(ref.getId());

            batch.set(ref, p.toMap());
        }

        batch.commit()
                .addOnSuccessListener(dispatcher::onSuccess)
                .addOnFailureListener(dispatcher::onFail);
    }

    private ArrayList<Product> products(){
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Category> categories = categories();

        for(Category c:categories){
            for(int i = 0; i < 30; i++){
                Map<String, Double> pricing = new HashMap<>();
                pricing.put("KG", 100.0);
                pricing.put("5KG", 450.0);

                products.add(new Product(
                        c.getName() + " product " + i,
                        "Nice product here for sale. Good quality. Healthy",
                        pricing,
                        c.getImageUrl(),
                        c,
                        new User("Seller 1", "seller1@gmail.com", "0700123456", new ArrayList<>())
                ));
            }
        }

        return products;
    }

    private ArrayList<Category> categories(){
        ArrayList<Category> categories = new ArrayList<>();
        String timestamp = Utils.getCurrentTimestamp();
        categories.add(new Category(
                "Cereals",
                "Cereals description",
                "https://st.depositphotos.com/1642482/2529/i/450/depositphotos_25296471-stock-photo-corn.jpg",
                "1",
                timestamp
        ));

        categories.add(new Category(
                "Nuts",
                "Nuts description",
                "https://static5.depositphotos.com/1013388/468/i/450/depositphotos_4686241-stock-photo-peanuts.jpg",
                "2",
                timestamp
        ));

        categories.add(new Category(
                "Fruits",
                "Fruits description",
                "https://st3.depositphotos.com/13349494/16394/i/450/depositphotos_163941980-stock-photo-fruits.jpg",
                "3",
                timestamp
        ));

        categories.add(new Category(
                "Vegetables",
                "Vegetables description",
                "https://static9.depositphotos.com/1177973/1240/i/450/depositphotos_12404675-stock-photo-fresh-vegetables-isolated-on-white.jpg",
                "4",
                timestamp
        ));

        categories.add(new Category(
                "Animal Products",
                "Animal products description",
                "https://static9.depositphotos.com/1177973/1240/i/450/depositphotos_12404675-stock-photo-fresh-vegetables-isolated-on-white.jpg",
                "5",
                timestamp
        ));

        categories.add(new Category(
                "Animal Feed",
                "Animal feed description",
                "https://st2.depositphotos.com/1821481/5447/i/450/depositphotos_54476775-stock-photo-rabbit-food.jpg",
                "6",
                timestamp
        ));

        return categories;
    }

}
