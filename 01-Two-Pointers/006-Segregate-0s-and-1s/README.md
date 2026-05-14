# Q6. Segregate 0s and 1s in an Array

**Difficulty:** Easy &nbsp;|&nbsp; **Pattern:** Two Pointers &nbsp;|&nbsp; **Companies:** Microsoft, Amazon, Adobe

🔗 [GeeksForGeeks Link](https://www.geeksforgeeks.org/dsa/segregate-0s-and-1s-in-an-array-by-traversing-array-once/)

---

## What Does "Segregate" Mean Here?

**Segregate** = separate into groups.

We have an array that contains only two values — `0` and `1` — mixed up randomly.
Our job is to push all `0`s to the **left** and all `1`s to the **right**, in-place (without creating a new array).

```
Before: [0, 1, 0, 1, 1, 1]   ← mixed up
After:  [0, 0, 1, 1, 1, 1]   ← all 0s left, all 1s right
```

---

## 📝 Problem

Given an array `arr[]` containing only `0`s and `1`s, rearrange it **in-place** so that all `0`s appear before all `1`s.

### Examples

| Input                          | Output                      |
|--------------------------------|-----------------------------|
| `[0, 1, 0, 1, 0, 0, 1, 1, 1, 0]` | `[0, 0, 0, 0, 0, 1, 1, 1, 1, 1]` |
| `[0, 1, 0]`                   | `[0, 0, 1]`                 |
| `[1, 1]`                       | `[1, 1]`                    |
| `[0]`                          | `[0]`                       |

---

## 🧠 Approaches

---

### Method 1 — Brute Force (Bubble Sort style)

**Idea:** Compare every adjacent pair. If a `1` comes before a `0`, they are in the wrong order — swap them. Repeat until no more swaps are needed.

**Visual:**
```
[0, 1, 0, 1, 1, 1]
      ↑ ↑
   1 before 0 → swap them

[0, 0, 1, 1, 1, 1]  ✅ done after passes
```

**Pseudo Code:**
```
n = arr.length

for i from 0 to n-2:              ← outer pass (repeat n-1 times)
    for j from 0 to n-i-2:        ← inner: check adjacent pairs
        if arr[j] == 1 AND arr[j+1] == 0:
            swap arr[j] and arr[j+1]   ← 1 before 0 → swap!
```



**Dry Run** — `arr = [1, 0, 1, 0]`
```
Pass 1:
  j=0: arr[0]=1, arr[1]=0 → swap → [0, 1, 1, 0]
  j=1: arr[1]=1, arr[2]=1 → no swap
  j=2: arr[2]=1, arr[3]=0 → swap → [0, 1, 0, 1]

Pass 2:
  j=0: arr[0]=0, arr[1]=1 → no swap
  j=1: arr[1]=1, arr[2]=0 → swap → [0, 0, 1, 1]
  j=2: arr[2]=1, arr[3]=1 → no swap

Result: [0, 0, 1, 1] ✅
```

**Why it's slow:** For each element, we may need to bubble it across the whole array.

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

---

### Method 2 — Count 0s and Overwrite (Two Passes)

**Idea:**
- Pass 1: Count how many `0`s there are.
- Pass 2: Fill the first `count0` positions with `0`, and the rest with `1`.

**Pseudo Code:**
```
count0 = 0
for each x in arr:
    if x == 0: count0++

for i from 0 to count0-1:
    arr[i] = 0

for i from count0 to n-1:
    arr[i] = 1
```

**Dry Run** — `arr = [0, 1, 0, 1, 1, 1]`
```
Pass 1 (count):
  x=0 → count0=1
  x=1 → skip
  x=0 → count0=2
  x=1 → skip
  x=1 → skip
  x=1 → skip
  → count0 = 2

Pass 2 (overwrite):
  arr[0] = 0, arr[1] = 0    ← 2 zeros
  arr[2] = 1, arr[3] = 1, arr[4] = 1, arr[5] = 1  ← rest are 1s

Result: [0, 0, 1, 1, 1, 1] ✅
```

**Limitation:** This overwrites values. If `0` and `1` are IDs for large objects (not just numbers), this approach won't work — you'd lose the actual objects.

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

### Method 3 — Two Pointer Swap (One Pass)

**Idea:**
Place one pointer (`left`) at the start and one (`right`) at the end.
- `left` → moves right, looking for a misplaced `1` (a 1 that is too far left)
- `right` → moves left, looking for a misplaced `0` (a 0 that is too far right)
- When both find a misplaced element, **one swap** fixes both positions at once.
- Stop when the two pointers meet or cross.

> **Interview tip:** Start by explaining the Count-Overwrite approach (Method 2).
> Then say: *"But that overwrites values. If we use two pointers, we can rearrange elements in a single pass without losing any data."*

**Visual — `arr = [0, 1, 0, 1, 1, 1]`:**
```
 [0,  1,  0,  1,  1,  1]
  ↑                   ↑
 left               right

Step 1:
  left  → arr[0]=0, skip → arr[1]=1, STOP  (misplaced 1 found)
  right → arr[5]=1, skip → arr[4]=1, skip → arr[3]=1, skip
        → arr[2]=0, STOP  (misplaced 0 found)
  left(1) < right(2) → swap → arr = [0, 0, 1, 1, 1, 1]
  left=2, right=1

Step 2:
  left(2) > right(1) → STOP ✅

Result: [0, 0, 1, 1, 1, 1] ✅
```

**Pseudo Code:**
```
left  = 0
right = n - 1

while left < right:

    while left < right AND arr[left] == 0:   ← skip correct 0s
        left++

    while left < right AND arr[right] == 1:  ← skip correct 1s
        right--

    if left < right:                         ← both found a misplaced element
        swap arr[left] and arr[right]
        left++
        right--
```

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

### Method 4 — Hoare's Partition (Optimal) ⭐

**Idea:** Two pointers — `lo` from the left, `hi` from the right — "close in" toward each other.
- `lo` skips over correct elements (0s already on the left).
- `hi` skips over correct elements (1s already on the right).
- When both find a misplaced element, one swap fixes both positions.

This is the same idea as **partitioning in QuickSort**, applied to a 0/1 array.

**Why `lo = -1` and `hi = n`?**
We use `do-while` loops which move the pointer **before** checking. So we start one step outside the array to ensure we always check at least one element.

**Pseudo Code:**
```
lo = -1
hi = n

loop forever:
    do: lo++  while lo < n AND arr[lo] == 0   ← skip 0s from left
    do: hi--  while hi >= 0 AND arr[hi] == 1  ← skip 1s from right
    if lo >= hi: break                         ← pointers crossed, done
    swap arr[lo] and arr[hi]                   ← fix both misplaced elements
```

**Dry Run** — `arr = [0, 1, 0, 1, 1, 1]`, `n = 6`

```
Initial state:
  arr = [0, 1, 0, 1, 1, 1]
  lo = -1,  hi = 6

─────────────────────────────
Iteration 1:
  Move lo right (skip 0s):
    lo=-1 → lo=0, arr[0]=0 → keep moving
    lo=0  → lo=1, arr[1]=1 → STOP  (found a misplaced 1)

  Move hi left (skip 1s):
    hi=6  → hi=5, arr[5]=1 → keep moving
    hi=5  → hi=4, arr[4]=1 → keep moving
    hi=4  → hi=3, arr[3]=1 → keep moving
    hi=3  → hi=2, arr[2]=0 → STOP  (found a misplaced 0)

  lo=1 < hi=2 → swap arr[1] and arr[2]
  arr = [0, 0, 1, 1, 1, 1]

─────────────────────────────
Iteration 2:
  Move lo right (skip 0s):
    lo=1  → lo=2, arr[2]=1 → STOP

  Move hi left (skip 1s):
    hi=2  → hi=1, arr[1]=0 → STOP

  lo=2 >= hi=1 → BREAK ✅

Result: [0, 0, 1, 1, 1, 1] ✅
```

**Why it's optimal:** Each element is visited at most once. Swaps are minimal — we only swap when necessary.

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

## 📊 Comparison Table

| Method                    | Time   | Space | Passes | Notes                                        |
|---------------------------|--------|-------|--------|----------------------------------------------|
| Brute Force (Bubble)      | O(n²)  | O(1)  | n      | Too slow for large inputs                    |
| Count + Overwrite         | O(n)   | O(1)  | 2      | Overwrites values — fails for object arrays  |
| **Two Pointer Swap**      | **O(n)** | **O(1)** | **1** | Clean, rearranges actual elements         |
| **Hoare's Partition**     | **O(n)** | **O(1)** | **1** | ⭐ **Optimal — use this in interviews**   |

---

## 💡 Key Insights

1. **Only two values** → we don't need comparison-based sorting.
2. **Count-Overwrite** is simple but has a flaw: it destroys original values (not suitable for objects).
3. **Two Pointer Swap** is safe — it physically moves elements without overwriting, and does it in one pass.
4. **Hoare's Partition** uses `do-while` loops and initializes pointers outside the array bounds (`lo=-1`, `hi=n`) — this is a common pattern that confuses beginners, but the reason is simple: the loop moves first, then checks.
5. This problem is a **simpler version of Sort Colors (Problem 5)** with 2 values instead of 3.

---

## 🔗 Connection to Sort Colors (Problem 5)

| Feature         | Segregate 0s and 1s     | Sort Colors (0, 1, 2)       |
|-----------------|-------------------------|-----------------------------|
| Values          | 0 and 1 only            | 0, 1, and 2                 |
| Partition type  | 2-way                   | 3-way                       |
| Optimal algo    | Hoare's Partition       | Dutch National Flag         |
| Complexity      | O(n) time, O(1) space   | O(n) time, O(1) space       |

Think of this problem as a **warm-up** for Sort Colors.

---

## ⚠️ Edge Cases

| Case               | Expected Output | Why                              |
|--------------------|-----------------|----------------------------------|
| All `0`s: `[0,0,0]`| `[0, 0, 0]`     | No swap needed, pointers cross immediately |
| All `1`s: `[1,1,1]`| `[1, 1, 1]`     | Same — already segregated        |
| Single element `[0]` or `[1]` | unchanged | Loop exits instantly     |
| Already sorted `[0,0,1,1]` | `[0, 0, 1, 1]` | No swaps, correct answer |

---

## 🏷️ Method Signature (GFG)

```java
public void segregate0and1(int[] arr)
```

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
