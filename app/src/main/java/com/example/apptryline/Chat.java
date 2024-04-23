package com.example.apptryline;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apptryline.databinding.ChatBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends Fragment {

    private ChatBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Model_Chat> chatArrayList;
    private Adapter_Chat adapterChat;
    private String idUser, idMyUser;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setStackFromEnd(true);
        binding.recyclerChat.setHasFixedSize(true);
        binding.recyclerChat.setLayoutManager(linearLayout);

        if (getArguments() != null) {
            idUser = getArguments().getString("uid");
        }

        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        binding.botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = binding.mensajeAEnviar.getText().toString().trim();
                if (!TextUtils.isEmpty(mensaje)) {
                    enviarMensaje(mensaje);
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("uid").equalTo(idUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String name = "" + dataSnapshot.child("name").getValue();
                            String foto = "" + dataSnapshot.child("profileImage").getValue();

                            binding.nombreUsuario.setText(name);
                            if (foto.equals("")){
                                binding.fotoUsuario.setImageResource(R.drawable.perfil_predeterminado);
                            } else {
                                Glide.with(requireContext()).load(foto).into(binding.fotoUsuario);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        chatArrayList = new ArrayList<>();
        adapterChat = new Adapter_Chat(requireContext(), chatArrayList);
        binding.recyclerChat.setAdapter(adapterChat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        leerMensajes();
    }

    private void leerMensajes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mensajes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model_Chat modelChat = dataSnapshot.getValue(Model_Chat.class);
                    if (modelChat != null && ((modelChat.getIdUsuarioRecibe().equals(idMyUser) && modelChat.getIdUsuarioEnvia().equals(idUser))
                            || (modelChat.getIdUsuarioRecibe().equals(idUser) && modelChat.getIdUsuarioEnvia().equals(idMyUser)))){
                        chatArrayList.add(modelChat);
                    }
                }
                adapterChat.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void enviarMensaje(String mensaje) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Mensajes");
        String messageId = databaseReference.push().getKey();
        idMyUser = firebaseUser.getUid();
        Model_Chat modelChat = new Model_Chat(mensaje, idMyUser, idUser, String.valueOf(System.currentTimeMillis()), false);

        if (messageId != null) {
            databaseReference.child(messageId).setValue(modelChat)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(requireContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                            binding.mensajeAEnviar.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Error al enviar el mensaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class Model_Chat {

        String mensaje, idUsuarioEnvia, idUsuarioRecibe, timestamp;
        boolean mensajeVisto;

        public Model_Chat() {
        }

        public Model_Chat(String mensaje, String idUsuarioEnvia, String idUsuarioRecibe, String timestamp, boolean mensajeVisto) {
            this.mensaje = mensaje;
            this.idUsuarioEnvia = idUsuarioEnvia;
            this.idUsuarioRecibe = idUsuarioRecibe;
            this.timestamp = timestamp;
            this.mensajeVisto = mensajeVisto;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getIdUsuarioEnvia() {
            return idUsuarioEnvia;
        }

        public void setIdUsuarioEnvia(String idUsuarioEnvia) {
            this.idUsuarioEnvia = idUsuarioEnvia;
        }

        public String getIdUsuarioRecibe() {
            return idUsuarioRecibe;
        }

        public void setIdUsuarioRecibe(String idUsuarioRecibe) {
            this.idUsuarioRecibe = idUsuarioRecibe;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public boolean isMensajeVisto() {
            return mensajeVisto;
        }

        public void setMensajeVisto(boolean mensajeVisto) {
            this.mensajeVisto = mensajeVisto;
        }
    }

    public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.HolderChat> {

        private static final int MSG_IZQUIERDO = 0;
        private static final int MSG_DERECHO = 1;
        Context context;
        ArrayList<Model_Chat> chatArrayList;

        public Adapter_Chat(Context context, ArrayList<Model_Chat> chatArrayList) {
            this.context = context;
            this.chatArrayList = chatArrayList;
        }

        @NonNull
        @Override
        public HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == MSG_DERECHO){
                View view = LayoutInflater.from(context).inflate(R.layout.chat_derecha, parent, false);
                return new HolderChat(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_izquierda, parent, false);
                return new HolderChat(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull HolderChat holder, int position) {
            String mensaje = chatArrayList.get(position).getMensaje();
            holder.mensaje.setText(mensaje);
        }

        @Override
        public int getItemCount() {
            return chatArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (chatArrayList.get(position).getIdUsuarioEnvia().equals(firebaseUser.getUid())){
                return MSG_DERECHO;
            } else {
                return MSG_IZQUIERDO;
            }
        }

        class HolderChat extends RecyclerView.ViewHolder{

            TextView mensaje;

            public HolderChat(@NonNull View itemView) {
                super(itemView);
                mensaje = itemView.findViewById(R.id.mensaje);
            }
        }
    }
}
