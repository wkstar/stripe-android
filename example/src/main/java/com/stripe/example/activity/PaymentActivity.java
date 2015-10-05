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

import com.stripe.android.Http;
import com.stripe.android.HttpAsync;
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

    public static final String PUBLISHABLE_KEY = "pk_test_ONUkI9pWcWTjA6L6EHu2QUJI";
    private ArrayList<Card> cards = new ArrayList<Card>();
    private ProgressDialogFragment progressFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);

        progressFragment = ProgressDialogFragment.newInstance(R.string.progressMessage);


        StripeUtil getCard = new StripeUtil();
        getCard.execute();
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

            StripeUtil getCard = new StripeUtil();
            getCard.execute();
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(getApplicationContext(), "DONE", duration).show();
        }
    }

    public void payWithCard(Integer position)  {

        StripeUtil getCard = new StripeUtil();
        getCard.execute();
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(getApplicationContext(), "DONE", duration).show();
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

    class StripeUtil extends AsyncTask<String, Void, Customer> {
        protected Customer doInBackground(String... hate) {
            com.stripe.Stripe.apiKey = PUBLISHABLE_KEY;

            Customer tom = null;
            try {
                tom = Customer.retrieve("cus_5ILWUP9V8hptpF");
            } catch (CardException e) {
                System.out.print(e.toString());
            } catch (APIException e) {
                System.out.print(e.toString());
            } catch (AuthenticationException e) {
                System.out.print(e.toString());
            } catch (InvalidRequestException e) {
                System.out.print(e.toString());
            } catch (APIConnectionException e) {
                System.out.print(e.toString());
            }

            return tom;
        }

        protected void onPostExecute(Customer result) {
        }
    }



}