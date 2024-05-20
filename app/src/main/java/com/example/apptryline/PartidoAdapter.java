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

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<String> partidosIds;
    private String equipoId;
    private Context context;

    public PartidoAdapter(List<String> partidosIds, String equipoId, Context context) {
        this.partidosIds = partidosIds;
        this.equipoId = equipoId;
        this.context = context;
    }

    @NonNull
    @Override
    public PartidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_partido, parent, false);
        return new PartidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoViewHolder holder, int position) {
        String partidoId = partidosIds.get(position);
        holder.textViewPartido.setText(partidoId);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Partido.class);
            intent.putExtra("partidoId", partidoId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return partidosIds.size();
    }

    public void updatePartidos(List<String> nuevosPartidos) {
        this.partidosIds = nuevosPartidos;
        notifyDataSetChanged();
    }

    public static class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPartido;

        public PartidoViewHolder(View itemView) {
            super(itemView);
            textViewPartido = itemView.findViewById(R.id.textViewPartido);
        }
    }
}
