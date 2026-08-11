<img width="1049" height="1024" alt="Q4  Trapping Rain Water part 1" src="https://github.com/user-attachments/assets/bbbab8db-2f5d-4571-88c5-f5da033e8b9b" />
<img width="1535" height="1024" alt="Q4  Trapping Rain Water part 2" src="https://github.com/user-attachments/assets/dcb3fd17-01c9-4c73-980c-c1a140bc82ed" />
<img width="1536" height="1024" alt="Q4  Trapping Rain Water part 3" src="https://github.com/user-attachments/assets/beb1fea5-ec18-47e9-88c6-b3a9fc800203" />




# Q4. Trapping Rain Water

**Difficulty:** Hard &nbsp;|&nbsp; **Pattern:** Two Pointers / Prefix-Suffix / Monotonic Stack &nbsp;|&nbsp; **Companies:** Amazon, Google, Microsoft, Adobe, Goldman Sachs,Sumsung

🔗 [LeetCode 42](https://leetcode.com/problems/trapping-rain-water/)

---

## 📝 Problem

Given `n` non-negative integers representing an **elevation map** where the width of each bar is `1`, compute how much water it can trap after raining.

### Examples

| Input                                   | Output | Explanation                                    |
|-----------------------------------------|--------|------------------------------------------------|
| `[0,1,0,2,1,0,1,3,2,1,2,1]`            | `6`    | 6 units trapped in the valleys                 |
| `[4,2,0,3,2,5]`                         | `9`    | 9 units trapped between the walls              |

### Constraints
- `1 <= height.length <= 2 × 10⁴`
- `0 <= height[i] <= 10⁵`

---

## 🗺️ Visual — Example 1

```
height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
index  =  0  1  2  3  4  5  6  7  8  9 10 11

 3 |                        █
 2 |         █  ~  ~  ~  █  █  █  ~  █
 1 |   █  ~  █  █  ~  █  █  █  █  █  █
 0 +------------------------------------------
    0  1  2  3  4  5  6  7  8  9 10 11

█ = wall/bar     ~ = trapped water

Water at each index:
  i=0: 0   i=1: 0   i=2: 1   i=3: 0
  i=4: 1   i=5: 2   i=6: 1   i=7: 0
  i=8: 0   i=9: 1   i=10:0   i=11:0
  Total = 0+0+1+0+1+2+1+0+0+1+0+0 = 6 ✅
```

---

## 🧠 Theory — Building the Logic from Scratch

### Step 1 — The Real-World Analogy

Imagine pouring water over a hilly terrain.  
Water **fills valleys** — it stays trapped wherever there are taller walls on BOTH sides.  
Water **runs off** edges — if there is no tall wall on one side, water escapes.

---

### Step 2 — Think About ONE Bar at a Time

Ask yourself: **"How much water sits on top of bar `i`?"**

```
      |
   ?  |  ← how high does water reach here?
      |
   ───┼───
      i
```

Water level above bar `i` is determined by:
- The **tallest wall to its LEFT**  (call it `leftMax`)
- The **tallest wall to its RIGHT** (call it `rightMax`)
- Water fills up to the **lower of the two ceilings** (because water spills over the shorter wall)

```
leftMax = 3       rightMax = 5
   |                   |
   |   ~~~water~~~     |
   |___________________| ← water level = min(3,5) = 3
         bar height = 1
         water trapped = 3 - 1 = 2
```

**Formula derived:**
```
water[i] = max(0,  min(leftMax[i], rightMax[i])  −  height[i])
                    ↑ the water ceiling             ↑ the bar itself
```

If `min(leftMax, rightMax) <= height[i]`, the bar is at or above the water level → 0 water.

---

### Step 3 — Scenario Diagrams

#### 🔹 Scenario A: Single Bar

```
 3 |  █
 2 |  █
 1 |  █
 0 +------
      i

leftMax  = height[i]
rightMax = height[i]
water[i] = min(h,h) - h = 0
```
> A lone bar traps **0 water** — no walls on either side to hold it.

---

#### 🔹 Scenario B: Strictly Decreasing [5, 4, 3, 2, 1]

```
 5 |  █
 4 |  █  █
 3 |  █  █  █
 2 |  █  █  █  █
 1 |  █  █  █  █  █
 0 +------------------
    0  1  2  3  4

rightMax at every i = height[i] itself (nothing taller to the right)
water[i] = min(leftMax, height[i]) - height[i] = 0 always
```
> Water **flows off the right edge**. No trapping possible.

---

#### 🔹 Scenario C: Strictly Increasing [1, 2, 3, 4, 5]

```
 5 |              █
 4 |           █  █
 3 |        █  █  █
 2 |     █  █  █  █
 1 |  █  █  █  █  █
 0 +------------------
    0  1  2  3  4

leftMax at every i = height[i] itself (nothing taller to the left)
water[i] = min(height[i], rightMax) - height[i] = 0 always
```
> Water **flows off the left edge**. No trapping possible.

---

#### 🔹 Scenario D: Two Bars with a Gap [3, 0, 0, 0, 4]

```
 4 |  █              █
 3 |  █              █
 2 |  █              █
 1 |  █              █
 0 +--------------------
    0  1  2  3  4

For i=1: leftMax=3, rightMax=4 → water = min(3,4)-0 = 3
For i=2: leftMax=3, rightMax=4 → water = min(3,4)-0 = 3
For i=3: leftMax=3, rightMax=4 → water = min(3,4)-0 = 3
Total = 9 units
```

```
 4 |  █  ~  ~  ~  █
 3 |  █  ~  ~  ~  █
 2 |  █  ~  ~  ~  █
 1 |  █  ~  ~  ~  █
 0 +--------------------
       ↑ water fills to height 3 (the shorter wall)
```
> Water ceiling = **min of the two wall heights** = 3. The taller right wall (4) does NOT help beyond 3 — water spills over the left wall at 3.

---

#### 🔹 Scenario E: Classic Valley / Pocket [3, 1, 3]

```
 3 |  █     █
 2 |  █     █
 1 |  █  █  █
 0 +----------
    0  1  2

i=1: leftMax=3, rightMax=3 → water = min(3,3) - 1 = 2
Total = 2 units
```

```
 3 |  █  ~  █     ← water fills to level 3
 2 |  █  ~  █
 1 |  █  █  █
 0 +----------
```
> Perfect symmetric valley. Water fills to the top of both equal walls.

---

#### 🔹 Scenario F: Asymmetric Valley [5, 1, 2]

```
 5 |  █
 4 |  █
 3 |  █
 2 |  █     █
 1 |  █  █  █
 0 +----------
    0  1  2

i=1: leftMax=5, rightMax=2 → water = min(5,2) - 1 = 1
i=2: leftMax=5, rightMax=2 → water = min(5,2) - 2 = 0
Total = 1 unit
```
> The **shorter wall (rightMax=2)** is the bottleneck. Even though left wall is 5, water only fills to 2.

---

#### 🔹 Scenario G: Multiple Pockets [0,1,0,2,1,0,1,3,2,1,2,1]

```
 3 |                        █
 2 |         █           █  █     █
 1 |   █     █  █     █  █  █  █  █
 0 +-------------------------------------------
    0  1  2  3  4  5  6  7  8  9 10 11

Pocket 1:  between i=1 and i=3  →  water at i=2 = 1
Pocket 2:  between i=3 and i=7  →  water at i=4,5,6 = 1+2+1 = 4
Pocket 3:  between i=7 and i=10 →  water at i=9 = 1
Total = 6
```

---

### Step 4 — The Key Observation (Why min of leftMax and rightMax?)

```
           leftMax = 4          rightMax = 6
               |                      |
          4    |   water level = 4    |  6
               |______________________|
                  bar height = 1
                  water = 4 - 1 = 3
```

If leftMax < rightMax:
- Water level = leftMax (it would spill over the left wall if higher)
- We don't even need to know the exact rightMax — just that it's >= leftMax

If rightMax < leftMax:
- Water level = rightMax (it would spill over the right wall if higher)
- We don't even need to know the exact leftMax — just that it's >= rightMax

**This observation powers the O(1) space Two Pointer solution!**

---

### Step 5 — The Complete Formula

```
For each bar i from 0 to n-1:

  leftMax[i]  = max(height[0], height[1], ..., height[i])
  rightMax[i] = max(height[i], height[i+1], ..., height[n-1])

  waterLevel[i] = min(leftMax[i], rightMax[i])
  trapped[i]    = waterLevel[i] - height[i]   (always >= 0)

  answer = sum of all trapped[i]
```

---

## 🧠 Approaches

### Method 1 — Brute Force
**Logic:** For each bar `i`, scan left to find `leftMax` and scan right to find `rightMax`. Apply the formula.

```
for each i:
    leftMax  = max(height[0..i])     ← O(n) scan
    rightMax = max(height[i..n-1])   ← O(n) scan
    water[i] = min(leftMax, rightMax) - height[i]
```

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

**Drawback:** Redundant scanning — we recompute leftMax and rightMax for every bar from scratch.

---

### Method 2 — Prefix / Suffix Arrays (Better)
**Logic:** Precompute `leftMax[]` in one left-to-right pass, `rightMax[]` in one right-to-left pass, then calculate water in a third pass.

```
leftMax[0]   = height[0]
leftMax[i]   = max(leftMax[i-1], height[i])    ← left sweep

rightMax[n-1]= height[n-1]
rightMax[i]  = max(rightMax[i+1], height[i])   ← right sweep

water[i] = min(leftMax[i], rightMax[i]) - height[i]
```

| Time | Space |
|------|-------|
| O(n) | O(n)  |

**Improvement:** Three O(n) passes instead of O(n²). Uses two extra arrays.

---

### Method 3 — Two Pointer ⭐ (Optimal)
**Logic:** Eliminate the two extra arrays using the key observation:

> If `height[left] <= height[right]`, we know `rightMax >= height[right] >= height[left]`,  
> so the water at `left` is entirely governed by `maxLeft` — we don't need `rightMax`.  
> Process `left` and advance it. Mirror logic for the right side.

```
left=0, right=n-1, maxLeft=0, maxRight=0, water=0

while left < right:
    if height[left] <= height[right]:
        if height[left] >= maxLeft → maxLeft = height[left]   (wall, no water)
        else                       → water += maxLeft - height[left]
        left++
    else:
        if height[right] >= maxRight → maxRight = height[right]  (wall, no water)
        else                         → water += maxRight - height[right]
        right--
```

| Time | Space |
|------|-------|
| O(n) | O(1)  |

---

### Method 4 — Monotonic Stack
**Logic:** Think **horizontally** — find water in each "layer" between valleys.

Stack holds bar indices in **decreasing height** order.  
When we find a bar taller than the stack top, we found the right wall of a pocket.  
Pop the valley floor, use the new stack top as the left wall.

```
for each bar `right`:
    while stack not empty AND height[right] > height[stack.top]:
        bottom = stack.pop()             ← valley floor
        if stack empty → break           ← no left wall
        left = stack.top
        width       = right - left - 1
        waterHeight = min(height[left], height[right]) - height[bottom]
        water      += width × waterHeight
    push right
```

| Time | Space |
|------|-------|
| O(n) | O(n)  |

Great for understanding the "horizontal layer" view of water trapping.

---

## 🔍 Dry Run — Method 3 (Two Pointer)

**Input:** `height = [4, 2, 0, 3, 2, 5]`

```
Initial: left=0, right=5, maxLeft=0, maxRight=0, water=0

Step 1: h[0]=4, h[5]=5 → h[left] <= h[right]
        h[0]=4 >= maxLeft=0 → maxLeft=4  (wall, no water)
        left → 1

Step 2: h[1]=2, h[5]=5 → h[left] <= h[right]
        h[1]=2 < maxLeft=4 → water += 4-2 = 2   (total=2)
        left → 2

Step 3: h[2]=0, h[5]=5 → h[left] <= h[right]
        h[2]=0 < maxLeft=4 → water += 4-0 = 4   (total=6)
        left → 3

Step 4: h[3]=3, h[5]=5 → h[left] <= h[right]
        h[3]=3 < maxLeft=4 → water += 4-3 = 1   (total=7)
        left → 4

Step 5: h[4]=2, h[5]=5 → h[left] <= h[right]
        h[4]=2 < maxLeft=4 → water += 4-2 = 2   (total=9)
        left → 5

Step 6: left=5 == right=5 → STOP

Answer: 9 ✅
```

---

## 🔍 Dry Run — Method 4 (Monotonic Stack)

**Input:** `height = [4, 2, 0, 3, 2, 5]`

```
stack=[], water=0

right=0: h=4  → stack empty, push 0         stack=[0]
right=1: h=2  → h[1]=2 < h[0]=4, push 1     stack=[0,1]
right=2: h=0  → h[2]=0 < h[1]=2, push 2     stack=[0,1,2]
right=3: h=3  → h[3]=3 > h[2]=0:
    pop 2 (bottom, h=0)
    left=1 (h=2), right=3 (h=3)
    width=3-1-1=1, wH=min(2,3)-0=2 → water+=2   (total=2)
    h[3]=3 > h[1]=2:
    pop 1 (bottom, h=2)
    left=0 (h=4), right=3 (h=3)
    width=3-0-1=2, wH=min(4,3)-2=1 → water+=2   (total=4)
    h[3]=3 < h[0]=4, stop. push 3             stack=[0,3]

right=4: h=2  → h[4]=2 < h[3]=3, push 4     stack=[0,3,4]

right=5: h=5  → h[5]=5 > h[4]=2:
    pop 4 (bottom, h=2)
    left=3 (h=3), right=5 (h=5)
    width=5-3-1=1, wH=min(3,5)-2=1 → water+=1   (total=5)
    h[5]=5 > h[3]=3:
    pop 3 (bottom, h=3)
    left=0 (h=4), right=5 (h=5)
    width=5-0-1=4, wH=min(4,5)-3=1 → water+=4   (total=9)
    h[5]=5 > h[0]=4:
    pop 0 (bottom, h=4)
    stack empty → break
    push 5                                     stack=[5]

Answer: 9 ✅
```

---

## 📊 Comparison

| Method               | Time   | Space | Interview Value                          |
|----------------------|--------|-------|------------------------------------------|
| Brute Force          | O(n²)  | O(1)  | Show understanding of the formula        |
| Prefix/Suffix Arrays | O(n)   | O(n)  | Clear stepping stone to optimal          |
| **Two Pointer**      | **O(n)** | **O(1)** | ⭐ **Best answer — submit this**     |
| Monotonic Stack      | O(n)   | O(n)  | Powerful pattern, shows depth of CS skill|

---

## 💡 Key Insights

1. **Per-bar formula:** `water[i] = min(leftMax[i], rightMax[i]) - height[i]` — derive this first before any optimization.
2. **Minimum of maxima:** The water level at any bar is the LOWER ceiling, because water spills over the shorter wall.
3. **Two pointer safety:** If `height[left] <= height[right]`, the right side is guaranteed to be >= `height[left]`, so the left bar's water is fully determined by `maxLeft` alone — we can safely process and advance `left`.
4. **No water at edges:** `height[0]` and `height[n-1]` never trap water (no wall on the outer side), but the formula handles this naturally.
5. **Stack = horizontal thinking:** Where two pointers and prefix arrays work bar-by-bar (vertically), the stack processes horizontal water layers between valleys.

---

## ⚠️ Edge Cases

- `n == 1` → single bar, no trapping → `0`.
- All same height `[3,3,3]` → no valley → `0`.
- All zeros `[0,0,0]` → no walls → `0`.
- One tall bar in the middle `[0,0,5,0,0]` → no enclosing walls → `0`.
- Monotone increasing or decreasing → `0` (water runs off one edge).
- Two equal tall bars with zeros in between `[3,0,0,3]` → fills completely.

---

## 🏷️ Method Signature (LeetCode)
```java
public int trap(int[] height)
```

---

## 🔗 Related Problems
- [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/) — find the best two-wall container (simpler cousin)
- [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) — monotonic stack, inverted problem
- [407. Trapping Rain Water II](https://leetcode.com/problems/trapping-rain-water-ii/) — 3D version using min-heap BFS

---

## 🔁 Revision Tracker
- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
