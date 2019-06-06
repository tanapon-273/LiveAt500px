package com.example.wazps.liveat500px.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wazps.liveat500px.R;
import com.example.wazps.liveat500px.adapter.PhotoListAdapter;
import com.example.wazps.liveat500px.dao.PhotoItemCollectionDao;
import com.example.wazps.liveat500px.dao.PhotoItemDao;
import com.example.wazps.liveat500px.datatype.MutableInteger;
import com.example.wazps.liveat500px.manager.HttpManager;
import com.example.wazps.liveat500px.manager.PhotoListManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Wazps on 6/5/2559.
 */
public class MainFragment extends Fragment {

    public interface FragmentListener {
        void onPhotoItemClicked(PhotoItemDao dao);

    }

    ListView listView;
    Button btnNewPhoto;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    PhotoListManager photoListManager;
    MutableInteger lastPositionInteger;


    boolean isLoadingMore = false;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //Save & Restore Instance State
        init(savedInstanceState);
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView,savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);

    }

    private void initInstances(View rootView,Bundle savedInstanceState) {

        btnNewPhoto = (Button)rootView.findViewById(R.id.btnNewPhoto);
        btnNewPhoto.setOnClickListener(buttonClickListener);
        // Init 'View' instance(s) with rootView.findViewById here
        listView = (ListView)rootView.findViewById(R.id.ListView);
        listAdapter = new PhotoListAdapter(lastPositionInteger);
        listAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(listViewItemClickListener);

        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FFFFFF"));
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.PAM);
        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);
        listView.setOnScrollListener(listViewScrollListener);

        if (savedInstanceState == null)
        refreshData();
    }

    private void refreshData(){
        if (photoListManager.getCount() == 0 )
            reloadData();
        else
            reloadDataNewer();
    }

    private void reloadDataNewer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListAfter(maxId);
        call.enqueue(new PhoListLoadCallback(PhoListLoadCallback.MODE_RELOAD_NEWER));

    }

    private void loadMoreData() {
        if (isLoadingMore)
            return;
        isLoadingMore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListBefore(minId);
        call.enqueue(new PhoListLoadCallback(PhoListLoadCallback.MODE_LOAD_MORE));

    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhoListLoadCallback(PhoListLoadCallback.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State
        outState.putBundle("photoListManager",
                photoListManager.onSaveInstanceState());

        //Save Instance State lastPositionInteger
        outState.putBundle("lastPositionInteger",
                lastPositionInteger.onSaveInstanceState());

    }

    private void onRestoreInstanceState(Bundle savedInstanceState){
        // Restore Instance State
        photoListManager.onRestoreInstanceState(
                savedInstanceState.getBundle("photoListManager"));

        //Restore Instance State lastPositionInteger
        lastPositionInteger.onRestoreInstanceState(
                savedInstanceState.getBundle("lastPositionInteger"));

    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showButtonNewPhoto(){
        // Show New Photo Button
        btnNewPhoto.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_in);
        btnNewPhoto.startAnimation(anim);
    }
    private void hideButtonNewPhoto(){
        // Hide New Photo Button
        btnNewPhoto.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(
                Contextor.getInstance().getContext(),
                R.anim.zoom_fade_out);
        btnNewPhoto.startAnimation(anim);
    }

    private void showToast(String text){
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }


    /***********************************************
     * Listener Zone
     ***********************************************/

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhoto) {
                listView.smoothScrollToPosition(0);
                hideButtonNewPhoto();
            }
        }
    };

    SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new
            SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view,
                             int firstVisibleItem,
                             int visibleItemCount,
                             int totalItemCount) {
            if (view == listView){
            swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                if (photoListManager.getCount() > 0) {
                    // Load More
                    loadMoreData();
                }
                }
            }

        }
    };

    AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Intent
            if (position < photoListManager.getCount()) {
                PhotoItemDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }
        }
    };



    /***********************************************
     * Inner Class
     ***********************************************/

    class PhoListLoadCallback implements Callback<PhotoItemCollectionDao>{
        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;
        public PhoListLoadCallback(int mode){
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()){

                PhotoItemCollectionDao dao = response.body();
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                }else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appedDaoAtButtomPosition(dao);
                }else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagIfCapable(mode);
                listAdapter.setDao(photoListManager.getDao());
                listAdapter.notifyDataSetChanged();
                if (mode == MODE_RELOAD_NEWER){
                    int additionalSize =
                            (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    listAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisiblePosition + additionalSize,top);
                    if (additionalSize > 0)
                        showButtonNewPhoto();
                }else {

                }

                showToast("Load Completed");

            }else {
                clearLoadingMoreFlagIfCapable(mode);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            clearLoadingMoreFlagIfCapable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());

        }

        private void clearLoadingMoreFlagIfCapable(int mode){
            if (mode == MODE_LOAD_MORE)
                isLoadingMore = false;
        }

    }


}
