![Uploading Q3. Longest Repeating Character Replacement.png…]()


# Q3. Longest Repeating Character Replacement

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Sliding Window (Variable Size) &nbsp;|&nbsp; **Companies:** Amazon, Google, Facebook, Bloomberg

🔗 [LeetCode #424](https://leetcode.com/problems/longest-repeating-character-replacement/description/)

---

## 📝 Problem

You are given a string `s` (only **uppercase English letters**) and an integer `k`.  
You can **replace at most `k` characters** in `s` with any uppercase letter.

Return the length of the **longest substring** where all characters are the **same**, after at most `k` replacements.

### Examples

| Input              | k | Output | Why                                                        |
|--------------------|---|--------|------------------------------------------------------------|
| `"ABAB"`           | 2 | 4      | Replace 2 'A's → "BBBB" (or 2 'B's → "AAAA")            |
| `"AABABBA"`        | 1 | 4      | Replace middle 'A' → "AABBBBA" → "BBBB" has length 4      |
| `"AAAA"`           | 2 | 4      | Already all same — no replacements needed                  |
| `"ABCDE"`          | 1 | 2      | Best: replace any 1 char to make 2 same adjacent chars     |

---

## 🔑 Core Insight — The Magic Formula

This is the **key to the whole problem**. Read this carefully:

```
In ANY window (substring), the minimum number of replacements needed
to make all characters the same =

    windowLength - maxFreq

where maxFreq = frequency of the MOST COMMON character in the window.

Why? Because we want to keep the most frequent character and
     replace everything else.

Example: window = "AABBB"  (length = 5)
         frequencies: A=2, B=3
         maxFreq = 3  (B appears most)
         replacements needed = 5 - 3 = 2
         (replace both A's with B → "BBBBB")

A window is VALID if:   windowLength - maxFreq  <=  k
A window is INVALID if: windowLength - maxFreq  >   k
```

---

## 🪟 Why Sliding Window? (Variable Size)

```
Condition given: "at most k replacements" → Variable Size window

4-Step Framework:

Step 1: Pattern?
        → Longest SUBSTRING + condition (≤ k replacements) → Sliding Window ✅

Step 2: Fixed or Variable?
        → No fixed k size given, condition given → VARIABLE SIZE

Step 3: Starting window?
        → Start with left=0, right=0, empty freq array

Step 4: Each step?
        → EXPAND right: add s[right] to freq, update maxFreq
        → SHRINK left: if (windowLen - maxFreq) > k, move left right by 1
        → UPDATE answer: maxLen = max(maxLen, right - left + 1)
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n³))

**How it works:**
1. Try every possible substring using two nested loops
2. For each substring, count the frequency of every character
3. Find `maxFreq` (most frequent char in that window)
4. Check if `length - maxFreq <= k` → valid window
5. Track the maximum valid length

**Pseudo Code:**
```
maxLen = 0
for i from 0 to n-1:                          ← start of window
    for j from i to n-1:                      ← end of window
        freq[] = count all chars in s[i..j]   ← O(n) per pair
        maxFreq = max value in freq[]
        if (j - i + 1) - maxFreq <= k:        ← valid?
            maxLen = max(maxLen, j - i + 1)
return maxLen
```

**Dry Run** — `s = "AABABBA"`, `k = 1`

```
i=0, j=0: "A"       maxFreq=1  len=1  1-1=0 ≤ 1 ✅  maxLen=1
i=0, j=1: "AA"      maxFreq=2  len=2  2-2=0 ≤ 1 ✅  maxLen=2
i=0, j=2: "AAB"     maxFreq=2  len=3  3-2=1 ≤ 1 ✅  maxLen=3
i=0, j=3: "AABA"    maxFreq=3  len=4  4-3=1 ≤ 1 ✅  maxLen=4
i=0, j=4: "AABAB"   maxFreq=3  len=5  5-3=2 > 1 ❌
i=0, j=5: "AABABB"  maxFreq=3  len=6  6-3=3 > 1 ❌
... keep going ...
i=1, j=4: "ABABB"   maxFreq=3  len=5  5-3=2 > 1 ❌
i=3, j=6: "ABBA"→ A=2,B=2 maxFreq=2  len=4  4-2=2 > 1 ❌
...

Answer: 4 ✅ (window "AABA" with 1 replacement: B→A)
```

**Complexity Analysis:**

| Time  | Space | Reason                                              |
|-------|-------|-----------------------------------------------------|
| O(n³) | O(1)  | Two nested loops × O(n) inner frequency count; freq array is fixed size 26 |

---

### Method 2 — Better: Two Pointers (O(n²))

**Idea:** Fix the start `i`. Expand `j` rightward, maintaining a live frequency count.  
Check validity at every `j` without recomputing from scratch.

**Pseudo Code:**
```
maxLen = 0
for i from 0 to n-1:
    freq[26] = {0}
    maxFreq = 0
    for j from i to n-1:
        freq[s[j] - 'A']++
        maxFreq = max(maxFreq, freq[s[j] - 'A'])
        if (j - i + 1) - maxFreq <= k:    ← valid window
            maxLen = max(maxLen, j - i + 1)
        else:
            break   ← once invalid, no point extending further (optional opt.)
return maxLen
```

**Dry Run** — `s = "AABABBA"`, `k = 1`

```
i=0:
  j=0: freq[A]=1  maxFreq=1  len=1  1-1=0 ≤ 1 ✅  maxLen=1
  j=1: freq[A]=2  maxFreq=2  len=2  2-2=0 ≤ 1 ✅  maxLen=2
  j=2: freq[A]=2,B=1  maxFreq=2  len=3  3-2=1 ≤ 1 ✅  maxLen=3
  j=3: freq[A]=3,B=1  maxFreq=3  len=4  4-3=1 ≤ 1 ✅  maxLen=4
  j=4: freq[A]=3,B=2  maxFreq=3  len=5  5-3=2 > 1 ❌  break

i=3:
  j=3: A=1  maxFreq=1  len=1  ✅
  j=4: A=1,B=1  maxFreq=1  len=2  2-1=1 ≤ 1 ✅
  j=5: A=1,B=2  maxFreq=2  len=3  3-2=1 ≤ 1 ✅
  j=6: A=2,B=2  maxFreq=2  len=4  4-2=2 > 1 ❌  break
...

Answer: 4 ✅
```

**Complexity Analysis:**

| Time  | Space | Reason                                             |
|-------|-------|----------------------------------------------------|
| O(n²) | O(1)  | Outer loop × inner loop; freq array is fixed 26    |

---

### Method 3 — Sliding Window ⭐ (Optimal)

**Idea:** Keep a live window `[left, right]`.
- Expand `right`, add `s[right]` to frequency count, update `maxFreq`
- If window becomes **invalid** `(windowLen - maxFreq > k)` → shrink `left` by 1
- The window **never shrinks below the best answer seen so far**

**Visual** — `s = "AABABBA"`, `k = 1`

```
Array:   A  A  B  A  B  B  A
Index:   0  1  2  3  4  5  6

left=0, freq={}, maxFreq=0, maxLen=0

→ right=0: add 'A' → freq={A:1}  maxFreq=1
           len=1  1-1=0 ≤ 1 ✅   maxLen=1
           window: [A]ABABBA

→ right=1: add 'A' → freq={A:2}  maxFreq=2
           len=2  2-2=0 ≤ 1 ✅   maxLen=2
           window: [AA]BABBA

→ right=2: add 'B' → freq={A:2,B:1}  maxFreq=2
           len=3  3-2=1 ≤ 1 ✅   maxLen=3
           window: [AAB]ABBA

→ right=3: add 'A' → freq={A:3,B:1}  maxFreq=3
           len=4  4-3=1 ≤ 1 ✅   maxLen=4
           window: [AABA]BBA

→ right=4: add 'B' → freq={A:3,B:2}  maxFreq=3
           len=5  5-3=2 > 1 ❌  SHRINK!
           remove s[left=0]='A' → freq={A:2,B:2}  left=1
           len=4  still maxLen=4
           window: A[ABAB]BA

→ right=5: add 'B' → freq={A:2,B:3}  maxFreq=3
           len=5  5-3=2 > 1 ❌  SHRINK!
           remove s[left=1]='A' → freq={A:1,B:3}  left=2
           len=4  still maxLen=4
           window: AA[BABB]A

→ right=6: add 'A' → freq={A:2,B:3}  maxFreq=3
           len=5  5-3=2 > 1 ❌  SHRINK!
           remove s[left=2]='B' → freq={A:2,B:2}  left=3
           len=4  still maxLen=4
           window: AAB[ABBA]

Answer: maxLen = 4 ✅
```

**Pseudo Code:**
```
left = 0
freq[26] = {0}
maxFreq = 0
maxLen = 0

for right from 0 to n-1:
    c = s[right] - 'A'
    freq[c]++
    maxFreq = max(maxFreq, freq[c])       ← update most frequent count

    if (right - left + 1) - maxFreq > k:  ← window is invalid
        freq[s[left] - 'A']--             ← shrink from left
        left++

    maxLen = max(maxLen, right - left + 1) ← window is always valid here
                                           ← (or same size as best)
return maxLen
```

**Complexity Analysis:**

| Time | Space | Reason                                                 |
|------|-------|--------------------------------------------------------|
| O(n) | O(1)  | Single pass; freq array is fixed size 26 (constant)    |

---

## 🤯 The "maxFreq Never Decreases" Trick

> This is the most subtle part of the optimal solution. **Read this carefully.**

When we shrink the window (left++), we do NOT recompute `maxFreq`.  
`maxFreq` only ever **increases or stays the same** — it never goes down.

**Why is this correct?**

```
We are searching for the LONGEST valid window.
The answer can only improve if maxFreq INCREASES.
If maxFreq stays the same, the window size stays the same → no improvement.
If maxFreq decreases → window would need to shrink more → definitely no improvement.

So: we only grow maxLen when we find a character with higher frequency.
    We shrink the window by exactly 1 when invalid.
    → Window size stays flat until a new maxFreq is found.
    → We never "check smaller windows" — no wasted work.
```

**Proof with example:** `s = "BAAAB"`, `k = 0`

```
right=0: add B  freq={B:1}  maxFreq=1  len=1  1-1=0 ≤ 0 ✅  maxLen=1
right=1: add A  freq={B:1,A:1}  maxFreq=1  len=2  2-1=1 > 0 ❌
         remove s[0]='B'→ freq={A:1}  left=1   len still 1
right=2: add A  freq={A:2}  maxFreq=2  len=2  2-2=0 ≤ 0 ✅  maxLen=2
right=3: add A  freq={A:3}  maxFreq=3  len=3  3-3=0 ≤ 0 ✅  maxLen=3
right=4: add B  freq={A:3,B:1}  maxFreq=3  len=4  4-3=1 > 0 ❌
         remove s[1]='A'→ freq={A:2,B:1}  left=2  len still 3

Answer: 3  ("AAA") ✅
```

---

## 📊 Comparison Table

| Method                   | Time  | Space | Key Idea                                         |
|--------------------------|-------|-------|--------------------------------------------------|
| Brute Force              | O(n³) | O(1)  | All substrings + freq count from scratch each time |
| Better (Two Pointers)    | O(n²) | O(1)  | Fix start, extend right with live freq count      |
| **Sliding Window** ⭐    | **O(n)** | **O(1)** | Expand right, shrink left by 1 when invalid; maxFreq never decreases |

---

## 💡 Key Insights

1. **Magic formula:** `replacements needed = windowLength - maxFreq`  
   We always keep the most frequent character and replace everything else.

2. **Valid condition:** `windowLength - maxFreq <= k`  
   If this holds → the window can be made all-same with at most `k` replacements.

3. **maxFreq never decreases:** When shrinking, we don't recompute maxFreq downward.  
   This is safe because we only care about windows BIGGER than the current best.

4. **Window size monotonically grows:** In the optimal solution, the window never shrinks below `maxLen`.  
   Every time we move both left and right by 1, the window size stays the same.  
   The window grows only when a new `maxFreq` is found.

5. **Only uppercase letters:** freq array is fixed size 26 → O(1) space always.

---

## 🔍 Edge Cases

| Input          | k | Output | Why                                      |
|----------------|---|--------|------------------------------------------|
| `"AAAA"`       | 0 | 4      | Already all same                         |
| `"ABCD"`       | 0 | 1      | No replacements — each char is alone     |
| `"ABCD"`       | 4 | 4      | Replace all → entire string              |
| `"A"`          | 1 | 1      | Single character                         |
| `"AABABBA"`    | 1 | 4      | Classic test case                        |

---

## 🔁 Dry Run — `s = "ABAB"`, `k = 2`

```
left=0, freq={}, maxFreq=0, maxLen=0

right=0: add 'A' → freq={A:1}       maxFreq=1  len=1  1-1=0 ≤ 2 ✅  maxLen=1
right=1: add 'B' → freq={A:1,B:1}   maxFreq=1  len=2  2-1=1 ≤ 2 ✅  maxLen=2
right=2: add 'A' → freq={A:2,B:1}   maxFreq=2  len=3  3-2=1 ≤ 2 ✅  maxLen=3
right=3: add 'B' → freq={A:2,B:2}   maxFreq=2  len=4  4-2=2 ≤ 2 ✅  maxLen=4

Answer: 4 ✅  (replace both B's → "AAAA", or both A's → "BBBB")
```

---

## 🏷️ Method Signature (LeetCode)

```java
// LeetCode — Java
public int characterReplacement(String s, int k)
```

---

## ⚠️ Common Mistakes

| Mistake                                     | Fix                                                        |
|---------------------------------------------|------------------------------------------------------------|
| Using `length - maxFreq <= k` on a shrunk window without updating maxFreq | Don't update maxFreq down — it's intentional |
| Forgetting `freq[s[left] - 'A']--` when shrinking | Always decrement left char's freq when moving left         |
| Using `while` instead of `if` to shrink      | Use `if` — we shrink by exactly 1 each time, not a loop   |
| Using lowercase or mixed-case input         | Constraints say uppercase only — `s[i] - 'A'` works safely |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
