package com.cropcircle.filmcircle.ui.collection.tv;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cropcircle.filmcircle.database.MediaTVRepository;
import com.cropcircle.filmcircle.models.tv.MediaTV;

import java.util.List;

public class CollectionsTVViewModel extends ViewModel {
    private MediaTVRepository repository;

    public CollectionsTVViewModel() {
        repository = new MediaTVRepository();
    }

    public LiveData<List<MediaTV>> getFavoriteTv(int userId, String sessionId){
        return repository.getFavoriteTv(userId, sessionId);
    }
}