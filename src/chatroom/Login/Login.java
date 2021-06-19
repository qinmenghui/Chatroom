package chatroom.Login;

import chatroom.ChatClient;
import chatroom.Register;
import chatroom.Register.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {
    //北部
    JLabel jbl1;

    //中部　
    JPanel jtp, jp2;
    JLabel jp2_jbl1, jp2_jbl2;
    JButton jp2_jb1, jp2_jbl3;
    JTextField jp2_jtf;
    JPasswordField jp2_jpf;
    JCheckBox jp2_jcb1, jp2_jcb2;

    public static void main(String[] args) {
        Login login = new Login();
    }

    //GUI实现
    public Login() {
        jbl1 = new JLabel(new ImageIcon("src/chatroom/Image/xb1.png"));//背景图片
        jp2 = new JPanel(new GridLayout(3, 3));
        jp2 = new JPanel(new GridLayout(3, 3));//设置好一个3X3的布局
        jp2_jbl1 = new JLabel("JQ账号", JLabel.CENTER);//第一个参数为显示的文字，第二个为居中
        jp2_jbl2 = new JLabel("JQ密码", JLabel.CENTER);
        jp2_jbl3 = new JButton("忘记密码");
        jp2_jbl3.setForeground(Color.blue);//忘记密码为蓝色
        jp2_jbl3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"请联系管理员查询数据库","提示",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jp2_jb1 = new JButton("注册");
        jp2_jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
            }
        });
        jp2_jtf = new JTextField();
        jp2_jpf = new JPasswordField();
        jp2_jcb1 = new JCheckBox("记住密码");
        jp2_jcb2 = new JCheckBox("隐身登录");
// 把控件按照顺序加入到jp2中
        jp2.add(jp2_jbl1);
        jp2.add(jp2_jtf);
        jp2.add(jp2_jb1);
        jp2.add(jp2_jbl2);
        jp2.add(jp2_jpf);
        jp2.add(jp2_jbl3);
        jp2.add(jp2_jcb1);
        jp2.add(jp2_jcb2);


        jtp = new JPanel();
        jtp.add(jp2);
//南部组件为三个按钮，全部定义好。并且在构造函数里实现好

// 定义南部组件

        JPanel jp1;
        JButton jp1_jb1, jp1_jb2, jp1_jb3;

// 南部
        jp1 = new JPanel();
        jp1_jb1 = new JButton("登录");
        jp1_jb1.addActionListener(this);
        jp1_jb2 = new JButton("退出");
        jp1_jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jp1_jb3 = new JButton("帮助");
        jp1.add(jp1_jb1);
        jp1.add(jp1_jb2);
        jp1.add(jp1_jb3);
        jp1_jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "1.首先你要设置服务器IP和端口，然后输入一个属于自己的的用户名，如果此用户名已被使用，你将需要重新输入一个用户名进行登录，\n"
                                + "2.登录你将看到一个在线客户列表（如果你不是第一个登录的用户）\n"
                                + "3.在当前的在线客户列表中选择你要发送消息的对象，可以一个或多个\n"
                                + "4.选择对象后，你将在会话标签栏中看到你当前进行会话的对象。\n"
                                + "5.登录成功后你可以选择退出按钮，使当前用户退出，重新登录\n"
                                + "6.关闭此窗口表示离开", "帮助",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jp1.add(jp1_jb1);
        jp1.add(jp1_jb2);
        jp1.add(jp1_jb3);

//然后把北、中、南三个部分的组件全部插入到JFrame中，并且给JFrame设置好大小，关闭的方式，最后设置JFrame可见。

// 把JP1放到南部

        this.setTitle("JQ登陆");
        this.add(jbl1, "North");
        this.add(jtp, "Center");
        this.add(jp1, "South");
        this.setSize(450, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    //登陆实现
    public void actionPerformed(ActionEvent e) {
        Tool_Login tool = new Tool_Login();
        String account = null;
        String password = null;
        account = jp2_jtf.getText();
        password = String.valueOf(jp2_jpf.getPassword());
        try {
            if (tool.getConnection(account, password)) {
                JOptionPane.showMessageDialog(null,"登陆成功！","提示",JOptionPane.INFORMATION_MESSAGE);
                this.dispose();//关闭登陆界面
                new ChatClient();//打开Client
            } else {JOptionPane.showMessageDialog(null,"账号或密码错误！","提示",JOptionPane.INFORMATION_MESSAGE);
            jp2_jtf.setText("");
            jp2_jpf.setText("");
            jp2_jtf.requestFocusInWindow();

            };
        } catch (Exception a) {
            a.printStackTrace();
        }
    }

    }
