package p001;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Problem 1: Pair Sum in a Sorted and Rotated Array
 *
 * Given a sorted-and-then-rotated array of positive integers and a target,
 * check if any two elements sum to the target.
 *
 * Example: arr = [7, 9, 1, 3, 5], target = 6 → true  (1 + 5 = 6)
 *
 * Pattern   : Two Pointers (circular for rotation)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    // Idea : Try every possible pair (i, j) and check their sum.
    // Time : O(n^2) — nested loops
    // Space: O(1)  — no extra memory
    // ============================================================
    public boolean pairInSortedRotatedBrute(int[] arr, int target) {
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] + arr[j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // ============================================================
    // Method 2 — HashSet (Better)
    // Idea : For each number, its "complement" (target - number)
    //        must also exist in the array to form a valid pair.
    //        We use a HashSet to check this in O(1) time.
    // Time : O(n)  — single loop
    // Space: O(n)  — HashSet stores up to n elements
    // ============================================================
    public boolean pairInSortedRotatedHash(int[] arr, int target) {
        HashSet<Integer> seen = new HashSet<>();

        for (int num : arr) {
            int complement = target - num;

            // If complement was seen earlier, we found our pair
            if (seen.contains(complement)) {
                return true;
            }

            // Otherwise, remember this number for future iterations
            seen.add(num);
        }
        return false;
    }

    // ============================================================
    // Method 3 — Two Pointer Circular (Optimal) ⭐
    // Idea : The array was originally sorted, so we can use two
    //        pointers at the smallest and largest elements.
    //        Since the array is rotated, we use circular indexing
    //        (% n) to wrap around the ends.
    //
    // Step 1: Find the pivot = index of the minimum element.
    //         The minimum element is where the array "drops" (arr[i] < arr[i-1]).
    //         lo → smallest element (pivot)
    //         hi → largest element  (just before pivot, circularly)
    //
    // Step 2: Classic two-pointer:
    //         sum == target → found!
    //         sum < target  → move lo forward (need bigger left value)
    //         sum > target  → move hi backward (need smaller right value)
    //         Stop when lo and hi meet (all pairs checked).
    //
    // Circular index formulas:
    //   move lo right → lo = (lo + 1) % n
    //   move hi left  → hi = (hi - 1 + n) % n   ← +n avoids negative index
    //
    // Time : O(n)  — one pass to find pivot + one pass for two pointers
    // Space: O(1)  — only a few variables
    // ============================================================
    public boolean pairInSortedRotated(int[] arr, int target) {
        int n = arr.length;

        // Step 1: Find pivot (index of the minimum element)
        // The drop point is where arr[i] < arr[i-1]
        int lo = 0;
        for (int i = 1; i < n; i++) {
            if (arr[i] < arr[i - 1]) {
                lo = i;
                break;
            }
        }

        // hi is the index just before lo — that's the maximum element
        // Using (lo - 1 + n) % n handles the case where lo = 0
        int hi = (lo - 1 + n) % n;

        // Step 2: Two-pointer scan with circular movement
        while (lo != hi) {
            int sum = arr[lo] + arr[hi];

            if (sum == target) {
                return true;
            } else if (sum < target) {
                lo = (lo + 1) % n;       // sum too small → increase lo
            } else {
                hi = (hi - 1 + n) % n;   // sum too big  → decrease hi
            }
        }
        return false;
    }

    // ============================================================
    // Method 4 — Sort + Two Pointer (Alternative)
    // Idea : Sort the array first (rotation no longer matters),
    //        then use the standard two-pointer technique.
    //        We clone the array to avoid modifying the original.
    // Time : O(n log n) — sorting dominates
    // Space: O(n)       — for the cloned array
    // ============================================================
    public boolean pairInSortedRotatedSort(int[] arr, int target) {
        int[] sorted = arr.clone();    // copy so we don't modify original
        Arrays.sort(sorted);

        int lo = 0;
        int hi = sorted.length - 1;

        while (lo < hi) {
            int sum = sorted[lo] + sorted[hi];

            if (sum == target) {
                return true;
            } else if (sum < target) {
                lo++;    // sum too small → move left pointer right
            } else {
                hi--;    // sum too big  → move right pointer left
            }
        }
        return false;
    }

    // ============================================================
    // Driver — runs all test cases and prints results
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests   = { {7, 9, 1, 3, 5}, {2, 3, 4, 1}, {10, 7, 4, 1} };
        int[]   targets = { 6,                3,             9             };
        boolean[] expected = { true,           true,          false         };

        System.out.println("=== Pair Sum in Sorted Rotated Array ===\n");

        for (int i = 0; i < tests.length; i++) {
            boolean result = sol.pairInSortedRotated(tests[i], targets[i]);
            String status = (result == expected[i]) ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (i + 1) + ": " + status);
            System.out.println("  Array  : " + Arrays.toString(tests[i]));
            System.out.println("  Target : " + targets[i]);
            System.out.println("  Result : " + result);
            System.out.println();
        }
    }
}
