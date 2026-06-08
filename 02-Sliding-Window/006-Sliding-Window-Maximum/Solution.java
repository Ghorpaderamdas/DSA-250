package p012;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

/**
 * Problem: Sliding Window Maximum
 * GFG / LeetCode #239 — Hard
 *
 * Given array arr[] and window size k,
 * return the maximum element in every window of size k.
 *
 * Example: arr=[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]
 *
 * KEY INSIGHT (Monotonic Deque):
 *   Maintain a deque of INDICES, decreasing by value (front=max).
 *   - Remove front if outside window.
 *   - Remove back elements smaller than incoming (they're useless).
 *   - Front is always the window maximum.
 *
 * Pattern   : Sliding Window (Fixed Size) + Monotonic Deque
 * Difficulty: Hard
 */
public class Solution {

    // ============================================================
    // Method 1 — Brute Force
    //
    // Idea : For each window of size k, scan all k elements to find max.
    //
    // Time : O(n × k) — outer loop × inner scan
    // Space: O(1)     — no extra data structure
    // ============================================================
    public int[] maxSlidingWindowBrute(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];

        for (int i = 0; i <= n - k; i++) {         // start of each window
            int windowMax = Integer.MIN_VALUE;
            for (int j = i; j < i + k; j++) {      // scan k elements
                windowMax = Math.max(windowMax, arr[j]);
            }
            result[i] = windowMax;
        }

