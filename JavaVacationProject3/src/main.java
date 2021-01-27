import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;

public class main {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/ryan";
    private static final String USER = "root";
    private static final String PASSWORD = "265958";
    private static Connection conn = null;


    static Scanner input = new Scanner(System.in);
    private static Integer id;//用户id
    private static String username;//用户登录注册的姓名
    private static String password;//用户密码
    //获取时间

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

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = conn.createStatement();

        System.out.println("——————————欢迎使用——————————");
        System.out.printf("请输入功能选项：\n1.注册用户\n2.登录用户\n3.退出");
        menu1();


    }

    public static void menu1() throws SQLException {
        switch (input.nextInt()){
            case 1:
                zhuce();
                System.out.println("请输入功能选项：\n1.登录用户\n2.退出");
                menu2();
            case 2:
                denglu();
                System.out.println("请输入功能选项：\n1.开始游戏\n2.查询用户信息\n3.查询用户对战记录\n4.退出");
                menu3();
            case 3:System.exit(0);
            default:System.out.println("输入错误！请重新输入");menu1();
        }
    }

    public static void menu2() throws SQLException {
        switch (input.nextInt()){
            case 1:
                denglu();
                System.out.println("请输入功能选项：\n1.开始游戏\n2.查询用户信息\n3.查询用户对战记录\n4.退出");
                menu3();
            case 2:System.exit(0);
            default:System.out.println("输入错误！请重新输入");menu2();
        }
    }

    public static void menu3() throws SQLException {
        switch (input.nextInt()){
            case 1:
                game();
                System.out.println("请输入功能选项：\n1.开始游戏\n2.查询用户信息\n3.查询用户对战记录\n4.退出");
                menu3();
            case 2:
                chaxun();
                System.out.println("请输入功能选项：\n1.开始游戏\n2.查询用户信息\n3.查询用户对战记录\n4.退出");
                menu3();
            case 3:
                chaxun2();
                System.out.println("请输入功能选项：\n1.开始游戏\n2.查询用户信息\n3.查询用户对战记录\n4.退出");
                menu3();
            case 4:System.exit(0);
            default:System.out.println("输入错误！请重新输入");menu3();
        }
    }



    //用户注册,添加到user表中
    public static void zhuce() throws SQLException {
        System.out.println("请输入所注册的用户ID:");
        id = input.nextInt();

        System.out.println("请输入所注册的登录名称：");
        username = input.next();
        System.out.println("请输入所注册的登录密码：");
        password = input.next();
        String sql = "insert into user (ID,player_username,player_password,play_createtime) values(?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setInt(1, id);
        ptmt.setString(2, username);
        ptmt.setString(3, password);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date createtime = new Date();
        ptmt.setString(4, df.format(createtime));
        ptmt.execute();
        System.out.println("注册成功！");
    }

    //用户登录
    public static void denglu() throws SQLException {
        System.out.println("请输入你的登录名称：");
        username = input.next();
        System.out.println("请输入你的登录密码：");
        password = input.next();

        String sql = "select player_username,player_password from user where player_username=? and player_password=?";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, username);
        ptmt.setString(2, password);
        ResultSet rs = ptmt.executeQuery();
        //从登录用户给出的账号密码来检测查询在数据库表中是否存在相同的账号密码
        if (rs.next()) {
            System.out.println("登录成功！");
        } else {
            System.out.println("姓名或密码错误！\n请重新登录：");
            denglu();
        }
    }

    public static void chaxun() throws SQLException {
        System.out.println("请输入所查询的用户名：");
        String username=input.next();
        String sql="select * from user where player_username=?";
        PreparedStatement ptmt=conn.prepareStatement(sql);
        ptmt.setString(1,username);
        ResultSet rs=ptmt.executeQuery();
        if (rs.next()){
            System.out.println("ID:"+rs.getInt("ID"));
            System.out.println("用户名："+rs.getString("player_username"));
            System.out.println("创建时间："+rs.getString("play_createtime"));
            System.out.println("查询成功！");
        }else{
            System.out.println("查询失败！没有该用户名！");
        }
    }

    public static void chaxun2() throws SQLException {
        System.out.println("请输入所查询的用户id：");
        Integer id=input.nextInt();
        String sql="select * from record where game_ID=?";
        PreparedStatement ptmt=conn.prepareStatement(sql);
        ptmt.setInt(1,id);
        ResultSet rs=ptmt.executeQuery();
        if (rs.next()){
            System.out.println("game_ID:" + rs.getInt("game_ID"));
            System.out.println("game_begintime：" + rs.getString("game_begintime"));
            System.out.println("game_exittime：" + rs.getString("game_exittime"));
            System.out.println("game_user1ID：" + rs.getString("game_user1ID"));
            System.out.println("game_user2ID：" + rs.getString("game_user2ID"));
            System.out.println("game_winner_ID：" + rs.getString("game_winner_ID"));
            System.out.println("game_user1cards：" + rs.getString("game_user1cards"));
            System.out.println("game_user2cards：" + rs.getString("game_user2cards"));
            System.out.println("---------------------------");
            System.out.println("查询成功！");
        }else{
            System.out.println("查询失败！没有该用用户id！");
        }
    }

    public static void game() throws SQLException {
        GameDesign s=new GameDesign();

        Date game_begintime=new Date();
        System.out.println("----------------------开始创建卡牌----------------------");
        s.setCard();
        System.out.println("------------------------开始洗牌----------------------");
        s.disorder();
        System.out.println("----------------------开始创建玩家---------------------");
        s.setplayer();
        System.out.println("------------------------开始发牌----------------------");
        s.sendcard();
        s.compare();
        Date game_exittime=new Date();

        s.db(game_begintime,game_exittime);
    }

    //失败品：查询胜率
    /*public static void chaxunWin() throws SQLException {
        System.out.println("请输入所查询的用户名id：");
        Integer id=input.nextInt();
        String sql="select * from record where game_ID=?";
        PreparedStatement ptmt=conn.prepareStatement(sql);
        ptmt.setInt(1,id);
        ResultSet rs=ptmt.executeQuery();
        if (rs.next()){
            int i=0,sum=0;
            if(rs.getString("game_winner_ID")!="wang"){
                i++;
                sum++;
            }else{
                sum++;
            }

            int win=i/sum;
            System.out.println("胜率为："+win);

        }else{
            System.out.println("查询失败！没有该用户名！");
        }
    }*/
}


