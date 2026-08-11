<img width="1536" height="1024" alt="Prefix Sum Pattern — Beginner&#39;s Guide" src="https://github.com/user-attachments/assets/8ce22848-c153-4aa0-aa14-de042c77aad5" />

<img width="1024" height="1536" alt="Prefix Sum Pattern — Beginner&#39;s Guide  2" src="https://github.com/user-attachments/assets/287648a6-5714-4e8a-b290-4e88764364bc" />


# Prefix Sum Pattern — Beginner's Guide

---

## 🤔 What Problem Does It Solve?

Imagine you have an array and you need to answer **many "sum from index L to R" questions**.

The naive approach: loop from L to R every time → **O(N) per query** — if you have Q queries, total is **O(N × Q)** — slow for large inputs.

**Prefix Sum idea:** instead of re-summing the range from scratch every time, just:
- 🏗 **Build** a prefix array once — each cell stores the running total from index 0
- ⚡ **Answer** any range query in O(1) using just two values from that prefix array

Result: **O(N) build + O(1) per query** — That's the magic.

---

## ✅ When Should You Use Prefix Sum?

| ✅ USE it when... | ❌ Do NOT use it when... |
|---|---|
| Input is a **static array** (no updates after building) | Array is **frequently modified** after building |
| You need **multiple range sum / count queries** | You need **range min or range max** |
| Goal is **sum, count, XOR of a subarray** | — |
| Condition is **"sum equals K", "count in range L to R"** | — |

---

## 📦 Two Types of Prefix Sum

```
                        Prefix Sum
                               |
           ----------------------------------------
           |                                      |
    1D Prefix Sum                        2D Prefix Sum
    (works on a 1D array)                (works on a matrix / grid)

    Examples:                            Examples:
    • Sum of any subarray [L, R]         • Sum of any rectangle in grid
    • Count subarrays with sum = k       • Count of elements in sub-matrix
    • Equilibrium index                  • Max sum sub-matrix
```

---

## 🧩 4-Step Framework (works for ANY prefix sum problem)

```
Step 1 → Identify the Pattern
         Do you have multiple range queries on a static array?
         Do you need sum / count / XOR over a subarray?
         If yes → Prefix Sum ✅

Step 2 → Build the Prefix Array (do this ONCE)
         prefix[0] = arr[0]
         prefix[i] = prefix[i-1] + arr[i]   for i from 1 to n-1

Step 3 → Write the Range Query Formula
         Sum of arr[L...R] = prefix[R] - prefix[L-1]
         Special case: if L == 0, answer is just prefix[R]

Step 4 → Answer all queries in O(1)
         For each query (L, R):
             if L == 0  → answer = prefix[R]
             else       → answer = prefix[R] - prefix[L-1]
```

---

## 🔍 Worked Example: Range Sum Query

**Problem:** Given `arr = [3, 1, 4, 1, 5]`, answer multiple queries:
- Query 1: sum from index 1 to 3 → ?
- Query 2: sum from index 0 to 4 → ?
- Query 3: sum from index 2 to 4 → ?

**All range answers (brute force check):**
```
Query 1: arr[1]+arr[2]+arr[3] = 1+4+1 = 6
Query 2: arr[0]+arr[1]+arr[2]+arr[3]+arr[4] = 3+1+4+1+5 = 14
Query 3: arr[2]+arr[3]+arr[4] = 4+1+5 = 10
```

### Applying the 4-Step Framework

**Step 1 — Pattern?**
Multiple range sum queries on a fixed array → Prefix Sum ✅

**Step 2 — Build the Prefix Array?**
```
arr    = [3,  1,  4,  1,  5]
Index:    0   1   2   3   4

prefix[0] = 3
prefix[1] = prefix[0] + arr[1] = 3 + 1 = 4
prefix[2] = prefix[1] + arr[2] = 4 + 4 = 8
prefix[3] = prefix[2] + arr[3] = 8 + 1 = 9
prefix[4] = prefix[3] + arr[4] = 9 + 5 = 14

prefix = [3, 4, 8, 9, 14]
```

