package p002;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Largest Subarray with 0 Sum
 * GFG — Medium
 *
 * Given array arr[] of integers (can include negatives), find the
 * LENGTH of the longest contiguous subarray whose sum is 0.
 *
 * KEY INSIGHT (Prefix Sum + HashMap):
 *   If prefix[j] == prefix[i], then:
 *     sum(arr[i+1 .. j]) = prefix[j] - prefix[i] = 0
 *
 *   So we look for REPEATED prefix sums!
 *   When the same prefix sum appears at indices i and j (j > i),
 *   the subarray arr[i+1 .. j] has sum = 0.
 *   Length = j - i.
 *
 *   → Store FIRST occurrence of each prefix sum in a HashMap.
 *   → Initialize map = {0: -1} to handle subarrays that start at index 0.
 *   → When same prefix sum is seen again, compute length and update max.
 *   → NEVER overwrite: keeping the EARLIEST occurrence gives the LONGEST subarray.
 *
 * Pattern   : Prefix Sum + HashMap
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Try every (start, end) pair. Compute sum incrementally.
    //        If sum == 0 → track length.
    //
    // Time : O(n²) — two nested loops; sum updated in O(1) per step
    // Space: O(1)  — no extra memory
    // ============================================================
    public int maxLenBrute(int[] arr, int n) {
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum == 0) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 2 — Better: Prefix Sum Array + All Pairs Check (O(n²))
    //
    // Idea : Precompute prefix[] array. Then check all pairs (i, j):
    //        sum(arr[i..j-1]) = prefix[j] - prefix[i] == 0
    //        ↔ prefix[j] == prefix[i]
    //
    // This makes the key mathematical relationship visually obvious
    // and directly leads to the O(n) HashMap solution.
    //
    // Time : O(n²) — two loops; prefix build O(n)
    // Space: O(n)  — prefix array of size n+1
    // ============================================================
    public int maxLenPrefix(int[] arr, int n) {
        // prefix[i] = sum of arr[0..i-1]
        // prefix[0] = 0 (empty prefix)
        int[] prefix = new int[n + 1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }

        int maxLen = 0;

        // check every pair of prefix-sum indices
        for (int i = 0; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (prefix[j] == prefix[i]) {
                    // arr[i..j-1] has sum 0  →  length = j - i
                    maxLen = Math.max(maxLen, j - i);
                }
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 3 — Optimal: Prefix Sum + HashMap ⭐
    //
    // KEY MATH:
    //   sum(arr[i+1 .. j]) = prefix[j] - prefix[i] = 0
    //                      → prefix[j] == prefix[i]
    //
    //   So: if prefix sum at index j equals a prefix sum seen earlier at i,
    //       subarray arr[i+1..j] has sum 0 and length = j - i.
    //
    // INITIALIZATION  map = {0 : -1}:
    //   Prefix sum 0 at virtual index -1 (before the array).
    //   If prefix[j] == 0 at index j:
    //     length = j - (-1) = j + 1  ← subarray arr[0..j] has sum 0 ✅
    //
    // ONLY STORE FIRST OCCURRENCE:
    //   We want the LONGEST subarray → earliest start gives maximum length.
    //   Never overwrite an existing map entry.
    //
    // Dry Run (arr = [15, -2, 2, -8, 1, 7, 10, 23]):
    //
    //   map={0:-1}  prefix=0  maxLen=0
    //
    //   i=0: prefix=15   new  → map={0:-1, 15:0}
    //   i=1: prefix=13   new  → map={..., 13:1}
    //   i=2: prefix=15   seen at 0!  len=2-0=2    maxLen=2
    //   i=3: prefix=7    new  → map={..., 7:3}
    //   i=4: prefix=8    new  → map={..., 8:4}
    //   i=5: prefix=15   seen at 0!  len=5-0=5    maxLen=5  ← ANSWER!
    //   i=6: prefix=25   new
    //   i=7: prefix=48   new
    //
    //   Answer: 5 ✅  (arr[1..5] = [-2, 2, -8, 1, 7], sum = 0)
    //
    // Time : O(n)  — single pass; HashMap operations O(1) average
    // Space: O(n)  — HashMap stores at most n+1 prefix sums
    // ============================================================
    public int maxLen(int[] arr, int n) {
        // map: prefix sum → FIRST index where this sum was seen
        Map<Integer, Integer> firstOccurrence = new HashMap<>();
        firstOccurrence.put(0, -1);  // empty prefix (sum=0) at virtual index -1

        int prefix = 0;
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            prefix += arr[i];  // update running prefix sum

            if (firstOccurrence.containsKey(prefix)) {
                // same prefix sum seen before → subarray between them has sum 0
                int len = i - firstOccurrence.get(prefix);
                maxLen = Math.max(maxLen, len);
                // DO NOT update map → keep FIRST occurrence for the longest subarray
            } else {
                // first time seeing this prefix sum → record this index
                firstOccurrence.put(prefix, i);
            }
        }

        return maxLen;
    }


    // ============================================================
    // Driver — verify all three methods
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {15, -2, 2, -8, 1, 7, 10, 23},   // 5  → arr[1..5]=[-2,2,-8,1,7]
            {1, 2, 3},                          // 0  → no zero-sum subarray
            {0, 0, 0},                          // 3  → entire array
            {1, -1, 3},                         // 2  → arr[0..1]=[1,-1]
            {-3, 3, -3, 3}                      // 4  → entire array sum=0
        };
        int[] expected = {5, 0, 3, 2, 4};

        System.out.println("=== Largest Subarray with 0 Sum ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int   n   = arr.length;

            int m1 = sol.maxLenBrute(arr, n);
            int m2 = sol.maxLenPrefix(arr, n);
            int m3 = sol.maxLen(arr, n);

            boolean pass   = (m3 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  arr      : " + Arrays.toString(arr));
            System.out.println("  Expected : " + expected[t]);
            System.out.println("  Brute O(n²)    : " + m1);
            System.out.println("  Prefix O(n²)   : " + m2);
            System.out.println("  HashMap O(n)   : " + m3 + "  ⭐ optimal");
            System.out.println();
        }
    }
}
