package org;

public class ThreeNum {


    public ListNode add(ListNode one, ListNode other) {
        ListNode head = new ListNode();
        ListNode result = head;
        while (one != null && other != null) {
            result.next = new ListNode(one.val + other.val);
            result = result.next;

            one = one.next;
            other = other.next;
        }

        result = head.next;
        while (result != null) {
            if (result.val > 9) {
                result.val = result.val % 10;

                if (result.next != null) {
                    result.next.val = result.next.val + 1;
                } else {
                    result.next = new ListNode(1);
                }

            }
            result = result.next;
        }

        return head;
    }

    // 2->4->3  5>6>4
    // 708
    // 5>6>7  7>8>9
    // 2571
    public static void main(String[] args) {
        ThreeNum threeNum = new ThreeNum();
        ListNode one = new ListNode(2);
        one.next = new ListNode(4);
        one.next.next = new ListNode(3);

        ListNode other = new ListNode(5);
        other.next = new ListNode(6);
        other.next.next = new ListNode(4);

        /*one = new ListNode(5);
        one.next = new ListNode(6);
        one.next.next = new ListNode(7);

        other = new ListNode(7);
        other.next = new ListNode(8);
        other.next.next = new ListNode(9);*/
        ListNode listNode = threeNum.add(one, other);
    }
}

class ListNode {
    Integer val;
    ListNode next;

    public ListNode() {
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public ListNode(int val) {
        this.val = val;
    }
}
