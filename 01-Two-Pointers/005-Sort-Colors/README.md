<img width="1536" height="1024" alt="Q5  Sort Colors" src="https://github.com/user-attachments/assets/1d73c66a-7471-4825-b24b-f9435c3b495d" />



# Q5. Sort Colors

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Two Pointers (Dutch National Flag) &nbsp;|&nbsp; **Companies:** Microsoft, Amazon, Adobe, Bloomberg, Uber

🔗 [LeetCode 75](https://leetcode.com/problems/sort-colors/)

---

## 📝 Problem

Given an array `nums` with `n` objects colored **red**, **white**, or **blue**, sort them **in-place** so that objects of the same color are adjacent in the order: red → white → blue.

Use integers to represent colors:
- `0` → Red
- `1` → White
- `2` → Blue

> **You must NOT use the library sort function.**  
> **Follow-up:** Can you do it in a **single pass** with **O(1) space**?

### Examples

| Input           | Output          |
|-----------------|-----------------|
| `[2,0,2,1,1,0]` | `[0,0,1,1,2,2]` |
| `[2,0,1]`       | `[0,1,2]`       |
| `[0]`           | `[0]`           |

### Constraints
- `1 <= nums.length <= 300`
- `nums[i]` is `0`, `1`, or `2`

---

## 🗺️ Visual — What We're Doing

```
Input:   [2, 0, 2, 1, 1, 0]
          B  R  B  W  W  R        (B=Blue, R=Red, W=White)

Goal:    [0, 0, 1, 1, 2, 2]
          R  R  W  W  B  B

Visually:
Before:  [🔵, 🔴, 🔵, ⚪, ⚪, 🔴]
After:   [🔴, 🔴, ⚪, ⚪, 🔵, 🔵]
```

---

## 🧠 Theory — Building the Logic from Scratch

### Step 1 — The Real-World Analogy (Dutch National Flag 🇳🇱)

The problem is named after the **Dutch National Flag** — three horizontal stripes: Red, White, Blue.

```
┌─────────────────┐
│   RED   (0s)    │
├─────────────────┤
│   WHITE (1s)    │
├─────────────────┤
│   BLUE  (2s)    │
└─────────────────┘
```

We want to partition a mixed array into exactly these three sections **in one pass**.

---

### Step 2 — Why Simple Sorting Wastes Time

We only have **3 distinct values** (0, 1, 2). A general-purpose sort like `O(n log n)` is overkill — it doesn't use the fact that values are limited to {0, 1, 2}.

We can exploit this structure to achieve **O(n)** in a single pass.

---

### Step 3 — The Counting Idea (Two-Pass Insight)

**Observation:** If we just know **how many** 0s, 1s, and 2s exist, we can reconstruct the sorted array.

```
Input: [2, 0, 2, 1, 1, 0]
Count: 0→2,  1→2,  2→2

Write back: [0, 0, 1, 1, 2, 2]  ✅
```

But this is **two passes** (one to count, one to write). Can we do it in **one pass**?

---

### Step 4 — The Three-Region Invariant (Key Insight)

Divide the array into **four regions** at any point during processing:

```
 ←─ sorted ─→ ←─ sorted ─→ ←─ unknown ─→ ←─ sorted ─→
 [  0s zone  ][  1s zone  ][  ? ? ? ?  ][  2s zone  ]
  0        low-1  low    mid-1  mid   high  high+1   n-1
```

- `[0 .. low-1]`   → confirmed **0s** (red)
- `[low .. mid-1]` → confirmed **1s** (white)
- `[mid .. high]`  → **unknown**, yet to process
- `[high+1 .. n-1]`→ confirmed **2s** (blue)

We **shrink the unknown region** until it disappears (`mid > high`).

---

### Step 5 — The Three Cases (How Unknown Shrinks)

When we look at `nums[mid]`:

#### Case A: `nums[mid] == 0` (Red)
```
Before:  [..0s..][..1s..][0, ?, ?, ?][..2s..]
                  ↑low    ↑mid

Swap nums[low] ↔ nums[mid]:
After:   [..0s..0][..1s..][1, ?, ?, ?][..2s..]   ← 0 joins red zone
                    ↑low+1  ↑mid+1
```
Why advance both `low` and `mid`?  
Because `nums[low]` was a confirmed `1` (in the white zone) — after the swap it goes to `mid`'s old spot and is still a valid `1`. So `mid` can safely advance.

#### Case B: `nums[mid] == 1` (White)
```
Before:  [..0s..][..1s..][1, ?, ?, ?][..2s..]
                          ↑mid

nums[mid] is already in the correct zone (white).
Just advance mid:
After:   [..0s..][..1s..1][?, ?, ?][..2s..]
                            ↑mid+1
```

#### Case C: `nums[mid] == 2` (Blue)
```
Before:  [..0s..][..1s..][2, ?, ?, ?][..2s..]
                          ↑mid        ↑high

Swap nums[mid] ↔ nums[high]:
After:   [..0s..][..1s..][?, ?, ?, 2][..2s..]
                          ↑mid  ↑high-1
```
Why NOT advance `mid`?  
Because the value that came from `high` is **unknown** — it could be 0, 1, or 2. We must re-examine it at `mid` in the next iteration.

---

### Step 6 — Diagram: Full Pass on [2, 0, 2, 1, 1, 0]

```
Initial:
  [2,  0,  2,  1,  1,  0]
   ↑low=0
   ↑mid=0                  ↑high=5

Step 1: nums[mid]=2 → swap(mid=0, high=5)
  [0,  0,  2,  1,  1,  2]
   ↑low=0                   high=4
   ↑mid=0  (don't advance mid!)

Step 2: nums[mid]=0 → swap(low=0, mid=0) [no-op], low++, mid++
  [0,  0,  2,  1,  1,  2]
       ↑low=1
       ↑mid=1               high=4

Step 3: nums[mid]=0 → swap(low=1, mid=1) [no-op], low++, mid++
  [0,  0,  2,  1,  1,  2]
            ↑low=2
            ↑mid=2          high=4

Step 4: nums[mid]=2 → swap(mid=2, high=4)
  [0,  0,  1,  1,  2,  2]
            ↑low=2  high=3
            ↑mid=2  (don't advance mid!)

Step 5: nums[mid]=1 → mid++
  [0,  0,  1,  1,  2,  2]
            ↑low=2  high=3
                ↑mid=3

Step 6: nums[mid]=1 → mid++
  [0,  0,  1,  1,  2,  2]
            ↑low=2  high=3
                    ↑mid=4

mid(4) > high(3) → STOP ✅

Result: [0, 0, 1, 1, 2, 2]
```

---

### Step 7 — Why This is Called "Dutch National Flag"

Edsger Dijkstra proposed this problem in 1976 as a programming exercise.  
The three-color partition maps directly to the three stripes of the Dutch flag.  
It is the foundational algorithm behind the **3-way partition** used in **QuickSort** (to handle equal elements efficiently).

---

## 🧠 Approaches

### Method 1 — Brute Force (Bubble Sort)
**Logic:** Repeatedly compare adjacent elements and bubble larger values to the end. General-purpose, does not exploit the limited domain {0, 1, 2}.

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

---

### Method 2 — Counting Sort (Better)
**Logic:**
1. Count how many 0s, 1s, 2s are in the array.
2. Overwrite the array: fill `count0` zeros, then `count1` ones, then `count2` twos.

```
Pass 1: count  →  count0=2, count1=2, count2=2
Pass 2: write  →  [0,0,1,1,2,2]
```


| Time | Space |
|------|-------|
| O(n) | O(1)  |

**Drawback:** Two passes over the array.

---

### Method 3 — Dutch National Flag ⭐ (Optimal)
**Logic:** Single pass with three pointers `low`, `mid`, `high`.

```
Invariant at all times:
  [0..low-1]    = all 0s
  [low..mid-1]  = all 1s
  [mid..high]   = unknown
  [high+1..n-1] = all 2s

Rules:
  nums[mid] == 0 → swap(low, mid), low++, mid++
  nums[mid] == 1 → mid++
  nums[mid] == 2 → swap(mid, high), high--
```

| Time | Space |
|------|-------|
| O(n) | O(1)  |

Single pass, constant space, no extra counting arrays.

---

### Method 4 — Two-Pass Write Pointer (Alternative)
**Logic:**
- Pass 1: scan array, write all `0`s to front using a pointer.
- Pass 2: continue from where 0s ended, write all `1`s next.
- Remaining positions are automatically `2`s.

| Time | Space |
|------|-------|
| O(n) | O(1)  |

Two passes but very easy to understand and remember.

---

## 🔍 Dry Run — Method 3 (Dutch National Flag)

**Input:** `nums = [2, 0, 1]`

```
Initial: low=0, mid=0, high=2
Array:   [2, 0, 1]

Step 1: nums[mid=0]=2 → swap(0,2) → [1, 0, 2], high=1
        mid stays at 0

Step 2: nums[mid=0]=1 → mid++    → [1, 0, 2], mid=1

Step 3: nums[mid=1]=0 → swap(low=0, mid=1) → [0, 1, 2], low=1, mid=2

mid(2) > high(1) → STOP

Result: [0, 1, 2]  ✅
```

---

## 📊 Comparison

| Method                    | Time   | Space | Passes | Interview Value                       |
|---------------------------|--------|-------|--------|---------------------------------------|
| Brute (Bubble Sort)       | O(n²)  | O(1)  | n      | Baseline only                         |
| Counting Sort             | O(n)   | O(1)  | 2      | Clean stepping stone                  |
| **Dutch National Flag**   | **O(n)** | **O(1)** | **1** | ⭐ **Best answer — submit this**  |
| Two-Pass Write Pointer    | O(n)   | O(1)  | 2      | Easy to code, good backup             |

---

## 💡 Key Insights

1. **Limited domain = shortcut**: Only 3 values → don't need comparison-based sort.
2. **Three-region invariant**: Maintain `[0s | 1s | unknown | 2s]`; shrink unknown until empty.
3. **Why `mid` doesn't advance on swap with `high`**: The value coming from `high` is unverified — it must be re-checked.
4. **Why `mid` advances on swap with `low`**: `low` only ever holds a `1` (it's in the confirmed white zone), so after the swap the `1` is safe at `mid`'s old position.
5. **Real-world use**: This 3-way partition is the core optimization in **3-way QuickSort** (Dijkstra / Sedgewick) to efficiently handle many duplicate keys.

---

## ⚠️ Edge Cases

- All same color `[2,2,2]` → already "sorted", loop terminates with no swaps.
- Single element `[0]`, `[1]`, `[2]` → `mid > high` immediately, no-op.
- Already sorted `[0,0,1,1,2,2]` → loop runs but makes no swaps.
- Reverse sorted `[2,2,1,1,0,0]` → all cases triggered, finishes correctly.
- No `1`s `[2,0,2,0]` → `low == mid` throughout the middle zone shrinks to zero width.

---

## 🏷️ Method Signature (LeetCode)
```java
public void sortColors(int[] nums)
```

---

## 🔗 Related Problems
- [912. Sort an Array](https://leetcode.com/problems/sort-an-array/) — general sort, good for practicing merge/quick sort
- [283. Move Zeroes](https://leetcode.com/problems/move-zeroes/) — 2-region version of same idea
- [905. Sort Array By Parity](https://leetcode.com/problems/sort-array-by-parity/) — 2-color variant (even/odd)
- [Quicksort 3-way partition](https://en.wikipedia.org/wiki/Dutch_national_flag_problem) — direct application

---

## 🔁 Revision Tracker
- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
