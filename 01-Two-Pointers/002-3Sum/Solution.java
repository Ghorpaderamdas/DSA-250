package p002;

import java.util.*;

/**
 * Problem 2: 3Sum (LeetCode 15)
 *
 * Given an integer array nums, return all triplets [nums[i], nums[j], nums[k]]
 * such that i != j, i != k, j != k, and nums[i] + nums[j] + nums[k] == 0.
 * The solution set must not contain duplicate triplets.
 *
 * Pattern: Two Pointers (Sort + 2-pointer reduction of 3Sum to 2Sum)
 * Difficulty: Medium
 *
 * Constraints:
 *   3 <= nums.length <= 3000
 *   -10^5 <= nums[i] <= 10^5
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    // Time:  O(n^3)
    // Space: O(n)  (HashSet for dedupe)
    // ============================================================
    public List<List<Integer>> threeSumBrute(int[] nums) {
        Set<List<Integer>> resultSet = new HashSet<>();
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], nums[k]);
                        Collections.sort(triplet);
                        resultSet.add(triplet);
                    }
                }
            }
        }
        return new ArrayList<>(resultSet);
    }

    // ============================================================
    // Method 2 — Hashing (Better)
    // Time:  O(n^2)
    // Space: O(n)
    // ============================================================
    public List<List<Integer>> threeSumHashing(int[] nums) {
        Set<List<Integer>> resultSet = new HashSet<>();
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < n; j++) {
                int need = -(nums[i] + nums[j]);
                if (seen.contains(need)) {
                    List<Integer> triplet = Arrays.asList(nums[i], nums[j], need);
                    Collections.sort(triplet);
                    resultSet.add(triplet);
                }
                seen.add(nums[j]);
            }
        }
        return new ArrayList<>(resultSet);
    }

    // ============================================================
    // Method 3 — Sort + Two Pointer (Optimal) ⭐
    // Time:  O(n^2)
    // Space: O(1) extra (in-place sort)
    // ============================================================
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicate i
            if (nums[i] > 0) break;                         // early exit

            int L = i + 1, R = n - 1;
            while (L < R) {
                int sum = nums[i] + nums[L] + nums[R];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[L], nums[R]));
                    while (L < R && nums[L] == nums[L + 1]) L++; // skip duplicate L
                    while (L < R && nums[R] == nums[R - 1]) R--; // skip duplicate R
                    L++;
                    R--;
                } else if (sum < 0) {
                    L++;
                } else {
                    R--;
                }
            }
        }
        return result;
    }

    // ============================================================
    // Driver
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] tests = {
            {-1, 0, 1, 2, -1, -4},
            {0, 1, 1},
            {0, 0, 0}
        };
        List<List<List<Integer>>> expected = Arrays.asList(
            Arrays.asList(Arrays.asList(-1, -1, 2), Arrays.asList(-1, 0, 1)),
            Collections.emptyList(),
            Collections.singletonList(Arrays.asList(0, 0, 0))
        );

        for (int i = 0; i < tests.length; i++) {
            List<List<Integer>> got = sol.threeSum(tests[i].clone());
            boolean ok = equalsIgnoreOrder(got, expected.get(i));
            System.out.println(
                "Test " + (i + 1) + ": " + Arrays.toString(tests[i]) +
                " → " + got +
                (ok ? "  ✅" : "  ❌ expected " + expected.get(i))
            );
        }
    }

    // Compare two triplet lists ignoring order of triplets
    private static boolean equalsIgnoreOrder(List<List<Integer>> a, List<List<Integer>> b) {
        if (a.size() != b.size()) return false;
        Set<List<Integer>> sa = new HashSet<>(a);
        Set<List<Integer>> sb = new HashSet<>(b);
        return sa.equals(sb);
    }
}
