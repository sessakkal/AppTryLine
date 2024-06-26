package com.example.apptryline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.HolderChat> {

    private static final int MSG_IZQUIERDO = 0;
    private static final int MSG_DERECHO = 1;
    private Context context;
    private ArrayList<ModelChat> chatArrayList;
    private FirebaseUser firebaseUser;

    public AdapterChat(Context context, ArrayList<ModelChat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_DERECHO) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_derecha, parent, false);
            return new HolderChat(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_izquierda, parent, false);
            return new HolderChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderChat holder, int position) {
        ModelChat modelChat = chatArrayList.get(position);
        holder.mensaje.setText(modelChat.getMensaje());

        String senderId = modelChat.getIdUsuarioEnvia();
        FirebaseDatabase.getInstance().getReference("Usuarios").child(senderId).child("nombreUsuario")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String username = snapshot.getValue(String.class);
                            holder.userNameTv.setText(username);
                        } else {
                            holder.userNameTv.setText("Usuario desconocido");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.userNameTv.setText("Error al cargar usuario");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatArrayList.get(position).getIdUsuarioEnvia().equals(firebaseUser.getUid())) {
            return MSG_DERECHO;
        } else {
            return MSG_IZQUIERDO;
        }
    }

    class HolderChat extends RecyclerView.ViewHolder {
        TextView mensaje, userNameTv;

        public HolderChat(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.mensaje);
            userNameTv = itemView.findViewById(R.id.user_name);
        }
    }
}
