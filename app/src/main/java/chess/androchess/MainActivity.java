package chess.androchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Board currentGame;
    String selectedMove = "";
    boolean blackTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginGame(View view){
        currentGame = new Board();
    }

    public void sendID(View view) {
        String coordinates = view.getTag().toString();
        if (selectedMove.length() < 2){
            selectedMove += coordinates;
        }
        else{
            selectedMove += " " + coordinates;
            boolean turn = currentGame.move(selectedMove, blackTurn);
            if (turn){
                //Successful move
                blackTurn = !blackTurn;
            }
            //reset selected move to empty
            selectedMove = "";
        }
    }
}