**Step 3 — Formula?**
```
Sum(L, R) = prefix[R] - prefix[L-1]
(if L == 0, answer = prefix[R])
```

**Step 4 — Answer queries in O(1)?**
```
Query 1: L=1, R=3 → prefix[3] - prefix[0] = 9 - 3 = 6   ✅
Query 2: L=0, R=4 → prefix[4]             = 14           ✅
Query 3: L=2, R=4 → prefix[4] - prefix[1] = 14 - 4 = 10  ✅
```

### Visual — How the Prefix Array is Built

```
Index:    0      1      2      3      4
Array: [  3  ] [  1  ] [  4  ] [  1  ] [  5  ]

Step 1:  [  3  ]
          prefix[0] = 3

Step 2:  [  3  ] → [  4  ]
          prefix[1] = 3 + 1 = 4

Step 3:  [  3  ] → [  4  ] → [  8  ]
          prefix[2] = 4 + 4 = 8

Step 4:  [  3  ] → [  4  ] → [  8  ] → [  9  ]
          prefix[3] = 8 + 1 = 9

Step 5:  [  3  ] → [  4  ] → [  8  ] → [  9  ] → [ 14  ]
          prefix[4] = 9 + 5 = 14
```

```
Now answering Query 1: sum from index 1 to 3
                                             ↓ prefix[R]
prefix = [  3  ] [  4  ] [  8  ] [  9  ] [ 14  ]
          ↑ prefix[L-1]           ↑ prefix[3]

Answer = prefix[3] - prefix[0] = 9 - 3 = 6  ✅
```

### Pseudocode

```
function buildPrefix(arr):
    n = length of arr
    prefix = new array of size n

    // Step 2: Build prefix array (done only once)
    prefix[0] = arr[0]
    for i from 1 to n-1:
        prefix[i] = prefix[i-1] + arr[i]

    return prefix

function rangeQuery(prefix, L, R):
    // Step 4: Answer in O(1)
    if L == 0:
        return prefix[R]
    return prefix[R] - prefix[L-1]
```

### Dry Run — Building prefix (arr = [3, 1, 4, 1, 5])

| i | arr[i] | prefix[i-1] | prefix[i] = prefix[i-1] + arr[i] |
|---|--------|-------------|-----------------------------------|
| 0 | 3      | —           | **3**                             |
| 1 | 1      | 3           | 3 + 1 = **4**                     |
| 2 | 4      | 4           | 4 + 4 = **8**                     |
| 3 | 1      | 8           | 8 + 1 = **9**                     |
| 4 | 5      | 9           | 9 + 5 = **14**                    |

**Result: prefix = [3, 4, 8, 9, 14]** ✅

---

## 💡 Key Insights

### 1. Why is this faster than brute force?

| Approach | How it works | Time Complexity |
|---|---|---|
| Brute Force (inner loop) | Re-sum every element from L to R for each query | O(N) per query = O(N × Q) total |
| Prefix Sum | Build once, then subtract two values | **O(N) build + O(1) per query** |

### 2. The "L == 0" Edge Case

The formula `prefix[R] - prefix[L-1]` breaks when L = 0 because `prefix[-1]` doesn't exist!

```
// Always handle this case
if (L == 0) return prefix[R];            // ← no subtraction needed
else        return prefix[R] - prefix[L-1];
```

### 3. Use `long`, not `int`!

With `arr[i]` up to 10⁶ and `n` up to 10⁶, max prefix sum = **10¹²**

This **overflows** a 32-bit `int` (max ~2.1 × 10⁹).

```java
// ❌ Wrong — can silently overflow
int[] prefix = new int[n];

// ✅ Correct — use long
long[] prefix = new long[n];
```

---

## ⏱️ Time & Space Complexity

| | Complexity | Why |
|---|---|---|
| **Build Time** | **O(N)** | Visit each element once to fill prefix array |
| **Query Time** | **O(1)** | Just two array lookups and one subtraction |
| **Space** | **O(N)** | One extra prefix array of same size as input |

Build it once. Answer everything in O(1). That's the trade-off — spend O(N) space to save query time.

---

## 🧠 Quick Summary

