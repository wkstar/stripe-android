package com.stripe.example.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

import com.stripe.model.Customer;


import com.stripe.android.compat.AsyncTask;

import com.stripe.example.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.example.dialog.ErrorDialogFragment;
import com.stripe.example.dialog.ProgressDialogFragment;
import com.stripe.example.PaymentForm;
import com.stripe.example.TokenList;

import java.util.ArrayList;


public class PaymentActivity extends FragmentActivity {

    /*
     * Change this to your publishable key.
     *
     * You can get your key here: https://manage.stripe.com/account/apikeys
     */
    public static final String PUBLISHABLE_KEY = "pk_test_ONUkI9pWcWTjA6L6EHu2QUJI";
    private ArrayList<Card> cards = new ArrayList<Card>();
    private ProgressDialogFragment progressFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);
    }

    public void saveCreditCard(PaymentForm form) {

        final Card card = new Card(
                form.getCardNumber(),
                form.getExpMonth(),
                form.getExpYear(),
                form.getCvc());

        boolean validation = card.validateCard();
        if (validation) {
            startProgress();
            new Stripe().createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            getTokenList().addToList(token);
                            cards.add(card);
                            finishProgress();
                        }
                        public void onError(Exception error) {
                            handleError(error.getLocalizedMessage());
                            finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            handleError("The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            handleError("The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            handleError("The CVC code that you entered is invalid");
        } else {
            handleError("The card details that you entered are invalid");
        }
    }

    public void payWithCard(Integer position)  {

        int duration = Toast.LENGTH_SHORT;
        com.stripe.Stripe.apiKey = "pk_test_ONUkI9pWcWTjA6L6EHu2QUJI";

        Customer tom = null;
        try {
            tom = Customer.retrieve("cus_5ILWUP9V8hptpF");
        } catch (CardException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (APIException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (AuthenticationException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (InvalidRequestException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (APIConnectionException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        }


        Card card = cards.get(position);


        Toast.makeText(getApplicationContext(), card.getNumber(), duration).show();
    }

    private void startProgress() {
        progressFragment.show(getSupportFragmentManager(), "progress");
    }

    private void finishProgress() {
        progressFragment.dismiss();
    }

    private void handleError(String error) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(R.string.validationErrors, error);
        fragment.show(getSupportFragmentManager(), "error");
    }

    private TokenList getTokenList() {
        return (TokenList)(getSupportFragmentManager().findFragmentById(R.id.token_list));
    }
}

public class StripeUtil extends AsyncTask<String, Void, Card> {
    protected Card doInBackground(String customerCode) {

        int duration = Toast.LENGTH_SHORT;
        com.stripe.Stripe.apiKey = "pk_test_ONUkI9pWcWTjA6L6EHu2QUJI";

        Customer tom = null;
        try {
            tom = Customer.retrieve("cus_5ILWUP9V8hptpF");
        } catch (CardException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (APIException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (AuthenticationException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (InvalidRequestException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        } catch (APIConnectionException e) {
            Toast.makeText(getApplicationContext(), e.toString(), duration).show();
        }

        return tom;
    }

    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }
}
