public class Card {
    String type;
    String point;
    Card(String type,String point){
        this.type =type;
        this.point=point;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Card))
            return false;
        Card other = (Card) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (point == null) {
            if (other.point != null)
                return false;
        } else if (!point.equals(other.point))
            return false;
        return true;
    }
}
