# Q6. Sliding Window Maximum

**Difficulty:** Hard &nbsp;|&nbsp; **Pattern:** Sliding Window + Monotonic Deque &nbsp;|&nbsp; **Companies:** Amazon, Google, Uber, Microsoft

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/dsa/sliding-window-maximum-maximum-of-all-subarrays-of-size-k/) &nbsp;|&nbsp; [LeetCode #239](https://leetcode.com/problems/sliding-window-maximum/)

---

## 📝 Problem

Given an array `arr[]` and a window size `k`, find the **maximum element in every window of size `k`** as the window slides from left to right.

### Examples

| Input                          | k | Output          |
|--------------------------------|---|-----------------|
| `[1, 3, -1, -3, 5, 3, 6, 7]`  | 3 | `[3,3,5,5,6,7]` |
| `[1, 2, 3, 1, 4, 5, 2, 3, 6]` | 3 | `[3,3,4,5,5,5,6]`|
| `[8, 5, 10, 7, 9, 4]`         | 3 | `[10,10,10,9]`  |
| `[1, 2, 3]`                    | 1 | `[1,2,3]`       |

**Explanation for Example 1 (`k=3`):**
```
Window [1,3,-1]  → max = 3
Window [3,-1,-3] → max = 3
Window [-1,-3,5] → max = 5
Window [-3,5,3]  → max = 5
Window [5,3,6]   → max = 6
Window [3,6,7]   → max = 7
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n × k))

**How it works:**
1. Loop through every possible window starting position
2. For each window of size `k`, scan all `k` elements to find the maximum
3. Record each window's maximum

**Pseudo Code:**
```
result = []
for i from 0 to n-k:                  ← start of each window
    windowMax = -infinity
    for j from i to i+k-1:            ← scan k elements
        windowMax = max(windowMax, arr[j])
    result.add(windowMax)
return result
```

**Dry Run** — `arr = [1, 3, -1, -3, 5, 3, 6, 7]`, `k = 3`

```
i=0: window=[1, 3,-1]   scan: max(1,3,-1)  = 3  → result=[3]
i=1: window=[3,-1,-3]   scan: max(3,-1,-3) = 3  → result=[3,3]
i=2: window=[-1,-3, 5]  scan: max(-1,-3,5) = 5  → result=[3,3,5]
i=3: window=[-3, 5, 3]  scan: max(-3,5,3)  = 5  → result=[3,3,5,5]
i=4: window=[5, 3, 6]   scan: max(5,3,6)   = 6  → result=[3,3,5,5,6]
i=5: window=[3, 6, 7]   scan: max(3,6,7)   = 7  → result=[3,3,5,5,6,7]

Answer: [3, 3, 5, 5, 6, 7] ✅
```

**Complexity Analysis:**

| Time  | Space | Reason                                 |
|-------|-------|----------------------------------------|
| O(nk) | O(1)  | Outer loop × inner scan of k elements  |

---

### Method 2 — Better: Max Heap (O(n log k))

**Idea:** Use a **max heap** (priority queue) of `{value, index}` pairs.
- Add the new element to the heap
- **Lazily remove** elements outside the window from the top (check if index < window start)
- Top of heap = maximum in current window

**Pseudo Code:**
```
maxHeap = PriorityQueue (max by value)
result = []

for right from 0 to n-1:
    maxHeap.add({arr[right], right})

    // remove elements outside the window from top
    while maxHeap.top().index < right - k + 1:
        maxHeap.remove()

    // window is full → record max
    if right >= k - 1:
        result.add(maxHeap.top().value)

return result
```

**Dry Run** — `arr = [1, 3, -1, -3, 5, 3]`, `k = 3`

```
right=0: heap={(1,0)}  not full yet
right=1: heap={(3,1),(1,0)}  not full yet
right=2: heap={(3,1),(1,0),(-1,2)}  FULL (right=k-1=2)
         top=(3,1) → index 1 >= 0 (right-k+1=0) ✅  result=[3]
right=3: heap={(3,1),(1,0),(-1,2),(-3,3)}
         top=(3,1) → index 1 >= 1 (right-k+1=1) ✅  result=[3,3]
right=4: add (5,4) → heap={(5,4),(3,1),...}
         top=(5,4) → index 4 >= 2 ✅  result=[3,3,5]
right=5: add (3,5)
         top=(5,4) → index 4 >= 3 ✅  result=[3,3,5,5]

Answer: [3,3,5,5,...] ✅
```

**Complexity Analysis:**

| Time       | Space  | Reason                                          |
|------------|--------|-------------------------------------------------|
| O(n log k) | O(k)   | Each element inserted/removed once; heap size ≤ k |

---

### Method 3 — Sliding Window + Monotonic Deque ⭐ (Optimal)

**Idea:** Use a **deque (double-ended queue)** that stores **indices** in a special order:
- The deque is always **monotonically decreasing** in values
- Front of deque = index of the **maximum** in the current window
- Remove from **front** if that index is outside the window
- Remove from **back** any index whose value is **≤ current** (they can never be the max while the current element is in the window)

**Why remove smaller elements from the back?**
```
If arr[back] <= arr[right]:
  → arr[back] is in the window EARLIER than arr[right]
  → arr[right] is BIGGER (or equal)
  → arr[back] will NEVER be the maximum while arr[right] is in the window
  → arr[right] leaves the window AFTER arr[back] does
  → So arr[back] is USELESS → throw it out!
```

**Visual** — `arr = [1, 3, -1, -3, 5, 3, 6, 7]`, `k = 3`

```
Deque stores INDICES. Values shown in brackets for clarity.

right=0: arr[0]=1
  deque empty → add 0
  deque: [0]  (values: [1])
  not full yet (right < k-1=2)

right=1: arr[1]=3
  back=0, arr[0]=1 ≤ arr[1]=3 → remove 0 from back (1 is useless now)
  deque empty → add 1
  deque: [1]  (values: [3])
  not full yet

right=2: arr[2]=-1
  back=1, arr[1]=3 > arr[2]=-1 → keep it (3 might still be max)
  add 2 to back
  deque: [1, 2]  (values: [3, -1])
  FULL! front=1 → arr[1]=3  result=[3]

right=3: arr[3]=-3
  front=1 → index 1, window start = right-k+1 = 1 → 1 >= 1 ✅ keep
  back=2, arr[2]=-1 > arr[3]=-3 → keep
  add 3 to back
  deque: [1, 2, 3]  (values: [3, -1, -3])
  front=1 → arr[1]=3  result=[3,3]

right=4: arr[4]=5
  front=1 → index 1, window start = 4-3+1=2 → 1 < 2 ❌ remove 1 from front!
  deque: [2, 3]
  back=3, arr[3]=-3 ≤ arr[4]=5 → remove 3
  back=2, arr[2]=-1 ≤ arr[4]=5 → remove 2
  deque empty → add 4
  deque: [4]  (values: [5])
  front=4 → arr[4]=5  result=[3,3,5]

right=5: arr[5]=3
  front=4 → index 4, window start = 5-3+1=3 → 4 >= 3 ✅ keep
  back=4, arr[4]=5 > arr[5]=3 → keep (5 is still bigger)
  add 5 to back
  deque: [4, 5]  (values: [5, 3])
  front=4 → arr[4]=5  result=[3,3,5,5]

right=6: arr[6]=6
  front=4 → window start = 6-3+1=4 → 4 >= 4 ✅ keep
  back=5, arr[5]=3 ≤ arr[6]=6 → remove 5
  back=4, arr[4]=5 ≤ arr[6]=6 → remove 4
  deque empty → add 6
  deque: [6]  (values: [6])
  front=6 → arr[6]=6  result=[3,3,5,5,6]

right=7: arr[7]=7
  front=6 → window start = 7-3+1=5 → 6 >= 5 ✅ keep
  back=6, arr[6]=6 ≤ arr[7]=7 → remove 6
  deque empty → add 7
  deque: [7]  (values: [7])
  front=7 → arr[7]=7  result=[3,3,5,5,6,7]

Answer: [3, 3, 5, 5, 6, 7] ✅
```

**Pseudo Code:**
```
deque = empty (stores indices, front=max side, back=min side)
result = []

for right from 0 to n-1:

    STEP 1: Remove from FRONT if outside window
    while deque not empty and deque.front < right - k + 1:
        deque.removeFront()

    STEP 2: Remove from BACK if value <= arr[right] (they're useless)
    while deque not empty and arr[deque.back] <= arr[right]:
        deque.removeBack()

    STEP 3: Add current index to back
    deque.addBack(right)

    STEP 4: Record answer when window is full
    if right >= k - 1:
        result.add(arr[deque.front()])    ← front is always the maximum

return result
```

**Complexity Analysis:**

| Time | Space | Reason                                                         |
|------|-------|----------------------------------------------------------------|
| O(n) | O(k)  | Each index added/removed at most once; deque holds at most k elements |

---

## 📊 Comparison Table

| Method                   | Time       | Space | Key Data Structure          |
|--------------------------|------------|-------|-----------------------------|
| Brute Force              | O(n × k)   | O(1)  | None                        |
| Max Heap                 | O(n log k) | O(k)  | Priority Queue              |
| **Monotonic Deque** ⭐   | **O(n)**   | **O(k)** | Deque (ArrayDeque)       |

---

## 💡 Key Insights

1. **Monotonic Deque = "Useless elements thrown out early"**  
   Any element smaller than the new incoming element (and older) can never be the max → discard.

2. **Front = current window's maximum** always (because deque is decreasing by value).

3. **Two removal rules:**
   - **Front removal** = element is **out of window** (index too old)
   - **Back removal** = element is **useless** (value too small)

4. **`<=` vs `<` when removing from back:**  
   Use `<=` (remove equal elements too) because we want the rightmost occurrence of the max  
   (more recent index stays longer in the window — better for future windows).

---

## 🔍 Edge Cases

| Input            | k | Output       | Why                        |
|------------------|---|--------------|----------------------------|
| `[5]`            | 1 | `[5]`        | Single element             |
| `[1,2,3,4,5]`   | 5 | `[5]`        | One window = whole array   |
| `[5,4,3,2,1]`   | 3 | `[5,4,3]`    | Decreasing — max is always leftmost |
| `[1,1,1,1]`     | 2 | `[1,1,1]`    | All same                   |

---

## ⚠️ Common Mistakes

| Mistake                                          | Fix                                                  |
|--------------------------------------------------|------------------------------------------------------|
| Storing values (not indices) in deque            | Store **indices** — needed to check window bounds    |
| Forgetting to remove from front when out of window | Always check front's index vs `right - k + 1`     |
| Using `<` instead of `<=` when removing from back | Use `<=` to discard equal (older) elements          |
| Recording result before window is full           | Only record when `right >= k - 1`                   |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
