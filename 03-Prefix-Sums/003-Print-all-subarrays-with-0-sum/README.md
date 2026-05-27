# Q3. Print All Subarrays with 0 Sum

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Prefix Sum + HashMap (Multi-Occurrence) &nbsp;|&nbsp; **Companies:** Adobe, Amazon, Samsung

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/dsa/print-all-subarrays-with-0-sum/)

---

## ⚠️ Important Note — Key Difference from Q2

> **Q2 (Largest Subarray with 0 Sum):** Store FIRST occurrence of each prefix sum → find the longest.  
> **Q3 (Print ALL Subarrays with 0 Sum):** Store ALL occurrences → report every valid subarray.
>
> One word difference in the problem → one structural change in the HashMap value type.

---

## 📝 Problem Statement

Given an array `arr[]` of integers, find and print **all contiguous subarrays** whose elements sum to exactly **0**.

### Examples

| Input | All 0-Sum Subarrays | Count |
|---|---|---|
| `[1, 2, -3, 4, -4]` | `[1,2,-3]`, `[4,-4]`, `[1,2,-3,4,-4]` | 3 |
| `[0, 0, 0]` | Every prefix of every suffix | 6 |
| `[1, 2, 3]` | None | 0 |
| `[0]` | `[0]` | 1 |

---

## 🔒 Constraints

- `1 ≤ arr.length ≤ 10³`
- `-10⁴ ≤ arr[i] ≤ 10⁴`

---

## 🔑 Key Observations

- Same math as Q2: `prefix[j] == prefix[i]` → `sum(arr[i+1..j]) = 0`
- Multiple past indices can share the same prefix sum → multiple valid subarrays at one `j`
- We cannot stop at the first match; we must enumerate ALL previous occurrences
- For all-zeros array of size n: there are `n(n+1)/2` subarrays (output is inherently O(n²))
- Init `map = {0: [-1]}` handles subarrays starting at index 0 (same as Q2)

---

## 💡 Intuition Building

**Q2 recap:** Store `prefixSum → firstIndex`. When same prefix seen again, one length = j - firstIndex.

**Q3 difference:** What if prefix sum P appeared at indices 2, 5, 8 and now j = 11?
```
Index 2 → subarray arr[3..11], length 9
Index 5 → subarray arr[6..11], length 6
Index 8 → subarray arr[9..11], length 3
```
All three are different, valid, and must be reported!

**The one structural change:**
```
Q2: HashMap<Integer, Integer>       → prefix sum → FIRST index only
Q3: HashMap<Integer, List<Integer>> → prefix sum → ALL indices (List)
```

**From O(n³) → O(n²) → O(n + k):**
```
O(n³): Recompute sum from scratch for every (start, end, inner) triple
O(n²): Precompute prefix array, check all equal pairs
O(n+k): Single pass; k = total number of result pairs
        (k can be O(n²) in worst case — unavoidable when output is large)
```

---

## 📊 Approaches Overview

| Approach | Technique | Time | Space | Use In Interview? |
|---|---|---|---|---|
| Brute Force | Three nested loops | O(n³) | O(1) | ❌ Too slow |
| Prefix Array | Two loops, equal-pair check | O(n²) | O(n) | ⚠️ Shows math |
| **Prefix + HashMap (Lists)** ⭐ | All occurrences per sum | **O(n+k)** | **O(n)** | ✅ Always |

---

## APPROACH 1 — BRUTE FORCE

### Idea
Three nested loops: outer two define `(start, end)`, innermost computes sum from scratch.

### Algorithm
1. For `start` from 0 to n-1
2. For `end` from `start` to n-1
3. Compute `sum = arr[start] + ... + arr[end]` (inner loop from scratch)
4. If `sum == 0` → record `[start, end]`

### Dry Run — `arr = [1, 2, -3, 4, -4]`

