package chess.androchess;

/**
 * Created by Brian on 12/10/2017.
 */

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


/**@author Jeff
 * @author Brian
 * Where chess is played. Moves are "translated" from raw input into numbers*/
public class Chess {
    public static Board board;

    public static void main (String [] args) throws IOException{

        PrintWriter writer = new PrintWriter("a.txt");
        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"saved");
        directory.mkdir();
        File recording = new File (directory.getAbsolutePath()+"/a.txt");
        newGame();
        boolean blackTurn = false;
        boolean drawAttempt=false;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            if (blackTurn) {
                System.out.print("Black's move: ");
            } else {
                System.out.print("White's move: ");
            }
            String input = scanner.nextLine();
            if (drawAttempt) {
                if (input.equals("draw")){
                    break;
                }
            }
            if (input.equals("resign")) {
                if (blackTurn) {
                    System.out.println("White wins");
                    break;
                } else {
                    System.out.println("Black wins");
                    break;
                }
            }
            if (board.move(input, blackTurn)){
                writer.println(input);
                if (board.checkmateDetected) {
                    break;
                }
                board.printBoard();
                blackTurn = !blackTurn;
                String inArr[] = input.split(" ");
                if (inArr.length == 3){
                    if (inArr[2].equals("draw?")) {
                        drawAttempt = true;
                    }
                }
            } else {
                invalidMove();
                continue;
            }
        }
        writer.close();
        System.out.println("Do you wish to save this game? (y/n)");
        String answer = scanner.nextLine();

        while(true){
            if (answer.equals("y")) {
                System.out.print("Save Recording As: ");
                File newFile = new File (scanner.nextLine().concat(".txt"));
                boolean success = recording.renameTo(newFile);

                while (!success) {
                    System.out.println("Error");
                    newFile = new File (scanner.nextLine().concat(".txt"));
                    success = recording.renameTo(newFile);
                }
                recording.renameTo(newFile);
                break;
            } else if (answer.equals("n")){
                System.out.println("Game Not Saved");
                break;
            }
            else {
                System.out.println("Invalid");
                answer = scanner.nextLine();
            }
        }
        recording.delete();
        scanner.close();
    }
    public static void newGame(){
        board = new Board();
        board.printBoard();
    }
    public static void Check(){
        System.out.println("Check");
        System.out.println("");
    }
    public static void Checkmate(){
        System.out.println("Checkmate");
        System.out.println("");
    }
    public static void Stalemate(){
        System.out.println("Stalemate");
        System.out.println("");
    }
    public static void invalidMove(){
        System.out.println("Illegal move, try again");
    }
}