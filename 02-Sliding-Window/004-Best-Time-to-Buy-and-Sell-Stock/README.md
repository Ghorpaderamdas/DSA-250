# Q4. Best Time to Buy and Sell Stock

**Difficulty:** Easy &nbsp;|&nbsp; **Pattern:** Sliding Window / Two Pointers &nbsp;|&nbsp; **Companies:** Amazon, Google, Facebook, Microsoft, Goldman Sachs

🔗 [LeetCode #121](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/)

---

## 🏪 Real-World Story First

> Imagine you're looking at a newspaper that shows the price of one stock for the next 6 days.
> You can **buy on ONE day** and **sell on ONE later day**.
> You want to make the **most money possible**.
> If you can't make any profit, just don't trade (return 0).

```
  Day:     1    2    3    4    5    6
  Price:  $7   $1   $5   $3   $6   $4

  Question: On which day should you BUY and which day should you SELL?

  Answer:  BUY on Day 2 ($1)  →  SELL on Day 5 ($6)
           Profit = $6 - $1 = $5  💰
```

**The KEY constraint:** You must BUY before you SELL.  
You're looking at past data — you can't go back in time!

---

## 📝 Problem Statement

Given an array `prices[]` where `prices[i]` = stock price on day `i`:
- **Buy** on one day, **sell** on a **later day**
- **Maximize profit** = `prices[sellDay] - prices[buyDay]`
- If no profit possible → return **`0`**

### Examples

| Input                 | Output | Best Action                         |
|-----------------------|--------|-------------------------------------|
| `[7, 1, 5, 3, 6, 4]` | `5`    | Buy at 1 (Day 2), Sell at 6 (Day 5) |
| `[7, 6, 4, 3, 1]`    | `0`    | Prices always fall — don't trade    |
| `[2, 4, 1]`          | `2`    | Buy at 2 (Day 1), Sell at 4 (Day 2) |

---

## 📈 Stock Price Chart — Visual Understanding

```
  prices = [7, 1, 5, 3, 6, 4]

  Price
    7 │  █
    6 │  █                    █
    5 │  █             █      █
    4 │  █             █      █    █
    3 │  █             █   █  █    █
    2 │  █             █   █  █    █
    1 │  █    █        █   █  █    █
      └──────────────────────────────── Days
         D1   D2   D3  D4  D5  D6
        ($7) ($1) ($5)($3)($6)($4)

  ════════════════════════════════════
  Best trade: BUY at D2($1), SELL at D5($6)
  
            BUY here           SELL here
               ↓                  ↓
    7 │  █    ↓                   ↓
    6 │  █   [ ]                [█]  ← Sell $6
    5 │  █   [ ]       █        [█]
    4 │  █   [ ]       █        [█]  █
    3 │  █   [ ]       █    █   [█]  █
    2 │  █   [ ]       █    █   [█]  █
    1 │  █   [█] ← Buy($1)  █   [█]  █
      └──────────────────────────────
         D1   D2   D3   D4   D5   D6
  
         Profit = $6 - $1 = $5 💰
  ════════════════════════════════════
```

---

## ⚠️ The Golden Rules

```
  ┌─────────────────────────────────────────────────┐
  │                                                 │
  │  ✅ Buy day MUST come BEFORE sell day           │
  │  ✅ Only ONE buy + ONE sell allowed             │
  │  ✅ Return 0 if no profit is possible           │
  │                                                 │
  │  ❌ Cannot sell on a day before buying          │
  │  ❌ Cannot do multiple transactions             │
  │                                                 │
  └─────────────────────────────────────────────────┘

  Timeline view:
  
  Day:  1    2    3    4    5    6
        ←────── can BUY here ──────→
                    ↕
  If you BUY on Day 3, you can only SELL on Day 4, 5, or 6
  
             [BUY]──────────────→[SELL]
              D3                  D4/D5/D6
```

---

## 🧠 All Approaches

---

## Method 1 — Brute Force 🐢 (O(n²))

### 💡 Idea in Plain English

> "Try EVERY possible combination of (buy day, sell day).  
>  Calculate profit for each pair. Keep the best."

It's like checking every pair manually:
```
  Try buy D1, sell D2 → profit = 1 - 7 = -6   (loss!)
  Try buy D1, sell D3 → profit = 5 - 7 = -2   (loss!)
  Try buy D1, sell D4 → profit = 3 - 7 = -4   (loss!)
  ...keep going...
  Try buy D2, sell D5 → profit = 6 - 1 = 5    (best!)
  ...
```

### 🖼️ All Pairs Visualized

```
  prices = [7, 1, 5, 3, 6, 4]
  
  All valid (buy → sell) pairs where buy comes BEFORE sell:
  
  D1→D2: 1-7 = -6  ❌        D2→D3: 5-1 = +4  ✅
  D1→D3: 5-7 = -2  ❌        D2→D4: 3-1 = +2  ✅
  D1→D4: 3-7 = -4  ❌        D2→D5: 6-1 = +5  ✅ ← BEST
  D1→D5: 6-7 = -1  ❌        D2→D6: 4-1 = +3  ✅
  D1→D6: 4-7 = -3  ❌        D3→D4: 3-5 = -2  ❌
                              D3→D5: 6-5 = +1  ✅
                              D3→D6: 4-5 = -1  ❌
                              D4→D5: 6-3 = +3  ✅
                              D4→D6: 4-3 = +1  ✅
                              D5→D6: 4-6 = -2  ❌
  
  Maximum profit = 5  (buy D2@$1, sell D5@$6) ✅
```

### 📝 Pseudo Code

```
maxProfit = 0

for i from 0 to n-1:              ← try every BUY day
    for j from i+1 to n-1:        ← try every SELL day after buy day
        profit = prices[j] - prices[i]
        maxProfit = max(maxProfit, profit)

return maxProfit
```

### 🔢 Step-by-Step Dry Run — `[7, 1, 5, 3, 6, 4]`

```
  i=0 (BUY @ $7):
    j=1: $1 - $7 = -6  → maxProfit stays 0
    j=2: $5 - $7 = -2  → maxProfit stays 0
    j=3: $3 - $7 = -4  → maxProfit stays 0
    j=4: $6 - $7 = -1  → maxProfit stays 0
    j=5: $4 - $7 = -3  → maxProfit stays 0

  i=1 (BUY @ $1):
    j=2: $5 - $1 = +4  → maxProfit = 4
    j=3: $3 - $1 = +2  → maxProfit = 4
    j=4: $6 - $1 = +5  → maxProfit = 5  ⬅ NEW BEST!
    j=5: $4 - $1 = +3  → maxProfit = 5

  i=2 (BUY @ $5):
    j=3: $3 - $5 = -2  → maxProfit = 5
    j=4: $6 - $5 = +1  → maxProfit = 5
    j=5: $4 - $5 = -1  → maxProfit = 5
  ...
  
  ✅ Answer: 5
```

### ❌ Why It's Too Slow

```
  For n = 100,000 days:
  
  Total pairs checked = n × (n-1) / 2
                      = 100,000 × 99,999 / 2
                      = ~5,000,000,000 operations  ← 5 BILLION! 🐢

  At 10⁸ operations/second → takes ~50 seconds. Way too slow!
```

### ⏱️ Complexity

| Time  | Space | Reason                            |
|-------|-------|-----------------------------------|
| O(n²) | O(1)  | Two nested loops, no extra memory |

---

## Method 2 — Kadane's Variant 🧮 (O(n))

### 💡 Idea in Plain English

> "Convert the prices into daily GAINS and LOSSES.  
>  Then find the best streak of consecutive gains."

### 🖼️ Price → Daily Change Conversion

```
  prices = [ 7,  1,  5,  3,  6,  4]
             D1  D2  D3  D4  D5  D6

  Daily change = today's price - yesterday's price
  
  D2-D1: 1-7  = -6  (price FELL by 6)
  D3-D2: 5-1  = +4  (price ROSE by 4)
  D4-D3: 3-5  = -2  (price FELL by 2)
  D5-D4: 6-3  = +3  (price ROSE by 3)
  D6-D5: 4-6  = -2  (price FELL by 2)

  change = [-6, +4, -2, +3, -2]

  On a number line:
  -6         +4    -2    +3    -2
  ←─────  ──→  ←─  ──→  ←─
  D1→D2  D2→D3 D3→D4 D4→D5 D5→D6
```

### 🔑 The Key Insight

```
  BUY on Day 2, SELL on Day 5:
  
  Profit = price[D5] - price[D2]
         = 6 - 1
         = (6-3) + (3-5) + (5-1)        ← chain of daily changes
         = change[D5] + change[D4] + change[D3]
         = +3  +  -2  +  +4
         = +5 ✅
  
  ┌─────────────────────────────────────────────────────────┐
  │  Profit of buying on Day i and selling on Day j =      │
  │  SUM of daily changes from Day i+1 to Day j            │
  │                                                         │
  │  → Max profit = MAX SUBARRAY SUM of change array!      │
  └─────────────────────────────────────────────────────────┘
```

### 🖼️ Kadane's Algorithm on Change Array

```
  change = [-6, +4, -2, +3, -2]

  Kadane's rule:
    → Keep adding to currentSum
    → If currentSum goes negative → RESET to 0 (don't carry a loss!)
    → Track the maximum currentSum seen

  Step-by-step:

  ┌────────┬────────────┬─────────────────────────────────────┬─────────┐
  │ Change │ currentSum │ Action                              │ maxProfit│
  ├────────┼────────────┼─────────────────────────────────────┼─────────┤
  │  -6    │  0+(-6)=-6 │ Negative! Reset to 0               │    0    │
  │  +4    │  0+4 = 4   │ Growing!                            │    4    │
  │  -2    │  4+(-2)= 2 │ Still positive, keep going          │    4    │
  │  +3    │  2+3 = 5   │ NEW BEST! 🎉                        │    5    │
  │  -2    │  5+(-2)= 3 │ Still positive                      │    5    │
  └────────┴────────────┴─────────────────────────────────────┴─────────┘
  
  ✅ Answer: 5

  Why reset at -6?
  Starting a buy-sell on Day 1→Day 2 loses money.
  Better to start fresh from Day 2 onwards! Don't drag a loss.
```

### ⏱️ Complexity

| Time | Space | Reason                             |
|------|-------|------------------------------------|
| O(n) | O(1)  | One pass; compute change on-the-fly |

---

## Method 3 — Track the Minimum Price 🏔️ (O(n)) ⭐

### 💡 Idea in Plain English

> "Scan day by day. At any sell day, the best profit comes from  
>  buying at the CHEAPEST price seen SO FAR before that day."
>
> Keep a running `minPrice`. At each day:
> - If today is cheaper than minPrice → update minPrice (potential new buy day)
> - Otherwise → compute `profit = today - minPrice` → update maxProfit

### 🖼️ The "Valley to Peak" Concept

```
  The best profit = height of the tallest "valley to peak" gap
  where the VALLEY is to the LEFT of the PEAK.
  
  prices = [7, 1, 5, 3, 6, 4]
  
   7 │  █
   6 │  █                    █  ← PEAK ($6)
   5 │  █             █      █
   4 │  █             █      █    █
   3 │  █             █   █  █    █
   2 │  █             █   █  █    █
   1 │  █    █        █   █  █    █
     └──────────────────────────────
          D1   D2   D3  D4  D5  D6
  
              ↑ VALLEY ($1)      ↑ PEAK ($6)
              └────── gap ───────┘
                  = 6 - 1 = 5 💰 (Best!)
```

### 🔢 Step-by-Step Dry Run — `[7, 1, 5, 3, 6, 4]`

```
  ┌──────┬────────────────────┬────────────────────────────────┬───────────┐
  │ Price│ minPrice (cheapest │ Profit = Price - minPrice      │ maxProfit │
  │      │ seen so far)       │                                │           │
  ├──────┼────────────────────┼────────────────────────────────┼───────────┤
  │  $7  │  7  (new low!)     │  0 (can't sell on buy day)     │     0     │
  │  $1  │  1  (new low! 📉)  │  0 (just updated min, skip)   │     0     │
  │  $5  │  1  (no change)    │  5 - 1 = 4                     │     4     │
  │  $3  │  1  (no change)    │  3 - 1 = 2                     │     4     │
  │  $6  │  1  (no change)    │  6 - 1 = 5  ← NEW BEST! 💰    │     5     │
  │  $4  │  1  (no change)    │  4 - 1 = 3                     │     5     │
  └──────┴────────────────────┴────────────────────────────────┴───────────┘
  
  ✅ Answer: 5  (Buy at $1, Sell at $6)
```

### 🖼️ Visual: minPrice "Floor" Moving Down

```
  prices =  7    1    5    3    6    4
  
  Imagine a "floor" that can only move DOWN (never up):
  
  D1: floor=7  ──────
                       \
  D2: floor=1  ─────── \ ──────────────────
                          ↓
                         $1 (new floor!)
  
  At each day, profit = (current price) - (floor level)
  
  D3: profit = 5-1 = 4
  D4: profit = 3-1 = 2
  D5: profit = 6-1 = 5  ← BEST 🏆
  D6: profit = 4-1 = 3
```

### 🔢 Dry Run — Falling Prices `[7, 6, 4, 3, 1]`

```
  prices = [7, 6, 4, 3, 1]  (always falling)
  
  ┌──────┬──────────┬──────────────────────┬───────────┐
  │ Price│ minPrice │ Profit               │ maxProfit │
  ├──────┼──────────┼──────────────────────┼───────────┤
  │  $7  │  7       │  0                   │     0     │
  │  $6  │  6 📉    │  0                   │     0     │
  │  $4  │  4 📉    │  0                   │     0     │
  │  $3  │  3 📉    │  0                   │     0     │
  │  $1  │  1 📉    │  0                   │     0     │
  └──────┴──────────┴──────────────────────┴───────────┘
  
  Every day is a new minimum. minPrice always drops.
  Profit is always 0 or negative → return 0 ✅
```

### 📝 Pseudo Code

```
minPrice  = +∞    ← start with impossibly high price
maxProfit = 0     ← assume no trade initially

for each price in prices[]:
    
    if price < minPrice:
        minPrice = price      ← found a cheaper BUY day!
    else:
        profit = price - minPrice
        maxProfit = max(maxProfit, profit)

return maxProfit
```

### ⏱️ Complexity

| Time | Space | Reason                                             |
|------|-------|----------------------------------------------------|
| O(n) | O(1)  | Single scan left-to-right; only 2 variables needed |

---

## Method 4 — Sliding Window / Two Pointers 🪟 (O(n)) ⭐⭐

### 💡 Idea in Plain English

> Use TWO pointers:
> - `L` (Left)  = **BUY pointer** — points to the cheapest day seen so far
> - `R` (Right) = **SELL pointer** — scans forward one step at a time
>
> At each step:
> - If `prices[R] > prices[L]` → profitable! Compute profit, move R forward
> - If `prices[R] < prices[L]` → found a cheaper buy day! Move L to R (new window start)
> - Always move R forward

### 🖼️ What is the "Window" Here?

```
  Window = [L ........ R]
           [BUY day .. SELL day]
  
  The window GROWS when we find profit.
  The window RESETS (L jumps to R) when we find a cheaper buy day.
  
  prices = [7, 1, 5, 3, 6, 4]
            ↑  ↑
            L  R   ← start here
```

### 🖼️ Step-by-Step Pointer Movement

```
  prices = [7, 1, 5, 3, 6, 4]
  Index:    0  1  2  3  4  5

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 0: Initialize  L=0, R=1,  maxProfit=0                    │
  └─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 1: L=0($7), R=1($1)                                      │
  │                                                                 │
  │   7  [1]  5   3   6   4                                        │
  │   L   R                                                         │
  │                                                                 │
  │  prices[R]=$1 < prices[L]=$7                                   │
  │  → $1 is CHEAPER than $7!                                       │
  │  → Better to BUY at R, so move L=R                             │
  │  → L=1,  maxProfit=0                                           │
  └─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 2: L=1($1), R=2($5)                                      │
  │                                                                 │
  │   7   1  [5]  3   6   4                                        │
  │       L   R                                                     │
  │       └───┘                                                     │
  │                                                                 │
  │  prices[R]=$5 > prices[L]=$1                                   │
  │  → Profitable! profit = 5 - 1 = 4                              │
  │  → maxProfit = 4                                               │
  │  → Move R forward                                              │
  └─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 3: L=1($1), R=3($3)                                      │
  │                                                                 │
  │   7   1   5  [3]  6   4                                        │
  │       L       R                                                 │
  │       └───────┘                                                 │
  │                                                                 │
  │  prices[R]=$3 > prices[L]=$1                                   │
  │  → profit = 3 - 1 = 2  (not better than 4)                    │
  │  → maxProfit stays 4                                           │
  │  → Move R forward                                              │
  └─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 4: L=1($1), R=4($6)   ← THE BEST TRADE! 🏆              │
  │                                                                 │
  │   7   1   5   3  [6]  4                                        │
  │       L           R                                             │
  │       └───────────┘                                             │
  │                                                                 │
  │  prices[R]=$6 > prices[L]=$1                                   │
  │  → profit = 6 - 1 = 5  ← NEW BEST! 💰                         │
  │  → maxProfit = 5                                               │
  │  → Move R forward                                              │
  └─────────────────────────────────────────────────────────────────┘

  ┌─────────────────────────────────────────────────────────────────┐
  │  STEP 5: L=1($1), R=5($4)                                      │
  │                                                                 │
  │   7   1   5   3   6  [4]                                       │
  │       L               R                                         │
  │       └───────────────┘                                         │
  │                                                                 │
  │  prices[R]=$4 > prices[L]=$1                                   │
  │  → profit = 4 - 1 = 3  (not better than 5)                    │
  │  → maxProfit stays 5                                           │
  │  → R has reached the end → STOP                               │
  └─────────────────────────────────────────────────────────────────┘

  ✅ Final Answer: maxProfit = 5
```

### 🤔 Why Move L to R When Price Drops?

```
  Suppose: prices[R] < prices[L]
  
  Example: L is at $7, R finds $1
  
  For any future sell day with price P:
  
      profit if we keep buying at L($7):  P - $7
      profit if we buy at R($1)        :  P - $1
  
      Since $1 < $7:
      P - $1 > P - $7  for ALL values of P!
  
      ┌─────────────────────────────────────────────────┐
      │  Buying at the LOWER price ALWAYS gives         │
      │  MORE profit for ANY future selling price.      │
      │  So we ALWAYS prefer the cheaper buy day.      │
      └─────────────────────────────────────────────────┘
  
  Example:
      Future sell price = $6
      Buy at $7 → profit = 6-7 = -1  (loss!)
      Buy at $1 → profit = 6-1 = +5  (gain!) ← clearly better
```

### 📝 Pseudo Code

```
L = 0          ← BUY pointer (cheapest day so far)
R = 1          ← SELL pointer
maxProfit = 0

while R < n:
    if prices[R] > prices[L]:              ← profitable trade
        profit = prices[R] - prices[L]
        maxProfit = max(maxProfit, profit)
    else:                                   ← found cheaper buy day
        L = R                              ← shift BUY pointer here
    R = R + 1                              ← always move SELL pointer forward

return maxProfit
```

### ⏱️ Complexity

| Time | Space | Reason                                                    |
|------|-------|-----------------------------------------------------------|
| O(n) | O(1)  | R visits each element once; L moves forward, never back   |

---

## 🔄 Second Example Walkthrough — `[3, 1, 4, 1, 5]`

```
  prices = [3, 1, 4, 1, 5]
  Index:    0  1  2  3  4

  Step 1: L=0($3), R=1($1)
          prices[R]=$1 < prices[L]=$3 → cheaper! L=1
  
  Step 2: L=1($1), R=2($4)
          prices[R]=$4 > prices[L]=$1 → profit=3  maxProfit=3
  
  Step 3: L=1($1), R=3($1)
          prices[R]=$1 = prices[L]=$1 → no profit, and $1 is tied
          L=3  (treating equal as "new cheaper or same" — move L)
  
  Step 4: L=3($1), R=4($5)
          prices[R]=$5 > prices[L]=$1 → profit=4  maxProfit=4
  
  ✅ Answer: 4  (Buy D2@$1 or D4@$1, Sell D5@$5)
```

---

## 📊 All Methods — Side-by-Side Comparison

```
  prices = [7, 1, 5, 3, 6, 4]

  ┌─────────────────────────┬────────┬───────┬───────────────────────────┐
  │ Method                  │  Time  │ Space │ Core Idea                 │
  ├─────────────────────────┼────────┼───────┼───────────────────────────┤
  │ Brute Force             │ O(n²)  │ O(1)  │ Try ALL (buy,sell) pairs  │
  │ Kadane's Variant        │ O(n)   │ O(1)  │ Max sum on change array   │
  │ Min Price Tracking    ⭐ │ O(n)   │ O(1)  │ Track running minimum     │
  │ Sliding Window        ⭐ │ O(n)   │ O(1)  │ L=buy, R=sell; two ptrs  │
  └─────────────────────────┴────────┴───────┴───────────────────────────┘
  
  ✅ Methods 3 & 4 are the same time/space — both optimal.
     Method 4 makes the "window" thinking explicit.
```

---

## 🔍 Edge Cases — With Diagrams

```
  Case 1: Only 1 day  →  prices = [5]
  ┌────────────────────────────┐
  │  Can't buy AND sell same   │
  │  day in one transaction    │
  │  → return 0                │
  └────────────────────────────┘

  Case 2: Always falling  →  prices = [5, 4, 3, 2, 1]
  
   5 │ █
   4 │ █  █
   3 │ █  █  █
   2 │ █  █  █  █
   1 │ █  █  █  █  █
     └──────────────
     D1 D2 D3 D4 D5
  
  Every day is a new low → L always chases R → profit always 0
  → return 0

  Case 3: Always rising  →  prices = [1, 2, 3, 4, 5]
  
   5 │             █
   4 │          █  █
   3 │       █  █  █
   2 │    █  █  █  █
   1 │ █  █  █  █  █
     └──────────────
     D1 D2 D3 D4 D5
  
  L stays at D1($1), R keeps finding higher prices
  Best sell = last day D5($5), profit = 5-1 = 4 ✅

  Case 4: All same  →  prices = [3, 3, 3, 3]
  No gain possible → profit = 0 ✅
```

---

## 💡 Key Takeaways (Remember These!)

```
  ┌──────────────────────────────────────────────────────────────┐
  │                                                              │
  │  1. To maximize profit on sell day j:                       │
  │     → Buy at the CHEAPEST price seen before day j           │
  │     → profit = prices[j] - minPriceSeenSoFar               │
  │                                                              │
  │  2. When prices[R] < prices[L]:                             │
  │     → L = R  (always buy at the cheaper price)             │
  │     → Buying cheaper is ALWAYS better for any future sell   │
  │                                                              │
  │  3. Initialize maxProfit = 0 (NOT Integer.MIN_VALUE)        │
  │     → If no profit possible, return 0 (not negative)        │
  │                                                              │
  │  4. This is a VARIABLE sliding window:                      │
  │     → No fixed size k                                        │
  │     → Window = [buyDay ... sellDay]                         │
  │     → Resets when a cheaper buy day is found                │
  │                                                              │
  └──────────────────────────────────────────────────────────────┘
```

---

## ⚠️ Common Mistakes

| ❌ Mistake | ✅ Fix |
|-----------|--------|
| `maxProfit = Integer.MIN_VALUE` | Use `maxProfit = 0` — return 0 if no trade |
| `prices[j] - prices[i]` without checking `j > i` | Outer loop `i`, inner loop starts at `i+1` |
| Moving L backward when L > R | L only moves forward — `L = Math.max(L, ...)` |
| Forgetting single-element case | Check `if n < 2 → return 0` |

---

## 🔁 Revision Tracker

- [ ] Rev 1 (after 3 days)
- [ ] Rev 2 (after 1 week)
- [ ] Rev 3 (after 3 weeks)
- [ ] Rev 4 (after 2 months)
