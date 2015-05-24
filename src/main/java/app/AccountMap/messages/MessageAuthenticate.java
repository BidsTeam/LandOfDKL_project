package app.AccountMap.messages;


import DAO.logic.UserLogic;
import app.AccountMap.AccountMap;
import messageSystem.Address;
import messageSystem.Message;
import util.MessageList;

public final class MessageAuthenticate extends MessageTemplate {
    private int userId;

    public MessageAuthenticate(Address to, int userId) {
        super(to);
        this.userId = userId;
    }

    @Override
    protected void exec(AccountMap service) {
        service.putUser(userId);
//        final Message back = new MessageIsAuthenticated(getTo(), getFrom(), sessionId, account);
//        service.getMessageSystem().sendMessage(back);
    }
}
