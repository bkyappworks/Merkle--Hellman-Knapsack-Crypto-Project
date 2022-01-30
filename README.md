# Merkle--Hellman-Knapsack-Crypto-Project
Implement key generation, encryption and decryption using the Merkle-Hellman Knapsack Cryptosystem.

Reference: http://en.wikipedia.org/wiki/Merkle–Hellman_knapsack_cryptosystem

Note that the example provided on the wiki is an example using small integers with w = {2, 7, 11, 21, 42, 89, 180, 354}. In this project, w will consist of 640 huge integers.

SinglyLinkedList will be used to hold two lists of Java BigIntegers. One list, w, will be used to hold the superincreasing sequence of integers that make up part of the private key and used for decryption. Powers of 7 make up superincreasing sequence. The second list, b, will be used to hold the public key material used for encryption. 

