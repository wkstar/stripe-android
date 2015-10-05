package com.stripe.android;

import com.stripe.android.Http;
import com.stripe.android.compat.AsyncTask;

public abstract class HttpAsync extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        return Http.get(urls[0]);
    }
}