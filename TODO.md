# TODO

_Specification \<number\>_ below refers to the section in the paper [A comprehensive study of Convergent and Commutative Replicated Data Types](http://hal.upmc.fr/docs/00/55/55/88/PDF/techreport.pdf) describing the CRDT.

<del>Scratched out</del> means DONE.

## Documentation

Write docs for: 

* <del>The functionality and semantics of the CvRDT</del>
* <del>The JSON view of each CvRDT</del>
* <del>Simple JavaScript demo querying the REST server</del>
* <del>How to start up the cluster service</del>
* <del>REST API</del>
* <del>Scala CRDT API</del>
* <del>How to run the DemoRestServer</del>
* <del>Architecture (pub/sub, leveldb) etc.</del>
* <del>Configuration</del>
* <del>LevelDB and other storage options</del>
* <del>Explain difference between CvRDT and CmRDT</del>
* More background and concepts around CRDT, CAP and CALM

## CvRDTs (state-based)

### Counters

* <del>Specification 6  State-based increment-only counter</del>
* <del>Specification 7  State-based PN-Counter</del>
* Implement Handoff Counter as described in [this paper](http://arxiv.org/abs/1307.3207) and this [code](https://github.com/pssalmeida/clj-crdt/blob/master/src/crdt/handoff_counter.clj)

### Sets

* <del>Specification 11 State-based grow-only Set (G-Set)</del>
* <del>Specification 12 State-based 2P-Set</del>
* Optimized OR Set - as defined in [An Optimized Conﬂict-free Replicated Set](http://pagesperso-systeme.lip6.fr/Marc.Shapiro/papers/RR-8083.pdf)

### Registers

* Specification 8       State-based Last-Writer-Wins Register (LWW Register)
* Specification 10      State-based Multi-Value Register (MV-Register)

## CmRDTs (ops-based)

### Counters

* Specification 5 op-based Counter

### Sets

* Specification 9 Op-based LWW-Register
* Specification 13 U-Set: Op-based 2P-Set with unique elements
* Specification 14 Molli, Weiss, Skaf Set
* Optimized OR Set - as defined in [An Optimized Conﬂict-free Replicated Set](http://pagesperso-systeme.lip6.fr/Marc.Shapiro/papers/RR-8083.pdf)

### Graphs

* Specification 16 2P2P-Graph (op-based)
* Specification 17 Add-only Monotonic DAG (op-based)
* Specification 18 Add-Remove Partial Order

### Array

* Specification 19 Replicated Growable Array (RGA)

### Shopping Cart

* Specification 21 Op-based Observed-Remove Shopping Cart (OR-Cart)

## Misc Stuff

### Reliable Broadcast for CmRDTs

* Using akka-persistence

### Client API 
* Write a JavaScript CRDT library. So a client can grab the real JSON representation, modify it, do the merges as needed and then push the result back to the server.
* Java API for CRDT classes/ClusterClient

### Replication

* If a new node joins the cluster then the current state for all CvRDTs is not immediately replicated. It will be eventually - for all the CvRDTs that are updated again. This is not good. How can we solve this? 

