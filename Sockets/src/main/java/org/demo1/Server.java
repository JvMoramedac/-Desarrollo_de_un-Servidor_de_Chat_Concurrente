package org.demo1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in = null;
        DataOutputStream out = null;

        final int PUERTO = 5000;


        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado. Esperando clientes...");

            while (true) {
                // Esperar a que un cliente realice petición
                sc = servidor.accept();

                System.out.println("Cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                // Leer mensaje del cliente
                String mensaje = in.readUTF();
                System.out.println(mensaje);

                // Envio un mensaje al cliente
                out.writeUTF("Le saludo desde el servidor");

                // Cerrar conexión con el cliente
                sc.close();
                System.out.println("Cliente desconectado");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
