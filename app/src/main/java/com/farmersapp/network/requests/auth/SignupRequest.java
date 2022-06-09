package com.farmersapp.network.requests.auth;

import android.content.Context;

import com.farmersapp.network.Request;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.models.User;

public class SignupRequest extends Request {
    User user;
    String password;

    public SignupRequest(User user, String password){
        this.user = user;
        this.password = password;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        // Create auth record
        getAuth().createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    // Add record to db
                    user.setId(authResult.getUser().getUid());

                    getFirestore().collection(User.DB_COLLECTION_NAME)
                            .document(user.getId())
                            .set(user.toMap())
                            .addOnSuccessListener(unused -> {
                                dispatcher.onSuccess(user);
                            })
                            .addOnFailureListener(dispatcher::onFail);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
