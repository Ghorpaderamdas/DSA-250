package p013;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Count Distinct Elements in Every Window of Size K
 * GFG — Medium
 *
 * Given array arr[] and window size k,
 * return the count of distinct elements in every window of size k.
 *
 * Example: arr=[1,2,1,3,4,2,3], k=4 → [3,4,4,3]
 *   {1,2,1,3}→3 distinct, {2,1,3,4}→4, {1,3,4,2}→4, {3,4,2,3}→3
 *
 * KEY INSIGHT:
 *   Use a HashMap to track frequency of elements in the current window.
 *   map.size() = distinct count at any moment.
 *   When sliding: add incoming element (right), remove outgoing element (left=i-k).
 *   If outgoing element's frequency drops to 0 → remove from map → distinct decreases.
 *
 * Pattern   : Sliding Window (Fixed Size)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : For every window, build a fresh HashSet and count its size.
    //
    // Time : O(n × k) — outer loop × inner scan of k elements
    // Space: O(k)     — HashSet holds at most k elements
    // ============================================================
    public List<Integer> countDistinctBrute(int[] arr, int k) {
        int n = arr.length;
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i <= n - k; i++) {          // start of each window
            Set<Integer> set = new HashSet<>();
            for (int j = i; j < i + k; j++) {       // scan k elements
                set.add(arr[j]);
            }
            result.add(set.size());                  // distinct count for this window
        }

        return result;
    }


    // ============================================================
    // Method 2 — Optimal: Sliding Window + HashMap ⭐
    //
    // Idea : Instead of rebuilding the set from scratch each window,
    //        SLIDE the window:
    //          ADD incoming element:  map[arr[right]]++
    //          REMOVE outgoing element (arr[i-k]):
    //              map[arr[i-k]]--
    //              if freq drops to 0 → remove from map (no longer in window)
    //          map.size() = current distinct count
    //
    // Step 1: Build the FIRST window [0..k-1] and record distinct count.
    // Step 2: Slide from index k to n-1:
    //           - Add arr[i] (new right element)
    //           - Remove arr[i-k] (outgoing left element)
    //           - Record map.size()
    //
    // Visual (arr=[1,2,1,3,4,2,3], k=4):
    //
    //   First window [1,2,1,3]:
    //     add 1 → {1:1}
    //     add 2 → {1:1,2:1}
    //     add 1 → {1:2,2:1}
    //     add 3 → {1:2,2:1,3:1}
    //     distinct = 3  result=[3]
    //
    //   Slide i=4 (add arr[4]=4, remove arr[0]=1):
    //     add 4 → {1:2,2:1,3:1,4:1}
    //     remove 1: freq[1]=2→1 > 0 → keep in map
    //     map = {1:1,2:1,3:1,4:1}  distinct=4  result=[3,4]
    //
    //   Slide i=5 (add arr[5]=2, remove arr[1]=2):
    //     add 2 → {1:1,2:2,3:1,4:1}
    //     remove 2: freq[2]=2→1 > 0 → keep
    //     map = {1:1,2:1,3:1,4:1}  distinct=4  result=[3,4,4]
    //
    //   Slide i=6 (add arr[6]=3, remove arr[2]=1):
    //     add 3 → {1:1,2:1,3:2,4:1}
    //     remove 1: freq[1]=1→0 → REMOVE from map!
    //     map = {2:1,3:2,4:1}  distinct=3  result=[3,4,4,3]
    //
    //   Answer: [3, 4, 4, 3] ✅
    //
    // Time : O(n)  — each element added once, removed once
    // Space: O(k)  — map holds at most k distinct elements
    // ============================================================
    public List<Integer> countDistinct(int[] arr, int k) {
        int n = arr.length;
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> freq = new HashMap<>(); // element → frequency in window

        // STEP 1: Build the first window [0 .. k-1]
        for (int i = 0; i < k; i++) {
            freq.merge(arr[i], 1, Integer::sum); // freq[arr[i]]++
        }
        result.add(freq.size()); // distinct count of first window

        // STEP 2: Slide the window from index k to n-1
        for (int i = k; i < n; i++) {

            // ADD incoming element (new right boundary)
            freq.merge(arr[i], 1, Integer::sum);

            // REMOVE outgoing element (old left boundary = arr[i - k])
            int outgoing = arr[i - k];
            if (freq.get(outgoing) == 1) {
                freq.remove(outgoing); // freq dropped to 0 → remove from map
            } else {
                freq.merge(outgoing, -1, Integer::sum); // just decrement
            }

            // map.size() = number of distinct elements in current window
            result.add(freq.size());
        }

        return result;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {1, 2, 1, 3, 4, 2, 3},
            {1, 2, 4, 4},
            {1, 1, 1, 1, 1},
            {1, 2, 3, 4},
            {1, 2, 3, 4}
        };
        int[]   ks       = {4, 2, 3, 4, 1};
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(3, 4, 4, 3));
        expected.add(Arrays.asList(2, 2, 1));
        expected.add(Arrays.asList(1, 1, 1));
        expected.add(Arrays.asList(4));
        expected.add(Arrays.asList(1, 1, 1, 1));

        System.out.println("=== Count Distinct Elements in Every Window of Size K ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int   k   = ks[t];

            List<Integer> m1 = sol.countDistinctBrute(arr, k);  // O(nk)
            List<Integer> m2 = sol.countDistinct(arr, k);        // O(n) ⭐

            boolean pass   = m2.equals(expected.get(t));
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  arr      : " + Arrays.toString(arr) + ", k=" + k);
            System.out.println("  Expected : " + expected.get(t));
            System.out.println("  Brute    : " + m1);
            System.out.println("  Optimal  : " + m2 + "  ⭐");
            System.out.println();
        }
    }
}
