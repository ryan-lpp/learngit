public class Player {

    int id;
    String name;
    public Player(int id,String name) {
        this.id=id;
        this.name=name;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
