package chess.androchess;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {
    Board currentGame;
    String selectedMove = "";
    boolean blackTurn;
    boolean gameStart = false;
    ImageView selector;
    FrameLayout selected;
    View movedPiece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginGame(View view){
        if (gameStart){
            clearBoard();
            currentGame = null;
        }
        gameStart = true;
        blackTurn = false;
        currentGame = new Board();
        selector = new ImageView(this);
        selector.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(),R.drawable.selection));
        createWhitePieces();
        createBlackPieces();
    }

    public void sendID(View view) {
        if (gameStart) {
            String coordinates = view.getTag().toString();
            FrameLayout current = findViewById(view.getId());
            if (selectedMove.length() < 2) {
                //Select
                selectedMove += coordinates;
                movedPiece = current.getChildAt(0);
                current.addView(selector);
                selected = current;
            } else {
                selectedMove += " " + coordinates;
                boolean turn = currentGame.move(selectedMove, blackTurn);
                if (turn) {
                    //Successful move
                    blackTurn = !blackTurn;
                    //Remove everything
                    selected.removeAllViews();
                    //Draw new piece
                    current.removeAllViews();
                    current.addView(movedPiece);
                    System.out.println("Successful Move");
                }
                else{
                    System.out.println("Failed Move");
                }

                //Unselect
                selected.removeView(selector);
                //reset selected move to empty
                selectedMove = "";
            }
        }
    }
    private void clearBoard(){
        int i;
        //Clear entire board
        TableRow zero = findViewById(R.id.zero);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)zero.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow one = findViewById(R.id.one);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)one.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow two = findViewById(R.id.two);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)two.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow three = findViewById(R.id.three);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)three.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow four = findViewById(R.id.four);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)four.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow five = findViewById(R.id.five);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)five.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow six = findViewById(R.id.six);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)six.getChildAt(i);
            temp.removeAllViews();
        }
        TableRow seven = findViewById(R.id.seven);
        for (i = 0; i < 8; i++){
            FrameLayout temp = (FrameLayout)seven.getChildAt(i);
            temp.removeAllViews();
        }
    }

    private void createWhitePieces(){
        FrameLayout a1, a2, b1, b2, c1, c2, d1, d2, e1, e2, f1, f2, g1, g2, h1, h2;

        a1 = findViewById(R.id.a1);
        ImageView Rookw1 = new ImageView(this);
        Rookw1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitecastle));
        a1.addView(Rookw1);

        b1 = findViewById(R.id.b1);
        ImageView Knightw1 = new ImageView(this);
        Knightw1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whiteknight));
        b1.addView(Knightw1);

        c1 = findViewById(R.id.c1);
        ImageView Bishopw1 = new ImageView(this);
        Bishopw1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitebishop));
        c1.addView(Bishopw1);

        d1 = findViewById(R.id.d1);
        ImageView Queenw = new ImageView(this);
        Queenw.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitequeen));
        d1.addView(Queenw);

        e1 = findViewById(R.id.e1);
        ImageView Kingw = new ImageView(this);
        Kingw.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whiteking));
        e1.addView(Kingw);

        f1 = findViewById(R.id.f1);
        ImageView Bishopw2 = new ImageView(this);
        Bishopw2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitebishop));
        f1.addView(Bishopw2);

        g1 = findViewById(R.id.g1);
        ImageView Knightw2 = new ImageView(this);
        Knightw2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whiteknight));
        g1.addView(Knightw2);

        h1 = findViewById(R.id.h1);
        ImageView Rookw2 = new ImageView(this);
        Rookw2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitecastle));
        h1.addView(Rookw2);
        
        a2 = findViewById(R.id.a2);
        ImageView Pawnw1 = new ImageView(this);
        Pawnw1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        a2.addView(Pawnw1);

        b2 = findViewById(R.id.b2);
        ImageView Pawnw2 = new ImageView(this);
        Pawnw2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        b2.addView(Pawnw2);

        c2 = findViewById(R.id.c2);
        ImageView Pawnw3 = new ImageView(this);
        Pawnw3.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        c2.addView(Pawnw3);

        d2 = findViewById(R.id.d2);
        ImageView Pawnw4 = new ImageView(this);
        Pawnw4.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        d2.addView(Pawnw4);

        e2 = findViewById(R.id.e2);
        ImageView Pawnw5 = new ImageView(this);
        Pawnw5.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        e2.addView(Pawnw5);

        f2 = findViewById(R.id.f2);
        ImageView Pawnw6 = new ImageView(this);
        Pawnw6.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        f2.addView(Pawnw6);

        g2 = findViewById(R.id.g2);
        ImageView Pawnw7 = new ImageView(this);
        Pawnw7.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        g2.addView(Pawnw7);

        h2 = findViewById(R.id.h2);
        ImageView Pawnw8 = new ImageView(this);
        Pawnw8.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.whitepawn));
        h2.addView(Pawnw8);
    }
    private void createBlackPieces(){
        FrameLayout a7, a8, b7, b8, c7, c8, d7, d8, e7, e8, f7, f8, g7, g8, h7, h8;

        a8 = findViewById(R.id.a8);
        ImageView Rookb1 = new ImageView(this);
        Rookb1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackcastle));
        a8.addView(Rookb1);

        b8 = findViewById(R.id.b8);
        ImageView Knightb1 = new ImageView(this);
        Knightb1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackknight));
        b8.addView(Knightb1);

        c8 = findViewById(R.id.c8);
        ImageView Bishopb1 = new ImageView(this);
        Bishopb1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackbishop));
        c8.addView(Bishopb1);

        d8 = findViewById(R.id.d8);
        ImageView Queenb = new ImageView(this);
        Queenb.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackqueen));
        d8.addView(Queenb);

        e8 = findViewById(R.id.e8);
        ImageView Kingb = new ImageView(this);
        Kingb.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackking));
        e8.addView(Kingb);

        f8 = findViewById(R.id.f8);
        ImageView Bishopb2 = new ImageView(this);
        Bishopb2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackbishop));
        f8.addView(Bishopb2);

        g8 = findViewById(R.id.g8);
        ImageView Knightb2 = new ImageView(this);
        Knightb2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackknight));
        g8.addView(Knightb2);

        h8 = findViewById(R.id.h8);
        ImageView Rookb2 = new ImageView(this);
        Rookb2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackcastle));
        h8.addView(Rookb2);

        a7 = findViewById(R.id.a7);
        ImageView Pawnb1 = new ImageView(this);
        Pawnb1.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        a7.addView(Pawnb1);

        b7 = findViewById(R.id.b7);
        ImageView Pawnb2 = new ImageView(this);
        Pawnb2.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        b7.addView(Pawnb2);

        c7 = findViewById(R.id.c7);
        ImageView Pawnb3 = new ImageView(this);
        Pawnb3.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        c7.addView(Pawnb3);

        d7 = findViewById(R.id.d7);
        ImageView Pawnb4 = new ImageView(this);
        Pawnb4.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        d7.addView(Pawnb4);

        e7 = findViewById(R.id.e7);
        ImageView Pawnb5 = new ImageView(this);
        Pawnb5.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        e7.addView(Pawnb5);

        f7 = findViewById(R.id.f7);
        ImageView Pawnb6 = new ImageView(this);
        Pawnb6.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        f7.addView(Pawnb6);

        g7 = findViewById(R.id.g7);
        ImageView Pawnb7 = new ImageView(this);
        Pawnb7.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        g7.addView(Pawnb7);

        h7 = findViewById(R.id.h7);
        ImageView Pawnb8 = new ImageView(this);
        Pawnb8.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), R.drawable.blackpawn));
        h7.addView(Pawnb8);
    }
}
