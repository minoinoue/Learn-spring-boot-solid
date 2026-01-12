package com.example.learn_spring_framework.dto.response;

import java.util.List;

public class PageResponse<T> {
	private int page; //current page
	private int size; //numbers of items per page
	private long totalItems; //total of items in dtb
	private int totalPages; //total of page
	private List<T> data; //list of StudentInfoResponse
	
	public PageResponse() {}
	
	public PageResponse(int page, int size, long totalItems, int totalPages, List<T> data) {
		this.page = page;
		this.size = size;
		this.totalItems = totalItems;
		this.totalPages = totalPages;
		this.data = data;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
