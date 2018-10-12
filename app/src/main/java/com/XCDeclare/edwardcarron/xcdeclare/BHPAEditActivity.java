package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BHPAEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhpaedit);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final EditText BHPAInput = findViewById(R.id.BHPAInput);
        BHPAInput.setText(declaration.GetBHPANum());

        View.OnClickListener confirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                declaration.setBHPANum(BHPAInput.getText().toString());

                Intent intent = new Intent(BHPAEditActivity.this, DeclareActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button confirmBtn = findViewById(R.id.BHPAEditConfirmBtn);

        confirmBtn.setOnClickListener(confirmBtnClicked);

    }








}
