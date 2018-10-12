package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

public class EditOtherBHPAsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_other_bhpas);


        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final int numBHPAs = declaration.otherBHPAs.length;

        LinearLayout otherBHPAsLayout = findViewById(R.id.otherBhpasEditsLayout);

        final EditText[] edits = new EditText[numBHPAs];

        for (int i = 0 ; i < numBHPAs ; i++){

            EditText edit = makeEditNumber(declaration.otherBHPAs[i], i);

            edits[i] = edit;

            otherBHPAsLayout.addView(edit);

        }

        View.OnClickListener confirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List notEmptyBhpas = new LinkedList();

                for(int k = 0 ; k < numBHPAs; k++){

                    String Bhpa = edits[k].getText().toString();

                    if(Bhpa.length() > 0 ){

                        notEmptyBhpas.add(Bhpa);

                    }
                }

                if (notEmptyBhpas.size() == 0){

                    notEmptyBhpas.add("");
                }

                String[] newBHPAs = new String[notEmptyBhpas.size()];

                for(int x = 0 ; x < notEmptyBhpas.size(); x++){

                    newBHPAs[x] = notEmptyBhpas.get(x).toString();


                }



                declaration.setOtherBHPAs(newBHPAs);

                Intent intent = new Intent(EditOtherBHPAsActivity.this, DeclareActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);

            }
        };

        Button confirmBtn = findViewById(R.id.confirmEditOtherBhpasBtn);
        confirmBtn.setOnClickListener(confirmBtnClicked);


        View.OnClickListener addOtherBhpaBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditOtherBHPAsActivity.this, AddOtherBHPAActivity.class );

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button addOtherBhpaBtn = findViewById(R.id.addOtherBhpaBtn);
        addOtherBhpaBtn.setOnClickListener(addOtherBhpaBtnClicked);


    }



    private EditText makeEditNumber(String text, int id){

        EditText edit = new EditText(this);

        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit.setText(text);
        edit.setId(id);
        edit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return edit;
    }
}
