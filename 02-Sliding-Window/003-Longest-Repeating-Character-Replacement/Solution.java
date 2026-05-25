package p003;

/**
 * Problem: Longest Repeating Character Replacement
 * LeetCode #424 — Medium
 *
 * Given a string s (uppercase letters only) and integer k,
 * you may replace at most k characters with any uppercase letter.
 * Return the length of the longest substring where all chars are the same
 * after at most k replacements.
 *
 * KEY INSIGHT:
 *   replacements needed in a window = windowLength - maxFreq
 *   where maxFreq = frequency of the most common character in the window.
 *   A window is VALID if: windowLength - maxFreq <= k
 *
 * Example: s = "AABABBA", k = 1 → 4
 *          s = "ABAB",    k = 2 → 4
 *
 * Pattern   : Sliding Window (Variable Size)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Check every possible substring s[i..j].
    //        For each, count frequencies and find maxFreq.
    //        If (length - maxFreq) <= k → valid window.
    //        Track the maximum valid window length.
    //
    // Time : O(n³) — two nested loops × O(n) freq count per pair
    //               (with the 26-char loop inside)
    // Space: O(1)  — freq array is fixed size 26
    // ============================================================
    public int characterReplacementBrute(String s, int k) {
        int n = s.length();
        int maxLen = 0;

        // try every starting index i
        for (int i = 0; i < n; i++) {

            int[] freq = new int[26]; // frequency of each char in window s[i..j]

            // try every ending index j (inclusive)
            for (int j = i; j < n; j++) {
                freq[s.charAt(j) - 'A']++; // add s[j] to window

                // find the most frequent character in current window
                int maxFreq = 0;
                for (int c = 0; c < 26; c++) {
                    maxFreq = Math.max(maxFreq, freq[c]);
                }

                int windowLen = j - i + 1;

                // replacements needed = windowLen - maxFreq
                // valid if replacements needed <= k
                if (windowLen - maxFreq <= k) {
                    maxLen = Math.max(maxLen, windowLen);
                }
                // if invalid, we could break early (optional optimization)
                // but since adding more chars might increase maxFreq, we continue
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 2 — Better: Two Pointers (O(n²))
    //
    // Idea : Fix the starting index i.
    //        Extend j rightward, maintaining a LIVE frequency count.
    //        Also track maxFreq as we go — no need for inner 26-loop.
    //        Check validity at each j in O(1).
    //
    // Improvement over Brute Force:
    //   - freq[] is updated incrementally (no recompute from scratch)
    //   - maxFreq is updated in O(1) per step
    //   → removes one O(n) factor → O(n²) total
    //
    // Time : O(n²) — outer loop × inner loop
    // Space: O(1)  — freq array of size 26 (reset each outer iteration)
    // ============================================================
    public int characterReplacementBetter(String s, int k) {
        int n = s.length();
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int[] freq = new int[26]; // fresh freq count for each new start
            int maxFreq = 0;

            for (int j = i; j < n; j++) {
                int c = s.charAt(j) - 'A';
                freq[c]++;

                // maxFreq can only go up (we're adding one char per step)
                maxFreq = Math.max(maxFreq, freq[c]);

                int windowLen = j - i + 1;

                // valid if replacements needed (windowLen - maxFreq) <= k
                if (windowLen - maxFreq <= k) {
                    maxLen = Math.max(maxLen, windowLen);
                }
                // Note: we do NOT break when invalid because adding more
                // of the dominant character could make it valid again.
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 3 — Sliding Window ⭐ (Optimal)
    //
    // Idea : Keep a live window [left..right].
    //        Expand right: add s[right] to freq, update maxFreq.
    //        If window becomes INVALID (windowLen - maxFreq > k):
    //            shrink left by exactly 1 (remove s[left], left++)
    //        Answer grows only when maxFreq increases.
    //
    // KEY TRICK: maxFreq NEVER DECREASES.
    //   When we shrink the window, we don't recompute maxFreq downward.
    //   Why? Because we're searching for a window LARGER than the current best.
    //   If maxFreq dropped, the window would need to shrink more → no improvement.
    //   So we simply keep maxFreq as-is until a new char exceeds it.
    //
    // Visual (s = "AABABBA", k = 1):
    //
    //   right=0: add A  freq={A:1}      maxFreq=1  len=1  1-1=0≤1 ✅  maxLen=1
    //   right=1: add A  freq={A:2}      maxFreq=2  len=2  2-2=0≤1 ✅  maxLen=2
    //   right=2: add B  freq={A:2,B:1}  maxFreq=2  len=3  3-2=1≤1 ✅  maxLen=3
    //   right=3: add A  freq={A:3,B:1}  maxFreq=3  len=4  4-3=1≤1 ✅  maxLen=4
    //   right=4: add B  freq={A:3,B:2}  maxFreq=3  len=5  5-3=2>1 ❌
    //            remove s[0]=A → freq={A:2,B:2}  left=1   len=4  maxLen=4
    //   right=5: add B  freq={A:2,B:3}  maxFreq=3  len=5  5-3=2>1 ❌
    //            remove s[1]=A → freq={A:1,B:3}  left=2   len=4  maxLen=4
    //   right=6: add A  freq={A:2,B:3}  maxFreq=3  len=5  5-3=2>1 ❌
    //            remove s[2]=B → freq={A:2,B:2}  left=3   len=4  maxLen=4
    //
    //   Answer: 4 ✅
    //
    // Time : O(n)  — single pass; each element processed once by right,
    //                at most once removed by left
    // Space: O(1)  — freq array of fixed size 26
    // ============================================================
    public int characterReplacement(String s, int k) {
        int n = s.length();
        int left = 0;
        int maxFreq = 0; // max frequency of any single char in current window
        int maxLen = 0;

        int[] freq = new int[26]; // freq[i] = count of ('A' + i) in current window

        for (int right = 0; right < n; right++) {
            int c = s.charAt(right) - 'A';
            freq[c]++;

            // update maxFreq — only ever increases or stays the same
            // (intentional: we do NOT decrease maxFreq when shrinking)
            maxFreq = Math.max(maxFreq, freq[c]);

            // check if current window is valid
            // windowLen = right - left + 1
            // replacements needed = windowLen - maxFreq
            if ((right - left + 1) - maxFreq > k) {
                // window is invalid → shrink from left by exactly 1
                freq[s.charAt(left) - 'A']--;
                left++;
                // Note: we use IF (not WHILE) because:
                // - we shrink by 1 to maintain window size
                // - window can't become valid again by shrinking more
                //   (that would reduce size below current best → no gain)
            }

            // window [left..right] is now valid (or same size as best)
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }


    // ============================================================
    // Method 3b — Sliding Window with WHILE (cleaner but O(2n))
    //
    // Same idea as Method 3 but uses a while loop to shrink.
    // maxFreq IS recomputed correctly here (full 26-char scan).
    // This is easier to understand but slightly slower.
    //
    // Time : O(26n) = O(n)  — 26-char scan inside while, but total
    //                          shrinks ≤ n → still linear overall
    // Space: O(1)
    // ============================================================
    public int characterReplacementWhileVersion(String s, int k) {
        int n = s.length();
        int left = 0;
        int maxLen = 0;
        int[] freq = new int[26];

        for (int right = 0; right < n; right++) {
            freq[s.charAt(right) - 'A']++;

            // shrink window until it's valid again
            while (getMaxFreq(freq) < (right - left + 1) - k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    /**
     * Helper: returns the maximum value in a frequency array.
     * O(26) = O(1) since array is fixed size.
     */
    private int getMaxFreq(int[] freq) {
        int max = 0;
        for (int count : freq) {
            max = Math.max(max, count);
        }
        return max;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        String[] inputs   = { "ABAB", "AABABBA", "AAAA", "ABCD", "ABCD", "A",    "BAAAB" };
        int[]    ks       = {    2,       1,        0,      0,      4,     1,       0     };
        int[]    expected = {    4,       4,        4,      1,      4,     1,       3     };

        System.out.println("=== Longest Repeating Character Replacement ===\n");

        for (int t = 0; t < inputs.length; t++) {
            String s = inputs[t];
            int    k = ks[t];

            int m1 = sol.characterReplacementBrute(s, k);        // O(n³)
            int m2 = sol.characterReplacementBetter(s, k);       // O(n²)
            int m3 = sol.characterReplacement(s, k);             // O(n) ⭐
            int m3b = sol.characterReplacementWhileVersion(s, k); // O(n) while

            boolean pass   = (m3 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  Input        : \"" + s + "\", k=" + k);
            System.out.println("  Expected     : " + expected[t]);
            System.out.println("  Brute O(n³)  : " + m1);
            System.out.println("  Better O(n²) : " + m2);
            System.out.println("  Sliding O(n) : " + m3 + "  ⭐ optimal (if-shrink)");
            System.out.println("  While  O(n)  : " + m3b + "  (while-shrink, easier to read)");
            System.out.println();
        }
    }
}
