package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Thread {
    public static final int PORT = 5555;
    private ServerSocket serverSocket = null;

    public GameServer() throws IOException{
        serverSocket = new ServerSocket(PORT);
        while (true){
            System.out.println("Waiting on port " + PORT + "...");
            Socket socket = serverSocket.accept();
            new ClientThread(socket, serverSocket).start();
        }
    }

    public static void main(String[] args) {
        try{
            GameServer server = new GameServer();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Program terminated");
        }
    }
}
