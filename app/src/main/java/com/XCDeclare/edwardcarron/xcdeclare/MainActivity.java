package com.XCDeclare.edwardcarron.xcdeclare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback{

    private View mainLayout ;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        String defaultEmail = getResources().getString(R.string.declareEmailAddressDefault);
        String declareAddress = sharedPref.getString(getString(R.string.declareEmailKey), defaultEmail);

        String defaultFilePath = getResources().getString(R.string.filePathDefault);
        String filePath = sharedPref.getString(getString(R.string.filePathKey), defaultFilePath);

        String defaultBHPA = "";
        String BHPA = sharedPref.getString(getString(R.string.BHPAKey), defaultBHPA);

        EditText emailEdit = findViewById(R.id.declareAddressEdit);
        EditText filePathEdit = findViewById(R.id.filePathInput);
        EditText BHPAEdit = findViewById(R.id.BHPANumInput);


        emailEdit.setText(declareAddress);
        filePathEdit.setText(filePath);

        if(BHPA.equals("")){

        }else{
            BHPAEdit.setText(BHPA);

        }


    }

    public void openErrorMessageBox(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.error);
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

    public void savePreferences(){

        EditText BHPANumInput = findViewById(R.id.BHPANumInput);
        String BHPA =  BHPANumInput.getText().toString();

        EditText emailEdit = findViewById(R.id.declareAddressEdit);
        String email = emailEdit.getText().toString();

        EditText filePathEdit = findViewById(R.id.filePathInput);
        String filePath = filePathEdit.getText().toString();


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPref.edit();

        editor.putString(getString(R.string.BHPAKey), BHPA);
        editor.putString(getString(R.string.declareEmailKey),email);
        editor.putString(getString(R.string.filePathKey), filePath);

        editor.commit();


    }



    public void parseBtnClicked(View view) throws IOException, XmlPullParserException {

        EditText BHPANumInput = findViewById(R.id.BHPANumInput);
        EditText emailEdit = findViewById(R.id.declareAddressEdit);


       String flightXml = "";
        List<FlightPoint> flightPoints ;
       String error = "";

       Declaration declaration = new Declaration(BHPANumInput.getText().toString());





        String emailAddress = emailEdit.getText().toString();

        declaration.setDeclarationEmail(emailAddress);

       if(error.equals("")){


           // Fetch Default.tsk
           flightXml = fetchFlightDetailData();


           if(flightXml == null){
               error = getString(R.string.fileNotFound);
           }

       }


       if(error.equals("")) {


           // parse Default.tsk

           flightPoints = parse(flightXml);

           if(flightPoints == null){
               error = getString(R.string.parserException);
           }else{


               if(flightPoints.size() == 0){

                   error = getString(R.string.noFlightData);

               }else {

                   String[] turnRefs = new String[flightPoints.size() - 2];


                   int turnCount = 0;

                   for (FlightPoint flightPoint : flightPoints) {

                       if (flightPoint.type.equals("Start")) {
                           declaration.setStartRef(flightPoint.getGridReference());
                       } else if (flightPoint.type.equals("Finish")) {
                           declaration.setFinishRef(flightPoint.getGridReference());
                       } else {
                           turnRefs[turnCount] = flightPoint.getGridReference();
                           turnCount++;
                       }

                   }

                   declaration.setTurnpointsRefs(turnRefs);

                   String[] otherBHPAs = new String[]{""};

                   declaration.setOtherBHPAs(otherBHPAs);


                   Intent intent = new Intent(this, DeclareActivity.class);

                   intent.putExtra("declarationParcel", declaration);


                   startActivity(intent);
               }
           }


       }

       if(error.equals("")){

           savePreferences();
       }

       if(!error.equals("")){

             openErrorMessageBox(error);
       }



    }




    private void requestReadExternalStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Snackbar.make(mainLayout, R.string.requestStorageReadExplanation,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }
    }


    private String fetchFlightDetailData(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission();
        }


        EditText filePathEdit = findViewById(R.id.filePathInput);

        String  filePath = filePathEdit.getText().toString();

        File DefaultTsk = new File(filePath);

        StringBuilder text = new StringBuilder();

        if (DefaultTsk.exists()) {


            try {


                BufferedReader br = new BufferedReader(new FileReader(DefaultTsk));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            } catch (IOException e) {
                System.out.println(e.toString());


            }


        }else{


          return null;

        }
        return text.toString();
    }



//


    private static final String ns = null;

    public List parse(String flightXml) { //throws XmlPullParserException, IOException {

        InputStream in = null;

        try {
            in = new ByteArrayInputStream(flightXml.getBytes());
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTask(parser);
        } catch( XmlPullParserException e){

        } catch (IOException e){

        }
        finally {

            if(in != null) {

                try {
                    in.close();
                }catch (IOException e){

                }
            }
        }

        return null;
    }

    private List readTask(XmlPullParser parser) throws XmlPullParserException, IOException {
        List points = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Task");  // skip to the next opening 'task' tag
        while (parser.next() != XmlPullParser.END_TAG) {    // itterate thru till passed the closing 'task' tag
            if (parser.getEventType() != XmlPullParser.START_TAG) { // ??????
                continue;
            }
            String name = parser.getName(); // name of the current child tag of current task tag
            // Starts by looking for the entry tag
            if (name.equals("Point")) { // if the child is a 'point' tag
                points.add(readPoint(parser));  //
            } else {
                skip(parser);
            }
         }
        return points;
    }

    private FlightPoint  readPoint(XmlPullParser parser) throws XmlPullParserException, IOException{

        parser.require(XmlPullParser.START_TAG, ns, "Point"); // throws an error if not at a Point opening tag?

        String type = parser.getAttributeValue(null, "type");
        String gridReference = null;

        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("Waypoint")){
                gridReference = readWaypoint(parser);

            }else{
                skip(parser);
            }

        }

        FlightPoint point = new FlightPoint(type, gridReference);

        return point;
    }

    private String readWaypoint(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Waypoint");

        String gridReference = null;
        int count = 0;
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("Location")){
                gridReference = readLocation(parser);

            }else{
                skip(parser);
            }
            count++;
        }

        return gridReference;

    }


    private String readLocation(XmlPullParser parser) throws XmlPullParserException, IOException{


        String latitude =  parser.getAttributeValue(null, "latitude");
        String longitude = parser.getAttributeValue(null, "longitude");

        parser.require(XmlPullParser.START_TAG, ns, "Location");


        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }else{



            }
        }

        String gridReference = latitude + " " + longitude;

        return  gridReference;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
