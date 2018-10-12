package com.XCDeclare.edwardcarron.xcdeclare;

/**
 * Created by edwardcarron on 29/03/2018.
 */

public class FlightPoint {

    String type;
    String gridReference;

    public FlightPoint(String type, String gridReference){
        this.type = type;
        this.gridReference = gridReference;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getGridReference(){
        return this.gridReference;
    }
    public void setGridReference(String gridReference){
        this.gridReference = gridReference;
    }

}
