/**
 * 95-771 Data Structures and Algorithms
 * @author Yueh Liu
 * Assignment #1
 **/
package edu.cmu.andrew.yuehl;
import edu.colorado.nodes.ObjectNode;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList {
    private ObjectNode head;
    private ObjectNode tail;
    private int size;
    // use ptr as iterator
    private static ObjectNode ptr;
    /**
     * Constructs a new SinglyLinkedList object.
     */
    public SinglyLinkedList() {
        head = null;
        tail = null;
    }

    /**
     * reset the iterator to the beginning of the list That is, set a reference to the head of the list
     */
    public void reset() {
//        iterator();
        ptr = head;
    }

    /**
     * @precondition: ptr is not null
     * return the Object pointed to by the iterator and increment the iterator to the next node in the list.
     * This reference becomes null if the object returned is the last node on the list.
     * @return
     */

    public Object next() throws Exception {
//        return iterator().next();
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
        if (ptr.getLink() != null) {
            return true;
        }
        return false;
    }
    /**
     * Add a node containing the Object c to the head of the linked list.
     * @param c - -- a single Object This method increments a count of the number of nodes on the list.
     * The count is returned by countNodes.
     */
    public void addAtFrontNode(Object c) {
        head = new ObjectNode(c,head);
        if (size == 0) {
            tail = head;
        }
        // increase size
        size++;
    }
    /**
     * Add a node containing the Object c to the end of the linked list. No searching of the list is required.
     * The tail pointer is used to access the last node in O(1) time.
     * @param c - -- a single Object
     */
    public void addAtEndNode(Object c) {
        if (head == null) {
            addAtFrontNode(c);
            return;
        }
        // traverse to the end
        ObjectNode tmp = head;
        while (tmp.getLink() != null) {
            tmp = tmp.getLink();
        }
        // when reach tail node
        ObjectNode newTail = new ObjectNode(c,null);
        tmp.setLink(newTail);
        tail = newTail;
        // increase size
        size++;
    }
    /**
     * Counts the number of nodes in the list
     * @return the number of nodes in the list between head and tail inclusive. No list traversal is performed. Simply return the value of countNodes.
     */
    public int countNodes() {
        return size;
    }
    /**
     * Returns a reference (0 based) to the object with list index i. *
     * @param i
     * @return reference to an object with list index i. The first object in the list is at position 0.
     */
    public Object getObjectAt(int i) {
        if (head == null) {
            return BigInteger.valueOf(-1);
        } else if (i < 0 || i > size) {
            return BigInteger.valueOf(-1);
        } else {
            ObjectNode tmp = head;
            for (int j = 0; j < i; j++) {
                tmp = tmp.getLink();
            }
            return tmp.getData();
        }
    }
    /**
     * Returns the data in the tail of the list
     * @return the data in the tail of the list
     */
    public Object getLast() {
        return tail.getData();
    }
    /**
     * Returns the data in the head of the list
     * @return the data in the head of the list
     */
    public Object getHeadNode() {
        return head;
    }
    public Object reverseList(ObjectNode head) {
        // Define ListNode prev that serves as head.next
        ObjectNode prev = null;
        while (head != null) {
            ObjectNode newHead = head.getLink(); // save the value before we move head pointer
            head.setLink(prev); // head.next points to prev
            prev = head; // prev points to pld head
            head = newHead;
        }
        return prev; // which is the head that hasn't updated for next round
    }
    /**
     * Returns the list as a String
     *
     * @return a String containing the Objects in the list
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ObjectNode pointer = this.head;
        while (pointer != null) {
            result.append(pointer.getData());
            pointer = pointer.getLink();
        }
        return result.toString();
    }
    /**
     * test each method of the class
     * Test Driver: Testing with BigInteger data and a list of lists.
     * @param args
     */
    public static void main(String[] args) {
        SinglyLinkedList head = new SinglyLinkedList();
        for (int i = 25; i >= 0; i--) {
            Object item = (char)(97 + i);
            head.addAtFrontNode(item);
        }
        System.out.println(head.toString());
        System.out.println("test addAtFrontNode()");
        head.addAtFrontNode('A');
        System.out.println(head.toString());

        System.out.println("test addAtEndNode()");
        head.addAtEndNode('B');
        System.out.println(head.toString());

        System.out.println("test countNodes()");
        System.out.println(head.countNodes());

        System.out.println("test getObjectAt");
        System.out.println(head.getObjectAt(27));

        System.out.println("test getLast()");
        System.out.println(head.getLast());

        System.out.println("ptr before reset()");
        System.out.println(ptr);
        System.out.println("ptr after reset()");
        head.reset();
        System.out.println(ptr);
        System.out.println("test hasNext() & next()");

        while (head.hasNext()) {
            try {
                System.out.println(head.next().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


