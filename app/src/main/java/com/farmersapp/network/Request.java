package com.farmersapp.network;

import android.content.Context;

import com.farmersapp.models.User;

/**
 * Base class for a network request to do something
 */
public abstract class Request {
    /**
     * Logic for the network request to be dispatched
     * @param context The current app context
     * @param loggedInUser The logged in user, if session is active
     * @param dispatcher The instance of the request dispatcher that dispatched this request, to receive callbacks
     */
    public abstract void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher);
}
