package com.example.challengesolver;

import java.util.ArrayList;

import android.util.Log;

public class Solver {
	private static final String TAG = Solver.class.getName();
	private static final int INTERVAL_A_CAPACITY = 180;
	private static final int INTERVAL_B_CAPACITY = 480;
	private static final int NUM_MINUTES_IN_HOUR = 60;
	
	public static String getOptimalSchedule(ArrayList<ConferenceTalk> allTalks) {
		// Solution 1
		Solution s1 = getSubproblemSolution(allTalks, INTERVAL_A_CAPACITY, INTERVAL_B_CAPACITY);
		// Solution 2
		Solution s2 = getSubproblemSolution(allTalks, INTERVAL_B_CAPACITY, INTERVAL_A_CAPACITY);
		
		// Determine optimal
		String formattedSolution = "";
		if(s1.getTotalVotes() >= s2.getTotalVotes()) {
			formattedSolution = s1.print();
		} else {
			formattedSolution = s2.print();
		}
		
		return formattedSolution;
	}
	
	private static Solution getSubproblemSolution(ArrayList<ConferenceTalk>talks, int capacity1, int capacity2) {
		// Calculate Solution 
		boolean[] solution1 = getSolutionForIndividualKnapsack(talks, capacity1);
		ArrayList<ConferenceTalk> remaingTalks = new ArrayList<ConferenceTalk>();
		for(int i = 0; i<talks.size(); i++) {
			if(!solution1[i]){
				remaingTalks.add(talks.get(i));
			}
		}
		boolean[] solution2 = getSolutionForIndividualKnapsack(remaingTalks, capacity2);

		// Calculate total votes for solution 
		int votes = 0;
		ArrayList<ConferenceTalk> talksInterval1 = new ArrayList<ConferenceTalk>();
		ArrayList<ConferenceTalk> talksInterval2 = new ArrayList<ConferenceTalk>();
		
		for(int i = 0; i<solution1.length; i++){
			if(solution1[i]){
				ConferenceTalk talk = talks.get(i);
				votes += talk.getVotes();
				talksInterval1.add(talk);
			}
		}

		for(int i = 0; i<solution2.length; i++){
			if(solution2[i]){
				ConferenceTalk talk = remaingTalks.get(i);
				votes += talk.getVotes();
				talksInterval2.add(talk);
			}
		}
		
		return new Solution(capacity1, capacity2, talksInterval1, talksInterval2, votes);
	}
	
	private static boolean[] getSolutionForIndividualKnapsack(ArrayList<ConferenceTalk>talks, int capacity) {
		final int INPUT_SIZE = talks.size();
		
		// All elements are false by default
		boolean[] includedTalks = new boolean[INPUT_SIZE];
		
		// All elements are 0 by default
		int[][] maxVotes = new int[INPUT_SIZE+1][capacity+1];
		
		for(int i = 1; i <= INPUT_SIZE; i++){
			for(int j = 0; j <= capacity; j++) {
				ConferenceTalk talk = talks.get(i-1);
				if(talk.getMinutes() <= j){
					maxVotes[i][j] = max(maxVotes[i-1][j-talk.getMinutes()]+talk.getVotes(), maxVotes[i-1][j]);
				} else {
					maxVotes[i][j] = maxVotes[i-1][j];
				}
			}
		}
		
		//Optimal solution is m[input_size][capacity]
		
		for(int i = INPUT_SIZE, j = capacity; i>0 && j>0; i--) {
			int currSol = maxVotes[i][j];
			int prevSol = maxVotes[i-1][j];
			if(prevSol != currSol) {
				includedTalks[i-1] = true;
//				Log.d(TAG, "talk included = "+ talks.get(i-1).getName());
				j = j-talks.get(i-1).getMinutes();
			}
		}
		
		return includedTalks;
	}

	private static int max(int i, int j) {
		if (i>=j) {
			return i;
		} else {
			return j;
		}
	}
	
	private static class Solution {
		private int capacity1;
		private int capacity2;
		private ArrayList<ConferenceTalk> solutionForKnapSack1;
		private ArrayList<ConferenceTalk> solutionForKnapSack2;
		private int totalVotes;
		
		public Solution(int capacity1, int capacity2,
				ArrayList<ConferenceTalk> solutionForKnapSack1,
				ArrayList<ConferenceTalk> solutionForKnapSack2, int totalVotes) {
			super();
			this.capacity1 = capacity1;
			this.capacity2 = capacity2;
			this.solutionForKnapSack1 = solutionForKnapSack1;
			this.solutionForKnapSack2 = solutionForKnapSack2;
			this.totalVotes = totalVotes;
		}
		
		public int getTotalVotes() {
			return totalVotes;
		}
		
		public String print() {
			String formattedSolution = "";
			
			int startTime1 = 9*NUM_MINUTES_IN_HOUR;
			int startTime2 = 13*NUM_MINUTES_IN_HOUR;
			
			formattedSolution += "Solution is : \n";
			Log.d(TAG, "Solution is : ");
			// compute and print start and end times for each talk
			if(capacity1 == INTERVAL_A_CAPACITY) {
				formattedSolution += computeStartAndEndTimes(solutionForKnapSack1, startTime1);
				formattedSolution += computeStartAndEndTimes(solutionForKnapSack2, startTime2);
			} else if(capacity2 == INTERVAL_A_CAPACITY) {
				formattedSolution += computeStartAndEndTimes(solutionForKnapSack2, startTime1);
				formattedSolution += computeStartAndEndTimes(solutionForKnapSack1, startTime2);
			}
			
			formattedSolution += "Total votes = " + totalVotes + "\n";
			
			Log.d(TAG, "Total votes = " + totalVotes);
			
			return formattedSolution;
		}
		
		private static String computeStartAndEndTimes(ArrayList<ConferenceTalk> solutionSet, int startTime) {
			String solution = "";
			for(int i = 0; i<solutionSet.size(); i++) {
				ConferenceTalk talk = solutionSet.get(i);
				int start, end;
				if(i==0) {
					start = startTime;
				} else {
					start = solutionSet.get(i-1).getEndTime();
				}
				talk.setStartTime(start);
				
				end = talk.getStartTime()+talk.getMinutes();
				talk.setEndTime(end);
				
				solution += getFormattedTime(start) + " - " + getFormattedTime(end) +" : "+talk.getName() + "\n";
				
				Log.d(TAG, getFormattedTime(start) + " - " + getFormattedTime(end) +" : "+talk.getName());
			}
			return solution;
		}
		
		private static String getFormattedTime(int time) {
			String res = "";
			int timeHours = time/NUM_MINUTES_IN_HOUR;
			int timeMins = time%NUM_MINUTES_IN_HOUR;
			boolean isPm = false;
			
			if(timeHours>=12) {
				if(timeHours>12) {
					timeHours -= 12;
				}
				isPm = true;
			}
			
			res += timeHours;
			res += ":";
			res += timeMins;
			
			if(timeMins == 0) {
				res += "0";
			}
			
			if(isPm) {
				res += "pm";
			} else {
				res += "am";	
			}
			
			return res;
		}
	}
}
