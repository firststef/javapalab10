package server;

import game.GameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket = null;
    private ServerSocket server = null;
    private GameController gameController = null;

    ClientThread (Socket socket, ServerSocket server, GameController gameController) {
        this.socket = socket;
        this.server = server;
        this.gameController = gameController;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String request = in.readLine();
                System.out.println("Server received the request " + request);
                out.println("Server received the request " + request);

                if (request.equals("stop")) {
                    server.close();
                    out.println("Server stopped");
                    return;
                }

                gameController.solveRequest(request, socket);
            }
        }
        catch (IOException e){
            System.out.println("Communication error");
        }
        finally {
            try{
                socket.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
