<img width="1024" height="1536" alt="Q2  Largest Subarray with 0 Sum" src="https://github.com/user-attachments/assets/72d05182-4fd5-48a4-82c2-ba5f3a4205ef" />



# Q2. Largest Subarray with 0 Sum

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Prefix Sum + HashMap &nbsp;|&nbsp; **Companies:** Amazon, Flipkart, Goldman Sachs, Microsoft

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/find-the-largest-subarray-with-0-sum/)

---

## 📝 Problem Statement

Given an array `arr[]` of integers (positive, negative, and zero), find the **length of the longest subarray** whose elements sum to exactly **0**.

### Examples

| Input | Output | Subarray |
|---|---|---|
| `[15, -2, 2, -8, 1, 7, 10, 23]` | 5 | `[-2, 2, -8, 1, 7]` → sum=0 |
| `[1, 2, 3]` | 0 | No subarray sums to 0 |
| `[0, 0, 0]` | 3 | Entire array |
| `[1, -1, 3]` | 2 | `[1, -1]` → sum=0 |
| `[-3, 3, -3, 3]` | 4 | Entire array → sum=0 |

---

## 🔒 Constraints

- `1 ≤ arr.length ≤ 10⁵`
- `-10⁴ ≤ arr[i] ≤ 10⁴`

---

## 🔑 Key Observations

- Negative numbers exist → sliding window / two-pointer approach BREAKS (non-monotonic sums)
- The correct tool is **Prefix Sum + HashMap**
- If two prefix sums are equal, the subarray between them has sum 0
- We want the LONGEST such subarray → store the EARLIEST (first) occurrence of each prefix sum
- Initialise map with `{0: -1}` to handle subarrays that start at index 0

---

## 💡 Intuition Building

**Why brute force is obvious but slow:**  
Try every `(start, end)` pair and check if the sum is 0. That's O(n²) pairs.

**The prefix sum relationship:**
```
Define prefix[i] = arr[0] + arr[1] + ... + arr[i]

sum(arr[i+1 .. j]) = prefix[j] - prefix[i]

If this equals 0:  prefix[j] = prefix[i]
```

So instead of checking every pair, we just look for **equal prefix sums**.

**The aha moment:**
> Two prefix sums being equal means the subarray between them cancels out to zero!

**Why a HashMap?**  
We want the FIRST occurrence of each prefix sum (earliest start = longest length).  
HashMap gives O(1) lookup and storage.

**From O(n³) → O(n²) → O(n):**
```
O(n³): Try all (start, end), compute sum from scratch each time
O(n²): Precompute prefix array, check all pairs
O(n) : As we compute prefix sums, store first occurrence in a map
       When a prefix sum is seen again → length = currentIndex - firstIndex
```

---

## 📊 Approaches Overview

| Approach | Technique | Time | Space | Use In Interview? |
|---|---|---|---|---|
| Brute Force | Double loop + running sum | O(n²) | O(1) | ❌ Too slow |
| Prefix Array + Pairs | All pairs of prefix values | O(n²) | O(n) | ⚠️ Shows math |
| **Prefix Sum + HashMap** ⭐ | First occurrence map | **O(n)** | **O(n)** | ✅ Always |

---

## APPROACH 1 — BRUTE FORCE

