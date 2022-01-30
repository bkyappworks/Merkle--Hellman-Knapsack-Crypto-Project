package edu.cmu.andrew.yuehl;

import edu.colorado.nodes.ObjectNode;

import java.util.Random;

public class OrderedLinkedListOfIntegers {
    // member data
    private ObjectNode head;
    private ObjectNode tail;
    private int size;
    private static ObjectNode ptr; // provide for list iteration
    //constructor
    OrderedLinkedListOfIntegers(){
        head = null;
        tail = null;
    }
    /**
     * reset the iterator to the beginning of the list That is, set a reference to the head of the list
     */
    public void reset() {
        ptr = head;
    }
    /**
     * @precondition: ptr is not null
     * return the Object pointed to by the iterator and increment the iterator to the next node in the list.
     * This reference becomes null if the object returned is the last node on the list.
     * @return
     */

    public Object next() throws Exception {
        if (ptr != null) {
            ptr = ptr.getLink();
            return ptr;
        }
        throw new Exception();
    }
    /**
     * true if the iterator/pointer is not null
     */

    public boolean hasNext() {
        if (ptr != null) {
            return true;
        }
        return false;
    }

    /**
     * Allow integers to be added to the list.
     * Always maintain the list of integers in increasing order.
     * reference: https://www.netjstech.com/2019/03/sorted-linked-list-in-java.html
     */
    public void sortedAdd(int val) {
        ObjectNode newNode = new ObjectNode(val,null);
        ObjectNode cur = head;
        ObjectNode prev = null;
        // loop through the Linked list until find a node with value greater than val
        while (cur != null && val > (int)(cur.getData())) {
            prev = cur;
            cur = cur.getLink();
        }
        // cur == null || val <= (int)(cur.getData())
        if (prev == null) {
            head = newNode;
        } else {
            prev.setLink(newNode);
        }
        newNode.setLink(cur);
    }

    /**
     *
     * @return a new OrderedLinkedListOfIntegers that holds the merged contents of its two OrderedLinkedListOfIntegers parameters
     */
    public static Object merge(OrderedLinkedListOfIntegers list1,OrderedLinkedListOfIntegers list2) {
        // If two empty lists are passed to merge() then merge() will return an empty list
        ObjectNode merged = new ObjectNode(0,null);
        ObjectNode cur = merged;
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ObjectNode cur1 = list1.head;
        ObjectNode cur2 = list2.head;
        while (cur1 != null && cur2 != null) {
            if ((int)(cur1.getData()) < (int)(cur2.getData())) {
                cur.setLink(cur1);
                cur1 = cur1.getLink();
            } else {
                cur.setLink(cur2);
                cur2 = cur2.getLink();
            }
            cur = cur.getLink();
        }
        // until this point, either list1 or list2 is null
        if (cur1 != null) {
            cur.setLink(cur1);
            cur1 = cur1.getLink();
        }
        if (cur2 != null) {
            cur.setLink(cur2);
            cur2 = cur2.getLink();
        }
        return merged.getLink();
    }

    /**
     * Returns the list as a String
     * @return a String containing the Objects in the list
     */
    @Override
    public String toString() {
        return head + " ";
    }

    public static void main(String[] args) {
        OrderedLinkedListOfIntegers head = new OrderedLinkedListOfIntegers();
        System.out.println(head.toString());
        head.sortedAdd(5);
        System.out.println(head.toString());
        head.sortedAdd(1);
        System.out.println(head.toString());
        head.sortedAdd(2);
        System.out.println(head.toString());
        OrderedLinkedListOfIntegers head2 = new OrderedLinkedListOfIntegers();
        head2.sortedAdd(4);
        head2.sortedAdd(3);
        System.out.println(head2.toString());
        System.out.println("Test merge()");
        System.out.println(merge(head,head2));
        System.out.println("Test merge() with empty lists");
        OrderedLinkedListOfIntegers head3 = new OrderedLinkedListOfIntegers();
        OrderedLinkedListOfIntegers head4 = new OrderedLinkedListOfIntegers();
        System.out.println(merge(head3,head4));
        OrderedLinkedListOfIntegers head5 = new OrderedLinkedListOfIntegers();
        for (int i = 0; i < 20; i++) {
            Random rand = new Random();
            int item = rand.nextInt(10);
            head5.sortedAdd(item);
        }
        OrderedLinkedListOfIntegers head6 = new OrderedLinkedListOfIntegers();
        for (int i = 0; i < 20; i++) {
            Random rand = new Random();
            int item = rand.nextInt(10);
            head5.sortedAdd(item);
        }
        System.out.println(merge(head5,head6).toString());
    }
}
