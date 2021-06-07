package chatroom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class Login extends JFrame implements ActionListener {
    //����
    JLabel jbl1;

    //�в���
    JPanel jtp, jp2,jp2_1;
    JLabel jp2_jbl1, jp2_jbl2;
    JButton jp2_jb1, jp2_jbl3;
    JTextField jp2_jtf;
    JPasswordField jp2_jpf;
    JCheckBox jp2_jcb1, jp2_jcb2;

    public static void main(String[] args) {
        Login login = new Login();
    }

    public Login() {
        jbl1 = new JLabel(new ImageIcon("src/chatroom/xb1.png"));//����ͼƬ
        jp2 = new JPanel(new GridLayout(3, 3));
        jp2 = new JPanel(new GridLayout(3, 3));//���ú�һ��3X3�Ĳ���
        jp2_jbl1 = new JLabel("JQ����", JLabel.CENTER);//��һ������Ϊ��ʾ�����֣��ڶ���Ϊ����
        jp2_jbl2 = new JLabel("JQ����", JLabel.CENTER);
        jp2_jbl3 = new JButton("��������");
        jp2_jbl3.setForeground(Color.blue);//��������Ϊ��ɫ
        jp2_jb1 = new JButton("�������");
        jp2_jtf = new JTextField();
        jp2_jpf = new JPasswordField();
        jp2_jcb1 = new JCheckBox("��ס����");
        jp2_jcb2 = new JCheckBox("�����¼");
// �ѿؼ�����˳����뵽jp2��
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
//�ϲ����Ϊ������ť��ȫ������á������ڹ��캯����ʵ�ֺ�

// �����ϲ����

        JPanel jp1;
        JButton jp1_jb1, jp1_jb2, jp1_jb3;

// �ϲ�
        jp1 = new JPanel();
        jp1_jb1 = new JButton("��¼");
        jp1_jb1.addActionListener(this);
        jp1_jb2 = new JButton("�˳�");
        jp1_jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jp1_jb3 = new JButton("����");
        jp1.add(jp1_jb1);
        jp1.add(jp1_jb2);
        jp1.add(jp1_jb3);
        jp1_jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "1.������Ҫ���÷�����IP�Ͷ˿ڣ�Ȼ������һ�������Լ��ĵ��û�����������û����ѱ�ʹ�ã��㽫��Ҫ��������һ���û������е�¼��\n"
                                + "2.��¼�㽫����һ�����߿ͻ��б�����㲻�ǵ�һ����¼���û���\n"
                                + "3.�ڵ�ǰ�����߿ͻ��б���ѡ����Ҫ������Ϣ�Ķ��󣬿���һ������\n"
                                + "4.ѡ�������㽫�ڻỰ��ǩ���п����㵱ǰ���лỰ�Ķ���\n"
                                + "5.��¼�ɹ��������ѡ���˳���ť��ʹ��ǰ�û��˳������µ�¼\n"
                                + "6.�رմ˴��ڱ�ʾ�뿪", "����",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });



//Ȼ��ѱ����С����������ֵ����ȫ�����뵽JFrame�У����Ҹ�JFrame���úô�С���رյķ�ʽ���������JFrame�ɼ���

// ��JP1�ŵ��ϲ�

        this.setTitle("JQ��½");
        this.add(jbl1, "North");
        this.add(jtp, "Center");
        this.add(jp1, "South");
        this.setSize(450, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();//�رյ�½����
        new ChatClient();//��Client
    }
}