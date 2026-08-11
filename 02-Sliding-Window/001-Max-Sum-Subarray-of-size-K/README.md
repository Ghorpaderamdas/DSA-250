<img width="1149" height="1369" alt="Q1  Max Sum Subarray of Size K" src="https://github.com/user-attachments/assets/4a6120ea-7d3c-4b05-803a-2f906d2da864" />



# Q1. Max Sum Subarray of Size K

**Difficulty:** Easy &nbsp;|&nbsp; **Pattern:** Sliding Window &nbsp;|&nbsp; **Companies:** Amazon, Microsoft, Adobe

🔗 [GeeksForGeeks Link](https://www.geeksforgeeks.org/problems/max-sum-subarray-of-size-k5313/1)

---

## What is a Subarray?

A **subarray** is a **contiguous** (connected) part of an array — you pick a start and an end, and take everything in between.

```
arr = [1, 4, 2, 10, 23, 3]

Valid subarrays of size 3:
  [1, 4, 2]       ← starts at index 0
  [4, 2, 10]      ← starts at index 1
  [2, 10, 23]     ← starts at index 2
  [10, 23, 3]     ← starts at index 3
```

> ❌ `[1, 2, 23]` is NOT a subarray — the elements are not next to each other.

---

## 🪟 What is the Sliding Window Pattern?

> **Core idea:** Sliding Window converts an O(n×k) nested loop into a single O(n) loop by reusing previous window data instead of recomputing from scratch.

### When to USE Sliding Window

| ✅ Apply when...                        | ❌ Do NOT apply on... |
|-----------------------------------------|-----------------------|
| Input is an **array / string / list**   | Linked list           |
| You need a **subarray or substring**    | Non-contiguous data   |
| You're finding **max, min, sum, count, average, longest, shortest** | — |
| Condition uses **at most K, at least K, exactly K** | — |

### Two Types of Sliding Window

```
                        Sliding Window
                              |
          ----------------------------------------
          |                                      |
   Fixed Size Window                    Dynamic / Variable Size Window
   (window size = k is given)           (no fixed size — a condition is given)
   
   Example problems:                    Example problems:
   • Max sum of size-k subarray         • Longest subarray with sum ≤ k
   • Average of every size-k window     • Smallest window containing all chars
```

### 4-Step Framework to Solve Any Sliding Window Problem

```
Step 1: Identify the pattern
        → Is it subarray/substring? Is it max/min/sum/count?

Step 2: Is the window fixed or variable?
        → Fixed  : window size k is given directly
        → Variable: a condition is given (e.g. sum ≤ k)

Step 3: Find the data/info of the STARTING window
        → Compute the answer for the first window manually

Step 4: Find the data/info of the NEXT window
        → What do you ADD (entering element) and REMOVE (leaving element)?
```

---

## 📝 Problem

Given an array `arr[]` and a number `k`, return the **maximum sum** of any subarray of size exactly `k`.

### Examples

| Input                               | k | Output | Why                          |
|-------------------------------------|---|--------|------------------------------|
| `[100, 200, 300, 400]`              | 2 | 700    | 300 + 400 = 700 ✅           |
| `[1, 4, 2, 10, 23, 3, 1, 0, 20]`   | 4 | 39     | 4 + 2 + 10 + 23 = 39 ✅      |
| `[100, 200, 300, 400]`              | 1 | 400    | Just pick 400 ✅              |

---

## 🧠 Approaches

---

### Method 1 — Brute Force

**How it works (3 steps):**
1. Use **two nested loops** to check every possible subarray of size k
2. Calculate the sum of each subarray from scratch
3. Keep track of the maximum subarray sum seen so far

**Pseudo Code:**
```
maxSum = -infinity
for i from 0 to n-k:           ← pick starting index of window
    windowSum = 0
    for j from i to i+k-1:    ← sum k elements from index i
        windowSum += arr[j]
    maxSum = max(maxSum, windowSum)
return maxSum
```

**Dry Run** — `arr = [1, 4, 2, 10, 23, 3, 1, 0, 20]`, `k = 4`

```
i=0: arr[0..3] = 1+4+2+10   = 17        maxSum = 17
i=1: arr[1..4] = 4+2+10+23  = 39  ←     maxSum = 39  (new max!)
i=2: arr[2..5] = 2+10+23+3  = 38        maxSum = 39
i=3: arr[3..6] = 10+23+3+1  = 37        maxSum = 39
i=4: arr[4..7] = 23+3+1+0   = 27        maxSum = 39
i=5: arr[5..8] = 3+1+0+20   = 24        maxSum = 39

Answer: 39
```

**Why it's slow:** For n = 1,000,000 and k = 500,000, each window needs 500,000 additions → 500 billion total operations!

**Complexity Analysis:**

| Time   | Space | Reason                                      |
|--------|-------|---------------------------------------------|
| O(n×k) | O(1)  | Outer loop × inner loop, no extra memory    |

---

### Method 2 — Prefix Sum (Better)

**Idea:** Precompute a "running total" array called `prefix`. Then the sum of any window `[i..i+k-1]` can be found in O(1) using a simple subtraction.

**What is a prefix sum?**
```
arr    =  [ 1,  4,  2,  10,  23]
index  =    0   1   2   3    4

prefix =  [ 0,  1,  5,  7,  17,  40]
index  =    0   1   2   3   4    5

prefix[i] = arr[0] + arr[1] + ... + arr[i-1]
            (sum of the first i elements)

How to get sum of arr[1..3]:
  = prefix[4] - prefix[1]
  = 17 - 1
  = 16  ✅  (4 + 2 + 10 = 16)
```

**Formula:**
```
sum of window [i .. i+k-1]  =  prefix[i+k] - prefix[i]
```

**Pseudo Code:**
```
Build prefix array:
  prefix[0] = 0
  for i from 0 to n-1:
      prefix[i+1] = prefix[i] + arr[i]

maxSum = -infinity
for i from 0 to n-k:
    windowSum = prefix[i+k] - prefix[i]   ← O(1) per window
    maxSum = max(maxSum, windowSum)
return maxSum
```

**Dry Run** — `arr = [1, 4, 2, 10, 23, 3, 1, 0, 20]`, `k = 4`
```
prefix = [0, 1, 5, 7, 17, 40, 43, 44, 44, 64]

i=0: prefix[4] - prefix[0] = 17 - 0  = 17       maxSum = 17
i=1: prefix[5] - prefix[1] = 40 - 1  = 39  ←    maxSum = 39
i=2: prefix[6] - prefix[2] = 43 - 5  = 38        maxSum = 39
i=3: prefix[7] - prefix[3] = 44 - 7  = 37        maxSum = 39
i=4: prefix[8] - prefix[4] = 44 - 17 = 27        maxSum = 39
i=5: prefix[9] - prefix[5] = 64 - 40 = 24        maxSum = 39

Answer: 39 ✅
```

**Complexity Analysis:**

| Time | Space | Reason                                       |
|------|-------|----------------------------------------------|
| O(n) | O(n)  | One pass to build prefix + one pass to query; extra array of size n+1 |

---

### Method 3 — Sliding Window ⭐ (Optimal)

**Apply the 4-Step Framework:**

```
Step 1: Pattern?
        → Finding MAX SUM of a subarray → Sliding Window ✅

Step 2: Fixed or variable window?
        → k is given directly → FIXED SIZE window

Step 3: Data/info of starting window?
        → Sum of first k elements: arr[0] + arr[1] + ... + arr[k-1]

Step 4: Data/info of next window?
        → ADD arr[i]     (new element entering from the right)
        → REMOVE arr[i-k] (old element leaving from the left)
```

**Visual — how the window slides:**
```
arr = [1, 4, 2, 10, 23, 3],  k = 3

                     EXIT ←         → ENTER
                      ↓                 ↓
Initial:  [1,  4,  2] 10   23   3      windowSum = 1+4+2   = 7
Slide 1:   1  [4,  2, 10]  23   3      windowSum = 7+10-1  = 16  (add 10, remove 1)
Slide 2:   1   4  [2, 10, 23]   3      windowSum = 16+23-4 = 35  (add 23, remove 4)
Slide 3:   1   4   2  [10, 23, 3]      windowSum = 35+3-2  = 36  (add 3,  remove 2) ← max
```

**Pseudo Code:**
```
Step 1: Compute the FIRST window sum
    windowSum = arr[0] + arr[1] + ... + arr[k-1]
    maxSum    = windowSum

Step 2: Slide from index k to n-1
    for i from k to n-1:
        windowSum = windowSum + arr[i] - arr[i-k]   ← add new, remove old
        maxSum    = max(maxSum, windowSum)

return maxSum
```

**Dry Run** — `arr = [1, 4, 2, 10, 23, 3, 1, 0, 20]`, `k = 4`

```
Step 1: First window → [1, 4, 2, 10]
  windowSum = 1+4+2+10 = 17
  maxSum    = 17

Step 2: Slide
  i=4: add arr[4]=23, remove arr[0]=1  → windowSum = 17+23-1  = 39   maxSum = 39 ✅
  i=5: add arr[5]=3,  remove arr[1]=4  → windowSum = 39+3-4   = 38   maxSum = 39
  i=6: add arr[6]=1,  remove arr[2]=2  → windowSum = 38+1-2   = 37   maxSum = 39
  i=7: add arr[7]=0,  remove arr[3]=10 → windowSum = 37+0-10  = 27   maxSum = 39
  i=8: add arr[8]=20, remove arr[4]=23 → windowSum = 27+20-23 = 24   maxSum = 39

Answer: 39 ✅
```

**Complexity Analysis:**

| Time | Space | Reason                                             |
|------|-------|----------------------------------------------------|
| O(n) | O(1)  | Single pass through array; only 2-3 variables used |

---

## 📊 Comparison Table

| Method             | Time   | Space | When to use                               |
|--------------------|--------|-------|-------------------------------------------|
| Brute Force        | O(n×k) | O(1)  | Only for tiny inputs / understanding      |
| Prefix Sum         | O(n)   | O(n)  | When you need sums of many different ranges |
| **Sliding Window** | **O(n)** | **O(1)** | ⭐ **Best — use this in interviews**   |

---

## 💡 Key Insights

1. **Sliding window = optimized nested loop.** Instead of recomputing the sum of k elements each time (O(k) per window), you update it in O(1) with one addition and one subtraction.

2. **Fixed vs Variable window:** This problem has a fixed window size (k is given). If no size is given and a condition is given instead (e.g., "sum ≤ target"), that's a variable window problem — a different strategy.

3. **`maxSum = windowSum` (not `Integer.MIN_VALUE`):** We initialize `maxSum` with the first window's sum directly — since we already computed it, there's no need to start from the smallest possible value.

---

## ⚠️ Edge Cases to Know

| Case              | What happens                           |
|-------------------|----------------------------------------|
| k == n            | Only one window — the entire array     |
| k == 1            | Answer is just the maximum element     |
| All elements are 0 | Answer is 0                           |
| n < k             | No valid window exists → return 0      |

---

## 🏷️ Method Signature (GFG)

```java
// GFG online judge — Java
public int maxSubarraySum(int[] arr, int k)
```

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
