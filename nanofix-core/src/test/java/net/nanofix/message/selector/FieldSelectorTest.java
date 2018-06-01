package net.nanofix.message.selector;

import net.nanofix.field.StringField;
import net.nanofix.message.FIXMessage;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.hamcrest.Matchers.*;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 06:20
 */
public class FieldSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        new FieldSelector(null);
    }

    @Test
    public void testIsSelected() throws Exception {
        FIXMessage msg = mock(FIXMessage.class);
        //TODO when(msg.getFieldValue(11)).thenReturn("abc");
        assertThat(new FieldSelector(new StringField(11, "abc")).select(msg), is(true));
    }
}
