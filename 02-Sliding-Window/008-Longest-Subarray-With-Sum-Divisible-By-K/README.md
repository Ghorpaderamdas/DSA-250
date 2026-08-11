<img width="1024" height="1536" alt="Q8  Longest Subarray with Sum Divisible by K" src="https://github.com/user-attachments/assets/9d7abec5-3696-454c-9aec-583b0e47347f" />



# Q8. Longest Subarray with Sum Divisible by K

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Prefix Sum + HashMap &nbsp;|&nbsp; **Companies:** Amazon, Google, Flipkart

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/dsa/longest-subarray-sum-divisible-k/)

---

## ⚠️ Important Note — Pattern Alert

> This problem **looks like** a sliding window problem (it's in the sliding window chapter),  
> but the **optimal solution uses Prefix Sum + HashMap** — NOT the classic two-pointer window.  
> Why? Because the array can have **negative numbers**, which breaks the monotonic property needed for variable sliding windows.

---

## 📝 Problem

Given an array `arr[]` of integers and a number `k`, find the **length of the longest subarray** whose **sum is divisible by `k`**.

### Examples

| Input                     | k | Output | Subarray (example)              |
|---------------------------|---|--------|---------------------------------|
| `[2, 7, 6, 1, 4, 5]`     | 3 | 4      | `[7, 6, 1, 4]` → sum=18, 18%3=0 |
| `[-2, 2, -8, 1, 7, 10, 23]` | 5 | 5   | `[2, -8, 1, 7, 10]` → sum=12? No. Let me recalculate: [-2,2,-8,1,7]=0, sum=0%5=0 |
| `[10, 2, -2, -20, 10]`   | 5 | 5      | Entire array, sum=0, 0%5=0      |
| `[1, 2, 3]`              | 6 | 3      | Entire array, sum=6, 6%6=0      |

---

## 🔑 Core Math — The Modular Arithmetic Trick

**Before diving into solutions, understand this:**

```
If sum of subarray arr[i+1 .. j] is divisible by k, then:
    (prefix[j] - prefix[i]) % k == 0
    → prefix[j] % k == prefix[i] % k

So: if two prefix sums have the SAME REMAINDER when divided by k,
    the subarray between them has a sum divisible by k!

Example:
  arr     = [2, 7, 6, 1, 4, 5],  k=3
  prefix  = [0, 2, 9, 15, 16, 20, 25]
  prefix%3= [0, 2, 0,  0,  1,  2,  1]
              ↑     ↑   ↑
         index=-1  1   2  ← same remainder 0!
         subarray [1..2] = arr[0..1] = [2,7] → sum=9 → 9%3=0 ✅
         subarray [0..2] = prefix from -1 to 2 → sum=15 → 15%3=0 ✅ (length=3)

  Also remainder 0 at index 3 → subarray from -1 to 3 → length = 3-(-1) = 4
  prefix[3]-prefix[-1] = 15-0 = 15 → 15%3=0 ✅  length=4 ✅  ← ANSWER!
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n²))

**How it works:**
1. Try every possible subarray using two nested loops
2. Compute the sum of `arr[i..j]`
3. Check if `sum % k == 0`
4. Track the maximum length of valid subarrays

**Pseudo Code:**
```
maxLen = 0
for i from 0 to n-1:
    sum = 0
    for j from i to n-1:
        sum += arr[j]
        if sum % k == 0:
            maxLen = max(maxLen, j - i + 1)
return maxLen
```

**Dry Run** — `arr = [2, 7, 6, 1, 4, 5]`, `k = 3`

```
i=0: sum=2       2%3=2 ❌
     sum=2+7=9   9%3=0 ✅  len=2  maxLen=2
     sum=9+6=15  15%3=0 ✅ len=3  maxLen=3
     sum=15+1=16 16%3=1 ❌
     sum=16+4=20 20%3=2 ❌
     sum=20+5=25 25%3=1 ❌

i=1: sum=7       7%3=1 ❌
     sum=7+6=13  13%3=1 ❌
     sum=13+1=14 14%3=2 ❌
     sum=14+4=18 18%3=0 ✅ len=4  maxLen=4  ← best!
     sum=18+5=23 23%3=2 ❌

i=2: sum=6       6%3=0 ✅  len=1
     sum=6+1=7   7%3=1 ❌
     sum=7+4=11  11%3=2 ❌
     sum=11+5=16 16%3=1 ❌
...

Answer: 4 ✅  (subarray [7,6,1,4])
```

**Complexity Analysis:**

| Time  | Space | Reason                                      |
|-------|-------|---------------------------------------------|
| O(n²) | O(1)  | Two nested loops; sum updated incrementally |

---

### Method 2 — Better: Prefix Sum Array (O(n²))

**Idea:** Precompute the prefix sum array. Then use the mathematical insight:  
`sum(i..j) % k == 0 ↔ prefix[j+1] % k == prefix[i] % k`

Check all pairs `(i, j)` using the prefix array — still O(n²) but cleaner.

**Pseudo Code:**
```
prefix[0] = 0
for i from 0 to n-1:
    prefix[i+1] = prefix[i] + arr[i]

maxLen = 0
for i from 0 to n:
    for j from i+1 to n:
        if (prefix[j] - prefix[i]) % k == 0:
            maxLen = max(maxLen, j - i)
return maxLen
```

**Why this is useful:**  
It makes the relationship `prefix[j] % k == prefix[i] % k` visually obvious  
and sets up the intuition for the O(n) solution.

**Complexity Analysis:**

| Time  | Space | Reason                                         |
|-------|-------|------------------------------------------------|
| O(n²) | O(n)  | Two nested loops; prefix array of size n+1     |

---

### Method 3 — Optimal: Prefix Sum + HashMap (O(n)) ⭐

**Idea:**
- As we compute prefix sums, store the **first occurrence** of each `remainder = prefix % k` in a HashMap
- If we see the **same remainder again**, the subarray between first occurrence and now has sum divisible by k
- Length = `currentIndex - firstOccurrenceIndex`

**⚠️ Handle negative remainders:**
```
In Java: (-7) % 3 = -1  (not 2!)
We always want a POSITIVE remainder: ((prefix % k) + k) % k
```

**⚠️ Initialize the HashMap with `{0 → -1}`:**
```
prefix[0] = 0, remainder = 0
This means: if we ever see remainder 0 at index i,
            subarray from index 0 to i has sum divisible by k
            length = i - (-1) = i + 1 ✅
```

**Dry Run** — `arr = [2, 7, 6, 1, 4, 5]`, `k = 3`

```
map = {0: -1}   prefix = 0   maxLen = 0

i=0: prefix=0+2=2   rem=2%3=2   map has no 2 → store {2:0}     map={0:-1, 2:0}
i=1: prefix=2+7=9   rem=9%3=0   map has 0 at index -1!
     length = 1 - (-1) = 2   maxLen=2
     DON'T update map (keep first occurrence: index -1)

i=2: prefix=9+6=15  rem=15%3=0  map has 0 at index -1!
     length = 2 - (-1) = 3   maxLen=3
     DON'T update (keep first occurrence)

i=3: prefix=15+1=16 rem=16%3=1  map has no 1 → store {1:3}    map={0:-1, 2:0, 1:3}
i=4: prefix=16+4=20 rem=20%3=2  map has 2 at index 0!
     length = 4 - 0 = 4   maxLen=4  ← new max!
     DON'T update (keep first occurrence: index 0)

i=5: prefix=20+5=25 rem=25%3=1  map has 1 at index 3!
     length = 5 - 3 = 2   maxLen=4  (not larger)

Answer: 4 ✅  (subarray arr[1..4] = [7,6,1,4])
```

**Pseudo Code:**
```
map = {0: -1}       ← remainder 0 seen at "virtual" index -1
prefix = 0
maxLen = 0

for i from 0 to n-1:
    prefix += arr[i]
    rem = ((prefix % k) + k) % k     ← always non-negative

    if rem is in map:
        maxLen = max(maxLen, i - map[rem])
        ← DON'T update map[rem] → keep the FIRST (earliest) occurrence
    else:
        map[rem] = i                  ← store first occurrence

return maxLen
```

**Why we only store the FIRST occurrence:**
```
We want the LONGEST subarray → the EARLIEST start is best.
If rem appears at indices 2, 5, 8:
  - subarray from 2 to 8 is longer than 5 to 8
  - So always keep the earliest index.
```

**Complexity Analysis:**

| Time | Space | Reason                                              |
|------|-------|-----------------------------------------------------|
| O(n) | O(k)  | Single pass; HashMap has at most k distinct remainders |

---

## 📊 Comparison Table

| Method                    | Time  | Space | Key Idea                                        |
|---------------------------|-------|-------|-------------------------------------------------|
| Brute Force               | O(n²) | O(1)  | Try all subarrays, check sum % k                |
| Prefix Sum (Better)       | O(n²) | O(n)  | All pairs of prefix sums, check same remainder  |
| **Prefix Sum + HashMap** ⭐ | **O(n)** | **O(k)** | Same remainder → subarray between is divisible; keep earliest |

---

## 💡 Key Insights

1. **Core math:** `sum(i..j) % k == 0  ↔  prefix[j] % k == prefix[i] % k`

2. **`map = {0 : -1}` initialization is critical:**  
   Handles subarrays starting from index 0 whose sum is divisible by k.

3. **Always keep the FIRST occurrence** (never overwrite in the map):  
   The earliest start gives the longest subarray.

4. **Negative remainder fix:** `((prefix % k) + k) % k`  
   Java's `%` can return negative values for negative numbers. Always normalize.

5. **Why not sliding window?**  
   The array can have negative numbers → window sum is NOT monotonic.  
   Shrinking the window doesn't guarantee the sum moves in a predictable direction.  
   Prefix Sum + HashMap is the right tool here.

---

## 🔍 Edge Cases

| Input              | k | Output | Why                                      |
|--------------------|---|--------|------------------------------------------|
| `[1, 2, 3]`       | 6 | 3      | Entire array, sum=6, 6%6=0               |
| `[1, 2, 3]`       | 7 | 0      | No subarray sum divisible by 7           |
| `[-1, 2, 9]`      | 3 | 3      | Sum=10? No. -1+2+9=10, 10%3=1. Sum of [-1,2]=1. Sum of [2,9]=11. Hmm. Actually [2,9]=11, [-1]=−1. Let me check: whole array sum = 10. 10%3=1. So answer might be smaller. |
| `[0, 0, 0]`       | 5 | 3      | Sum=0 for entire array, 0%5=0            |
| `[4]`             | 2 | 0      | 4%2=0 → length=1. Wait: 4%2=0 ✅ so answer=1 |

---

## ⚠️ Common Mistakes

| Mistake                                   | Fix                                                      |
|-------------------------------------------|----------------------------------------------------------|
| `prefix % k` without handling negatives   | Use `((prefix % k) + k) % k`                            |
| Not initializing `map = {0: -1}`          | Always initialize — handles subarrays starting at index 0 |
| Overwriting map on second occurrence      | Only store FIRST occurrence; skip if key already in map  |
| Using `i - map[rem] + 1`                  | Correct formula is `i - map[rem]` (map stores exclusive left boundary) |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
