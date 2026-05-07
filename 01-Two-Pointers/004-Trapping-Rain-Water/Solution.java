package p004;

import java.util.Stack;

/**
 * Problem 4: Trapping Rain Water (LeetCode 42)
 *
 * Given n non-negative integers representing an elevation map where the width
 * of each bar is 1, compute how much water it can trap after raining.
 *
 * Pattern: Two Pointers / Prefix-Suffix / Monotonic Stack
 * Difficulty: Hard
 *
 * Constraints:
 *   1 <= height.length <= 2 * 10^4
 *   0 <= height[i] <= 10^5
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    // Time:  O(n^2)
    // Space: O(1)
    //
    // For every bar i, scan LEFT to find leftMax, scan RIGHT to find rightMax.
    // Water at i = max(0, min(leftMax, rightMax) - height[i])
    // ============================================================
    public int trapBrute(int[] height) {
        int n = height.length;
        int totalWater = 0;

        for (int i = 0; i < n; i++) {
            // find max height to the left of i (including i)
            int leftMax = 0;
            for (int l = 0; l <= i; l++) {
                leftMax = Math.max(leftMax, height[l]);
            }

            // find max height to the right of i (including i)
            int rightMax = 0;
            for (int r = i; r < n; r++) {
                rightMax = Math.max(rightMax, height[r]);
            }

            // water level = min ceiling − bar height
            totalWater += Math.min(leftMax, rightMax) - height[i];
        }
        return totalWater;
    }

    // ============================================================
    // Method 2 — Precompute Prefix/Suffix Arrays (Better)
    // Time:  O(n)
    // Space: O(n)
    //
    // Precompute leftMax[i] = max height in height[0..i]
    //           rightMax[i] = max height in height[i..n-1]
    // Then answer = sum of (min(leftMax[i], rightMax[i]) - height[i])
    // ============================================================
    public int trapPrefixSuffix(int[] height) {
        int n = height.length;
        int[] leftMax  = new int[n];
        int[] rightMax = new int[n];

        // fill leftMax: running max from left
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }

        // fill rightMax: running max from right
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }

        // accumulate water
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            totalWater += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return totalWater;
    }

    // ============================================================
    // Method 3 — Two Pointer (Optimal) ⭐
    // Time:  O(n)
    // Space: O(1)
    //
    // Key insight: if height[left] <= height[right], the water at `left`
    // is fully determined by maxLeft (because rightMax >= height[right] >= height[left]).
    // We don't need to know the actual rightMax — just knowing it is >= maxLeft is enough.
    // Symmetrically for the right side.
    // ============================================================
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int maxLeft = 0, maxRight = 0;
        int totalWater = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= maxLeft) {
                    maxLeft = height[left];   // new left boundary found, no water here
                } else {
                    totalWater += maxLeft - height[left];  // trapped below maxLeft ceiling
                }
                left++;
            } else {
                if (height[right] >= maxRight) {
                    maxRight = height[right]; // new right boundary found, no water here
                } else {
                    totalWater += maxRight - height[right]; // trapped below maxRight ceiling
                }
                right--;
            }
        }
        return totalWater;
    }

    // ============================================================
    // Method 4 — Monotonic Stack
    // Time:  O(n)
    // Space: O(n)
    //
    // Think horizontally: find each "valley" between two taller bars.
    // Stack stores indices of bars in decreasing height order.
    // When we find a taller bar, we pop and compute water in the pocket.
    // ============================================================
    public int trapStack(int[] height) {
        int n = height.length;
        Stack<Integer> stack = new Stack<>(); // stores indices, heights are decreasing
        int totalWater = 0;

        for (int right = 0; right < n; right++) {
            // pop every bar that is shorter than height[right]
            while (!stack.isEmpty() && height[right] > height[stack.peek()]) {
                int bottomIdx = stack.pop();          // the valley floor

                if (stack.isEmpty()) break;           // no left wall

                int leftIdx  = stack.peek();          // left wall index
                int width    = right - leftIdx - 1;
                int waterHeight = Math.min(height[leftIdx], height[right]) - height[bottomIdx];
                totalWater  += width * waterHeight;
            }
            stack.push(right);
        }
        return totalWater;
    }

    // ============================================================
    // Driver
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1},
            {4, 2, 0, 3, 2, 5},
            {3, 0, 0, 2, 0, 4},
            {1, 0, 1},
            {3, 1, 2, 4, 0, 1, 3, 2}
        };
        int[] expected = {6, 9, 10, 1, 11};

        String[] methods = {"Brute", "Prefix/Suffix", "Two Pointer", "Stack"};

        for (int m = 0; m < methods.length; m++) {
            System.out.println("=== " + methods[m] + " ===");
            for (int i = 0; i < tests.length; i++) {
                int got = switch (m) {
                    case 0 -> sol.trapBrute(tests[i]);
                    case 1 -> sol.trapPrefixSuffix(tests[i]);
                    case 2 -> sol.trap(tests[i]);
                    default -> sol.trapStack(tests[i]);
                };
                System.out.println(
                    "  Test " + (i + 1) + ": " + java.util.Arrays.toString(tests[i]) +
                    " → " + got +
                    (got == expected[i] ? "  ✅" : "  ❌ expected " + expected[i])
                );
            }
            System.out.println();
        }
    }
}
