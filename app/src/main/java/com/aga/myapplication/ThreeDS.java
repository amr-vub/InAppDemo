package com.aga.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wl.sips.inapp.sdk.PaymentManager;
import com.wl.sips.inapp.sdk.pojo.CardCheckEnrollmentResponse;
import com.wl.sips.inapp.sdk.pojo.OrderResponse;
import com.wl.sips.inapp.sdk.pojo.PaymentProviderResponse;

import java.net.URLEncoder;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static android.R.attr.data;
import static android.R.attr.drawable;
import static com.aga.myapplication.R.id.textView;

public class ThreeDS extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText amount;
    EditText cardNbr;
    EditText expirayData;
    EditText csc;
    Button pay, scan;
    Spinner spinner;
    RedirectionResponse redirectionResponse = null;
    OrderResponse cardValidateAuthenticationAndOrderResponse = null;
    CardCheckEnrollmentResponse cardCheckEnrollmentResponse = null;
    final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface();
    WebView webView;
    String PaRes = null;
    String[] payBrands = {"MASTERCARD", "VISA", "MAESTRO", "VPAY"};
    String PaymentBrand = "MASTERCARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_ds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner = (Spinner) findViewById(R.id.payMeans);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, payBrands);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        amount = (EditText) findViewById(R.id.amount);
        cardNbr = (EditText) findViewById(R.id.crdNbr);
        expirayData = (EditText) findViewById(R.id.expiryDate);
        csc = (EditText) findViewById(R.id.csc);

        scan = (Button) findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(v);
            }
        });

        pay = (Button) findViewById(R.id.py3ds);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().equals("") || cardNbr.getText().toString().equals("") ||
                        expirayData.getText().toString().equals("") || csc.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill ALL fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTsk().execute();
            }
        });
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
        webView.getSettings().setLoadWithOverviewMode(true);
/**/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    protected CharSequence getTxt(TextView v) {
        return v.getText();
    }

    protected int getTxtLen(TextView v) {
        return v.getText().length();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PaymentBrand = payBrands[position];
        System.out.println("swdw------------------- " + PaymentBrand);
        switch (position)
        {
            case 0: pay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.mastercard));
                break;
            case 1: pay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.visa));
                break;
            case 2: pay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.meastero));
                break;
            case 3: pay.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vpay));
                break;
        }
        if(PaymentBrand == "MASTERCARD" || PaymentBrand == "VISA")
        {
            scan.setVisibility(View.VISIBLE);
        }
        else
        {
            scan.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncTsk extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //System.out.println(getTxtLen(amount));
            if (getTxtLen(amount) == 0 || getTxtLen(cardNbr) == 0) {
                System.out.println(" return null;");
                return null;
            }
            redirectionResponse = NW.post_BC(getTxt(amount).toString(), PaymentBrand, "THREEDSECUREANDORDER");

            if (redirectionResponse != null) {

                cardCheckEnrollmentResponse = Payment.cardCheckEnrollment(getApplicationContext(), redirectionResponse,
                        getTxt(cardNbr).toString(), getTxt(csc).toString(), getTxt(expirayData).toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            //textView.setText(getTransactionDataResponse.toString());

            if (cardCheckEnrollmentResponse.getRedirectionStatusCode().equals("00")) {
                String data = "<script type=\"text/javascript\">window.onload = function(){"
                        + "document.forms['acs'].submit();" +
                        "}</script>"
                        + "Please wait..."
                        + "<form method=\"POST\" name=\"acs\" action=\"" +
                        cardCheckEnrollmentResponse.getAuthentRedirectionUrl() + "\"  style=\"display:none;\">"
                        + "<input type=\"text\" name=\"MD\" value=\"" +
                        1 + "\">"
                        + "<input type=\"text\" name=\"TermUrl\" value=\"https://demosips.000webhostapp.com/reply.php\">"
                        + "<input type=\"text\" name=\"PaReq\" value=\"" +
                        cardCheckEnrollmentResponse.getPaReqMessage() + "\">"
                        + "<input type=\"submit\" value=\"Submit\">"
                        + "</form>";
                System.out.println(data);
                webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            /*
            String uri = Uri.parse(cardCheckEnrollmentResponse.getAuthentRedirectionUrl())
                    .buildUpon()
                    .appendQueryParameter("MD", "1")
                    .appendQueryParameter("PaReq", cardCheckEnrollmentResponse.getPaReqMessage())
                    .appendQueryParameter("TermUrl", cardCheckEnrollmentResponse.getPaReqMessage())
                    .build().toString();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
*/
            }
            else
            {
                Toast.makeText(getApplicationContext(), "ERROR .. Response Code: " + cardCheckEnrollmentResponse.getRedirectionStatusCode()
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void putPaRes(String paRes) {
            // Url encode algorithm to apply on the pares received
            // Call cardValidateAuthentication with paRes message encoded
            System.out.println("---------------------Call cardValidateAuthentication with paRes message encoded-------------------------");
            System.out.println(paRes);
            PaRes = URLEncoder.encode(paRes);
            new SecondAsyncTask().execute();
        }
    }


    private class SecondAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            cardValidateAuthenticationAndOrderResponse = Payment.cardValidateAuthenticationAndOrder(getApplicationContext(), PaRes, cardCheckEnrollmentResponse);

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            //textView.setText(getTransactionDataResponse.toString());
            if (cardValidateAuthenticationAndOrderResponse != null) {
                Toast.makeText(getApplicationContext(),
                        ((cardValidateAuthenticationAndOrderResponse.getInAppResponseCode().equals("00")) ?
                                ("Accepted: " + cardValidateAuthenticationAndOrderResponse.getCaptureMode() + " Auth ID: " + cardValidateAuthenticationAndOrderResponse.getAuthorisationId())
                                :
                                (cardValidateAuthenticationAndOrderResponse.getErrorFieldName()
                                        + " " +
                                        cardValidateAuthenticationAndOrderResponse.getInAppResponseCode().toString()))
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, 5);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                cardNbr.setText(scanResult.cardNumber);
                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
            System.out.println("--------------" + resultDisplayStr);
        }
        // else handle other activity results
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

}
