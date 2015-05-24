package app.AccountMap.messages;

import app.AccountMap.AccountMap;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;


public abstract class MessageTemplate extends Message {
    public MessageTemplate(Address to) {
        super(to);
    }

    @Override
    public final void exec(Abonent abonent) {
        if (abonent instanceof AccountMap) {
            exec((AccountMap) abonent);
        }
    }

    protected abstract void exec(AccountMap service);
}
