# 03 — Prefix Sums 🧮

> **Difficulty Level:** Beginner Friendly  
> **Prerequisites:** Arrays, Loops, Basic Math

---

## 📚 Table of Contents

1. [What is a Prefix Sum?](#-what-is-a-prefix-sum)
2. [Real-Life Analogy](#-real-life-analogy)
3. [How It Works (Step by Step)](#-how-it-works-step-by-step)
4. [Types of Prefix Sums](#-types-of-prefix-sums)
5. [Why Do We Need It?](#-why-do-we-need-it)
6. [When To Use Prefix Sum](#-when-to-use-prefix-sum)
7. [When NOT To Use Prefix Sum](#-when-not-to-use-prefix-sum)
8. [Benefits](#-benefits)
9. [Limitations](#-limitations)
10. [Code Template](#-code-template)
11. [Practice Problems](#-practice-problems)

---

## 🔍 What is a Prefix Sum?

A **Prefix Sum** (also called a **Cumulative Sum**) is a technique where you build a new array in which every position stores the **sum of all elements from the start up to that position** in the original array.

```
Original Array:   [3,  1,  4,  1,  5,  9,  2]
Index:             0   1   2   3   4   5   6

Prefix Sum Array: [3,  4,  8,  9, 14, 23, 25]
Index:             0   1   2   3   4   5   6
```

> 💡 `prefix[i]` = `arr[0] + arr[1] + ... + arr[i]`

---

## 🌍 Real-Life Analogy

Imagine you walk 10 steps on Monday, 5 on Tuesday, 8 on Wednesday, and 3 on Thursday.

| Day       | Steps | Total Steps So Far |
|-----------|-------|--------------------|
| Monday    | 10    | 10                 |
| Tuesday   | 5     | 15                 |
| Wednesday | 8     | 23                 |
| Thursday  | 3     | 26                 |

The **"Total Steps So Far"** column is your **Prefix Sum**!

Now, if someone asks *"How many steps did you walk from Tuesday to Thursday?"*  
👉 Answer = Total(Thursday) − Total(Monday) = **26 − 10 = 16**

You answered instantly without re-counting! That's the magic of prefix sums. ✨

---

## 🛠 How It Works (Step by Step)

### Step 1 — Build the Prefix Sum Array

```
arr    = [3, 1, 4, 1, 5]
prefix = [0, 0, 0, 0, 0]   ← start empty

prefix[0] = arr[0]          = 3
prefix[1] = prefix[0]+arr[1]= 3+1 = 4
prefix[2] = prefix[1]+arr[2]= 4+4 = 8
prefix[3] = prefix[2]+arr[3]= 8+1 = 9
prefix[4] = prefix[3]+arr[4]= 9+5 = 14
```

Result → `prefix = [3, 4, 8, 9, 14]`

### Step 2 — Answer Range Sum Queries in O(1)

> *"What is the sum from index 1 to index 3?"*

```
Sum(L, R) = prefix[R] - prefix[L-1]
Sum(1, 3) = prefix[3] - prefix[0]
          = 9 - 3
          = 6  ✅

Check: arr[1]+arr[2]+arr[3] = 1+4+1 = 6  ✅
```

**Formula:**
```
Sum of arr[L...R] = prefix[R] - prefix[L-1]

(When L = 0, Sum = prefix[R])
```

---

## 🗂 Types of Prefix Sums

### 1️⃣ 1D Prefix Sum (Most Common)

Works on a **1D array**. Used for range sum queries on a list.

```
arr    = [2, 4, 1, 3, 5]
prefix = [2, 6, 7, 10, 15]
```

---

### 2️⃣ 2D Prefix Sum

Works on a **2D matrix**. Used for rectangle sum queries.

```
Matrix:          2D Prefix:
1  2  3          1   3   6
4  5  6   →      5  12  21
7  8  9         12  27  45
```

Query: Sum of rectangle from (r1,c1) to (r2,c2):
```
Sum = prefix[r2][c2]
    - prefix[r1-1][c2]
    - prefix[r2][c1-1]
    + prefix[r1-1][c1-1]
```

---

### 3️⃣ Suffix Sum

Instead of building from the left (start), build from the **right (end)**.

```
arr    = [3, 1, 4, 1, 5]
suffix = [14, 11, 10, 6, 5]

suffix[i] = arr[i] + arr[i+1] + ... + arr[n-1]
```

Useful when you need "sum from i to end" queries.

---

### 4️⃣ Prefix XOR / Prefix AND / Prefix OR

Same idea — instead of `+`, use XOR / AND / OR.

```
arr        = [3, 5, 2, 7]
prefix_xor = [3, 6, 4, 3]   ← each element XORed from start
```

Used in range XOR queries.

---

### 5️⃣ Difference Array (Reverse of Prefix Sum)

Instead of querying ranges, you **update ranges** efficiently.

```
To add +5 to all elements from index L to R:
  diff[L]   += 5
  diff[R+1] -= 5

Then take prefix sum of diff[] to get final array.
```

Used when you have many range-update operations followed by point queries.

---

## ❓ Why Do We Need It?

Without prefix sum, every range query requires a loop:

```
// Brute Force — O(N) per query
int rangeSum(int[] arr, int L, int R) {
    int sum = 0;
    for (int i = L; i <= R; i++)
        sum += arr[i];
    return sum;
}
```

If you have **N = 100,000** elements and **Q = 100,000** queries:
- Brute Force → 100,000 × 100,000 = **10 billion operations** 😱 (TLE)
- Prefix Sum  → Build once O(N) + answer each query O(1) → **200,000 operations** ✅

> 🚀 Prefix Sum turns **O(N) per query → O(1) per query**

---

## ✅ When To Use Prefix Sum

Use prefix sum when:

| Situation | Example |
|-----------|---------|
| 🔁 Multiple range sum queries on a **static** (unchanging) array | "Find sum from i to j, Q times" |
| 📊 Count of elements satisfying a condition in a range | "Count even numbers from L to R" |
| 🎯 Finding subarrays with a target sum | "Find subarray with sum = k" |
| 🔢 Problems involving cumulative frequency | Histogram, distribution problems |
| 🗺 2D grid rectangle queries | "Sum of a rectangular sub-matrix" |
| ➕ Range update + single point query (Difference Array) | "Add 3 to all elements from i to j" |
| 🏃 XOR / bitwise range queries | "XOR from index 3 to 7" |

**Common problem patterns:**
- "Subarray sum equals K"
- "Range sum query"
- "Count subarrays with even sum"
- "Maximum subarray sum" (combined with other techniques)
- "Equilibrium index" (prefix sum = suffix sum)

---

## ❌ When NOT To Use Prefix Sum

Avoid prefix sum when:

| Situation | Why it Fails | Better Alternative |
|-----------|-------------|-------------------|
| 🔄 Array is **frequently updated** | Prefix array becomes stale after every update | Segment Tree / Fenwick Tree (BIT) |
| 🎲 You need **range min / range max** | Prefix sum only works for addition | Sparse Table / Segment Tree |
| 🌳 Tree or graph problems | Prefix sum is for linear arrays | DFS prefix sums / Euler Tour |
| 🔍 You need **positional info**, not just totals | Sum doesn't tell you where elements are | Binary Search / Two Pointers |
| 💾 Memory is extremely tight | Needs O(N) extra space | Sliding Window (if applicable) |

---

## 🌟 Benefits

| Benefit | Details |
|---------|---------|
| ⚡ **Speed** | O(1) per range query after O(N) build time |
| 🧠 **Simple to understand** | Just cumulative sums — easy to code |
| 📦 **Easy to implement** | Only needs a single extra array |
| 🔄 **Versatile** | Works with XOR, AND, OR, products, and more |
| 🏗 **Building block** | Base for more complex structures (BIT, Segment Tree) |
| 🧩 **Pairs well with** | HashMap (for subarray sum = K), Binary Search, Two Pointers |

---

## ⚠️ Limitations

| Limitation | Details |
|------------|---------|
| 🔄 **Static Array Only** | Cannot handle dynamic updates efficiently (rebuilding is O(N)) |
| 💾 **Extra Space** | Requires O(N) or O(N×M) additional memory |
| ➕ **Addition Only (by default)** | Doesn't directly support min/max queries |
| 🎯 **Off-by-One Errors** | `prefix[L-1]` fails when L=0 — needs careful indexing |
| 🔒 **Preprocessing Step** | Must build the prefix array first before answering queries |

---

## 💻 Code Template

### C++ — 1D Prefix Sum

```cpp
#include <bits/stdc++.h>
using namespace std;

int main() {
    int n;
    cin >> n;

    vector<int> arr(n), prefix(n);

    for (int i = 0; i < n; i++) cin >> arr[i];

    // Step 1: Build Prefix Sum
    prefix[0] = arr[0];
    for (int i = 1; i < n; i++)
        prefix[i] = prefix[i - 1] + arr[i];

    // Step 2: Answer Range Queries
    int L, R;
    cin >> L >> R;

    int sum = (L == 0) ? prefix[R] : prefix[R] - prefix[L - 1];

    cout << sum << "\n";
    return 0;
}
```

### Java — 1D Prefix Sum

```java
import java.util.*;

public class PrefixSum {
    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5};
        int n = arr.length;

        // Build Prefix Sum
        int[] prefix = new int[n];
        prefix[0] = arr[0];
        for (int i = 1; i < n; i++)
            prefix[i] = prefix[i - 1] + arr[i];

        // Range Query: sum from L to R
        int L = 1, R = 3;
        int sum = (L == 0) ? prefix[R] : prefix[R] - prefix[L - 1];

        System.out.println("Sum from " + L + " to " + R + " = " + sum);
    }
}
```

### Python — 1D Prefix Sum

```python
arr = [3, 1, 4, 1, 5]
n = len(arr)

# Build Prefix Sum
prefix = [0] * n
prefix[0] = arr[0]
for i in range(1, n):
    prefix[i] = prefix[i - 1] + arr[i]

# Range Query: sum from L to R
def range_sum(L, R):
    if L == 0:
        return prefix[R]
    return prefix[R] - prefix[L - 1]

print(range_sum(1, 3))   # Output: 6
```

---

## 📝 Practice Problems

| # | Problem | Difficulty | Key Idea |
|---|---------|------------|----------|
| 1 | Range Sum Query - Immutable (LeetCode 303) | 🟢 Easy | Classic 1D prefix sum |
| 2 | Range Sum Query 2D - Immutable (LeetCode 304) | 🟡 Medium | 2D prefix sum |
| 3 | Subarray Sum Equals K (LeetCode 560) | 🟡 Medium | Prefix sum + HashMap |
| 4 | Find Pivot Index (LeetCode 724) | 🟢 Easy | Prefix = Suffix |
| 5 | Product of Array Except Self (LeetCode 238) | 🟡 Medium | Prefix & Suffix product |
| 6 | Count Subarrays with Given XOR | 🟡 Medium | Prefix XOR + HashMap |
| 7 | Continuous Subarray Sum (LeetCode 523) | 🟡 Medium | Prefix sum + modulo |
| 8 | Corporate Flight Bookings (LeetCode 1109) | 🟡 Medium | Difference array |

---

## 🧠 Quick Summary

```
┌─────────────────────────────────────────────────────┐
│               PREFIX SUM CHEAT SHEET                │
├─────────────────────────────────────────────────────┤
│  Build:   prefix[i] = prefix[i-1] + arr[i]          │
│  Query:   sum(L,R)  = prefix[R] - prefix[L-1]       │
│  Time:    O(N) build  +  O(1) per query              │
│  Space:   O(N)                                       │
├─────────────────────────────────────────────────────┤
│  USE when:   static array + multiple range queries   │
│  AVOID when: frequent updates → use BIT/Seg Tree     │
└─────────────────────────────────────────────────────┘
```

---

> 📌 **Next Topic:** [04 - Two Pointers](../04-Two-Pointers/README.md)  
> 📌 **Previous Topic:** [02 - Sliding Window](../02-Sliding-Window/README.md)
