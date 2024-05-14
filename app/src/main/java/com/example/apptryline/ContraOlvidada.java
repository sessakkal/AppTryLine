package com.example.apptryline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ContraOlvidada extends AppCompatActivity {

    private EditText correoEditText;
    private Button enviarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contra_olvidada);

        correoEditText = findViewById(R.id.correo_edittext);
        enviarButton = findViewById(R.id.enviar_button);

        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoEditText.getText().toString().trim();
                if (!correo.isEmpty()) {
                    enviarCorreoReset(correo);
                } else {
                    Toast.makeText(ContraOlvidada.this, "Por favor, introduce tu correo electrónico", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enviarCorreoReset(String correo) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(correo)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ContraOlvidada.this, "Se ha enviado un correo electrónico para restablecer tu contraseña", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ContraOlvidada.this, "Error al enviar el correo electrónico de restablecimiento de contraseña", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
    public void goBack(View view) {
        onBackPressed();
    }
}


