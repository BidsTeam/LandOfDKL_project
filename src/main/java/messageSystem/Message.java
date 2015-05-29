package messageSystem;



public abstract class Message {

    private final Address to;

    public Message( Address to) {
        this.to = to;
    }


    public Address getTo() {
        return to;
    }

    public abstract void exec(Abonent abonent);
}