```
start=0, end=0: sum=1  ❌
start=0, end=1: sum=3  ❌
start=0, end=2: sum=0  ✅ → arr[0..2] = [1, 2, -3]
start=0, end=3: sum=4  ❌
start=0, end=4: sum=0  ✅ → arr[0..4] = [1, 2, -3, 4, -4]
start=1, end=1: sum=2  ❌
start=1, end=2: sum=-1 ❌
start=1, end=3: sum=3  ❌
start=1, end=4: sum=-1 ❌
start=2, end=2: sum=-3 ❌
start=2, end=3: sum=1  ❌
start=2, end=4: sum=-3 ❌
start=3, end=3: sum=4  ❌
start=3, end=4: sum=0  ✅ → arr[3..4] = [4, -4]
start=4, end=4: sum=-4 ❌

3 subarrays found ✅
```

### Java Code

```java
public List<int[]> findSubarraysBrute(int[] arr) {
    int n = arr.length;
    List<int[]> result = new ArrayList<>();

    for (int start = 0; start < n; start++) {
        for (int end = start; end < n; end++) {
            int sum = 0;
            for (int k = start; k <= end; k++) sum += arr[k]; // sum from scratch
            if (sum == 0) result.add(new int[]{start, end});
        }
    }
    return result;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n³) | O(1) | Three nested loops |

**Pros:** Straightforward  
**Cons:** Extremely slow for large arrays

---

## APPROACH 2 — BETTER: Prefix Sum Array

### Idea
Precompute `prefix[]`. Then for every pair `(i, j)` with `i < j`:  
if `prefix[j] == prefix[i]` → subarray `arr[i..j-1]` has sum 0.

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n²) | O(n) | Two nested loops; O(n) prefix array |

---

## APPROACH 3 — OPTIMAL: Prefix Sum + HashMap (All Occurrences) ⭐

### Deep Intuition

```
At each index j, the prefix sum is P.
Every previous index i where prefix[i] == P gives a zero-sum subarray:
    arr[i+1 .. j]   with sum = prefix[j] - prefix[i] = 0.

There can be MULTIPLE such i values for the same j.
We must enumerate all of them.
```

### Key Difference from Q2

```
Q2: map[P] = firstIndex        → only one subarray per match
Q3: map[P] = [i₁, i₂, i₃, ...] → one subarray per entry in the list
```

### Critical Rule: ALWAYS append current index

- In Q2: we skip overwriting (keep only first)
- In Q3: we **always** add current index to the list (every occurrence matters)

### Critical Init: `map = {0: [-1]}`

Prefix sum 0 at virtual index -1. If `prefix[j] == 0` at index `j`:
- subarray `arr[-1+1 .. j] = arr[0..j]` has sum 0 ✅

### Dry Run — `arr = [1, 2, -3, 4, -4]`

```
prefix values:
  index: -1   0   1   2   3   4
  prefix: 0   1   3   0   4   0

map starts: {0: [-1]}

i=0: prefix=1  new → map = {0:[-1], 1:[0]}
i=1: prefix=3  new → map = {0:[-1], 1:[0], 3:[1]}
i=2: prefix=0  seen at [-1]!
               → arr[0..2] = [1, 2, -3] ✅
               add 2 → map = {0:[-1, 2], 1:[0], 3:[1]}
i=3: prefix=4  new → map = {0:[-1,2], 1:[0], 3:[1], 4:[3]}
i=4: prefix=0  seen at [-1, 2]!
               → arr[0..4] = [1, 2, -3, 4, -4] ✅  (from -1)
               → arr[3..4] = [4, -4]             ✅  (from  2)
               add 4 → map = {0:[-1, 2, 4], ...}

3 subarrays ✅
```

### Pseudo Code

```
map = {0: [-1]}      ← virtual index -1 for prefix 0
prefix = 0

