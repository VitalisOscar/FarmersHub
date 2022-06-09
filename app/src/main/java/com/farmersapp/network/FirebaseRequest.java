package com.farmersapp.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Base class for one or more requests to firebase
 */
public abstract class FirebaseRequest extends Request {
    protected FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }

    protected FirebaseFirestore getFirestore(){
        return FirebaseFirestore.getInstance();
    }
}
