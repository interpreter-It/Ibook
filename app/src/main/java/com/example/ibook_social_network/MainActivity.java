package com.example.ibook_social_network;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Ibook - приложение соцсети.
 * 21.10.2021  01:09 Кулаков Дмитрий
 * 0.1 (101)
 */

public class MainActivity extends AppCompatActivity implements SendingPost.Callback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextActivityAuthorization();
        findViewById(R.id.regbutton).setOnClickListener(v->{
            Intent intent= new Intent(MainActivity.this,RegistrationActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Переход в мессенджер
     */
    void nextActivityAuthorization(){
        findViewById(R.id.avtorization).setOnClickListener(v->{
            if(checkAuthorization()) {
                new SendingPost(this).execute("authorization",getPhone(),getPassword());
            }
        });
    }
    String getPhone(){
        TextView number = findViewById(R.id.editTextPhone);
        return number.getText().toString();
    }
    String getPassword(){
        TextView password = findViewById(R.id.editTextTextPassword);
        return password.getText().toString();
    }

    /**
     * Проверка длины телефона и пароля
     */
    boolean checkAuthorization(){
        String login = getPhone();
        if(!(login.length() == 12 || login.length() == 11))Toast.makeText(getApplicationContext(),"Неверный телефон", Toast.LENGTH_SHORT).show();
        if(getPassword().length() < 6)Toast.makeText(getApplicationContext(),"Миннимальная длина пароля шесть символов", Toast.LENGTH_SHORT).show();
        return (login.length() == 12 || login.length() == 11) && getPassword().length() >= 6;
    }

    @Override
    public void callingBack(String dataResponse) {
        Log.e("IbookServer",dataResponse);
        if(dataResponse.equals("{\"text\":true}")){
            Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
            startActivity(intent);
        }
        else dialogRegistration();
    }
    void dialogRegistration(){
        AlertDialog.Builder registration = new AlertDialog.Builder(MainActivity.this);
        registration.setMessage("Вы не зарегестрированны. Хотите это исправить?")
                .setCancelable(false)
                .setPositiveButton("Да", (dialog, which) -> {
                    dialog.cancel();
                   Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                   // intent.putExtra("phone", getPhone());
                   startActivity(intent);
                })
                .setNegativeButton("Нет", (dialog, which) -> finish());
        AlertDialog alert = registration.create();
        alert.setTitle("Регистрация");
        alert.show();
    }
}