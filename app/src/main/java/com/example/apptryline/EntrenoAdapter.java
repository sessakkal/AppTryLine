package com.example.apptryline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntrenoAdapter extends RecyclerView.Adapter<EntrenoAdapter.EntrenoViewHolder> {

    private List<String> entrenosIds;
    private String equipoId;
    private Context context;

    public EntrenoAdapter(List<String> entrenosIds, String equipoId, Context context) {
        this.entrenosIds = entrenosIds;
        this.equipoId = equipoId;
        this.context = context;
    }

    @NonNull
    @Override
    public EntrenoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_entreno, parent, false);
        return new EntrenoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrenoViewHolder holder, int position) {
        String entrenoId = entrenosIds.get(position);
        holder.textViewEntreno.setText(entrenoId);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Entreno.class);
            intent.putExtra("entrenoId", entrenoId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return entrenosIds.size();
    }

    public void updateEntrenos(List<String> nuevosEntrenos) {
        this.entrenosIds = nuevosEntrenos;
        notifyDataSetChanged();
    }

    public static class EntrenoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntreno;

        public EntrenoViewHolder(View itemView) {
            super(itemView);
            textViewEntreno = itemView.findViewById(R.id.textViewEntreno);
        }
    }
}
