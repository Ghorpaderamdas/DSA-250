package p018;

import java.util.Arrays;

/**
 * Problem: Count Subsequences with Product Less Than K (using DP)
 * GFG — Medium
 *
 * Given an array of POSITIVE integers and a positive integer k,
 * count the number of non-empty SUBSEQUENCES whose product of elements < k.
 *
 * SUBSEQUENCE = any subset of elements (not necessarily contiguous).
 * Example: arr=[1,2,3] has 7 non-empty subsequences:
 *          {1},{2},{3},{1,2},{1,3},{2,3},{1,2,3}
 *
 * KEY DP INSIGHT (0/1 Knapsack on product space):
 *   dp[j] = number of subsequences with product EXACTLY j.
 *   Initialize dp[1] = 1  (represents the empty subsequence).
 *
 *   For each element val:
 *     Iterate j from k-1 DOWN to val:
 *       if j % val == 0:  dp[j] += dp[j / val]
 *
 *   ← WHY HIGH-TO-LOW?  Same reason as 0/1 sum knapsack:
 *     prevents the same element from being used multiple times.
 *
 *   Answer = sum(dp[1..k-1]) - 1   (subtract 1 for the empty subsequence)
 *
 * Pattern   : Dynamic Programming (0/1 Knapsack on product space)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force (Recursive enumeration of all subsets)
    //
    // Idea : For each element, choose to include or skip.
    //        Recursively explore both options. At the leaf, check
    //        if we actually took at least one element and product < k.
    //
    //        Cleaner trick: count ALL subsets (including empty) with
    //        product < k, then subtract 1 for the empty subset at the end.
    //
    // Time : O(2^n) — 2^n subsets; early pruning reduces constant factor
    // Space: O(n)   — recursion stack depth n
    // ============================================================
    public int countSubsetsBrute(int[] arr, int k) {
        // total valid = (subsets including empty with product < k) - 1
        return countHelper(arr, 0, 1L, k) - 1;
    }

    /**
     * Recursively count ALL subsets (including empty) where product < k.
     * product is long to avoid overflow during multiplication.
     */
    private int countHelper(int[] arr, int index, long product, int k) {
        if (product >= k) return 0;       // prune: already too large, no hope
        if (index == arr.length) return 1; // valid subset (product < k)

        // skip current element  OR  include current element
        return countHelper(arr, index + 1, product, k)
             + countHelper(arr, index + 1, product * arr[index], k);
    }


    // ============================================================
    // Method 2 — Better: 2D DP
    //
    // Idea : dp[i][j] = number of subsequences using arr[0..i-1]
    //                   with product exactly j.
    //
    // Recurrence:
    //   dp[i][j] = dp[i-1][j]                    ← exclude arr[i-1]
    //            + dp[i-1][j / arr[i-1]]          ← include arr[i-1]
    //                (only when j % arr[i-1] == 0)
    //
    // Base case: dp[0][1] = 1  (empty array → empty subsequence with product 1)
    //
    // Answer: sum(dp[n][1..k-1]) - 1   (subtract 1 for empty subsequence)
    //
    // Dry Run (arr = [1, 2], k = 3):
    //
    //   dp[0] = [0, 1, 0]   ← only dp[0][1] = 1 (empty subsequence)
    //
    //   i=1, val=1:
    //     j=1: dp[1][1] = dp[0][1] + dp[0][1/1] = 1 + 1 = 2   {empty, {1}}
    //     j=2: dp[1][2] = dp[0][2] + dp[0][2/1] = 0 + 0 = 0
    //
    //   i=2, val=2:
    //     j=1: dp[2][1] = dp[1][1] + (1%2≠0)  = 2
    //     j=2: dp[2][2] = dp[1][2] + dp[1][1] = 0 + 2 = 2   {{2}, {1,2}}
    //
    //   sum(dp[2][1..2]) = 2 + 2 = 4
    //   answer = 4 - 1 = 3 ✅  ({1}, {2}, {1,2})
    //
    // Time : O(n × k) — fill (n+1) × k table
    // Space: O(n × k) — 2D DP table
    // ============================================================
    public int countSubsets2D(int[] arr, int k) {
        int n = arr.length;

        // dp[i][j] = count of subsequences from arr[0..i-1] with product j
        int[][] dp = new int[n + 1][k];
        dp[0][1] = 1;  // base case: empty subsequence → product 1

        for (int i = 1; i <= n; i++) {
            int val = arr[i - 1];
            for (int j = 1; j < k; j++) {
                dp[i][j] = dp[i - 1][j];  // option: exclude arr[i-1]

                // option: include arr[i-1] → only if j is divisible by val
                if (j % val == 0) {
                    dp[i][j] += dp[i - 1][j / val];
                }
            }
        }

        // sum all valid products and subtract 1 for the empty subsequence
        int total = 0;
        for (int j = 1; j < k; j++) {
            total += dp[n][j];
        }
        return total - 1;
    }


    // ============================================================
    // Method 3 — Optimal: 1D DP (Space-Optimized) ⭐
    //
    // KEY IDEA:
    //   Compress the 2D table to a 1D array using the
    //   0/1 knapsack HIGH-TO-LOW iteration trick.
    //
    //   dp[j] = number of subsequences with product exactly j
    //
    //   Initialize dp[1] = 1  (empty subsequence starting point)
    //
    //   For each val in arr:
    //     for j from k-1 DOWN to val:
    //       if j % val == 0:
    //         dp[j] += dp[j / val]
    //         ↑ meaning: a subsequence that had product j/val
    //           can gain val to become product j.
    //
    //   WHY HIGH-TO-LOW (not low-to-high)?
    //     If low-to-high: dp[val] += dp[1], then dp[val²] += dp[val]
    //     → val is used TWICE in the same subsequence. WRONG!
    //     High-to-low ensures we read dp[j/val] BEFORE it was updated
    //     in this round → val is used at most once. ✅
    //
    // Dry Run (arr = [1, 2, 3, 4], k = 10):
    //
    //   Init:  dp = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
    //
    //   val=1: j=9..1, j%1==0 always, dp[j] += dp[j/1] = dp[j]
    //          j=1: dp[1] += dp[1] = 1 → dp[1]=2   (only j=1 was nonzero)
    //          dp = [0, 2, 0, 0, 0, 0, 0, 0, 0, 0]
    //
    //   val=2: j=8,6,4,2  (multiples of 2, from high to low)
    //          j=2: dp[2] += dp[1] = 2 → dp[2]=2
    //          dp = [0, 2, 2, 0, 0, 0, 0, 0, 0, 0]
    //
    //   val=3: j=9,6,3
    //          j=6: dp[6] += dp[2] = 2 → dp[6]=2
    //          j=3: dp[3] += dp[1] = 2 → dp[3]=2
    //          dp = [0, 2, 2, 2, 0, 0, 2, 0, 0, 0]
    //
    //   val=4: j=8,4
    //          j=8: dp[8] += dp[2] = 2 → dp[8]=2
    //          j=4: dp[4] += dp[1] = 2 → dp[4]=2
    //          dp = [0, 2, 2, 2, 2, 0, 2, 0, 2, 0]
    //
    //   sum(dp[1..9]) = 2+2+2+2+0+2+0+2+0 = 12
    //   answer = 12 - 1 = 11 ✅
    //
    //   {1},{2},{3},{4},{1,2},{1,3},{1,4},{2,3},{2,4},{1,2,3},{1,2,4} = 11 ✅
    //
    // Time : O(n × k) — n elements, each does O(k) work
    // Space: O(k)     — 1D dp array of size k
    // ============================================================
    public int countSubsets(int[] arr, int k) {
        // dp[j] = count of subsequences with product exactly j
        int[] dp = new int[k];
        dp[1] = 1;  // seed: empty subsequence has product 1

        for (int val : arr) {
            // iterate HIGH-TO-LOW: ensures each element used at most once (0/1 knapsack)
            for (int j = k - 1; j >= val; j--) {
                if (j % val == 0) {
                    // a subsequence with product j/val gains val → product becomes j
                    dp[j] += dp[j / val];
                }
            }
        }

        // sum all valid products 1..k-1, subtract 1 for empty subsequence
        int total = 0;
        for (int j = 1; j < k; j++) {
            total += dp[j];
        }
        return total - 1;
    }


    // ============================================================
    // Driver — verify all three methods
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {1, 2, 3, 4},  // k=10 → 11: {1},{2},{3},{4},{1,2},{1,3},{1,4},{2,3},{2,4},{1,2,3},{1,2,4}
            {1, 2},        // k=3  → 3:  {1},{2},{1,2}
            {2, 3},        // k=7  → 3:  {2},{3},{2,3}=6
            {4},           // k=5  → 1:  {4}
            {5, 3, 2}      // k=7  → 4:  {5},{3},{2},{3,2}=6
        };
        int[]  ks       = {10, 3, 7, 5, 7};
        int[]  expected = {11, 3, 3, 1, 4};

        System.out.println("=== Count Subsequences with Product Less Than K ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int   k   = ks[t];

            int m1 = sol.countSubsetsBrute(arr, k);
            int m2 = sol.countSubsets2D(arr, k);
            int m3 = sol.countSubsets(arr, k);

            boolean pass   = (m3 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  arr        : " + Arrays.toString(arr) + ", k=" + k);
            System.out.println("  Expected   : " + expected[t]);
            System.out.println("  Brute O(2^n*n) : " + m1);
            System.out.println("  2D DP O(n*k)   : " + m2);
            System.out.println("  1D DP O(n*k)   : " + m3 + "  ⭐ optimal space");
            System.out.println();
        }
    }
}