### Idea
Try every possible `(start, end)` pair. Build the sum incrementally (add one element at a time so we don't recompute from scratch). If sum = 0, track max length.

### Algorithm
1. Outer loop `i` from `0` to `n-1` (subarray start)
2. Inner loop `j` from `i` to `n-1` (subarray end)
3. Accumulate `sum += arr[j]`
4. If `sum == 0`, update `maxLen = max(maxLen, j - i + 1)`
5. Return `maxLen`

### Dry Run — `arr = [15, -2, 2, -8, 1, 7, 10, 23]`

```
i=0: sum=15 ❌  sum=13 ❌  sum=15 ❌  sum=7 ❌  sum=8 ❌  sum=15 ✅ len=6  ...
i=1: sum=-2 ❌  sum=0 ✅ len=2  sum=-8 ❌  sum=-7 ❌  sum=0 ✅ len=5  ← maxLen=5
i=2: ...
...

Answer: 5 ✅
```

### Java Code

```java
public int maxLenBrute(int[] arr, int n) {
    int maxLen = 0;
    for (int i = 0; i < n; i++) {
        int sum = 0;
        for (int j = i; j < n; j++) {
            sum += arr[j];
            if (sum == 0) maxLen = Math.max(maxLen, j - i + 1);
        }
    }
    return maxLen;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n²) | O(1) | Two nested loops; sum incremented not recomputed |

**Pros:** Simple, no extra data structures  
**Cons:** Too slow for n = 10⁵

---

## APPROACH 2 — BETTER: Prefix Sum Array

### Idea
Precompute a prefix sum array. Then use the relationship:

```
sum(arr[i..j-1]) = prefix[j] - prefix[i]  =  0
                ↔ prefix[j] == prefix[i]
```

Check all pairs `(i, j)` in the prefix array for equality.

### Dry Run — `arr = [15, -2, 2, -8, 1, 7, 10, 23]`

```
prefix = [0, 15, 13, 15, 7, 8, 15, 25, 48]
         ↑   0   1   2   3  4   5   6   7   (indices)

Equal pairs:
  prefix[0]=0  and  (nothing) — nothing yet
  prefix[2]=13 and  (nothing)
  prefix[1]=15, prefix[3]=15  → j-i = 3-1 = 2   → arr[1..2]=[-2,2]
  prefix[1]=15, prefix[6]=15  → j-i = 6-1 = 5   ← best!
  prefix[3]=15, prefix[6]=15  → j-i = 6-3 = 3

Answer: 5 ✅
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n²) | O(n) | Two loops checking all prefix pairs; O(n) prefix array |

---

## APPROACH 3 — OPTIMAL: Prefix Sum + HashMap ⭐

### Deep Intuition — Why Does This Work?

```
If prefix[j] == prefix[i]  (j > i):
  sum(arr[i+1 .. j]) = prefix[j] - prefix[i] = 0

The subarray arr[i+1 .. j] sums to 0.
Length = j - i.

We want the LONGEST such subarray → we want the SMALLEST i for each prefix value.
→ Store the FIRST time we see each prefix sum.
→ Never overwrite → keep the earliest start.
```

### Critical Initialization: `map = {0: -1}`

```
Why? If prefix[j] == 0 at index j, then:
  length = j - (-1) = j + 1
  This correctly captures subarray arr[0..j] which has sum 0.

Without this: we'd miss subarrays that start at index 0.
```

### Dry Run — `arr = [15, -2, 2, -8, 1, 7, 10, 23]`

```
map = {0: -1}    prefix = 0    maxLen = 0

i=0: prefix = 0+15 = 15     15 not in map → map[15] = 0
i=1: prefix = 15+(-2) = 13  13 not in map → map[13] = 1
i=2: prefix = 13+2 = 15     15 IS in map at index 0!
     length = 2 - 0 = 2     maxLen = 2
     (DON'T update map — keep first occurrence: 0)
i=3: prefix = 15+(-8) = 7   7 not in map → map[7] = 3
i=4: prefix = 7+1 = 8       8 not in map → map[8] = 4
i=5: prefix = 8+7 = 15      15 IS in map at index 0!
     length = 5 - 0 = 5     maxLen = 5  ← ANSWER!
     (DON'T update map — keep first occurrence: 0)
i=6: prefix = 15+10 = 25    new → map[25] = 6
i=7: prefix = 25+23 = 48    new → map[48] = 7

Answer: 5 ✅  (arr[1..5] = [-2, 2, -8, 1, 7])
```

### Pseudo Code

```
map = {0: -1}       ← prefix 0 at virtual index -1
prefix = 0
maxLen = 0

for i from 0 to n-1:
    prefix += arr[i]

    if prefix IS in map:
        maxLen = max(maxLen, i - map[prefix])
        ← DO NOT update: keep FIRST occurrence
    else:
        map[prefix] = i    ← store first occurrence

return maxLen
```

