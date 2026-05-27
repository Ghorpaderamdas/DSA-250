# Q1. Product of Array Except Self

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Prefix Products &nbsp;|&nbsp; **Companies:** Amazon, Apple, Facebook, Google, Microsoft

🔗 [LeetCode 238](https://leetcode.com/problems/product-of-array-except-self/description/)

---

## 📝 Problem Statement

Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the **product of all elements of `nums` except `nums[i]`**.

You must write an algorithm that runs in **O(n) time** and **without using division**.

### Examples

| Input | Output | Explanation |
|---|---|---|
| `[1, 2, 3, 4]` | `[24, 12, 8, 6]` | 24=2×3×4, 12=1×3×4, 8=1×2×4, 6=1×2×3 |
| `[-1, 1, 0, -3, 3]` | `[0, 0, 9, 0, 0]` | One zero makes almost everything 0; the zero slot gets the product of rest |
| `[0, 0]` | `[0, 0]` | Both positions see at least one zero on the other side |
| `[1, 0]` | `[0, 1]` | pos-0 sees zero → 0; pos-1 sees 1 → 1 |

---

## 🔒 Constraints

- `2 ≤ nums.length ≤ 10⁵`
- `-30 ≤ nums[i] ≤ 30`
- The product of any prefix or suffix of `nums` is guaranteed to fit in a 32-bit integer
- **Division is NOT allowed**

---

## 🔑 Key Observations

- `answer[i]` = (everything to the LEFT of i) × (everything to the RIGHT of i)
- These two parts are independent — we can compute each separately
- Left products can be built in a single left-to-right pass
- Right products can be applied in a single right-to-left pass using just one variable
- The output array can double as the left-products array → **O(1) extra space**
- Division is avoided entirely — no zero-handling edge case needed

---

## 💡 Intuition Building

**Why brute force is obvious but slow:**
For each index `i`, loop over all `n-1` other elements and multiply them. Clear logic, but `n × (n-1)` multiplications → O(n²).

**The aha moment — separation of concerns:**
> `answer[i] = LEFT[i] × RIGHT[i]`

Every answer is just a left product multiplied by a right product.  
We can compute all left products in one pass, all right products in another pass.  
Two O(n) passes beats one O(n²) double loop.

**The space optimization:**
Instead of two O(n) arrays (left and right), we store left products in the output array itself, then apply right products on-the-fly with a single running variable — reducing extra space to O(1).

**From O(n²) → O(n):**
```
O(n²): for every i, multiply all j ≠ i (recompute each time)
O(n) : precompute left array + precompute right array, multiply
O(n) : precompute left into output + running right variable (no extra array)
```

---

## 📊 Approaches Overview

| Approach | Technique | Time | Space | Use In Interview? |
|---|---|---|---|---|
| Brute Force | Double loop | O(n²) | O(1) | ❌ Too slow |
| Division Trick | Product / element | O(n) | O(1) | ⚠️ Not allowed here |
| **Prefix × Suffix** ⭐ | Left pass + right pass | **O(n)** | **O(1)** | ✅ Always |

---

## APPROACH 1 — BRUTE FORCE

### Idea
For every index `i`, multiply all elements except the one at `i`.

### Algorithm
1. Create result array of size `n`
2. For each `i` from `0` to `n-1`:
   - Set `product = 1`
   - For each `j` from `0` to `n-1`, skip if `j == i`, else `product *= nums[j]`
   - Set `result[i] = product`
3. Return result

### Dry Run — `nums = [1, 2, 3, 4]`

```
i=0: skip j=0 → 2×3×4 = 24
i=1: skip j=1 → 1×3×4 = 12
i=2: skip j=2 → 1×2×4 = 8
i=3: skip j=3 → 1×2×3 = 6

result = [24, 12, 8, 6] ✅
```

### Java Code

```java
public int[] productExceptSelfBrute(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];

    for (int i = 0; i < n; i++) {
        int product = 1;
        for (int j = 0; j < n; j++) {
            if (i != j) product *= nums[j]; // skip index i
        }
        result[i] = product;
    }

    return result;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n²) | O(1) | Two nested loops; n×(n-1) multiplications |

**Pros:** Simple to understand and implement  
**Cons:** Too slow for n = 10⁵

---

## APPROACH 2 — BETTER: Division Trick

### Idea
Multiply all elements together → `totalProduct`. Then `answer[i] = totalProduct / nums[i]`.

Handle zeros separately (you can't divide by zero):
- Two or more zeros → all answers are 0
- Exactly one zero → only the zero's position gets a nonzero answer
- No zeros → simple division

### Algorithm
1. Count zeros; compute product of all non-zero elements
2. Fill answer based on zero count

### ⚠️ Why We Don't Use This
LeetCode **explicitly forbids division** in the problem statement. This is shown only so you understand *why* the optimal solution exists — to avoid division entirely.

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n) | O(1) | Two passes, constant extra variables |

---

## APPROACH 3 — OPTIMAL: Prefix × Suffix Products ⭐

### Deep Intuition — Why Does This Work?

For any index `i`:
```
answer[i] = (nums[0] × nums[1] × ... × nums[i-1])
          × (nums[i+1] × nums[i+2] × ... × nums[n-1])
```

The **left part** only depends on elements before `i`.  
The **right part** only depends on elements after `i`.

We can compute ALL left parts in one left-to-right pass and ALL right parts in one right-to-left pass.

### Key Space Trick
- Use the **output array** to store left products (doesn't count as extra space)
- Use a **single `suffix` variable** to accumulate right products on the fly

No second array needed!

### Algorithm (Numbered Steps)
1. Initialize `result[0] = 1` (nothing to the left of index 0)
2. **Left pass** — `for i from 1 to n-1`:  
   `result[i] = result[i-1] * nums[i-1]`
3. Initialize `suffix = 1` (nothing to the right of last index)
4. **Right pass** — `for i from n-1 down to 0`:  
   `result[i] = result[i] * suffix`  (combine left and right)  
   `suffix = suffix * nums[i]`  (extend suffix one step left)
5. Return `result`

### Dry Run — `nums = [1, 2, 3, 4]`

**After left pass:**
```
i=0: result[0] = 1                   (nothing left)
i=1: result[1] = result[0] × nums[0] = 1×1 = 1
i=2: result[2] = result[1] × nums[1] = 1×2 = 2
i=3: result[3] = result[2] × nums[2] = 2×3 = 6

result = [1, 1, 2, 6]
```

**Right pass (suffix starts = 1):**
```
i=3: result[3] = 6 × 1   = 6   suffix = 1×4   = 4
i=2: result[2] = 2 × 4   = 8   suffix = 4×3   = 12
i=1: result[1] = 1 × 12  = 12  suffix = 12×2  = 24
i=0: result[0] = 1 × 24  = 24  suffix = 24×1  = 24

result = [24, 12, 8, 6] ✅
```

### Java Code (Interview-Quality)

```java
public int[] productExceptSelf(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];

    // STEP 1: left products
    result[0] = 1;
    for (int i = 1; i < n; i++) {
        result[i] = result[i - 1] * nums[i - 1];
    }

    // STEP 2: multiply by running right (suffix) product
    int suffix = 1;
    for (int i = n - 1; i >= 0; i--) {
        result[i] = result[i] * suffix;
        suffix    = suffix * nums[i];
    }

    return result;
}
```

### Complexity

| Time | Space | Reason |
|---|---|---|
| O(n) | O(1) extra | Two passes; only a scalar `suffix` variable |

*(The output array is O(n) but is not counted as "extra" space per convention.)*

### How to Explain in an Interview

> *"I split the problem into two independent parts: left products and right products.  
> For index i, the answer is the product of everything to the left times everything to the right.  
> I use the output array itself to store left products in a left-to-right pass.  
> Then I make a right-to-left pass with a running 'suffix' variable and multiply it into each position.  
> Two passes, O(n) time, O(1) extra space, no division at all."*

---

## ⚠️ Common Mistakes

| Mistake | Fix |
|---|---|
| Trying to use division | The problem forbids it; use prefix × suffix |
| Forgetting `result[0] = 1` | Nothing is to the left of index 0; left product = 1 |
| Using two separate O(n) arrays | Use output array for left, a single variable for right |
| Overwriting `suffix` before using it | Update `suffix` AFTER computing `result[i]` in the right pass |

---

## 🔍 Edge Cases

| Input | Output | Why |
|---|---|---|
| `[1, 0]` | `[0, 1]` | Left of pos-0 = nothing; right of pos-1 = 1 |
| `[0, 0]` | `[0, 0]` | Each side sees a zero |
| `[-1, -1]` | `[-1, -1]` | Negative × negative = positive handled naturally |
| `[1, 1, 1, 1]` | `[1, 1, 1, 1]` | All products of others = 1 |

---

## 🧩 Pattern Recognition

**This problem IS:** Prefix Products — a structural cousin of Prefix Sums.  
**Use this pattern when:** You need some aggregate of "everything except i" in O(n).

**General template:**
```
leftProduct[i]  = product of arr[0..i-1]
rightProduct[i] = product of arr[i+1..n-1]
answer[i]       = leftProduct[i] × rightProduct[i]
```

The same trick extends to: prefix XOR, prefix max/min, prefix GCD.

---

## 🎯 Interview Tips

1. **Start by explaining the left × right insight** — don't jump straight to code
2. **Acknowledge division is forbidden** before describing the approach
3. **Mention space optimization** — goes from O(n) extra → O(1) extra using the output array
4. **Verify with a simple example** — `[1,2,3,4]` → `[24,12,8,6]` in your head

---

## 🔗 Related Problems

- LeetCode 42 — Trapping Rain Water (prefix max from left + right)
- LeetCode 152 — Maximum Product Subarray (prefix products)
- LeetCode 724 — Find Pivot Index (prefix sums)
- GFG — Largest Subarray with 0 Sum (prefix sums + HashMap)

---

## 📌 Revision Notes

- `answer[i]` = LEFT product × RIGHT product — the core insight
- Use output array for left products → O(1) extra space
- Right pass: running `suffix` variable, update suffix AFTER multiplying into result
- Division is forbidden — this is why prefix × suffix approach exists
- Two O(n) passes; no edge case for zeros needed

---

## 🏁 Key Takeaways

> This problem teaches you to **think in two directions** — left pass and right pass. The prefix-product pattern generalises beyond this one problem: any time you need "everything except index i", consider building prefix and suffix aggregates.

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
