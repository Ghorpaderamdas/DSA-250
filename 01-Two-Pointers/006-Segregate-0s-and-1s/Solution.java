package p006;

import java.util.Arrays;

/**
 * Problem 6: Segregate 0s and 1s in an Array
 *
 * Given an array containing only 0s and 1s, move all 0s to the left
 * and all 1s to the right — in-place, without using extra space.
 *
 * Example: arr = [0, 1, 0, 1, 1, 1]  →  [0, 0, 1, 1, 1, 1]
 *
 * Pattern   : Two Pointers (Hoare's Partition)
 * Difficulty: Easy
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force (Bubble Sort style)
    //
    // Idea:
    //   Look at every pair of ADJACENT elements (side by side).
    //   If we see a 1 followed by a 0, they are in the wrong order.
    //   Swap them. Repeat this for all pairs, n-1 times.
    //   Each full pass "bubbles" at least one 0 to its correct place.
    //
    // Think of it like sorting cards by hand — slowly push
    //   each 0 leftward one step at a time.
    //
    // Time : O(n^2) — two nested loops
    // Space: O(1)  — only a temp variable for swapping
    // ============================================================
    public void segregateBrute(int[] arr) {
        int n = arr.length;

        // Outer loop: each pass guarantees one more element is in place
        for (int i = 0; i < n - 1; i++) {

            // Inner loop: compare every adjacent pair in this pass
            // We go up to n-i-1 because the last i elements are already sorted
            for (int j = 0; j < n - i - 1; j++) {

                // Is 1 sitting before 0? That's the wrong order — swap!
                if (arr[j] == 1 && arr[j + 1] == 0) {
                    int tmp    = arr[j];
                    arr[j]     = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    // ============================================================
    // Method 2 — Count 0s and Overwrite (Two Passes)
    //
    // Idea:
    //   Pass 1: Go through the array once and count how many 0s exist.
    //   Pass 2: Overwrite the array.
    //             → Fill positions 0 to count0-1 with 0
    //             → Fill positions count0 to n-1  with 1
    //
    // Example:
    //   arr     = [0, 1, 0, 1, 1, 1]
    //   count0  = 2
    //   rewrite → [0, 0, 1, 1, 1, 1]
    //
    // Limitation:
    //   We are OVERWRITING values, not rearranging them.
    //   If 0 and 1 were labels for real objects (e.g., students),
    //   we would lose the original objects — this approach fails there.
    //
    // Time : O(n) — two separate passes
    // Space: O(1) — just one counter variable
    // ============================================================
    public void segregateCount(int[] arr) {
        // Pass 1: count how many 0s are in the array
        int count0 = 0;
        for (int x : arr) {
            if (x == 0) {
                count0++;
            }
        }

        // Pass 2: write 0s first, then 1s
        for (int i = 0; i < count0; i++) {
            arr[i] = 0;                   // fill the left part with 0s
        }
        for (int i = count0; i < arr.length; i++) {
            arr[i] = 1;                   // fill the right part with 1s
        }
    }

    // ============================================================
    // Method 3 — Two Pointer Swap (One Pass)
    //
    // Idea:
    //   Place one pointer (left) at the start and one (right) at the end.
    //   left  → looks for a misplaced 1 (a 1 that is too far left)
    //   right → looks for a misplaced 0 (a 0 that is too far right)
    //   When both are found, swap them — both land in the correct half.
    //   Stop when the pointers meet or cross.
    //
    // Visual for arr = [0, 1, 0, 1, 1, 1]:
    //
    //   [0,  1,  0,  1,  1,  1]
    //    ↑                   ↑
    //   left               right
    //
    //   left  → skip 0 at index 0 → stop at index 1 (found a 1)
    //   right → skip 1 at index 5 → skip 1 at index 4 → skip 1 at index 3
    //         → stop at index 2 (found a 0)
    //   left(1) < right(2) → swap arr[1] and arr[2]
    //   arr = [0, 0, 1, 1, 1, 1]
    //
    //   Next: left moves to 2, right moves to 1 → left > right → STOP ✅
    //
    // Time : O(n) — single pass
    // Space: O(1)
    // ============================================================
    public void segregateTwoPointer(int[] arr) {
        int left  = 0;                // start from the beginning
        int right = arr.length - 1;  // start from the end

        while (left < right) {

            // Move left forward past all correct 0s
            while (left < right && arr[left] == 0) {
                left++;
            }

            // Move right backward past all correct 1s
            while (left < right && arr[right] == 1) {
                right--;
            }

            // arr[left] is a misplaced 1, arr[right] is a misplaced 0
            // Swap them — both are now in the correct half
            if (left < right) {
                arr[left]  = 0;
                arr[right] = 1;
                left++;
                right--;
            }
        }
    }

    // ============================================================
    // Method 4 — Hoare's Partition (Optimal) ⭐
    //
    // Idea:
    //   Same two-pointer concept as Method 3, but uses do-while loops
    //   which move the pointer BEFORE checking the value.
    //   Because of this, we initialize lo one step BEFORE the array
    //   (lo = -1) and hi one step AFTER the array (hi = n).
    //
    //   lo → advances right until it finds a misplaced 1
    //   hi → retreats left  until it finds a misplaced 0
    //   Swap → both elements go to their correct side
    //   Stop when lo >= hi (pointers have crossed — all done)
    //
    // Why start at lo=-1 and hi=n?
    //   do { lo++; } → moves lo FIRST, then checks.
    //   If we started lo at 0, the first element would be skipped.
    //   Starting at -1 means the first do-step lands us at index 0 correctly.
    //
    // This is the core idea behind QuickSort's partition step.
    //
    // Time : O(n) — each element is visited at most once
    // Space: O(1)
    // ============================================================
    public void segregate0and1(int[] arr) {
        int n  = arr.length;
        int lo = -1;   // one step before the array (do-while moves first)
        int hi = n;    // one step after  the array (do-while moves first)

        while (true) {
            // Advance lo right, skipping 0s that are already on the left side
            do { lo++; } while (lo < n && arr[lo] == 0);

            // Retreat hi left, skipping 1s that are already on the right side
            do { hi--; } while (hi >= 0 && arr[hi] == 1);

            // Pointers have crossed → no misplaced elements remain
            if (lo >= hi) {
                break;
            }

            // arr[lo] = misplaced 1 (too far left)
            // arr[hi] = misplaced 0 (too far right)
            // Swap both into their correct halves at once
            int tmp = arr[lo];
            arr[lo] = arr[hi];
            arr[hi] = tmp;
        }
    }

    // ============================================================
    // Driver — runs all test cases and verifies each method
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {0, 1, 0, 1, 0, 0, 1, 1, 1, 0},   // mixed, larger array
            {0, 1, 0},                           // simple 3 elements
            {1, 1},                              // all 1s
            {0},                                 // single element
            {1, 0, 1, 0, 1}                      // starts and ends with 1
        };
        int[][] expected = {
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 1},
            {1, 1},
            {0},
            {0, 0, 1, 1, 1}
        };

        String[] methods = {
            "Brute (Bubble Sort)",
            "Count + Overwrite",
            "Two Pointer Swap",
            "Hoare's Partition (Optimal)"
        };

        for (int m = 0; m < methods.length; m++) {
            System.out.println("=== " + methods[m] + " ===");
            for (int i = 0; i < tests.length; i++) {
                int[] copy = tests[i].clone();   // fresh copy for each test
                switch (m) {
                    case 0 -> sol.segregateBrute(copy);
                    case 1 -> sol.segregateCount(copy);
                    case 2 -> sol.segregateTwoPointer(copy);
                    case 3 -> sol.segregate0and1(copy);
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
