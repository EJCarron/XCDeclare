package com.XCDeclare.edwardcarron.xcdeclare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by edwardcarron on 26/03/2018.
 */

public class Declaration implements Parcelable {

    String BHPANum;
    String startRef;
    String[] turnpointsRefs;
    String finishRef;
    String[] otherBHPAs;
    String declarationEmail;

    public Declaration(
            String BHPANum,
            String startRef,
            String[] turnpointsRefs,
            String finishRef,
            String[] otherBHPAs,
            String declarationEmail
            ){
        this.BHPANum = BHPANum;
        this.startRef = startRef;
        this.turnpointsRefs = turnpointsRefs;
        this.finishRef = finishRef;
        this.otherBHPAs = otherBHPAs;
        this.declarationEmail = declarationEmail;
    }

    public void setBHPANum(String BHPA){
                this.BHPANum = BHPA;
    }
    public String GetBHPANum(){
        return this.BHPANum;
    }

    public void setStartRef(String ref){
        this.startRef = ref;
    }
    public String getStartRef(){
        return  startRef;
    }

    public void setTurnpointsRefs(String[] refs){
        this.turnpointsRefs = refs;
    }
    public String[] getTurnpointsRefs(){
        return  this.turnpointsRefs;
    }

    public void setFinishRef(String ref){
        this.finishRef = ref;
    }
    public String getFinishRef(){
        return this.finishRef;
    }

    public void setOtherBHPAs(String[] BHPAs){
        this.otherBHPAs = BHPAs;
    }
    public String[] getOtherBHPAs(){
        return this.otherBHPAs;
    }

    public void setDeclarationEmail(String declarationEmail) {this.declarationEmail = declarationEmail;}
    public String getDeclarationEmail() {return  this.declarationEmail;}

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(this.BHPANum);
        dest.writeString(this.startRef);
        dest.writeStringArray(this.turnpointsRefs);
        dest.writeString(this.finishRef);
        dest.writeStringArray(this.otherBHPAs);
        dest.writeString(this.declarationEmail);

    }

    //constructor used for parcel
    public Declaration(Parcel parcel){
        this.BHPANum = parcel.readString();
        this.startRef = parcel.readString();
        this.turnpointsRefs = parcel.createStringArray();
        this.finishRef = parcel.readString();
        this.otherBHPAs = parcel.createStringArray();
        this.declarationEmail = parcel.readString();

    }
    public Declaration(String BHPANum){
        this.BHPANum = BHPANum;
    }
    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<Declaration> CREATOR = new Parcelable.Creator<Declaration>(){

        @Override
        public Declaration createFromParcel(Parcel parcel) {
            return new Declaration(parcel);
        }

        @Override
        public Declaration[] newArray(int size) {
            return new Declaration[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    public Declaration clone(){

        String BHPANumClone = this.BHPANum;
        String startRefClone = this.startRef;
        String[] turnpointsRefsClone = this.turnpointsRefs;
        String finishRefClone = this.finishRef;
        String[] otherBHPAsClone = this.otherBHPAs;
        String declarationEmailClone = this.declarationEmail;

        Declaration clone  = new Declaration(BHPANumClone,startRefClone,turnpointsRefsClone,finishRefClone,otherBHPAsClone, declarationEmailClone);

        return clone;
    }

    public String renderDeclarationEmail(){

        String email = "";

        email += this.BHPANum + "\n";
        email += this.startRef + "\n";

        for (int i = 0; i < this.turnpointsRefs.length; i++){

            email += this.turnpointsRefs[i] + "\n";
        }

        email += this.finishRef + "\n";

        for (int j = 0 ; j < this.otherBHPAs.length ; j++){

            email += this.otherBHPAs[j] + "\n";
        }

        return  email;


    }

    public void makeStartFinishMatch(){

        this.finishRef = this.startRef;

    }

    public Boolean isLegalTriangle(){

        if(this.startRef.equals(this.finishRef)){

            return true;

        }
        return  false;

    }
}
