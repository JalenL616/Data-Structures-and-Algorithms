# Deques and Randomized Queues

This project, completed for my high school data structures and algorithms course, involves the **implementation of two fundamental data structures: a double-ended queue (Deque) and a randomized queue**. This project is based on the programming project from Princeton University's "Algorithms, Part 1" course on Coursera.
The goal is to create generic, efficient, and iterable implementations of these data structures from scratch, without using Java's built-in collections library for the underlying storage.

https://www.coursera.org/learn/algorithms-part1/programming/zamjZ/deques-and-randomized-queues

**Project Overview**

This project focuses on the creation of two related, yet distinct, queue-like data structures:
**Deque**: A generalization of a queue that allows adding and removing items from either the front or the back.
**Randomized Queue**: A queue variant where the item removed is chosen uniformly at random from the items currently in the queue.
Both data structures are implemented to be generic and include iterators that operate independently of one another.

**Deque**

A double-ended queue, or Deque, is a sequence of items where you can add or remove items from either the "front" or the "back" of the queue. It combines the functionality of both a stack and a queue.
The Deque is implemented using a **doubly-linked list**. This approach was chosen because it allows for efficient addition and removal of nodes at both ends (head and tail) of the list.
Each node in the list stores an item, a reference to the next node, and a reference to the previous node.
The Deque class maintains references to the first and last nodes of the list.
This structure guarantees that all operations (addFirst, addLast, removeFirst, removeLast) are performed in constant time in the worst case.

**Randomized Queue**

A Randomized Queue is similar to a standard queue, except that the dequeue operation removes a random item. The sample operation allows for inspecting a random item without removing it.
The Randomized Queue is implemented using a **resizing array**. This data structure was chosen to provide efficient access to any item by its index, which is necessary for selecting a random item.
Items are added to the end of the array.
To dequeue, a random index between 0 and N-1 (where N is the number of items) is chosen. The item at that random index is swapped with the item at the last position (N-1), and then the last item is removed. This avoids gaps in the array and makes removal an efficient operation.
The underlying array is doubled in size when it becomes full and halved when it becomes one-quarter full to maintain performance guarantees.
All operations take constant amortized time.

**Dependencies**

This project requires the algs4.jar library from the Princeton Algorithms, Part I course, which provides the necessary standard libraries for I/O and testing.
