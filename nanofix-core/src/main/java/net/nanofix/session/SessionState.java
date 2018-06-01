package net.nanofix.session;

// LoggedOut( false, false, false, false)
// Connecting(true, f, f, f)
// ConnectedWaitingForLogon(true, f, f, f)
// ReceivedLogon(f, f, f, true)
// SentLogonWaitingResponse(true, f, f, true)
// LoggedOn(false, true, true, true)
// SentLogoutWaitingResponse(false, true, false, true)
// ReceivedLogout(false, false, true, true)
// Disconnecting(false, false, false, false)

// canSendApplicationMessages
// canSendSessionMessages
// canReceiveLogonMessage
// canReceiveMessages

public enum SessionState {
    LoggedOut,
    Connecting,
    LoggingOn,
    LoggedOn,
    Disconnected
}
