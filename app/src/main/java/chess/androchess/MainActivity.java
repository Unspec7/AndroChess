package chess.androchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {
    Board currentGame;
    String selectedMove = "";
    boolean blackTurn = false;
    boolean gameStart = false;
    TableRow a1,a2,a3,a4,a5,a6,a7,a8,
             b1,b2,b3,b4,b5,b6,b7,b8,
             c1,c2,c3,c4,c5,c6,c7,c8,
             d1,d2,d3,d4,d5,d6,d7,d8,
             e1,e2,e3,e4,e5,e6,e7,e8,
             f1,f2,f3,f4,f5,f6,f7,f8,
             g1,g2,g3,g4,g5,g6,g7,g8,
             h1,h2,h3,h4,h5,h6,h7,h8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginGame(View view){
        gameStart = true;
        currentGame = new Board();
    }

    public void sendID(View view) {
        if (gameStart) {
            String coordinates = view.getTag().toString();
            if (selectedMove.length() < 2) {
                //Select
                selectedMove += coordinates;
                System.out.println(coordinates);
            } else {
                selectedMove += " " + coordinates;
                System.out.println(coordinates);
                boolean turn = currentGame.move(selectedMove, blackTurn);
                if (turn) {
                    //Successful move
                    blackTurn = !blackTurn;
                }
                //reset selected move to empty
                selectedMove = "";
                //Unselect
            }
        }
    }
}
