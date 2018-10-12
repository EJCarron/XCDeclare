package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_edit);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final EditText startInput = findViewById(R.id.startEditInput);
        startInput.setText(declaration.startRef);

        View.OnClickListener confirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                declaration.setStartRef(startInput.getText().toString());

                Intent intent = new Intent(StartEditActivity.this, DeclareActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button confirmBtn = findViewById(R.id.startEditConfirmBtn);

        confirmBtn.setOnClickListener(confirmBtnClicked);
    }
}
