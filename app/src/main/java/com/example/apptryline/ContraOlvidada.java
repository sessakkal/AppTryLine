package com.example.apptryline;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

public class ContraOlvidada extends AppCompatActivity {

    private EditText correoEditText;
    private Button enviarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contra_olvidada);

        // Inicializar vistas
        correoEditText = findViewById(R.id.correo_edittext);
        enviarButton = findViewById(R.id.enviar_button);

        // Configurar el clic del botón de enviar
        enviarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la dirección de correo electrónico ingresada por el usuario
                String correo = correoEditText.getText().toString().trim();

                // Validar si el campo de correo electrónico está vacío
                if (correo.isEmpty()) {
                    Toast.makeText(ContraOlvidada.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                } else {
                    // Enviar correo electrónico al correo ingresado
                    enviarCorreoElectronico(correo);
                }
            }
        });
    }

    private void enviarCorreoElectronico(String correo) {
        final String username = "tu_correo@gmail.com";
        final String password = "tu_contraseña";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Agregar esta línea

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
            message.setSubject("Restablecimiento de contraseña");
            message.setText("Hola,\n\n"
                    + "Haz solicitado restablecer tu contraseña. Sigue este enlace para continuar:\n"
                    + "https://www.ejemplo.com/restablecer_contraseña\n\n"
                    + "Si no has solicitado esto, ignora este correo.\n\n"
                    + "Saludos,\n"
                    + "Equipo de soporte");

            Transport.send(message);

            Log.i("Correo", "¡Correo enviado correctamente!");

            // Mostrar un mensaje de éxito al usuario
            Toast.makeText(this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();

        } catch (MessagingException e) {
            // Manejar la excepción de manera adecuada
            Log.e("Error", "Error al enviar el correo electrónico", e);
            Toast.makeText(this, "Error al enviar el correo electrónico", Toast.LENGTH_SHORT).show();
        }
    }
}


