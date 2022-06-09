package com.farmersapp.network.requests.market;

import android.content.Context;

import com.farmersapp.network.FirebaseRequest;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.Product;
import com.farmersapp.models.User;
import com.google.firebase.firestore.Query;

public class GetSingleProductRequest extends FirebaseRequest {

    // Seller d limits to a single product for a particular seller, e.g from a seller's dashboard
    String id, seller_id;

    public GetSingleProductRequest(String id){
        this(id, null);
    }

    public GetSingleProductRequest(String id, String seller_id){
        this.id = id;
        this.seller_id = seller_id;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        Query query = getFirestore().collection(Product.DB_COLLECTION_NAME)
                .whereEqualTo("id", this.id);

        // seller id constraint
        if(seller_id != null){
            query = query.whereEqualTo("seller.id", seller_id);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Product product;

                    if(queryDocumentSnapshots.size() == 0){
                        dispatcher.onFail(new Exception("Product not found or cannot be accessed on this screen"));
                    }

                    product = Product.fromMap(queryDocumentSnapshots.getDocuments().get(0).getData());

                    // Return result
                    dispatcher.onSuccess(product);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
