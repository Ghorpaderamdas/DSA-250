# 👆👆 Two Pointers Pattern — Beginner's Guide

---

## 🤔 What Problem Does It Solve?

Imagine you have a **sorted array** and need to find a pair of elements that adds up to a target.

The naive approach: check every possible pair with two nested loops → **O(n²)** — slow for large inputs.

**Two Pointers idea:** instead of checking every pair from scratch, place one pointer at the start and one at the end, then:
- ➡️ **Move left pointer right** when the current sum is too small
- ⬅️ **Move right pointer left** when the current sum is too large

Result: **O(n)** — one single pass through the array. That's the magic.

---

## ✅ When Should You Use Two Pointers?

| ✅ USE it when... | ❌ Do NOT use it when... |
|---|---|
| Input is a **sorted array or string** | Array is **unsorted** and cannot be sorted |
| You are looking for **pairs or triplets** with a target | You need **all subarrays** (use Sliding Window) |
| Goal is **sum, difference, palindrome check, area** | Elements are in a **non-sequential structure** (tree, graph) |
| You need to **remove duplicates or partition** an array | Order of elements must be preserved AND array is unsorted |

---

## 📦 Two Types of Two Pointers

```
                        Two Pointers
                               |
           ----------------------------------------
           |                                      |
    Opposite Ends                        Same Direction
    (left starts at 0,                   (both pointers start at 0
     right starts at n-1)                 and move forward together)

    Examples:                            Examples:
    • Pair sum in sorted array           • Remove duplicates from sorted array
    • Container with most water          • Move all zeroes to end
    • Trapping rain water                • Segregate 0s and 1s
    • Check if palindrome                • Slow + Fast pointer (cycle detection)
```

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/33e8e6b0-712f-45ba-b020-5fafbc26e893" />


---

## 🧩 4-Step Framework (works for ANY two pointers problem)

```
Step 1 → Identify the Pattern
         Sorted array? Looking for pairs/triplets? Partition problem?
         If yes → Two Pointers ✅

Step 2 → Opposite Ends or Same Direction?
         Opposite Ends : searching for a pair, area, or palindrome
         Same Direction: removing/moving elements, fast+slow pointer

Step 3 → Set Up the Starting Positions
         Opposite Ends: left = 0, right = n - 1
         Same Direction: slow = 0, fast = 0 (or fast = 1)

Step 4 → Define the Move Condition
         Opposite Ends: sum < target → move left right (left++)
                        sum > target → move right left (right--)
                        sum == target → found the answer!
         Same Direction: condition met → move slow pointer
                         always move fast pointer
```

---

## 🔍 Worked Example: Pair Sum in Sorted Array

**Problem:** Given sorted `arr = [1, 2, 3, 4, 6]` and `target = 6`, find if any two elements add up to 6.

**All pairs (brute force check):**
```
(1+2)=3   (1+3)=4   (1+4)=5   (1+6)=7
(2+3)=5   (2+4)=6 ✅ ← found it, but brute force checked 6 pairs already
```

### Applying the 4-Step Framework

**Step 1 — Pattern?**
Sorted array, looking for a pair with target sum → Two Pointers ✅

**Step 2 — Opposite Ends or Same Direction?**
Searching for a pair → **Opposite Ends**

**Step 3 — Starting Positions?**
```
left = 0   → points to arr[0] = 1
right = 4  → points to arr[4] = 6
```

**Step 4 — Move Condition?**
```
sum < target → left++    (need a bigger number, move left pointer right)
sum > target → right--   (need a smaller number, move right pointer left)
sum == target → return true!
```

### Visual — How the Pointers Move

```
Index:    0      1      2      3      4
Array: [  1  ] [  2  ] [  3  ] [  4  ] [  6  ]
Target = 6

Step 1:  ↑left                         ↑right
         arr[0] + arr[4] = 1 + 6 = 7   → 7 > 6, move right LEFT

Step 2:  ↑left                  ↑right
         arr[0] + arr[3] = 1 + 4 = 5   → 5 < 6, move left RIGHT

Step 3:         ↑left           ↑right
         arr[1] + arr[3] = 2 + 4 = 6   → 6 == 6, FOUND! ✅
```

### Pseudocode

```
function hasPairWithSum(arr, target):
    left = 0
    right = length of arr - 1

    while left < right:
        sum = arr[left] + arr[right]

        if sum == target:
            return true             // pair found!
        else if sum < target:
            left++                  // need bigger number → move left right
        else:
            right--                 // need smaller number → move right left

    return false                    // no pair found
```