```
Prefix Sum = Precompute Once, Query Instantly

Build Phase  → prefix[i] = prefix[i-1] + arr[i]   ← done ONCE in O(N)
Query Phase  → sum(L,R)  = prefix[R] - prefix[L-1] ← done in O(1)

Edge case: if L == 0, answer = prefix[R]   ← no subtraction!

Always use long[] not int[] for prefix array in competitive programming!
```

---

# 🔢 Prefix Sum Pattern — Beginner's Visual Guide

> **One line summary:** Prefix sum converts an **O(N × Q)** repeated-range-sum into **O(N + Q)** by precomputing a running total once and answering every query with two array accesses.

---

## 🧠 The Core Idea — In One Picture

```
Instead of this (slow — re-sums every time):
┌─────────────────────────────────────────┐
│  for each query (L, R):                 │
│    sum = 0                              │
│    for i from L to R:                   │  ← O(N) per query ❌
│      sum += arr[i]                      │
└─────────────────────────────────────────┘

Do this instead (fast — precompute then lookup):
┌─────────────────────────────────────────┐
│  Build: prefix[i] = prefix[i-1] + arr[i]│  ← O(N) once
│  Query: prefix[R] - prefix[L-1]         │  ← O(1) per query ✅
└─────────────────────────────────────────┘
```

---

## ✅ When to Use vs Skip

```
✅ USE Prefix Sum when...              ❌ SKIP when...
──────────────────────────────────     ──────────────────────────────────
 Static array (no updates mid-way)      Array values change frequently
 Multiple range sum/count queries       Need range MIN or range MAX
 Subarray sum equals K                  Need exact positions, not totals
 Condition involves sum over [L, R]     Memory is critically tight
```

---

## 📦 Two Types of Prefix Sum

```
                     Prefix Sum
                           │
         ──────────────────┴────────────────────
         │                                      │
  ┌─────────────────┐               ┌───────────────────────┐
  │  1D Prefix Sum  │               │   2D Prefix Sum       │
  │  (on array)     │               │   (on matrix/grid)    │
  └─────────────────┘               └───────────────────────┘
         │                                      │
  One prefix array                  One prefix matrix
         │                                      │
  Example:                           Example:
  "Sum of arr[L...R]                 "Sum of rectangle
   answered in O(1)"                  (r1,c1) to (r2,c2)"
```

---

## 🧩 4-Step Framework (works for ANY prefix sum problem)

```
╔═══════╦══════════════════════════════════════════════════════════╗
║ Step  ║ What to do                                               ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  1    ║ Multiple range queries on a fixed array?                 ║
║       ║ Sum / count / XOR over subarrays?                        ║
║       ║ If YES → Prefix Sum ✅                                   ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  2    ║ Build the prefix array — do this ONCE                    ║
║       ║ prefix[0] = arr[0]                                       ║
║       ║ prefix[i] = prefix[i-1] + arr[i]                        ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  3    ║ Set up the query formula                                 ║
║       ║ sum(L, R) = prefix[R] - prefix[L-1]                     ║
║       ║ Edge case: if L == 0, answer = prefix[R]                ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  4    ║ Answer every query in O(1)                               ║
║       ║ LOOKUP  → prefix[R]                                      ║
║       ║ SUBTRACT → prefix[L-1]  (skip if L == 0)                ║
╚═══════╩══════════════════════════════════════════════════════════╝
```

---

## 🔍 Worked Example — Range Sum Query

**Problem:** `arr = [3, 1, 4, 1, 5]` → Answer: sum from index 1 to 3?

### Step 1 — Identify pattern
Multiple **range sum queries** on a static array → Prefix Sum ✅

### Step 2 — Build prefix array
```
prefix[0] = arr[0] = 3
prefix[1] = 3 + 1  = 4
prefix[2] = 4 + 4  = 8
prefix[3] = 8 + 1  = 9
prefix[4] = 9 + 5  = 14
```

### Step 3 — Query formula
```
sum(L=1, R=3) = prefix[3] - prefix[0]
              = 9 - 3
              = 6  ✅
```

### Step 4 — Answer in O(1)
```
LOOKUP prefix[R=3]   = 9
SUBTRACT prefix[L-1=0] = 3
Result = 6  ✅
```

