package game;

import game.Game;
import javafx.util.Pair;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private List<Game> games = new ArrayList<>();

    public void addGame(Game game){
        games.add(game);
    }

    public Game getGame(String s){
        for (Game g : games){
            if(g.getName().equals(s)){
                return g;
            }
        }
        return null;
    }

    public void solveRequest(String s, Socket socket) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        if (s.startsWith("create game ")){
            String game = s.substring((new String("create game ")).length());
            addGame(new Game(game, socket));
            out.println("Successfully made a game");
            return;
        }
        else if (s.startsWith("join game ")){
            String game = s.substring((new String("join game ")).length());
            synchronized(this) {
                if (getGame(game) != null && getGame(game).addPlayer2(socket)){
                    out.println("Joined Game");
                }
                else {
                    out.println("Game is already full");
                }
            }
            return;
        }
        else if (s.startsWith("submit move ")){
            String game = s.substring((new String("submit move ")).length());
            String move;
            try{
                move = game.split(" ")[1];
            }
            catch (ArrayIndexOutOfBoundsException e){
                move = "_";
            }
            game = game.split(" ")[0];
            synchronized (this){
                if (getGame(game) != null){
                    Pair<String, String> pair = getGame(game).attemptMove(socket, move);

                    //Send message to the initiator
                    out.println(pair.getKey());

                    //Print message to the other
                    if (!pair.getValue().equals("")) {
                        Socket other = getGame(game).getOtherSocket(socket);
                        if (other != null){
                            PrintWriter out2 = new PrintWriter(other.getOutputStream(), true);
                            out2.println(pair.getValue());
                        }
                    }
                }
                else{
                    out.println("No such game exists");
                }
            }
            return;
        }
        out.println("Unknown command");
    }
}
