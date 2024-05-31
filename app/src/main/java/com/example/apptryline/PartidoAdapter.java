package com.example.apptryline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder> {

    private List<PartidoDatos> partidos;
    private Context context;

    public PartidoAdapter(List<PartidoDatos> partidos, Context context) {
        this.partidos = partidos;
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
        PartidoDatos partido = partidos.get(position);
        holder.equipoLocal.setText(partido.getEquipoLocal());
        holder.equipoVisitante.setText(partido.getEquipoVisitante());
        holder.fecha.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(partido.getFecha()));
        holder.hora.setText(partido.getHoraInicio());
        holder.ubicacion.setText(partido.getUbicacionTexto());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Partido.class);
            intent.putExtra("partidoId", partido.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }

    public void updatePartidos(List<PartidoDatos> nuevosPartidos) {
        this.partidos = nuevosPartidos;
        notifyDataSetChanged();
    }

    public static class PartidoViewHolder extends RecyclerView.ViewHolder {
        TextView equipoLocal;
        TextView equipoVisitante;
        TextView fecha;
        TextView hora;
        TextView ubicacion;

        public PartidoViewHolder(View itemView) {
            super(itemView);
            equipoLocal = itemView.findViewById(R.id.equipoLocal);
            equipoVisitante = itemView.findViewById(R.id.equipoVisitante);
            fecha = itemView.findViewById(R.id.fecha);
            hora = itemView.findViewById(R.id.hora);
            ubicacion = itemView.findViewById(R.id.ubicacion);
        }
    }
}
