package com.example.kamusmanarang.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;


import androidx.appcompat.widget.AppCompatImageView;

import com.example.kamusmanarang.Api.Model.Bahasa;
import com.example.kamusmanarang.DaftarKataActivity;
import com.example.kamusmanarang.R;

import java.util.ArrayList;

public class AdapterPilihBahasa extends ArrayAdapter<Bahasa> {

    private String selectBahasa;

    public AdapterPilihBahasa(Activity context, ArrayList<Bahasa> mBahasa, String selectBahasa) {
        super(context, 0, mBahasa);
        this.selectBahasa = selectBahasa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_select_bahasa, parent, false);
        }

        Bahasa mBahasa = getItem(position);
        CheckedTextView ctv_bahasa = (CheckedTextView) listItemView.findViewById(R.id.ctv_bahasa);
        AppCompatImageView btn_daftarkata = (AppCompatImageView) listItemView.findViewById(R.id.btn_daftarkata);
        ctv_bahasa.setText(mBahasa.getBahasa());
        if(selectBahasa.equals(mBahasa.getBahasa())){
            ctv_bahasa.setChecked(true);
        }else{
            ctv_bahasa.setChecked(false);
        }
        if(mBahasa.getBahasa().equals("Mamuju")){
            btn_daftarkata.setVisibility(View.VISIBLE);
            btn_daftarkata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DaftarKataActivity.class);
                    v.getContext().startActivity(intent);

                }
            });
        }else{
            btn_daftarkata.setVisibility(View.GONE);
        }
        return listItemView;
    }
}