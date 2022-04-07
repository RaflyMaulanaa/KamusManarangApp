package com.example.kamusmanarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kamusmanarang.Api.Client;
import com.example.kamusmanarang.Api.InterfaceApi;
import com.example.kamusmanarang.Api.Model.Data_respon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataKosakataActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText et_bahasaindonesia, et_bahasamamuju;
    private Button btn_simpan;

    private String bahasaindonesia, bahasamamuju;
    private String action,id;

    private InterfaceApi interfaceApi;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kosakata);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        et_bahasaindonesia = (EditText) findViewById(R.id.et_bahasaindonesia);
        et_bahasamamuju = (EditText) findViewById(R.id.et_bahasamamuju);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        interfaceApi = (InterfaceApi) Client.getClient().create(InterfaceApi.class);

        pDialog = new ProgressDialog(DataKosakataActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.getString("title") != null){
                toolbar.setTitle(extras.getString("title"));
            }
            if(extras.getString("action") != null) {
                action = extras.getString("action");
                if(extras.getString("action").equals("edit")){
                    if(extras.getString("indonesia") != null){
                        et_bahasaindonesia.setText(extras.getString("indonesia"));
                        et_bahasaindonesia.setSelection(et_bahasaindonesia.getText().length());
                    }
                    if(extras.getString("mamuju") != null){
                        et_bahasamamuju.setText(extras.getString("mamuju"));
                    }
                    if(extras.getString("id") != null){
                        id = extras.getString("id");
                    }
                }
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bahasaindonesia = et_bahasaindonesia.getText().toString();
                bahasamamuju = et_bahasamamuju.getText().toString();

                if(bahasaindonesia.isEmpty()){
                    et_bahasaindonesia.setError("Kosakata bahasa indonesia masih kosong");
                    et_bahasaindonesia.requestFocus();
                }else if(bahasamamuju.isEmpty()){
                    et_bahasamamuju.setError("Kosakata bahasa mamuju masih kosong");
                    et_bahasamamuju.requestFocus();
                }else{
                    if(action != null) {
                        pDialog.show();
                        interfaceApi.kosakata(action,bahasaindonesia,bahasamamuju,id).enqueue(new Callback<Data_respon>() {
                            @Override
                            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                                if(response.isSuccessful()){
                                    Data_respon data_respon = (Data_respon) response.body();
                                    if(!data_respon.isError()){
                                        Intent intent = new Intent(DataKosakataActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("action", true);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(DataKosakataActivity.this, data_respon.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(DataKosakataActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                                }
                                pDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<Data_respon> call, Throwable t) {
                                Toast.makeText(DataKosakataActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}