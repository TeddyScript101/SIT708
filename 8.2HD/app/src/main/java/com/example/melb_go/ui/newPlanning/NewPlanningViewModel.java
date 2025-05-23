package com.example.melb_go.ui.newPlanning;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import com.example.melb_go.adapter.ScheduleAttractionsAdapter;

public class NewPlanningViewModel extends ViewModel {

    private final MutableLiveData<List<ScheduleAttractionsAdapter.TouristAttractionWithTime>> _attractions =
            new MutableLiveData<>();

    private final MutableLiveData<Double> _selectedLat = new MutableLiveData<>();
    private final MutableLiveData<Double> _selectedLng = new MutableLiveData<>();

    public void setAttractions(List<ScheduleAttractionsAdapter.TouristAttractionWithTime> attractions) {
        _attractions.setValue(attractions);
    }

    public LiveData<List<ScheduleAttractionsAdapter.TouristAttractionWithTime>> getAttractions() {
        return _attractions;
    }

    public void setSelectedLat(double lat) {
        _selectedLat.setValue(lat);
    }

    public void setSelectedLng(double lng) {
        _selectedLng.setValue(lng);
    }

    public LiveData<Double> getSelectedLat() {
        return _selectedLat;
    }

    public LiveData<Double> getSelectedLng() {
        return _selectedLng;
    }

    // Optional: Convenience method to check if starting point is set
    public boolean hasSelectedLocation() {
        return _selectedLat.getValue() != null && _selectedLng.getValue() != null;
    }
}
