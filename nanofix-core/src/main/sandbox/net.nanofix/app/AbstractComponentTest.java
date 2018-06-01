package net.nanofix.app;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * User: Mark
 * Date: 23/04/12
 * Time: 06:20
 */
public class AbstractComponentTest {

    class DummyComponent extends AbstractComponent {}

    @Test
    public void testOpen() throws Exception {
        Component component = new DummyComponent();
        assertThat("opened", component.isOpen(), is(false));
        assertThat("started", component.isStarted(), is(false));
        assertThat("stopped", component.isStopped(), is(false));
        assertThat("closed", component.isClosed(), is(false));
        assertThat("state", component.getState(), is(ComponentState.New));

        component.open();
        assertThat("opened", component.isOpen(), is(true));
        assertThat("started", component.isStarted(), is(false));
        assertThat("stopped", component.isStopped(), is(false));
        assertThat("closed", component.isClosed(), is(false));
        assertThat("state", component.getState(), is(ComponentState.Open));
    }

    @Test(expected = ComponentStateException.class)
    public void testOpenException() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.open();
    }

    @Test
    public void testStart() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.start();
        assertThat("opened", component.isOpen(), is(false));
        assertThat("started", component.isStarted(), is(true));
        assertThat("stopped", component.isStopped(), is(false));
        assertThat("closed", component.isClosed(), is(false));
        assertThat("state", component.getState(), is(ComponentState.Started));
    }

    @Test(expected = ComponentStateException.class)
    public void testStartException() throws Exception {
        Component component = new DummyComponent();
        component.start();
    }

    @Test
    public void testRefresh() throws Exception {

    }

    @Test
    public void testStop() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.start();
        component.stop();
        assertThat("opened", component.isOpen(), is(false));
        assertThat("started", component.isStarted(), is(false));
        assertThat("stopped", component.isStopped(), is(true));
        assertThat("closed", component.isClosed(), is(false));
        assertThat("state", component.getState(), is(ComponentState.Stopped));
    }

    @Test(expected = ComponentStateException.class)
    public void testStopException() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.stop();
    }

    @Test
    public void testClose() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.start();
        component.stop();
        component.close();
        assertThat("opened", component.isOpen(), is(false));
        assertThat("started", component.isStarted(), is(false));
        assertThat("stopped", component.isStopped(), is(false));
        assertThat("closed", component.isClosed(), is(true));
        assertThat("state", component.getState(), is(ComponentState.Closed));
    }

    @Test(expected = ComponentStateException.class)
    public void testCloseException() throws Exception {
        Component component = new DummyComponent();
        component.open();
        component.start();
        component.close();
    }
}
