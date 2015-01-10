# multiple-knapsack-solver

The conference scheduling problem is a multiple knapsack problem with 2 knapsacks. The approach I have used to solve this problem is a combination of greedy and dynamic programming. 

Consider 9am-12pm as interval1 and 1pm-9pm as interval2. I solve for interval1 as if it is a knapsack. I then remove the talks selected for interval1 from the list of talks and solve for interval2 as if it was another knapsack. This gives me one possible solution. I then flip the order of the intervals, i. e., I consider interval2 first and solve for it and then interval1. So I have 2 possible solutions and the best one is the one with maximum votes.

This approach has a complexity of O(n*K!**(sum of all interval capacities)) where n is the number of talks and K is the number of knapsacks or disjoint intervals. The tradeoff with this approach is that the run time grows in factorial time for the number of intervals. If we generalize to K knapsacks it will be better to use the algorithms discussed in http://www.or.deis.unibo.it/kp/Chapter6.pdf.



