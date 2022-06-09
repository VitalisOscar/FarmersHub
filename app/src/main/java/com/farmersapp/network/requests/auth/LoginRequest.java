package com.farmersapp.network.requests.auth;

import android.content.Context;

import com.farmersapp.network.Request;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.User;

public class LoginRequest extends Request {
    String email, password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        // Auth login
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Get the record from db
                    getFirestore().collection(User.DB_COLLECTION_NAME)
                            .document(authResult.getUser().getUid())
                            .get()
                            .addOnSuccessListener(snapshot -> {
                                dispatcher.onSuccess(User.fromMap(snapshot.getData()));
                            })
                            .addOnFailureListener(dispatcher::onFail);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
