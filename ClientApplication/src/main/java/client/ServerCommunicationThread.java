package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerCommunicationThread extends Thread {
    private Socket socket;

    ServerCommunicationThread(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String response = in.readLine();
                System.out.println(response);
            }
        }
        catch (IOException ignored){ } //Cand se va inchide socketul din thread-ul principal se va termina cu exceptie
    }
}
