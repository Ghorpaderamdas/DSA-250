<img width="1024" height="1536" alt="Q4  Count Subsequences with Product Less Than K (using DP)" src="https://github.com/user-attachments/assets/4091fb4f-fc94-4acd-8e7f-693324f04abc" />



# Q4. Count Subsequences with Product Less Than K (using DP)

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Dynamic Programming (0/1 Knapsack on Product Space) &nbsp;|&nbsp; **Companies:** Amazon, Google, Microsoft

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/dsa/count-subsequences-product-less-k/)

---

## ⚠️ Important Note — Pattern Alert

> This problem **looks like** a prefix sum problem (it's in the prefix sums chapter),  
> but the **optimal solution uses Dynamic Programming** — specifically 0/1 Knapsack adapted for products.  
> Why? Because we're counting **subsequences** (any subset, not contiguous), which requires DP to track products efficiently.

---

## 📝 Problem Statement

Given an array of **positive integers** `arr[]` and a positive integer `k`, count the number of **non-empty subsequences** of `arr` whose **product of elements is less than `k`**.

A **subsequence** = any subset of elements chosen in order (not necessarily contiguous).

### Examples

| Input | k | Output | Valid Subsequences |
|---|---|---|---|
| `[1, 2, 3, 4]` | 10 | 11 | `{1},{2},{3},{4},{1,2},{1,3},{1,4},{2,3},{2,4},{1,2,3},{1,2,4}` |
| `[1, 2]` | 3 | 3 | `{1},{2},{1,2}` |
| `[2, 3]` | 7 | 3 | `{2},{3},{2,3}`=6<7 |
| `[4]` | 5 | 1 | `{4}` |
| `[5, 3, 2]` | 7 | 4 | `{5},{3},{2},{3,2}`=6 |

---

## 🔒 Constraints

- `1 ≤ arr.length ≤ 10`
- `1 ≤ arr[i] ≤ 100`
- `1 ≤ k ≤ 10⁵`
- All elements are positive integers

---

## 🔑 Key Observations

- We want **subsequences** (all 2^n subsets), not subarrays (contiguous windows)
- Products can grow large quickly → DP over the **product space** [1, k-1] 
- Each element is used at most once → 0/1 Knapsack (not unbounded)
- Iterating product space **high-to-low** prevents reusing the same element
- An "empty" subsequence has product 1 → seed `dp[1] = 1` and subtract 1 at the end
- Total time O(n × k) is feasible for small n and moderate k

---

## 💡 Intuition Building

**Why brute force is obvious but exponential:**  
Enumerate all 2^n subsets. For each, compute product. Count if product < k.  
For n = 20 that's ~1 million subsets. Slow.

**The DP key insight — "product space":**
```
Instead of tracking which elements are chosen,
track HOW MANY subsequences produce each product value.

dp[j] = number of subsequences with product exactly j

For each new element val, any existing subsequence with product p
can be extended to have product p × val.
```

**Why 0/1 Knapsack (high-to-low iteration)?**
```
Suppose val = 2. If we iterate low-to-high:
  dp[2] += dp[1]   (correct: subsequence × val → product 2)
  dp[4] += dp[2]   (WRONG: dp[2] was just updated using val!
                    → this uses val TWICE in the same subsequence)

High-to-low avoids this: we read dp[j/val] BEFORE it's updated in this round.
```

**From O(2^n) → O(n × k):**
```
O(2^n * n): enumerate subsets, compute product each time
O(n × k):   1D DP over product space, 2 nested loops — polynomial
O(n × k):   same time, O(k) space (compress 2D → 1D)
```

---

## 📊 Approaches Overview

| Approach | Technique | Time | Space | Use In Interview? |
|---|---|---|---|---|
| Brute Force | Recursive subset enumeration | O(2^n) | O(n) | ❌ Exponential |
| 2D DP | dp[i][j] = subsets of first i elems with product j | O(n × k) | O(n × k) | ⚠️ Shows logic |
| **1D DP (Optimised)** ⭐ | Space-compressed 0/1 knapsack | **O(n × k)** | **O(k)** | ✅ Always |

---

## APPROACH 1 — BRUTE FORCE

### Idea
Recursively try including or excluding each element. At each leaf, if at least one element was taken and product < k, count it.

**Cleaner trick:** Count ALL subsets (including empty) with product < k, then subtract 1 for the empty subset.

### Algorithm
1. Recursive function `count(index, product)`:
   - If `product >= k` → prune (return 0)
   - If `index == n` → valid (return 1)
   - Return `count(index+1, product) + count(index+1, product × arr[index])`
2. Answer = `count(0, 1) - 1`  (subtract 1 for empty subset)

### Dry Run — `arr = [2, 3]`, k = 7

```
count(0, 1):
  skip 2 → count(1, 1):
    skip 3 → count(2, 1) = 1  (empty, product=1 < 7)
    take 3 → count(2, 3) = 1  (product=3 < 7)
    total = 2
  take 2 → count(1, 2):
    skip 3 → count(2, 2) = 1  (product=2 < 7)
    take 3 → count(2, 6) = 1  (product=6 < 7)
    total = 2
  total = 4

answer = 4 - 1 = 3  ({2}, {3}, {2,3}) ✅
```

### Java Code

```java
public int countSubsetsBrute(int[] arr, int k) {
    return countHelper(arr, 0, 1L, k) - 1;  // -1 for empty subset
}

private int countHelper(int[] arr, int index, long product, int k) {
    if (product >= k)          return 0;  // prune
    if (index == arr.length)   return 1;  // valid subset

    return countHelper(arr, index + 1, product, k)              // skip
         + countHelper(arr, index + 1, product * arr[index], k); // include
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(2^n) | O(n) | 2^n leaf nodes; n-deep recursion stack |

---

## APPROACH 2 — BETTER: 2D DP

### Idea
`dp[i][j]` = number of subsequences using `arr[0..i-1]` with product **exactly** `j`.

### Recurrence
```
dp[i][j] = dp[i-1][j]                    ← exclude arr[i-1]
          + dp[i-1][j / arr[i-1]]         ← include arr[i-1]
            (only when j % arr[i-1] == 0)
```

### Base Case
`dp[0][1] = 1` — empty array, empty subsequence has product 1.

### Dry Run — `arr = [1, 2]`, k = 3

```
dp[0] = [0, 1, 0]   (indices 0..2)

i=1, val=1:
  j=1: dp[1][1] = dp[0][1] + dp[0][1/1] = 1 + 1 = 2   {empty, {1}}
  j=2: dp[1][2] = dp[0][2] + dp[0][2/1] = 0 + 0 = 0

i=2, val=2:
  j=1: dp[2][1] = dp[1][1] + (1%2≠0) = 2
  j=2: dp[2][2] = dp[1][2] + dp[1][1] = 0 + 2 = 2   {{2}, {1,2}}

sum(dp[2][1..2]) = 2 + 2 = 4
answer = 4 - 1 = 3 ✅
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n × k) | O(n × k) | Fill (n+1) × k table |

