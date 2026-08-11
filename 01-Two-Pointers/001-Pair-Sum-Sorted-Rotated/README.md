<img width="1024" height="1536" alt="Q1  Pair Sum in a Sorted and Rotated Array" src="https://github.com/user-attachments/assets/787e95aa-db76-476e-9498-182fd5dffdda" />

# Q1. Pair Sum in a Sorted and Rotated Array

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Two Pointers &nbsp;|&nbsp; **Companies:** Microsoft, Google, Apple

🔗 [GeeksForGeeks Link](https://www.geeksforgeeks.org/dsa/given-a-sorted-and-rotated-array-find-if-there-is-a-pair-with-a-given-sum/)

---

## What is a "Sorted and Rotated" Array?

A **sorted array** is simply arranged in order from smallest to largest:
```
[1, 3, 5, 7, 9]
```

A **rotated array** means we picked some point and shifted elements around:
```
Original: [1, 3, 5, 7, 9]
Rotate 2: [7, 9, 1, 3, 5]   ← took last 2 elements and moved them to front
```

Notice: the array is still mostly sorted — it just has one "break point" where it drops from big to small (from 9 → 1). That break point is called the **pivot**.

---

## 📝 Problem

Given an array of positive integers `arr[]` that is **sorted and then rotated**, check if there exists **any pair** whose sum equals a given `target`.

### Examples

| Input              | Target | Output  | Why                              |
|--------------------|--------|---------|----------------------------------|
| `[7, 9, 1, 3, 5]` | 6      | `true`  | 1 + 5 = 6 ✅                     |
| `[2, 3, 4, 1]`    | 3      | `true`  | 2 + 1 = 3 ✅                     |
| `[10, 7, 4, 1]`   | 9      | `false` | No two elements add up to 9      |

---

## 🧠 Approaches

---

### Method 1 — Brute Force

**Idea:** Check every possible pair `(i, j)` and see if their sum equals the target.

**Pseudo Code:**
```
for i from 0 to n-1:
    for j from i+1 to n-1:
        if arr[i] + arr[j] == target:
            return true
return false
```

**Dry Run** — `arr = [7, 9, 1, 3, 5]`, `target = 6`
```
i=0, j=1 → 7+9 = 16  ❌
i=0, j=2 → 7+1 = 8   ❌
i=0, j=3 → 7+3 = 10  ❌
i=0, j=4 → 7+5 = 12  ❌
i=1, j=2 → 9+1 = 10  ❌
i=1, j=3 → 9+3 = 12  ❌
i=1, j=4 → 9+5 = 14  ❌
i=2, j=3 → 1+3 = 4   ❌
i=2, j=4 → 1+5 = 6   ✅ return true
```

**Why it's slow:** We check every pair — for n=1000 elements that's ~500,000 checks!

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

---

### Method 2 — HashSet (Better)

**Idea:** For each number, we need its "complement" (`target - number`) to complete the pair.
Instead of scanning backward, we store previously seen numbers in a set and check instantly.

**Pseudo Code:**
```
create an empty HashSet called "seen"
for each number in arr:
    complement = target - number
    if complement is in "seen":
        return true          ← we found our pair!
    add number to "seen"
return false
```

**Dry Run** — `arr = [7, 9, 1, 3, 5]`, `target = 6`
```
seen = {}

num=7 → complement = 6-7 = -1 → not in seen → add 7 → seen={7}
num=9 → complement = 6-9 = -3 → not in seen → add 9 → seen={7,9}
num=1 → complement = 6-1 =  5 → not in seen → add 1 → seen={7,9,1}
num=3 → complement = 6-3 =  3 → not in seen → add 3 → seen={7,9,1,3}
num=5 → complement = 6-5 =  1 → 1 IS in seen ✅ → return true
                                    (pair: 5 + 1 = 6)
```

**Why it's faster:** Each lookup in a HashSet is O(1) — no inner loop needed.
**Downside:** We use extra memory to store the set.

| Time | Space |
|------|-------|
| O(n) | O(n)  |

---

### Method 3 — Two Pointer Circular ⭐ (Optimal)

**Idea:** The array was originally sorted, so even after rotation, the structure is preserved.
We just need to handle the "wrap-around" using **circular indexing** (modulo `% n`).

**Key Observations:**
- The **smallest** element is right after the "break point" (the pivot).
- The **largest** element is just before the pivot.
- We place `lo` at the smallest, `hi` at the largest, then use classic two-pointer logic.

**What is circular indexing?**
```
Array:  [7, 9, 1, 3, 5]
Index:   0  1  2  3  4

Normal:  right of index 4 = index 5 → doesn't exist!
Circular: (4 + 1) % 5 = 0  → wraps back to start ✅

Normal:  left of index 0 = index -1 → doesn't exist!
Circular: (0 - 1 + 5) % 5 = 4 → wraps back to end ✅
```

**Pseudo Code:**
```
Step 1: Find pivot (index of the minimum element)
    lo = 0
    for i from 1 to n-1:
        if arr[i] < arr[i-1]:
            lo = i
            break

Step 2: Set hi to the index just before lo (the maximum element)
    hi = (lo - 1 + n) % n

Step 3: Two-pointer scan
    while lo != hi:
        sum = arr[lo] + arr[hi]
        if sum == target  → return true
        if sum < target   → lo = (lo + 1) % n    (need bigger number)
        if sum > target   → hi = (hi - 1 + n) % n (need smaller number)

    return false
```

**Dry Run** — `arr = [7, 9, 1, 3, 5]`, `target = 6`

```
Step 1: Find pivot (minimum element)
  Compare arr[0] vs arr[1] → 7 vs 9 → no drop
  Compare arr[1] vs arr[2] → 9 vs 1 → DROP! → lo = 2 (value = 1)

Step 2: Set hi
  hi = (2 - 1 + 5) % 5 = 6 % 5 = 1  → hi = 1 (value = 9)

Array with pointers:
  Index:  0   1   2   3   4
  Value: [7] [9] [1] [3] [5]
              ↑           
             hi  lo
              1   2

Step 3: Two-pointer scan

  Iteration 1:
    lo=2 (val=1), hi=1 (val=9)
    sum = 1 + 9 = 10 > 6  → move hi left
    hi = (1 - 1 + 5) % 5 = 0

  Iteration 2:
    lo=2 (val=1), hi=0 (val=7)
    sum = 1 + 7 = 8 > 6   → move hi left
    hi = (0 - 1 + 5) % 5 = 4

  Iteration 3:
    lo=2 (val=1), hi=4 (val=5)
    sum = 1 + 5 = 6 == 6  ✅ return true!
```

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

### Method 4 — Sort + Two Pointer (Alternative)

**Idea:** Sort the array first (rotation no longer matters), then use standard two-pointer.

```
Step 1: Sort the array (copy it first to avoid modifying original)
Step 2: lo = 0, hi = n-1
Step 3: while lo < hi:
            sum = arr[lo] + arr[hi]
            if sum == target → return true
            if sum < target  → lo++
            if sum > target  → hi--
Step 4: return false
```

**Downside:** Sorting costs O(n log n) — slower than Method 3.

| Time       | Space |
|------------|-------|
| O(n log n) | O(1)  |

---

## 📊 Comparison Table

| Method                   | Time       | Space | When to use                          |
|--------------------------|------------|-------|--------------------------------------|
| Brute Force              | O(n²)      | O(1)  | Only for very small inputs            |
| HashSet                  | O(n)       | O(n)  | When rotation doesn't matter         |
| **Two Pointer Circular** | **O(n)**   | **O(1)** | ⭐ **Best — use this in interviews** |
| Sort + Two Pointer       | O(n log n) | O(1)  | Simpler code, but slower             |

---

## 💡 Key Insight

When an array is **sorted + rotated**, you can still use two pointers — just make the indices **circular** using `% n`. Finding the pivot tells you where the smallest element is, and you can simulate the original sorted order without actually un-rotating the array.

---

## ⚠️ Edge Cases to Know

| Case                          | What happens                                    |
|-------------------------------|-------------------------------------------------|
| Array not rotated (sorted)    | Pivot stays at index 0 — still works correctly  |
| All elements are the same     | No pair found (both pointers meet) — works      |
| n = 2                         | Directly checks `arr[0] + arr[1] == target`     |

---

## 🏷️ Method Signature (GFG)

```java
public boolean pairInSortedRotated(int[] arr, int target)
```

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
