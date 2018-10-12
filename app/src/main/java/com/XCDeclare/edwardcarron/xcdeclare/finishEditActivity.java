package com.XCDeclare.edwardcarron.xcdeclare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class finishEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_edit);

        Intent intent = getIntent();

        final Declaration declaration = intent.getParcelableExtra("declarationParcel");

        final EditText finishInput = findViewById(R.id.finishRefEdit);
        finishInput.setText(declaration.finishRef);

        View.OnClickListener confirmBtnClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                declaration.setFinishRef(finishInput.getText().toString());

                Intent intent = new Intent(finishEditActivity.this, DeclareActivity.class);

                intent.putExtra("declarationParcel", declaration);

                startActivity(intent);
            }
        };

        Button confirmBtn = findViewById(R.id.finishRefEditConfirmBtn);

        confirmBtn.setOnClickListener(confirmBtnClicked);
    }
}