---

## APPROACH 3 — OPTIMAL: 1D DP (Space-Optimised 0/1 Knapsack) ⭐

### Deep Intuition — Why Does This Work?

```
Think of dp[j] as: "how many ways can I form a subsequence with product exactly j?"

Starting state: dp[1] = 1 (empty subsequence → product 1)

For each element val, we have a binary choice for every existing subsequence:
  - SKIP val → dp[j] stays the same
  - TAKE val → a subsequence with product j/val becomes product j

So the update is:
  dp[j] += dp[j / val]   (only when j % val == 0)

The 0/1 knapsack constraint (each val used at most once):
  Iterate j from HIGH to LOW so dp[j/val] hasn't been updated yet this round.
```

### Why HIGH-TO-LOW (not LOW-TO-HIGH)?

```
Example: val = 2, dp = [0, 1, 0, 0, 0, ...]

Low-to-high (WRONG):
  j=2: dp[2] += dp[1] = 1  → dp[2] = 1  ← {2}
  j=4: dp[4] += dp[2] = 1  → dp[4] = 1  ← uses updated dp[2] = uses val TWICE! → {2,2} ❌

High-to-low (CORRECT):
  j=4: dp[4] += dp[2] = 0  → dp[4] = 0  (dp[2] not yet updated)
  j=2: dp[2] += dp[1] = 1  → dp[2] = 1  ← {2}  ✅  no double-use!
```

### Algorithm

```
dp = int[k],  dp[1] = 1

for each val in arr:
    for j from k-1 DOWN to val:
        if j % val == 0:
            dp[j] += dp[j / val]

answer = sum(dp[1..k-1]) - 1
```

### Dry Run — `arr = [1, 2, 3, 4]`, k = 10

```
Init: dp = [0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
            j:  0  1  2  3  4  5  6  7  8  9

val=1: j from 9 to 1, j%1==0 always, dp[j] += dp[j]
  only j=1 is nonzero: dp[1] += dp[1] = 1 → dp[1] = 2
  dp = [0, 2, 0, 0, 0, 0, 0, 0, 0, 0]
  (Represents: {empty} and {1} both have product 1)

val=2: j from 8,6,4,2 (multiples of 2 below 10)
  j=8: dp[8] += dp[4] = 0
  j=6: dp[6] += dp[3] = 0
  j=4: dp[4] += dp[2] = 0
  j=2: dp[2] += dp[1] = 2 → dp[2] = 2
  dp = [0, 2, 2, 0, 0, 0, 0, 0, 0, 0]
  (dp[2]=2: subsequences with product 2 → {2} and {1,2})

val=3: j from 9,6,3
  j=9: dp[9] += dp[3] = 0
  j=6: dp[6] += dp[2] = 2 → dp[6] = 2
  j=3: dp[3] += dp[1] = 2 → dp[3] = 2
  dp = [0, 2, 2, 2, 0, 0, 2, 0, 0, 0]
  (dp[3]=2: {3},{1,3};  dp[6]=2: {2,3},{1,2,3})

val=4: j from 8,4
  j=8: dp[8] += dp[2] = 2 → dp[8] = 2
  j=4: dp[4] += dp[1] = 2 → dp[4] = 2
  dp = [0, 2, 2, 2, 2, 0, 2, 0, 2, 0]
  (dp[4]=2: {4},{1,4};  dp[8]=2: {2,4},{1,2,4})

sum(dp[1..9]) = 2+2+2+2+0+2+0+2+0 = 12
answer = 12 - 1 = 11 ✅

Verified: {1},{2},{3},{4},{1,2},{1,3},{1,4},{2,3},{2,4},{1,2,3},{1,2,4} = 11 ✅
```

