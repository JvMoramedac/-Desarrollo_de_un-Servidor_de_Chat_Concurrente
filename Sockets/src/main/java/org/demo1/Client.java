package org.demo1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        //Host del servidor al que nos queremos conectar
        final String HOST = "localhost";
        //Puerto del servidor al que nos queremos conectar
        final int PUERTO = 5000;
        DataInputStream in = null;
        DataOutputStream out = null;

        try {
            Socket socket = new Socket(HOST, PUERTO);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            // Enviar mensaje al servidor
            out.writeUTF("Saludos desde el cliente");

            // Leer mensaje del servidor
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            // Cerrar conexión
            socket.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
    }
    }
}