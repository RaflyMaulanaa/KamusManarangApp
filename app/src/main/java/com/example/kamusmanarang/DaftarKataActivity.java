package com.example.kamusmanarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamusmanarang.Adapter.AdapterDaftarKata;
import com.example.kamusmanarang.Api.Client;
import com.example.kamusmanarang.Api.InterfaceApi;
import com.example.kamusmanarang.Api.Model.DaftarKata;
import com.example.kamusmanarang.Api.Model.DaftarKata_respon;
import com.example.kamusmanarang.Api.Model.Data_respon;
import com.example.kamusmanarang.Storage.SharedPMUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarKataActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private InterfaceApi interfaceApi;
    private AdapterDaftarKata adapterDaftarKata;
    private RecyclerView list_daftarkata;
    private LinearLayoutManager layoutManager;
    private EditText et_pencarian;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<DaftarKata> daftarKata = new ArrayList();

    private ProgressDialog pDialog;

    private ConstraintLayout constraint_info;
    private ImageView iv_info;
    private TextView tv_title, tv_subtitle;
    private Button btn_submit;

    private FloatingActionButton float_tambah;

    private String pencarianKosakata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarkata);
        list_daftarkata = (RecyclerView) findViewById(R.id.list_daftarkata);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperfrsh);

        constraint_info = (ConstraintLayout) findViewById(R.id.constraint_info);
        iv_info = (ImageView) findViewById(R.id.iv_info);
        tv_title = (TextView) findViewById(R.id.tv_title);
//        et_pencarian = (EditText) findViewById(R.id.et_pencarian);
        tv_subtitle = (TextView) findViewById(R.id.tv_subtitle);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        float_tambah = (FloatingActionButton) findViewById(R.id.float_tambah);
        if(SharedPMUser.getmInstance(DaftarKataActivity.this).isLoggedIn()) {
            float_tambah.show();
            float_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DaftarKataActivity.this, DataKosakataActivity.class);
                    intent.putExtra("title","Tambah Kosakata");
                    intent.putExtra("action","tambah");
                    startActivity(intent);
                }
            });
        }else{
            float_tambah.hide();
        }

        layoutManager = new LinearLayoutManager(DaftarKataActivity.this);
        list_daftarkata.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Daftar Kata");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(DaftarKataActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(false);

        interfaceApi = (InterfaceApi) Client.getClient().create(InterfaceApi.class);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                daftarKata.clear();
                loadData();
            }
        });

//        et_pencarian.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                loadData();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        loadData();
    }

    private void loadData(){
//        pencarianKosakata = et_pencarian.getText().toString();
        constraint_info.setVisibility(View.GONE);
        pDialog.show();
        interfaceApi.getDaftarKata(pencarianKosakata).enqueue(new Callback<DaftarKata_respon>() {
            @Override
            public void onResponse(Call<DaftarKata_respon> call, Response<DaftarKata_respon> response) {
                DaftarKata_respon daftarKata_respon = (DaftarKata_respon) response.body();
                if(response.isSuccessful()) {
                    if (!daftarKata_respon.isError()) {
                        adapterDaftarKata = new AdapterDaftarKata(((DaftarKata_respon) response.body()).getData(), DaftarKataActivity.this);
                        list_daftarkata.setAdapter(adapterDaftarKata);
                    } else {
                        showInfo("Gagal menampilkan daftar kata", daftarKata_respon.getMessage(), "Refresh");
                    }
                }else{
                    showInfo("Gagal terhubung keserver","Daftar kata tidak dapat ditampilkan","Refresh");
                }
                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DaftarKata_respon> call, Throwable t) {
                showInfo("Gagal terhubung keserver","Daftar kata tidak dapat ditampilkan","Refresh");
                pDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showInfo(String title, String subtitle, String button){
        constraint_info.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        tv_subtitle.setText(subtitle);
        btn_submit.setText(button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftarKata.clear();
                loadData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }else if(id == R.id.login){
            Intent intent = new Intent(DaftarKataActivity.this, LoginActivity.class);
            startActivity(intent);
        }else if(id == R.id.logout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Apakah anda yakin ingin logout?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(SharedPMUser.getmInstance(DaftarKataActivity.this).isLoggedIn()) {
                                SharedPMUser.getmInstance(DaftarKataActivity.this).clear();
                            }
                            Intent intent = new Intent(DaftarKataActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("logout", true);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login = menu.findItem(R.id.login);
        MenuItem logout = menu.findItem(R.id.logout);
        if(SharedPMUser.getmInstance(DaftarKataActivity.this).isLoggedIn()) {
            login.setVisible(false);
            logout.setVisible(true);
        }else{
            login.setVisible(true);
            logout.setVisible(false);
        }
        return true;
    }

    public void hapusKosaKata(String id){
        pDialog.show();
        interfaceApi.kosakata("hapus",null,null,id).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()) {
                    Data_respon data_respon = (Data_respon) response.body();
                    if (!data_respon.isError()) {
                        loadData();
                    } else {
                        Toast.makeText(DaftarKataActivity.this, data_respon.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }else{
                    Toast.makeText(DaftarKataActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Data_respon> call, Throwable t) {
                Toast.makeText(DaftarKataActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }
}