package com.example.wazps.liveat500px.datatype;

import android.os.Bundle;

/**
 * Created by Wazps on 7/5/2559.
 */
public class MutableInteger {

    private int value;

    public MutableInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Bundle onSaveInstanceState(){
        //Save Instance State lastPositionInteger
        Bundle bundle = new Bundle();
        bundle.putInt("value",value);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        //Restore Instance State lastPositionInteger
        value = savedInstanceState.getInt("value");

    }
}
