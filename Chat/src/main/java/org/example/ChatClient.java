package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements Runnable {
    private static final int PORT = 3000;
    private static final String host = "192.168.56.1";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean running = true;

    @Override
    public void run() {
        try {
            // Set up client
            clientSocket = new Socket(host, PORT);

            // Create IO tools
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Handle input
            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            // Handle server output
            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                System.out.println(inMessage);
            }

        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        running = false;
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
            System.err.println("Error during shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public class InputHandler implements Runnable {
        private BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        private PrintWriter stdout = new PrintWriter(System.out, true);

        @Override
        public void run() {
            try {
                while (running) {
                    String message = stdin.readLine();
                    if (message.equals("/quit")) {
                        out.println(message);
                        stdin.close();
                        stdout.close();
                        shutdown();
                    } else {
                        out.println(message);
                    }

                }
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.run();
    }
}
