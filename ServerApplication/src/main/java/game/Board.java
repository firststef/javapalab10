package game;

public class Board {
    private Matrix<Character> matrix = new Matrix<>(19, 19, '0');

    public Character getAt(int line, int col){
        return matrix.getAt(line, col);
    }

    public boolean setAt(int line, int col, Character c){
        return matrix.setAt(line, col, c);
    }

    @Override
    public String toString(){
        return matrix.toString();
    }
}