### Dry Run (arr = [1, 2, 3, 4, 6], target = 6)

| Step | left | right | arr[left] | arr[right] | sum | Action |
|------|------|-------|-----------|------------|-----|--------|
| 1    | 0    | 4     | 1         | 6          | 7   | sum > target → right-- |
| 2    | 0    | 3     | 1         | 4          | 5   | sum < target → left++ |
| 3    | 1    | 3     | 2         | 4          | **6** | sum == target → **return true ✅** |

**Answer: true** (pair is 2 and 4) ✅

---

## 💡 Key Insights

### 1. Why is this faster than nested loops?

| Approach | How it works | Time Complexity |
|---|---|---|
| Naive (two loops) | Check every possible pair (i, j) for all i < j | O(n²) |
| Two Pointers | Each pointer moves at most n steps total | **O(n)** |

### 2. Opposite Ends vs Same Direction

- **Opposite Ends (this problem):** searching for pairs, palindrome check, container area → pointers start at both ends and move inward
- **Same Direction:** removing/partitioning elements, fast-slow cycle detection → both start at the left and one runs ahead

### 3. Why does moving the pointer make sense?

```
arr = [1, 2, 3, 4, 6],  left=0, right=4
sum = 1 + 6 = 7 > target(6)

Why move right-- and not left++?
  If we move left++  : sum becomes 2+6=8 → even BIGGER ❌
  If we move right-- : sum becomes 1+4=5 → smaller, closer to target ✅

Array is sorted, so:
  sum too BIG   → right-- (right side has big values, bring it down)
  sum too SMALL → left++  (left side has small values, bring it up)
```

---

## ⏱️ Time & Space Complexity

| | Complexity | Why |
|---|---|---|
| **Time** | **O(n)** | Each pointer moves at most n steps; total moves ≤ 2n |
| **Space** | **O(1)** | Only two integer variables (left, right) — no extra array |

No extra space needed. No nested loops. Just two pointers moving toward each other.

---

## 🧠 Quick Summary

```
Two Pointers = Optimized Nested Loop for Sorted Arrays

Opposite Ends  → left=0, right=n-1 → move based on sum vs target
Same Direction → slow=0, fast=0/1  → fast runs ahead, slow follows condition

The array MUST be sorted for opposite-ends technique to work correctly!
```

---

# 👆👆 Two Pointers Pattern — Beginner's Visual Guide

> **One line summary:** Two pointers converts an **O(n²)** nested loop into **O(n)** by using the sorted property to intelligently skip pairs that can never be the answer.

---

## 🧠 The Core Idea — In One Picture

```
Instead of this (slow — checks every pair):
┌─────────────────────────────────────────┐
│  for i from 0 to n-1:                   │
│    for j from i+1 to n-1:               │  ← O(n²) ❌
│      if arr[i] + arr[j] == target        │
└─────────────────────────────────────────┘

Do this instead (fast — use sorted property):
┌─────────────────────────────────────────┐
│  left = 0,  right = n - 1               │
│  sum < target → left++   ← go bigger   │
│  sum > target → right--  ← go smaller   O(n) ✅
└─────────────────────────────────────────┘
```

---

## ✅ When to Use vs Skip

```
✅ USE Two Pointers when...             ❌ SKIP when...
──────────────────────────────────      ──────────────────────────────────
 Array is sorted (or can be sorted)      Array is unsorted and order matters
 Looking for pairs or triplets            You need subarrays → Sliding Window
 Palindrome check                         Problem is on a tree or graph
 Container / area problems                Need all combinations, not just one
 Remove duplicates / partition            Index positions must stay unchanged
```

---

## 📦 Two Types of Two Pointers

```
                     Two Pointers
                           │
         ──────────────────┴────────────────────
         │                                      │
  ┌─────────────────────┐          ┌─────────────────────────┐
  │  Opposite Ends      │          │   Same Direction        │
  │  left=0, right=n-1  │          │   slow=0, fast=0 or 1   │
  └─────────────────────┘          └─────────────────────────┘
         │                                      │
  Pointers move inward                Pointers move forward
         │                                      │
  Example:                            Example:
  "Find pair with sum = target"       "Remove duplicates
   in sorted array"                    from sorted array"
```

---

## 🧩 4-Step Framework (works for ANY two pointers problem)

