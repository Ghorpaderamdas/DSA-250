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
    // Idea : For every starting index i, compute the sum of the
    //        next k elements. Track the maximum sum seen so far.
    // Time : O(n * k) — outer loop runs n-k+1 times,
    //                    inner loop runs k times each
    // Space: O(1)    — only a few variables
    // ============================================================
    public long maxSumBrute(int[] arr, int k) {
        int n = arr.length;
        long maxSum = Long.MIN_VALUE;

        // Try every window starting at index i
        for (int i = 0; i <= n - k; i++) {
            long windowSum = 0;

            // Sum k elements starting from index i
            for (int j = i; j < i + k; j++) {
                windowSum += arr[j];
            }

            // Update max if this window is better
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // ============================================================
    // Method 2 — Prefix Sum (Better)
    // Idea : Precompute a prefix-sum array where prefix[i] holds
    //        the sum of arr[0..i-1].
    //        Then, sum of arr[i..i+k-1] = prefix[i+k] - prefix[i].
    //        This gives us each window's sum in O(1) after O(n) setup.
    // Time : O(n)  — one pass to build prefix, one pass to query
    // Space: O(n)  — prefix-sum array of size n+1
    // ============================================================
    public long maxSumPrefix(int[] arr, int k) {
        int n = arr.length;

        // Build prefix sum: prefix[i] = arr[0] + arr[1] + ... + arr[i-1]
        long[] prefix = new long[n + 1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }

        long maxSum = Long.MIN_VALUE;

        // Window [i .. i+k-1] has sum = prefix[i+k] - prefix[i]
        for (int i = 0; i <= n - k; i++) {
            long windowSum = prefix[i + k] - prefix[i];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // ============================================================
    // Method 3 — Sliding Window (Optimal) ⭐
    // Idea : Instead of recomputing the entire window sum each time,
    //        slide the window one step at a time:
    //          - ADD the new element entering from the right
    //          - SUBTRACT the old element leaving from the left
    //        This keeps the sum updated in O(1) per step.
    //
    // Visual:
    //   arr = [1, 4, 2, 10, 23, 3, ...], k = 4
    //
    //   Window 1: [1, 4, 2, 10]  sum = 17
    //   Window 2: [4, 2, 10, 23] sum = 39  ← add 23, remove 1
    //   Window 3: [2, 10, 23, 3] sum = 38  ← add 3,  remove 4
    //
    // Time : O(n)  — single pass through the array
    // Space: O(1)  — only a few variables, no extra array
    // ============================================================
    public long maxSumSliding(int[] arr, int k) {
        int n = arr.length;

        // Step 1: Compute sum of the FIRST window (indices 0 to k-1)
        long windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }

        long maxSum = windowSum;

        // Step 2: Slide the window from index k to n-1
        for (int i = k; i < n; i++) {
            windowSum += arr[i];         // add new element entering from the right
            windowSum -= arr[i - k];     // remove old element leaving from the left
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // ============================================================
    // Driver — runs all test cases and prints results
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] arrays  = {
            {100, 200, 300, 400},
            {1, 4, 2, 10, 23, 3, 1, 0, 20},
            {100, 200, 300, 400}
        };
        int[]    ks       = { 2,   4,   1  };
        long[]   expected = { 700, 39,  400 };

        System.out.println("=== Max Sum Subarray of Size K ===\n");

        for (int t = 0; t < arrays.length; t++) {
            long brute   = sol.maxSumBrute(arrays[t], ks[t]);
            long prefix  = sol.maxSumPrefix(arrays[t], ks[t]);
            long sliding = sol.maxSumSliding(arrays[t], ks[t]);

            boolean pass = (sliding == expected[t]);
            String status = pass ? "✅ PASS" : "❌ FAIL";

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
