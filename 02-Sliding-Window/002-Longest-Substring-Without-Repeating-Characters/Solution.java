package p002;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Longest Substring Without Repeating Characters
 * LeetCode #3 — Medium
 *
 * Given a string s, find the length of the longest substring
 * that contains NO duplicate characters.
 *
 * Example: s = "abcabcbb"  → 3  ("abc")
 *          s = "bbbbb"     → 1  ("b")
 *          s = "pwwkew"    → 3  ("wke")
 *
 * Pattern   : Sliding Window (Variable Size)
 * Difficulty: Medium
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : Try every possible substring s[i..j].
    //        For each one, check if all characters are unique
    //        using a HashSet (inner helper loop).
    //        Track the maximum length seen.
    //
    // Time : O(n³) — two nested loops × one unique check per pair
    // Space: O(min(n,m)) — HashSet holds at most min(n, charset) chars
    // ============================================================
    public int lengthOfLongestSubstringBrute(String s) {
        int n = s.length();
        int maxLen = 0;

        // try every starting index i
        for (int i = 0; i < n; i++) {
            // try every ending index j (inclusive)
            for (int j = i; j < n; j++) {
                // check if s[i..j] has all unique characters
                if (isUnique(s, i, j)) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }

        return maxLen;
    }

    /**
     * Helper: returns true if s[start..end] has no duplicate characters.
     * Uses a HashSet — add each char; if already present, return false.
     */
    private boolean isUnique(String s, int start, int end) {
        Set<Character> seen = new HashSet<>();
        for (int k = start; k <= end; k++) {
            if (seen.contains(s.charAt(k))) {
                return false; // duplicate found
            }
            seen.add(s.charAt(k));
        }
        return true; // all unique
    }


    // ============================================================
    // Method 2 — Better: HashSet + Two Pointers
    //
    // Idea : Fix the starting index i.
    //        Extend j rightward, adding chars to a HashSet.
    //        Stop as soon as s[j] is already in the set.
    //        Move i to the next position and repeat.
    //
    // Better than Brute Force because the inner loop stops early
    // at the first duplicate (no need for a separate isUnique call).
    //
    // Time : O(n²) — outer loop × inner loop (stops at first duplicate)
    // Space: O(min(n,m)) — HashSet
    // ============================================================
    public int lengthOfLongestSubstringBetter(String s) {
        int n = s.length();
        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            Set<Character> seen = new HashSet<>();

            for (int j = i; j < n; j++) {
                char c = s.charAt(j);

                if (seen.contains(c)) {
                    break; // duplicate found — this window is invalid, try next i
                }

                seen.add(c);
                maxLen = Math.max(maxLen, j - i + 1); // window length = j - i + 1
            }
        }

        return maxLen;
    }


    // ============================================================
    // Method 3 — Sliding Window + HashSet
    //
    // Idea : Maintain a live window [left..right] using a HashSet.
    //        Expand right pointer one step at a time.
    //        If s[right] is a duplicate, shrink from left until
    //        the duplicate is removed, then add s[right].
    //
    // Key difference from Method 2:
    //   We DON'T restart from scratch — we shrink from the left.
    //   Each character is added and removed at most ONCE → O(2n) = O(n)
    //
    // Visual (s = "abcabcbb"):
    //
    //   [a]bcabcbb      set={a}       len=1
    //   [ab]cabcbb      set={a,b}     len=2
    //   [abc]abcbb      set={a,b,c}   len=3  ← maxLen=3
    //   abc[a]bcbb → 'a' duplicate! shrink:
    //    left++ → [bc]a... set={b,c}
    //   [bca]bcbb       set={b,c,a}   len=3
    //   ...
    //
    // Time : O(2n) = O(n) — each char added/removed at most once
    // Space: O(min(n,m)) — HashSet
    // ============================================================
    public int lengthOfLongestSubstringHashSet(String s) {
        int n = s.length();
        int left = 0;
        int maxLen = 0;
        Set<Character> window = new HashSet<>();

        for (int right = 0; right < n; right++) {
            char c = s.charAt(right);

            // shrink window from left until duplicate is removed
            while (window.contains(c)) {
                window.remove(s.charAt(left));
                left++;
            }

            // now s[right] is safe to add — no duplicate in window
            window.add(c);

            // window is s[left..right], length = right - left + 1
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }


    // ============================================================
    // Method 4 — Sliding Window + HashMap ⭐ (Optimal)
    //
    // Idea : Use a HashMap<character, lastSeenIndex>.
    //        When a duplicate s[right] is found, instead of
    //        shrinking one step at a time, JUMP left directly
    //        to (lastSeenIndex + 1).
    //
    // ⚠️ IMPORTANT: always use left = max(left, map.get(c)+1)
    //    to prevent left from moving BACKWARD when the duplicate
    //    was already outside the current window.
    //
    //    Example: "abba"
    //      right=3 sees 'a', map['a']=0
    //      But left is already 2 (past 'b').
    //      If we set left=0+1=1, window "bba" has two 'b's → WRONG!
    //      So: left = max(left=2, 0+1=1) = 2 ✅
    //
    // Visual (s = "abcabcbb"):
    //
    //   right=0: 'a' new   → map={a:0}          left=0  len=1  max=1
    //   right=1: 'b' new   → map={a:0,b:1}      left=0  len=2  max=2
    //   right=2: 'c' new   → map={a:0,b:1,c:2}  left=0  len=3  max=3
    //   right=3: 'a' dup@0 → left=max(0,1)=1    map={a:3,b:1,c:2}   len=3
    //   right=4: 'b' dup@1 → left=max(1,2)=2    map={a:3,b:4,c:2}   len=3
    //   right=5: 'c' dup@2 → left=max(2,3)=3    map={a:3,b:4,c:5}   len=3
    //   right=6: 'b' dup@4 → left=max(3,5)=5    map={a:3,b:6,c:5}   len=2
    //   right=7: 'b' dup@6 → left=max(5,7)=7    map={a:3,b:7,c:5}   len=1
    //
    //   Answer: 3 ✅
    //
    // Time : O(n)          — single pass, no nested loops
    // Space: O(min(n,m))   — HashMap holds at most min(n, charset) entries
    // ============================================================
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int left = 0;
        int maxLen = 0;

        // map: character → its most recent index in s
        Map<Character, Integer> lastIndex = new HashMap<>();

        for (int right = 0; right < n; right++) {
            char c = s.charAt(right);

            // if c was seen before AND its last index is inside the current window
            // → jump left pointer past the old occurrence
            if (lastIndex.containsKey(c) && lastIndex.get(c) >= left) {
                left = lastIndex.get(c) + 1;
            }

            // record the latest index of this character
            lastIndex.put(c, right);

            // window [left..right], length = right - left + 1
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }


    // ============================================================
    // Method 5 — Sliding Window + int[128] Array (Fastest)
    //
    // Idea : Same as Method 4 but use a fixed int array of size 128
    //        (ASCII table size) instead of a HashMap.
    //        Direct array access is faster than HashMap hashing.
    //
    // Constraints say s can contain English letters, digits,
    // symbols, and spaces → all fit within ASCII 0-127.
    //
    // Time : O(n)  — single pass
    // Space: O(1)  — fixed array of size 128 (constant)
    // ============================================================
    public int lengthOfLongestSubstringArray(String s) {
        int n = s.length();
        int left = 0;
        int maxLen = 0;

        // lastIndex[c] = last seen index of character c, or -1 if not seen
        int[] lastIndex = new int[128];
        Arrays.fill(lastIndex, -1); // -1 means "not seen yet"

        for (int right = 0; right < n; right++) {
            int c = s.charAt(right); // cast char to int → ASCII index

            // if c was seen before AND its last index is inside the current window
            if (lastIndex[c] >= left) {
                left = lastIndex[c] + 1; // jump left past the duplicate
            }

            lastIndex[c] = right; // update last seen index
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        String[] inputs   = { "abcabcbb", "bbbbb", "pwwkew", "",  "abba", "dvdf", "au",  " " };
        int[]    expected = {      3,         1,       3,      0,    2,      3,      2,    1  };

        System.out.println("=== Longest Substring Without Repeating Characters ===\n");

        for (int t = 0; t < inputs.length; t++) {
            String s = inputs[t];

            int m1 = sol.lengthOfLongestSubstringBrute(s);     // Method 1: O(n³)
            int m2 = sol.lengthOfLongestSubstringBetter(s);    // Method 2: O(n²)
            int m3 = sol.lengthOfLongestSubstringHashSet(s);   // Method 3: O(n) HashSet
            int m4 = sol.lengthOfLongestSubstring(s);          // Method 4: O(n) HashMap ⭐
            int m5 = sol.lengthOfLongestSubstringArray(s);     // Method 5: O(n) int[]

            boolean pass   = (m4 == expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  Input       : \"" + s + "\"");
            System.out.println("  Expected    : " + expected[t]);
            System.out.println("  Brute O(n³) : " + m1);
            System.out.println("  Better O(n²): " + m2);
            System.out.println("  HashSet O(n): " + m3);
            System.out.println("  HashMap O(n): " + m4 + "  ⭐ optimal");
            System.out.println("  Array  O(n) : " + m5);
            System.out.println();
        }
    }
}
