package p004;

/**
 * Problem: Best Time to Buy and Sell Stock
 * LeetCode #121 — Easy
 *
 * Given prices[] where prices[i] = stock price on day i.
 * Buy on one day, sell on a LATER day (buy before sell).
 * Return the maximum profit. If no profit possible, return 0.
 *
 * Example: prices = [7, 1, 5, 3, 6, 4]  → 5  (buy at 1, sell at 6)
 *          prices = [7, 6, 4, 3, 1]      → 0  (prices always fall)
 *
 * KEY INSIGHT:
 *   To maximize profit on any sell day j:
 *   → buy at the minimum price seen before day j
 *   → profit = prices[j] - minPriceSoFar
 *
 * Pattern   : Sliding Window / Two Pointers (also Greedy)
 * Difficulty: Easy
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Try every possible buy-sell pair (i, j) where i < j.
    //        profit = prices[j] - prices[i]
    //        Track the maximum profit seen.
    //
    // Time : O(n²) — two nested loops
    // Space: O(1)  — only a few variables
    // ============================================================
    public int maxProfitBrute(int[] prices) {
        int n = prices.length;
        int maxProfit = 0;

        // try every buy day i
        for (int i = 0; i < n - 1; i++) {
            // try every sell day j (must be after buy day)
            for (int j = i + 1; j < n; j++) {
                int profit = prices[j] - prices[i];
                maxProfit = Math.max(maxProfit, profit);
            }
        }

        return maxProfit;
        // Note: maxProfit initialized to 0 because if all prices fall,
        //       every profit is negative → we return 0 (no transaction).
    }


    // ============================================================
    // Method 2 — Kadane's Algorithm Variant (Better)
    //
    // Idea : Convert prices[] to a daily change array:
    //        change[i] = prices[i] - prices[i-1]
    //
    //        Key observation:
    //          Buy on day i, sell on day j →
    //          profit = prices[j] - prices[i]
    //                 = sum of changes from day i+1 to day j
    //
    //        So: max profit = max subarray sum of change[]
    //        → Solve with Kadane's algorithm.
    //
    // Visual (prices = [7, 1, 5, 3, 6, 4]):
    //   change = [-6, +4, -2, +3, -2]
    //                  ↑         ↑
    //             start here   end here → sum = 4-2+3 = 5 ✅
    //
    // Kadane's: currentSum += change[i]
    //           if currentSum < 0 → reset to 0 (don't carry a loss forward)
    //           maxSum = max(maxSum, currentSum)
    //
    // Time : O(n)  — one pass for change array + one pass for Kadane's
    // Space: O(1)  — compute change on the fly, no extra array needed
    // ============================================================
    public int maxProfitKadane(int[] prices) {
        int n = prices.length;
        if (n < 2) return 0; // need at least 2 days to make a trade

        int currentSum = 0;
        int maxProfit  = 0;

        // start from day 1 (compute change = prices[i] - prices[i-1] on the fly)
        for (int i = 1; i < n; i++) {
            int dailyChange = prices[i] - prices[i - 1]; // gain/loss from yesterday
            currentSum += dailyChange;

            if (currentSum < 0) {
                currentSum = 0; // don't carry a cumulative loss — reset window
            }

            maxProfit = Math.max(maxProfit, currentSum);
        }

        return maxProfit;
    }


    // ============================================================
    // Method 3 — Min Price Tracking ⭐ (Optimal, most intuitive)
    //
    // Idea : Scan left to right, tracking the minimum price seen so far.
    //        At every day, compute profit = currentPrice - minPrice.
    //        Track the maximum profit.
    //
    // Why correct?
    //   To maximize profit on sell day j:
    //   → buy at the cheapest price on any day before j
    //   → profit = prices[j] - min(prices[0..j-1])
    //   → Greedy: keep a running minimum as we scan.
    //
    // Visual (prices = [7, 1, 5, 3, 6, 4]):
    //
    //   day 0: price=7  minPrice=7   profit=0        maxProfit=0
    //   day 1: price=1  minPrice=1   profit=0        maxProfit=0  ← new min
    //   day 2: price=5  minPrice=1   profit=5-1=4    maxProfit=4
    //   day 3: price=3  minPrice=1   profit=3-1=2    maxProfit=4
    //   day 4: price=6  minPrice=1   profit=6-1=5    maxProfit=5  ← best!
    //   day 5: price=4  minPrice=1   profit=4-1=3    maxProfit=5
    //
    //   Answer: 5 ✅
    //
    // Time : O(n)  — single pass through prices[]
    // Space: O(1)  — two variables: minPrice, maxProfit
    // ============================================================
    public int maxProfitMinTracking(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int minPrice  = Integer.MAX_VALUE; // cheapest buy day seen so far
        int maxProfit = 0;                 // best profit seen so far

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;          // found a cheaper buy day — update
            } else {
                // potential sell day — compute profit with the cheapest buy
                int profit = price - minPrice;
                maxProfit = Math.max(maxProfit, profit);
            }
        }

        return maxProfit;
    }


    // ============================================================
    // Method 4 — Sliding Window / Two Pointers ⭐⭐ (Optimal)
    //
    // Idea : Two pointers:
    //          left  → buy  day (cheapest day seen so far)
    //          right → sell day (scanning forward)
    //
    //        Each step:
    //          if prices[right] > prices[left]:
    //              → valid profit; compute and update maxProfit
    //          else:
    //              → prices[right] is a CHEAPER buy day
    //              → move left = right  (start fresh window from here)
    //          always: right++
    //
    // Why move left = right when prices[right] < prices[left]?
    //   Any future sell price p satisfies:
    //     p - prices[right] > p - prices[left]   (since prices[right] < prices[left])
    //   So buying at right is ALWAYS better than buying at left.
    //   → No point keeping left where it is.
    //
    // Visual (prices = [7, 1, 5, 3, 6, 4]):
    //
    //   L=0, R=1: prices[1]=1 < prices[0]=7 → new min! L=1
    //   L=1, R=2: prices[2]=5 > prices[1]=1 → profit=4  maxProfit=4
    //   L=1, R=3: prices[3]=3 > prices[1]=1 → profit=2  maxProfit=4
    //   L=1, R=4: prices[4]=6 > prices[1]=1 → profit=5  maxProfit=5 ✅
    //   L=1, R=5: prices[5]=4 > prices[1]=1 → profit=3  maxProfit=5
    //
    //   Answer: 5 ✅
    //
    // Time : O(n)  — single pass; right moves from 1 to n-1
    // Space: O(1)  — two pointers + maxProfit variable
    // ============================================================
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int left      = 0; // buy  day pointer (cheapest day so far)
        int right     = 1; // sell day pointer
        int maxProfit = 0;

        while (right < prices.length) {
            if (prices[right] > prices[left]) {
                // selling today is profitable
                int profit = prices[right] - prices[left];
                maxProfit = Math.max(maxProfit, profit);
            } else {
                // prices[right] <= prices[left]
                // found a cheaper (or equal) buy day → shift left to here
                left = right;
            }

            right++; // always advance the sell day
        }

        return maxProfit;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {7, 1, 5, 3, 6, 4},  // classic
            {7, 6, 4, 3, 1},      // always falling
            {2, 4, 1},            // buy 2, sell 4
            {1, 2},               // two days
            {2, 1},               // falling two days
            {3, 1, 4, 1, 5},      // multiple lows
            {1},                  // single day
            {1, 1, 1, 1}          // all same
        };
        int[] expected = { 5, 0, 2, 1, 0, 4, 0, 0 };

        System.out.println("=== Best Time to Buy and Sell Stock ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] prices = inputs[t];

            int m1 = sol.maxProfitBrute(prices);       // O(n²)
            int m2 = sol.maxProfitKadane(prices);      // O(n) Kadane's
            int m3 = sol.maxProfitMinTracking(prices);  // O(n) min tracking
            int m4 = sol.maxProfit(prices);             // O(n) sliding window ⭐

            boolean pass   = (m4 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            // build a readable prices string
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < prices.length; i++) {
                sb.append(prices[i]);
                if (i < prices.length - 1) sb.append(", ");
            }
            sb.append("]");

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  Input           : " + sb);
            System.out.println("  Expected        : " + expected[t]);
            System.out.println("  Brute O(n²)     : " + m1);
            System.out.println("  Kadane O(n)     : " + m2);
            System.out.println("  MinTrack O(n)   : " + m3);
            System.out.println("  SlidingWin O(n) : " + m4 + "  ⭐ optimal");
            System.out.println();
        }
    }
}
