# Q1. Pair Sum in a Sorted and Rotated Array

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Two Pointers &nbsp;|&nbsp; **Companies:** Microsoft, Google, Apple

🔗 [GeeksForGeeks Link](https://www.geeksforgeeks.org/dsa/given-a-sorted-and-rotated-array-find-if-there-is-a-pair-with-a-given-sum/)

---

## 📝 Problem
Given an array of positive elements `arr[]` that is **sorted and then rotated** around an unknown point, check if there exists a pair with sum equal to a given `target`.

### Examples
| Input                              | Target | Output | Reason                          |
|------------------------------------|--------|--------|---------------------------------|
| `[7, 9, 1, 3, 5]`                  | 6      | true   | `arr[2] + arr[4] = 1 + 5 = 6`   |
| `[2, 3, 4, 1]`                     | 3      | true   | `arr[0] + arr[3] = 2 + 1 = 3`   |
| `[10, 7, 4, 1]`                    | 9      | false  | No pair sums to 9               |

---

## 🧠 Approaches

### Method 1 — Brute Force
**Logic:** Try every pair `(i, j)`. Simple but slow.

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

### Method 2 — HashSet (Better)
**Logic:** For each number, check if `target - num` was already seen.

| Time | Space |
|------|-------|
| O(n) | O(n)  |

### Method 3 — Two Pointer Circular ⭐ (Optimal)
**Logic:** The array was originally sorted, so:
1. Find the **pivot** (index of minimum element) → that's the smallest, set `lo` here.
2. Set `hi = (lo - 1 + n) % n` → that's the largest.
3. Move pointers using **circular indices** (`% n`):
   - If `sum < target` → `lo = (lo + 1) % n`
   - If `sum > target` → `hi = (hi - 1 + n) % n`
   - If `sum == target` → found!

| Time | Space |
|------|-------|
| O(n) | O(1)  |

### Method 4 — Sort + Two Pointer (Alternative)
**Logic:** Sort the array (rotation no longer matters), then standard two-pointer.

| Time       | Space |
|------------|-------|
| O(n log n) | O(1)  |

---

## 🔍 Dry Run — Method 3 (Optimal)

**Input:** `arr = [7, 9, 1, 3, 5]`, `target = 6`

```
Step 1: Find pivot (min element)
  arr[0]=7, arr[1]=9, arr[2]=1 ← arr[2] < arr[1], pivot found
  lo = 2 (value 1)
  hi = (2 - 1 + 5) % 5 = 1 (value 9)

Step 2: Two-pointer circular scan
  Iter 1: lo=2, hi=1 → sum = 1+9 = 10 > 6 → hi = (1-1+5)%5 = 0
  Iter 2: lo=2, hi=0 → sum = 1+7 = 8  > 6 → hi = (0-1+5)%5 = 4
  Iter 3: lo=2, hi=4 → sum = 1+5 = 6  ✅ return true
```

---

## 📊 Comparison

| Method        | Time       | Space | Interview Value          |
|---------------|------------|-------|--------------------------|
| Brute Force   | O(n²)      | O(1)  | Baseline only            |
| HashSet       | O(n)       | O(n)  | Good if rotation ignored |
| **Two Pointer Circular** | **O(n)** | **O(1)** | ⭐ **Best answer**     |
| Sort + TP     | O(n log n) | O(1)  | Simple but slower        |

---

## 💡 Key Insight
When an array is **sorted + rotated**, you can still use two pointers — just make the indices **circular** with modulo. Knowing the pivot lets you simulate the original sorted order without actually rotating back.

---

## ⚠️ Edge Cases
- Array not actually rotated (already sorted) → pivot stays at 0, still works
- All elements same → pivot at 0, two pointers converge correctly
- `n == 2` → direct check `arr[0] + arr[1] == target`

---

## 🏷️ Method Signature (GFG)
```java
public boolean pairInSortedRotated(int[] arr, int target)
```
> ⚠️ GFG expects `pairInSortedRotated`, not `hasPairWithSum`.

---

## 🔁 Revision Tracker
- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
