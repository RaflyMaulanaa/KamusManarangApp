package com.example.kamusmanarang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamusmanarang.Adapter.AdapterPilihBahasa;
import com.example.kamusmanarang.Api.Client;
import com.example.kamusmanarang.Api.InterfaceApi;
import com.example.kamusmanarang.Api.Model.Bahasa;
import com.example.kamusmanarang.Api.Model.Data_respon;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_pilihbahasa;
    private ArrayList<Bahasa> list = new ArrayList<>();
    private AdapterPilihBahasa adapterPilihBahasa;

    private String selectBahasa = "Indonesia";

    private TextView tv_translate1, tv_translate2, tv_info;
    private AppCompatEditText et_inputtranslate;

    private AppCompatImageView btn_hapus, btn_speech, btn_copytranslate1, btn_copytranslate2, btn_speech1, btn_speech2;
    private static final int RECOGNIZER_RESULT = 1;

    private FloatingTextButton btn_translate;
    private InterfaceApi interfaceApi;

    private AppCompatTextView tv_outputtranslate1, tv_outputtranslate2;
    private View loading1, loading2;

    private ClipboardManager clipboard;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        interfaceApi = (InterfaceApi) Client.getClient().create(InterfaceApi.class);

        btn_pilihbahasa = (AppCompatButton) findViewById(R.id.btn_pilihbahasa);
        tv_info = (TextView) findViewById(R.id.tv_info);
        String info = "Kata berwarna <font color=#ff0000>Merah</font> tidak ditemukan pada Kosa Kata";
        tv_info.setText(Html.fromHtml(info));
        tv_translate1 = (TextView) findViewById(R.id.tv_translate1);
        tv_translate2 = (TextView) findViewById(R.id.tv_translate2);
        tv_outputtranslate1 = (AppCompatTextView) findViewById(R.id.tv_outputtranslate1);
        tv_outputtranslate2 = (AppCompatTextView) findViewById(R.id.tv_outputtranslate2);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Speech gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.getBoolean("logout") == true) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if(extras.getBoolean("action") == true){
                Intent intent = new Intent(MainActivity.this, DaftarKataActivity.class);
                startActivity(intent);
            }
        }

        btn_speech1 = (AppCompatImageView) findViewById(R.id.btn_speech1);
        btn_speech2 = (AppCompatImageView) findViewById(R.id.btn_speech2);

        btn_speech1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String translate1 = tv_outputtranslate1.getText().toString();
                if(!translate1.isEmpty()){
                    texttoSpeech(translate1,tv_translate1.getText().toString());
                }
            }
        });

        btn_speech2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String translate2 = tv_outputtranslate2.getText().toString();
                if(!translate2.isEmpty()){
                    texttoSpeech(translate2,tv_translate2.getText().toString());
                }
            }
        });

        et_inputtranslate = (AppCompatEditText) findViewById(R.id.et_inputtranslate);
        et_inputtranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(et_inputtranslate.getText().toString().isEmpty()){
                    tv_info.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_translate = (FloatingTextButton) findViewById(R.id.btn_translate);

        loading1 = (View) findViewById(R.id.loading1);
        loading2 = (View) findViewById(R.id.loading2);

        btn_hapus = (AppCompatImageView) findViewById(R.id.btn_hapus);
        btn_speech = (AppCompatImageView) findViewById(R.id.btn_speech);
        btn_copytranslate1 = (AppCompatImageView) findViewById(R.id.btn_copytranslate1);
        btn_copytranslate2 = (AppCompatImageView) findViewById(R.id.btn_copytranslate2);

        list = new ArrayList<>();
        list.add(new Bahasa("Indonesia"));
        list.add(new Bahasa("Inggris"));
        list.add(new Bahasa("Mamuju"));

        pilihBahasa(selectBahasa);

        btn_pilihbahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.item_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer));
                ListView list_bahasa = (ListView) bottomSheetView.findViewById(R.id.list_bahasa);
                adapterPilihBahasa = new AdapterPilihBahasa(MainActivity.this,list,selectBahasa);
                list_bahasa.setAdapter(adapterPilihBahasa);
                list_bahasa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bahasa mBahasa = list.get(position);
                        btn_pilihbahasa.setText(mBahasa.getBahasa());
                        selectBahasa = mBahasa.getBahasa();
                        pilihBahasa(selectBahasa);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_inputtranslate.getText().clear();
            }
        });

        btn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan Sesuatu");
                startActivityForResult(speechIntent,RECOGNIZER_RESULT);
            }
        });

        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!et_inputtranslate.getText().toString().isEmpty()) {
                    closeKeyboard();
                    tv_info.setVisibility(View.VISIBLE);
                }
                translate(et_inputtranslate.getText().toString());
            }
        });

        btn_copytranslate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_outputtranslate1.getText().length() != 0){
                    clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(tv_outputtranslate1.getText());
                    Toast.makeText(MainActivity.this, "Translate bahasa "+tv_translate1.getText()+" berhasil dicopy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_copytranslate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_outputtranslate2.getText().length() != 0){
                    clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(tv_outputtranslate2.getText());
                    Toast.makeText(MainActivity.this, "Translate bahasa "+tv_translate2.getText()+" berhasil dicopy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void translate(String inputtranslate){
        if(!inputtranslate.isEmpty()) {
            if(checkJumlahKata(inputtranslate) < 300) {
                loading1.setVisibility(View.VISIBLE);
                loading2.setVisibility(View.VISIBLE);
                if (selectBahasa.equals("Indonesia")) {
                    translateMamuju(selectBahasa, inputtranslate);
                    translateGoogle(selectBahasa, inputtranslate);
                } else if (selectBahasa.equals("Inggris")) {
                    translateGoogle(selectBahasa, inputtranslate);
                } else if (selectBahasa.equals("Mamuju")) {
                    translateMamuju(selectBahasa, inputtranslate);
                }
            }else{
                Toast.makeText(this, "Translate tidak boleh lebih dari 300 kata", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "Kata atau kalimat masih kosong", Toast.LENGTH_SHORT).show();
        }
    }

    private void translateMamuju(final String bahasa, final String translate){
        interfaceApi.translateMamuju(bahasa, translate).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()) {
                    Data_respon data_respon = (Data_respon) response.body();
                    if (!data_respon.isError()) {
                        if(translate.equals(data_respon.getResult())){
                            Toast.makeText(MainActivity.this, "Kata tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                        if (selectBahasa.equals("Indonesia")) {
                            tv_outputtranslate1.setTextColor(MainActivity.this.getResources().getColor(R.color.red));
                            tv_outputtranslate1.setText(Html.fromHtml(data_respon.getResult()));
                            loading1.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Inggris")) {
                            tv_outputtranslate2.setTextColor(MainActivity.this.getResources().getColor(R.color.red));
                            tv_outputtranslate2.setText(Html.fromHtml(data_respon.getResult()));
                            loading2.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Mamuju")) {
                            tv_outputtranslate1.setTextColor(MainActivity.this.getResources().getColor(R.color.red));
                            tv_outputtranslate1.setText(Html.fromHtml(data_respon.getResult()));
                            translateGoogle("Indonesia", String.valueOf(Html.fromHtml(data_respon.getResult())));
                            loading1.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, data_respon.getResult(), Toast.LENGTH_SHORT).show();
                        if (selectBahasa.equals("Indonesia")) {
                            tv_outputtranslate1.setText(null);
                            loading1.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Inggris")) {
                            tv_outputtranslate2.setText(null);
                            loading2.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Mamuju")) {
                            tv_outputtranslate1.setText(null);
                            loading1.setVisibility(View.GONE);
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                    if (selectBahasa.equals("Indonesia")) {
                        tv_outputtranslate1.setText(null);
                        loading1.setVisibility(View.GONE);
                    } else if (selectBahasa.equals("Inggris")) {
                        tv_outputtranslate2.setText(null);
                        loading2.setVisibility(View.GONE);
                    } else if(selectBahasa.equals("Mamuju")) {
                        tv_outputtranslate1.setText(null);
                        loading1.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data_respon> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                if (selectBahasa.equals("Indonesia")) {
                    tv_outputtranslate1.setText(null);
                    loading1.setVisibility(View.GONE);
                } else if (selectBahasa.equals("Inggris")) {
                    tv_outputtranslate2.setText(null);
                    loading2.setVisibility(View.GONE);
                } else if(selectBahasa.equals("Mamuju")) {
                    tv_outputtranslate1.setText(null);
                    loading1.setVisibility(View.GONE);
                }
            }
        });
    }

    private void translateGoogle(final String bahasa, final String translate){
        String from = null, to = null;
        if(bahasa.equals("Indonesia") || bahasa.equals("Mamuju")){
            from = "id";
            to = "en";
        }else if(bahasa.equals("Inggris")){
            from = "en";
            to = "id";
        }
        interfaceApi.translateGoogle(from,to,translate).enqueue(new Callback<Data_respon>() {
            @Override
            public void onResponse(Call<Data_respon> call, Response<Data_respon> response) {
                if(response.isSuccessful()) {
                    Data_respon data_respon = (Data_respon) response.body();
                    if (!data_respon.isError()) {
                        if(translate.equals(data_respon.getResult())){
                            Toast.makeText(MainActivity.this, "Kata tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                        if (selectBahasa.equals("Indonesia")) {
                            tv_outputtranslate2.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                            tv_outputtranslate2.setText(data_respon.getResult());
                            loading2.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Inggris")) {
                            tv_outputtranslate1.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                            tv_outputtranslate1.setText(data_respon.getResult());
                            translateMamuju("Indonesia", data_respon.getResult());
                            loading1.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Mamuju")) {
                            tv_outputtranslate2.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
                            tv_outputtranslate2.setText(data_respon.getResult());
                            loading2.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, data_respon.getResult(), Toast.LENGTH_SHORT).show();
                        if (selectBahasa.equals("Indonesia")) {
                            tv_outputtranslate2.setText(null);
                            loading2.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Inggris")) {
                            tv_outputtranslate1.setText(null);
                            loading1.setVisibility(View.GONE);
                        } else if (selectBahasa.equals("Mamuju")) {
                            tv_outputtranslate2.setText(null);
                            loading2.setVisibility(View.GONE);
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                    if (selectBahasa.equals("Indonesia")) {
                        tv_outputtranslate2.setText(null);
                        loading2.setVisibility(View.GONE);
                    } else if (selectBahasa.equals("Inggris")) {
                        tv_outputtranslate1.setText(null);
                        loading1.setVisibility(View.GONE);
                    } else if(selectBahasa.equals("Mamuju")){
                        tv_outputtranslate2.setText(null);
                        loading2.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data_respon> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Gagal terhubung keserver", Toast.LENGTH_SHORT).show();
                if (selectBahasa.equals("Indonesia")) {
                    tv_outputtranslate2.setText(null);
                    loading2.setVisibility(View.GONE);
                } else if (selectBahasa.equals("Inggris")) {
                    tv_outputtranslate1.setText(null);
                    loading1.setVisibility(View.GONE);
                } else if(selectBahasa.equals("Mamuju")){
                    tv_outputtranslate2.setText(null);
                    loading2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==  RECOGNIZER_RESULT && data != null){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            et_inputtranslate.setText(matches.get(0).toString());
            et_inputtranslate.setSelection(et_inputtranslate.getText().length());
            translate(et_inputtranslate.getText().toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pilihBahasa(String selectBahasa){
        tv_outputtranslate1.setText(null);
        tv_outputtranslate2.setText(null);
        if(selectBahasa.equals("Indonesia")){
            tv_translate1.setText("Mamuju");
            tv_translate2.setText("Inggris");
        }
        if(selectBahasa.equals("Inggris")){
            tv_translate1.setText("Indonesia");
            tv_translate2.setText("Mamuju");
        }
        if(selectBahasa.equals("Mamuju")){
            tv_translate1.setText("Indonesia");
            tv_translate2.setText("Inggris");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void texttoSpeech(String data, String country) {
        int language = 0;
        if (country.equals("Indonesia") || country.equals("Mamuju")){
            language = textToSpeech.setLanguage(new Locale("in", "ID"));
        }else if(country.equals("Inggris")){
            language = textToSpeech.setLanguage(new Locale("en", "US"));
        }
        if(!country.isEmpty()) {
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Bahasa speech tidak support", Toast.LENGTH_SHORT).show();
            } else {
//                textToSpeech.setPitch(0.8f);
                textToSpeech.setSpeechRate(0.8f);
                textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    private int checkJumlahKata(String word){
        int position = 0;
        int spaceCount = 0;

        while (position<word.length()){
            if(word.charAt(position) == ' '){
                spaceCount++;
            }
            position++;
        }

        return spaceCount;
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}