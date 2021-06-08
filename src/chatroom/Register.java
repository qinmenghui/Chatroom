package chatroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**实现注册功能**/
public class Register extends JFrame {
    public static Dimension screenSize;
    private JPasswordField pwdFld;
    private JPasswordField pwd2Fld;
    private JTextField account;
    private JButton ok;
    private JButton reset;
    private JButton cancel;
    private JLabel jl;







    public Register() {
        this.init();
        this.add(jl);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }



    public void init() {
        //标题
        this.setTitle("注册JQ新账号");
        this.setSize(350,250);
        getContentPane().setLayout(null);
        setResizable(false);
        JLabel label = new JLabel("账号*");
        label.setBounds(24,36,59,17);
        getContentPane().add(label);

        account = new JTextField();//账号
        account.setBounds(90,34,110,22);
        getContentPane().add(account);

        JLabel label5 = new JLabel("密码:*");
        label5.setBounds(24, 72, 50, 17);
        getContentPane().add(label5);

        JLabel label3 = new JLabel("确认密码:*");
        label3.setBounds(24, 107, 65, 17);
        getContentPane().add(label3);

        pwdFld = new JPasswordField();//密码框
        pwdFld.setBounds(90, 70, 110, 22);
        getContentPane().add(pwdFld);

        pwd2Fld = new JPasswordField();
        pwd2Fld.setBounds(90, 105, 110, 22);
        getContentPane().add(pwd2Fld);

        jl = new JLabel(new ImageIcon("src/chatroom/Image/tx.png"));
        jl.setBounds(220,34,100,100);
        //按钮
        ok = new JButton("确认");
        ok.setBounds(27, 176, 60, 28);
        getContentPane().add(ok);

        reset = new JButton("重填");
        reset.setBounds(123, 176, 60, 28);
        getContentPane().add(reset);

        cancel = new JButton("取消");
        cancel.setBounds(223, 176, 60, 28);
        getContentPane().add(cancel);

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register.this.dispose();
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(pwdFld.getPassword().length==0||pwd2Fld.getPassword().length==0||account.getText().length()==0){
                    JOptionPane.showMessageDialog(null,"带*为必填内容！","提示",JOptionPane.INFORMATION_MESSAGE);
                }else if(!new String(pwdFld.getPassword()).equals(new String(pwd2Fld.getPassword()))){
                    JOptionPane.showMessageDialog(null,"两次密码输入不一致","提示",JOptionPane.INFORMATION_MESSAGE);
                    pwdFld.setText("");
                    pwd2Fld.setText("");
                    pwdFld.requestFocusInWindow();//焦点回到密码
                }else{
                    JOptionPane.showMessageDialog(null,"注册成功","提示",JOptionPane.INFORMATION_MESSAGE);

                }


            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                account.setText("");
                pwdFld.setText("");
                pwd2Fld.setText("");
                account.requestFocusInWindow();//账号获得焦点
            }
        });
    }



}