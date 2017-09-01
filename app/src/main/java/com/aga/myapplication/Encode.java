package com.aga.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.aga.myapplication.R.id.amount;
import static com.wl.sips.inapp.sdk.enums.SdkOperation.WALLETORDER;

/**
 * Created by A599885 on 3/08/2017.
 *
 * This class provide an implementation for the JSON request encoding for diff
 * Payment requests, e.g. order initialize, wallet order etc...
 */
public class Encode {

    static String secretKey = "" ;
    static String merchantId = "" ;
    static Calendar c = Calendar.getInstance();

    static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
    static String formattedDate = df.format(c.getTime());

    public static JSONObject encodeInitializeOrder(String amount, String paymentMeanBrand, String sdkOperationName )
    {
        JSONObject  jsonObject= new JSONObject();
        String interfaceVersionInApp = "IR_MB_1.3";
        //String interfaceVersion = "IR_WS_2.10";
        String txRef = String.valueOf((int)Math.floor(Math.random() * 24456) );

        //String S10Txref = "{s10TransactionId:1234, s10TransactionIdDate:20170802}";
        JSONObject jsonObjTxRef = new JSONObject();
        //String normalReturnUrl =  "http://localhost/www/public/Start.php";
        try {
            jsonObjTxRef.put("s10TransactionId", txRef);
            jsonObjTxRef.put("s10TransactionIdDate", formattedDate);

            jsonObject.put("amount", amount);
            jsonObject.put("captureMode", "VALIDATION");
            jsonObject.put("currencyCode", "978");
            jsonObject.put("interfaceVersion", interfaceVersionInApp);
            jsonObject.put("keyVersion", "1");
            jsonObject.put("merchantId", merchantId);
            //jsonObject.put("normalReturnUrl", normalReturnUrl);
            //jsonObject.put("orderChannel", "INTERNET");
            jsonObject.put("paymentMeanBrand", paymentMeanBrand);
            //jsonObject.put("paymentPattern", "ONE_SHOT");
            jsonObject.put("s10TransactionReference", jsonObjTxRef );
            // jsonObject.put("s10TransactionId", TxId);
            //jsonObject.put("s10TransactionIdDate", "20170802");
            jsonObject.put("sdkOperationName", sdkOperationName);
            jsonObject.put("sdkVersion", "SDK100");
            //jsonObject.put("templateName", "marc");
            //jsonObject.put("transactionReference", txRef);


            //System.out.println(Math.floor(Math.random() * 244532656));

            String dataIn = amount + "VALIDATION" + "978"+ interfaceVersionInApp +merchantId+ paymentMeanBrand + txRef + formattedDate + sdkOperationName +"SDK100";//+txRef;

            jsonObject.put("seal", ExampleHMAC.computeSeal(dataIn, secretKey));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static JSONObject encodeWalletOrder(String amount, String wlID)
    {
        JSONObject  jsonObject= new JSONObject();
        String interfaceVersionInApp = "IR_MB_1.3";
        //String interfaceVersion = "IR_WS_2.10";
        String txRef = String.valueOf((int)Math.floor(Math.random() * 24456) );

        //String S10Txref = "{s10TransactionId:1234, s10TransactionIdDate:20170802}";
        JSONObject jsonObjTxRef = new JSONObject();
        JSONObject jsonObjfraudData = new JSONObject();
        //String normalReturnUrl =  "http://localhost/www/public/Start.php";
        try {
            jsonObjTxRef.put("s10TransactionId", txRef);
            jsonObjTxRef.put("s10TransactionIdDate", formattedDate);

            jsonObjfraudData.put("bypass3DS", "MERCHANTWALLET");

            jsonObject.put("amount", amount);
            jsonObject.put("captureMode", "VALIDATION");
            jsonObject.put("currencyCode", "978");
            //jsonObject.put("fraudData", jsonObjfraudData);
            jsonObject.put("interfaceVersion", interfaceVersionInApp);
            jsonObject.put("keyVersion", "1");
            jsonObject.put("merchantId", merchantId);
            jsonObject.put("merchantWalletId", wlID);
            //jsonObject.put("normalReturnUrl", normalReturnUrl);
            //jsonObject.put("orderChannel", "INTERNET");
            jsonObject.put("paymentMeanBrand", "MASTERCARD");
            //jsonObject.put("paymentPattern", "ONE_SHOT");
            jsonObject.put("s10TransactionReference", jsonObjTxRef );
            // jsonObject.put("s10TransactionId", TxId);
            //jsonObject.put("s10TransactionIdDate", "20170802");
            jsonObject.put("sdkOperationName", "WALLETORDER");
            jsonObject.put("sdkVersion", "SDK100");
            //jsonObject.put("templateName", "marc");
            ///jsonObject.put("transactionReference", txRef);


            //System.out.println(Math.floor(Math.random() * 244532656));

            String dataIn = amount + "VALIDATION" + "978"+ /*"MERCHANTWALLET" +*/ interfaceVersionInApp +merchantId+ wlID + "MASTERCARD" + txRef + formattedDate + "WALLETORDER" +"SDK100";//+txRef;//"BCMCMOBILE" +

            jsonObject.put("seal", ExampleHMAC.computeSeal(dataIn, secretKey));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject encodeWalletInitiate(String wlID, String sdkOpName)
    {
        JSONObject  jsonObject= new JSONObject();
        String interfaceVersionInApp = "IR_MB_1.3";
        //String interfaceVersion = "IR_WS_2.10";
        String txRef = String.valueOf((int)Math.floor(Math.random() * 24456) );

        System.out.println(formattedDate);

        //String S10Txref = "{s10TransactionId:1234, s10TransactionIdDate:20170802}";
        //JSONObject jsonObjTxRef = new JSONObject();
        //String normalReturnUrl =  "http://localhost/www/public/Start.php";
        try {
            //jsonObjTxRef.put("s10TransactionId", txRef);
            //jsonObjTxRef.put("s10TransactionIdDate", "20170802");

            //jsonObject.put("amount", amount);
            //jsonObject.put("currencyCode", "978");
            jsonObject.put("interfaceVersion", interfaceVersionInApp);
            jsonObject.put("keyVersion", "1");
            jsonObject.put("merchantId", merchantId);
            jsonObject.put("merchantWalletId", wlID);
            //jsonObject.put("orderChannel", "INTERNET");
            //jsonObject.put("paymentMeanBrand", "BCMCMOBILE");
            //jsonObject.put("paymentPattern", "ONE_SHOT");
            //jsonObject.put("s10TransactionReference", jsonObjTxRef );
            // jsonObject.put("s10TransactionId", TxId);
            //jsonObject.put("s10TransactionIdDate", "20170802");
            jsonObject.put("sdkOperationName", sdkOpName);
            jsonObject.put("sdkVersion", "SDK100");
            //jsonObject.put("templateName", "marc");
            //jsonObject.put("transactionReference", txRef);


            //System.out.println(Math.floor(Math.random() * 244532656));

            String dataIn = interfaceVersionInApp +merchantId+ wlID + sdkOpName + "SDK100";//+txRef;

            jsonObject.put("seal", ExampleHMAC.computeSeal(dataIn, secretKey));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
