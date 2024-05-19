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

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.ViewHolder> {

    private List<String> partidosIds;
    private Context context;

    public PartidoAdapter(List<String> partidosIds, Context context) {
        this.partidosIds = partidosIds;
        this.context = context;
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

        // Set an OnClickListener to navigate to the Partido activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Partido.class);
                intent.putExtra("partidoId", partidoId);
                context.startActivity(intent);
            }
        });
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

    // Method to update the list of matches in the adapter
    public void updatePartidos(List<String> partidosIds) {
        this.partidosIds = partidosIds;
        notifyDataSetChanged(); // Notify the RecyclerView that data has changed
    }
}
