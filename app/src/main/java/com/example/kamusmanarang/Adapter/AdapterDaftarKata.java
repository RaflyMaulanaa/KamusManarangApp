package com.example.kamusmanarang.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kamusmanarang.Api.Model.DaftarKata;
import com.example.kamusmanarang.DaftarKataActivity;
import com.example.kamusmanarang.DataKosakataActivity;
import com.example.kamusmanarang.R;
import com.example.kamusmanarang.Storage.SharedPMUser;

import java.util.List;

public class AdapterDaftarKata extends RecyclerView.Adapter<AdapterDaftarKata.MyViewHolder> {

    private List<DaftarKata> dataDaftarKata;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_indonesia, tv_mamuju;
        LinearLayout linearKosakata;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tv_indonesia = (TextView) view.findViewById(R.id.tv_indonesia);
            tv_mamuju = (TextView) view.findViewById(R.id.tv_mamuju);
            linearKosakata = (LinearLayout) view.findViewById(R.id.linearKosakata);
        }
    }

    public AdapterDaftarKata(List<DaftarKata> list, Context context2) {
        dataDaftarKata = list;
        context = context2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daftarkata, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_indonesia.setText(dataDaftarKata.get(position).getIndonesia());
        holder.tv_mamuju.setText(dataDaftarKata.get(position).getMamuju());
        if(SharedPMUser.getmInstance(context).isLoggedIn()) {
            holder.linearKosakata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] items = {"Edit", "Hapus"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Pilih Opsi");
                    dialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                Intent intent = new Intent(context, DataKosakataActivity.class);
                                intent.putExtra("title", "Edit Kosakata");
                                intent.putExtra("action", "edit");
                                intent.putExtra("indonesia", dataDaftarKata.get(position).getIndonesia());
                                intent.putExtra("mamuju", dataDaftarKata.get(position).getMamuju());
                                intent.putExtra("id", dataDaftarKata.get(position).getId());
                                context.startActivity(intent);
                            }
                            if (i == 1) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Hapus Kosakata")
                                        .setMessage("Apakah anda yakin ingin menghapus kosakata?")
                                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                ((DaftarKataActivity) context).hapusKosaKata(dataDaftarKata.get(position).getId());
                                            }
                                        })
                                        .setNegativeButton("Batal", null)
                                        .show();
                            }
                        }
                    });
                    dialog.create().show();
                }
            });
        }
    }

    public int getItemCount() {
        return dataDaftarKata.size();
    }

    public void updateDaftarKata(List<DaftarKata> list) {
        dataDaftarKata.clear();
        dataDaftarKata.addAll(list);
        notifyDataSetChanged();
    }
        
}
