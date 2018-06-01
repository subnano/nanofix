package net.nanofix.message.selector;

import net.nanofix.message.DefaultFIXMessageFactory;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 11:52
 */
public class MsgTypeSelectorTest {

    private DefaultFIXMessageFactory messageFactory;

    @Before
    public void setup() {
        messageFactory = new DefaultFIXMessageFactory(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        new MsgTypeSelector(Arrays.asList(new String[] {}));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new MsgTypeSelector("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNullInList() {
        new MsgTypeSelector(Arrays.asList(new String[] { null }));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDuplicatesInList() {
        new MsgTypeSelector(Arrays.asList(new String[] { "A", "B", "C", "A" }));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testEmptyStringInListConstructor() {
        new MsgTypeSelector(Arrays.asList(new String[] { "" }));
    }

    public void testIsSelected() throws Exception {
        assertThat(new MsgTypeSelector("A").select(createMessage("A")), is(true));
    }

    public void testIsNotSelected() throws Exception {
        assertThat(new MsgTypeSelector("A").select(createMessage("B")), is(false));
    }

    private FIXMessage createMessage(String msgType) {
        return messageFactory.createMessage(new MsgType(msgType));
    }
}
