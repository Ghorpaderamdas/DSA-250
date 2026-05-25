# Q4. Best Time to Buy and Sell Stock

**Difficulty:** Easy &nbsp;|&nbsp; **Pattern:** Sliding Window / Two Pointers &nbsp;|&nbsp; **Companies:** Amazon, Google, Facebook, Microsoft, Goldman Sachs

🔗 [LeetCode #121](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/)

---

## 📝 Problem

You are given an array `prices[]` where `prices[i]` is the **price of a stock on day `i`**.

- You can **buy on one day** and **sell on a later day** (you must buy before you sell).
- You want to **maximize your profit**.
- Return the **maximum profit** possible. If no profit can be made, return `0`.

### Examples

| Input                      | Output | Buy Day | Sell Day | Why                       |
|----------------------------|--------|---------|----------|---------------------------|
| `[7, 1, 5, 3, 6, 4]`      | 5      | Day 2 (price=1) | Day 5 (price=6) | 6 - 1 = 5 ✅ |
| `[7, 6, 4, 3, 1]`         | 0      | —       | —        | Prices always fall — no profit |
| `[2, 4, 1]`               | 2      | Day 1 (price=2) | Day 2 (price=4) | 4 - 2 = 2 ✅ |
| `[1, 2]`                  | 1      | Day 1 (price=1) | Day 2 (price=2) | 2 - 1 = 1 ✅ |

---

## ⚠️ Key Rules

```
✅ You MUST buy before you sell  (buy on day i, sell on day j where j > i)
✅ Only ONE transaction (one buy + one sell)
❌ You CANNOT sell before you buy
❌ You CANNOT buy and sell on the same day (though same day gives profit = 0)
```

---

## 🧠 Approaches

---

### Method 1 — Brute Force (O(n²))

**How it works:**
1. Use **two nested loops** — pick every pair of days (buy day `i`, sell day `j`)
2. Compute `profit = prices[j] - prices[i]`  (only when `j > i`)
3. Track the **maximum profit** across all valid pairs

**Pseudo Code:**
```
maxProfit = 0
for i from 0 to n-1:              ← buy day
    for j from i+1 to n-1:        ← sell day (must be AFTER buy day)
        profit = prices[j] - prices[i]
        maxProfit = max(maxProfit, profit)
return maxProfit
```

**Dry Run** — `prices = [7, 1, 5, 3, 6, 4]`

```
i=0 (buy@7):
    j=1: sell@1  → 1-7  = -6   maxProfit=0
    j=2: sell@5  → 5-7  = -2   maxProfit=0
    j=3: sell@3  → 3-7  = -4   maxProfit=0
    j=4: sell@6  → 6-7  = -1   maxProfit=0
    j=5: sell@4  → 4-7  = -3   maxProfit=0

i=1 (buy@1):
    j=2: sell@5  → 5-1  =  4   maxProfit=4
    j=3: sell@3  → 3-1  =  2   maxProfit=4
    j=4: sell@6  → 6-1  =  5   maxProfit=5  ← new max!
    j=5: sell@4  → 4-1  =  3   maxProfit=5

i=2 (buy@5):
    j=3: sell@3  → 3-5  = -2   maxProfit=5
    j=4: sell@6  → 6-5  =  1   maxProfit=5
    j=5: sell@4  → 4-5  = -1   maxProfit=5

... (remaining pairs give ≤ 5)

Answer: 5 ✅  (Buy on day 2 at price 1, sell on day 5 at price 6)
```

**Why it's slow:** For `n = 100,000` days: `n² / 2 = 5,000,000,000` comparisons — too slow!

**Complexity Analysis:**

| Time  | Space | Reason                                 |
|-------|-------|----------------------------------------|
| O(n²) | O(1)  | Two nested loops; no extra memory      |

---

### Method 2 — Better: Kadane's Variant using Difference Array (O(n))

**Idea:** Convert the price array into a **daily change** array:
```
change[i] = prices[i] - prices[i-1]     (profit/loss from day i-1 to day i)
```
Then find the **maximum subarray sum** of the change array using Kadane's algorithm.

**Why does this work?**
```
prices = [7, 1, 5, 3, 6, 4]

Buying on day i and selling on day j:
profit = prices[j] - prices[i]
       = (prices[j] - prices[j-1]) + (prices[j-1] - prices[j-2]) + ... + (prices[i+1] - prices[i])
       = change[j] + change[j-1] + ... + change[i+1]
       = sum of changes from day i+1 to day j

So: max profit = max subarray sum of the change array!
```

**Change Array:**
```
prices = [ 7,  1,  5,  3,  6,  4]
change = [  , -6, +4, -2, +3, -2]
                ↑
            index 1 = prices[1] - prices[0] = 1 - 7 = -6
```

**Kadane's Algorithm — max subarray sum:**
```
currentSum = 0
maxSum = 0

for each value in change[]:
    currentSum += value
    if currentSum < 0:
        currentSum = 0    ← reset (don't carry a loss)
    maxSum = max(maxSum, currentSum)
```

**Dry Run** — `change = [-6, +4, -2, +3, -2]`

```
value=-6: currentSum=0+(-6)=-6 → reset to 0   maxSum=0
value=+4: currentSum=0+4=4                      maxSum=4
value=-2: currentSum=4-2=2                      maxSum=4
value=+3: currentSum=2+3=5                      maxSum=5  ← new max!
value=-2: currentSum=5-2=3                      maxSum=5

Answer: 5 ✅
```

**Complexity Analysis:**

| Time | Space | Reason                                       |
|------|-------|----------------------------------------------|
| O(n) | O(n)  | One pass for change array + one pass Kadane's |

> Can be done in O(1) space by computing change on the fly without storing the array.

---

### Method 3 — Minimum Price Tracking (O(n)) ⭐

**Idea:**
- Track the **minimum price seen so far** as we scan left to right
- At every day, compute `profit = current price - minPrice`
- Track the **maximum profit** seen

**Why is this correct?**
```
To maximize profit on sell day j:
    profit = prices[j] - (minimum price on any day before j)

So at every sell day j, the best buy day is the cheapest day we've seen so far.
```

**Pseudo Code:**
```
minPrice = infinity
maxProfit = 0

for each price in prices[]:
    if price < minPrice:
        minPrice = price          ← found a cheaper buy day
    else:
        profit = price - minPrice ← potential sell day
        maxProfit = max(maxProfit, profit)

return maxProfit
```

**Dry Run** — `prices = [7, 1, 5, 3, 6, 4]`

```
price=7: minPrice=7    profit=0        maxProfit=0
price=1: minPrice=1    profit=0        maxProfit=0   (new min found!)
price=5: minPrice=1    profit=5-1=4    maxProfit=4
price=3: minPrice=1    profit=3-1=2    maxProfit=4
price=6: minPrice=1    profit=6-1=5    maxProfit=5   ← new max!
price=4: minPrice=1    profit=4-1=3    maxProfit=5

Answer: 5 ✅  (Buy at 1, sell at 6)
```

**Dry Run** — `prices = [7, 6, 4, 3, 1]` (always falling)

```
price=7: minPrice=7  profit=0   maxProfit=0
price=6: minPrice=6  profit=0   maxProfit=0
price=4: minPrice=4  profit=0   maxProfit=0
price=3: minPrice=3  profit=0   maxProfit=0
price=1: minPrice=1  profit=0   maxProfit=0

Answer: 0 ✅  (No profitable transaction possible)
```

**Complexity Analysis:**

| Time | Space | Reason                                             |
|------|-------|----------------------------------------------------|
| O(n) | O(1)  | Single pass; only two variables (minPrice, maxProfit) |

---

### Method 4 — Sliding Window / Two Pointers (O(n)) ⭐⭐

**Idea:**  
Use two pointers — `left` (buy day) and `right` (sell day):
- `left` always points to the **cheapest day seen so far** (potential buy day)
- `right` scans forward one step at a time (potential sell day)
- If `prices[right] > prices[left]` → valid profit, update `maxProfit`
- If `prices[right] < prices[left]` → found a **cheaper buy day**, move `left = right`
- Always move `right` forward

**This is sliding window because:**
```
- Window = [left ... right] = [buy day ... sell day]
- Window shrinks to size 1 when a new minimum is found (left = right)
- Window expands as right moves forward
- Answer = maximum window "value" (profit) seen
```

**Visual** — `prices = [7, 1, 5, 3, 6, 4]`

```
Index:   0  1  2  3  4  5
Prices:  7  1  5  3  6  4

left=0, right=1:
  prices[right]=1 < prices[left]=7 → new min found!
  move left=1 (no profit, buy day shifts)
  [7] (1) 5  3  6  4

left=1, right=2:
  prices[right]=5 > prices[left]=1 → profit = 5-1 = 4  maxProfit=4
  [ 7  1→←5] 3  6  4

left=1, right=3:
  prices[right]=3 > prices[left]=1 → profit = 3-1 = 2  maxProfit=4
  [ 7  1←----→3] 6  4

left=1, right=4:
  prices[right]=6 > prices[left]=1 → profit = 6-1 = 5  maxProfit=5 ✅
  [ 7  1←--------→6] 4

left=1, right=5:
  prices[right]=4 > prices[left]=1 → profit = 4-1 = 3  maxProfit=5
  [ 7  1←------------→4]

Answer: 5 ✅
```

**Visual** — `prices = [3, 1, 4, 1, 5]`

```
L=0, R=1: prices[1]=1 < prices[0]=3 → new min! L=1
           3 [1] 4  1  5

L=1, R=2: prices[2]=4 > prices[1]=1 → profit=3  maxProfit=3
           3 [1→←4] 1  5

L=1, R=3: prices[3]=1 = prices[1]=1 → new min (≤), L=3
           3  1  4 [1] 5
           (tie: we take the later one as new left — same effect)

L=3, R=4: prices[4]=5 > prices[3]=1 → profit=4  maxProfit=4
           3  1  4 [1→←5]

Answer: 4 ✅
```

**Pseudo Code:**
```
left = 0        ← buy day (cheapest day so far)
right = 1       ← sell day
maxProfit = 0

while right < n:
    if prices[right] > prices[left]:
        profit = prices[right] - prices[left]
        maxProfit = max(maxProfit, profit)
    else:
        left = right        ← new cheaper buy day found
    right++                 ← always move right forward

return maxProfit
```

**Complexity Analysis:**

| Time | Space | Reason                                                      |
|------|-------|-------------------------------------------------------------|
| O(n) | O(1)  | Single pass with two pointers; only 2-3 extra variables     |

---

## 📊 Comparison Table

| Method                      | Time  | Space | Key Idea                                         |
|-----------------------------|-------|-------|--------------------------------------------------|
| Brute Force                 | O(n²) | O(1)  | Try every buy-sell pair                          |
| Kadane's Variant            | O(n)  | O(n)  | Max subarray sum on difference array             |
| **Min Price Tracking** ⭐   | **O(n)** | **O(1)** | Track cheapest price seen; compute profit each day |
| **Sliding Window** ⭐⭐     | **O(n)** | **O(1)** | left=buy, right=sell; move left when cheaper day found |

> Methods 3 and 4 are the same complexity — both are O(n) O(1). Method 4 (sliding window) makes the "two pointer" intuition explicit.

---

## 💡 Key Insights

1. **You can never sell before you buy** → that's why left pointer (buy) always stays behind right pointer (sell).

2. **To maximize profit on any sell day `j`:**  
   → You want the **cheapest possible buy day before `j`**  
   → Greedy: track the running minimum price.

3. **When a new minimum is found** `(prices[right] < prices[left])`:  
   → It's never profitable to buy at `left` and sell after this new minimum.  
   → Because: if `prices[right] < prices[left]` and `prices[future] > prices[left]`,  
     &emsp; then `prices[future] - prices[right] > prices[future] - prices[left]`  
   → So the new minimum is always a better buy candidate.

4. **Return 0 for no profit:**  
   → Initialize `maxProfit = 0` (not `Integer.MIN_VALUE`)  
   → If prices only fall, we return 0 (the problem says "if no profit possible → return 0")

---

## 🔍 Edge Cases

| Input         | Output | Why                                          |
|---------------|--------|----------------------------------------------|
| `[1]`         | 0      | Only 1 day — can't buy AND sell              |
| `[1, 2]`      | 1      | Buy at 1, sell at 2                          |
| `[2, 1]`      | 0      | Can't sell before buying — price only falls  |
| `[7,6,4,3,1]` | 0      | Always decreasing — no profitable transaction|
| `[1,1,1,1]`   | 0      | All same price — profit = 0                  |
| `[3,1,4,1,5]` | 4      | Buy at 1 (day 2 or day 4), sell at 5         |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
