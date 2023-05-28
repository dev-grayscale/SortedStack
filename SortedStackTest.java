import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.EmptyStackException;

public class SortedStackTest {

  @Test
  public void test() {
    SortedStack sortedStack = new SortedStack();

    Assertions.assertTrue(sortedStack.isEmpty());
    Assertions.assertThrows(EmptyStackException.class, sortedStack::peek);
    Assertions.assertThrows(EmptyStackException.class, sortedStack::pop);

    Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> sortedStack.push(null));
    Assertions.assertEquals("Null is not allowed", t.getMessage());

    sortedStack.push(4);

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());
    Assertions.assertEquals(4, sortedStack.peek());

    sortedStack.push(2);

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());
    Assertions.assertEquals(2, sortedStack.peek());

    sortedStack.push(1);

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());
    Assertions.assertEquals(1, sortedStack.peek());

    sortedStack.push(5);

    Assertions.assertFalse(getBuffer(sortedStack).isEmpty());
    Assertions.assertEquals(1, sortedStack.peek());
    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(1, sortedStack.pop());
    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(2, sortedStack.pop());
    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(4, sortedStack.peek());
    Assertions.assertEquals(4, sortedStack.pop());
    Assertions.assertEquals(5, sortedStack.pop());

    Assertions.assertTrue(sortedStack.isEmpty());
    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    sortedStack.push(2);

    Assertions.assertEquals(2, sortedStack.peek());

    sortedStack.push(4);

    Assertions.assertFalse(getBuffer(sortedStack).isEmpty()); // 2 is in buffer
    Assertions.assertFalse(sortedStack.isEmpty()); // 4 is in primary

    sortedStack.push(5);

    Assertions.assertFalse(getBuffer(sortedStack).isEmpty()); // 2,4 is in buffer
    Assertions.assertFalse(sortedStack.isEmpty()); // 5 is in primary

    sortedStack.push(1);

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty()); // buffer should be empty
    Assertions.assertFalse(sortedStack.isEmpty()); // 5,4,2,1 in primary

    sortedStack.push(0);

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty()); // buffer should be empty
    Assertions.assertFalse(sortedStack.isEmpty()); // 5,4,2,1,0 in primary

    sortedStack.push(3);

    Assertions.assertFalse(getBuffer(sortedStack).isEmpty()); // 1,2 in buffer
    Assertions.assertFalse(sortedStack.isEmpty()); // 5,4,3 in primary

    Assertions.assertEquals(0, sortedStack.peek());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty()); // buffer should be empty
    Assertions.assertFalse(sortedStack.isEmpty()); // all elements in primary

    Assertions.assertEquals(0, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(1, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(2, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(3, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(4, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());

    Assertions.assertEquals(5, sortedStack.pop());

    Assertions.assertTrue(getBuffer(sortedStack).isEmpty());
    Assertions.assertTrue(sortedStack.isEmpty());
  }

  private Stack<Integer> getBuffer(SortedStack sortedStack) {
    Try<Object> bufferField = ReflectionUtils.tryToReadFieldValue(SortedStack.class, "buffer", sortedStack);

    try {
      return (Stack<Integer>) bufferField.get();
    } catch (Exception ignored) {
    }

    return null;
  }
}
