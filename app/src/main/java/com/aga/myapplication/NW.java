package com.aga.myapplication;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.sql.DriverManager.println;

/**
 * Created by A599885 on 3/08/2017.
 * <p>
 * Provide the network requests
 */

public class NW {

    static HttpURLConnection httpURLConnection;
    static Gson gson = new Gson();

    public static RedirectionResponse post_BC(String amount, String paymentMeanBrand, String sdkOperationName) {
        //mainActivity = new MainActivity();
        //String jsonREqURL = "https://payment-webinit.sips-atos.com/rs-services/v2/paymentInit";
        String orderInitURL = "https://office-server.sips-atos.com/rs-services/v2/checkoutInApp/orderInitialize"; // in-App
        RedirectionResponse redirectionResponse;
        String text = "";
        try {
            URL url = new URL(orderInitURL); //Enter URL here // https://payment-webinit.sips-atos.com/rs-services/v2/paymentInit
            //https://www.w3schools.com/angular/customers.php
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            //httpURLConnection.setInstanceFollowRedirects(true);
            System.out.println("setDoOutput");
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            System.out.println("setRequestMethod");
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`

            System.out.println("setRequestProperty");
            HttpURLConnection.setFollowRedirects(true);

            JSONObject jsonObject;

            switch (sdkOperationName)
            {
                case "WALLETORDER":
                    jsonObject = Encode.encodeWalletOrder(amount, MainActivity.imei);
                    break;
                default:
                    jsonObject = Encode.encodeInitializeOrder(amount, paymentMeanBrand,sdkOperationName);
            }

            System.out.println(jsonObject.toString());


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            httpURLConnection.connect();

            System.out.println("connect");
            wr.flush();
            wr.close();
            int status = httpURLConnection.getResponseCode();
            System.out.println(status);
            InputStream inputStream;
//if
            if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_ACCEPTED) {
                System.out.println("getErrorStream");
                inputStream = httpURLConnection.getErrorStream();
            } else {
                System.out.println("getInputStream");
                inputStream = httpURLConnection.getInputStream();
            }

            System.out.println("BufferedReader");
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String vl;

            JSONObject js = new JSONObject();
            System.out.println("while");
            while ((vl = bf.readLine()) != null) {
                System.out.println(vl);
                text += vl;
            } /**/
            bf.close();

            redirectionResponse = gson.fromJson(text, RedirectionResponse.class);
            System.out.println("gson");
            return redirectionResponse;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            System.out.println("MalformedURLException");
        } catch (IOException e)

        {
            e.printStackTrace();
            System.out.println("IOException ");
        } finally
        {
            httpURLConnection.disconnect();
        }
        //send2ndPost();
        //httpURLConnection.
        //cardOrder();
        return null;
    }

    public static RedirectionResponse post_WL(String wlID, String sdkOpName) {
        //mainActivity = new MainActivity();
        //String jsonREqURL = "https://payment-webinit.sips-atos.com/rs-services/v2/paymentInit";
        String InitURL = "https://office-server.sips-atos.com/rs-services/v2/walletInApp/walletInitialize"; // in-App
        RedirectionResponse redirectionResponse;
        String text = "";
        try {
            URL url = new URL(InitURL); //Enter URL here // https://payment-webinit.sips-atos.com/rs-services/v2/paymentInit
            //https://www.w3schools.com/angular/customers.php
            httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            //httpURLConnection.setInstanceFollowRedirects(true);
            System.out.println("setDoOutput");
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            System.out.println("setRequestMethod");
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`

            System.out.println("setRequestProperty");
            HttpURLConnection.setFollowRedirects(true);


            JSONObject jsonObject = Encode.encodeWalletInitiate(wlID, sdkOpName);
            System.out.println(jsonObject.toString());


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            httpURLConnection.connect();

            System.out.println("connect");
            wr.flush();
            wr.close();
            int status = httpURLConnection.getResponseCode();
            System.out.println(status);
            InputStream inputStream;
//if
            if (status != HttpURLConnection.HTTP_OK && status != HttpURLConnection.HTTP_ACCEPTED) {
                System.out.println("getErrorStream");
                inputStream = httpURLConnection.getErrorStream();
            } else {
                System.out.println("getInputStream");
                inputStream = httpURLConnection.getInputStream();
            }

            System.out.println("BufferedReader");
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String vl;

            JSONObject js = new JSONObject();
            System.out.println("while");
            while ((vl = bf.readLine()) != null) {
                System.out.println(vl);
                text += vl;
            } /**/
            bf.close();

            redirectionResponse = gson.fromJson(text, RedirectionResponse.class);
            System.out.println("gson");
            return redirectionResponse;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            System.out.println("MalformedURLException");
        } catch (IOException e)

        {
            e.printStackTrace();
            System.out.println("IOException ");
        } finally
        {
            httpURLConnection.disconnect();
        }
        //send2ndPost();
        //httpURLConnection.
        //cardOrder();
        return null;
    }
}
