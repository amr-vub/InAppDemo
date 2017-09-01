package com.aga.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wl.sips.inapp.sdk.pojo.PaymentProviderResponse;

public class BCActivity extends AppCompatActivity {

    TextView amount;
    RedirectionResponse redirectionResponse;
    PaymentProviderResponse paymentProviderResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button payButton = (Button) findViewById(R.id.bcBtnPay);
        amount = (TextView) findViewById(R.id.amount);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Please insert an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                AsynctT asyncT = new AsynctT();
                asyncT.execute();

                Toast.makeText(getApplicationContext(), "Please wait...redirecting to BC App", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private class AsynctT extends AsyncTask<Void, Void, Void> {

        String text = "";

        // String dataIn;
        Gson gson = new Gson();

        //HttpsURLConnection httpURLConnection;

        // MainActivity mainActivity;
        @Override
        protected Void doInBackground(Void... params) {
            System.out.println(getTxtLen(amount));

            redirectionResponse = NW.post_BC(getTxt(amount).toString(), "BCMCMOBILE", "PAYMENTPROVIDERORDER");
            //redirectionResponse =  NW.post_WL();

            paymentProviderResponse = Payment.paymentProviderOrder(getApplicationContext(), redirectionResponse);
            // addCardResponse = Payment.addCard(getApplicationContext(),"", "","" ,redirectionResponse);

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            //amount.setText(redirectionResponse.getRedirectionUrl());
            if (redirectionResponse == null || redirectionResponse.getRedirectionStatusCode() != 0) {
                if (redirectionResponse != null) {
                    Toast.makeText(getApplicationContext(),"Response code: " + redirectionResponse.getRedirectionStatusCode() + " \nStatusMessage: " + redirectionResponse.getRedirectionStatusMessage()
                            , Toast.LENGTH_SHORT).show();
                }
                return;
            }
            String uri = Uri.parse(paymentProviderResponse.getOuterRedirectionUrl()).toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
            /*
            String uri = Uri.parse(redirectionResponse.getRedirectionUrl())
                    .buildUpon()
                    .appendQueryParameter("redirectionData", redirectionResponse.getRedirectionData())
                    .appendQueryParameter("redirectionVersion", redirectionResponse.getRedirectionVersion() )
                    .build().toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
            */
        }

        protected CharSequence getTxt(TextView v) {
            return v.getText();
        }

        protected int getTxtLen(TextView v) {
            return v.getText().length();
        }


    }

}
