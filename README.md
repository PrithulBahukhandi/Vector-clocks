# Vector-clocks
Distributed systems


*** NOTE : This lab was built on lab-2 (Asynchronous message server) code. Inorder to run this comment out all the jms methods or install jms and import jms libraries. ****

I have implemented a message service consisting of a server process and three clients. Each client process will connect
to the server over a socket. The server is able to handle all three clients concurrently. Clients will maintain
vector clocks, and the messages exchanged via the message service will be constrained exclusively to those vector
clocks.
Each client will be assigned a fixed username: A, B, or C. The username will be selected by the user when the client
starts and will remain the same for the duration of the lab assessment. The server displays which clients are
connected; however, it is assumed that all three clients are be connected to the server on commencement of the lab
run, and will remain connected until the conclusion of the lab run.
Two or more clients may not use the same username simultaneously. Should the server detect a concurrent conflict in
username, the client’s connection should be rejected, and the client’s user should be prompted to select a different
name.
Each client will maintain a vector clock. Every two to ten seconds, each client will randomly choose one other client
(e.g., a unicast) and send that client its vector clock. 

When a client sends a message, it will print the intended recipient of the message, as well as its updated vector clock, to
its respective GUI. When a client receives a message, it will update and print its vector clock, along with the sender’s ID.
All clients should remain connected to the server until manually disconnected by the user.
