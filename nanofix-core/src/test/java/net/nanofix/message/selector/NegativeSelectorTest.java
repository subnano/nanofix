package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 19:24
 */
public class NegativeSelectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
        new NegativeSelector(null);
    }

    @Test
    public void testIsSelected() throws Exception {
        MessageSelector selector = mock(MessageSelector.class);
        when(selector.isSelected(any(FIXMessage.class))).thenReturn(true);
        assertThat(new NegativeSelector(selector).isSelected(mock(FIXMessage.class)), is(false));
    }
}
