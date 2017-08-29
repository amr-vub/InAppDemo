package com.aga.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wl.sips.inapp.sdk.pojo.GetWalletDataResponse;
import com.wl.sips.inapp.sdk.pojo.OrderResponse;
import com.wl.sips.inapp.sdk.pojo.WalletPaymentMeanData;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.aga.myapplication.R.id.crdAlias;
import static com.aga.myapplication.R.id.fab;

public class WLOrderActivity extends AppCompatActivity {

    Button WLPayBtn;
    //EditText walletId;
    EditText amount;
    TextView res;
    ImageView brand;
    TextView maskedPAN;
    ListView listView;
    TableLayout tableLayout;
    RedirectionResponse redirectionResponse=null;
    OrderResponse orderResponse = null;
    GetWalletDataResponse getWalletDataResponse = null;
    ArrayList<WalletPaymentMeanData> walletPaymentMeanDataList = null;
    ArrayList<CheckBox> checkBoxList = null;
    int paymeantMeanID=1;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(walletPaymentMeanDataList == null) {
            new AsyncTsk_walletData().execute();
        }
        else
        {
            for(ListIterator<WalletPaymentMeanData> it = walletPaymentMeanDataList.listIterator();
                it.hasNext(); )
            {
                drawBrands(it.next());
            }
        }

        //listView = (ListView) findViewById(R.id.list);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        WLPayBtn = (Button) findViewById(R.id.WlPayBtn);
        //walletId = (EditText) findViewById(R.id.WLId);
        amount = (EditText) findViewById(R.id.amount);
        res = (TextView) findViewById(R.id.res);
        //brand = (ImageView) findViewById(R.id.brand);
        //maskedPAN = (TextView)  findViewById(R.id.maskedPAN);
        /*
        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageDarkened(brand))
                {
                    undarkenImage(brand);
                }
                else
                    darkenImage(brand);
            }
        });*/

        WLPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicsdsk");
                System.out.println("Clicsdsk");
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

    private class AsyncTsk extends AsyncTask<Void, Void, Void>
    {
        String wlId = MainActivity.imei;//walletId.getText().toString();
        String amnt = amount.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {
            // creat orderInitialze order
            redirectionResponse = NW.post_BC(amnt, "MASTERCARD", "WALLETORDER");
            if(redirectionResponse!=null && redirectionResponse.getRedirectionStatusCode() == 0) {
                System.out.println(wlId);
                System.out.println(amnt);
                // call cardorder API
                orderResponse = Payment.walletOrder(getApplicationContext(), redirectionResponse, paymeantMeanID);
                System.out.println("paymeantMeanID: ============= " + paymeantMeanID);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o)
        {
            if(orderResponse != null) {
                System.out.println(orderResponse);
                Toast.makeText(getApplicationContext(), "Response Code: " + orderResponse.getInAppResponseCode() +
                        (orderResponse.getErrorFieldName() == null ? "": ("\nError:" + orderResponse.getErrorFieldName()) ) , Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class AsyncTsk_walletData extends AsyncTask<Void, Void, Void>
    {
        String wlId = MainActivity.imei;//walletId.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {
            // creat orderInitialze order
            redirectionResponse = NW.post_WL(wlId, "GETWALLETDATA");
            if(redirectionResponse!=null && redirectionResponse.getRedirectionStatusCode() == 0) {
                System.out.println(wlId);
                // call cardorder API
                getWalletDataResponse = Payment.getWalletDataResponseOrder(getApplicationContext(), redirectionResponse);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void o)
        {
            ArrayList<String> maskedPANList = new ArrayList<String>();

            if(getWalletDataResponse != null && getWalletDataResponse.getInAppResponseCode().equals("00")) {
                //maskedPAN.setText(getWalletDataResponse.getWalletPaymentMeanData().get(0).getMaskedPan());
                walletPaymentMeanDataList = new ArrayList<>(getWalletDataResponse.getWalletPaymentMeanData());
                checkBoxList = new ArrayList<>();
                for(ListIterator<WalletPaymentMeanData> it = getWalletDataResponse.getWalletPaymentMeanData().listIterator();
                    it.hasNext(); )
                {
                    drawBrands(it.next());
                }
            }
        }
    }

    private void drawBrands(WalletPaymentMeanData walletPaymentMeanData)
    {
        TableRow tr = new TableRow(getApplicationContext());
        tr.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        TextView tx = new TextView(getApplicationContext());
        //TextView tx2 = new TextView(getApplicationContext());

        CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setId(walletPaymentMeanData.getPaymentMeanId());
        checkBoxList.add(checkBox);
        // add action listenr to update the selected maskedPAN
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    paymeantMeanID = buttonView.getId();
                    for(ListIterator<CheckBox> it = checkBoxList.listIterator();
                        it.hasNext(); )
                    {
                        CheckBox checkBox1 = it.next();
                        if(checkBox1.getId() !=  buttonView.getId())
                        {
                            checkBox1.setChecked(false);
                        }
                    }
                }
            }
        });
        tr.addView(checkBox);

        // adding listner to selections

        ImageView imageView = new ImageView(getApplicationContext());
        //imageView.setPadding(50, 50, 50, 50);
        imageView.setBackgroundResource(getImageForBrand(walletPaymentMeanData.getPaymentMeanBrand()));
        tr.addView(imageView);

        //card aliase
        /*
        tx2.setText(walletPaymentMeanData.getPaymentMeanAlias());
        tx2.setTextColor(Color.parseColor("#000000"));
        tx2.setPadding(10, 0, 0, 0);
        tr.addView(tx2);
        */
        // masked pan
        tx.setText(walletPaymentMeanData.getMaskedPan());
        tx.setTextColor(Color.parseColor("#000000"));
        tx.setPadding(10, 0, 0, 0);
        tr.addView(tx);

        tableLayout.addView(tr);
    }
    private int getImageForBrand(String brand)
    {
        if(brand.equals("MASTERCARD"))
            return R.drawable.tt;
        else
            return R.drawable.vv;
    }
    private void darkenImage(ImageView imageView)
    {
        imageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void undarkenImage(ImageView imageView)
    {
        imageView.clearColorFilter();
    }

    private boolean isImageDarkened(ImageView image)
    {
        return image.getColorFilter() != null;
    }
}
