package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
    public static final String serverAddress = "127.0.0.1";
    public static final int PORT=5555;

    public static void main(String[] args){
        try{
            Socket socket = new Socket(serverAddress, PORT);
            new ServerCommunicationThread(socket).start();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scan = new Scanner(System.in);

            while (true){
                String request = scan.nextLine();
                if (request.equals("exit")){
                    return;
                }

                out.println(request);
            }
        }
        catch (UnknownHostException e){
            System.out.println("Server unavailable");
        }
        catch (IOException e){
            System.out.println("Client terminated " + e);
        }
    }
}
