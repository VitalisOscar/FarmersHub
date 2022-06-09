package com.farmersapp.network.requests.market;

import android.content.Context;

import com.farmersapp.network.Request;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.Product;
import com.farmersapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class GetRecentProductsRequest extends Request {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        getFirestore().collection(Product.DB_COLLECTION_NAME)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(6)
                .get()
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
