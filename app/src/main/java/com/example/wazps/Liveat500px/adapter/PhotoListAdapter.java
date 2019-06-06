package com.example.wazps.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wazps.liveat500px.R;
import com.example.wazps.liveat500px.dao.PhotoItemCollectionDao;
import com.example.wazps.liveat500px.dao.PhotoItemDao;
import com.example.wazps.liveat500px.datatype.MutableInteger;
import com.example.wazps.liveat500px.manager.PhotoListManager;
import com.example.wazps.liveat500px.view.PhotoListitem;

/**
 * Created by Wazps on 6/5/2559.
 */
public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDao dao;

    MutableInteger lastPositionInteger;

    public PhotoListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if (dao == null)
            return 1;
        if (dao.getData() == null)
            return 1;
        return dao.getData().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() -1 ? 1 : 0 ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() -1){
            // Progress Bar
            ProgressBar item;
            if (convertView != null)
                item = (ProgressBar) convertView;
            else
                item = new ProgressBar(parent.getContext());
            return item;
        }

            PhotoListitem item;
            if (convertView != null)
                item = (PhotoListitem) convertView;
            else
                item = new PhotoListitem(parent.getContext());

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUsername() + "\n" + dao.getCamera());
        item.setImageUrl(dao.getImageUrl());
        if (position > lastPositionInteger.getValue()) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(),
                    R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPositionInteger.setValue(position);
        }
            return item;
    }
    public void increaseLastPosition (int amount){
        lastPositionInteger.setValue(lastPositionInteger.getValue() + amount);
    }


}
