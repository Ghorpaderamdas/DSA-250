# 🪟 Sliding Window Pattern — Beginner's Guide

---

## 🤔 What Problem Does It Solve?

Imagine you have an array and you need to check **every subarray of size k**.

The naive approach: use two nested loops → **O(n × k)** — slow for large inputs.

**Sliding Window idea:** instead of re-computing the sum of k elements from scratch each time, just:
- ➕ **Add** the new element entering from the right
- ➖ **Remove** the old element leaving from the left

Result: **O(n)** — one single pass through the array. That's the magic.

---

## ✅ When Should You Use Sliding Window?

| ✅ USE it when... | ❌ Do NOT use it when... |
|---|---|
| Input is an **array / string / list** | Data is a linked list or non-contiguous |
| You need a **subarray or substring** | Elements are unordered / no sequence |
| Goal is **max, min, sum, count, average, longest, shortest** | — |
| Condition has **"at most K", "at least K", "exactly K"** | — |

---

## 📦 Two Types of Sliding Window

```
                        Sliding Window
                               |
           ----------------------------------------
           |                                      |
    Fixed Size Window                  Variable Size Window
    (window size k is directly given)  (a condition is given, no fixed size)

    Examples:                          Examples:
    • Max sum of subarray of size k    • Longest subarray with sum ≤ k
    • Average of every k-size window   • Smallest window with all characters
```

---

## 🧩 4-Step Framework (works for ANY sliding window problem)

```
Step 1 → Identify the Pattern
         Is it subarray/substring? max/min/sum/count?
         If yes → Sliding Window ✅

Step 2 → Fixed or Variable Window?
         Fixed   : k (size) is directly given in the problem
         Variable: a condition is given (e.g., "sum ≤ k", "at most K distinct chars")

Step 3 → Find the Starting Window
         Compute the answer for the first window manually
         (index 0 to k-1)

Step 4 → Slide the Window
         What do you ADD?   → arr[i]     (new element entering from right)
         What do you REMOVE? → arr[i-k]  (old element leaving from left)
```

---

## 🔍 Worked Example: Maximum Sum of Subarray of Size K

**Problem:** Given `arr = [100, 200, 300, 400]` and `k = 2`, find the maximum sum of any subarray of size 2.

**All windows:**
```
Window 1: [100, 200] → sum = 300
Window 2: [200, 300] → sum = 500
Window 3: [300, 400] → sum = 700  ← answer
```

### Applying the 4-Step Framework

**Step 1 — Pattern?**
Finding max sum of a subarray → Sliding Window ✅

**Step 2 — Fixed or Variable?**
`k = 2` is given directly → **Fixed Size Window**

**Step 3 — Starting Window?**
Sum of first k elements: `arr[0] + arr[1]` = `100 + 200` = **300**

**Step 4 — Next Window?**
```
ADD    arr[i]     → new element coming in from the right
REMOVE arr[i - k] → old element going out from the left
```

### Visual — How the Window Slides

```
Index:    0      1      2      3
Array: [100]  [200]  [300]  [400]

Step 1:  [████  ████]              → sum = 100 + 200 = 300
          ^left  ^right

Step 2:         [████  ████]       → remove 100, add 300 → sum = 500
                 ^left  ^right

Step 3:                [████  ████] → remove 200, add 400 → sum = 700 ✅
                        ^left  ^right
```

### Pseudocode

```
function maxSumSubarray(arr, k):
    n = length of arr

    // Step 3: Compute first window
    windowSum = sum of arr[0] to arr[k-1]
    maxSum = windowSum

    // Step 4: Slide the window
    for i from k to n-1:
        windowSum = windowSum + arr[i]       // add new element (right)
        windowSum = windowSum - arr[i - k]   // remove old element (left)
        maxSum = max(maxSum, windowSum)

    return maxSum
```

### Dry Run (arr = [100, 200, 300, 400], k = 2)

| i | Action | windowSum | maxSum |
|---|--------|-----------|--------|
| — | First window: 100 + 200 | 300 | 300 |
| 2 | +arr[2]=300, -arr[0]=100 | 500 | 500 |
| 3 | +arr[3]=400, -arr[1]=200 | 700 | **700** |

**Answer: 700** ✅

---

## 💡 Key Insights

### 1. Why is this faster than nested loops?

| Approach | How it works | Time Complexity |
|---|---|---|
| Naive (two loops) | Recompute sum of k elements for every window | O(n × k) |
| Sliding Window | Update sum in O(1) per step (one add, one subtract) | **O(n)** |

### 2. Fixed vs Variable Window

- **Fixed (this problem):** k is given → window size never changes
- **Variable:** a condition is given (e.g., "sum ≤ target") → use two pointers, shrink window when condition breaks → different strategy

### 3. Use `long`, not `int`!

With `arr[i]` up to 10⁶ and `k` up to 10⁶, max possible sum = **10¹²**

This **overflows** a 32-bit `int` (max ~2.1 × 10⁹).

```java
// ❌ Wrong — can overflow
int windowSum = 0;

// ✅ Correct — use long
long windowSum = 0;
```

---

## ⏱️ Time & Space Complexity

| | Complexity | Why |
|---|---|---|
| **Time** | **O(n)** | We visit each element exactly once |
| **Space** | **O(1)** | Only storing a few variables (windowSum, maxSum) |

No extra array needed. No nested loops. Just two pointers and running totals.

---

## 🧠 Quick Summary

```
Sliding Window = Optimized Nested Loop

Fixed Window   → k is given directly → loop from k to n, add arr[i], remove arr[i-k]
Variable Window → condition is given  → use two pointers (left, right), shrink when needed

Always use long for sums in competitive programming!
```