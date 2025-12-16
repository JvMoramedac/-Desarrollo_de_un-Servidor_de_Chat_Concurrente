package Tarea1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    // Lista global para la difusión de mensajes
    private static final List<GestorCliente> CLIENTES_CONECTADOS = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

        ServerSocket servidor = null;// Fase 1: Declaración ServerSocket
        final int PUERTO = 5000;// Puerto de conexión

         // Fase 1: Creación del ServerSocket

        try {
            servidor = new ServerSocket(PUERTO);// Fase 1: Bind y Listen
            System.out.println("Servidor iniciado. Esperando clientes...");

            // Fase 3: El main solo desvía el trabajo a nuevos hilos
            while (true) {
                // Fase 3: Esperar conexión (Aquí se bloquearía el Main Thread en Fase 1)
                Socket sc = servidor.accept();// Fase 1: Accept
                //Aqui iria el thread sleep para simular retardo en la conexion
                // Fase 3: Crear el gestor para el cliente
                GestorCliente gestor = new GestorCliente(sc);

                CLIENTES_CONECTADOS.add(gestor);

                // Fase 3: Crear y arrancar el hilo (Solución a Fase 1)
                new Thread(gestor).start();
            }
        } catch (IOException e) {
            System.err.println("Error grave en el ServerSocket: " + e.getMessage());// Fase 1: Manejo de errores
        } finally {
            if (servidor != null) {
                try {
                    servidor.close();// Fase 1: Cierre del ServerSocket
                } catch (IOException e) {
                    System.err.println("Error al cerrar el ServerSocket: " + e.getMessage());
                }
            }
        }
    }

    /** Envía un mensaje a todos los clientes conectados, excluyendo al remitente si es un mensaje de chat. */
    public static void difundirMensaje(String mensaje, GestorCliente remitente) {// Fase 2 & Fase 4: Difusión de mensajes
        synchronized (CLIENTES_CONECTADOS) {// Sincronización para evitar ConcurrentModificationException
            for (GestorCliente cliente : CLIENTES_CONECTADOS) {// Fase 2 & Fase 4: Iterar sobre clientes
                if (cliente != remitente) {// Fase 4: Excluir remitente
                    cliente.enviarMensaje(mensaje);
                }
            }
        }
    }

    /**
     * Clase Anidada: Implementa la lógica de chat en un hilo separado (Fase 3).
     */
    private static class GestorCliente implements Runnable { // Fase 3: Implementa Runnable

        private final Socket clienteSocket;// Fase 3: Socket del cliente
        private DataOutputStream out;// Fase 2: Stream de salida
        private String nombreUsuario;// Fase 4: Nombre de usuario
        private final String clienteIP;// Fase 4: Dirección IP del cliente

        public GestorCliente(Socket socket) { // Fase 3: Constructor recibe Socket
            this.clienteSocket = socket;// Fase 3: Asigna el Socket
            this.clienteIP = socket.getInetAddress().getHostAddress(); // Fase 4: IP
        }

        public void enviarMensaje(String mensaje) {// Fase 2: Método para enviar mensajes
            try {
                if (out != null) {// Verifica que el stream esté inicializado
                    out.writeUTF(mensaje);// Fase 2: Envía el mensaje
                }
            } catch (IOException e) {
                // Manejo de error de envío
            }
        }

        @Override
        public void run() { // Fase 3: Método run() con lógica de Fase 2
            // Fase 2 & Fase 4: try-with-resources
            try (Socket sc = this.clienteSocket;
                 DataInputStream in = new DataInputStream(sc.getInputStream())) {// Fase 2: Stream de entrada

                this.out = new DataOutputStream(sc.getOutputStream());

                // Fase 4: Petición de nombre
                out.writeUTF("SERVER: Por favor, introduce tu nombre de usuario:");
                this.nombreUsuario = in.readUTF();

                // Fase 4: Notificación de Conexión (Logging)
                String mensajeConexion = String.format("SERVER: >>> %s (%s) se ha conectado.", nombreUsuario, clienteIP);
                System.out.println(mensajeConexion);
                difundirMensaje(mensajeConexion, null);

                // Fase 2: Bucle de conversación
                boolean salir = false;
                while (!salir) { // Fase 2: Bucle while
                    String mensajeRecibido = in.readUTF();

                    if (mensajeRecibido.equalsIgnoreCase("FIN")) { // Fase 2: Protocolo de fin
                        salir = true;
                    } else {
                        // Fase 4: Difusión del chat
                        String mensajeChat = String.format("%s: %s", nombreUsuario, mensajeRecibido);
                        System.out.println(mensajeChat);
                        difundirMensaje(mensajeChat, this);
                    }
                }

            } catch (EOFException | SocketException e) {
                // Fase 4: Manejo de desconexiones abruptas
                System.out.println("El cliente " + (nombreUsuario != null ? nombreUsuario : clienteIP) + " se ha desconectado inesperadamente.");
            } catch (IOException e) {
                System.err.println("Error de comunicación con " + (nombreUsuario != null ? nombreUsuario : clienteIP) + ": " + e.getMessage());
            } finally {
                // Limpieza y notificación de desconexión
                if (nombreUsuario != null) {
                    String mensajeDesconexion = String.format("SERVER: <<< %s (%s) se ha desconectado.", nombreUsuario, clienteIP);
                    CLIENTES_CONECTADOS.remove(this);
                    difundirMensaje(mensajeDesconexion, null);
                    System.out.println(mensajeDesconexion);
                }
            }
        }
    }
}