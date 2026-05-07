# Q3. Container With Most Water

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Two Pointers &nbsp;|&nbsp; **Companies:** Flipkart, Dunzo, Amazon, Google, Adobe

🔗 [LeetCode 11](https://leetcode.com/problems/container-with-most-water/)

---

## 📝 Problem

You are given an integer array `height` of length `n`.  
There are `n` vertical lines drawn such that the two endpoints of the `i-th` line are `(i, 0)` and `(i, height[i])`.

Find **two lines** that together with the x-axis form a container, such that the container **contains the most water**.

Return the **maximum amount of water** a container can store.

> You may **not** slant the container.

---

### Visual Understanding

```
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
index  =  0  1  2  3  4  5  6  7  8

 8 |  █           █
 7 |  █           █        █
 6 |  █  █        █        █
 5 |  █  █     █  █        █
 4 |  █  █     █  █  █     █
 3 |  █  █     █  █  █  █  █
 2 |  █  █  █  █  █  █  █  █
 1 |█ █  █  █  █  █  █  █  █
   +---------------------------
    0  1  2  3  4  5  6  7  8

Best container: left wall at index 1 (height=8), right wall at index 8 (height=7)
Width  = 8 - 1 = 7
Height = min(8, 7) = 7
Area   = 7 × 7 = 49  ✅
```

---

### Examples

| Input                              | Output | Reason                                                          |
|------------------------------------|--------|-----------------------------------------------------------------|
| `[1, 8, 6, 2, 5, 4, 8, 3, 7]`     | `49`   | Lines at index 1 and 8: min(8,7) × (8−1) = 7×7 = 49           |
| `[1, 1]`                           | `1`    | Only two lines: min(1,1) × (1−0) = 1×1 = 1                     |
| `[4, 3, 2, 1, 4]`                  | `16`   | Lines at index 0 and 4: min(4,4) × (4−0) = 4×4 = 16            |
| `[1, 2, 1]`                        | `2`    | Lines at index 0 and 1: min(1,2) × (1−0) = 1×1 = 1... or 0&2 = 2 |

### Constraints
- `2 <= height.length <= 10⁵`
- `0 <= height[i] <= 10⁴`

---

## 🧠 Theory — How to Think About This Problem

### The Core Formula
Water stored between two walls at indices `i` and `j` (where `i < j`):
```
area = min(height[i], height[j])  ×  (j - i)
         ↑ shorter wall limits depth    ↑ width of container
```

The two factors **pull against each other**:
- Moving pointers **inward** → width **decreases**
- We hope to find **taller walls** → depth might **increase**

### Why Two Pointers Work (The Key Insight)

Start with the **widest possible container**: `left = 0`, `right = n-1`.

Now ask: **which pointer should we move inward?**

Suppose `height[left] < height[right]`. What happens if we move `right` inward?
- Width **shrinks** (bad)
- The new height is still limited by `height[left]` (the shorter wall) — so depth **cannot improve**
- → Area can only **decrease or stay same** → moving `right` is **pointless**

So we must move `left` inward — the shorter wall — because:
- Width shrinks (unavoidable)
- But we **might** find a taller wall on the left, giving a chance to increase area

This greedy choice is **safe**: by always discarding the shorter wall's position, we never miss the optimal answer.

### Mathematical Proof (Informal)
Suppose the optimal pair is `(i*, j*)`. The two-pointer approach will eventually consider this pair because:
- It starts from outside `(i*, j*)` 
- It only discards a position when it's **provably suboptimal** (shorter wall can't do better)
- So `(i*, j*)` is never discarded before being evaluated

---

## 🧠 Approaches

### Method 1 — Brute Force
**Logic:** Try every possible pair `(i, j)`. Compute area for each and track the maximum.

```
for i from 0 to n-2:
    for j from i+1 to n-1:
        area = min(height[i], height[j]) × (j - i)
        maxArea = max(maxArea, area)
```

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

**Drawback:** Too slow for `n = 10^5` (10^10 operations).

---

### Method 2 — Two Pointer ⭐ (Optimal)
**Logic:**
1. Place `left = 0` and `right = n-1` (start with widest container).
2. Compute area at current pair.
3. Move the **shorter wall** inward — it's the only side that could possibly yield a better area.
4. Repeat until `left >= right`.

