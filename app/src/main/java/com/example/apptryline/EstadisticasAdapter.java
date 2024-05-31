package com.example.apptryline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EstadisticasAdapter extends RecyclerView.Adapter<EstadisticasAdapter.EstadisticasViewHolder> {

    private List<Estadistica> estadisticasList;
    private LayoutInflater inflater;

    public EstadisticasAdapter(Context context, List<Estadistica> estadisticasList) {
        this.estadisticasList = estadisticasList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EstadisticasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.estadisticas, parent, false);
        return new EstadisticasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadisticasViewHolder holder, int position) {
        Estadistica estadistica = estadisticasList.get(position);
        holder.tituloTextView.setText(estadistica.getTitulo());
        holder.datoFavorTextView.setText(String.valueOf(estadistica.getDatoFavor()));
        holder.datoContraTextView.setText(String.valueOf(estadistica.getDatoContra()));
        holder.progressBar.setMax(estadistica.getMax());
        holder.progressBar.setProgress(estadistica.getProgreso());
    }

    @Override
    public int getItemCount() {
        return estadisticasList.size();
    }

    public static class EstadisticasViewHolder extends RecyclerView.ViewHolder {

        private TextView tituloTextView;
        private TextView datoFavorTextView;
        private TextView datoContraTextView;
        private ProgressBar progressBar;

        public EstadisticasViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.titulo);
            datoFavorTextView = itemView.findViewById(R.id.dato_favor);
            datoContraTextView = itemView.findViewById(R.id.dato_contra);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }
}
