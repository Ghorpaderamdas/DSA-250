package p003;

/**
 * Problem 3: Container With Most Water (LeetCode 11)
 *
 * Given an integer array height of length n, find two lines that together
 * with the x-axis form a container that holds the most water.
 * Return the maximum amount of water a container can store.
 *
 * Pattern: Two Pointers
 * Difficulty: Medium
 *
 * Constraints:
 *   2 <= height.length <= 10^5
 *   0 <= height[i] <= 10^4
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    // Time:  O(n^2)
    // Space: O(1)
    // Try every pair (i, j) and compute water = min(h[i], h[j]) * (j - i)
    // ============================================================
    public int maxAreaBrute(int[] height) {
        int maxWater = 0;
        int n = height.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int water = Math.min(height[i], height[j]) * (j - i);
                maxWater = Math.max(maxWater, water);
            }
        }
        return maxWater;
    }

    // ============================================================
    // Method 2 — Two Pointer (Optimal) ⭐
    // Time:  O(n)
    // Space: O(1)
    // Start with widest container, greedily shrink from the shorter side.
    // Moving the taller side inward can never increase area — only moving
    // the shorter side gives a chance to find a taller boundary.
    // ============================================================
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int water = Math.min(height[left], height[right]) * (right - left);
            maxWater = Math.max(maxWater, water);

            if (height[left] <= height[right]) {
                left++;   // move the shorter wall inward
            } else {
                right--;  // move the shorter wall inward
            }
        }
        return maxWater;
    }

    // ============================================================
    // Method 3 — Two Pointer with Early Exit (Optimised Variant)
    // Time:  O(n)
    // Space: O(1)
    // Same as Method 2, but skip walls shorter than current best boundary
    // to avoid redundant area checks.
    // ============================================================
    public int maxAreaEarlyExit(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;
        int maxLeft = 0, maxRight = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] > maxLeft) {
                    maxLeft = height[left];
                    int water = maxLeft * (right - left);
                    maxWater = Math.max(maxWater, water);
                }
                left++;
            } else {
                if (height[right] > maxRight) {
                    maxRight = height[right];
                    int water = maxRight * (right - left);
                    maxWater = Math.max(maxWater, water);
                }
                right--;
            }
        }
        return maxWater;
    }

    // ============================================================
    // Driver
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {1, 8, 6, 2, 5, 4, 8, 3, 7},
            {1, 1},
            {4, 3, 2, 1, 4},
            {1, 2, 1},
            {2, 3, 4, 5, 18, 17, 6}
        };
        int[] expected = {49, 1, 16, 2, 17};

        System.out.println("=== Brute Force ===");
        for (int i = 0; i < tests.length; i++) {
            int got = sol.maxAreaBrute(tests[i]);
            System.out.println(
                "Test " + (i + 1) + ": height=" + java.util.Arrays.toString(tests[i]) +
                " → " + got +
                (got == expected[i] ? "  ✅" : "  ❌ expected " + expected[i])
            );
        }

        System.out.println("\n=== Two Pointer (Optimal) ===");
        for (int i = 0; i < tests.length; i++) {
            int got = sol.maxArea(tests[i]);
            System.out.println(
                "Test " + (i + 1) + ": height=" + java.util.Arrays.toString(tests[i]) +
                " → " + got +
                (got == expected[i] ? "  ✅" : "  ❌ expected " + expected[i])
            );
        }

        System.out.println("\n=== Two Pointer with Early Exit ===");
        for (int i = 0; i < tests.length; i++) {
            int got = sol.maxAreaEarlyExit(tests[i]);
            System.out.println(
                "Test " + (i + 1) + ": height=" + java.util.Arrays.toString(tests[i]) +
                " → " + got +
                (got == expected[i] ? "  ✅" : "  ❌ expected " + expected[i])
            );
        }
    }
}
