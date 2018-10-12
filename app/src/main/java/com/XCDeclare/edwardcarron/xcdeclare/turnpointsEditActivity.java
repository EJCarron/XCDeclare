package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class turnpointsEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnpoints_edit);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final int numTurnPoints = declaration.turnpointsRefs.length;

        LinearLayout turnpointEditsLayout = findViewById(R.id.turnpointEditsLayout);

        final EditText[] edits = new EditText[numTurnPoints];


        for (int i = 0 ; i < numTurnPoints ; i++){

            EditText edit = makeEditText(declaration.turnpointsRefs[i], i);

            edits[i] = edit;

            turnpointEditsLayout.addView(edit);

        }


        View.OnClickListener editTurnsConfirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] newTurns = new String[numTurnPoints];

                for (int j = 0 ; j < numTurnPoints ; j++){

                    newTurns[j] = edits[j].getText().toString();

                }

                declaration.setTurnpointsRefs(newTurns);

                Intent intent = new Intent(turnpointsEditActivity.this, DeclareActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);

            }
        };

        Button editTurnsConfirmBtn = findViewById(R.id.turnpointsEditConfirmBtn);

        editTurnsConfirmBtn.setOnClickListener(editTurnsConfirmBtnClicked);

    }

    private EditText makeEditText(String text, int id){

        EditText edit = new EditText(this);

        edit.setText(text);
        edit.setId(id);
        edit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return edit;
    }
}
