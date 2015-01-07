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
	
	
}
