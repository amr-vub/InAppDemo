package com.aga.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

public class AndroidPay extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private SupportWalletFragment mWalletFragment;
    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";
    private MaskedWallet mMaskedWallet;
    private GoogleApiClient mGoogleApiClient;
    public static final int FULL_WALLET_REQUEST_CODE = 889;
    private FullWallet mFullWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Check if WalletFragment exists
        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);

        // It does not, add it
        if(mWalletFragment == null) {
            System.out.println("innnnnnnnnnnnnnnnnnnnnnn");
            // Wallet fragment style
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(BuyButtonText.BUY_NOW)
                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);

            // Wallet fragment options
            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();


            // Instantiate the WalletFragment
            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

            // Initialize the WalletFragment
            WalletFragmentInitParams.Builder startParamsBuilder =
                    WalletFragmentInitParams.newBuilder()
                            .setMaskedWalletRequest(generateMaskedWalletRequest())
                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
                            .setAccountName("Google I/O Codelab");
            mWalletFragment.initialize(startParamsBuilder.build());

            // Add the fragment to the UI
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                    .commit();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();

        IsReadyToPayRequest isReadyToPayRequest = IsReadyToPayRequest.newBuilder().build();

        Wallet.Payments.isReadyToPay(mGoogleApiClient, isReadyToPayRequest).setResultCallback(
                new ResultCallback<BooleanResult>() {
            @Override
            public void onResult(@NonNull BooleanResult booleanResult) {
                //hideProgressDialog();
                if (booleanResult.getStatus().isSuccess()) {
                    if (booleanResult.getValue()) {
                        // Show Android Pay buttons alongside regular checkout button
                        // ...
                        Toast.makeText(getApplicationContext(), "booleanResult.getValue()" + booleanResult.getValue(), Toast.LENGTH_SHORT).show();

                    } else {
                        // Hide Android Pay buttons, show a message that Android Pay
                        // cannot be used yet, and display a traditional checkout button
                        // ...
                        Toast.makeText(getApplicationContext(), "EROR:booleanResult.getValue()" + booleanResult.getValue(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error making isReadyToPay call
                    System.out.println("isReadyToPay:" + booleanResult.getStatus());
                }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MASKED_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mMaskedWallet = data
                                .getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        Toast.makeText(this, "Got Masked Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user canceled the operation
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mFullWallet = data
                                .getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        // Show the credit card number
                        Toast.makeText(this,
                                mFullWallet.getProxyCard().getPan().toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
        int errorCode ;//= -1;
        if (data != null) {
            errorCode = data.getIntExtra(
                    WalletConstants.EXTRA_ERROR_CODE, -1);
            handleError(errorCode);
        }
    }
    private void handleError(int errorCode) {
        switch (errorCode) {
            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
                Toast.makeText(this, "An ERROR_CODE_INVALID_PARAMETERS Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
                Toast.makeText(this, "An ERROR_CODE_AUTHENTICATION_FAILURE Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
                Toast.makeText(this, "An ERROR_CODE_BUYER_ACCOUNT_ERROR Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
                Toast.makeText(this, "An ERROR_CODE_MERCHANT_ACCOUNT_ERROR Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
                Toast.makeText(this, "An ERROR_CODE_SERVICE_UNAVAILABLE Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
                Toast.makeText(this, "An ERROR_CODE_UNSUPPORTED_API_VERSION Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            case WalletConstants.ERROR_CODE_UNKNOWN:
                Toast.makeText(this, "An ERROR_CODE_UNKNOWN Occurred" + errorCode, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "An default Occurred " + errorCode, Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private MaskedWalletRequest generateMaskedWalletRequest(){
        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName("Google I/O Codelab")
                        .setPhoneNumberRequired(true)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode("USD")
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice("10.00")
                                .addLineItem(LineItem.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setDescription("Google I/O Sticker")
                                        .setQuantity("1")
                                        .setUnitPrice("10.00")
                                        .setTotalPrice("10.00")
                                        .build())
                                .build())
                        .setEstimatedTotalPrice("15.00")
                        .build();
        return maskedWalletRequest;
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Google I/O Sticker")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice(".10")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }

    public void requestFullWallet(View view) {
        if (mMaskedWallet == null) {
            Toast.makeText(this, "No masked wallet, can't confirm", Toast.LENGTH_SHORT).show();
            return;
        }
        Wallet.Payments.loadFullWallet(mGoogleApiClient,
                generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                FULL_WALLET_REQUEST_CODE);
    }

    public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(
            String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }
    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // GoogleApiClient is connected, we don't need to do anything here
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // GoogleApiClient is temporarily disconnected, no action needed
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // GoogleApiClient failed to connect, we should log the error and retry
    }
}
