package com.pao.laboratory13.exercise2;

import com.pao.laboratory13.exercise1.ProtocolEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int CLIENT_COUNT = 2;

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9000;
        CountDownLatch clientsDone = new CountDownLatch(CLIENT_COUNT);
        ExecutorService executor = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Listening on port " + port);

            executor.submit(() -> acceptClients(serverSocket, executor, clientsDone));

            Thread.sleep(150);
            runClient("CLIENT-1", port, Arrays.asList("AUTH alice", "OPEN", "SEND hi", "HISTORY", "CLOSE"));
            runClient("CLIENT-2", port, Arrays.asList("AUTH bob", "OPEN", "BROADCAST all", "HISTORY", "CLOSE"));

            clientsDone.await(5, TimeUnit.SECONDS);
            System.out.println("[SERVER] All clients done. Shutting down.");
        } finally {
            executor.shutdownNow();
        }
    }

    private static void acceptClients(ServerSocket serverSocket, ExecutorService executor, CountDownLatch clientsDone) {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            try {
                Socket socket = serverSocket.accept();
                int clientNo = i + 1;
                executor.submit(() -> handleClient("SESSION-" + clientNo, socket, clientsDone));
            } catch (Exception e) {
                System.out.println("[SERVER] Accept stopped");
                return;
            }
        }
    }

    private static void handleClient(String sessionName, Socket socket, CountDownLatch clientsDone) {
        ProtocolEngine engine = new ProtocolEngine();
        try (Socket clientSocket = socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            System.out.println("[SERVER] " + sessionName + " connected");

            String line;
            while ((line = in.readLine()) != null) {
                String response = engine.execute(line);
                out.println(response);
                System.out.println("[SERVER] " + sessionName + " " + line + " => " + response);
            }
        } catch (Exception e) {
            System.out.println("[SERVER] " + sessionName + " error: " + e.getMessage());
        } finally {
            System.out.println("[SERVER] " + sessionName + " disconnected");
            clientsDone.countDown();
        }
    }

    private static void runClient(String name, int port, List<String> commands) {
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", port);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("[" + name + "] Connected");

                for (String command : commands) {
                    out.println(command);
                    String response = in.readLine();
                    System.out.println("[" + name + "] >> " + command + " => " + response);
                }

                System.out.println("[" + name + "] Disconnected");
            } catch (Exception e) {
                System.out.println("[" + name + "] Error: " + e.getMessage());
            }
        }, name).start();
    }
}
