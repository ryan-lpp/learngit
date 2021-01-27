import java.util.ArrayList;
import java.util.List;

public class Rule{
    List<Card> ruleCards=new ArrayList<Card>();
    public void setRuleCards(){
        String[] colors= {"♦","♣","♥","♠"};
        String[] points= {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        for(int i=0;i<points.length;i++) {
            for(int j=0;j<colors.length;j++) {
                ruleCards.add(new Card(colors[j],points[i]));
            }
        }
    }
    public int myCompare(Card o1, Card o2) {
        if(ruleCards.lastIndexOf(o1)-ruleCards.lastIndexOf(o2)>0) {		//大于0则o1>02
            return 0;				//0和2是玩家1的手牌  （+1）1和3是玩家2的手牌
        }else {
            return 2;
        }
    }

}
