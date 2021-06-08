package chatroom;

import java.sql.*;
public class Tool {
    //1.定义方法，并接受参数
    public static boolean getConnection(String account, String password) throws Exception{
        // 2.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //3.获取数据库连接对象
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","07125216");
        //定义sql，（判断输入的账号以及密码是否在表内）
        String sql = "select * from table_users where account = '"+account+"' and password='"+password+"'";
        //获取执行sql的对象
        Statement state = con.createStatement();
        //执行sql
        ResultSet rs = state.executeQuery(sql);
        //判断结果
        return rs.next();
    }
}
