package com.aga.myapplication;

import android.content.Context;

import com.wl.sips.inapp.sdk.PaymentManager;
import com.wl.sips.inapp.sdk.exception.NetworkException;
import com.wl.sips.inapp.sdk.exception.TechnicalException;
import com.wl.sips.inapp.sdk.pojo.AddCardResponse;
import com.wl.sips.inapp.sdk.pojo.CardCheckEnrollmentResponse;
import com.wl.sips.inapp.sdk.pojo.GetTransactionDataResponse;
import com.wl.sips.inapp.sdk.pojo.GetWalletDataResponse;
import com.wl.sips.inapp.sdk.pojo.OrderResponse;
import com.wl.sips.inapp.sdk.pojo.PaymentProviderResponse;

import java.net.URL;
import java.net.URLEncoder;

import static com.aga.myapplication.R.id.cardNbr;

/**
 * Created by A599885 on 3/08/2017.
 */

public class Payment {

    public static PaymentProviderResponse paymentProviderOrder(Context context, RedirectionResponse redirectionResponse)
    {
        PaymentProviderResponse paymentProviderResponse = null;
        PaymentManager paymentManager =  new PaymentManager();
        try {
            paymentProviderResponse = paymentManager.paymentProviderOrder(context, redirectionResponse.getPublicKeyValue(),
                    redirectionResponse.getRedirectionData(), redirectionResponse.getRedirectionVersion(), redirectionResponse.getRedirectionUrl());
            System.out.println("paymentProviderOrder sent");
            System.out.println(paymentProviderResponse.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return paymentProviderResponse;
    }

    public static CardCheckEnrollmentResponse cardCheckEnrollment(Context context, RedirectionResponse redirectionResponse, String cardNbr, String csv, String expiryDate)
    {
        CardCheckEnrollmentResponse cardCheckEnrollmentResponse = null;
        PaymentManager paymentManager =  new PaymentManager();
        try {
            cardCheckEnrollmentResponse = paymentManager.cardCheckEnrollment(context, cardNbr, csv, expiryDate,
                    redirectionResponse.getPublicKeyValue(), redirectionResponse.getRedirectionData(),
                    redirectionResponse.getRedirectionUrl(), redirectionResponse.getRedirectionVersion());
            System.out.println("cardCheckEnrollment sent");
            System.out.println(cardCheckEnrollmentResponse.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return cardCheckEnrollmentResponse;
    }

    public static OrderResponse cardValidateAuthenticationAndOrder(Context context, String paResMessage, CardCheckEnrollmentResponse cardCheckEnrollmentResponse)
    {
        OrderResponse orderResponse = null;
        PaymentManager paymentManager =  new PaymentManager();
        try {
            orderResponse = paymentManager.cardValidateAuthenticationAndOrder(context, paResMessage, cardCheckEnrollmentResponse.getTransactionContextData()
            , cardCheckEnrollmentResponse.getRedirectionUrl(), cardCheckEnrollmentResponse.getTransactionContextVersion());
            System.out.println("cardValidateAuthenticationAndOrder sent");
            System.out.println(orderResponse.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return orderResponse;
    }

    public static OrderResponse cardOrder(Context context, String cardNbr, String csv, String expiryDate, RedirectionResponse redirectionResponse)
    {
        PaymentManager paymentManager =  new PaymentManager();
        OrderResponse ord = null;
        try {
            ord = paymentManager.cardOrder(context, cardNbr, csv, expiryDate, redirectionResponse.getPublicKeyValue(),
                    redirectionResponse.getRedirectionData(), redirectionResponse.getRedirectionUrl(), redirectionResponse.getRedirectionVersion());
            System.out.println("cardOrder sent");
            System.out.println(ord.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return ord;
    }

    public static GetTransactionDataResponse getTransactionData(Context context, PaymentProviderResponse paymentProviderResponse)
    {
        PaymentManager paymentManager =  new PaymentManager();
        GetTransactionDataResponse getTransactionDataResponse = null;
        try {
            getTransactionDataResponse = paymentManager.getTransactionData(context, paymentProviderResponse.getTransactionContextData(),
                    paymentProviderResponse.getTransactionContextVersion(), paymentProviderResponse.getRedirectionUrl());
            System.out.println("getTransactionDataResponse sent");
            System.out.println(getTransactionDataResponse.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        return getTransactionDataResponse;
    }

    public static AddCardResponse addCard(Context context, String cardNumber, String paymentMeanAlias, String
            cardExpiryDate, RedirectionResponse redirectionResponse)
    {
        PaymentManager paymentManager =  new PaymentManager();
        AddCardResponse addCardResponse = null;
        try {
            addCardResponse = paymentManager.addCard(context, cardNumber, paymentMeanAlias, cardExpiryDate, redirectionResponse.getPublicKeyValue(),
                    redirectionResponse.getRedirectionData(), redirectionResponse.getRedirectionUrl(), redirectionResponse.getRedirectionVersion());
            System.out.println("addCard sent");
            System.out.println(addCardResponse.toString());
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return addCardResponse;
    }

    public static OrderResponse walletOrder(Context context, RedirectionResponse redirectionResponse
            , int paymentMeanID)
    {
        PaymentManager paymentManager = new PaymentManager();
        OrderResponse orderResponse = null;
        try {
            orderResponse = paymentManager.walletOrder( paymentMeanID ,redirectionResponse.getPublicKeyValue(), redirectionResponse.getRedirectionData()
            , redirectionResponse.getRedirectionUrl(), redirectionResponse.getRedirectionVersion(), context);
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return orderResponse;
    }

    public static GetWalletDataResponse getWalletDataResponseOrder(Context context, RedirectionResponse redirectionResponse)
    {
        PaymentManager paymentManager = new PaymentManager();
        GetWalletDataResponse getWalletDataResponse = null;
        try {
            getWalletDataResponse = paymentManager.getWalletData(redirectionResponse.getPublicKeyValue(),
                    redirectionResponse.getRedirectionData(), redirectionResponse.getRedirectionUrl(),
                    redirectionResponse.getRedirectionVersion(), context);
        } catch (TechnicalException e) {
            e.printStackTrace();
        } catch (NetworkException e) {
            e.printStackTrace();
        }
        return getWalletDataResponse;
    }
}
