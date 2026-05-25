# Q2. Longest Substring Without Repeating Characters

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Sliding Window (Variable Size) &nbsp;|&nbsp; **Companies:** Amazon, Google, Facebook, Microsoft, Apple

🔗 [LeetCode Link](https://leetcode.com/problems/longest-substring-without-repeating-characters/description/)

---

## 🧩 What is a Substring?

A **substring** is a **contiguous** (connected) sequence of characters inside a string — just like a subarray is a contiguous part of an array.

```
s = "abcabcbb"

Valid substrings:
  "a"        → length 1
  "ab"       → length 2
  "abc"      → length 3  ← longest without repeating characters ✅
  "abca"     → has 'a' twice ❌ NOT valid
```

> ❌ `"ace"` taken from `"abcde"` is NOT a substring — the characters are not contiguous. That would be a **subsequence**.

---

## 📝 Problem

Given a string `s`, find the length of the **longest substring** that has **no duplicate characters**.

### Examples

| Input        | Output | Longest Substring (example) |
|--------------|--------|------------------------------|
| `"abcabcbb"` | 3      | `"abc"` (also `"bca"`, `"cab"`) |
| `"bbbbb"`    | 1      | `"b"` |
| `"pwwkew"`   | 3      | `"wke"` |
| `""`         | 0      | `""` (empty string) |
| `"abcde"`    | 5      | `"abcde"` (whole string) |

---

## 🧠 Why Sliding Window? (Variable Size)

This problem does NOT give us a fixed window size.  
Instead it gives us a **condition**: no duplicate characters.

```
                    Sliding Window
                         |
         ----------------------------------------
         |                                      |
  Fixed Size Window                  Dynamic / Variable Size Window
  (k is given)                       (a condition is given)

  Example: Max sum of size-k          Example: THIS PROBLEM ← 
           subarray                            Longest substring without
                                               repeating characters
```

**4-Step Framework Applied:**

```
Step 1: Pattern?
        → Longest SUBSTRING + a condition (no duplicates) → Sliding Window ✅

Step 2: Fixed or Variable window?
        → No fixed size given, condition given → VARIABLE SIZE window

Step 3: Data/info of starting window?
        → Start with an empty window: left = 0, right = 0, empty set

Step 4: What happens each step?
        → EXPAND: move right pointer, add character to set
        → SHRINK:  if duplicate found, shrink from left until duplicate is gone
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n³))

**How it works:**
1. Use **two nested loops** to generate every possible substring
2. For each substring, use a **third inner check** to see if all characters are unique
3. Track the maximum length of valid substrings

**Pseudo Code:**
```
maxLen = 0
for i from 0 to n-1:                    ← start of substring
    for j from i to n-1:                ← end of substring
        if isUnique(s, i, j):           ← check if s[i..j] has all unique chars
            maxLen = max(maxLen, j-i+1)
return maxLen

isUnique(s, start, end):
    set = {}
    for k from start to end:
        if s[k] is in set → return false
        add s[k] to set
    return true
```

**Dry Run** — `s = "pwwkew"`

```
i=0, j=0: "p"     → unique → len=1   maxLen=1
i=0, j=1: "pw"    → unique → len=2   maxLen=2
i=0, j=2: "pww"   → 'w' repeats ❌
i=1, j=1: "w"     → unique → len=1
i=1, j=2: "ww"    → 'w' repeats ❌
i=2, j=2: "w"     → unique → len=1
i=2, j=3: "wk"    → unique → len=2
i=2, j=4: "wke"   → unique → len=3   maxLen=3
i=2, j=5: "wkew"  → 'w' repeats ❌
i=3, j=3: "k"     → unique → len=1
i=3, j=4: "ke"    → unique → len=2
i=3, j=5: "kew"   → unique → len=3
...

Answer: 3 ✅
```

**Why it's slow:** For a string of length n = 10,000:
- n² substrings × n check each = **n³ = 1,000,000,000,000 operations** 🐢

**Complexity Analysis:**

| Time  | Space       | Reason                                                |
|-------|-------------|-------------------------------------------------------|
| O(n³) | O(min(n,m)) | Two nested loops × one unique check; m = charset size |

---

### Method 2 — Better: HashSet + Two Pointers (O(n²))

**Idea:** Fix the start `i` of the window. Expand end `j` rightward.  
As soon as a duplicate is found, stop — the longest window starting at `i` is found.  
Move to next start `i+1`.

**Pseudo Code:**
```
maxLen = 0
for i from 0 to n-1:
    set = {}
    for j from i to n-1:
        if s[j] is in set → break   ← duplicate found, stop this window
        add s[j] to set
        maxLen = max(maxLen, j - i + 1)
return maxLen
```

**Dry Run** — `s = "abcabcbb"`

```
i=0: j=0 add 'a' {a}     len=1
     j=1 add 'b' {a,b}   len=2
     j=2 add 'c' {a,b,c} len=3  ← maxLen=3
     j=3 's[3]'='a' in set → break

i=1: j=1 add 'b' {b}     len=1
     j=2 add 'c' {b,c}   len=2
     j=3 add 'a' {b,c,a} len=3
     j=4 's[4]'='b' in set → break

i=2: j=2 add 'c' {c}     len=1
     j=3 add 'a' {c,a}   len=2
     j=4 add 'b' {c,a,b} len=3
     j=5 's[5]'='c' in set → break
...

Answer: 3 ✅
```

**Better than Brute Force** because we stop expanding as soon as we find a duplicate, but still O(n²) worst case (e.g., `"abcdef..."` — no duplicates at all).

**Complexity Analysis:**

| Time  | Space       | Reason                                                    |
|-------|-------------|-----------------------------------------------------------|
| O(n²) | O(min(n,m)) | Outer loop × inner loop; inner loop stops at first duplicate |

---

### Method 3 — Sliding Window + HashSet (O(n)) ⭐

**Idea:** Instead of restarting from a new `i` every time, keep the window alive!  
- If `s[right]` causes a duplicate → shrink from `left` until the duplicate is gone
- Then expand `right` again

**This avoids recomputing from scratch — we just adjust the window.**

**Visual:**
```
s = "abcabcbb"
     0123456 7

left=0, right=0, set={}

→ right=0: add 'a' → {a}       window="a"      len=1  maxLen=1
→ right=1: add 'b' → {a,b}     window="ab"     len=2  maxLen=2
→ right=2: add 'c' → {a,b,c}   window="abc"    len=3  maxLen=3
→ right=3: 'a' in set!
           remove s[left=0]='a' → {b,c}   left=1
           now 'a' not in set → add 'a' → {b,c,a}  window="bca"  len=3
→ right=4: 'b' in set!
           remove s[left=1]='b' → {c,a}   left=2
           now 'b' not in set → add 'b' → {c,a,b}  window="cab"  len=3
→ right=5: 'c' in set!
           remove s[left=2]='c' → {a,b}   left=3
           now 'c' not in set → add 'c' → {a,b,c}  window="abc"  len=3
→ right=6: 'b' in set!
           remove s[left=3]='a' → {b,c}   left=4
           'b' still in set!
           remove s[left=4]='b' → {c}     left=5
           now 'b' not in set → add 'b' → {c,b}    window="cb"   len=2
→ right=7: 'b' in set!
           remove s[left=5]='c' → {b}     left=6
           'b' still in set!
           remove s[left=6]='b' → {}      left=7
           now 'b' not in set → add 'b' → {b}       window="b"   len=1

Answer: maxLen = 3 ✅
```

**Pseudo Code:**
```
left = 0
set = {}
maxLen = 0

for right from 0 to n-1:
    while s[right] is in set:       ← shrink window from left
        remove s[left] from set
        left++
    add s[right] to set             ← expand window to the right
    maxLen = max(maxLen, right - left + 1)

return maxLen
```

**Complexity Analysis:**

| Time | Space       | Reason                                                       |
|------|-------------|--------------------------------------------------------------|
| O(2n) = O(n) | O(min(n,m)) | Each character is added and removed at most once — total 2n operations |

> **Note:** This is called "O(2n)" sometimes because in the worst case each character is visited twice (once by `right`, once by `left`). Still linear — O(n).

---

### Method 4 — Sliding Window + HashMap (O(n)) ⭐⭐ Optimal

**Idea:** Instead of shrinking one step at a time from the left (which can be slow),  
use a **HashMap** to store each character's **last seen index**.  
When a duplicate is found at `s[right]`, **jump** `left` directly past the previous occurrence!

**Why is this better than Method 3?**
```
Method 3: "aaaaaaa" — each time 'a' is seen, left shrinks one by one → still O(n) total
           but conceptually slower — more while-loop iterations

Method 4: "aaaaaaa" — when 'a' is seen again, left JUMPS directly past last 'a' → 1 step!
           Always O(n) — no nested while loop at all
```

**Visual:**
```
s = "abcabcbb"
     01234567

map = {}   left = 0   maxLen = 0

right=0: 'a' not in map → map={'a':0}                   window="a"      len=1  maxLen=1
right=1: 'b' not in map → map={'a':0,'b':1}             window="ab"     len=2  maxLen=2
right=2: 'c' not in map → map={'a':0,'b':1,'c':2}       window="abc"    len=3  maxLen=3
right=3: 'a' in map, last index=0 → left=max(0,0+1)=1
         map={'a':3,'b':1,'c':2}    window="bca"         len=3  maxLen=3
right=4: 'b' in map, last index=1 → left=max(1,1+1)=2
         map={'a':3,'b':4,'c':2}    window="cab"         len=3  maxLen=3
right=5: 'c' in map, last index=2 → left=max(2,2+1)=3
         map={'a':3,'b':4,'c':5}    window="abc"         len=3  maxLen=3
right=6: 'b' in map, last index=4 → left=max(3,4+1)=5
         map={'a':3,'b':6,'c':5}    window="cb"          len=2  maxLen=3
right=7: 'b' in map, last index=6 → left=max(5,6+1)=7
         map={'a':3,'b':7,'c':5}    window="b"           len=1  maxLen=3

Answer: 3 ✅
```

**⚠️ The `max(left, map.get(c) + 1)` trick:**
```
Why max(left, oldIndex + 1)?

Consider: s = "abba"
                0123

right=3: 'a' in map, last index=0 → 0+1=1
         But left is already at 2 (moved past first 'b')!
         If we set left=1, the window "bba" would contain two 'b's → WRONG!

So we always take: left = max(left, map.get(c) + 1)
                               ↑           ↑
                         current left   just past old duplicate
```

**Pseudo Code:**
```
left = 0
map = {}        ← character → last seen index
maxLen = 0

for right from 0 to n-1:
    c = s[right]
    if c is in map AND map[c] >= left:     ← duplicate is inside current window
        left = map[c] + 1                  ← jump left past the old duplicate
    map[c] = right                         ← update last seen index
    maxLen = max(maxLen, right - left + 1) ← update answer

return maxLen
```

**Complexity Analysis:**

| Time | Space       | Reason                                         |
|------|-------------|------------------------------------------------|
| O(n) | O(min(n,m)) | Single pass, no nested loops; m = charset size |

---

## 📊 Comparison Table

| Method                  | Time  | Space       | Key Data Structure | Notes                           |
|-------------------------|-------|-------------|--------------------|---------------------------------|
| Brute Force             | O(n³) | O(min(n,m)) | HashSet            | Check every substring           |
| Better (HashSet + 2ptr) | O(n²) | O(min(n,m)) | HashSet            | Stop at first duplicate         |
| Sliding Window (HashSet)| O(n)  | O(min(n,m)) | HashSet            | Shrink left one step at a time  |
| **Sliding Window (HashMap)** | **O(n)** | **O(min(n,m))** | **HashMap** | ⭐ Jump left directly — fastest |

> `m` = size of the character set (26 for lowercase letters, 128 for ASCII, 256 for extended ASCII). Since `m` is a constant, space is effectively O(1) in practice.

---

## 💡 Key Insights

1. **Why HashMap beats HashSet (Method 4 vs Method 3):**  
   HashSet shrinks the window one step at a time from the left — still O(n) total but more iterations.  
   HashMap stores the last index, so we **jump** the left pointer directly — always O(n) with one single loop.

2. **The `max(left, map.get(c) + 1)` guard:**  
   Prevents left from moving backward when a duplicate is found outside the current window.  
   Example: `"abba"` — when we see the second `'a'`, the old index 0 is behind `left=2`, so we must not move left back to 1.

3. **Window length formula:**  
   `length = right - left + 1` → because both `left` and `right` are inclusive indices.

4. **Variable vs Fixed Window:**  
   This is a **variable window** problem — there is no fixed `k`. The window grows and shrinks based on the condition "no duplicates".

---

## 🔍 All Edge Cases

| Input        | Output | Why                                  |
|--------------|--------|--------------------------------------|
| `""`         | 0      | Empty string — loop never runs       |
| `"a"`        | 1      | Single character — always valid      |
| `"aaa"`      | 1      | All same — window stays size 1       |
| `"abcde"`    | 5      | No duplicates — whole string         |
| `"abba"`     | 2      | Tricky — tests the `max(left,...)` guard |
| `"dvdf"`     | 3      | `"vdf"` — left jumps from 0 to 1 when 'd' repeats |
| `" "` (space)| 1      | Space is a valid character           |
| `"au"`       | 2      | Two unique characters                |

---

## 🔁 Method 4 Dry Run — `"dvdf"`

```
s = "dvdf"
     0123

left=0, map={}, maxLen=0

right=0: 'd' not in map → map={'d':0}             len=1  maxLen=1
right=1: 'v' not in map → map={'d':0,'v':1}       len=2  maxLen=2
right=2: 'd' in map, last=0 → left=max(0,0+1)=1
         map={'d':2,'v':1}    window="vd"          len=2  maxLen=2
right=3: 'f' not in map → map={'d':2,'v':1,'f':3} len=3  maxLen=3

Answer: 3  (window "vdf") ✅
```

---

## 🏷️ Method Signature (LeetCode)

```java
// LeetCode — Java
public int lengthOfLongestSubstring(String s)
```

---

## 📦 Integer Array Version (ASCII trick)

For pure lowercase English letters or full ASCII, a **fixed-size integer array** can replace the HashMap — faster in practice due to no hashing overhead:

```java
int[] lastIndex = new int[128];   // ASCII table has 128 characters
Arrays.fill(lastIndex, -1);       // -1 means "not seen yet"

for (int right = 0; right < n; right++) {
    int c = s.charAt(right);
    if (lastIndex[c] >= left) {      // duplicate inside window
        left = lastIndex[c] + 1;
    }
    lastIndex[c] = right;
    maxLen = Math.max(maxLen, right - left + 1);
}
```

| Method              | Time | Space | Notes                                   |
|---------------------|------|-------|-----------------------------------------|
| HashMap version     | O(n) | O(m)  | Works for any character type            |
| int[128] array      | O(n) | O(1)  | ⭐ Fastest — direct array access, no hashing |

---

## ⚠️ Common Mistakes

| Mistake                                | Fix                                                              |
|----------------------------------------|------------------------------------------------------------------|
| `left = map.get(c) + 1` without `max` | Use `left = Math.max(left, map.get(c) + 1)` — prevents going backward |
| Not handling empty string              | Return 0 immediately if `s == null \|\| s.length() == 0`          |
| Using `s[i]` instead of `s.charAt(i)` | Java strings use `.charAt(i)`, not array indexing                |
| Forgetting to update `map[c] = right` | Always update the index AFTER moving left                        |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
