package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket = null;
    private ServerSocket server = null;

    public ClientThread (Socket socket, ServerSocket server) {
        this.socket = socket;
        this.server = server;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String request = in.readLine();

                if (request.equals("stop")) {
                    server.close();
                    out.println("Server stopped");
                    return;
                }

                System.out.println("Server received the request " + request);
                out.println("Server received the request " + request);
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
