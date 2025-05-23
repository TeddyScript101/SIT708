package com.example.melb_go.ui.bookmarks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.melb_go.adapter.BookmarkAdapter;
import com.example.melb_go.R;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.databinding.FragmentBookmarksBinding;

import java.util.ArrayList;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private BookmarkAdapter adapter;

    private ApiService apiService;

    public BookmarksFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        String token = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        BookmarksViewModel viewModel =
                new ViewModelProvider(this).get(BookmarksViewModel.class);

        viewModel.init(token);
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        adapter = new BookmarkAdapter(getContext(), new ArrayList<>(), apiService, token, attractionId -> {
            Bundle bundle = new Bundle();
            bundle.putString("attraction_id", attractionId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_bookmarks_to_detailFragment, bundle);

        },
                () -> viewModel.refreshBookmarks());

        binding.bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.bookmarkRecyclerView.setAdapter(adapter);

        viewModel.getBookmarks().observe(getViewLifecycleOwner(), attractions -> {

            if (attractions != null) {
                adapter.updateData(attractions);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        BookmarksViewModel viewModel = new ViewModelProvider(this).get(BookmarksViewModel.class);
        viewModel.refreshBookmarks(); // Add this method in ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
