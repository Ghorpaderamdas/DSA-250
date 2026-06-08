package p017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Problem: Print All Subarrays with 0 Sum
 * GFG — Medium
 *
 * Given array arr[] of integers, find and print ALL contiguous subarrays
 * whose sum is exactly 0. (Multiple subarrays may exist.)
 *
 * KEY DIFFERENCE from "Largest Subarray with 0 Sum":
 *   There: store FIRST occurrence only (to maximize length).
 *   Here : store ALL occurrences (to report every valid subarray).
 *
 * KEY INSIGHT (Prefix Sum + HashMap with List):
 *   If prefix[j] == prefix[i], then sum(arr[i+1 .. j]) = 0.
 *   → HashMap stores:  prefix sum  →  List of ALL indices where seen.
 *   → When prefix[j] equals a previous sum seen at multiple indices,
 *     each gives one valid subarray.
 *
 * Pattern   : Prefix Sum + HashMap (multi-occurrence)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Three nested loops.
    //        For every (start, end) pair, compute sum from scratch.
    //        If sum == 0 → record [start, end].
    //
    // Time : O(n³) — three nested loops
    // Space: O(1)  — no extra memory (output list excluded)
    // ============================================================
    public List<int[]> findSubarraysBrute(int[] arr) {
        int n = arr.length;
        List<int[]> result = new ArrayList<>();

        for (int start = 0; start < n; start++) {
            for (int end = start; end < n; end++) {
                // compute sum of arr[start..end] from scratch
                int sum = 0;
                for (int k = start; k <= end; k++) {
                    sum += arr[k];
                }
                if (sum == 0) {
                    result.add(new int[]{start, end});
                }
            }
        }

        return result;
    }


    // ============================================================
    // Method 2 — Better: Prefix Sum + Two-Loop Pair Check (O(n²))
    //
    // Idea : Precompute prefix array. Check all pairs (i, j):
    //        sum(arr[i..j-1]) = prefix[j] - prefix[i] == 0
    //        ↔  prefix[j] == prefix[i]
    //        → subarray arr[i..j-1] has sum 0, length = j - i.
    //
    // This makes the "equal prefix sums" pattern explicit
    // and sets up the intuition for the O(n) solution.
    //
    // Time : O(n²) — two nested loops; prefix build O(n)
    // Space: O(n)  — prefix array of size n+1
    // ============================================================
    public List<int[]> findSubarraysPrefix(int[] arr) {
        int n = arr.length;
        List<int[]> result = new ArrayList<>();

        // prefix[i] = arr[0] + arr[1] + ... + arr[i-1]
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + arr[i];
        }

        // check every pair (i, j) where i < j
        for (int i = 0; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (prefix[j] == prefix[i]) {
                    // arr[i..j-1] has sum 0
                    result.add(new int[]{i, j - 1});
                }
            }
        }

        return result;
    }


    // ============================================================
    // Method 3 — Optimal: Prefix Sum + HashMap (All Occurrences) ⭐
    //
    // KEY MATH:
    //   prefix[j] == prefix[i]  →  sum(arr[i+1 .. j]) == 0
    //
    //   Unlike the "Largest Subarray" problem, here we need ALL pairs.
    //   → Store ALL indices for each prefix sum in a List.
    //
    //   HashMap: prefix sum → List<Integer> of every index where seen.
    //
    //   When prefix[j] matches, every previous index i in the list gives
    //   a valid subarray: arr[i+1 .. j]  (start = i+1, end = j).
    //
    // INITIALIZATION:
    //   map = {0: [-1]}  ← sum 0 first seen at virtual index -1
    //   If prefix[j] == 0: subarray arr[0..j] has sum 0  (start = -1+1 = 0) ✅
    //
    // Dry Run (arr = [1, 2, -3, 4, -4]):
    //
    //   prefix values : 0, 1, 3, 0, 4, 0
    //   (at indices)  : -1, 0, 1, 2, 3, 4
    //
    //   map={0:[-1]}
    //   i=0: prefix=1   new → map={0:[-1], 1:[0]}
    //   i=1: prefix=3   new → map={..., 3:[1]}
    //   i=2: prefix=0   seen at [-1] → subarray arr[0..2]=[1,2,-3]   ✅
    //         add 2 → map={0:[-1,2], ...}
    //   i=3: prefix=4   new → map={..., 4:[3]}
    //   i=4: prefix=0   seen at [-1,2] → arr[0..4]=[1,2,-3,4,-4] ✅
    //                               → arr[3..4]=[4,-4]              ✅
    //         add 4 → map={0:[-1,2,4], ...}
    //
    //   3 subarrays found ✅
    //
    // Time : O(n + k) — n for the scan; k = total number of result pairs
    //        (worst case O(n²) for all-zeros array, but output is O(n²) too)
    // Space: O(n)     — HashMap with n+1 total entries across all lists
    // ============================================================
    public List<int[]> findSubarrays(int[] arr) {
        int n = arr.length;
        List<int[]> result = new ArrayList<>();

        // map: prefix sum → ALL indices where this prefix sum was seen
        Map<Integer, List<Integer>> occurrences = new HashMap<>();

        // initialize: prefix sum 0 at virtual index -1 (before the array)
        occurrences.put(0, new ArrayList<>(Arrays.asList(-1)));

        int prefix = 0;

        for (int i = 0; i < n; i++) {
            prefix += arr[i];

            if (occurrences.containsKey(prefix)) {
                // every previous index j in the list gives a valid subarray: arr[j+1..i]
                for (int prevIdx : occurrences.get(prefix)) {
                    result.add(new int[]{prevIdx + 1, i});
                }
            }

            // add current index to the list for this prefix sum
            occurrences.computeIfAbsent(prefix, x -> new ArrayList<>()).add(i);
        }

        return result;
    }


    // ============================================================
    // Helper: print a list of subarrays with their elements
    // ============================================================
    private static void printSubarrays(int[] arr, List<int[]> subarrays, String label) {
        System.out.println("  " + label + ": " + subarrays.size() + " subarray(s)");
        for (int[] range : subarrays) {
            StringBuilder sb = new StringBuilder("    arr[")
                .append(range[0]).append("..").append(range[1]).append("] = [");
            for (int k = range[0]; k <= range[1]; k++) {
                sb.append(arr[k]);
                if (k < range[1]) sb.append(", ");
            }
            sb.append("]");
            System.out.println(sb);
        }
    }


    // ============================================================
    // Driver — verify all three methods on the same inputs
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs = {
            {6, 3, -1, -3, 4, -2, 2, 4, 6, -12, -7},  // several subarrays
            {0, 0, 0},                                   // C(3,2)+3 = 6 subarrays
            {1, 2, -3, 4, -4},                           // 3 subarrays
            {1, 2, 3},                                   // 0 subarrays
            {0}                                          // 1 subarray
        };

        System.out.println("=== Print All Subarrays with 0 Sum ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            List<int[]> m1 = sol.findSubarraysBrute(arr);
            List<int[]> m2 = sol.findSubarraysPrefix(arr);
            List<int[]> m3 = sol.findSubarrays(arr);

            boolean pass   = (m1.size() == m3.size());
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status
                + "  arr = " + Arrays.toString(arr));
            printSubarrays(arr, m3, "Optimal (HashMap)");
            System.out.println("  Brute O(n³)  count: " + m1.size());
            System.out.println("  Prefix O(n²) count: " + m2.size());
            System.out.println("  HashMap      count: " + m3.size() + "  ⭐");
            System.out.println();
        }
    }
}
