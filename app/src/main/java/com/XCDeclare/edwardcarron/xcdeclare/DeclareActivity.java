package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeclareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        LinearLayout layout = findViewById(R.id.DeclareLayout);



        //------------------ Building Page --------------------------------

        TextView BHPAView = findViewById(R.id.BHPAView);
        BHPAView.setText(declaration.BHPANum);

        TextView startView = findViewById(R.id.startGridView);
        startView.setText(declaration.startRef);

        LinearLayout turnsLayout = findViewById(R.id.TurnpointLayout);

        for(int i = 0; i < declaration.turnpointsRefs.length ; i++){

            TextView turn = makeTextView(declaration.turnpointsRefs[i]);

            turnsLayout.addView(turn);

        }

        TextView finishView = findViewById(R.id.finishGridView);
        finishView.setText(declaration.finishRef);


        LinearLayout otherBHPAsLayout = findViewById(R.id.otherBHPALayout);

        for(int j = 0; j < declaration.otherBHPAs.length; j++){

            TextView BHPA = makeTextView(declaration.otherBHPAs[j]);

            otherBHPAsLayout.addView(BHPA);
        }

        //-----------------------------------------------------------------------------


        //-----------------------------Setting Edit Buttons ------------------------------

        View.OnClickListener BHPAEditBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DeclareActivity.this, BHPAEditActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button BHPAEditBtn = findViewById(R.id.BHPAEditBtn);

        BHPAEditBtn.setOnClickListener(BHPAEditBtnClicked);

        View.OnClickListener startEditBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclareActivity.this, StartEditActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button startEditBtn = findViewById(R.id.startEditBtn);
        startEditBtn.setOnClickListener(startEditBtnClicked);


        View.OnClickListener turnpointsEditBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclareActivity.this, turnpointsEditActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button turnsEditBtn = findViewById(R.id.turnsEditBtn);

        turnsEditBtn.setOnClickListener(turnpointsEditBtnClicked);


        View.OnClickListener finishEditBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclareActivity.this, finishEditActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button finishEditBtn = findViewById(R.id.finishEditBtn);
        finishEditBtn.setOnClickListener(finishEditBtnClicked);

        View.OnClickListener otherBHPAsEditBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeclareActivity.this, EditOtherBHPAsActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button otherBHPAsEditBtn = findViewById(R.id.otherBHPAEditBtn);
        otherBHPAsEditBtn.setOnClickListener(otherBHPAsEditBtnClicked);

        //-----------------------------------------------------------------------------------------------

        //-------------------------- Declaring ------------------------------------------------------

        View.OnClickListener declareBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String error = "";

                //Checking if BHPA number is there
                if (error.equals("")) {

                    if (declaration.BHPANum.length() == 0) {

                        error = getString(R.string.noBHPAWarning);

                    }

                }

                // Checking if legal triangle
                if (error.equals("")) {

                    if (declaration.turnpointsRefs.length == 2) {

                        if (!declaration.isLegalTriangle()) {

                            error = getString(R.string.notLegalTriangle);

                        }
                    }
                }


                if (error.equals("")) {

                    declare(declaration);
                }

                if (!error.equals("")) {


                    if (error.equals(getString(R.string.notLegalTriangle))) {


                        openNotLegalTriangleDialogue(declaration);


                    } else {

                        openWarningMessageBox(error);
                    }

                }

            }
        };

        Button declareBtn = findViewById(R.id.declareBtn);

        declareBtn.setOnClickListener(declareBtnClicked);


    }

    public void openNotLegalTriangleDialogue(final Declaration declaration){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.notLegalTriangle);
        builder.setMessage(R.string.notLegalTriangleMessage);
        builder.setPositiveButton(R.string.makeChange, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                declaration.makeStartFinishMatch();

                recreate();

            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });
        builder.setNeutralButton(R.string.declare, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DeclareActivity.this.declare(declaration);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    public void declare(Declaration declaration){

        Log.i("Send email", "");
        String[] TO = {declaration.getDeclarationEmail()};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Flight Declaration");
        emailIntent.putExtra(Intent.EXTRA_TEXT, declaration.renderDeclarationEmail());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DeclareActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }



    }

    public void openWarningMessageBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.warning);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }


    private TextView makeTextView(String text){

        TextView view = new TextView(this);

        view.setText(text);
        view.setTextColor(Color.BLACK);
        //view.setId(id);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return view;

    }

//    public void BHPAEditBtn_Clicked(){}
//
//    public void startEditBtn_Clicked(){}
//
//    public void turnsEditBtn_Clicked(){}
//
//    public void finishEditBtn_Clicked(){}
//
//    public void otherBHPAsEditBtn_Clicked(){}







//    private EditText[] makeArrayEdits(String[] texts, int idStart){
//
//        EditText[] edits = new EditText[texts.length];
//
//        for(int i = 0; i < texts.length; i++){
//
//            EditText edit = makeEditText(texts[i],(idStart + i));
//
//            edits[i] = edit;
//        }
//        return  edits;
//
//    }

    private Button makeButton(String text, int id, View.OnClickListener listener){

        Button button = new Button(this);

        button.setText(text);
        button.setId(id);
        button.setOnClickListener(listener);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return button;
    }

}

