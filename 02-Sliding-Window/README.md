<img width="1536" height="1024" alt="Sliding Window Pattern — Beginner&#39;s Guide" src="https://github.com/user-attachments/assets/f520d109-c5eb-4de3-bb02-f1226ab00395" />



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




# 🪟 Sliding Window Pattern — Beginner's Visual Guide

> **One line summary:** Sliding window converts an **O(n × k)** nested loop into **O(n)** by reusing the previous window instead of recomputing from scratch.

---

## 🧠 The Core Idea — In One Picture

```
Instead of this (slow — recomputes every time):
┌─────────────────────────────────────────┐
│  for each starting position i:          │
│    for j from i to i+k:                 │  ← O(n × k) ❌
│      sum += arr[j]                      │
└─────────────────────────────────────────┘

Do this instead (fast — reuse previous sum):
┌─────────────────────────────────────────┐
│  windowSum += arr[i]     ← ADD entering │
│  windowSum -= arr[i-k]   ← REMOVE leaving  O(n) ✅
└─────────────────────────────────────────┘
```

---

## ✅ When to Use vs Skip

```
✅ USE Sliding Window when...           ❌ SKIP when...
─────────────────────────────────────   ──────────────────────────────
 Input is an array / string / list       Data is a Linked List
 You need a subarray or substring        Elements are non-contiguous
 Goal: max, min, sum, count              Elements are unordered
 Condition: "at most K", "exactly K"     No fixed or conditional window
```

---

## 📦 Two Types of Sliding Window

```
                     Sliding Window
                           │
         ──────────────────┴────────────────────
         │                                      │
  ┌─────────────────┐               ┌───────────────────────┐
  │  Fixed Window   │               │   Variable Window     │
  │  (k is given)   │               │   (condition given)   │
  └─────────────────┘               └───────────────────────┘
         │                                      │
  Window size stays the same         Window grows and shrinks
         │                                      │
  Example:                           Example:
  "Max sum of subarray               "Longest subarray
   of size k"                         with sum ≤ target"
```

---

## 🧩 4-Step Framework (works for ANY sliding window problem)

```
╔═══════╦══════════════════════════════════════════════════════════╗
║ Step  ║ What to do                                               ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  1    ║ Is it subarray/substring? max/min/sum/count?             ║
║       ║ If YES → Sliding Window ✅                               ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  2    ║ Fixed or Variable?                                       ║
║       ║ k given directly → Fixed                                 ║
║       ║ Condition given (e.g. sum ≤ k) → Variable                ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  3    ║ Find starting window data                                ║
║       ║ Compute sum of first k elements manually                 ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  4    ║ Slide the window                                         ║
║       ║ ADD  → arr[i]       (new element entering from right)    ║
║       ║ REMOVE → arr[i-k]   (old element leaving from left)      ║
╚═══════╩══════════════════════════════════════════════════════════╝
```

---

## 🔍 Worked Example — Maximum Sum of Subarray of Size K

**Problem:** `arr = [100, 200, 300, 400]`, `k = 2` → Find max sum of any subarray of size 2.

### Step 1 — Identify pattern
Finding **max sum of subarray** → Sliding Window ✅

### Step 2 — Fixed or Variable?
`k = 2` is given directly → **Fixed size window**

### Step 3 — Starting window
```
arr[0] + arr[1]  =  100 + 200  =  300
```

### Step 4 — Slide it!

```
ADD    arr[i]       new element entering from RIGHT
REMOVE arr[i - k]   old element leaving from LEFT
```

---

## 🎥 Visual — Watch the Window Slide

```
 Index:   [  0  ]   [  1  ]   [  2  ]   [  3  ]
 Array:   [ 100 ]   [ 200 ]   [ 300 ]   [ 400 ]
```

```
 ┌─────────────────────────────────────────────────────────────────────┐
 │                                                                     │
 │  Step 1:  ╔═══════╗  ╔═══════╗                                     │
 │           ║  100  ║  ║  200  ║    300      400                     │
 │           ╚═══════╝  ╚═══════╝                                     │
 │                                                                     │
 │           sum = 100 + 200 = 300    maxSum = 300                     │
 │           (first window: just add k elements directly)              │
 │                                                                     │
 ├─────────────────────────────────────────────────────────────────────┤
 │                                                                     │
 │  Step 2:            ╔═══════╗  ╔═══════╗                           │
 │            100      ║  200  ║  ║  300  ║    400                    │
 │                     ╚═══════╝  ╚═══════╝                           │
 │            └──out──┘                   └──in──┘                    │
 │                                                                     │
 │           sum = prevSum + arr[2] - arr[0]                          │
 │               = 300     + 300    - 100   = 500    maxSum = 500      │
 │                                                                     │
 │  ❓ Why not just 200 + 300 = 500 directly?                          │
 │     Both give 500, but sliding window does it in O(1).              │
 │     If k = 100000, recomputing 100000 elements each step is slow.   │
 │     Sliding window always does just 1 add + 1 subtract = O(1) ✅    │
 │                                                                     │
 ├─────────────────────────────────────────────────────────────────────┤
 │                                                                     │
 │  Step 3:                       ╔═══════╗  ╔═══════╗                │
 │            100      200        ║  300  ║  ║  400  ║                │
 │                                ╚═══════╝  ╚═══════╝                │
 │                     └──out──┘                      └──in──┘        │
 │                                                                     │
 │           sum = prevSum + arr[3] - arr[1]                          │
 │               = 500     + 400    - 200   = 700    maxSum = 700 ✅   │
 │                                                                     │
 └─────────────────────────────────────────────────────────────────────┘
```

