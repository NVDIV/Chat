package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements Runnable {
    private static final int PORT = 3000;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private ExecutorService pool;
    private boolean running = true;

    @Override
    public void run() {
        try {
            // Set up the Server
            serverSocket = new ServerSocket(PORT);
            pool = Executors.newCachedThreadPool();
            System.out.println("Server is up.");

            // Wait for connections
            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler clientHandler : clients) {
                clientHandler.shutdown();
            }
            pool.shutdown();
        } catch (IOException e) {
            System.err.println("Error during server shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String nickname;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Create IO tools
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Get user's nickname
                out.println("Please enter your nickname: ");
                nickname = in.readLine();
                broadcastMessage(nickname + " joined the Chat!");

                // Handle user messages
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/quit")) {
                        broadcastMessage(nickname + " left the Chat.");
                        break;
                    }
                    broadcastMessage(nickname + ": " + message);
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                shutdown();
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler clientHandler : clients) {
                clientHandler.out.println(message);
            }
        }

        public void shutdown() {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Error during client shutdown: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.run();
    }
}