```
╔═══════╦══════════════════════════════════════════════════════════╗
║ Step  ║ What to do                                               ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  1    ║ Sorted array? Pairs/triplets? Palindrome? Partition?     ║
║       ║ If YES → Two Pointers ✅                                 ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  2    ║ Opposite Ends or Same Direction?                         ║
║       ║ Looking for a pair/area → Opposite Ends                  ║
║       ║ Removing/partitioning elements → Same Direction          ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  3    ║ Place the pointers                                       ║
║       ║ Opposite: left = 0, right = n - 1                        ║
║       ║ Same dir: slow = 0, fast = 1 (or both at 0)             ║
╠═══════╬══════════════════════════════════════════════════════════╣
║  4    ║ Define the move condition                                ║
║       ║ sum < target → left++  (go bigger)                       ║
║       ║ sum > target → right-- (go smaller)                      ║
║       ║ sum == target → answer found!                            ║
╚═══════╩══════════════════════════════════════════════════════════╝
```

---

## 🔍 Worked Example — Pair Sum in Sorted Array

**Problem:** `arr = [1, 2, 3, 4, 6]`, `target = 6` → Find if any pair sums to 6.

### Step 1 — Identify pattern
Sorted array, find pair with target sum → Two Pointers ✅

### Step 2 — Opposite Ends or Same Direction?
Searching for a pair → **Opposite Ends**

### Step 3 — Place pointers
```
left  = 0      → arr[0] = 1
right = n-1=4  → arr[4] = 6
```

### Step 4 — Move condition
```
sum < target → left++    (left side is small, bring it up)
sum > target → right--   (right side is large, bring it down)
sum == target → found!
```

---

## 🎥 Visual — Watch the Pointers Move

```
 Index:   [  0  ]   [  1  ]   [  2  ]   [  3  ]   [  4  ]
 Array:   [  1  ]   [  2  ]   [  3  ]   [  4  ]   [  6  ]
 Target = 6
```

```
 ┌─────────────────────────────────────────────────────────────────────┐
 │                                                                     │
 │  Step 1:  ╔═══════╗                                ╔═══════╗       │
 │           ║   1   ║    2       3       4           ║   6   ║       │
 │           ╚═══════╝                                ╚═══════╝       │
 │           ↑left                                    ↑right          │
 │                                                                     │
 │           sum = 1 + 6 = 7    7 > 6  →  right--                     │
 │           (sum too big, right side has large values, reduce it)     │
 │                                                                     │
 ├─────────────────────────────────────────────────────────────────────┤
 │                                                                     │
 │  Step 2:  ╔═══════╗                        ╔═══════╗               │
 │           ║   1   ║    2       3           ║   4   ║    6          │
 │           ╚═══════╝                        ╚═══════╝               │
 │           ↑left                            ↑right                  │
 │                                                                     │
 │           sum = 1 + 4 = 5    5 < 6  →  left++                      │
 │           (sum too small, left side has small values, increase it)  │
 │                                                                     │
 ├─────────────────────────────────────────────────────────────────────┤
 │                                                                     │
 │  Step 3:            ╔═══════╗               ╔═══════╗              │
 │            1        ║   2   ║    3          ║   4   ║    6         │
 │                     ╚═══════╝               ╚═══════╝              │
 │                     ↑left                   ↑right                 │
 │                                                                     │
 │           sum = 2 + 4 = 6    6 == 6  →  FOUND! ✅                  │
 │           Pair: (2, 4)                                              │
 │                                                                     │
 └─────────────────────────────────────────────────────────────────────┘
```

---

## 📋 Dry Run Table

| Step | `left` | `right` | `arr[left]` | `arr[right]` | `sum` | Action |
|------|--------|---------|-------------|--------------|-------|--------|
| 1    | 0      | 4       | 1           | 6            | 7     | 7 > 6 → right-- |
| 2    | 0      | 3       | 1           | 4            | 5     | 5 < 6 → left++ |
| 3    | 1      | 3       | 2           | 4            | **6** | 6 == 6 → **FOUND ✅** |

> **Total steps: 3** — Brute force would check 10 pairs for n=5. Two pointers did it in 3 moves!

---

## 💻 Java Code

```java
public class TwoPointers {

    // Opposite Ends — Pair Sum in Sorted Array
    static boolean hasPairWithSum(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];

            if (sum == target) {
                System.out.println("Pair found: " + arr[left] + " + " + arr[right]);
                return true;                   // pair found!
            } else if (sum < target) {
                left++;                        // sum too small → move left right
            } else {
                right--;                       // sum too big  → move right left
            }
        }

        return false;                          // no pair found
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 6};
        int target = 6;

        System.out.println(hasPairWithSum(arr, target));  // true, Pair: 2 + 4
    }
}
```

