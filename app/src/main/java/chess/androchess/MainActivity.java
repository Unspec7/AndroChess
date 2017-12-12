package chess.androchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Board currentGame;
    String selectedMove = "";
    boolean blackTurn = false;
    boolean gameStart = false;

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
