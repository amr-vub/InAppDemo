package com.aga.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wl.sips.inapp.sdk.pojo.AddCardResponse;
import com.wl.sips.inapp.sdk.pojo.GetTransactionDataResponse;
import com.wl.sips.inapp.sdk.pojo.PaymentProviderResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    TextView amount;
    TextView textView;
    RedirectionResponse redirectionResponse;
    PaymentProviderResponse paymentProviderResponse = null;
    GetTransactionDataResponse getTransactionDataResponse;
    AddCardResponse addCardResponse;
    static String imei = "713438731";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            System.out.println("---------IF----------------------");
            // TODO
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            System.out.println("---------ELSE----------------------");
            TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            if(telephonyManager!=null) {
                System.out.println("-------------------------------");
                imei = telephonyManager.getDeviceId();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.textView);

        //PaymentManager

        Button payButton = (Button) findViewById(R.id.bcBtn);
        Button WlOrder = (Button) findViewById(R.id.WlPay);
        Button btn3ds = (Button) findViewById(R.id.btn3ds);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BCActivity.class));
            }
        });

        Button WLBtn = (Button) findViewById(R.id.WLBtn);

        WLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addWallet.class));
                //startActivity();
            }
        });

        WlOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WLOrderActivity.class));
            }
        });
/*
        Button androidPay = (Button) findViewById(R.id.andPay);
        androidPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendJSONreq();
                Intent intent = new Intent(MainActivity.this, AndroidPay.class);
                EditText editText = (EditText) findViewById(R.id.editText);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                //startActivity();
            }
        });
        */

        btn3ds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ThreeDS.class));
            }
        });
        //amount.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (paymentProviderResponse != null) {
            SecondAsyncTask secondAsyncTask = new SecondAsyncTask();
            secondAsyncTask.execute();
        }
    }

    public void sendreq() {
        String charset = "UTF-8";
        String query = "";
        try {
            query = String.format("redirectionData=%s&redirectionVersion=%s",
                    URLEncoder.encode(redirectionResponse.getRedirectionData(), charset),
                    URLEncoder.encode(redirectionResponse.getRedirectionVersion(), charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.postUrl(redirectionResponse.getRedirectionUrl(), query.getBytes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    private class SecondAsyncTask extends AsyncTask<Void, Void, Void> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {

            getTransactionDataResponse = Payment.getTransactionData(getApplicationContext(), paymentProviderResponse);

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            //textView.setText(getTransactionDataResponse.toString());
            Toast.makeText(getApplicationContext(),"Response code: " + getTransactionDataResponse.getInAppResponseCode() + " \nStatusMessage: " + getTransactionDataResponse.getErrorFieldName()
                    , Toast.LENGTH_SHORT).show();
        }
    }
}

