# Q2. 3Sum

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Two Pointers &nbsp;|&nbsp; **Companies:** Amazon, Facebook, Microsoft, Apple, Bloomberg

🔗 [LeetCode Link](https://leetcode.com/problems/3sum/)

---

## 📝 Problem
Given an integer array `nums`, return **all the triplets** `[nums[i], nums[j], nums[k]]` such that:
- `i != j`, `i != k`, and `j != k`
- `nums[i] + nums[j] + nums[k] == 0`

The solution set **must not contain duplicate triplets**.

### Examples
| Input                  | Output                          | Reason                                              |
|------------------------|---------------------------------|-----------------------------------------------------|
| `[-1, 0, 1, 2, -1, -4]`| `[[-1,-1,2], [-1,0,1]]`         | Two distinct triplets sum to 0                      |
| `[0, 1, 1]`            | `[]`                            | No triplet sums to 0                                |
| `[0, 0, 0]`            | `[[0,0,0]]`                     | Only one valid triplet                              |

### Constraints
- `3 <= nums.length <= 3000`
- `-10⁵ <= nums[i] <= 10⁵`

---

## 🧠 Approaches

### Method 1 — Brute Force
**Logic:** Try every triplet `(i, j, k)`. Sort each triplet and add to a `HashSet` to dedupe.

| Time   | Space |
|--------|-------|
| O(n³)  | O(n)  |

### Method 2 — Hashing (Better)
**Logic:** Fix `i`, then for each `j > i` use a `HashSet` to find if `-(nums[i] + nums[j])` already appeared.

| Time   | Space |
|--------|-------|
| O(n²)  | O(n)  |

### Method 3 — Sort + Two Pointer ⭐ (Optimal)
**Logic:**
1. **Sort** the array → duplicates become adjacent, two pointers move monotonically.
2. Fix `i`, then use two pointers `L = i+1`, `R = n-1`.
3. Compute `sum = nums[i] + nums[L] + nums[R]`:
   - `sum == 0` → record triplet, then **skip duplicates** for both `L` and `R`.
   - `sum < 0`  → `L++` (need bigger sum).
   - `sum > 0`  → `R--` (need smaller sum).
4. **Skip duplicate `i`** with `if (i > 0 && nums[i] == nums[i-1]) continue;`
5. **Early exit** with `if (nums[i] > 0) break;` — sorted array, so no zero-sum possible.

| Time   | Space |
|--------|-------|
| O(n²)  | O(1)  |

---

## 🔍 Dry Run — Method 3 (Optimal)

**Input:** `nums = [-1, 0, 1, 2, -1, -4]`

```
After sort: [-4, -1, -1, 0, 1, 2]
              i=0  1   2  3  4  5

i=0 (-4):  L=1(-1), R=5(2) → sum=-3 < 0 → L++
           L=2(-1), R=5(2) → sum=-3 < 0 → L++
           L=3(0),  R=5(2) → sum=-2 < 0 → L++
           L=4(1),  R=5(2) → sum=-1 < 0 → L++  → L==R, stop

i=1 (-1):  L=2(-1), R=5(2) → sum=0  ✅ add [-1,-1,2]
                              skip dup L (none), skip dup R (none) → L++, R--
           L=3(0),  R=4(1) → sum=0  ✅ add [-1, 0, 1]
                              L++, R--  → L==R, stop

i=2 (-1):  duplicate of i=1 → SKIP

i=3 (0):   L=4(1),  R=5(2) → sum=3  > 0 → R--  → L==R, stop

i=4 (1):   nums[i] > 0 → BREAK (early exit)

Result: [[-1,-1,2], [-1,0,1]]
```

---

## 📊 Comparison

| Method            | Time   | Space | Interview Value             |
|-------------------|--------|-------|-----------------------------|
| Brute Force       | O(n³)  | O(n)  | Baseline only               |
| Hashing           | O(n²)  | O(n)  | Good intermediate step      |
| **Sort + Two Pointer** | **O(n²)** | **O(1)** | ⭐ **Best answer (submit this)** |

---

## 💡 Key Insights

1. **Sorting unlocks two pointers** — the `O(n log n)` sort cost is dominated by the `O(n²)` scan, so it's free in big-O terms.
2. **Reduce 3Sum → 2Sum** — fix one element, then the inner loop is just the classic two-pointer 2Sum on a sorted array.
3. **Three duplicate-skips** — at `i`, at `L` (after match), at `R` (after match). Missing any one produces duplicate triplets.
4. **Early termination** — once `nums[i] > 0` in a sorted array, no triplet with this `i` can sum to 0.

---

## ⚠️ Edge Cases
- All zeros `[0, 0, 0, 0]` → only `[0,0,0]` once (duplicate skip handles it).
- All positives or all negatives → no triplet possible, returns `[]`.
- `n == 3` → check the single triplet directly.
- Many duplicates of same value → the `i` skip prevents repeated work.

---

## 🏷️ Method Signature (LeetCode)
```java
public List<List<Integer>> threeSum(int[] nums)
```

---

## 🔗 Related Problems
- [1. Two Sum](https://leetcode.com/problems/two-sum/) — the 2Sum building block
- [16. 3Sum Closest](https://leetcode.com/problems/3sum-closest/) — same pattern, track closest sum
- [18. 4Sum](https://leetcode.com/problems/4sum/) — extend with one more outer loop
- [259. 3Sum Smaller](https://leetcode.com/problems/3sum-smaller/) — count triplets `< target`

---

## 🔁 Revision Tracker
- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
