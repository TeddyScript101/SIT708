package com.example.melb_go.ui.bookmarks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.melb_go.model.TouristAttraction;
import com.example.melb_go.repository.TouristAttractionRepository;

import java.util.List;

public class BookmarksViewModel extends ViewModel {

    private final MutableLiveData<List<TouristAttraction>> bookmarksLiveData = new MutableLiveData<>();
    private TouristAttractionRepository repository;

    public void init(String token) {
        if (repository != null) return;
        repository = new TouristAttractionRepository(token);
        fetchBookmarks();
    }

    private void fetchBookmarks() {
        repository.fetchBookmarks(new TouristAttractionRepository.CallbackListener() {
            @Override
            public void onSuccess(List<TouristAttraction> data) {
                bookmarksLiveData.setValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                bookmarksLiveData.setValue(null);
            }
        });
    }
    public void refreshBookmarks() {
        if (repository != null) {
            fetchBookmarks();
        }
    }

    public LiveData<List<TouristAttraction>> getBookmarks() {
        return bookmarksLiveData;
    }
}
