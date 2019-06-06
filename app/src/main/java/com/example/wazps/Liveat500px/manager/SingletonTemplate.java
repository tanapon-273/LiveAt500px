package com.example.wazps.liveat500px.manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by Wazps on 6/5/2559.
 */
public class SingletonTemplate {

    private static SingletonTemplate instance;

    public static SingletonTemplate getInstance() {
        if (instance == null)
            instance = new SingletonTemplate();
        return instance;
    }

    private Context mContext;

    private SingletonTemplate() {
        mContext = Contextor.getInstance().getContext();
    }

}
