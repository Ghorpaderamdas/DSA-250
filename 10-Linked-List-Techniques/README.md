# 🔗 Linked Lists in Java — Beginner's Complete Notes

> **One read = full understanding.** Visual diagrams, tables, mind maps, Java code — sab kuch andar hai.

---

## 📋 Table of Contents

1. [What is a Linked List?](#1-what-is-a-linked-list) 
2. [Array vs Array List vs Linked List](#2-array-vs-linked-list)
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
│  10  │ 1004 │────▶│  25  │ 1008 │────▶│  18  │ null │
└──────┴──────┘     └──────┴──────┘     └──────┴──────┘
 @1001                @1004               @1008 (last)
 [data] [next]
```

💡 **Think of it like a treasure hunt** — each clue (node) tells you where the next clue is. Last clue says `null` = hunt over.

> Note: in Java you never see/print these raw addresses — `next` is a **reference** to a `Node` object on the heap (conceptually "points to" the next node, but the JVM manages the actual memory for you). The `@1001`-style addresses above are just for visualizing the idea.

### Node Structure in Java

```java
class Node {
    int data;     // stores the value
    Node next;    // reference to the next node

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
// head = reference to the first node (head == null  →  empty list)
```

> 💡 In Java there are no raw pointers/`malloc`/`free`. A `Node` variable holds a **reference** to an object on the heap, and the **garbage collector** automatically frees a node once nothing references it anymore (no manual `free()`).

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
| Extra memory | Each node stores a reference too |
| No backward traversal | In singly LL — can only go forward |
| Cache unfriendly | Nodes scattered in RAM — slow for CPU cache |

---

## 🤔 Why Do We Even Need a Linked List?

> **Short answer:** Arrays (and ArrayLists) are great for *reading* data fast, but rigid and slow when you need to *grow, shrink, insert or delete*. Linked Lists exist to fix exactly that weak spot.

**1️⃣ What's the problem with Arrays?**
- **Fixed size** — `int[] arr = new int[5]` locks you to 5 slots forever. Need a 6th element? You must create a brand-new, bigger array and copy everything over by hand.
- **Costly insert/delete** — adding or removing an element in the middle means shifting every element after it one step → **O(n)**, slow for large data.

**2️⃣ Doesn't `ArrayList` already solve this?**
- Only partly. `ArrayList` *feels* dynamic — no size declared upfront, just call `add()` / `remove()` — but under the hood it is **still a plain array**.
- When that internal array fills up, Java silently allocates a **bigger array** (usually ~1.5× the size) and **copies every old element into it**. This hidden "resize" is an expensive O(n) operation you don't see in your code.
- Inserting/deleting in the *middle* of an `ArrayList` **still shifts elements**, exactly like a raw array → still O(n).

**3️⃣ So — does Linked List overcome these limitations of Array?**

| Limitation of Array | Does Linked List fix it? |
|---|---|
| Fixed size / wasted or insufficient memory | ✅ **Yes** — grows one node at a time, no pre-allocation, no bulk copying |
| Slow insert/delete (everything shifts) | ✅ **Yes** — just rewire 1–2 references → O(1) once you're at the right spot |
| Slow random access (`arr[i]` jumps directly) | ❌ **No — it's actually worse here.** A linked list must walk from `head` node by node → O(n) |

> 💡 **Takeaway:** A Linked List **trades away fast random access in exchange for fast, flexible insertion/deletion and a truly dynamic size.** Reach for arrays/`ArrayList` when you mostly *read by index*; reach for linked lists when you mostly *insert/delete* or don't know the size ahead of time (e.g., building stacks, queues, trees).

---

## 2. Array vs Array List vs Linked List

> 🧠 **Picture it like this:** An **Array** is a fixed row of lockers. An **ArrayList** is the same row of lockers — except when it's full, someone secretly builds a bigger row and quietly moves everything over for you. A **Linked List** is a treasure-hunt chain of boxes — you can clip a new box in anywhere without disturbing the others.

| Feature | Array | ArrayList | Linked List |
|---|---|---|---|
| Memory allocation | ❌ Static — fixed size, decided at creation | ⚠️ Looks dynamic, but is internally a resizable array | ✅ Truly dynamic — one node allocated at a time |
| Size | ❌ Fixed — must know the size in advance | ✅ Grows via `add()` — but resizing secretly copies the *whole* array (O(n)) | ✅ Grows/shrinks freely, one node at a time |
| Access by index | ✅ O(1) — `arr[i]` jumps directly | ✅ O(1) — `list.get(i)` jumps directly | ❌ O(n) — must walk from `head` |
| Insert/Delete at beginning | ❌ O(n) — shift every element over | ❌ O(n) — shift every element over | ✅ O(1) — just relink a couple of references |
| Insert/Delete in the middle | ❌ O(n) — shifting required | ❌ O(n) — shifting required (same underlying array) | ✅ O(1) once you're at the node — no shifting at all |
| Memory per element | ✅ Compact — stores only the data | ⚠️ Compact, but often carries spare unused capacity | ❌ Extra — every node also stores a reference |
| Cache performance | ✅ Excellent — elements sit next to each other | ✅ Excellent — elements sit next to each other | ❌ Poor — nodes scattered across the heap |
| Best used for | Fixed-size data, heavy index-based reads | General-purpose lists, frequent reads by index | Frequent inserts/deletes, building stacks, queues, trees, graphs |

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
   data+next  last→head   prev+data    Insert   coef+expo   Triplet /
   one dir    no null      +next       Delete   per node    Linked
              end         both dir     Display              repr
```

---

## 4. 3 Types of Linked Lists

### Type 1 — Singly Linked List
```
[1] ──▶ [2] ──▶ [3] ──▶ null
```
- Each node: `data + next`
- One direction only (forward)
- Last node → `next = null`

---

### Type 2 — Circular Linked List
```
[1] ──▶ [2] ──▶ [3] ──┐
 ▲                     │
 └─────────────────────┘
```
- Same as singly BUT last node → `next = head` (not `null`)
- No real "end" — it loops forever
- Traverse stops when `p == head` again

---

### Type 3 — Doubly Linked List
```
null ◀─ [1] ⇄ [2] ⇄ [3] ─▶ null
```
- Each node: `prev + data + next`
- Can traverse both forward AND backward
- First node's `prev = null`, Last node's `next = null`

---

## 5. Singly Linked List — All Operations

### 5.1 Node Structure (self-referential)

```java
class Node {
    int data;
    Node next;   // reference to same type = self-referential

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
// head = reference to first node
// head == null means empty list
```

---

### 5.2 INSERTION (3 ways)

#### 📍 Insert at Beginning

```
BEFORE:  HEAD ──▶ [10] ──▶ [25] ──▶ null
AFTER:   HEAD ──▶ [5] ──▶ [10] ──▶ [25] ──▶ null
                   ↑
                  NEW
```

**Steps:**
```java
Node newNode = new Node(x);
newNode.next = head;     // new node points to old first node
head = newNode;          // head now points to new node
```
> O(1) — works even on an empty list, since `head` would simply be `null` and `newNode.next = null`.

---

#### 📍 Insert at End

```
BEFORE:  HEAD ──▶ [10] ──▶ [25] ──▶ null
AFTER:   HEAD ──▶ [10] ──▶ [25] ──▶ [99] ──▶ null
                                      ↑
                                     NEW
```

**Steps:**
```java
Node newNode = new Node(x);
newNode.next = null;

if (head == null) {            // empty list → new node becomes head
    head = newNode;
    return;
}

Node temp = head;
while (temp.next != null)      // traverse till last node
    temp = temp.next;

temp.next = newNode;           // link last node to new node
```

---

#### 📍 Insert at Specific Position (before/after a node)

**After a node:**
```java
// temp = the target node we found
newNode.next = temp.next;
temp.next = newNode;
```

**Before a node:**
```java
// prev = the node just before the target
newNode.next = prev.next;      // new points to target
prev.next = newNode;           // prev now points to new
```

---

### 5.3 DELETION (3 ways)

| Where | Steps | Edge Case |
|---|---|---|
| **From Beginning** | `head = head.next` | If only 1 node: `head = null` |
| **From End** | Use 2 references → traverse → `secondLast.next = null` | If only 1 node: `head = null` |
| **Specific Node** | Find node + track `prev` → `prev.next = curr.next` | Check if first / last / only node |

> Java has no `free()` — once `prev.next` (or `head`) stops referencing a node, the **garbage collector** reclaims it automatically.

#### Visual — Delete from End (2-reference trick)

```
 prev     curr
  │        │
  ▼        ▼
[H] ──▶ [10] ──▶ [25] ──▶ [18] ──▶ null
                  │         │
                 prev      curr  ← stop when curr.next == null
                  │
                  └── prev.next = null  ← cut here
                      (curr is now unreferenced → GC reclaims it)
```

```java
if (head == null) return;                 // empty list
if (head.next == null) { head = null; return; }   // only 1 node

Node prev = head, curr = head.next;
while (curr.next != null) {                // walk till curr is the last node
    prev = curr;
    curr = curr.next;
}
prev.next = null;                          // cut the link to the last node
```

> ⚠️ **Common mistake:** Always keep 2 references for end/specific deletion. One (`curr`) finds the target, the other (`prev`) tracks the node just before it — you need `prev` to re-link the list.

---

### 5.4 DISPLAY (Traversal)

```java
void displayList(Node head) {
    if (head == null) {
        System.out.println("empty list");
        return;
    }
    Node p = head;
    while (p != null) {
        System.out.print(p.data + "\t");
        p = p.next;
    }
}
```

**Flow:**
```
p = head
While p != null:
    print p.data
    p = p.next
```

---

### 5.5 SEARCH (Find Node)

```java
boolean search(Node head, int target) {
    Node p = head;
    while (p != null) {
        if (p.data == target)
            return true;        // found
        p = p.next;
    }
    return false;               // not found
}
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
| Last node's next | `null` | `head` |
| Empty check | `head == null` | `head == null` |
| Traverse stop | `p != null` | `do…while (p != head)` |
| Insert at end | `newNode.next = null` | `newNode.next = head` |

### Insert at Beginning (Circular)

```java
Node newNode = new Node(x);

if (head == null) {                 // empty list → points to itself
    newNode.next = newNode;
    head = newNode;
    return;
}

Node last = head;
while (last.next != head)           // find the last node
    last = last.next;

newNode.next = head;                // new points to old first node
last.next = newNode;                // last node points to new node
head = newNode;                     // head now points to new node
```

### Delete from Beginning (Circular)

```java
if (head == null) return;                    // empty list

if (head.next == head) {                     // only 1 node
    head = null;
    return;
}

Node last = head;
while (last.next != head)                    // find the last node
    last = last.next;

head = head.next;                            // move head to second node
last.next = head;                            // last node points to new head
```

---

## 7. Doubly Linked List

> Each node has **3 parts**: `prev reference + data + next reference`

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
null ◀──┬──────────┬──▶ ◀──┬──────────┬──▶ ◀──┬──────────┬──▶ null
        │null│10│→1008│    │←1001│20│→1012│    │←1008│30│null│
        └──────────┘        └──────────┘        └──────────┘
          Node 1               Node 2               Node 3
```

### Java Class

```java
class Node {
    Node prev;   // link1: reference to previous node
    int data;
    Node next;   // link2: reference to next node

    Node(int data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
```

### Rules to remember

```
✅ head always points to first node
✅ first node → prev = null
✅ last node  → next = null
✅ Can traverse forward (using next) AND backward (using prev)
```

### Insert at Beginning (Doubly LL)

```java
Node newNode = new Node(x);
newNode.prev = null;
newNode.next = head;

if (head != null)            // list was NOT empty
    head.prev = newNode;

head = newNode;
```

### Insert at End (Doubly LL)

```java
Node newNode = new Node(x);
newNode.next = null;

if (head == null) {          // empty list
    newNode.prev = null;
    head = newNode;
    return;
}

Node temp = head;
while (temp.next != null)    // traverse to last node
    temp = temp.next;

temp.next = newNode;
newNode.prev = temp;
```

### Delete from Beginning (Doubly LL)

```java
if (head == null) return;            // empty list

if (head.next == null) {             // only 1 node
    head = null;
    return;
}

head = head.next;
head.prev = null;
```

---

## 8. Polynomial Representation

> Store a polynomial like `4x³ + 6x² + 10x + 6` as a linked list.

### Node Structure

```java
class Node {
    int coef;    // coefficient (4, 6, 10, 6)
    int expo;    // exponent   (3, 2,  1, 0)
    Node next;
}
```

### Visual

```
POLY
 │
 ▼
[HEAD] ──▶ [coef=4│expo=3│next] ──▶ [coef=6│expo=2│next] ──▶ [coef=10│expo=1│next] ──▶ [coef=6│expo=0│null]

= 4x³ + 6x² + 10x + 6
```

### Polynomial Addition Algorithm

```
p = first poly, q = second poly, r = result list

While p != null AND q != null:
    ┌─────────────────────────────────────────────────────┐
    │ if p.expo == q.expo:                                │
    │     r.coef = p.coef + q.coef      ← add both       │
    │     r.expo = p.expo                                 │
    │     advance BOTH p and q                            │
    │                                                     │
    │ else if p.expo > q.expo:                            │
    │     r.coef = p.coef               ← copy p's term  │
    │     advance only p                                  │
    │                                                     │
    │ else (q.expo > p.expo):                             │
    │     r.coef = q.coef               ← copy q's term  │
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
- Each row forms its own linked list (using the `right` reference)
- Each column forms its own linked list (using the `down` reference)

---

## 10. Quick Cheatsheet

> 📌 Read this the night before exam!

| Topic | Key Point | Don't Forget |
|---|---|---|
| **Node** | `int data; Node next;` | Last node → `next = null` |
| **Head** | Reference to first node | Empty: `head == null` |
| **Insert at start** | `newNode.next = head; head = newNode;` | O(1) — super fast |
| **Insert at end** | Traverse to last → `last.next = newNode;` | O(n) — must traverse |
| **Delete from start** | `head = head.next;` | GC reclaims the old node automatically |
| **Delete from end** | 2 references (`prev` + `curr`) → `prev.next = null;` | Check single-node edge case |
| **Circular LL** | `last.next = head` (not `null`) | Loop stop: `p == head` |
| **Doubly LL** | `prev + data + next` per node | `first.prev = null`, `last.next = null` |
| **Polynomial node** | `coef + expo + next` | Add when exponents are equal |
| **Sparse matrix Triplet** | `(row, col, value)` only for non-zeros | Row 0 = metadata |
| **Time: Access** | O(n) — no direct index | No random access! |
| **Time: Insert/Delete head** | O(1) — just reference change | Only if you already have the reference |

---

### ⚡ Edge Cases to Always Check (exam killer)

```
For EVERY operation, ask yourself:

  1️⃣  Is the list empty?          → head == null
  2️⃣  Does it have only 1 node?   → head.next == null
  3️⃣  Is target the first node?   → temp == head
  4️⃣  Is target the last node?    → temp.next == null

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

  CIRCULAR LL: same ops, but check `head` instead of `null`
  DOUBLY LL: same ops, but also update `prev` references
```

---

> 📘 **Unit 2 — Data Structures** | Singly LL · Circular LL · Doubly LL · Polynomial · Sparse Matrix