```
left = 0, right = n-1, maxWater = 0
while left < right:
    water = min(height[left], height[right]) × (right - left)
    maxWater = max(maxWater, water)
    if height[left] <= height[right]:
        left++
    else:
        right--
return maxWater
```

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

### Method 3 — Two Pointer with Skip Optimisation
**Logic:** Same as Method 2, but skip walls that are shorter than or equal to the current tallest wall we've seen on that side. Such walls can never improve the area because the height factor won't increase, and width is already shrinking.

| Time | Space |
|------|-------|
| O(n) | O(1)  |

Practically faster due to fewer area computations, but same Big-O.

---

## 🔍 Dry Run — Method 2 (Optimal)

**Input:** `height = [1, 8, 6, 2, 5, 4, 8, 3, 7]`

```
Indices:  0  1  2  3  4  5  6  7  8
Heights:  1  8  6  2  5  4  8  3  7

Step 1: L=0(h=1), R=8(h=7)  → water = min(1,7)×8 = 1×8 = 8    maxWater=8
        h[L]=1 < h[R]=7  → move L right
        
Step 2: L=1(h=8), R=8(h=7)  → water = min(8,7)×7 = 7×7 = 49   maxWater=49
        h[L]=8 > h[R]=7  → move R left
        
Step 3: L=1(h=8), R=7(h=3)  → water = min(8,3)×6 = 3×6 = 18   maxWater=49
        h[L]=8 > h[R]=3  → move R left
        
Step 4: L=1(h=8), R=6(h=8)  → water = min(8,8)×5 = 8×5 = 40   maxWater=49
        h[L]=8 == h[R]=8 → move L right  (either is fine when equal)
        
Step 5: L=2(h=6), R=6(h=8)  → water = min(6,8)×4 = 6×4 = 24   maxWater=49
        h[L]=6 < h[R]=8  → move L right
        
Step 6: L=3(h=2), R=6(h=8)  → water = min(2,8)×3 = 2×3 = 6    maxWater=49
        h[L]=2 < h[R]=8  → move L right

Step 7: L=4(h=5), R=6(h=8)  → water = min(5,8)×2 = 5×2 = 10   maxWater=49
        h[L]=5 < h[R]=8  → move L right

Step 8: L=5(h=4), R=6(h=8)  → water = min(4,8)×1 = 4×1 = 4    maxWater=49
        h[L]=4 < h[R]=8  → move L right

Step 9: L=6 == R=6  → STOP

Answer: 49 ✅
```

---

## 📊 Comparison

| Method                    | Time   | Space | Interview Value                       |
|---------------------------|--------|-------|---------------------------------------|
| Brute Force               | O(n²)  | O(1)  | Shows you understand the problem      |
| **Two Pointer**           | **O(n)** | **O(1)** | ⭐ **Best answer — submit this**   |
| Two Pointer + Skip Opt    | O(n)   | O(1)  | Bonus mention for practical speedup   |

---

## 💡 Key Insights

1. **Area = min(height) × width** — the shorter wall always limits depth; the only lever left is width.
2. **Always move the shorter wall** — moving the taller wall inward can only make things worse (width shrinks, height governed by shorter wall stays the same).
3. **Start widest** — beginning at opposite ends guarantees the maximum possible width as the starting point.
4. **Greedy is safe here** — every discarded position is provably suboptimal, so we never skip the true best pair.
5. **No sorting needed** — unlike 3Sum, the positions matter here (they determine width), so we cannot sort.

---

## ⚠️ Edge Cases

- `n == 2` → only one pair, return `min(height[0], height[1]) * 1`.
- All zeros `[0, 0, 0]` → area is always 0.
- Strictly increasing `[1, 2, 3, 4, 5]` → optimal is first and last line.
- Strictly decreasing `[5, 4, 3, 2, 1]` → optimal is first and last line.
- One very tall wall surrounded by short walls → tall wall never becomes the bottleneck.

---

## 🏷️ Method Signature (LeetCode)
```java
public int maxArea(int[] height)
```

---

## 🔗 Related Problems
- [42. Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) — water trapped at every bar (harder variant)
- [1. Two Sum](https://leetcode.com/problems/two-sum/) — same two-pointer spirit on sorted arrays
- [15. 3Sum](https://leetcode.com/problems/3sum/) — reduce to two-pointer after fixing one element

---

## 🔁 Revision Tracker
- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
