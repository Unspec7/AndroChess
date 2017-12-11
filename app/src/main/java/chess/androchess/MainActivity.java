package chess.androchess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {
    Chess currentGame;
    String selectedMove = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginGame(){
        currentGame = new Chess();
    }

    public void sendID(View view) {
        String coordinates = view.getTag().toString();
        if (selectedMove.length() < 2){
            selectedMove += coordinates;
        }
        else{
            selectedMove += " " + coordinates;

        }

    }
}
