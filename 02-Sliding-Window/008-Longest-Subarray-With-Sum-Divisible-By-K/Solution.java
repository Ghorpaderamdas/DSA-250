package p008;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Longest Subarray with Sum Divisible by K
 * GFG — Medium
 *
 * Given array arr[] and integer k, find the length of the longest
 * subarray whose sum is divisible by k.
 *
 * Example: arr=[2,7,6,1,4,5], k=3 → 4  (subarray [7,6,1,4], sum=18, 18%3=0)
 *
 * KEY INSIGHT (Modular Arithmetic):
 *   sum(i..j) % k == 0  ↔  prefix[j] % k == prefix[i] % k
 *
 *   If two prefix sums have the SAME REMAINDER mod k,
 *   the subarray between them has a sum divisible by k.
 *
 *   → Store FIRST occurrence of each remainder in a HashMap.
 *   → When same remainder seen again: length = currentIndex - firstIndex.
 *   → Initialize map with {0 : -1} (empty prefix has sum 0, remainder 0).
 *
 * ⚠️ NOT a classic sliding window (negative numbers make sum non-monotonic).
 *    Correct pattern: Prefix Sum + HashMap.
 *
 * Pattern   : Prefix Sum + HashMap
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Check every possible subarray.
    //        Compute sum incrementally (avoid O(n) inner scan).
    //        If sum % k == 0 → valid subarray → check length.
    //
    // Time : O(n²) — two nested loops; inner sum is incremental
    // Space: O(1)  — no extra memory
    // ============================================================
    public int longestSubarrayBrute(int[] arr, int k) {
        int n = arr.length;
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += arr[j];
                if (sum % k == 0) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 2 — Better: Prefix Sum Array (O(n²))
    //
    // Idea : Precompute prefix sums, then check all pairs (i, j):
    //        (prefix[j] - prefix[i]) % k == 0
    //        ↔ prefix[j] % k == prefix[i] % k
    //
    // This makes the modular arithmetic relationship explicit.
    //
    // Time : O(n²) — two nested loops; prefix computed in O(n)
    // Space: O(n)  — prefix array of size n+1
    // ============================================================
    public int longestSubarrayPrefix(int[] arr, int k) {
        int n = arr.length;

        // build prefix sum array
        // prefix[i] = arr[0] + arr[1] + ... + arr[i-1]
        int[] prefix = new int[n + 1];
        prefix[0] = 0;
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }

        int maxLen = 0;

        // check all pairs of prefix indices
        // subarray arr[i..j] = prefix[j+1] - prefix[i]
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int sum = prefix[j + 1] - prefix[i];
                if (sum % k == 0) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 3 — Optimal: Prefix Sum + HashMap ⭐
    //
    // KEY MATH:
    //   sum(i..j) = prefix[j] - prefix[i-1]   (prefix sums)
    //   sum(i..j) % k == 0
    //     ↔ prefix[j] % k == prefix[i-1] % k
    //
    //   So: track the FIRST occurrence of each (prefix % k) value.
    //   If we see the same remainder again at index j,
    //     subarray from firstIndex+1 to j has sum divisible by k.
    //     length = j - firstIndex
    //
    // INITIALIZATION:
    //   map = {0: -1}  ← remainder 0 was first seen at "virtual" index -1
    //   This handles subarrays starting at index 0:
    //     if prefix[j] % k == 0, length = j - (-1) = j + 1 ✅
    //
    // NEGATIVE REMAINDER:
    //   Java: (-7) % 3 = -1  (mathematically incorrect for our purpose)
    //   Fix:  ((prefix % k) + k) % k  → always non-negative
    //
    // ONLY STORE FIRST OCCURRENCE:
    //   We want the LONGEST subarray → earliest start → keep first index.
    //   Never overwrite an existing entry in the map.
    //
    // Visual (arr=[2,7,6,1,4,5], k=3):
    //
    //   map={0:-1}  prefix=0  maxLen=0
    //
    //   i=0: prefix=2    rem=2  new → map={0:-1, 2:0}
    //   i=1: prefix=9    rem=0  seen at -1!  len=1-(-1)=2  maxLen=2
    //   i=2: prefix=15   rem=0  seen at -1!  len=2-(-1)=3  maxLen=3
    //   i=3: prefix=16   rem=1  new → map={0:-1,2:0,1:3}
    //   i=4: prefix=20   rem=2  seen at 0!   len=4-0=4     maxLen=4  ← best!
    //   i=5: prefix=25   rem=1  seen at 3!   len=5-3=2     maxLen=4
    //
    //   Answer: 4 ✅  (arr[1..4] = [7,6,1,4], sum=18, 18%3=0)
    //
    // Time : O(n)  — single pass; HashMap operations are O(1) average
    // Space: O(k)  — at most k distinct remainders (0 to k-1)
    // ============================================================
    public int longestSubarray(int[] arr, int k) {
        // map: remainder → first index at which this remainder was seen
        // Use index -1 for the "before the array" position (prefix sum = 0)
        Map<Integer, Integer> firstOccurrence = new HashMap<>();
        firstOccurrence.put(0, -1); // prefix 0 at virtual index -1

        int prefix = 0;  // running prefix sum
        int maxLen = 0;

        for (int i = 0; i < arr.length; i++) {
            prefix += arr[i];

            // normalize remainder to be non-negative (handles negative numbers)
            // Java's % can return negative: e.g. (-7) % 3 = -1
            // Fix: ((prefix % k) + k) % k → always in [0, k-1]
            int rem = ((prefix % k) + k) % k;

            if (firstOccurrence.containsKey(rem)) {
                // same remainder seen before at firstOccurrence[rem]
                // subarray from firstOccurrence[rem]+1 to i is divisible by k
                int len = i - firstOccurrence.get(rem);
                maxLen = Math.max(maxLen, len);
                // DON'T update map → keep FIRST (earliest) occurrence for longest subarray
            } else {
                // first time seeing this remainder → store current index
                firstOccurrence.put(rem, i);
            }
        }

        return maxLen;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {2, 7, 6, 1, 4, 5},         // k=3 → 4
            {10, 2, -2, -20, 10},        // k=5 → 5 (whole array, sum=0)
            {1, 2, 3},                   // k=6 → 3 (whole array, sum=6)
            {1, 2, 3},                   // k=7 → 0 (no subarray sum div by 7)
            {-2, 2, -8, 1, 7, 10, 23},  // k=5
            {2, 3, 4}                    // k=2 → 2 (subarray [2,4] sum=6? no. [4]=4%2=0 len=1; [2,4] not contiguous. [3,4]=7%2=1. Hmm. [2]=2%2=0 len=1. Hmm actually whole: 9%2=1. Best: [2] or [4] len=1)
        };
        int[]   ks       = {3, 5, 6, 7, 5, 2};
        int[]   expected = {4, 5, 3, 0, 5, 2};
        // Note: test 6 [2,3,4] k=2: [2,3,4] sum=9%2=1. [2,3] sum=5%2=1. [3,4] sum=7%2=1.
        //   [2] sum=2%2=0 len=1. [4] sum=4%2=0 len=1. Hmm expected=2? Let me recalculate.
        //   Actually: [2,3,4] no. Hmm. Let me fix: [4,2]? not contiguous subarray.
        //   Actually wait: I need to double check. arr=[2,3,4], k=2.
        //   Brute: i=0: 2%2=0 len=1; 5%2=1; 9%2=1. i=1: 3%2=1; 7%2=1. i=2: 4%2=0 len=1.
        //   So answer = 1. Let me fix expected[5] = 1 but I already wrote 2. Let me just
        //   use a corrected example in the test to be safe.

        // Override last test to something cleaner
        inputs[5]   = new int[]{4, 2, 6};
        ks[5]       = 2;
        expected[5] = 3; // whole array sum=12, 12%2=0

        System.out.println("=== Longest Subarray with Sum Divisible by K ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int   k   = ks[t];

            int m1 = sol.longestSubarrayBrute(arr, k);    // O(n²)
            int m2 = sol.longestSubarrayPrefix(arr, k);   // O(n²)
            int m3 = sol.longestSubarray(arr, k);          // O(n) ⭐

            boolean pass   = (m3 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            // print array nicely
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                if (i < arr.length - 1) sb.append(", ");
            }
            sb.append("]");

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  arr       : " + sb + ", k=" + k);
            System.out.println("  Expected  : " + expected[t]);
            System.out.println("  Brute O(n²)     : " + m1);
            System.out.println("  Prefix O(n²)    : " + m2);
            System.out.println("  HashMap O(n)    : " + m3 + "  ⭐ optimal");
            System.out.println();
        }
    }
}
