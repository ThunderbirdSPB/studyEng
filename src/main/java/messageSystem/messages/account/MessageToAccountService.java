package messageSystem.messages.account;

import messageSystem.Address;
import messageSystem.Message;

public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }
}
