# 🔗 Linked Lists in Java — Complete Notes

> **One read = full understanding.** Visual diagrams, tables, mind maps, Java code, and interview-ready patterns — sab kuch andar hai.

---

## 📋 Table of Contents

1. [What is a Linked List?](#1-what-is-a-linked-list)
2. [Why Do We Need a Linked List?](#2-why-do-we-need-a-linked-list)
3. [Array vs ArrayList vs Linked List](#3-array-vs-arraylist-vs-linked-list)
4. [Big Picture — Mind Map](#4-big-picture--mind-map)
5. [Types of Linked Lists](#5-types-of-linked-lists)
6. [Singly Linked List — All Operations](#6-singly-linked-list--all-operations)
7. [Circular Linked List](#7-circular-linked-list)
8. [Doubly Linked List](#8-doubly-linked-list)
9. [Polynomial Representation](#9-polynomial-representation)
10. [Sparse Matrix](#10-sparse-matrix)
11. [Quick Cheatsheet](#11-quick-cheatsheet)
12. [Linked List Techniques (Advanced Patterns)](#12-linked-list-techniques-advanced-patterns)
    - 12.1 [Overview](#121-overview)
    - 12.2 [Categories & Sub-categories](#122-categories--sub-categories)
    - 12.3 [Visual Diagram Representations](#123-visual-diagram-representations)
    - 12.4 [Java Code Templates](#124-java-code-templates)
    - 12.5 [Quick Decision Guide](#125-quick-decision-guide)
    - 12.6 [When to Use These Techniques](#126-when-to-use-these-techniques)
    - 12.7 [When NOT to Use](#127-when-not-to-use)
    - 12.8 [Benefits](#128-benefits)
    - 12.9 [Drawbacks / Limitations](#129-drawbacks--limitations)
    - 12.10 [Time & Space Complexity Table](#1210-time--space-complexity-table)

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

## 2. Why Do We Need a Linked List?

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

## 3. Array vs ArrayList vs Linked List

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

## 4. Big Picture — Mind Map

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

## 5. Types of Linked Lists

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

## 6. Singly Linked List — All Operations

### 6.1 Node Structure (self-referential)

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

### 6.2 INSERTION (3 ways)

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

### 6.3 DELETION (3 ways)

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

### 6.4 DISPLAY (Traversal)

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

### 6.5 SEARCH (Find Node)

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

## 7. Circular Linked List

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

### Insert at End (Circular)

```
BEFORE:  HEAD─▶[10]─▶[15]─▶[22]─┐
          ▲                     │
          └─────────────────────┘

AFTER:   HEAD─▶[10]─▶[15]─▶[22]─▶[30]─┐
          ▲                           │
          └───────────────────────────┘
                                  ↑ NEW (last→next = head)
```

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

last.next = newNode;                // old last points to new node
newNode.next = head;                // new node (new last) points back to head
```

> Note: `head` itself doesn't move here — only the old last node's `next` link changes.

### Insert at Specific Position (Circular)

```java
// insert newNode AFTER a given node `temp` (temp != null, already part of the list)
Node newNode = new Node(x);
newNode.next = temp.next;
temp.next = newNode;
// if temp was the last node, newNode.next now correctly points to head
```

### Delete from End (Circular)

```
[H]─▶[10]─▶[15]─▶[22]─┐
      ▲      ▲         │
     prev   curr        │
             └───────────┘  ← curr.next == head, so curr is the last node

prev.next = head   ← cut here, curr is now unreferenced (GC reclaims it)
```

```java
if (head == null) return;                     // empty list

if (head.next == head) {                      // only 1 node
    head = null;
    return;
}

Node prev = head, curr = head.next;
while (curr.next != head) {                   // walk till curr is the last node
    prev = curr;
    curr = curr.next;
}
prev.next = head;                              // cut the link to the last node
```

### Delete Specific Node (Circular)

```java
if (head == null) return;                       // empty list

if (head.data == key && head.next == head) {    // only 1 node, matches
    head = null;
    return;
}

// special case: deleting the head node itself
if (head.data == key) {
    Node last = head;
    while (last.next != head)                   // find the last node
        last = last.next;
    head = head.next;
    last.next = head;                            // re-link last → new head
    return;
}

// general case: search for the node to delete
Node prev = head, curr = head.next;
do {
    if (curr.data == key) {
        prev.next = curr.next;
        return;
    }
    prev = curr;
    curr = curr.next;
} while (curr != head);
```

> ⚠️ Same edge-case checklist applies: empty list, single node, head node, specific/middle node.

### Display / Traversal (Circular)

```java
void displayList(Node head) {
    if (head == null) {
        System.out.println("empty list");
        return;
    }
    Node p = head;
    do {
        System.out.print(p.data + "\t");
        p = p.next;
    } while (p != head);          // stop when back at head, NOT at null
}
```

> ⚠️ Using a plain `while (p != null)` loop here causes an **infinite loop** — a circular list never has a `null` link. Always use `do…while (p != head)`.

---

## 8. Doubly Linked List

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

### Insert at Specific Position (Doubly LL)

```
BEFORE:  ... ⇄ [A] ⇄ [B] ⇄ ...
AFTER:   ... ⇄ [A] ⇄ [NEW] ⇄ [B] ⇄ ...
```

```java
// insert newNode AFTER a given node `temp` (temp != null, already part of the list)
Node newNode = new Node(x);
newNode.next = temp.next;
newNode.prev = temp;

if (temp.next != null)          // temp was NOT the last node
    temp.next.prev = newNode;

temp.next = newNode;
```

### Delete from End (Doubly LL)

```
[H] ⇄ [10] ⇄ [25] ⇄ [18] ⇄ null
                      ▲
                    temp (temp.next == null → temp is last)

temp.prev.next = null   ← cut here
(temp is now unreferenced → GC reclaims it)
```

```java
if (head == null) return;              // empty list

if (head.next == null) {               // only 1 node
    head = null;
    return;
}

Node temp = head;
while (temp.next != null)              // traverse to last node
    temp = temp.next;

temp.prev.next = null;                 // cut the link from the second-last node
```

### Delete Specific Node (Doubly LL)

```java
if (head == null || target == null) return;   // empty list / no target given

if (target == head) {                          // deleting the head node
    head = head.next;
    if (head != null) head.prev = null;
    return;
}

if (target.next != null)                       // target is NOT the last node
    target.next.prev = target.prev;

target.prev.next = target.next;                // relink around target
// works even if target was the last node, since target.next is null there
```

> 💡 The biggest advantage of Doubly LL deletion: you don't need a separate `prev` walk like in Singly LL — `target.prev` is already stored on the node itself.

### Display — Forward & Backward (Doubly LL)

```java
// Forward traversal (head → tail)
void displayForward(Node head) {
    Node p = head;
    while (p != null) {
        System.out.print(p.data + "\t");
        p = p.next;
    }
}

// Backward traversal (tail → head) — the whole point of having `prev`
void displayBackward(Node head) {
    if (head == null) return;

    Node p = head;
    while (p.next != null)      // walk to the last node first
        p = p.next;

    while (p != null) {         // now walk back using prev
        System.out.print(p.data + "\t");
        p = p.prev;
    }
}
```

---

## 9. Polynomial Representation

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

## 10. Sparse Matrix

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

## 11. Quick Cheatsheet

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

## 12. Linked List Techniques (Advanced Patterns)

> Beyond basic insert/delete/search, certain reusable *patterns* come up again and again in DSA and coding-interview problems built on linked lists. This section covers those techniques as a standalone toolkit.

### 12.1 Overview

| # | Technique | Purpose |
|---|---|---|
| 1 | **Dummy Node Technique** | Simplify edge cases (empty list, head deletion) |
| 2 | **Fast-Slow Pointers (Floyd's)** | Find middle, detect cycle, cycle start |
| 3 | **Reversal Technique** | Reverse whole list / sub-list / in groups of K |
| 4 | **Two-Pointer / Runner Technique** | Nth node from end, remove Nth node |
| 5 | **Merge Technique** | Merge two/k sorted lists |
| 6 | **Recursion Technique** | Reverse, merge, palindrome check recursively |
| 7 | **In-place Manipulation** | Reorder list, swap nodes in pairs, rotate list |
| 8 | **Cycle Detection & Removal** | Detect + remove loop in list |
| 9 | **Intersection Technique** | Find intersection point of two lists |

---

### 12.2 Categories & Sub-categories

| Category | Sub-category | Examples |
|---|---|---|
| **Traversal-based** | Single pass, Two pass | Find length, find middle |
| **Multi-pointer** | Fast-Slow, Two-pointer with gap | Cycle detect, Nth from end |
| **Structural modification** | Full reversal, Partial reversal, Group reversal | Reverse List, Reverse Between, Reverse K-Group |
| **Combining lists** | Merge (2 lists), Merge (K lists) | Merge Two Sorted Lists, Merge K Sorted Lists |
| **Node-value techniques** | Dummy head, Sentinel node | Remove Elements, Delete Duplicates |
| **Math/Logic on LL** | Add numbers, Palindrome check | Add Two Numbers, Palindrome Linked List |

---

### 12.3 Visual Diagram Representations

#### A) Dummy Node Technique
```
Real list:      [1] -> [2] -> [3] -> null
With Dummy:  [D] -> [1] -> [2] -> [3] -> null
              ^
           dummy.next = head (fake starting point)

Why? So head deletion/insertion doesn't need special-case code:
     dummy.next always exists, even if real head changes.
Return dummy.next at the end (the real new head).
```

#### B) Fast-Slow Pointers (find middle / detect cycle)
```
1 -> 2 -> 3 -> 4 -> 5 -> null
slow          fast

step1: slow=2, fast=3
step2: slow=3, fast=5
step3: fast.next==null -> stop. slow = middle (3)
```

#### C) Reversal Technique (whole list)
```
BEFORE:  null <- ?   [1] -> [2] -> [3] -> null
                       ↑
                     head

Iterative flip using 3 pointers: prev, curr, next
prev=null curr=1
  next=2; 1.next=null(prev); prev=1; curr=2
  next=3; 2.next=1(prev);    prev=2; curr=3
  next=null; 3.next=2(prev); prev=3; curr=null

AFTER:  null <- [1] <- [2] <- [3]
                                ^
                             new head = prev
```

#### D) Reverse in Groups of K (K=2)
```
BEFORE: [1] -> [2] -> [3] -> [4] -> [5] -> null
AFTER:  [2] -> [1] -> [4] -> [3] -> [5] -> null
        (reverse each block of 2, last odd group can stay as-is or reverse per rule)
```

#### E) Two-Pointer / Runner (Nth Node From End, n=2)
```
[1] -> [2] -> [3] -> [4] -> [5] -> null
fast starts n steps ahead:
fast: 1 -> 2 -> 3 (n=2 steps ahead)
Then move both together till fast hits null:
slow lands just before the target -> delete slow.next
```

#### F) Merge Two Sorted Lists
```
L1: [1] -> [3] -> [5] -> null
L2: [2] -> [4] -> [6] -> null

Merge using dummy node + pointer comparison:
Result: [1]->[2]->[3]->[4]->[5]->[6]->null
```

#### G) Cycle Detection (Floyd's) + Finding Cycle Start
```
1 -> 2 -> 3 -> 4 -> 5
          ^         |
          |_________|

slow & fast meet inside cycle -> cycle exists
Reset one pointer to head, move both 1 step at a time
-> they meet exactly at cycle START node
```

#### H) Palindrome Check (find middle + reverse half + compare)
```
[1] -> [2] -> [3] -> [2] -> [1] -> null
Step1: find middle -> split
Step2: reverse second half -> [1] -> [2] -> [3]
Step3: compare first half vs reversed second half
```

---

### 12.4 Java Code Templates

**Dummy Node (generic pattern)**
```java
ListNode dummy = new ListNode(-1);
dummy.next = head;
ListNode curr = dummy;
// ... do operations using curr, curr.next ...
return dummy.next; // real head (handles head changes safely)
```

**Reverse Linked List (iterative)**
```java
public ListNode reverseList(ListNode head) {
    ListNode prev = null, curr = head;
    while (curr != null) {
        ListNode next = curr.next; // save next
        curr.next = prev;          // reverse link
        prev = curr;                // move prev forward
        curr = next;                 // move curr forward
    }
    return prev; // new head
}
```

**Reverse Linked List (recursive)**
```java
public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) return head; // base case
    ListNode newHead = reverseList(head.next);
    head.next.next = head; // reverse the link
    head.next = null;
    return newHead;
}
```

**Merge Two Sorted Lists (using Dummy)**
```java
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(-1);
    ListNode curr = dummy;
    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
        else { curr.next = l2; l2 = l2.next; }
        curr = curr.next;
    }
    curr.next = (l1 != null) ? l1 : l2;
    return dummy.next;
}
```

**Remove Nth Node From End (Two-Pointer/Runner)**
```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(-1);
    dummy.next = head;
    ListNode fast = dummy, slow = dummy;
    for (int i = 0; i < n; i++) fast = fast.next; // move fast n steps
    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }
    slow.next = slow.next.next; // remove target node
    return dummy.next;
}
```

**Detect Cycle + Find Cycle Start**
```java
public ListNode detectCycle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) {           // cycle found
            slow = head;
            while (slow != fast) {    // find start
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }
    }
    return null; // no cycle
}
```

**Reverse in K-Group**
```java
public ListNode reverseKGroup(ListNode head, int k) {
    ListNode node = head;
    int count = 0;
    while (node != null && count < k) { node = node.next; count++; }
    if (count < k) return head; // fewer than k nodes left, leave as-is

    ListNode prev = reverseKGroup(node, k); // recurse for next group
    ListNode curr = head;
    while (count-- > 0) {
        ListNode next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
    }
    return prev;
}
```

**Palindrome Linked List**
```java
public boolean isPalindrome(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) { // find middle
        slow = slow.next;
        fast = fast.next.next;
    }
    ListNode secondHalfReversed = reverseList(slow); // reverse second half
    ListNode p1 = head, p2 = secondHalfReversed;
    while (p2 != null) {                              // compare
        if (p1.val != p2.val) return false;
        p1 = p1.next;
        p2 = p2.next;
    }
    return true;
}
```

---

### 12.5 Quick Decision Guide

```
Need to reverse (whole/part/groups)?      -> Reversal Technique
Need to find middle / detect cycle?       -> Fast-Slow Pointers
Need Nth node from end?                   -> Two-Pointer/Runner Technique
Need to merge sorted lists?               -> Merge Technique (+ Dummy Node)
Head might change / edge cases messy?     -> Dummy Node Technique
Need palindrome check?                    -> Fast-Slow (middle) + Reversal + Compare
Two lists intersecting somewhere?         -> Intersection Technique (align lengths first)
```

---

### 12.6 When to Use These Techniques

- Need to solve LL problems with **O(1) extra space** instead of using arrays/hashmaps
- Problem involves: reversing, detecting cycles, finding middle, merging, removing Nth node
- Interview/DSA problems explicitly involving **singly/doubly linked lists**
- Need to modify list structure **in-place** without creating new nodes/lists
- Problem has keywords: "reverse", "cycle", "middle", "merge", "Nth from end", "palindrome linked list", "intersection point"

---

### 12.7 When NOT to Use

- When **random access** is needed frequently → use Array/ArrayList instead (LL is O(n) to access by index)
- When the data structure isn't actually a linked list (arrays, trees, graphs need different techniques)
- When simplicity matters more than space — sometimes using a **HashSet** (O(n) space) is far simpler to code correctly than pointer manipulation, especially under time pressure
- For very simple operations like **display/traverse/search**, no special "technique" is needed — plain iteration suffices
- If a doubly linked list is available and you need backward info → use `prev` directly instead of forcing single-pointer techniques

---

### 12.8 Benefits

- Most techniques achieve **O(1) extra space** — no auxiliary array/hashmap needed
- **Single-pass (O(n))** solutions for problems that look like they'd need O(n²) or O(n) extra space
- Elegant, memory-efficient — great for large lists / memory-constrained systems
- Dummy node technique **eliminates messy edge-case code** (empty list, head deletion)
- Reusable patterns — same fast-slow / reversal ideas apply across many different problems

---

### 12.9 Drawbacks / Limitations

- **Pointer manipulation is error-prone** — easy to lose reference to a node (memory leak/lost segment) or create accidental cycles
- Logic is **hard to visualize/debug** without drawing diagrams — off-by-one and null-pointer errors are common
- Some techniques (like reverse-in-groups, cycle-start-detection) require **careful mathematical reasoning**, not just intuition
- Recursive techniques add **O(n) stack space**, defeating the "O(1) space" advantage unless done iteratively
- Not beginner-friendly — takes practice to master multi-pointer coordination

---

### 12.10 Time & Space Complexity Table

| Technique | Problem Example | Time | Space |
|---|---|---|---|
| Dummy Node | Remove Elements, Merge Lists | O(n) | O(1) |
| Fast-Slow (middle) | Find Middle Node | O(n) | O(1) |
| Fast-Slow (cycle detect) | Linked List Cycle | O(n) | O(1) |
| Fast-Slow (cycle start) | Linked List Cycle II | O(n) | O(1) |
| Reversal (iterative) | Reverse Linked List | O(n) | O(1) |
| Reversal (recursive) | Reverse Linked List | O(n) | O(n) — call stack |
| Reverse in K-Group | Reverse Nodes in k-Group | O(n) | O(1) iterative / O(n/k) recursive |
| Two-Pointer (Nth from end) | Remove Nth Node From End | O(n) | O(1) |
| Merge Two Sorted Lists | Merge Two Sorted Lists | O(n+m) | O(1) (in-place) |
| Merge K Sorted Lists | Merge K Sorted Lists | O(N log k) | O(k) — heap |
| Palindrome Check | Palindrome Linked List | O(n) | O(1) |
| Intersection of Two Lists | Intersection of Two LL | O(n+m) | O(1) |
| Add Two Numbers | Add Two Numbers (LL) | O(max(n,m)) | O(max(n,m)) for result list |

---

> 📘 **Unit 2 — Data Structures** | Singly LL · Circular LL · Doubly LL · Polynomial · Sparse Matrix · Linked List Techniques
