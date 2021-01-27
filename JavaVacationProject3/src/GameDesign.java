import java.net.IDN;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class GameDesign {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/ryan";
    private static final String USER = "root";
    private static final String PASSWORD = "265958";
    private static Connection conn = null;


    private static Integer game_ID;
    private static String game_user1ID;
    private static String game_user2ID;
    private static String game_user1cards;
    private static String game_user2cards;
    private static String game_winner_ID;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Card> cards=new ArrayList<Card>();	//卡牌集合
    List<Player> players=new ArrayList<Player>();//玩家集合
    List<Card> playerCard=new ArrayList<Card>();//玩家手牌集合
    Scanner in=new Scanner(System.in);
    Rule rule=new Rule();       //实例化Rule类


    public void setCard() {
        System.out.println("\t--------------正在创建卡牌...------------");
        String[] type = {"♦","♣","♥","♠"};
        String[] points= {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        for(int i=0;i<points.length;i++) {
            for(int j=0;j<type.length;j++) {
                cards.add(new Card(type[j], points[i]));
            }
        }
        System.out.println("----------------------创建卡牌成功----------------------");

    }

    /*
     *洗牌
     */
    public void disorder() {
        System.out.println("\t----------------正在洗牌...------------");
        Collections.shuffle(cards);
        System.out.println("------------------------洗牌成功----------------------");
    }

    public void setplayer() throws SQLException {
        System.out.println("--------------正在创建玩家...------------");
        int playerid;			//玩家的ID（唯一的）
        String playername;		//玩家名称
        Player pl;				//定义玩家(player)类型的pl用于实例化添加玩家到集合中


            do {
                try {
                    System.out.println("请输入玩家的ID：");
                    playerid=in.nextInt();
                    pl=new Player(playerid, null);
                    //判断id是否存在，不存在则跳出循环
                    if(players.contains(pl)) {
                        System.out.println("该ID已经存在！请重新输入！");
                    }else {
                        break;
                    }
                }catch(InputMismatchException e) {
                    System.out.println("输入格式错误！请输入0-9的整数！");
                    in.next();
                }

            } while (true);

            //将id与登录id进行对比
        String sql="select * from user where ID=?";
        PreparedStatement ptmt=conn.prepareStatement(sql);
        ptmt.setInt(1, playerid);
        ResultSet rs=ptmt.executeQuery();
        if (rs.next()){
            System.out.println("ID已找到！");
        }else{
            System.out.println("ID未找到！请注册该ID！");
            System.exit(0);
        }

            System.out.println("请输入玩家"+playerid+"名称：");
            playername=in.next();
            pl=new Player(playerid, playername);
            players.add(pl);


        System.out.println("----------------------创建玩家成功----------------------");

            System.out.println(pl.id+":"+pl.name);
            game_ID=pl.id;
            game_user1ID=pl.name;
            game_user2ID="wang";

    }
    public void sendcard() {
        System.out.println("----------------正在发牌...------------");
        for(int i=0;i<4;i++) {
            playerCard.add(cards.get(i));		//添加洗牌（打乱顺序）后的前四张牌到玩家手牌集合中（playerCard）
            if(i%2==0) {						//偶数为玩家1拿牌，基数为玩家2拿牌
                System.out.println("----玩家"+players.get(0).name+"拿牌----");
            }else {
                System.out.println("----玩家"+"wang"+"拿牌----");
            }
        }
    }
    /*
     * 比牌
     */
    public void compare() {
        rule.setRuleCards();
        int p1max=rule.myCompare(playerCard.get(0), playerCard.get(2));	//获取玩家1手中最大的牌 ，返回值是0或2
        int p2max=rule.myCompare(playerCard.get(1), playerCard.get(3))+1;//获取玩家2手中最大的牌，返回值是（0+1）1或（2+1）3
        int max=rule.myCompare(playerCard.get(p1max), playerCard.get(p2max));//将两个玩家手中最大的牌进行比较
        //返回0或2；0是玩家1获胜并输出玩家1手中最大的牌；2是玩家2获胜并输出玩家2手中最大的牌；
        if(max==0) {
            System.out.println("最大的牌为："+playerCard.get(p1max).type +playerCard.get(p1max).point);
            System.out.println("====="+players.get(0).name+"获胜=====");
            game_winner_ID=players.get(0).name;
        }else {
            System.out.println("最大的牌为："+playerCard.get(p2max).type +playerCard.get(p2max).point);
            System.out.println("====="+"wang"+"获胜=====");
            game_winner_ID="wang";
        }
        System.out.println(players.get(0).name+"的牌为："+playerCard.get(0).type +playerCard.get(0).point+","
                +playerCard.get(2).type +playerCard.get(2).point);
        game_user1cards=playerCard.get(0).type +playerCard.get(0).point+","
                +playerCard.get(2).type +playerCard.get(2).point;
        System.out.println("wang"+"的牌为："+playerCard.get(1).type +playerCard.get(1).point+","
                +playerCard.get(3).type +playerCard.get(3).point);
        game_user2cards=playerCard.get(1).type +playerCard.get(1).point+","
                +playerCard.get(3).type +playerCard.get(3).point;
    }



    public void db(Date game_begintime,Date game_exittime) throws SQLException {
        String sql = "insert into record (game_ID,game_user1ID,game_user2ID,game_winner_ID,game_user1cards,game_user2cards,game_begintime,game_exittime) values (?,?,?,?,?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        ptmt.setInt(1,game_ID);
        ptmt.setString(2,game_user1ID);
        ptmt.setString(3,game_user2ID);
        ptmt.setString(4,game_winner_ID);
        ptmt.setString(5,game_user1cards);
        ptmt.setString(6,game_user2cards);
        ptmt.setString(7, df.format(game_begintime));
        ptmt.setString(8, df.format(game_exittime));

        ptmt.execute();

        System.out.println("游戏记录保存成功！");


    }
}

