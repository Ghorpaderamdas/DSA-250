import java.util.Arrays;
import java.util.HashSet;

/**
 * Problem 1: Pair Sum in a Sorted and Rotated Array
 *
 * Given a sorted-and-then-rotated array of positive integers and a target,
 * check if any pair of elements sums to the target.
 *
 * Pattern: Two Pointers (with circular twist for rotation)
 * Difficulty: Medium
 *
 * Constraints:
 *   2 <= arr.length <= 10^6
 *   1 <= arr[i] <= 10^6
 *   1 <= target <= 10^6
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    // Time:  O(n^2)
    // Space: O(1)
    // ============================================================
    public boolean pairInSortedRotatedBrute(int[] arr, int target) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] + arr[j] == target) return true;
            }
        }
        return false;
    }

    // ============================================================
    // Method 2 — HashSet (Better)
    // Time:  O(n)
    // Space: O(n)
    // ============================================================
    public boolean pairInSortedRotatedHash(int[] arr, int target) {
        HashSet<Integer> seen = new HashSet<>();
        for (int num : arr) {
            if (seen.contains(target - num)) return true;
            seen.add(num);
        }
        return false;
    }

    // ============================================================
    // Method 3 — Two Pointer Circular (Optimal)
    // Time:  O(n)
    // Space: O(1)
    // ============================================================
    public boolean pairInSortedRotated(int[] arr, int target) {
        int n = arr.length;

        // Step 1: Find pivot (index of minimum element)
        int lo = 0;
        for (int i = 1; i < n; i++) {
            if (arr[i] < arr[i - 1]) {
                lo = i;
                break;
            }
        }
        int hi = (lo - 1 + n) % n;

        // Step 2: Two-pointer scan with circular indices
        while (lo != hi) {
            int sum = arr[lo] + arr[hi];
            if (sum == target) return true;
            if (sum < target) {
                lo = (lo + 1) % n;
            } else {
                hi = (hi - 1 + n) % n;
            }
        }
        return false;
    }

    // ============================================================
    // Method 4 — Sort + Two Pointer (Alternative)
    // Time:  O(n log n)
    // Space: O(1)  (in-place sort modifies input)
    // ============================================================
    public boolean pairInSortedRotatedSort(int[] arr, int target) {
        int[] copy = arr.clone();
        Arrays.sort(copy);
        int lo = 0, hi = copy.length - 1;
        while (lo < hi) {
            int sum = copy[lo] + copy[hi];
            if (sum == target) return true;
            if (sum < target) lo++;
            else hi--;
        }
        return false;
    }

    // ============================================================
    // Driver
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {7, 9, 1, 3, 5},
            {2, 3, 4, 1},
            {10, 7, 4, 1}
        };
        int[] targets = {6, 3, 9};
        boolean[] expected = {true, true, false};

        for (int i = 0; i < tests.length; i++) {
            boolean got = sol.pairInSortedRotated(tests[i], targets[i]);
            System.out.println(
                "Test " + (i + 1) + ": " + Arrays.toString(tests[i]) +
                " target=" + targets[i] +
                " → " + got +
                (got == expected[i] ? "  ✅" : "  ❌")
            );
        }
    }
}