        return result;
    }


    // ============================================================
    // Method 2 — Better: Max Heap (O(n log k))
    //
    // Idea : Use a max-heap of {value, index} pairs.
    //        When the top element's index is outside the window, remove it.
    //        Top of heap = current window's maximum.
    //
    // Note: "lazy deletion" — we don't remove out-of-window elements
    //        until they reach the top (hence heap might temporarily hold
    //        elements outside the current window).
    //
    // Time : O(n log k) — each element inserted once, removed at most once;
    //                     heap operations are O(log k)
    // Space: O(k)       — heap holds at most n elements in worst case
    //                     (with lazy deletion), but bounded by n in practice
    // ============================================================
    public int[] maxSlidingWindowHeap(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];

        // Max heap: compare by value descending; tie-break by index descending
        // int[0] = value, int[1] = index
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> b[0] != a[0] ? b[0] - a[0] : b[1] - a[1]
        );

        for (int right = 0; right < n; right++) {
            maxHeap.offer(new int[]{arr[right], right});

            // remove top elements that are outside the current window
            // window starts at: right - k + 1
            while (maxHeap.peek()[1] < right - k + 1) {
                maxHeap.poll();
            }

            // record result when window is full (right >= k-1)
            if (right >= k - 1) {
                result[right - k + 1] = maxHeap.peek()[0];
            }
        }

        return result;
    }


    // ============================================================
    // Method 3 — Sliding Window + Monotonic Deque ⭐ (Optimal)
    //
    // Idea : Use a Deque that stores INDICES.
    //        The deque is "monotonically decreasing" by value:
    //          - Front = index of the MAXIMUM in the current window
    //          - Back  = index of the smallest "candidate" max
    //
    // Two operations each step:
    //   1. Remove from FRONT if that index is outside the window
    //      (index < right - k + 1)
    //   2. Remove from BACK any index whose value <= arr[right]
    //      (they can never be max while arr[right] is in the window)
    //   3. Add right to back
    //   4. If window is full (right >= k-1): answer = arr[deque.front()]
    //
    // Visual (arr=[1,3,-1,-3,5,3,6,7], k=3):
    //
    //   right=0: arr[0]=1  deque=[0]        (values:[1])  not full
    //   right=1: arr[1]=3  1<=3 → pop 0, add 1
    //                      deque=[1]        (values:[3])  not full
    //   right=2: arr[2]=-1 3>-1 → keep, add 2
    //                      deque=[1,2]      (values:[3,-1]) FULL → result[0]=arr[1]=3
    //   right=3: arr[3]=-3 front=1≥1 ✅, -1>-3→keep, add 3
    //                      deque=[1,2,3]    (values:[3,-1,-3]) → result[1]=arr[1]=3
    //   right=4: arr[4]=5  front=1<2 ❌ pop; -3≤5 pop; -1≤5 pop; 3≤5 pop; add 4
    //                      deque=[4]        (values:[5]) → result[2]=arr[4]=5
    //   right=5: arr[5]=3  front=4≥3 ✅, 5>3→keep, add 5
    //                      deque=[4,5]      (values:[5,3]) → result[3]=arr[4]=5
    //   right=6: arr[6]=6  front=4≥4 ✅, 3≤6 pop; 5≤6 pop; add 6
    //                      deque=[6]        (values:[6]) → result[4]=arr[6]=6
    //   right=7: arr[7]=7  front=6≥5 ✅, 6≤7 pop; add 7
    //                      deque=[7]        (values:[7]) → result[5]=arr[7]=7
    //
    //   Answer: [3, 3, 5, 5, 6, 7] ✅
    //
    // Time : O(n)  — each index added and removed at most once
    // Space: O(k)  — deque holds at most k indices at any time
    // ============================================================
    public int[] maxSlidingWindow(int[] arr, int k) {
        int n = arr.length;
        int[] result = new int[n - k + 1];

        // deque stores INDICES; values at those indices are decreasing
        // front = index of max in current window
        // back  = index of smallest element still potentially useful
        Deque<Integer> deque = new ArrayDeque<>();

        for (int right = 0; right < n; right++) {

            // STEP 1: Remove from FRONT if outside the current window
            // window starts at: right - k + 1
            while (!deque.isEmpty() && deque.peekFirst() < right - k + 1) {
                deque.pollFirst();
            }

            // STEP 2: Remove from BACK any element <= arr[right]
            // Those elements are SMALLER and will exit the window BEFORE arr[right]
            // → they can NEVER be the maximum while arr[right] is present → useless!
            while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[right]) {
                deque.pollLast();
            }

            // STEP 3: Add current index to back of deque
            deque.addLast(right);

            // STEP 4: Record max when window is full
            // Window is full when we've processed at least k elements (right >= k-1)
            if (right >= k - 1) {
                result[right - k + 1] = arr[deque.peekFirst()]; // front = max
            }
        }

        return result;
    }


    // ============================================================
    // Driver — runs all methods on the same test cases
    // ============================================================
    public static void main(String[] args) {
        Solution sol = new Solution();

        int[][] inputs   = {
            {1, 3, -1, -3, 5, 3, 6, 7},
            {1, 2, 3, 1, 4, 5, 2, 3, 6},
            {8, 5, 10, 7, 9, 4},
            {1, 2, 3},
            {5, 4, 3, 2, 1},
            {1, 1, 1, 1}
        };
        int[]   ks        = {3, 3, 3, 1, 3, 2};
        int[][] expected  = {
            {3, 3, 5, 5, 6, 7},
            {3, 3, 4, 5, 5, 5, 6},
            {10, 10, 10, 9},
            {1, 2, 3},
            {5, 4, 3},
            {1, 1, 1}
        };

        System.out.println("=== Sliding Window Maximum ===\n");

        for (int t = 0; t < inputs.length; t++) {
            int[] arr = inputs[t];
            int   k   = ks[t];

            int[] m1 = sol.maxSlidingWindowBrute(arr, k);  // O(nk)
            int[] m2 = sol.maxSlidingWindowHeap(arr, k);   // O(n log k)
            int[] m3 = sol.maxSlidingWindow(arr, k);        // O(n) ⭐

            boolean pass   = Arrays.equals(m3, expected[t]);
            String  status = pass ? "✅ PASS" : "❌ FAIL";

            System.out.println("Test " + (t + 1) + ": " + status);
            System.out.println("  arr      : " + Arrays.toString(arr) + ", k=" + k);
            System.out.println("  Expected : " + Arrays.toString(expected[t]));
            System.out.println("  Brute    : " + Arrays.toString(m1));
            System.out.println("  Heap     : " + Arrays.toString(m2));
            System.out.println("  Deque    : " + Arrays.toString(m3) + "  ⭐");
            System.out.println();
        }
    }
}
