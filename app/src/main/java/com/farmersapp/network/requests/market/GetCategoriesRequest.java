package com.farmersapp.network.requests.market;

import android.content.Context;

import com.farmersapp.network.FirebaseRequest;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.Category;
import com.farmersapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class GetCategoriesRequest extends FirebaseRequest {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        getFirestore().collection(Category.DB_COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Category> categories = new ArrayList<>();

                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        categories.add(Category.fromMap(snapshot.getData()));
                    }

                    // Return result
                    dispatcher.onSuccess(categories);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