### Java Code (Interview-Quality)

```java
public int countSubsets(int[] arr, int k) {
    int[] dp = new int[k];
    dp[1] = 1;  // seed: empty subsequence with product 1

    for (int val : arr) {
        // HIGH-TO-LOW: 0/1 knapsack prevents using val more than once
        for (int j = k - 1; j >= val; j--) {
            if (j % val == 0) {
                // a subsequence with product j/val gains val → product becomes j
                dp[j] += dp[j / val];
            }
        }
    }

    int total = 0;
    for (int j = 1; j < k; j++) total += dp[j];
    return total - 1;  // subtract 1 for the empty subsequence
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n × k) | O(k) | n elements, O(k) work per element; 1D array of size k |

*(Compared to 2D DP: same time complexity but space drops from O(n × k) to O(k).)*

### How to Explain in an Interview

> *"This is a 0/1 Knapsack problem adapted for products instead of sums. I maintain a 1D dp array where dp[j] counts subsequences with product exactly j. I seed dp[1] = 1 for the empty starting state. For each element val, I iterate j from high to low — just like 0/1 sum knapsack — so val is used at most once per subsequence. At each j divisible by val, dp[j] gains dp[j/val] because: taking val in a subsequence that had product j/val gives product j. At the end, I sum dp[1..k-1] and subtract 1 for the empty subsequence."*

---

## ⚠️ Common Mistakes

| Mistake | Fix |
|---|---|
| Iterating j LOW to HIGH | Use HIGH-TO-LOW to prevent using val more than once |
| Not subtracting 1 at the end | Always subtract 1 for the empty subsequence seeded at dp[1] |
| Forgetting `j % val == 0` check | Only integer products are valid; skip non-divisible j |
| Starting with dp[0] = 1 instead of dp[1] = 1 | Empty product = 1, not 0; seed dp[1] = 1 |
| Integer overflow in brute force | Use `long` for the product parameter in recursion |

---

## 🔍 Edge Cases

| Input | k | Output | Why |
|---|---|---|---|
| `[1]` | 2 | 1 | {1} = product 1 < 2 |
| `[k]` | k | 0 | Single element equals k → not strictly less than |
| `[1, 1, 1]` | 2 | 7 | All 2³-1=7 non-empty subsets have product 1 < 2 |
| `[2, 3, 5]` | 31 | 7 | All 7 non-empty subsets: max product = 30 < 31 |

---

## 🧩 Pattern Recognition

**This problem IS:** 0/1 Knapsack — standard template adapted for product space.

**The standard 0/1 Knapsack template:**
```
For each item with weight w:
  For j from capacity DOWN to w:
    dp[j] = max(dp[j], dp[j-w] + value)   ← SUM knapsack

Product version (this problem):
  For each item val:
  For j from k-1 DOWN to val:
    if j % val == 0:
      dp[j] += dp[j / val]                 ← PRODUCT knapsack
```

The key analogy: `j - w` (subtract weight) → `j / val` (divide by element).

---

## 🎯 Interview Tips

1. **State the DP state clearly:** `dp[j]` = number of subsequences with product exactly j
2. **Motivate high-to-low iteration** with the double-use counterexample
3. **Explain the -1 at the end** — the empty subsequence is seeded but shouldn't be counted
4. **Draw the dp table** for `arr=[1,2,3,4], k=10` — the progression makes the algorithm clear

---

## 🔗 Related Problems

- LeetCode 416 — Partition Equal Subset Sum (0/1 knapsack on sums)
- LeetCode 494 — Target Sum (counting subsets with sum = target)
- LeetCode 474 — Ones and Zeroes (2D 0/1 knapsack)
- LeetCode 300 — Product constraints in DP arrays

---

## 📌 Revision Notes

- `dp[j]` = number of subsequences with product exactly j; init `dp[1] = 1`
- For each val: iterate j from `k-1` DOWN to `val`, update `dp[j] += dp[j/val]` if `j % val == 0`
- HIGH-TO-LOW = 0/1 knapsack = each element used at most once
- Final answer = `sum(dp[1..k-1]) - 1` (subtract 1 for empty subsequence)
- Same time O(nk) for both 2D and 1D DP; 1D wins on space: O(k) vs O(nk)

---

## 🏁 Key Takeaways

> This problem teaches you to **map product constraints to the knapsack framework**. The seed `dp[1] = 1` (representing the empty product = 1) and the high-to-low iteration are the two insights that make this work. Once you see "count subsets with product < k", the mental model is: product knapsack with value-space [1, k-1].

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
