package com.example.challengesolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConferenceTalk {

	@SerializedName("votes")
	private final int votes;
	
	@SerializedName("minutes")
	private final int minutes;
	
	@SerializedName("name")
	private final String name;
	
	private int startTime;
	private int endTime;

	public ConferenceTalk(int votes, int minutes, String name) {
		this.votes = votes;
		this.minutes = minutes;
		this.name = name;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getVotes() {
		return votes;
	}

	public String getName() {
		return name;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	
}
