package com.example.apptryline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class ConfirmacionAdapter extends ArrayAdapter<Confirmacion> {

    private Context context;
    private List<Confirmacion> confirmacionList;

    public ConfirmacionAdapter(@NonNull Context context, List<Confirmacion> confirmacionList) {
        super(context, R.layout.item_confirmacion, confirmacionList);
        this.context = context;
        this.confirmacionList = confirmacionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_confirmacion, parent, false);
        }

        Confirmacion currentConfirmacion = confirmacionList.get(position);

        TextView textViewUsuario = listItem.findViewById(R.id.text_view_usuario);
        TextView textViewComentario = listItem.findViewById(R.id.text_view_comentario);

        textViewUsuario.setText(currentConfirmacion.getUsuario());
        textViewComentario.setText(currentConfirmacion.getComentario());

        return listItem;
    }
}
