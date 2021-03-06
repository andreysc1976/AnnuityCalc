package ru.a_party.annuitycalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private double creditSumm;
    private int timeMonth;
    private double rate;
    private double monthPay;

    private AdView mAdView;

    private TextInputEditText editTextCreditSumm;
    private TextInputEditText editTextTimeMonth;
    private TextInputEditText editTextRate;
    private TextInputEditText editTextMonthPay;
    private TextInputEditText editTextOversize;

    private TextView hintTextView;
    private CardView cardViewHint;


    private double annuityK(double internalRate,int internalTimeMonth){
        if (internalTimeMonth<=0){
            return 0;
        }
        return (internalRate*Math.pow(1f+internalRate,internalTimeMonth))/(Math.pow(1f+internalRate,internalTimeMonth)-1f);
    }

    private void setOversizeValue()
    {
        double oversizeValue = monthPay*timeMonth - creditSumm;
        if (Double.isNaN(oversizeValue)){oversizeValue=0.00;}
        if (oversizeValue<0){oversizeValue=0.00;}
        editTextOversize.setText(String.format("%.2f", oversizeValue));
    }

    private void readValue(){

        try {
            creditSumm = Double.parseDouble(editTextCreditSumm.getText().toString());
        } catch (NumberFormatException ex){
            creditSumm=0;
        }


        try {
            timeMonth = Integer.parseInt(editTextTimeMonth.getText().toString());
        } catch (NumberFormatException ex)
        {
            timeMonth=0;
        }


        try {
            String stringRate = editTextRate.getText().toString();
            stringRate = stringRate.replace(',','.');
            rate = Double.parseDouble(stringRate)/100/12;
        }catch (NumberFormatException ex)
        {
            rate = 0;
        }


        try {
            String stringMonthPay=editTextMonthPay.getText().toString();
            stringMonthPay = stringMonthPay.replace(',','.');
            monthPay = Double.parseDouble(stringMonthPay);
        }catch (NumberFormatException ex){
            monthPay=0;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


        hintTextView = findViewById(R.id.hintTextView);

        editTextCreditSumm=findViewById(R.id.editTextCreditSumm);
        editTextTimeMonth = findViewById(R.id.editTextTimeMonth);
        editTextRate = findViewById(R.id.editTextRate);
        editTextMonthPay = findViewById(R.id.editTextMonthPay);

        editTextOversize = findViewById(R.id.editTextOversize);
        editTextOversize.setFocusable(false);
        editTextOversize.setLongClickable(false);


        cardViewHint = findViewById(R.id.cardViewHint);

        TextInputLayout inputLayoutSM = findViewById(R.id.textInputLayoutMonthPay);
        inputLayoutSM.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readValue();
                double annuityK = annuityK(rate,timeMonth);
                monthPay = creditSumm*annuityK;
                if (Double.isNaN(monthPay)){
                    Toast.makeText(getApplicationContext(),"???? ?????? ???????????? ?????? ?????????????? ?????????????? ??????????????????",Toast.LENGTH_LONG).show();
                    editTextMonthPay.setText(String.format("%.2f", 0f));
                } else {
                    editTextMonthPay.setText(String.format("%.2f", monthPay));
                }
                setOversizeValue();
            }
        });

        TextInputLayout inputLayoutRate = findViewById(R.id.textInputLayoutRate);
        inputLayoutRate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readValue();
                if (creditSumm/timeMonth>monthPay){
                    hintTextView.setText("???? ???????????????????? ?????????? ?????????????????? ??????????????, ???????????? ?????? ???????????????????? ?????? ?????????????????? ?? ???????????????? ???????????? ???????? ?????? ??????????????????, ???????????????? ????????????????");
                    return;
                }
                double pay = 0;
                boolean found = false;
                double i;
                for (i = 0; i <100000 ; i++) {
                    double annuityK = annuityK((i/100)/100/12,timeMonth);
                    pay = creditSumm*annuityK;
                    if (pay>=monthPay){
                        editTextRate.setText(String.format("%.2f",i/100));
                        found = true;
                        break;
                    }
                }
                if (!found || i/100>100){
                    if (found) {
                        editTextRate.setText(String.format("%.2f", i/100));
                    } else {
                        editTextRate.setText(String.format("%.2f", 99.99f));
                    }
                    hintTextView.setText("???????????????????? ???????????? ?????????? 100% ??????????????");
                    new Thread()
                    {
                        @Override
                        public void run() {
                            Float old = cardViewHint.getCardElevation();
                            ColorStateList oldColor = cardViewHint.getCardBackgroundColor();
                            cardViewHint.setCardBackgroundColor(Color.parseColor("#FFEEEE"));
                            for (int i = 0; i <100 ; i++) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                cardViewHint.setCardElevation(i);
                            }

                            for (int i = 100; i >0 ; i--) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                cardViewHint.setCardElevation(i);
                            }
                            cardViewHint.setCardElevation(old);
                            cardViewHint.setCardBackgroundColor(oldColor);
                        }
                    }.start();
                    return;
                }
                setOversizeValue();
                hintTextView.setText("???????????? ???????????? ???????????? ???? ?????????????? ?????????? ?????????????????? ???? ???????????????????? ????????????:  ???????????????? ?? ???????? ?????????????? ?????????? ???????? ???????????????? ?????????????????? ???? ???????? ???????????? ????????????????????????");
            }
        });

    }
}