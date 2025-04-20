package com.example.news_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HorizontalNewsAdapter.OnItemClickListener {

    private RecyclerView topStoriesRecyclerView;
    private RecyclerView generalNewsRecyclerView;

    private List<NewsItem> topStoriesList;
    private List<NewsItem> generalNewsList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        topStoriesRecyclerView = view.findViewById(R.id.recycler_top_stories);
        generalNewsRecyclerView = view.findViewById(R.id.recycler_general_news);

        topStoriesList = getDummyTopStories();
        generalNewsList = getDummyGeneralNews();

        setupRecyclerView(topStoriesRecyclerView, topStoriesList, true);
        setupRecyclerView(generalNewsRecyclerView, generalNewsList, false);

        return view;
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<NewsItem> itemList, boolean isHorizontal) {
        if (isHorizontal) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        // Set your adapter
        HorizontalNewsAdapter adapter = new HorizontalNewsAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        Log.d("RecyclerView", "Item count: " + itemList.size()); // Check item count
    }

    private List<NewsItem> getDummyTopStories() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem("TOP News 1", "https://imageresizer.static9.net.au/EObULjdDid8nqUO-Mjmswd7jnOY=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F286dbb32-21ae-4b9e-980c-6f716e091beb", "General news description 1"));
        list.add(new NewsItem("TOP News 2", "https://imageresizer.static9.net.au/01bwVWjgZlt-AbMxlHB_TqWPSUA=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F02fc579e-476c-4614-9a94-8b3bfaec31fb", "General news description 2"));
        list.add(new NewsItem("TOP News 3", "https://imageresizer.static9.net.au/8tnD4AgY07MUVabyqqwJR2xXAJQ=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F83fbbb1c-c912-456d-a8ff-1a97def10425", "General news description 3"));
        list.add(new NewsItem("TOP News 4", "https://content.api.news/v3/images/bin/4dbf34e3ed4676aea40f873d24f36c69?width=1024", "General news description 4"));
        list.add(new NewsItem("TOP News 5", "https://content.api.news/v3/images/bin/4e4f1f6c567fcc55a5e4d6be74a0324d?width=1024", "General news description 5"));
        list.add(new NewsItem("TOP News 6", "https://content.api.news/v3/images/bin/d3c13eeb336795138200303280517cc2?width=1024", "General news description 6"));
        list.add(new NewsItem("TOP News 7", "https://content.api.news/v3/images/bin/8667ac59db97ea4258635d8db3436e05?width=1024", "General news description 7"));
        list.add(new NewsItem("TOP News 8", "https://content.api.news/v3/images/bin/333a0c43b354c025a01598e4c3ede54b?width=1024", "General news description 8"));
        list.add(new NewsItem("TOP News 9", "https://content.api.news/v3/images/bin/91a3528cc6a06269d33aa88c1fe48564?width=1024", "General news description 9"));
        list.add(new NewsItem("TOP News 10", "https://content.api.news/v3/images/bin/c061ddea7195c4271022e066d2e80361?width=1024", "General news description 10"));
        return list;
    }

    private List<NewsItem> getDummyGeneralNews() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem("General News 1", "https://imageresizer.static9.net.au/EObULjdDid8nqUO-Mjmswd7jnOY=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F286dbb32-21ae-4b9e-980c-6f716e091beb", "General news description 1"));
        list.add(new NewsItem("General News 2", "https://imageresizer.static9.net.au/01bwVWjgZlt-AbMxlHB_TqWPSUA=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F02fc579e-476c-4614-9a94-8b3bfaec31fb", "General news description 2"));
        list.add(new NewsItem("General News 3", "https://imageresizer.static9.net.au/8tnD4AgY07MUVabyqqwJR2xXAJQ=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F83fbbb1c-c912-456d-a8ff-1a97def10425", "General news description 3"));
        list.add(new NewsItem("General News 4", "https://content.api.news/v3/images/bin/4dbf34e3ed4676aea40f873d24f36c69?width=1024", "General news description 4"));
        list.add(new NewsItem("General News 5", "https://content.api.news/v3/images/bin/4e4f1f6c567fcc55a5e4d6be74a0324d?width=1024", "General news description 5"));
        list.add(new NewsItem("General News 6", "https://content.api.news/v3/images/bin/d3c13eeb336795138200303280517cc2?width=1024", "General news description 6"));
        list.add(new NewsItem("General News 7", "https://content.api.news/v3/images/bin/8667ac59db97ea4258635d8db3436e05?width=1024", "General news description 7"));
        list.add(new NewsItem("General News 8", "https://content.api.news/v3/images/bin/333a0c43b354c025a01598e4c3ede54b?width=1024", "General news description 8"));
        list.add(new NewsItem("General News 9", "https://content.api.news/v3/images/bin/91a3528cc6a06269d33aa88c1fe48564?width=1024", "General news description 9"));
        list.add(new NewsItem("General News 10", "https://content.api.news/v3/images/bin/c061ddea7195c4271022e066d2e80361?width=1024", "General news description 10"));

        return list;
    }

    @Override
    public void onItemClick(NewsItem item) {
        DetailFragment detailFragment = DetailFragment.newInstance(item);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }
}

