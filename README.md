💬 Servidor de Chat Concurrente 🚀 (PSP)

Este proyecto transforma un servidor de sockets básico y bloqueante en una robusta aplicación de chat multihilo en Java, capaz de manejar múltiples 👥 clientes simultáneamente.

✨ Logros Destacados

✅ Concurrencia Total: Implementación de hilos (Threads) para evitar el bloqueo del servidor.

🛡️ Robustez: Manejo de desconexiones abruptas (EOFException).

📡 Difusión: Mensajes de chat se envían a todos los usuarios conectados.

👤 Identificación: Registro de usuarios por nombre e IP.

🛠️ Tecnologías Clave
Lenguaje: Java ☕

Conectividad: Java Sockets 🌐

Concurrencia: Threads y Runnable 🔄

Streams: Data Streams (DataInputStream, DataOutputStream) 📥📤

📌 Fases de Desarrollo y Arquitectura
El proyecto cumple estrictamente con las cuatro fases metodológicas requeridas:

I. Análisis del Bloqueo (Fase 1) 🛑
Se comprobó que el servidor original solo podía atender a un cliente a la vez. Si el Hilo Principal se bloqueaba (ej: con Thread.sleep()), impedía completamente la conexión del siguiente cliente.

II. Conversación Fluida (Fase 2) 🔁
Se estableció el protocolo básico de comunicación:

Se implementó el bucle de conversación (while (!FIN)) en Cliente y Servidor.

El cliente usa Scanner para la entrada de teclado. ⌨️

Se asegura el cierre limpio de recursos con try-with-resources.

III. El Servidor Multihilo (Fase 3) 💡
Se implementó la concurrencia delegando el trabajo:

Servidor (main): Su única misión es aceptar la conexión y lanzar un nuevo hilo.

new Thread(gestor).start();

GestorCliente: Implementa Runnable y se encarga de toda la lógica de chat, permitiendo a otros clientes conectarse y hablar simultáneamente.

IV. Mejoras Profesionales (Fase 4 - Bonus) ⭐
Funcionalidades añadidas para la robustez y experiencia de usuario:

Identificación Global: El servidor registra la IP (.getInetAddress()) y el nombre de usuario.

Notificaciones: Mensajes de conexión/desconexión se difunden a todos.

Cliente Asíncrono: El Client.java usa un hilo secundario (listener) para leer continuamente la red, evitando que el usuario se congele mientras escribe. ✍️

Manejo de Errores: Captura de excepciones (EOFException, SocketException) para evitar fallos catastróficos. 🩹

💻 Instrucciones de Ejecución
Iniciar Servidor: Ejecutar la clase Server.java.

Conectar Clientes: Ejecutar la clase GestorCliente.java (el cliente) tantas veces como usuarios se deseen.

Protocolo: Introduce tu nombre de usuario al conectar. Para finalizar tu sesión, escribe FIN. 🚪
