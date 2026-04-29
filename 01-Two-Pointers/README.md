# 🎯 Pattern 01 — Two Pointers

## Core Idea
Use **two indices** moving through the array (or string) to reduce O(n²) brute force to **O(n)** by avoiding redundant work.

## When to Use
- Sorted array (or can be sorted)
- Searching for **pairs / triplets** with target sum
- **Palindrome / reversal** problems
- **Container / area** problems

## Common Variants
| Variant                | Movement                      | Example                            |
|------------------------|-------------------------------|------------------------------------|
| Opposite ends          | `lo` from start, `hi` from end | Pair sum, container with most water |
| Same direction         | `slow` + `fast` both forward  | Remove duplicates, move zeroes     |
| Circular (rotated arr) | Indices wrap with `% n`       | Pair sum in rotated array          |

---

## Problems

| #  | Problem                                                              | Difficulty | Status | Companies                            |
|----|----------------------------------------------------------------------|------------|--------|--------------------------------------|
| 1  | [Pair Sum in Sorted & Rotated Array](./001-Pair-Sum-Sorted-Rotated)  | Medium     | ✅     | Microsoft, Google, Apple             |
| 2  | 3Sum                                                                 | Medium     | ⬜     | Adobe, Amazon, Microsoft             |
| 3  | Container With Most Water                                            | Medium     | ⬜     | Flipkart, Dunzo                      |
| 4  | Trapping Rain Water                                                  | Hard       | ⬜     | Samsung                              |
| 5  | Given Sum Pair                                                       | Easy       | ⬜     | Infosys, Amazon, Flipkart            |
