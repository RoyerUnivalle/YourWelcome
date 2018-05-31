package com.example.usuario.yourwelcome.Servicios;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String TAG = "NOTICIAS";

    //https://alvarez.tech/labs/android-fcm
    //https://firebase.google.com/docs/cloud-messaging/android/client?hl=es-419
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        System.out.println("Token: " + token);

        enviarTokenAlServidor(token);
    }

    private void enviarTokenAlServidor(String token) {
        // Enviar token al servidor
    }
}
