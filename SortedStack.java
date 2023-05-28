/**
 * Write a program to sort a stack such that the smallest items are on the top. You can use
 * an additional temporary stack, but you may not copy the elements into any other data structure
 * (such as an array). The stack supports the following operations: push, pop, peek, and isEmpty.
 *
 * For this challenge, we'll assume the following Stack structure:
 *
 * public class Stack<T> {
 *
 *   private Node<T> top;
 *
 *   private static class Node<T> {
 *     private final T data;
 *     private Node<T> next;
 *
 *     public Node(T data) {
 *       this.data = data;
 *     }
 *   }
 *
 *   public void push(T data) {
 *     Node<T> node = new Node<>(data);
 *     node.next = top;
 *     top = node;
 *   }
 *
 *   public T peek() {
 *     if (top == null) {
 *       throw new EmptyStackException();
 *     }
 *
 *     return top.data;
 *   }
 *
 *   public boolean isEmpty() {
 *     return top == null;
 *   }
 *
 *   public T pop() {
 *     if (top == null) {
 *       throw new EmptyStackException();
 *     }
 *
 *     T data = top.data;
 *     top = top.next;
 *
 *     return data;
 *   }
 * }
 *
 * Following the constraints, we might use only 1 Stack to hold the data temporarily. One way to achieve the desired outcome is to follow
 * the logic of Insertion Sort. Each time we add an element, we visit the existing ones (let's assume they are already sorted in ASC order)
 * and comparing them with it.
 *
 * - If the <b>value</b> is bigger than then currently visited one: proceed forward until a smaller or equal one is found, or there are no more elements to visit.
 * - If the <b>value</b> is smaller or equal to the currently visited one, or there are no more elements to visit:
 * we found the place for it that will preserve the desired order, and we might need to shift the elements on the right to allocate space to be added.
 *
 * To incorporate this behavior, we could put the logic in the <b>push()</b> method.
 *
 * Each time an element <b>P</b> is <b>pushed</b>, we start visiting the existing elements by <b>peeking</b>:
 *
 *  - if P is bigger than peek(), we pop() the element from the current stack and push() it in the temporary one. We proceed this way until we find peek()
 *  that is bigger or equal to P, or there are no more elements to visit.
 *  - if P is less than or equal to peek(), or there are no more elements to visit,
 *  we put it in that place and all the items in the temporary Stack should be restored in the primary one, if any.
 *
 *  So far.. on each <b>push()</b>, we place the smaller elements in the buffer, put the element in the next available position in <b>primary</b> and then
 *  push them back to <b>primary</b> stack. What about the scenarios where there are <b>N</b> consecutive <b>push()</b> operations with each value
 *  bigger than the previously added one?
 *  One way to optimize accordingly is to <b>not</b> restore the elements from the <b>buffer</b> on each push(), but only when we need them.
 *
 *  When will that be the case?
 *
 *  - pop() & peek() requires the element on top, so the elements from buffer should be restored back to primary
 *  - push() when the new element is smaller than those in the buffer (only the bigger elements from buffer will be pushed back)
 *  - for all other cases, the bigger elements will be in the primary while the smaller ones on the buffer
 *
 * This solution uses an approach similar to the one in <link>QueueViaStacks</link>
 */
public class SortedStack extends Stack<Integer> {
  private final Stack<Integer> buffer = new Stack<>();

  @Override
  public void push(Integer data) {
    if (data == null) {
      throw new IllegalArgumentException("Null is not allowed");
    }

    // put elements < data in buffer
    while (!isEmpty() && data > super.peek()) {
      buffer.push(super.pop());
    }

    // put elements > data in primary stack
    while (!buffer.isEmpty() && data < buffer.peek()) {
      super.push(buffer.pop());
    }

    // when the transfers above are done
    // the data should be in its correct (ordered) position
    super.push(data);
  }

  @Override
  public Integer peek() {
    pushToPrimary();

    return super.peek();
  }

  @Override
  public Integer pop() {
    pushToPrimary();

    return super.pop();
  }

  private void pushToPrimary() {
    while (!buffer.isEmpty()) {
      super.push(buffer.pop());
    }
  }
}
