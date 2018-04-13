package net.nanofix.session;

import net.nanofix.core.connector.Connector;
import net.nanofix.store.MessageStoreFactory;
import net.nanofix.util.ListenerSupport;

import static net.nanofix.util.Arguments.checkNotNull;

/**
 * Right now this accepts any inbound connection
 * It also uses one store factory for all sessions
 * todo add support to support white/black lists
 * todo add support to create from static sessionConfig only
 * todo add support for destroySession
 * User: Mark
 * Date: 20/04/12
 * Time: 18:38
 */
public class DynamicSessionFactory extends ListenerSupport<SessionFactoryListener> implements SessionFactory {

    private final SessionInfoBuilder builder = new SessionInfoBuilder();
    private final MessageStoreFactory messageStoreFactory;

    public DynamicSessionFactory(SessionFactoryListener listener, MessageStoreFactory messageStoreFactory) {
        this.messageStoreFactory = messageStoreFactory;
        addListener(listener);
    }

    @Override
    public Session createSession(SessionId sessionId, Connector connector) {
        checkNotNull("sessionId", sessionId);
        checkNotNull("connector", connector);
        Session session = createSessionFromSessionId(connector, sessionId);
        for (SessionFactoryListener listener : getListeners()) {
            listener.sessionCreated(connector, session);
        }
        return session;
    }

    private Session createSessionFromSessionId(Connector connector, SessionId sessionId) {
        return new NanoSession(
                connector,
                builder.reset()
                        .fromSessionId(sessionId)
                        .build(),
                messageStoreFactory.create(sessionId)
        );
    }
}
