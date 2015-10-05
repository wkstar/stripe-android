package com.stripe.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.support.v4.app.ListFragment;
import android.widget.SimpleAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stripe.android.Http;
import com.stripe.android.HttpAsync;
import com.stripe.android.compat.AsyncTask;
import com.stripe.example.TokenList;
import com.stripe.example.R;
import com.stripe.android.model.Token;
import com.stripe.example.activity.PaymentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stripe.example.R.id.expMonth;

public class TokenListFragment extends ListFragment implements TokenList {

    List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
    SimpleAdapter adapter;

    private String token = "3132a6d4c2b35d00d5a9b4919facc836cf918d33";
    private String customerId = "cus_5ILWUP9V8hptpF";

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        payUsingCard(position);
    }

    @Override
    public void onViewCreated(android.view.View view, android.os.Bundle savedInstanceState) {

        CardList cardList = new CardList();
        cardList.execute("http://localhost:80/index.php/stripeCardList?customer=" + customerId + "&token=" + token);

        super.onViewCreated(view, savedInstanceState);
        adapter = new SimpleAdapter(getActivity(),
                                    listItems,
                                    R.layout.list_item_layout,
                                    new String[]{"last4", "tokenId"},
                                    new int[]{R.id.last4, R.id.tokenId});
        setListAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setListAdapter(null);
    }

    public void payUsingCard(Integer position) {

        ((PaymentActivity)getActivity()).payWithCard(position);
    }

    @Override
    public void addToList(Token token) {
        String endingIn = getResources().getString(R.string.endingIn);
        Map<String, String> map = new HashMap<String, String>();
        map.put("last4", endingIn + " " + token.getCard().getLast4());
        map.put("tokenId", token.getId());
        listItems.add(map);
        adapter.notifyDataSetChanged();
    }


    class CardList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String fan = urls[0];
            return Http.get(fan) + "concat";
        }
        @Override
        protected void onPostExecute(String result) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("last4", "1233");
            map.put("tokenId", "tmiead");
            listItems.add(map);
        }

    }
}