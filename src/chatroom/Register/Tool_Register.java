package chatroom;

import java.awt.Window;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class Tool_Register {
    public void insert(String account1,String password) throws Exception{
        PreparedStatement ps = null;
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","07125216");

            String sql = "insert into table_users (account,password) values(?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, account1);
            ps.setString(2, password);
            int resultSet = ps.executeUpdate();
            if (resultSet > 0) {
                JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "注册失败，请重试", "提示", JOptionPane.INFORMATION_MESSAGE);

            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(con!=null){
                try{
                    con.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }


    }
}
