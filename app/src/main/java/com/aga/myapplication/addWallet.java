package com.aga.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wl.sips.inapp.sdk.pojo.AddCardResponse;
import com.wl.sips.inapp.sdk.pojo.OrderResponse;

import static com.aga.myapplication.MainActivity.imei;
import static com.aga.myapplication.R.id.crdNbr;

public class addWallet extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_READ_PHONE_STATE = 1;

    RedirectionResponse redirectionResponse=null;
    AddCardResponse addCardResponse=null;
    OrderResponse orderResponse = null;
    Button addWl;
    EditText cardNumber ;
    EditText cardAlias;
    EditText cardCsc;
    EditText cardExpirayDate;
    //EditText walletId;
    TextView wlRes;
    Spinner spinner;
    String[] payBrands = {"MASTERCARD", "VISA"};
    String PaymentBrand = "MASTERCARD";
    ImageView payBrandView;

    //String imei = "7128923";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        spinner = (Spinner) findViewById(R.id.payMeans);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, payBrands);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        payBrandView = (ImageView) findViewById(R.id.brandPic);

        System.out.println("1");
        addWl = (Button) findViewById(R.id.WlBtn);
        System.out.println(addWl);
        cardNumber = (EditText) findViewById(R.id.cardNbr);
        System.out.println("1");
        cardAlias = (EditText) findViewById(R.id.crdAlias);
        cardCsc = (EditText) findViewById(R.id.crdCsc);
        System.out.println("1");
        cardExpirayDate = (EditText) findViewById(R.id.expiryDate);
        System.out.println("1");
        //walletId = (EditText) findViewById(R.id.WlId);
        System.out.println(MainActivity.imei);
        wlRes = (TextView) findViewById(R.id.addWlResult);
        System.out.println("1");

        addWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click");
                AsyncTsk asyncT = new AsyncTsk();
                asyncT.execute();
            }
        });
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

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PaymentBrand = payBrands[position];
        System.out.println("swdw------------------- " + PaymentBrand);
        switch (position)
        {
            case 0: payBrandView.setImageResource(R.drawable.mastercard);
                break;
            case 1: payBrandView.setImageResource(R.drawable.visa);//ContextCompat.getDrawable(getApplicationContext(), R.drawable.visa));
                break;
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncTsk extends AsyncTask<Void, Void, Void>
    {
        String wlId = MainActivity.imei;
        String crdNbr = cardNumber.getText().toString();
        String crdAlias = cardAlias.getText().toString();
        String crdCsc = cardCsc.getText().toString();
        String cardespDt = cardExpirayDate.getText().toString();
        int status = 0;

        @Override
        protected Void doInBackground(Void... params) {
            /* First we need to do a 2 euros wallet order with the card data before adding it to the
             * wallet.
             * If the Tx passes, then add the card, otherwise, abort
             */
            // creating  cardOrder
            if(sendCardOrder() == 0)
            {
                status = 1;
                // create walletInitialze order
                sendWalletAddCardOrder();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o)
        {
            if(addCardResponse != null) {
                wlRes.setText("Response Code: " + addCardResponse.getInAppResponseCode() + "\nPayment mean ID :" + addCardResponse.getPaymentMeanId());
            }
            switch (status)
            {
                case 0:
                    Toast.makeText(getApplicationContext(), "invalide card Data", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "Erorr Adding card to the wallet", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Card successfully added to your wallet", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        private int sendCardOrder()
        {
            redirectionResponse = NW.post_BC("2", PaymentBrand, "CARDORDER");
            if(redirectionResponse!=null && redirectionResponse.getRedirectionStatusCode() == 0) {
                System.out.println(crdNbr);
                System.out.println(crdAlias);
                System.out.println(cardespDt);
                // call cardorder API
                orderResponse = Payment.cardOrder(getApplicationContext(), crdNbr, crdCsc, cardespDt, redirectionResponse);
                //System.out.println(orderResponse.toString());
                return Integer.valueOf(orderResponse.getInAppResponseCode());
            }

            return 1;
        }

        private void sendWalletAddCardOrder()
        {
            redirectionResponse = NW.post_WL(wlId, "ADDCARD");
            if(redirectionResponse!=null && redirectionResponse.getRedirectionStatusCode() == 0) {
                System.out.println(crdNbr);
                System.out.println(crdAlias);
                System.out.println(cardespDt);
                // call cardorder API
                addCardResponse = Payment.addCard(getApplicationContext(), crdNbr, crdAlias, cardespDt, redirectionResponse);

                System.out.println(addCardResponse.toString());
               if(Integer.valueOf(addCardResponse.getInAppResponseCode()) == 0)
                   status = 2;
            }
           // return 1;
        }
    }

}
