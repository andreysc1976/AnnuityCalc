package ru.a_party.annuitycalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private double creditSumm;
    private int timeMonth;
    private double rate;
    private double monthPay;

    private EditText editTextCreditSumm;
    private EditText editTextTimeMonth;
    private EditText editTextRate;
    private EditText editTextMonthPay;


    private double annuityK(double internalRate,int internalTimeMonth){
        if (internalTimeMonth<=0){
            return 0;
        }
        return (internalRate*Math.pow(1f+internalRate,internalTimeMonth))/(Math.pow(1f+internalRate,internalTimeMonth)-1f);
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
        setContentView(R.layout.activity_main);

        editTextCreditSumm=findViewById(R.id.editTextCreditSumm);
        editTextTimeMonth = findViewById(R.id.editTextTimeMonth);
        editTextRate = findViewById(R.id.editTextRate);
        editTextMonthPay = findViewById(R.id.editTextMonthPay);

        findViewById(R.id.buttonCalcMonthPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readValue();
                double annuityK = annuityK(rate,timeMonth);
                monthPay = creditSumm*annuityK;
                editTextMonthPay.setText(String.format("%.2f",monthPay));
            }
        });

        findViewById(R.id.buttonCalcRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readValue();
                double pay = 0;
                boolean found = false;
                for (double i = 0; i <10000 ; i++) {
                    double annuityK = annuityK((i/100)/100/12,timeMonth);
                    pay = creditSumm*annuityK;
                    if (pay>=monthPay){
                        editTextRate.setText(String.format("%.2f",i/100));
                        found = true;
                        break;
                    }
                }
                if (!found){
                    editTextRate.setText(String.format("%.2f",99.99f));
                    Toast toast = Toast.makeText(getApplicationContext(),"Процентная ставка более 100% годовых",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}