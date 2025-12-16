package Tarea1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class GestorCliente { // ACTÚA COMO EL CLIENTE

    public static void main(String[] args) {

        final String HOST = "localhost";// Dirección del servidor
        final int PUERTO = 5000;// Puerto de conexión

        // Fase 2: try-with-resources
        try (Scanner teclado = new Scanner(System.in);// Scanner para entrada por teclado
             Socket socket = new Socket(HOST, PUERTO);// Fase 1: Creación del Socket y conexión
             DataInputStream in = new DataInputStream(socket.getInputStream()); // Flujo de entrada
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {// Flujo de salida

            String nombreUsuario = ""; // Nombre del usuario

            // Fase 4: Captura petición de nombre
            String mensajeServidor = in.readUTF();// Fase 4: Lee mensaje del servidor
            System.out.println(mensajeServidor);// Fase 4: Muestra mensaje del servidor

            nombreUsuario = teclado.nextLine();// Fase 4: Lee nombre por teclado
            out.writeUTF(nombreUsuario);// Fase 4: Envía nombre al servidor
            final String finalNombreUsuario = nombreUsuario;// Necesario para el hilo

            System.out.println("--- Conectado como " + finalNombreUsuario + ". Escribe 'FIN' para salir. ---");

            // Fase 4: Hilo Secundario (LECTOR): Escucha los mensajes de difusión
            Thread listener = new Thread(() -> {// Fase 4: Hilo Lector
                try {
                    String receivedMessage;// Fase 4: Mensaje recibido
                    while (true) {// Fase 4: Bucle infinito
                        receivedMessage = in.readUTF();// Fase 4: Lee mensaje
                        System.out.println(receivedMessage);// Fase 4: Muestra mensaje
                        if (receivedMessage.equalsIgnoreCase("FIN")) break;// Fase 4: Salir si es "FIN"
                    }
                } catch (IOException e) {
                    System.out.println("Conexión con el servidor perdida o cerrada.");
                }
            });
            listener.start();// Fase 4: Inicia el hilo Lector

            // Fase 2: Hilo Principal (ESCRITOR): Envía mensajes del teclado
            String mensajeCliente = "";// Fase 2: Mensaje del cliente
            while (!mensajeCliente.equalsIgnoreCase("FIN")) { // Fase 2: Bucle while

                mensajeCliente = teclado.nextLine(); // Fase 2: Pide datos por teclado

                if (!mensajeCliente.equalsIgnoreCase("FIN")) {/// Fase 2: Añade nombre al mensaje
                    System.out.println(finalNombreUsuario + ": " + mensajeCliente);// Muestra el mensaje localmente
                }

                out.writeUTF(mensajeCliente); // Fase 2: Envía mensaje
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar o conectar con el servidor: " + e.getMessage());
        }
    }
}