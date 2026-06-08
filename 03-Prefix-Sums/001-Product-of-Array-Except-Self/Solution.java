package p015;

import java.util.Arrays;

/**
 * Problem: Product of Array Except Self
 * LeetCode 238 — Medium
 *
 * Given integer array nums[], return array answer[] such that answer[i]
 * equals the product of ALL elements of nums EXCEPT nums[i].
 *
 * Constraint: Must run in O(n) time. Division is NOT allowed.
 *
 * KEY INSIGHT (Prefix × Suffix Products):
 *   answer[i] = (product of all elements LEFT of i)
 *             × (product of all elements RIGHT of i)
 *
 *   Build prefix products left-to-right, then multiply by suffix
 *   products right-to-left using a single running variable.
 *   No division needed. O(1) extra space.
 *
 * Visual (nums = [1, 2, 3, 4]):
 *
 *   Left  products : [1,  1,  2,  6 ]   ← nothing | 1 | 1×2 | 1×2×3
 *   Right products : [24, 12,  4,  1 ]   ← 2×3×4  | 3×4 |  4 | nothing
 *   answer         : [24, 12,  8,  6 ]   ← multiply element-wise ✅
 *
 * Pattern   : Prefix Products
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : For each index i, multiply every element EXCEPT nums[i].
    //        Simple but slow: n × (n-1) multiplications total.
    //
    // Time : O(n²) — outer loop n, inner loop n-1
    // Space: O(1)  — no extra memory (output array excluded by convention)
    // ============================================================
    public int[] productExceptSelfBrute(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {            // skip the element we're computing for
                    product *= nums[j];
                }
            }
            result[i] = product;
        }

        return result;
    }


    // ============================================================
    // Method 2 — Better: Division Trick (O(n)) — conceptual only
    //
    // Idea : Multiply all elements → totalProduct.
    //        answer[i] = totalProduct / nums[i]
    //
    //        Special handling for zeros (division by zero is illegal):
    //          Two or more zeros  → every answer is 0
    //          Exactly one zero   → only the zero-position gets a nonzero answer
    //          No zeros           → divide freely
    //
    // NOTE: LeetCode explicitly FORBIDS division for this problem.
    //       Shown here to understand WHY the optimal avoids it.
    //
    // Time : O(n) — two passes
    // Space: O(1) — a few scalar variables
    // ============================================================
    public int[] productExceptSelfDivision(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        int zeroCount          = 0;
        int productWithoutZeros = 1;

        for (int num : nums) {
            if (num == 0) zeroCount++;
            else          productWithoutZeros *= num;
        }

        for (int i = 0; i < n; i++) {
            if      (zeroCount > 1)    result[i] = 0;                         // two+ zeros → always 0
            else if (zeroCount == 1)   result[i] = (nums[i] == 0) ? productWithoutZeros : 0;
            else                       result[i] = productWithoutZeros / nums[i]; // no zeros → safe divide
        }

        return result;
    }


    // ============================================================
    // Method 3 — Optimal: Prefix × Suffix Products ⭐
    //
    // KEY IDEA:
    //   answer[i] = LEFT[i]  ×  RIGHT[i]
    //
    //   Instead of building two separate arrays (O(n) extra space),
    //   we use the OUTPUT array itself to store left products,
    //   then do a single right-to-left pass with a running "suffix" variable.
    //
    //   STEP 1 — Left pass:
    //     result[0] = 1                         (nothing to the left)
    //     result[i] = result[i-1] * nums[i-1]   (extend left product)
    //
    //   STEP 2 — Right pass (running suffix multiplier):
    //     suffix = 1                             (nothing to the right)
    //     for i from n-1 down to 0:
    //       result[i] *= suffix                  (combine left × right)
    //       suffix    *= nums[i]                 (grow suffix leftward)
    //
    // Dry Run (nums = [1, 2, 3, 4]):
    //
    //   After left pass:
    //     result = [1, 1, 2, 6]
    //
    //   Right pass:
    //     i=3: result[3] = 6  × 1  = 6    suffix = 1×4  = 4
    //     i=2: result[2] = 2  × 4  = 8    suffix = 4×3  = 12
    //     i=1: result[1] = 1  × 12 = 12   suffix = 12×2 = 24
    //     i=0: result[0] = 1  × 24 = 24   suffix = 24×1 = 24
    //
    //   result = [24, 12, 8, 6] ✅
    //
    // Time : O(n)  — two passes
    // Space: O(1)  — only the output array (O(n) output not counted as extra)
    // ============================================================
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // STEP 1: fill result with left products
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // STEP 2: multiply by running right (suffix) products
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] = result[i] * suffix;  // left × right
            suffix    = suffix * nums[i];    // extend suffix leftward
        }

        return result;
    }


    // ============================================================
    // Driver — verify all three methods on the same inputs
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs = {
            {1, 2, 3, 4},         // [24, 12, 8, 6]
            {-1, 1, 0, -3, 3},    // [0, 0, 9, 0, 0]
            {0, 0},               // [0, 0]
            {1, 0},               // [0, 1]
            {2, 3, 4, 5}          // [60, 40, 30, 24]
        };
        int[][] expected = {
            {24, 12, 8, 6},
            {0, 0, 9, 0, 0},
            {0, 0},
            {0, 1},
            {60, 40, 30, 24}
        };

        System.out.println("=== Product of Array Except Self ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int[] m1  = sol.productExceptSelfBrute(arr);
            int[] m2  = sol.productExceptSelfDivision(arr);
            int[] m3  = sol.productExceptSelf(arr);

            boolean pass   = Arrays.equals(m3, expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  Input        : " + Arrays.toString(arr));
            System.out.println("  Expected     : " + Arrays.toString(expected[t]));
            System.out.println("  Brute O(n²)  : " + Arrays.toString(m1));
            System.out.println("  Division O(n): " + Arrays.toString(m2));
            System.out.println("  Optimal O(n) : " + Arrays.toString(m3) + "  ⭐");
            System.out.println();
        }
    }
}
