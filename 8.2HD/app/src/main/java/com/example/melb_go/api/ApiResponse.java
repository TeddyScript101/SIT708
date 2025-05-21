package com.example.melb_go.api;

import com.example.melb_go.model.TouristAttraction;

import java.util.List;

public class ApiResponse {
    private List<TouristAttraction> data;
    private Pagination pagination;

    public List<TouristAttraction> getData() {
        return data;
    }

    public void setData(List<TouristAttraction> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public static class Pagination {
        private int currentPage;
        private int itemsPerPage;
        private int totalItems;
        private int totalPages;

        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        public int getItemsPerPage() { return itemsPerPage; }
        public void setItemsPerPage(int itemsPerPage) { this.itemsPerPage = itemsPerPage; }
        public int getTotalItems() { return totalItems; }
        public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    }
}
