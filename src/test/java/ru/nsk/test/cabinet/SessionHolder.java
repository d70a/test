package ru.nsk.test.cabinet;

/**
 *
 * @author me
 */
public class SessionHolder {

    private final static ThreadLocal<SessionWrapper> local = new ThreadLocal<SessionWrapper>();
    

    public SessionWrapper getSession() {
        return local.get();
    }

    public void setSession(SessionWrapper session) {
        local.set(session);
    }
}
