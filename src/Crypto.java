/**
 * 95-771 Data Structures and Algorithms
 * @author Yueh Liu
 * Assignment #1
 * how key generation and the Merkle Hellman Knapsack cryptography is being performed:
 * 1. Use two SinglyLinkedLists to hold W,B
 * 2. Use createPrivateKey() to populate SinglyLinkedList W with superincreasing sequence of powers of 7
 * 3. Use createPublicKey() to populate SinglyLinkedList B,taking input parameter W, to calculate according bi with helper methods of createQ() and createR()
 * 4. encrypt()
     * encode each char of user input into Binary String, if the char is less than 8 bits then padding it with 0s, concatenate all Binary Strings into one Binary String and padding it to the length of 640.
     * calculate encrypted where bit value == 1
 *   * Let m be an n-bit message consisting of bits m1,m2...mn, with mi the highest order bit. Select each bi for which {\displaystyle mi is nonzero, and add them together.
 * 5. decrypt()
    * Conduct a series of modInverse calculation (see: https://en.wikipedia.org/wiki/Merkle%E2%80%93Hellman_knapsack_cryptosystem)
    * Keep subtract node.getData() in W from cprime as long as data until cprime == 0
    * Store all the index of value in last operation and perform the formula to calculate BigInteger m
    * Translate m back into binary string then use binaryToString() translate back to string
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.cmu.andrew.yuehl.SinglyLinkedList;
import edu.colorado.nodes.ObjectNode;

public class Crypto {
    private SinglyLinkedList W;
    private SinglyLinkedList B;
    BigInteger q;
    BigInteger r;

    public Crypto() {
        W = new SinglyLinkedList();
        B = new SinglyLinkedList();
    }

    /**
     * Key generation: Create a key to encrypt 8-bit numbers by creating a random super increasing sequence of 8 values
     * reference: https://stackoverflow.com/questions/29684733/please-explain-this-code-for-merkle-hellman-knapsack-cryptosystem
     * @param length
     */
    public void createPrivateKey(int length) { // user will input 640
        for (int i = 0; i < length; i++) { // each character can be represented in 8 bits
            BigInteger n = powerOfSeven(i);
            W.addAtEndNode(n);
        }
    }
    // generate powerOfSeven

    /**
     * @param i
     * @return the value of 7's power i
     */
    private BigInteger powerOfSeven(int i) {
        BigInteger b = new BigInteger("7");
        return b.pow(i);
    }

    /**
     * @param list
     * @return BigInteger that is bigger than the sum of nodes
     */
    private BigInteger createQ(SinglyLinkedList list) {
        Random rand = new Random();
        BigInteger r = BigInteger.valueOf(rand.nextInt(10));
        return sumOfNodes(list).add(r);
    }
    /**
     * reference: https://www.geeksforgeeks.org/sum-of-the-nodes-of-a-singly-linked-list/
     * @param w
     */
    static BigInteger sumOfNodes(SinglyLinkedList w) {
        BigInteger sum = new BigInteger("0");
        ObjectNode head = (ObjectNode)(w.getHeadNode());

        // traverse the nodes
        while (head != null) {
            sum = sum.add((BigInteger) (head.getData()));
            head = head.getLink();
        }
        // accumulate sum
        return sum;
    }

    /**
     * choose the next prime of q so that gcd(q,r) = 1
     * @param q
     * @return BigInteger r
     */
    private BigInteger createR(BigInteger q) {
        BigInteger r = q.nextProbablePrime();
        return r;
    }
    // createPublicKey
    public void createPublicKey(SinglyLinkedList w) {
        q = createQ(w);
        r = createR(q);
        ObjectNode head = (ObjectNode)(w.getHeadNode());
        while (head != null) {
            BigInteger b = ((BigInteger)head.getData()).multiply(r).mod(q);
            B.addAtEndNode(b);
            head = head.getLink();
        }
    }
    // encrypt
    public String encrypt(String input) { // string
        SinglyLinkedList list = B;
        ObjectNode node = (ObjectNode)list.getHeadNode();
        BigInteger encrypted = new BigInteger("0");
        int fullLength = B.countNodes(); // 640

        String binaryString = "";
        for (int i = 0; i < input.length(); i++) {
            String charBinaryString = Integer.toBinaryString(input.charAt(i)); // "A" == 1000001 -> "0000..."+1000001 -> 640
            binaryString += "0".repeat(8-charBinaryString.length())+charBinaryString;
        }
        binaryString = "0".repeat(fullLength-binaryString.length())+binaryString;

        for (int j = 0; j < binaryString.length();j++) {
            if (binaryString.charAt(j) == '1' && node != null) {
                encrypted = encrypted.add((BigInteger)list.getObjectAt(j));
            }
            node = node.getLink();
        }
        return encrypted.toString();
    }

    /**
     * Conduct a series of modInverse calculation (see: https://en.wikipedia.org/wiki/Merkle%E2%80%93Hellman_knapsack_cryptosystem)
     * Keep subtract node.getData() in W from cprime as long as data until cprime == 0
     * Store all the index of value in last operation and perform the formula to calculate BigInteger m
     * Translate m back into binary string then use binaryToString() translate back to string
     * reference: https://mkyong.com/java/java-convert-string-to-binary/
     * reference: https://stackoverflow.com/questions/4211705/binary-to-text-in-java
     * @param encrypt
     * @return decrypted String
     */
    public String decrypt(String encrypt) {
        // String -> BigInteger
        BigInteger encrypted = new BigInteger(encrypt);
        BigInteger rprime = r.modInverse(q);
        BigInteger cprime = encrypted.multiply(rprime).mod(q);

        // Use the greedy algorithm to decompose encrypted into a sum of wi values
        ObjectNode head = (ObjectNode)(W.getHeadNode());
        ObjectNode tail = (ObjectNode)(W.reverseList(head));
        List<Integer> index = new ArrayList<>();
        int idx = 0;
        int length = W.countNodes();
        // condition: >=, try next one (try 640 times)
        while (tail != null && idx < length) {
            if (cprime.compareTo((BigInteger)tail.getData()) >= 0) {
                cprime = cprime.subtract((BigInteger)tail.getData());
                tail = tail.getLink();
                index.add(idx);
                idx++;
            } else {
                tail = tail.getLink();
                idx++;
            }
        }

        // index: 1010111100..... => binary string => bigInteger => String
        BigInteger m = new BigInteger("0");
        BigInteger b = new BigInteger("2");
        for (int i = 0; i < index.size(); i++) {
            m = m.add(b.pow(index.get(i)));
        }
        String binaryString = m.toString(2);

        if (binaryString.length() % 8 != 0) {
            binaryString = "0" + binaryString;
        }

        ArrayList<String> s = new ArrayList<>();
        StringBuilder subString = new StringBuilder();
        int cnt = 1;
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            subString.append(binaryString.charAt(i));
            if (cnt % 8 == 0) {
                s.add(subString.reverse().toString());
                subString = new StringBuilder();
            }
            cnt++;
        }
        StringBuilder ans = new StringBuilder();
        for (String i:s) {
            ans.append(binaryToString(i));
        }
        return ans.reverse().toString();
    }

    /**
     * reference: https://stackoverflow.com/questions/14498804/binary-string-to-text-string-java-code
     * @param input
     * @return
     */
    public String binaryToString(String input){
        String[] ss = input.split( " " );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < ss.length; i++ ) {
            sb.append( (char)Integer.parseInt( ss[i], 2 ) );
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        Crypto crypto = new Crypto();
        // createPrivateKey
        crypto.createPrivateKey(640);
//        System.out.println("crypto.W = "+crypto.W);
        // sumOfNodes
//        System.out.println(crypto.sumOfNodes(crypto.W));
        // createQ
//        System.out.println(crypto.createQ(crypto.W));
        // createR
//        System.out.println(crypto.createR(crypto.createQ(crypto.W)));
        // createPublicKey
        crypto.createPublicKey(crypto.W);
        String test = "Welcome to Data Structures and Algorithms";
        System.out.println("Test String is: "+test);
        System.out.println("Result of decryption: "+crypto.decrypt(crypto.encrypt(test)));
    }
}
