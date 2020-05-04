package game;

public class Matrix<T> {
    private int l;
    private int c;
    private T[] values;

    Matrix(int lines, int columns, T defaultValue){
        l = lines;
        c = columns;

        values = (T[]) new Object[l * c];
        for (int i = 0; i < l * c; i++){
            values[i] = defaultValue;
        }
    }

    public boolean setAt(int line, int col, T value){
        try{
            values[line * c + col] = value;
        }
        catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
        return true;
    }

    public T getAt(int line, int col){
        if (line >= l || col >= c)
            throw new IndexOutOfBoundsException("");
        return values[line * c + col];
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < l; i++){
            for (int j = 0; j < c; j++){
                str.append(values[i * c + j].toString()).append(',');
            }
            str.append('\n');
        }
        return str.toString();
    }
}