---

## 🎥 Visual — Watch the Prefix Array Build & Query

```
 Index:   [  0  ]   [  1  ]   [  2  ]   [  3  ]   [  4  ]
 Array:   [  3  ]   [  1  ]   [  4  ]   [  1  ]   [  5  ]
```

```
 ┌─────────────────────────────────────────────────────────────────────┐
 │  BUILD PHASE (once, left to right)                                  │
 │                                                                     │
 │  i=0: prefix[0] = arr[0] = 3                                        │
 │       ╔═══════╗                                                     │
 │       ║   3   ║   ·       ·       ·       ·                        │
 │       ╚═══════╝                                                     │
 │                                                                     │
 │  i=1: prefix[1] = prefix[0] + arr[1] = 3 + 1 = 4                   │
 │       ╔═══════╗  ╔═══════╗                                         │
 │       ║   3   ║  ║   4   ║   ·       ·       ·                    │
 │       ╚═══════╝  ╚═══════╝                                         │
 │                                                                     │
 │  i=2: prefix[2] = prefix[1] + arr[2] = 4 + 4 = 8                   │
 │       ╔═══════╗  ╔═══════╗  ╔═══════╗                             │
 │       ║   3   ║  ║   4   ║  ║   8   ║   ·       ·                │
 │       ╚═══════╝  ╚═══════╝  ╚═══════╝                             │
 │                                                                     │
 │  i=3: prefix[3] = prefix[2] + arr[3] = 8 + 1 = 9                   │
 │       ╔═══════╗  ╔═══════╗  ╔═══════╗  ╔═══════╗                 │
 │       ║   3   ║  ║   4   ║  ║   8   ║  ║   9   ║   ·            │
 │       ╚═══════╝  ╚═══════╝  ╚═══════╝  ╚═══════╝                 │
 │                                                                     │
 │  i=4: prefix[4] = prefix[3] + arr[4] = 9 + 5 = 14                  │
 │       ╔═══════╗  ╔═══════╗  ╔═══════╗  ╔═══════╗  ╔════════╗    │
 │       ║   3   ║  ║   4   ║  ║   8   ║  ║   9   ║  ║   14   ║   │
 │       ╚═══════╝  ╚═══════╝  ╚═══════╝  ╚═══════╝  ╚════════╝    │
 │                                                                     │
 ├─────────────────────────────────────────────────────────────────────┤
 │  QUERY PHASE — sum(L=1, R=3)                                        │
 │                                                                     │
 │       ╔═══════╗  ╔═══════╗  ╔═══════╗  ╔═══════╗  ╔════════╗    │
 │       ║   3   ║  ║   4   ║  ║   8   ║  ║   9   ║  ║   14   ║   │
 │       ╚═══════╝  ╚═══════╝  ╚═══════╝  ╚═══════╝  ╚════════╝    │
 │          ↑                                 ↑                        │
 │       prefix[L-1=0] = 3          prefix[R=3] = 9                   │
 │                                                                     │
 │       Answer = prefix[3] - prefix[0] = 9 - 3 = 6  ✅               │
 │                                                                     │
 │  ❓ Why prefix[L-1] and not prefix[L]?                              │
 │     prefix[R] = sum of arr[0...R]                                   │
 │     prefix[L-1] = sum of arr[0...L-1]  ← everything BEFORE L       │
 │     Subtracting removes the part we don't want ✅                   │
 │                                                                     │
 └─────────────────────────────────────────────────────────────────────┘
```

---

## 📋 Dry Run Table — Query (L=1, R=3)

| Step | Action | Value |
|------|--------|-------|
| Build | prefix = [3, 4, 8, 9, 14] | — |
| Query | Look up prefix[R=3] | **9** |
| Query | Look up prefix[L-1=0] | **3** |
| Query | Subtract: 9 − 3 | **6 ✅** |

> **Verify:** arr[1]+arr[2]+arr[3] = 1+4+1 = **6** ✅

---

## 💻 Java Code

