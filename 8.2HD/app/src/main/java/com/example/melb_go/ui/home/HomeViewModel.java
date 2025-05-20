package com.example.melb_go.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.melb_go.TouristAttraction;
import com.example.melb_go.TouristAttractionRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    private MutableLiveData<List<TouristAttraction>> attractionList = new MutableLiveData<>();
    private MutableLiveData<List<String>> themeList = new MutableLiveData<>();

    private List<TouristAttraction> fullAttractionList = new ArrayList<>();
    private TouristAttractionRepository repository;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private String selectedTheme = "Place of Worship";



    public void setSearchQuery(String query) {
//        this.searchQuery = query;
        currentPage = 1;
        isLastPage = false;
        fullAttractionList.clear();
        repository.setSearchQuery(query);
    }

    public HomeViewModel() {
        repository = new TouristAttractionRepository();
        loadAttractions(currentPage, selectedTheme);
        loadThemes();
    }

    public LiveData<List<TouristAttraction>> getAttractionList() {
        return attractionList;
    }

    public LiveData<List<String>> getThemeList() {
        return themeList;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    // Set theme filter and reload attractions from page 1
    public void setSelectedTheme(String theme) {
        if ((theme == null && selectedTheme != null) || (theme != null && !theme.equals(selectedTheme))) {
            selectedTheme = theme;
            currentPage = 1;
            isLastPage = false;
            fullAttractionList.clear();
            loadAttractions(currentPage, selectedTheme);
        }
    }

    public void loadNextPage() {
        if (!isLoading && !isLastPage) {
            loadAttractions(currentPage + 1, selectedTheme);
        }
    }

    public void loadFirstPage() {
        currentPage = 1;
        isLastPage = false;
        fullAttractionList.clear();
        loadAttractions(currentPage, selectedTheme);

    }


    private void loadAttractions(int page, String theme) {
        isLoading = true;
        Log.d(TAG, "Loading page: " + page + ", theme: " + theme);

        repository.fetchAttractions(page, theme, new TouristAttractionRepository.CallbackListener() {
            @Override
            public void onSuccess(List<TouristAttraction> data) {
                isLoading = false;
                if (data != null && !data.isEmpty()) {
                    currentPage = page;
                    fullAttractionList.addAll(data);
                    attractionList.postValue(new ArrayList<>(fullAttractionList));
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading = false;
                Log.e(TAG, "Failed to fetch attractions", t);
            }
        });
    }

    public void loadThemes() {
        repository.fetchThemes(new TouristAttractionRepository.CallbackListener() {
            @Override
            public void onSuccess(List data) {
                //noinspection unchecked
                themeList.postValue((List<String>) data);
                Log.d(TAG, "Themes loaded: " + data);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Failed to fetch themes", t);
            }
        });
    }
}