---

## 📋 Dry Run Table

| `i` | Window elements | Action (sliding formula) | `windowSum` | `maxSum` |
|-----|-----------------|--------------------------|-------------|---------|
| —   | [100, 200] | First window: 100 + 200 | **300** | 300 |
| 2   | [200, 300] | prevSum(300) + arr[2](300) − arr[0](100) | **500** | 500 |
| 3   | [300, 400] | prevSum(500) + arr[3](400) − arr[1](200) | **700** | **700 ✅** |

> **Note:** The "Window elements" column shows the actual window. The "Action" column shows the O(1) sliding formula — not a direct recompute. Both give the same answer, but the formula scales to any k size.

---

## 💻 Pseudocode

```
function maxSumSubarray(arr, k):
    n = length of arr

    // Step 3: starting window (only time we loop over k elements)
    windowSum = arr[0] + arr[1] + ... + arr[k-1]
    maxSum = windowSum

    // Step 4: slide — O(1) update per step
    for i from k to n-1:
        windowSum = windowSum + arr[i]        // ← ADD   entering element (right)
        windowSum = windowSum - arr[i - k]    // ← REMOVE leaving element (left)
        maxSum = max(maxSum, windowSum)

    return maxSum
```

---

## ⏱️ Complexity Analysis

```
 ┌───────────────────┬─────────────────────┬────────────────────────────────┐
 │ Approach          │ Time Complexity     │ Why                            │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Naive (2 loops)   │   O(n × k)  ❌     │ Recomputes k elems every step  │
 │ Sliding Window    │   O(n)      ✅     │ Each element visited once      │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Space             │   O(1)      ✅     │ Only a few variables stored    │
 └───────────────────┴─────────────────────┴────────────────────────────────┘
```

---

## 💡 Key Insights

### 1. Sliding Window = Optimized Nested Loop
Every slide does just **one add + one subtract** instead of re-summing k elements.
That's why it goes from O(n × k) → O(n).

### 2. "Why not just recompute directly?" — The k problem

```
Small k (k=2):   200 + 300 = 500  ← only 2 operations, seems fine
Large k (k=100000): elem1 + elem2 + ... + elem100000 ← 100000 ops per step! ❌

Sliding window:  prevSum + newElem - oldElem ← always 2 ops, any k ✅
```

### 3. Fixed vs Variable

```
Fixed window   → k is given → loop i from k to n, add arr[i], remove arr[i-k]
Variable window → condition given → two pointer approach, shrink window when condition breaks
```

### 4. ⚠️ Always use `long` not `int` for sums!

```
Max arr[i] = 10^6
Max k      = 10^6
Max sum    = 10^12   ← OVERFLOWS int (max ~2.1 × 10^9)
```

```java
// ❌ Wrong — silent overflow bug
int windowSum = 0;

// ✅ Correct
long windowSum = 0;
```

---

## 🧠 Quick Summary Card

```
╔══════════════════════════════════════════════════════════════╗
║           SLIDING WINDOW — QUICK REFERENCE                   ║
╠══════════════════════════════════════════════════════════════╣
║  Pattern type  : Array / String / Subarray problems          ║
║  Key operation : ADD entering · REMOVE leaving               ║
║  Formula       : newSum = prevSum + arr[i] - arr[i-k]        ║
║  Time          : O(n)   — one pass through array             ║
║  Space         : O(1)   — no extra array needed              ║
╠══════════════════════════════════════════════════════════════╣
║  Fixed window  : k is given → classic sliding                ║
║  Variable win  : condition given → two pointers + shrink     ║
╠══════════════════════════════════════════════════════════════╣
║  ⚠️  Always use long for sum in CP — int can overflow!       ║
╚══════════════════════════════════════════════════════════════╝
```

---

## 📚 Practice Problems

| # | Problem | Type | Difficulty |
|---|---------|------|------------|
| 1 | Max sum subarray of size k | Fixed | ⭐ Easy |
| 2 | Average of every k-size window | Fixed | ⭐ Easy |
| 3 | Longest subarray with sum ≤ k | Variable | ⭐⭐ Medium |
| 4 | Longest substring with at most K distinct chars | Variable | ⭐⭐ Medium |
| 5 | Minimum window substring | Variable | ⭐⭐⭐ Hard |

---

*Made while learning DSA — one pattern at a time.* 🚀