for i from 0 to n-1:
    prefix += arr[i]

    if prefix IS in map:
        for each prevIdx in map[prefix]:
            report subarray arr[prevIdx+1 .. i]

    append i to map[prefix]   ← ALWAYS append (unlike Q2's skip-on-repeat)
```

### Java Code (Interview-Quality)

```java
public List<int[]> findSubarrays(int[] arr) {
    int n = arr.length;
    List<int[]> result = new ArrayList<>();

    Map<Integer, List<Integer>> occurrences = new HashMap<>();
    occurrences.put(0, new ArrayList<>(Arrays.asList(-1))); // virtual index -1

    int prefix = 0;

    for (int i = 0; i < n; i++) {
        prefix += arr[i];

        if (occurrences.containsKey(prefix)) {
            // each previous occurrence gives one valid subarray ending at i
            for (int prevIdx : occurrences.get(prefix)) {
                result.add(new int[]{prevIdx + 1, i});
            }
        }

        // ALWAYS add current index — every occurrence matters
        occurrences.computeIfAbsent(prefix, x -> new ArrayList<>()).add(i);
    }

    return result;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n + k) | O(n) | Single pass; k = total pairs found; worst case k = n(n+1)/2 |

*(For an all-zeros array, output is O(n²) — this is unavoidable, not a bug.)*

### How to Explain in an Interview

> *"This uses the same prefix sum equality as the 'Largest Subarray with 0 Sum' problem. The one change: instead of storing only the first occurrence, I store a list of ALL occurrences of each prefix sum. When I reach index j and see prefix sum P, I loop through every previous index stored under P — each gives a distinct zero-sum subarray ending at j. I still initialize with {0: [-1]} to handle subarrays starting at index 0. And crucially, I always append the current index to the list — every occurrence matters here."*

---

## ⚠️ Common Mistakes

| Mistake | Fix |
|---|---|
| Using single `Integer` value in map | Use `List<Integer>` to collect ALL occurrences |
| Skipping on second occurrence (Q2 habit) | Always append current index in Q3 |
| Not initializing `{0: [-1]}` | Misses subarrays starting at index 0 |
| Appending BEFORE checking | Check all previous first, THEN append current index |

---

## 🔍 Edge Cases

| Input | Output | Why |
|---|---|---|
| `[0, 0, 0]` | 6 subarrays | n(n+1)/2 = 3×4/2 = 6 — every contiguous portion sums to 0 |
| `[1, 2, 3]` | 0 subarrays | All prefix sums distinct; no repeated values |
| `[0]` | 1 subarray | prefix=0 seen at -1 → arr[0..0] |
| `[-1, 1, -1, 1]` | 4 subarrays | Prefix sums: 0,-1,0,-1,0 — many repeated |

---

## 🧩 Pattern Recognition

**When to store FIRST vs ALL occurrences:**

| Goal | Map Value | Rule |
|---|---|---|
| Longest subarray (Q2) | `Integer` (first index) | Put only on first occurrence; skip repeats |
| All subarrays (Q3) | `List<Integer>` (all indices) | Always append; never skip |
| Count subarrays (LeetCode 560) | `Integer` (count) | Increment count at each prefix |

---

## 🎯 Interview Tips

1. **Explicitly contrast with Q2** — shows you see the connection and the difference
2. **Emphasize the List vs Integer** — the single most important structural change
3. **Always append vs skip** — two words, but they make the problem print-all vs print-one
4. **Walk through the all-zeros case** — explains why output can be O(n²) naturally

---

## 🔗 Related Problems

- GFG — Largest Subarray with 0 Sum (Q2 — first occurrence only)
- LeetCode 560 — Subarray Sum Equals K (count, not print)
- LeetCode 974 — Subarray Sums Divisible by K
- LeetCode 325 — Maximum Size Subarray Sum Equals k

---

## 📌 Revision Notes

- Same prefix equality insight as Q2: `prefix[j] == prefix[i]` → zero-sum subarray
- Map value changes from `Integer` to `List<Integer>` → the key difference
- Always append current index (Q2 skips repeats; Q3 never does)
- Init `{0: [-1]}` handles index-0 start — same as Q2
- Worst-case output is O(n²) for all-zeros arrays — acceptable, unavoidable

---

## 🏁 Key Takeaways

> The only structural difference between Q2 and Q3 is **List vs Integer** in the HashMap value. This one change shifts the algorithm from "find best" to "find all". Recognising these near-identical problems as a family — and knowing exactly which line to change — is what separates strong candidates in interviews.

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
