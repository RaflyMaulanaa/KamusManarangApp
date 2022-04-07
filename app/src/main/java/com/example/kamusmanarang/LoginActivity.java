package com.example.kamusmanarang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kamusmanarang.Api.Client;
import com.example.kamusmanarang.Api.InterfaceApi;
import com.example.kamusmanarang.Api.Model.DataUser;
import com.example.kamusmanarang.Storage.SharedPMUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_user, et_password;
    private Button btn_login;

    private String user, password;

    private InterfaceApi interfaceApi;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        interfaceApi = (InterfaceApi) Client.getClient().create(InterfaceApi.class);

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = et_user.getText().toString();
                password = et_password.getText().toString();
                if(!user.isEmpty() && !password.isEmpty()){
                    pDialog.show();
                    interfaceApi.login(user,password).enqueue(new Callback<DataUser>() {
                        @Override
                        public void onResponse(Call<DataUser> call, Response<DataUser> response) {
                            if(response.isSuccessful()) {
                                DataUser dataUser = (DataUser) response.body();
                                if (!dataUser.isError()) {
                                    DataUser user = new DataUser(false, dataUser.getMessage(), dataUser.getNama(), dataUser.getUsername());
                                    SharedPMUser.getmInstance(LoginActivity.this).saveUser(user);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("action", true);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, dataUser.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<DataUser> call, Throwable throwable) {
                            Toast.makeText(LoginActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
