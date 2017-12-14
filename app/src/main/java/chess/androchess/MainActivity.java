package chess.androchess;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Board currentGame;
    Board undo;

    String selectedMove = "";
    String savename = "";
    String loadname = "";

    int first;
    int second;
    int replayIndex;

    boolean blackTurn;
    boolean gameStart = false;
    boolean drawOffered;
    boolean drawAccepted;
    boolean undone;
    boolean resigned;
    boolean replayStarted = false;
    boolean firstMove;

    FrameLayout selected;

    View movedPiece;

    TextView turnCountText;
    TextView displayedMessage;

    ImageView firstPiece;
    ImageView secondPiece;
    ImageView selector;

    String moves = "";
    String undoMoves = "";
    String [] replayMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turnCountText = findViewById(R.id.turnCounter);
        displayedMessage = findViewById(R.id.message);
    }

    public void beginGame(View view) {
        clearBoard();
        currentGame = null;
        savename = "";
        moves = "";
        resigned = false;
        gameStart = true;
        blackTurn = false;
        gameStart = true;
        drawOffered = false;
        drawAccepted = false;
        replayStarted = false;
        undone = true;
        displayedMessage.setText("");
        firstMove = false;
        Button replayMove = findViewById(R.id.replayMove);
        replayMove.setVisibility(View.GONE);

        //Creating game objects
        currentGame = new Board();
        undo = new Board();

        //Create selector image
        selector = new ImageView(this);
        selector.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(),R.drawable.selection));

        //Create all pieces
        createWhitePieces();
        createBlackPieces();
        setTurnCount();

    }


    public void setTurnCount(){
        if (!currentGame.checkmateDetected){
            if (drawAccepted){
                turnCountText.setText(getString(R.string.draw));
                gameStart = false;
                askSave();
            }
            else if (currentGame.check){
                displayedMessage.setText(getString(R.string.check));
            }
            else if (resigned) {
                displayedMessage.setText(getString(R.string.resigned));
                gameStart = false;
                askSave();
            }
            else{
                if (blackTurn){
                    turnCountText.setText(getString(R.string.blackTurn));
                }
                else{
                    turnCountText.setText(getString(R.string.whiteTurn));
                }
            }
        }
        else{//Set winner
            gameStart = false;
            turnCountText.setText(getString(currentGame.winner));
            displayedMessage.setText(getString(R.string.checkmate));
            askSave();
        }
    }

    public void askSave(){
        if (!replayStarted) {
            getNameSave();
        }
        else{
            replayStarted = false;
        }
    }

    public void promote() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.activity_main, null);
        b.setTitle("Promote: ");
        final Spinner promotions = (Spinner) mView.findViewById(R.id.promotions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.promotions));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        promotions.setAdapter(adapter);

        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!promotions.getSelectedItem().toString().equalsIgnoreCase("")) {

                }
            }
        });
        b.show();
    }

    public void copytoUndo() throws IOException, ClassNotFoundException{
        //Copy
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(currentGame);
        oos.flush();
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        //Paste
        ObjectInputStream ois = new ObjectInputStream(bais);
        undo = (Board)ois.readObject();
    }

    public void copytoCurrent() throws IOException, ClassNotFoundException{
        //Copy
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(undo);
        oos.flush();
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        //Paste
        ObjectInputStream ois = new ObjectInputStream(bais);
        currentGame = (Board)ois.readObject();
    }

    public void AI(View view){
        if (gameStart){
            if (!firstMove){
                currentGame.randomMove(blackTurn);
            }
            else{
                Toast.makeText(this, "Please unselect your piece", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void undo(View view) {
        if (gameStart) {
            if (!undone) {//Make sure you're only undoing one move
                //Return turn back to past turn
                blackTurn = !blackTurn;
                setTurnCount();
                moves = undoMoves;

                //Paste old board state
                try {
                    copytoCurrent();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }

                //Restore pieces back to old values
                FrameLayout oldFirst = findViewById(first);
                FrameLayout oldSecond = findViewById(second);
                oldSecond.removeView(firstPiece);
                oldFirst.addView(firstPiece);

                //Checking if the square it went to was originally empty
                if (secondPiece != null) {
                    oldFirst.removeView(secondPiece);
                    oldSecond.addView(secondPiece);
                } else {
                    oldSecond.removeAllViews();
                }
            }
            undone = true;
        }
    }

    public void replayMove(View view) {
        if (replayIndex < replayMoves.length){
            oneStep(replayMoves[replayIndex]);
            replayIndex++;
        }
    }

    public void sendID(View view) {
        if (gameStart) {
            String coordinates = view.getTag().toString();
            FrameLayout current = findViewById(view.getId());

            //If first selection
            if (selectedMove.length() < 2) {
                //Select
                firstMove = true;
                selectedMove += coordinates;
                movedPiece = current.getChildAt(0);
                current.addView(selector);
                selected = current;
            } else {//If second selection
                firstMove = false;
                selectedMove += " " + coordinates;
                try{
                    //Copy board state for undo
                    copytoUndo();
                }
                catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }

                //Move the piece
                boolean turn = currentGame.move(selectedMove, blackTurn);
                if (turn) {
                    //Successful move
                    //Used for undo
                    first = selected.getId();
                    second = current.getId();
                    firstPiece = (ImageView)selected.getChildAt(0);

                    //Checking if the square its moving to is empty or not for undo
                    if (current.getChildCount() != 0) {
                        secondPiece = (ImageView) current.getChildAt(0);
                    }
                    else{
                        secondPiece = null;
                    }

                    //Remove everything in the square its moving from
                    selected.removeAllViews();

                    //Draw new piece in the selected square
                    current.removeAllViews();
                    current.addView(movedPiece);

                    //Change turn
                    blackTurn = !blackTurn;
                    setTurnCount();

                    //Set variables
                    undone = false;
                    drawOffered = false;
                    if (gameStart) {
                        displayedMessage.setText("");
                    }
                    undoMoves = moves;
                    moves+=(selectedMove+"\r");

                }
                else{
                    Toast.makeText(this, "Illegal move", Toast.LENGTH_SHORT).show();
                }

                //Unselect
                selected.removeView(selector);
                //reset selected move to empty
                selectedMove = "";
            }
        }
    }

    public void oneStep(String myMove){
        if (myMove.substring(0, 2).equals("dr")){
            //Draw
            drawAccepted = true;
            displayedMessage.setText("");
            Button replayMove = findViewById(R.id.replayMove);
            replayMove.setVisibility(View.GONE);
            setTurnCount();
        }
        else if (myMove.substring(0, 2).equals("re")){
            //Resigned
            if (blackTurn){
                //White win
                turnCountText.setText(getString(R.string.whiteWin));
            }
            else{
                //Black win
                turnCountText.setText(getString(R.string.blackWin));
            }
            gameStart = false;
            resigned = true;
            Button replayMove = findViewById(R.id.replayMove);
            replayMove.setVisibility(View.GONE);
            setTurnCount();
        }
        else {
            boolean turn = currentGame.move(myMove, blackTurn);
            if (turn) {
                //Successful move
                String firstBox = myMove.substring(0, 2);
                String secondBox = myMove.substring(3);
                FrameLayout oldBox = findFrame(firstBox);
                FrameLayout newBox = findFrame(secondBox);
                //Remove everything in the square its moving from
                View oldPiece = oldBox.getChildAt(0);
                oldBox.removeAllViews();

                //Draw new piece in the selected square
                newBox.removeAllViews();
                newBox.addView(oldPiece);

                //Change turn
                blackTurn = !blackTurn;
                setTurnCount();

                //Set variables
                if (gameStart) {
                    displayedMessage.setText("");
                }
            }
        }
    }

    public FrameLayout findFrame(String myTag){
        TableRow myFrame;
        myFrame = findViewById(R.id.zero);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.one);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.two);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.three);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.four);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.five);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.six);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }
        myFrame = findViewById(R.id.seven);
        for (int i = 0; i < 8; i++){
            if (myFrame.getChildAt(i).getTag().toString().equals(myTag)){
                return (FrameLayout)myFrame.getChildAt(i);
            }
        }

        return null;
    }

    private void getNameSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savename = input.getText().toString()+".txt";

                saveGame();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void saveGame() {
        String dirName = "saved";
        File dir = new File(dirName);
        dir.mkdir();
        Toast.makeText(this, "Game saved as: " + savename, Toast.LENGTH_SHORT).show();
        String filename = savename;
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(moves.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(getFilesDir() + "/" + filename);
        System.out.println(file.getAbsolutePath());
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

    public void draw(View view){
        if (gameStart) {
            if (drawOffered) {
                drawAccepted = true;
                gameStart = false;
                moves += "dr aw" + "\r";
            } else {
                displayedMessage.setText(getString(R.string.offerDraw));
                drawOffered = true;
            }
            setTurnCount();
        }
    }

    public void resign(View view){
        if (gameStart) {
            if (blackTurn) {
                //White win
                turnCountText.setText(getString(R.string.whiteWin));
            } else {
                //Black win
                turnCountText.setText(getString(R.string.blackWin));
            }
            gameStart = false;
            resigned = true;
            moves += "re sn" + "\r";
            setTurnCount();
        }
    }

    private void setNameLoad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Load");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadname = input.getText().toString()+".txt";
                //HERE1
                loader();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadname = "";
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void loadReplay(View view) {
        loadname = "";
        replayMoves = new String[0];
        setNameLoad();
        System.out.println(loadname);
    }
    private void loader() {
        Board loaded = new Board();
        File load = new File ("/data/user/0/chess.androchess/files/"+loadname);
        System.out.println(load.getAbsolutePath());
        try (FileInputStream fis = new FileInputStream(load)) {
            Button replayMove = findViewById(R.id.replayMove);
            replayMove.setVisibility(View.VISIBLE);

            Toast.makeText(this, loadname + " loaded", Toast.LENGTH_SHORT).show();
            displayedMessage.setText("");
            replayStarted = true;
            resigned = false;
            drawAccepted = false;
            drawOffered = false;
            undone = true;
            gameStart = false;
            blackTurn = false;
            firstMove = false;
            replayIndex = 0;
            String input = "";
            char content;
            while ((content = (char)fis.read()) != -1) {

                if (content == '\uFFFF') {
                    break;
                }
                //JEFF LOOK HERE
                input+=Character.toString(content);
                /*if (content == '\r') {
                    System.out.println(input);
                    loaded.move(input, blackTurn);

                    blackTurn = !blackTurn;
                }*/
            }

            replayMoves = input.split("\r");
            clearBoard();
            createWhitePieces();
            createBlackPieces();
            currentGame = new Board();
            setTurnCount();

        } catch (IOException e) {
            Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show();
            Button replayMove = findViewById(R.id.replayMove);
            replayMove.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }
}