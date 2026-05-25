# Q7. Count Distinct Elements in Every Window of Size K

**Difficulty:** Medium &nbsp;|&nbsp; **Pattern:** Sliding Window (Fixed Size) &nbsp;|&nbsp; **Companies:** Amazon, Samsung, Paytm

🔗 [GeeksForGeeks](https://www.geeksforgeeks.org/dsa/count-distinct-elements-in-every-window-of-size-k/)

---

## 📝 Problem

Given an array `arr[]` and a number `k`, print the **count of distinct elements** in every window of size `k`.

### Examples

| Input                     | k | Output        | Why                                                       |
|---------------------------|---|---------------|-----------------------------------------------------------|
| `[1, 2, 1, 3, 4, 2, 3]`  | 4 | `[3, 4, 4, 3]`| Windows: {1,2,1,3}→3, {2,1,3,4}→4, {1,3,4,2}→4, {3,4,2,3}→3 |
| `[1, 2, 4, 4]`            | 2 | `[2, 2, 1]`   | {1,2}→2, {2,4}→2, {4,4}→1                               |
| `[1, 1, 1, 1, 1]`         | 3 | `[1, 1, 1]`   | All windows contain only 1                               |

---

## 🪟 Pattern: Fixed Size Sliding Window

```
Window size k is GIVEN → FIXED SIZE window

4-Step Framework:
Step 1: Pattern? → Count distinct per window → Sliding Window ✅
Step 2: Fixed or Variable? → k is given → FIXED SIZE
Step 3: Starting window? → Process first k elements, count distinct
Step 4: Slide? → Remove outgoing (leftmost) element, add incoming (right) element
                 Update distinct count accordingly
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n × k))

**How it works:**
1. Loop through every window starting position
2. For each window, use a **HashSet** to count distinct elements
3. Record `set.size()` for that window

**Pseudo Code:**
```
result = []
for i from 0 to n-k:                   ← start of each window
    set = {}
    for j from i to i+k-1:             ← scan k elements
        set.add(arr[j])
    result.add(set.size())
return result
```

**Dry Run** — `arr = [1, 2, 1, 3, 4, 2, 3]`, `k = 4`

```
i=0: window=[1,2,1,3] → set={1,2,3}     → distinct=3  result=[3]
i=1: window=[2,1,3,4] → set={2,1,3,4}   → distinct=4  result=[3,4]
i=2: window=[1,3,4,2] → set={1,3,4,2}   → distinct=4  result=[3,4,4]
i=3: window=[3,4,2,3] → set={3,4,2}     → distinct=3  result=[3,4,4,3]

Answer: [3, 4, 4, 3] ✅
```

**Complexity Analysis:**

| Time  | Space | Reason                                          |
|-------|-------|-------------------------------------------------|
| O(nk) | O(k)  | Outer loop × inner scan; set holds at most k elements |

---

### Method 2 — Optimal: Sliding Window + HashMap (O(n)) ⭐

**Idea:** Instead of rebuilding the set from scratch for each window,  
**slide** the window by:
1. **Add** the new incoming element (from right): increment its frequency
2. **Remove** the outgoing element (from left): decrement frequency; if it hits 0 → one less distinct element
3. `map.size()` always gives the current distinct count in O(1)

**Key Rule:**
```
When outgoing element's frequency drops to 0:
    → it's no longer in the window at all
    → remove it from the map
    → distinct count decreases

When incoming element is brand new (not in map):
    → put it in map with freq=1
    → distinct count increases automatically (map grows)
```

**Visual** — `arr = [1, 2, 1, 3, 4, 2, 3]`, `k = 4`

```
Step 1: Build first window [1, 2, 1, 3]
  add 1 → map={1:1}
  add 2 → map={1:1, 2:1}
  add 1 → map={1:2, 2:1}
  add 3 → map={1:2, 2:1, 3:1}
  distinct = map.size() = 3   result=[3]

Step 2: Slide to [2, 1, 3, 4]
  ADD incoming arr[4]=4  → map={1:2, 2:1, 3:1, 4:1}
  REMOVE outgoing arr[0]=1 → freq[1]=2-1=1 > 0 → keep in map
  map = {1:1, 2:1, 3:1, 4:1}
  distinct = map.size() = 4   result=[3, 4]

Step 3: Slide to [1, 3, 4, 2]
  ADD incoming arr[5]=2  → map={1:1, 2:2, 3:1, 4:1}
  REMOVE outgoing arr[1]=2 → freq[2]=2-1=1 > 0 → keep in map
  map = {1:1, 2:1, 3:1, 4:1}
  distinct = map.size() = 4   result=[3, 4, 4]

Step 4: Slide to [3, 4, 2, 3]
  ADD incoming arr[6]=3  → map={1:1, 2:1, 3:2, 4:1}
  REMOVE outgoing arr[2]=1 → freq[1]=1-1=0 → REMOVE from map!
  map = {2:1, 3:2, 4:1}
  distinct = map.size() = 3   result=[3, 4, 4, 3]

Answer: [3, 4, 4, 3] ✅
```

**Pseudo Code:**
```
map = {}   result = []

Step 1: Build the FIRST window [0 .. k-1]
for i from 0 to k-1:
    map[arr[i]]++

result.add(map.size())   ← first window's distinct count

Step 2: SLIDE the window from i=k to n-1
for i from k to n-1:
    ADD incoming element:
        map[arr[i]]++

    REMOVE outgoing element (arr[i-k]):
        map[arr[i-k]]--
        if map[arr[i-k]] == 0:
            remove arr[i-k] from map   ← no longer in window

    result.add(map.size())   ← distinct count for this window

return result
```

**Complexity Analysis:**

| Time | Space | Reason                                                    |
|------|-------|-----------------------------------------------------------|
| O(n) | O(k)  | Each element added/removed once; map holds at most k entries |

---

## 📊 Comparison Table

| Method                 | Time  | Space | Key Data Structure |
|------------------------|-------|-------|--------------------|
| Brute Force            | O(nk) | O(k)  | HashSet per window |
| **Sliding Window + HashMap** ⭐ | **O(n)** | **O(k)** | HashMap (freq map) |

---

## 💡 Key Insights

1. **`map.size()` = distinct count** at any time — this is O(1), no need to recount.

2. **Remove from map when frequency hits 0** — this is the critical step.  
   If you just decrement but don't remove, `map.size()` will be wrong.

3. **Fixed window** → always remove `arr[i - k]` when adding `arr[i]`.  
   The outgoing element is always exactly `k` positions behind the incoming one.

4. **Order of operations:** Add first, then remove (or remove first, then add — both work,  
   but add-then-remove is easier to reason about for the first window transition).

---

## 🔍 Edge Cases

| Input              | k | Output    | Why                                        |
|--------------------|---|-----------|--------------------------------------------|
| `[1,1,1,1]`       | 2 | `[1,1,1]` | All same — always 1 distinct               |
| `[1,2,3,4]`       | 4 | `[4]`     | One window = whole array, all distinct     |
| `[1,2,3,4]`       | 1 | `[1,1,1,1]`| Each window is a single element → 1 distinct |

---

## ⚠️ Common Mistakes

| Mistake                                   | Fix                                               |
|-------------------------------------------|---------------------------------------------------|
| Not removing key when frequency hits 0    | Always `map.remove(key)` when `map.get(key) == 0` |
| Using a Set instead of Map                | Set can't track duplicates — use a frequency Map  |
| Off-by-one in outgoing index              | Outgoing element = `arr[i - k]` when `i` is the new `right` |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
