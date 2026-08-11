<img width="1122" height="1402" alt="Q5  Minimum Window Substring" src="https://github.com/user-attachments/assets/0aaffd92-06b8-4f94-bc28-126abf295cb7" />



# Q5. Minimum Window Substring

**Difficulty:** Hard &nbsp;|&nbsp; **Pattern:** Sliding Window (Variable Size) &nbsp;|&nbsp; **Companies:** Amazon, Google, Facebook, Microsoft, Uber

🔗 [LeetCode #76](https://leetcode.com/problems/minimum-window-substring/description/)

---

## 📝 Problem

Given two strings `s` and `t`, return the **smallest substring of `s`** that contains **all characters of `t`** (including duplicates).

If no such substring exists, return `""`.

### Examples

| s              | t     | Output  | Why                                          |
|----------------|-------|---------|----------------------------------------------|
| `"ADOBECODEBANC"` | `"ABC"` | `"BANC"` | Contains A, B, C — smallest such window |
| `"a"`          | `"a"` | `"a"`   | s itself contains t                          |
| `"a"`          | `"aa"` | `""`   | t needs two 'a's but s only has one          |
| `"aa"`         | `"aa"` | `"aa"` | Both a's are needed                          |

---

## 🔑 Core Insight — `have` and `need`

```
need  = number of UNIQUE characters in t
have  = number of unique characters in t whose count in the window
        is >= their count in t

A window is VALID when:  have == need

Example: t = "ABC"   → need = 3  (A, B, C each needed once)
window = "ADOBEC"    → A:1, B:1, C:1 → have = 3 == need ✅ VALID
window = "ADOB"      → A:1, B:1, C:0 → have = 2 < need  ❌ INVALID
```

---

## 🪟 Sliding Window Idea (Variable Size)

```
4-Step Framework:

Step 1: Pattern?
        → Shortest SUBSTRING containing all of t → Sliding Window ✅

Step 2: Fixed or Variable?
        → No fixed size, condition given (contains all of t) → VARIABLE SIZE

Step 3: Starting window?
        → left=0, right=0, empty window, have=0

Step 4: Each step?
        → EXPAND right: add s[right] to windowFreq, update have
        → When VALID (have==need): record min window, SHRINK left
        → Repeat until right reaches end
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n² × m))

**How it works:**
1. Generate every possible substring of `s` (two nested loops)
2. For each substring, check if it contains all characters of `t`
3. Return the shortest valid substring

**Pseudo Code:**
```
minWindow = ""
for i from 0 to n-1:
    for j from i to n-1:
        sub = s[i..j]
        if containsAll(sub, t):            ← O(|sub| + |t|) per call
            if sub is shorter than minWindow:
                minWindow = sub
return minWindow
```

**Dry Run** — `s = "ADOBECODEBANC"`, `t = "ABC"`

```
i=0, j=0: "A"        → missing B, C ❌
i=0, j=1: "AD"       → missing B, C ❌
i=0, j=2: "ADO"      → missing B, C ❌
i=0, j=3: "ADOB"     → missing C ❌
i=0, j=4: "ADOBE"    → missing C ❌
i=0, j=5: "ADOBEC"   → has A,B,C ✅  len=6  minWindow="ADOBEC"
...
i=0, j=9: "ADOBECODEB" → ✅ len=10 > 6
...
i=5, j=11: "CODEBAN" → missing C... wait
...
i=9, j=12: "BANC"   → has B,A,N,C → A✅ B✅ C✅  len=4 → new min!

Answer: "BANC" ✅
```

**Complexity Analysis:**

| Time        | Space | Reason                                          |
|-------------|-------|-------------------------------------------------|
| O(n² × m)   | O(m)  | n² substrings × O(m) check each; m = length of t |

---

### Method 2 — Better: Two Pointers with Frequency Map (O(n²))

**Idea:** Fix the starting index `i`. Expand `j` rightward, maintaining a live frequency map.  
Stop as soon as the window is valid → record it. Move to next `i`.

**Pseudo Code:**
```
minWindow = ""
for i from 0 to n-1:
    windowFreq = {}
    have = 0
    for j from i to n-1:
        add s[j] to windowFreq
        if s[j] in tFreq and windowFreq[s[j]] == tFreq[s[j]]:
            have++
        if have == need:                    ← found valid window
            if j-i+1 < minLen:
                minWindow = s[i..j]
            break                           ← no point expanding further
return minWindow
```

**Complexity Analysis:**

| Time  | Space | Reason                                                    |
|-------|-------|-----------------------------------------------------------|
| O(n²) | O(m)  | Outer loop × inner loop stops at first valid window per `i` |

---

### Method 3 — Sliding Window ⭐ (Optimal)

**Idea:**
- Expand `right` → add characters to `windowFreq`, update `have`
- When `have == need` (window is valid):
  - Record the window if it's smaller than the current minimum
  - **Shrink** `left` → remove characters, update `have` if a char's count drops below needed
  - Keep shrinking while valid
- `right` always moves forward → O(n) total

**Visual** — `s = "ADOBECODEBANC"`, `t = "ABC"`

```
tFreq = {A:1, B:1, C:1}   need=3   have=0

→ R=0: add A → wFreq={A:1}  A satisfies t → have=1
→ R=1: add D → wFreq={A:1,D:1}
→ R=2: add O → wFreq={A:1,D:1,O:1}
→ R=3: add B → wFreq={...,B:1}  B satisfies t → have=2
→ R=4: add E → wFreq={...,E:1}
→ R=5: add C → wFreq={...,C:1}  C satisfies t → have=3 ✅ VALID!
       window = "ADOBEC"  len=6  → record as minimum

  SHRINK from left:
  → remove A (s[0]) → wFreq[A]=0 < tFreq[A]=1 → have=2  ❌ now invalid
  left=1

→ R=6: add O → wFreq={...,O:2}  have=2 still
→ R=7: add D → wFreq={...,D:2}
→ R=8: add E → wFreq={...,E:2}
→ R=9: add B → wFreq={...,B:2}
→ R=10:add A → wFreq={...,A:1} A satisfies t → have=3 ✅ VALID!
        window = s[1..10] = "DOBECODEBA"  len=10 > 6  not smaller

  SHRINK:
  → remove D (s[1]) → wFreq[D]=1  D not in tFreq → have stays 3  still VALID!
  → window = s[2..10] = "OBECODEBA" len=9 > 6
  → remove O (s[2]) → have=3 still VALID
  → window = s[3..10] = "BECODEBA" len=8 > 6
  → remove B (s[3]) → wFreq[B]=1 (still ≥ tFreq[B]=1) → have=3 still!
  → window = s[4..10] = "ECODEBA" len=7 > 6
  → remove E (s[4]) → have=3 still
  → window = s[5..10] = "CODEBA" len=6 == 6 (not smaller)
  → remove C (s[5]) → wFreq[C]=0 < tFreq[C]=1 → have=2  ❌ invalid
  left=6

→ R=11: add N → wFreq={...,N:1}  have=2
→ R=12: add C → wFreq={...,C:1}  C satisfies t → have=3 ✅ VALID!
         window = s[6..12] = "ODEBANC" len=7 > 6

  SHRINK:
  → remove O (s[6]) → have=3 still
  → window = s[7..12] = "DEBANC" len=6 == 6 (not smaller)
  → remove D (s[7]) → have=3 still
  → window = s[8..12] = "EBANC" len=5 < 6  ← NEW MINIMUM!
  → remove E (s[8]) → have=3 still
  → window = s[9..12] = "BANC" len=4 < 5  ← NEW MINIMUM!
  → remove B (s[9]) → wFreq[B]=0 < tFreq[B]=1 → have=2  ❌ invalid
  left=10

right exhausted → Answer: "BANC" ✅
```

**Pseudo Code:**
```
tFreq = frequency map of t
need  = tFreq.size()
have  = 0
windowFreq = {}
left = 0
minLen = ∞,  resLeft = 0,  resRight = 0

for right from 0 to n-1:
    c = s[right]
    windowFreq[c]++
    if c in tFreq and windowFreq[c] == tFreq[c]:
        have++                              ← one more char fully satisfied

    while have == need:                     ← window is valid → try to shrink
        if right - left + 1 < minLen:      ← update minimum
            minLen = right - left + 1
            resLeft = left,  resRight = right

        leftChar = s[left]
        windowFreq[leftChar]--
        if leftChar in tFreq and windowFreq[leftChar] < tFreq[leftChar]:
            have--                          ← lost a required char
        left++

return minLen == ∞ ? "" : s[resLeft..resRight]
```

**Complexity Analysis:**

| Time     | Space | Reason                                                      |
|----------|-------|-------------------------------------------------------------|
| O(n + m) | O(m)  | Each char in s added/removed at most once; m for freq maps  |

---

## 📊 Comparison Table

| Method              | Time      | Space | Key Idea                                        |
|---------------------|-----------|-------|-------------------------------------------------|
| Brute Force         | O(n² × m) | O(m)  | All substrings + containsAll check              |
| Better (Two Ptrs)   | O(n²)     | O(m)  | Fix left, expand right, stop at first valid     |
| **Sliding Window** ⭐ | **O(n+m)** | **O(m)** | Expand right, shrink left while valid; track `have` vs `need` |

---

## 💡 Key Insights

1. **`have` and `need` counters** avoid recomputing validity from scratch every step.  
   Only update `have` when a character's count exactly hits (or drops below) the required count.

2. **Shrink while valid (WHILE loop)** — this is the key difference from expanding.  
   We always try to minimize the window once it's valid.

3. **`windowFreq[c] == tFreq[c]` (not `>=`)** — we only increment `have` at the exact moment  
   the count reaches the required level (not every time we add more of that char).

4. **Duplicates in t** are handled automatically by the frequency maps.  
   e.g., `t = "AAB"` → tFreq = {A:2, B:1} → need 2 A's in the window.

---

## 🔍 Edge Cases

| s      | t    | Output | Why                                    |
|--------|------|--------|----------------------------------------|
| `"a"`  | `"b"` | `""`  | t not found in s                       |
| `"a"`  | `"aa"` | `""` | Not enough copies of 'a'               |
| `"aa"` | `"aa"` | `"aa"` | Need both a's                        |
| `"ABC"` | `"ABC"` | `"ABC"` | Entire s is the answer             |

---

## ⚠️ Common Mistakes

| Mistake                                    | Fix                                                      |
|--------------------------------------------|----------------------------------------------------------|
| Using `>=` in `have++` check               | Use `==` — only count when count exactly meets requirement |
| Not handling duplicates in t               | Use frequency map, not just a set                        |
| Forgetting to decrement `have` when shrinking | Check if left char's count drops below t's requirement |
| Returning wrong indices                    | Track `resLeft` and `resRight` separately from `left/right` |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
