package p001;

import java.util.Arrays;

/**
 * Problem: Max Sum Subarray of Size K
 *
 * Given an array of integers and a number k,
 * find the maximum sum among all contiguous subarrays of exactly size k.
 *
 * Example: arr = [1, 4, 2, 10, 23, 3, 1, 0, 20], k = 4
 *          → answer is 39  (4 + 2 + 10 + 23)
 *
 * Pattern   : Sliding Window
 * Difficulty: Easy
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Try every possible window of size k.
    //        For each starting index i, loop through k elements
    //        and compute their sum from scratch.
    //        Keep track of the maximum sum seen so far.
    //
    // Time : O(n * k) — outer loop runs (n-k+1) times,
    //                    inner loop runs k times each
    // Space: O(1)    — only a few variables, no extra array
    // ============================================================
    public int maxSubarraySumBrute(int[] arr, int k) {
        int n = arr.length;

        // edge case: array is smaller than window size
        if (n < k) {
            return 0;
        }

        int maxSum = 0;

        // try every window starting at index i
        for (int i = 0; i <= n - k; i++) {
            int windowSum = 0;

            // sum k elements starting from index i
            for (int j = i; j < i + k; j++) {
                windowSum += arr[j];
            }

            // update maxSum if this window is larger
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }

    // ============================================================
    // Method 2 — Prefix Sum (Better)
    //
    // Idea : Precompute a prefix array where prefix[i] = sum of
    //        arr[0] to arr[i-1].
    //        Then sum of any window arr[i .. i+k-1]
    //             = prefix[i+k] - prefix[i]   → O(1) per window
    //
    // Visual (arr = [1, 4, 2, 10], k = 2):
    //   prefix = [0, 1, 5, 7, 17]
    //   window [0..1] = prefix[2] - prefix[0] = 5 - 0  = 5
    //   window [1..2] = prefix[3] - prefix[1] = 7 - 1  = 6
    //   window [2..3] = prefix[4] - prefix[2] = 17 - 5 = 12
    //
    // Time : O(n)  — one pass to build prefix, one pass to query
    // Space: O(n)  — prefix array of size n+1
    // ============================================================
    public int maxSubarraySumPrefix(int[] arr, int k) {
        int n = arr.length;

        // edge case: array is smaller than window size
        if (n < k) {
            return 0;
        }

        // build prefix sum array
        // prefix[i] = arr[0] + arr[1] + ... + arr[i-1]
        // prefix[0] = 0  (empty sum)
        int[] prefix = new int[n + 1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }

        int maxSum = 0;

        // window [i .. i+k-1] sum = prefix[i+k] - prefix[i]
        for (int i = 0; i <= n - k; i++) {
            int windowSum = prefix[i + k] - prefix[i];
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }

    // ============================================================
    // Method 3 — Sliding Window (Optimal) ⭐
    //
    // Idea : Instead of recomputing the full window sum each time,
    //        slide the window one step right:
    //          ADD    the new element entering from the right
    //          REMOVE the old element leaving from the left
    //        One add + one subtract = O(1) per step.
    //
    // Visual (arr = [1, 4, 2, 10, 23, 3, ...], k = 4):
    //
    //   Step 1: [1,  4,  2, 10]      windowSum = 17   (first window)
    //   Step 2: [4,  2, 10, 23]      windowSum = 39   add 23, remove 1
    //   Step 3: [2, 10, 23,  3]      windowSum = 38   add  3, remove 4
    //   ...
    //   maxSum = 39 ✅
    //
    // Time : O(n)  — single pass through the array
    // Space: O(1)  — only a few variables, no extra array
    // ============================================================
   public int maxSubarraySum(int[] arr, int k) {
    int n = arr.length;

    // edge case: no valid window exists if array is smaller than k
    if (n < k) {
        return 0;
    }

    // Step 1: compute sum of the first window [0 .. k-1]
    int windowSum = 0;
    for (int i = 0; i < k; i++) {
        windowSum += arr[i];
    }

    // initialize maxSum with first window's sum
    // (avoids Integer.MIN_VALUE since we already have a valid window)
    int maxSum = windowSum;

    // Step 2: slide the window from index k to n-1
    // each step: add the new element entering from the right (arr[i])
    //            subtract the old element leaving from the left (arr[i-k])
    for (int i = k; i < n; i++) {
        windowSum += arr[i] - arr[i - k];
        maxSum = Math.max(maxSum, windowSum);
    }

    return maxSum;
}

    
    // ============================================================
    //  Method 3 — Sliding Window (Optimal) but pointer style ⭐
    // ============================================================

    public int maxSubarraySum2(int[] arr, int k) {

        // edge case 1: null or empty array
        if (arr == null || arr.length == 0) {
            return 0;
        }

        int n = arr.length;

        // edge case 2: window size is invalid (k <= 0)
        if (k <= 0) {
            return 0;
        }

        // edge case 3: window size is larger than array
        // no valid window of size k exists
        if (n < k) {
            return 0;
        }

        int low  = 0;
        int high = k - 1;       // high points to last element of first window

        int windowSum = 0;
        int maxSum    = Integer.MIN_VALUE;
        // Integer.MIN_VALUE so negatives are handled correctly
        // e.g. arr=[-5,-3,-1], k=2 → answer is -4, not 0

        // Step 1: compute sum of first window [low .. high] i.e. [0 .. k-1]
        for (int i = low; i <= high; i++) {
            windowSum += arr[i];
        }

        // Step 2: slide the window rightward until high goes past last index
        // each iteration: evaluate current window, then shift low and high by 1
        while (high < n) {

            // evaluate current window [low .. high]
            maxSum = Math.max(maxSum, windowSum);

            // shift window one step to the right
            high++;
            low++;

            // update windowSum only if new window is still within bounds
            // (high == n means window has gone out of bounds — skip update)
            if (high < n) {
                windowSum += arr[high];    // add element entering from the right
                windowSum -= arr[low - 1]; // remove element leaving from the left
                // low-1 because low is already incremented above
                // arr[low-1] is the element that just left the window
            }
        }

        return maxSum;
    }


    // ============================================================
    // Driver — runs all three methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] arrays   = {
            {100, 200, 300, 400},
            {1, 4, 2, 10, 23, 3, 1, 0, 20},
            {100, 200, 300, 400}
        };
        int[]  ks        = {  2,   4,   1 };
        int[]  expected  = {700,  39, 400 };

        System.out.println("=== Max Sum Subarray of Size K ===\n");

        for (int t = 0; t < arrays.length; t++) {
            int brute   = sol.maxSubarraySumBrute(arrays[t], ks[t]);
            int prefix  = sol.maxSubarraySumPrefix(arrays[t], ks[t]);
            int sliding = sol.maxSubarraySum(arrays[t], ks[t]);

            boolean pass   = (sliding == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  Array       : " + Arrays.toString(arrays[t]));
            System.out.println("  k           : " + ks[t]);
            System.out.println("  Expected    : " + expected[t]);
            System.out.println("  Brute Force : " + brute);
            System.out.println("  Prefix Sum  : " + prefix);
            System.out.println("  Sliding Win : " + sliding);
            System.out.println();
        }
    }
}