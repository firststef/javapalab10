package game;

import com.jcraft.jsch.*;
import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {
    private Board board = new Board();

    private Socket socket1;
    private Player player1;

    private Socket socket2;
    private Player player2;
    private String name;

    private int currentTurn = 1;
    private int turnNumber = 0;
    private boolean gameEnded = false;

    Game(String name, Socket s1){
        this.name = name;
        this.socket1 = s1;
        this.player1 = new Player();
    }

    boolean addPlayer2(Socket s2){
        if (this.player2 == null) {
            this.socket2 = s2;
            this.player2 = new Player();
            return true;
        }
        else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public Pair<String, String> attemptMove(Socket socket, String move){
        if (gameEnded){
            return new Pair<>("The game has ended", "The game has ended");
        }
        else if (socket2 != null){
            if (socket == socket1 && currentTurn == 1){
                currentTurn = 2;
                turnNumber++;
                board.setAt((int)Math.floor(Math.random() * 19), (int)Math.floor(Math.random() * 19),'W');
                if (turnNumber >= 2){
                    gameEnded = true;

                    uploadFile();
                }
                return new Pair<>("Accepted move", "Other player has moved " + move);
            }
            else if (socket == socket2 && currentTurn == 2){
                currentTurn = 1;
                board.setAt((int)Math.floor(Math.random() * 19), (int)Math.floor(Math.random() * 19),'B');
                return new Pair<>("Accepted move", "Other player has moved " + move);
            }
            else{
                return new Pair<>("Not your turn", "");
            }
        }
        else {
            return new Pair<>("Game not started yet", "");
        }
    }

    public Socket getOtherSocket(Socket socket){
        if (socket == socket1){
            return socket2;
        }
        else {
            return socket1;
        }
    }

    private void uploadFile(){
        try{
            System.out.println("Connecting to server");
            String password = new String(Files.readAllBytes(Paths.get("password.txt")), StandardCharsets.UTF_8);
            String html = "<!doctype html><head><title>Board</title></head><body>" +
                    board.toString().replaceAll("\n", "<br>") + "</body>";
            Files.write(Paths.get("./game.html"), html.getBytes());

            JSch jsch = new JSch();
            Session session = null;
            session = jsch.getSession("stefan.petrovici", "fenrir.info.uaic.ro", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put("./game.html", "game.html", ChannelSftp.OVERWRITE);
            sftpChannel.exit();
            session.disconnect();

            System.out.println("Uploaded file read");
        }
        catch (IOException ignored){
            System.out.println("Failed read");
        }
        catch (JSchException | SftpException ignored){
            System.out.println("Failed upload");
        }
    }
}
