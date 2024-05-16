package com.example.apptryline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.ViewHolder> {

    private List<String> partidosIds;

    public PartidoAdapter(List<String> partidosIds) {
        this.partidosIds = partidosIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String partidoId = partidosIds.get(position);
        holder.textViewPartido.setText(partidoId);
    }

    @Override
    public int getItemCount() {
        return partidosIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPartido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPartido = itemView.findViewById(R.id.textViewPartido);
        }
    }

    // Método para actualizar la lista de partidos del adaptador
    public void updatePartidos(List<String> partidosIds) {
        this.partidosIds = partidosIds;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }
}
