package com.ravan.paytmintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.ravan.paytmintegration.checksum_constant.Checksum;
import com.ravan.paytmintegration.checksum_constant.Constants;
import com.ravan.paytmintegration.checksum_constant.Helpers;
import com.ravan.paytmintegration.retrofit.ApiClient;
import com.ravan.paytmintegration.retrofit.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback{

    private ApiInterface apiInterface;
    Button btnPay;
    String custId="", orderId= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderId = Helpers.generateRandomString();
        custId = Helpers.generateRandomString();

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        init();
        btnPay();
    }


    private void init() {
        btnPay = findViewById(R.id.btnPay);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},101);

        }
    }

    private void btnPay() {

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChecksum();
            }
        });
    }

    private void generateChecksum(){
        Call<Checksum> call = apiInterface.getChecksum(
                Constants.M_ID,
                orderId,
                custId,
                Constants.CHANNEL_ID,
                "100000",
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );


    call.enqueue(new Callback<Checksum>() {
        @Override
        public void onResponse(Call<Checksum> call, Response<Checksum> response) {
            Checksum checksum = response.body();
            PaytmPGService pgService = PaytmPGService.getStagingService();


            HashMap<String,String> paramMap = new HashMap<>();

            paramMap.put("MID",Constants.M_ID);
            paramMap.put("ORDER_ID",orderId);
            paramMap.put("CUST_ID",custId);
            paramMap.put("CHANNEL_ID",Constants.CHANNEL_ID);
            paramMap.put("TXN_AMOUNT","100000");
            paramMap.put("WEBSITE",Constants.WEBSITE);
            paramMap.put("CALLBACK_URL",Constants.CALLBACK_URL);
            paramMap.put("CHECKSUMHASH",checksum.getChecksumHash());
            paramMap.put("INDUSTRY_TYPE_ID","Retail");


            PaytmOrder order = new PaytmOrder(paramMap);
            pgService.initialize(order,null);

            pgService.startPaymentTransaction(MainActivity.this,true,true,MainActivity.this);

        }

        @Override
        public void onFailure(Call<Checksum> call, Throwable t) {
            Toast.makeText(MainActivity.this, ""+call.toString(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {

    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {

    }

    @Override
    public void onBackPressedCancelTransaction() {

    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

    }


}
