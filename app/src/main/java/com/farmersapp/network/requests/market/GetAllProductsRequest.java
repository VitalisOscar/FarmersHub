package com.farmersapp.network.requests.market;

import android.content.Context;

import com.farmersapp.network.FirebaseRequest;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.Product;
import com.farmersapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class GetAllProductsRequest extends FirebaseRequest {

    Map<String, Object> filters;
    int page = 0, limit = 15;

    // Help in keeping track of where to start from on next page
    DocumentSnapshot previousSnapshot = null;

    public GetAllProductsRequest(Map<String, Object> filters, int page, int limit){
        this.filters = filters;
        this.page = page;
        this.limit = limit;
        this.previousSnapshot = null;
    }

    public GetAllProductsRequest(Map<String, Object> filters){
        this(filters, 0, 15);
    }

    public GetAllProductsRequest setPage(int page){
        this.page = page;
        return this;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        Query query = getFirestore().collection(Product.DB_COLLECTION_NAME);

        // Add Filters
        if(filters.containsKey("category_id")){
            query = query.whereLessThanOrEqualTo("category.id", filters.get("category_id"));
        }

        if(filters.containsKey("seller_id")){
            query = query.whereLessThanOrEqualTo("seller.id", filters.get("seller_id"));
        }

        if(filters.containsKey("keyword")){
            query = query.whereArrayContains("keyword", filters.get("keyword"));
        }

        query = query.orderBy("timestamp", Query.Direction.DESCENDING);

        // Pagination
        if(page > 0 && previousSnapshot != null){
            query = query.startAfter(previousSnapshot).limit(limit);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> results = new ArrayList<>();

                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        results.add(Product.fromMap(snapshot.getData()));
                    }

                    // Return result
                    dispatcher.onSuccess(results);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
