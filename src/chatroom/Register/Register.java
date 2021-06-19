package chatroom;

import chatroom.Tool_Register;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**ʵ��ע�Ṧ��**/
public class Register extends JFrame {
    public static Dimension screenSize;
    private JPasswordField pwdFld;
    private JPasswordField pwd2Fld;
    private JTextField account;
    private JButton ok;
    private JButton reset;
    private JButton cancel;
    private JLabel jl;
    private JLabel jl1;







    public Register() {
        this.init();
        this.add(jl);
        //this.add(jl1);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }



    public void init() {
        //����
        this.setTitle("ע��JQ���˺�");
        this.setSize(350, 250);
        getContentPane().setLayout(null);
        setResizable(false);
        JLabel label = new JLabel("�˺�*");
        label.setBounds(24, 36, 59, 17);
        getContentPane().add(label);

        account = new JTextField();//�˺�
        account.setBounds(90, 34, 110, 22);
        getContentPane().add(account);

        JLabel label5 = new JLabel("����:*");
        label5.setBounds(24, 72, 50, 17);
        getContentPane().add(label5);

        JLabel label3 = new JLabel("ȷ������:*");
        label3.setBounds(24, 107, 65, 17);
        getContentPane().add(label3);

        pwdFld = new JPasswordField();//�����
        pwdFld.setBounds(90, 70, 110, 22);
        getContentPane().add(pwdFld);

        pwd2Fld = new JPasswordField();
        pwd2Fld.setBounds(90, 105, 110, 22);
        getContentPane().add(pwd2Fld);

        jl = new JLabel(new ImageIcon("src/chatroom/Image/ranran.png"));
        jl.setBounds(210, 30, 130, 130);

        jl1 = new JLabel("��עȻȻ���ٶٽ����");
        jl1.setBounds(24,135,150,22);
        //��ť
        ok = new JButton("ȷ��");
        ok.setBounds(27, 176, 60, 28);
        getContentPane().add(ok);

        reset = new JButton("����");
        reset.setBounds(123, 176, 60, 28);
        getContentPane().add(reset);

        cancel = new JButton("ȡ��");
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
                if (pwdFld.getPassword().length == 0 || pwd2Fld.getPassword().length == 0 || account.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "��*Ϊ�������ݣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                } else if (!new String(pwdFld.getPassword()).equals(new String(pwd2Fld.getPassword()))) {
                    JOptionPane.showMessageDialog(null, "�����������벻һ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    pwdFld.setText("");
                    pwd2Fld.setText("");
                    pwdFld.requestFocusInWindow();//����ص�����
                } else {
                     Tool_Register tool = new Tool_Register();
                    String account1 = account.getText();
                    String password = String.valueOf(pwdFld.getPassword());
                    try{tool.insert(account1,password);
                    }catch (Exception a){
                        a.printStackTrace();
                    }
                    dispose();

                }
            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                account.setText("");
                pwdFld.setText("");
                pwd2Fld.setText("");
                account.requestFocusInWindow();//�˺Ż�ý���
            }
        });

    }




}