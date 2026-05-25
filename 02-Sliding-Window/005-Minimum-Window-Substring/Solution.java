package p005;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Minimum Window Substring
 * LeetCode #76 — Hard
 *
 * Given strings s and t, find the minimum window in s that contains
 * all characters of t (including duplicates).
 * Return "" if no such window exists.
 *
 * Example: s = "ADOBECODEBANC", t = "ABC" → "BANC"
 *
 * KEY INSIGHT:
 *   Track 'have' (unique chars in window meeting t's count)
 *   and 'need' (total unique chars in t).
 *   Window is VALID when have == need.
 *   Expand right → shrink left while valid → track minimum.
 *
 * Pattern   : Sliding Window (Variable Size)
 * Difficulty: Hard
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Try every substring of s, check if it contains all of t.
    //        Track the shortest valid substring.
    //
    // Time : O(n² × m) — n² substrings × O(m) check for each
    // Space: O(m)      — frequency map of t
    // ============================================================
    public String minWindowBrute(String s, String t) {
        int n = s.length();
        String result = "";

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                String sub = s.substring(i, j + 1);
                if (containsAll(sub, t)) {
                    if (result.isEmpty() || sub.length() < result.length()) {
                        result = sub;
                    }
                }
            }
        }

        return result;
    }

    /** Returns true if sub contains all characters of t with required frequencies. */
    private boolean containsAll(String sub, String t) {
        int[] tFreq = new int[128];
        for (char c : t.toCharArray()) tFreq[c]++;

        int[] subFreq = new int[128];
        for (char c : sub.toCharArray()) subFreq[c]++;

        for (int i = 0; i < 128; i++) {
            if (subFreq[i] < tFreq[i]) return false;
        }
        return true;
    }


    // ============================================================
    // Method 2 — Better: Two Pointers + Frequency Map (O(n²))
    //
    // Idea : Fix start i, extend j rightward with a live freq map.
    //        Stop as soon as have == need (first valid window from i).
    //        Move to i+1 and repeat.
    //
    // Time : O(n² × 128) ≈ O(n²) — nested loops; constant-time validity check
    // Space: O(1)  — fixed 128-size arrays
    // ============================================================
    public String minWindowBetter(String s, String t) {
        int n = s.length();
        int m = t.length();
        if (n < m) return "";

        int[] tFreq = new int[128];
        for (char c : t.toCharArray()) tFreq[c]++;
        int need = 0;
        for (int f : tFreq) if (f > 0) need++;

        String result = "";

        for (int i = 0; i < n; i++) {
            int[] windowFreq = new int[128];
            int have = 0;

            for (int j = i; j < n; j++) {
                int c = s.charAt(j);
                windowFreq[c]++;
                // check if this char now exactly satisfies t's requirement
                if (tFreq[c] > 0 && windowFreq[c] == tFreq[c]) have++;

                if (have == need) {
                    String sub = s.substring(i, j + 1);
                    if (result.isEmpty() || sub.length() < result.length()) {
                        result = sub;
                    }
                    break; // no point extending — window only gets larger from here
                }
            }
        }

        return result;
    }


    // ============================================================
    // Method 3 — Sliding Window ⭐ (Optimal)
    //
    // Idea : Two pointers left and right on s.
    //   - Expand right: add s[right] to windowFreq.
    //     If s[right]'s count in window now equals t's requirement → have++
    //   - When have == need: window is VALID.
    //     → Record min window, shrink left:
    //       If removing s[left] drops its count below t's requirement → have--
    //       left++
    //   - Repeat until right exhausts s.
    //
    // Visual (s = "ADOBECODEBANC", t = "ABC"):
    //
    //   Expand right until have==3:
    //     R=0:A  R=1:D  R=2:O  R=3:B(have=2)  R=4:E  R=5:C(have=3) → VALID!
    //   Record "ADOBEC" len=6, shrink left:
    //     remove A → have=2 ❌  left=1
    //   Continue expanding...
    //     R=10:A → have=3 VALID  window="DOBECODEBA" len=10
    //     shrink: D→O→B(still valid, B:2)→E→C(have=2)❌
    //   Continue...
    //     R=12:C → have=3 VALID  window="ODEBANC"
    //     shrink: O→D→E→B(have=2)❌ ... eventually "BANC" len=4 ← minimum!
    //
    // Time : O(n + m)  — each char in s visited at most twice
    // Space: O(m)      — two frequency maps of size at most |charset|
    // ============================================================
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        // build frequency map for t
        Map<Character, Integer> tFreq = new HashMap<>();
        for (char c : t.toCharArray()) {
            tFreq.merge(c, 1, Integer::sum);
        }

        int need = tFreq.size(); // unique chars in t
        int have = 0;            // unique chars satisfied in window

        Map<Character, Integer> windowFreq = new HashMap<>();
        int left = 0;
        int minLen = Integer.MAX_VALUE;
        int resLeft = 0, resRight = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            windowFreq.merge(c, 1, Integer::sum); // add to window

            // check if this char's count now meets t's requirement (exactly at threshold)
            if (tFreq.containsKey(c) && windowFreq.get(c).equals(tFreq.get(c))) {
                have++;
            }

            // shrink from left while window is valid
            while (have == need) {

                // update minimum window
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    resLeft  = left;
                    resRight = right;
                }

                // remove leftmost character
                char leftChar = s.charAt(left);
                windowFreq.merge(leftChar, -1, Integer::sum);

                // if removing this char breaks t's requirement → have decreases
                if (tFreq.containsKey(leftChar) &&
                    windowFreq.get(leftChar) < tFreq.get(leftChar)) {
                    have--;
                }

                left++; // shrink window
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(resLeft, resRight + 1);
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        String[][] inputs   = {
            {"ADOBECODEBANC", "ABC"},
            {"a",             "a"},
            {"a",             "aa"},
            {"aa",            "aa"},
            {"ABCDEF",        "ACE"},
            {"ab",            "b"}
        };
        String[] expected = {"BANC", "a", "", "aa", "ACE", "b"};

        System.out.println("=== Minimum Window Substring ===\n");

        for (int t = 0; t < inputs.length; t++) {
            String s  = inputs[t][0];
            String tS = inputs[t][1];

            String m1 = sol.minWindowBrute(s, tS);   // O(n²×m)
            String m2 = sol.minWindowBetter(s, tS);  // O(n²)
            String m3 = sol.minWindow(s, tS);         // O(n+m) ⭐

            boolean pass   = m3.equals(expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  s         : \"" + s + "\"");
            System.out.println("  t         : \"" + tS + "\"");
            System.out.println("  Expected  : \"" + expected[t] + "\"");
            System.out.println("  Brute     : \"" + m1 + "\"");
            System.out.println("  Better    : \"" + m2 + "\"");
            System.out.println("  Optimal   : \"" + m3 + "\"  ⭐");
            System.out.println();
        }
    }
}