```java
import java.util.*;

public class PrefixSum {

    // Step 2: Build prefix array — call this ONCE
    static long[] buildPrefix(int[] arr) {
        int n = arr.length;
        long[] prefix = new long[n];    // use long to avoid overflow

        prefix[0] = arr[0];
        for (int i = 1; i < n; i++) {
            prefix[i] = prefix[i - 1] + arr[i];
        }
        return prefix;
    }

    // Step 4: Answer range sum query in O(1)
    static long rangeSum(long[] prefix, int L, int R) {
        if (L == 0) return prefix[R];           // edge case: no subtraction needed
        return prefix[R] - prefix[L - 1];
    }

    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5};

        // Build once
        long[] prefix = buildPrefix(arr);

        // Answer multiple queries in O(1) each
        System.out.println(rangeSum(prefix, 1, 3));  // 6
        System.out.println(rangeSum(prefix, 0, 4));  // 14
        System.out.println(rangeSum(prefix, 2, 4));  // 10
    }
}
```

---

## ⏱️ Complexity Analysis

```
 ┌───────────────────┬─────────────────────┬────────────────────────────────┐
 │ Phase             │ Time Complexity     │ Why                            │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Brute Force Query │   O(N × Q)  ❌     │ Re-sums range for every query  │
 │ Prefix Build      │   O(N)      ✅     │ Each element visited once      │
 │ Prefix Query      │   O(1)      ✅     │ Two lookups + one subtract     │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Space             │   O(N)      ⚠️     │ Extra prefix array required    │
 └───────────────────┴─────────────────────┴────────────────────────────────┘
```

---

## 💡 Key Insights

### 1. Prefix Sum = Precomputed Running Total
Every prefix[i] silently stores the entire sum from index 0 to i.
Subtract two of them and you instantly get any subarray sum.
That's why it goes from O(N × Q) → O(N + Q).

### 2. "Why prefix[L-1] and not prefix[L]?" — The off-by-one explained

```
prefix[R]   = arr[0] + arr[1] + ... + arr[L-1] + arr[L] + ... + arr[R]
prefix[L-1] = arr[0] + arr[1] + ... + arr[L-1]

Subtract:
prefix[R] - prefix[L-1] = arr[L] + arr[L+1] + ... + arr[R]  ← exactly what we want ✅
```

### 3. Fixed build, many queries

```
One query  (Q=1):   Brute force O(N)   vs   Prefix O(N + 1)   → roughly same
Many queries (Q=N): Brute force O(N²)  vs   Prefix O(N + N)   → Prefix wins massively
```

### 4. ⚠️ Always use `long[]` not `int[]`!

```
Max arr[i] = 10^9
Max n      = 10^5
Max prefix = 10^14  ← OVERFLOWS int (max ~2.1 × 10^9)
```

```java
// ❌ Wrong — silent overflow bug
int[] prefix = new int[n];

// ✅ Correct
long[] prefix = new long[n];
```

---

## 🧠 Quick Summary Card

```
╔══════════════════════════════════════════════════════════════╗
║           PREFIX SUM — QUICK REFERENCE                       ║
╠══════════════════════════════════════════════════════════════╣
║  Build     : prefix[i] = prefix[i-1] + arr[i]               ║
║  Query     : sum(L,R)  = prefix[R] - prefix[L-1]            ║
║  Edge case : if L == 0, answer = prefix[R]                   ║
║  Build Time: O(N)  — one pass to fill prefix array           ║
║  Query Time: O(1)  — two lookups + one subtract              ║
║  Space     : O(N)  — one extra array                         ║
╠══════════════════════════════════════════════════════════════╣
║  USE when  : static array + multiple range queries           ║
║  AVOID when: array updates frequently → use BIT/Seg Tree     ║
╠══════════════════════════════════════════════════════════════╣
║  ⚠️  Always use long[] not int[] — prefix can overflow!      ║
╚══════════════════════════════════════════════════════════════╝
```



> 📌 **Next Topic:** [04 - Two Pointers](../04-Two-Pointers/README.md)  
> 📌 **Previous Topic:** [02 - Sliding Window](../02-Sliding-Window/README.md)

*Made while learning DSA — one pattern at a time.* 🚀