---

## ⏱️ Complexity Analysis

```
 ┌───────────────────┬─────────────────────┬────────────────────────────────┐
 │ Approach          │ Time Complexity     │ Why                            │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Naive (2 loops)   │   O(n²)     ❌     │ Checks every (i,j) pair        │
 │ Two Pointers      │   O(n)      ✅     │ Each pointer moves at most n   │
 ├───────────────────┼─────────────────────┼────────────────────────────────┤
 │ Space             │   O(1)      ✅     │ Only left + right variables    │
 └───────────────────┴─────────────────────┴────────────────────────────────┘
```

---

## 💡 Key Insights

### 1. Two Pointers = Optimized Nested Loop for Sorted Arrays
Instead of checking all pairs, the sorted order tells us *which direction to move*.
Every comparison eliminates an entire half of remaining possibilities — that's why it's O(n).

### 2. "Why does the direction logic work?" — The sorted trick

```
arr = [1, 2, 3, 4, 6]   target = 6

At left=0, right=4:  sum = 1+6 = 7 > 6

Can any pair (arr[0], arr[j]) where j < 4 give us 6?
  arr[0] + arr[3] = 1 + 4 = 5  → already tried next step
  arr[0] + arr[2] = 1 + 3 = 4  → going DOWN, never reaches 6
  arr[0] + arr[1] = 1 + 2 = 3  → even smaller ❌

So when sum > target with right at j, we KNOW no pair (left, j) or smaller j will work.
→ We safely skip all of them by doing right-- ✅
```

### 3. Opposite Ends vs Same Direction

```
Opposite Ends  → sorted array → find pair/triplet → pointers move inward
Same Direction → remove/partition → fast pointer runs ahead, slow follows condition
```

### 4. ⚠️ Array MUST be sorted for opposite-ends technique!

```
Unsorted arr = [6, 1, 4, 2, 3], target = 6

left=0, right=4:  6+3=9 > 6 → right--
left=0, right=3:  6+2=8 > 6 → right--
left=0, right=2:  6+4=10 > 6 → right--
left=0, right=1:  6+1=7 > 6 → right--
left=0, right=0:  loop ends → returns false ❌   (WRONG! 2+4=6 exists)

// ✅ Sort first, then apply two pointers
Arrays.sort(arr);   // [1, 2, 3, 4, 6]
hasPairWithSum(arr, 6);   // now works correctly
```

---

## 🧠 Quick Summary Card

```
╔══════════════════════════════════════════════════════════════╗
║           TWO POINTERS — QUICK REFERENCE                     ║
╠══════════════════════════════════════════════════════════════╣
║  Pattern type  : Sorted array / pair / partition problems    ║
║  Key operation : Move left++ or right-- based on comparison  ║
║  Condition     : sum < target → left++, sum > target → right-║
║  Time          : O(n)   — each pointer moves at most n steps ║
║  Space         : O(1)   — only two integer variables         ║
╠══════════════════════════════════════════════════════════════╣
║  Opposite Ends : pair/area/palindrome → left=0, right=n-1    ║
║  Same Direction: remove/partition    → slow=0, fast=1        ║
╠══════════════════════════════════════════════════════════════╣
║  ⚠️  Array MUST be sorted for opposite-ends to work!         ║
╚══════════════════════════════════════════════════════════════╝
```

---

## 📚 Practice Problems

| # | Problem | Type | Difficulty |
|---|---------|------|------------|
| 1 | Pair Sum in Sorted Array (LC 167) | Opposite Ends | ⭐ Easy |
| 2 | Valid Palindrome (LC 125) | Opposite Ends | ⭐ Easy |
| 3 | Given Sum Pair | Opposite Ends | ⭐ Easy |
| 4 | 3Sum (LC 15) | Opposite Ends + Fix one | ⭐⭐ Medium |
| 5 | Container With Most Water (LC 11) | Opposite Ends | ⭐⭐ Medium |
| 6 | Sort Colors / Dutch National Flag (LC 75) | Same Direction | ⭐⭐ Medium |
| 7 | Segregate 0s and 1s | Same Direction | ⭐⭐ Medium |
| 8 | Trapping Rain Water (LC 42) | Opposite Ends | ⭐⭐⭐ Hard |

---

> 📌 **Next Topic:** [02 - Sliding Window](../02-Sliding-Window/README.md)  
> 📌 **Previous Topic:** [00 - Arrays Basics](../00-Arrays/README.md)

*Made while learning DSA — one pattern at a time.* 🚀
