package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddOtherBHPAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_bhpa);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final EditText newBhpaEdit = findViewById(R.id.newBhpaEdit);

        View.OnClickListener addBhpaConfirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newBhpa = newBhpaEdit.getText().toString();

                if(newBhpa.length() > 0){

                    int oldNumBhpas = declaration.otherBHPAs.length;

                    String[] newOtherBhpas = new String[ oldNumBhpas + 1];

                    for(int i = 0; i < oldNumBhpas; i++){


                        newOtherBhpas[i] = declaration.otherBHPAs[i];

                    }

                    newOtherBhpas[oldNumBhpas] = newBhpa;

                    declaration.setOtherBHPAs(newOtherBhpas);
                }

                Intent intent = new Intent(AddOtherBHPAActivity.this, EditOtherBHPAsActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);

            }
        };

        Button addBhpaConfirmBtn = findViewById(R.id.confirmAddBhpaBtn);

        addBhpaConfirmBtn.setOnClickListener(addBhpaConfirmBtnClicked);

    }
}
