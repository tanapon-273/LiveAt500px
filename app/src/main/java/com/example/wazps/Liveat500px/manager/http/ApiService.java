package com.example.wazps.liveat500px.manager.http;

import com.example.wazps.liveat500px.dao.PhotoItemCollectionDao;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Wazps on 6/5/2559.
 */
public interface ApiService {
    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();

    @POST("list/after/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListAfter(@Path("id") int id);

    @POST("list/before/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListBefore(@Path("id") int id);
}
