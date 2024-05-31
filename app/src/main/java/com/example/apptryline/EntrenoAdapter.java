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

public class EntrenoAdapter extends RecyclerView.Adapter<EntrenoAdapter.EntrenoViewHolder> {

    private List<EntrenoDatos> entrenos;
    private Context context;

    public EntrenoAdapter(List<EntrenoDatos> entrenos, Context context) {
        this.entrenos = entrenos;
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
        EntrenoDatos entreno = entrenos.get(position);
        holder.descripcion.setText(entreno.getDescripcion());
        holder.fecha.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(entreno.getFecha()));
        holder.horaInicio.setText(entreno.getHoraInicio());
        holder.horaFin.setText(entreno.getHoraFin());
        holder.ubicacion.setText(entreno.getLugar());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Entreno.class);
            intent.putExtra("entrenoId", entreno.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return entrenos.size();
    }

    public void updateEntrenos(List<EntrenoDatos> nuevosEntrenos) {
        this.entrenos = nuevosEntrenos;
        notifyDataSetChanged();
    }

    public static class EntrenoViewHolder extends RecyclerView.ViewHolder {
        TextView descripcion;
        TextView fecha;
        TextView horaInicio;
        TextView horaFin;
        TextView ubicacion;

        public EntrenoViewHolder(View itemView) {
            super(itemView);
            descripcion = itemView.findViewById(R.id.descripcion);
            fecha = itemView.findViewById(R.id.fecha);
            horaInicio = itemView.findViewById(R.id.horaInicio);
            horaFin = itemView.findViewById(R.id.horaFin);
            ubicacion = itemView.findViewById(R.id.ubicacion);
        }
    }
}
