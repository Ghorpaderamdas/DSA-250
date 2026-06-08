# 🔗 Linked Lists — Beginner's Complete Notes

> **One read = full understanding.** Visual diagrams, tables, mind maps — sab kuch andar hai.

---

## 📋 Table of Contents

1. [What is a Linked List?](#1-what-is-a-linked-list)
2. [Array vs Linked List](#2-array-vs-linked-list)
3. [Big Picture — Mind Map](#3-big-picture--mind-map)
4. [3 Types of Linked Lists](#4-3-types-of-linked-lists)
5. [Singly Linked List — All Operations](#5-singly-linked-list--all-operations)
6. [Circular Linked List](#6-circular-linked-list)
7. [Doubly Linked List](#7-doubly-linked-list)
8. [Polynomial Representation](#8-polynomial-representation)
9. [Sparse Matrix](#9-sparse-matrix)
10. [Quick Cheatsheet](#10-quick-cheatsheet)

---

## 1. What is a Linked List?

> **One line:** A chain of boxes (nodes), where each box holds **data** + **address of next box**.

```
HEAD
 │
 ▼
┌──────┬──────┐     ┌──────┬──────┐     ┌──────┬──────┐
│  10  │ 1004 │────▶│  25  │ 1008 │────▶│  18  │ NULL │
└──────┴──────┘     └──────┴──────┘     └──────┴──────┘
 @1001                @1004               @1008 (last)
 [data] [next]
```

💡 **Think of it like a treasure hunt** — each clue (node) tells you where the next clue is. Last clue says `NULL` = hunt over.

### Node Structure in C

```c
struct node {
    int data;           // stores the value
    struct node *next;  // stores address of next node
};
typedef struct node* nptr;
```

### ✅ Advantages

| Feature | Why it helps |
|---|---|
| Dynamic size | Grows/shrinks at runtime — no pre-allocation needed |
| Easy Insert/Delete | No shifting of elements like arrays |
| Memory efficient | Allocates only when needed |
| Builds other DS | Used to make stacks, queues, trees, graphs |

### ❌ Disadvantages

| Problem | Impact |
|---|---|
| No random access | Must traverse from head — O(n) to reach any element |
| Extra memory | Each node stores a pointer too |
| No backward traversal | In singly LL — can only go forward |
| Cache unfriendly | Nodes scattered in RAM — slow for CPU cache |

---

## 2. Array vs Linked List

| Feature | Array | Linked List |
|---|---|---|
| Memory allocation | ❌ Static (compile time) | ✅ Dynamic (runtime) |
| Size | ❌ Fixed — must know in advance | ✅ Grows/shrinks freely |
| Access element | ✅ O(1) — index directly | ❌ O(n) — traverse one by one |
| Insert at beginning | ❌ O(n) — shift all elements | ✅ O(1) — just change pointers |
| Delete element | ❌ O(n) — shifting needed | ✅ O(1) if you have pointer |
| Memory per element | ✅ Compact — data only | ❌ Extra — pointer per node |
| Cache performance | ✅ Excellent — contiguous | ❌ Poor — scattered in RAM |
| Best used for | Sorting, searching, indexing | Stacks, queues, trees, graphs |

---

## 3. Big Picture — Mind Map

```
                        🔗 LINKED LISTS
                              │
        ┌─────────┬───────────┼───────────┬──────────┬──────────┐
        │         │           │           │          │          │
   🔹 Singly   🔄 Circular  ↔️ Doubly   ⚙️ Ops   📐 Poly   🔲 Sparse
      LL          LL          LL                               Matrix
        │         │           │           │          │          │
   data+next  last→head   prev+data    Insert   coeff+expo  Triplet /
   one dir    no NULL      +next       Delete   per node    Linked
              end         both dir     Display              repr
```

---

## 4. 3 Types of Linked Lists

### Type 1 — Singly Linked List
```
[1] ──▶ [2] ──▶ [3] ──▶ NULL
```
- Each node: `data + next`
- One direction only (forward)
- Last node → `next = NULL`

---

### Type 2 — Circular Linked List
```
[1] ──▶ [2] ──▶ [3] ──┐
 ▲                     │
 └─────────────────────┘
```
- Same as singly BUT last node → `next = HEAD` (not NULL)
- No real "end" — it loops forever
- Traverse stops when `p == head` again

---

### Type 3 — Doubly Linked List
```
NULL ◀─ [1] ⇄ [2] ⇄ [3] ─▶ NULL
```
- Each node: `prev + data + next`
- Can traverse both forward AND backward
- First node's `prev = NULL`, Last node's `next = NULL`

---

## 5. Singly Linked List — All Operations

### 5.1 Node Structure (self-referential)

```c
struct node {
    int data;
    struct node *next;   // pointer to same type = self-referential
};
// head = address of first node
// head->next = NULL means empty list
```

---

### 5.2 INSERTION (3 ways)

#### 📍 Insert at Beginning

```
BEFORE:  HEAD ──▶ [10] ──▶ [25] ──▶ NULL
AFTER:   HEAD ──▶ [5] ──▶ [10] ──▶ [25] ──▶ NULL
                   ↑
                  NEW
```

**Steps:**
```
1. new = malloc(sizeof(node))
2. new->data = x
3. new->next = head->next      ← new points to old first node
4. head->next = new            ← head now points to new node
```

---

#### 📍 Insert at End

```
BEFORE:  HEAD ──▶ [10] ──▶ [25] ──▶ NULL
AFTER:   HEAD ──▶ [10] ──▶ [25] ──▶ [99] ──▶ NULL
                                      ↑
                                     NEW
```

**Steps:**
```
1. new = malloc(sizeof(node))
2. new->data = x
3. new->next = NULL
4. Traverse till last node (temp->next == NULL)
5. temp->next = new            ← link last node to new
```

---

#### 📍 Insert at Specific Position (before/after a node)

**After a node:**
```
1. Find target node (temp)
2. new->next = temp->next
3. temp->next = new
```

**Before a node:**
```
1. Find node before target (prev)
2. new->next = prev->next      ← new points to target
3. prev->next = new            ← prev now points to new
```

---

### 5.3 DELETION (3 ways)

| Where | Steps | Edge Case |
|---|---|---|
| **From Beginning** | `head->next = head->next->next` then free old first | If only 1 node: `head->next = NULL` |
| **From End** | Use 2 pointers → traverse → `temp2->next = NULL` → free temp1 | If only 1 node: `head->next = NULL` |
| **Specific Node** | Find node + track prev → `prev->next = node->next` → free node | Check if first / last / only node |

#### Visual — Delete from End (2-pointer trick)

```
temp2    temp1
  │        │
  ▼        ▼
[H] ──▶ [10] ──▶ [25] ──▶ [18] ──▶ NULL
                  │         │
                temp2     temp1  ← stop when temp1->next == NULL
                  │
                  └── temp2->next = NULL  ← cut here
                      free(temp1)
```

> ⚠️ **Common mistake:** Always use 2 pointers for end/specific deletion. One (temp1) finds the target, other (temp2) tracks the previous node.

---

### 5.4 DISPLAY (Traversal)

```c
void displaylist(nptr h) {
    nptr p;
    if (h->next == NULL) {
        printf("empty list");
        return;
    }
    for (p = h->next; p != NULL; p = p->next)
        printf("%d\t", p->data);
}
```

**Flow:**
```
p = head->next
While p != NULL:
    print p->data
    p = p->next
```

---

### 5.5 SEARCH (Find Node)

```
p = head
While p->next != NULL:
    if p->next->data == target:
        print "found"
        return
    p = p->next
print "not found"
```

---

## 6. Circular Linked List

> Same as Singly LL — only ONE difference: **last node points back to HEAD**

```
*head                         node address
 1001                              ↓
  ▼                          ┌────────────┐
[10|1004] ──▶ [15|1008] ──▶ [22|1012] ──▶ [50|1001]
  ▲                                              │
  └──────────────────────────────────────────────┘
                    last→next = head (1001)
```

### Key Differences from Singly LL

| | Singly LL | Circular LL |
|---|---|---|
| Last node's next | `NULL` | `head` |
| Empty check | `head->next == NULL` | `head->next == head` |
| Traverse stop | `p != NULL` | `p != head` |
| Insert at end | `new->next = NULL` | `new->next = head` |

### Insert at Beginning (Circular)

```
1. new = malloc(sizeof(node))
2. new->data = x
3. new->next = head->next      ← new points to first real node
4. head->next = new            ← head points to new
   (for fresh empty: head->next = head)
```

### Delete from Beginning (Circular)

```
1. temp1 = head, temp2 = head
2. If only 1 node: head = NULL, free(temp1)
3. Else: traverse temp1 to last node (temp1->next == head)
4. head = temp2->next          ← move head to second node
5. temp1->next = head          ← last node points to new head
6. free(temp2)
```

---

## 7. Doubly Linked List

> Each node has **3 parts**: `prev pointer + data + next pointer`

```
         ┌─────────┬──────┬─────────┐
         │  PREV   │ DATA │  NEXT   │
         │(link1)  │      │(link2)  │
         └─────────┴──────┴─────────┘
    Points to              Points to
    previous node          next node
```

### Full Example

```
NULL ◀──┬──────────┬──▶ ◀──┬──────────┬──▶ ◀──┬──────────┬──▶ NULL
        │NULL│10│→1008│    │←1001│20│→1012│    │←1008│30│NULL│
        └──────────┘        └──────────┘        └──────────┘
          Node 1               Node 2               Node 3
```

### C Structure

```c
struct node {
    struct node *prev;   // link1: points to previous node
    int data;
    struct node *next;   // link2: points to next node
};
```

### Rules to remember

```
✅ head always points to first node
✅ first node → prev = NULL
✅ last node  → next = NULL
✅ Can traverse forward (using next) AND backward (using prev)
```

### Insert at Beginning (Doubly LL)

```
1. Create newNode
2. newNode->previous = NULL
3. If list empty:
      newNode->next = NULL, head = newNode
4. If list NOT empty:
      newNode->next = head
      head = newNode
```

### Insert at End (Doubly LL)

```
1. Create newNode, newNode->next = NULL
2. If list empty: newNode->prev = NULL, head = newNode
3. Else traverse to last node (temp->next == NULL)
4. temp->next = newNode
5. newNode->prev = temp
```

### Delete from Beginning (Doubly LL)

```
1. temp = head
2. If only 1 node: head = NULL, free(temp)
3. Else:
      head = temp->next
      head->previous = NULL
      free(temp)
```

---

## 8. Polynomial Representation

> Store a polynomial like `4x³ + 6x² + 10x + 6` as a linked list.

### Node Structure

```c
struct node {
    int coef;    // coefficient (4, 6, 10, 6)
    int expo;    // exponent   (3, 2,  1, 0)
    nptr next;
};
```

### Visual

```
POLY
 │
 ▼
[HEAD] ──▶ [coef=4│expo=3│next] ──▶ [coef=6│expo=2│next] ──▶ [coef=10│expo=1│next] ──▶ [coef=6│expo=0│NULL]

= 4x³ + 6x² + 10x + 6
```

### Polynomial Addition Algorithm

```
p = first poly, q = second poly, r = result list

While p != NULL AND q != NULL:
    ┌─────────────────────────────────────────────────────┐
    │ if p->expo == q->expo:                              │
    │     r->coef = p->coef + q->coef   ← add both      │
    │     r->expo = p->expo                              │
    │     advance BOTH p and q                           │
    │                                                     │
    │ else if p->expo > q->expo:                          │
    │     r->coef = p->coef             ← copy p's term  │
    │     advance only p                                  │
    │                                                     │
    │ else (q->expo > p->expo):                           │
    │     r->coef = q->coef             ← copy q's term  │
    │     advance only q                                  │
    └─────────────────────────────────────────────────────┘

After loop: copy remaining terms from whichever list is non-empty
```

### Example

```
List 1: [5|2] ──▶ [4|1] ──▶ [2|0]     = 5x² + 4x + 2
List 2:           [5|1] ──▶ [5|0]     =        5x + 5
                      ↕ (expo match at 1 and 0)
Result: [5|2] ──▶ [9|1] ──▶ [7|0]     = 5x² + 9x + 7
```

---

## 9. Sparse Matrix

> A matrix where **most elements are zero** — wasteful to store all of them.

**Example problem:**
```
100×100 matrix, only 10 non-zero values
→ Stores 10,000 values (wastes 9,990 cells!)
→ Solution: Store only non-zero values
```

### Representation 1 — Triplet (Array-based)

Store as `(row, col, value)` for each non-zero element only.

**Original matrix:**
```
0 0 0 0 9 0
0 8 0 0 0 0
4 0 0 2 0 0
0 0 0 0 0 5
0 0 2 0 0 0
```

**Triplet representation:**
```
┌──────┬─────────┬────────┐
│ Row  │  Col    │ Value  │
├──────┼─────────┼────────┤
│  5   │    6    │   6    │  ← matrix is 5×6 with 6 non-zeros
│  0   │    4    │   9    │
│  1   │    1    │   8    │
│  2   │    0    │   4    │
│  2   │    3    │   2    │
│  3   │    5    │   5    │
│  4   │    2    │   2    │
└──────┴─────────┴────────┘
```

### Representation 2 — Linked List

Uses 2 node types:

```
Header Node:                    Element Node:
┌────────────┬──────┬───────┐   ┌─────┬────────┬───────┬─────────┬───────┐
│ IndexValue │ down │ right │   │ row │ column │ value │ down/up │ right │
└────────────┴──────┴───────┘   └─────┴────────┴───────┴─────────┴───────┘
```

- `H0, H1, H2...` = header nodes for each row/column
- Each row forms its own linked list (using `right` pointer)
- Each column forms its own linked list (using `down` pointer)

---

## 10. Quick Cheatsheet

> 📌 Read this the night before exam!

| Topic | Key Point | Don't Forget |
|---|---|---|
| **Node** | `data + *next` | Last node → `next = NULL` |
| **Head** | Points to first node | Empty: `head->next == NULL` |
| **Insert at start** | `new->next = head->next; head->next = new` | O(1) — super fast |
| **Insert at end** | Traverse to last → `last->next = new` | O(n) — must traverse |
| **Delete from start** | `head->next = head->next->next` | Always `free()` deleted node |
| **Delete from end** | 2 pointers (temp1 + temp2) → `temp2->next = NULL` | Check single-node edge case |
| **Circular LL** | `last->next = head` (not NULL) | Loop stop: `p == head` |
| **Doubly LL** | `prev + data + next` per node | `first->prev = NULL`, `last->next = NULL` |
| **Polynomial node** | `coeff + expo + next` | Add when exponents are equal |
| **Sparse matrix Triplet** | `(row, col, value)` only for non-zeros | Row 0 = metadata |
| **Time: Access** | O(n) — no direct index | No random access! |
| **Time: Insert/Delete head** | O(1) — just pointer change | Only if you already have the pointer |

---

### ⚡ Edge Cases to Always Check (exam killer)

```
For EVERY operation, ask yourself:

  1️⃣  Is the list empty?          → head->next == NULL
  2️⃣  Does it have only 1 node?   → head->next->next == NULL
  3️⃣  Is target the first node?   → temp == head->next
  4️⃣  Is target the last node?    → temp->next == NULL

Missing any of these = wrong answer in exam.
```

---

### 🔁 Operations Summary Chart

```
SINGLY LL OPERATIONS
═════════════════════════════════════════════════════

  INSERT                    DELETE                DISPLAY
  ──────                    ──────                ───────
  At Beginning ─── O(1)     From Beginning ─ O(1)   O(n)
  At End       ─── O(n)     From End       ─ O(n)
  At Position  ─── O(n)     Specific Node  ─ O(n)

  CIRCULAR LL: same ops, but check head instead of NULL
  DOUBLY LL: same ops, but also update prev pointers
```

---

> 📘 **Unit 2 — Data Structures** | Singly LL · Circular LL · Doubly LL · Polynomial · Sparse Matrix