### Java Code (Interview-Quality)

```java
public int maxLen(int[] arr, int n) {
    Map<Integer, Integer> firstOccurrence = new HashMap<>();
    firstOccurrence.put(0, -1);  // prefix 0 at virtual index -1

    int prefix = 0;
    int maxLen = 0;

    for (int i = 0; i < n; i++) {
        prefix += arr[i];

        if (firstOccurrence.containsKey(prefix)) {
            int len = i - firstOccurrence.get(prefix);
            maxLen = Math.max(maxLen, len);
            // DON'T update → keep earliest occurrence
        } else {
            firstOccurrence.put(prefix, i);
        }
    }

    return maxLen;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n) | O(n) | Single pass; HashMap stores at most n+1 prefix sums |

### How to Explain in an Interview

> *"If I see the same prefix sum twice — at index i and index j — the subarray between them has sum 0. So I store the first occurrence of every prefix sum in a HashMap. When I encounter a prefix sum I've seen before, the length is current index minus the stored index. I initialize the map with {0: -1} to handle subarrays starting at index 0. The key rule is: never overwrite — I always want the EARLIEST occurrence to maximize length."*

---

## ⚠️ Common Mistakes

| Mistake | Fix |
|---|---|
| Not initialising `map = {0: -1}` | Without it, subarrays starting at index 0 are missed |
| Overwriting the map on second occurrence | Only store FIRST occurrence; skip if key already exists |
| Using `i - map[prefix] + 1` | Correct formula is `i - map[prefix]` (map stores left boundary EXCLUSIVE) |
| Trying sliding window for this problem | Won't work — negative elements make sums non-monotonic |

---

## 🔍 Edge Cases

| Input | Output | Why |
|---|---|---|
| `[0, 0, 0]` | 3 | Every prefix sum is 0; all tie back to index -1 |
| `[1, 2, 3]` | 0 | Prefix sums are all distinct; no repeats |
| `[-5, 5]` | 2 | Entire array sums to 0 |
| `[0]` | 1 | Single zero → sum=0 immediately |
| `[1, -1, 1, -1]` | 4 | Entire array, sum=0 |

---

## 🧩 Pattern Recognition

**Pattern:** Prefix Sum + HashMap (First Occurrence)  
**When to use:** "Find the LONGEST subarray with some property of its sum"  
(sum = 0, sum = k, sum divisible by k)

| Problem | Map Key | Map Value |
|---|---|---|
| Largest subarray sum = 0 | prefix sum | first index |
| Largest subarray sum = k | prefix - k | first index |
| Largest subarray sum divisible by k | prefix % k | first index |

Same template, different "key" formula!

---

## 🎯 Interview Tips

1. **Draw the prefix sum array first** — makes the "equal prefix = zero sum" insight visual
2. **Explain the map initialization `{0: -1}` proactively** — interviewers love this edge case
3. **Stress "never overwrite"** — makes the "longest" argument clear
4. **Mention the similarity to sliding window** but explain why it doesn't work here (negatives)

---

## 🔗 Related Problems

- GFG — Print All Subarrays with 0 Sum (store ALL occurrences)
- LeetCode 560 — Subarray Sum Equals K (same pattern, target = k)
- GFG — Longest Subarray with Sum Divisible by K (same pattern, key = prefix % k)
- LeetCode 525 — Contiguous Array (0/1 array, convert to +1/-1, same idea)

---

## 📌 Revision Notes

- `prefix[j] == prefix[i]` → sum(i+1..j) = 0, length = j - i
- Init map `{0: -1}` → handles subarrays starting at index 0
- Store FIRST occurrence only → keeps earliest start for longest subarray
- DO NOT use sliding window → negative numbers break monotonicity
- Length formula: `i - map.get(prefix)` (NOT i - map + 1)

---

## 🏁 Key Takeaways

> The core insight — **equal prefix sums mean zero-sum subarray between them** — is the heart of this problem. Master the `{0: -1}` initialisation and the "never overwrite" rule, and this entire family of prefix-sum problems becomes a template.

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
