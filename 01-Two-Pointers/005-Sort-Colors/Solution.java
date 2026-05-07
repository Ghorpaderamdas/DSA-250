package p005;

import java.util.Arrays;

/**
 * Problem 5: Sort Colors (LeetCode 75) — Dutch National Flag
 *
 * Given an array of 0s, 1s, and 2s, sort it in-place so all 0s come first,
 * then all 1s, then all 2s. No library sort allowed.
 *
 * Pattern: Two Pointers (Dutch National Flag)
 * Difficulty: Medium
 *
 * Constraints:
 *   1 <= nums.length <= 300
 *   nums[i] is 0, 1, or 2
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force (Bubble Sort style)
    // Time:  O(n^2)
    // Space: O(1)
    //
    // Repeatedly compare adjacent elements and swap if out of order.
    // ============================================================
    public void sortColorsBrute(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                }
            }
        }
    }

    // ============================================================
    // Method 2 — Counting Sort (Better)
    // Time:  O(n)   two passes
    // Space: O(1)   only three counters
    //
    // Count occurrences of 0, 1, 2 in first pass.
    // Overwrite the array in second pass.
    // ============================================================
    public void sortColorsCounting(int[] nums) {
        int count0 = 0, count1 = 0, count2 = 0;

        for (int num : nums) {
            if      (num == 0) count0++;
            else if (num == 1) count1++;
            else               count2++;
        }

        int idx = 0;
        while (count0-- > 0) nums[idx++] = 0;
        while (count1-- > 0) nums[idx++] = 1;
        while (count2-- > 0) nums[idx++] = 2;
    }

    // ============================================================
    // Method 3 — Dutch National Flag (Optimal) ⭐
    // Time:  O(n)   single pass
    // Space: O(1)
    //
    // Three pointers: low, mid, high
    //   [0..low-1]  → all 0s (red zone)
    //   [low..mid-1]→ all 1s (white zone, confirmed)
    //   [mid..high] → unknown (to be processed)
    //   [high+1..n-1]→ all 2s (blue zone)
    //
    // Invariant is maintained as mid sweeps from left to right.
    // ============================================================
    public void sortColors(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;

        while (mid <= high) {
            if (nums[mid] == 0) {
                swap(nums, low, mid);
                low++;
                mid++;                // nums[low] was confirmed 1, safe to advance
            } else if (nums[mid] == 1) {
                mid++;                // already in correct zone
            } else {                  // nums[mid] == 2
                swap(nums, mid, high);
                high--;               // don't advance mid: swapped-in value is unverified
            }
        }
    }

    // ============================================================
    // Method 4 — Two-Pass Two Pointer (Alternative)
    // Time:  O(n)   two passes
    // Space: O(1)
    //
    // Pass 1: push all 0s to the front using a write pointer.
    // Pass 2: push all 1s right after the 0s using the same idea.
    // 2s fill the remaining space automatically.
    // ============================================================
    public void sortColorsTwoPass(int[] nums) {
        int write = 0;

        // pass 1: place all 0s
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                swap(nums, write, i);
                write++;
            }
        }

        // pass 2: place all 1s (starting right after the 0s)
        for (int i = write; i < nums.length; i++) {
            if (nums[i] == 1) {
                swap(nums, write, i);
                write++;
            }
        }
        // 2s are already in the remaining positions
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // ============================================================
    // Driver
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {2, 0, 2, 1, 1, 0},
            {2, 0, 1},
            {0},
            {1},
            {2, 2, 2, 0, 0, 1},
            {0, 0, 0},
            {1, 1, 1},
            {2, 1, 0}
        };
        int[][] expected = {
            {0, 0, 1, 1, 2, 2},
            {0, 1, 2},
            {0},
            {1},
            {0, 0, 1, 2, 2, 2},
            {0, 0, 0},
            {1, 1, 1},
            {0, 1, 2}
        };

        String[] methods = {"Brute", "Counting", "Dutch NF (Optimal)", "Two-Pass"};

        for (int m = 0; m < methods.length; m++) {
            System.out.println("=== " + methods[m] + " ===");
            for (int i = 0; i < tests.length; i++) {
                int[] copy = tests[i].clone();
                switch (m) {
                    case 0 -> sol.sortColorsBrute(copy);
                    case 1 -> sol.sortColorsCounting(copy);
                    case 2 -> sol.sortColors(copy);
                    case 3 -> sol.sortColorsTwoPass(copy);
                }
                boolean ok = Arrays.equals(copy, expected[i]);
                System.out.println(
                    "  Test " + (i + 1) + ": " + Arrays.toString(tests[i]) +
                    " → " + Arrays.toString(copy) +
                    (ok ? "  ✅" : "  ❌ expected " + Arrays.toString(expected[i]))
                );
            }
            System.out.println();
        }
    }
}
