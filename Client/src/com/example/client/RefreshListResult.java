package com.example.client;

import java.util.List;

public class RefreshListResult {

	private List<Song> list = null;
	private String whatList = "";
	
	public RefreshListResult(List<Song> list, String whatList) {
		super();
		this.list = list;
		this.whatList = whatList;
	}

	public List<Song> getList() {
		return list;
	}

	public void setList(List<Song> list) {
		this.list = list;
	}

	public String getWhatList() {
		return whatList;
	}

	public void setWhatList(String whatList) {
		this.whatList = whatList;
	}

	
}
