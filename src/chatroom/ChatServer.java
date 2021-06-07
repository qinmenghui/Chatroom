package chatroom;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer extends JFrame {
	
	private JTextArea displayJTextArea;
	
	private ServerSocket serverSocket;
	
	private Socket socket;
	
	public JMenuBar jmb;
	public JMenu setJMenu;
	public JMenu setPortJMenuItem;
	
	static int port;        //Ҫ�����Ķ˿�
	
	private Map<String,Server_Thread> threadList = new HashMap<String,Server_Thread>();        //�߳��б�
	
	public ChatServer() {
		this.setTitle("������");
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		displayJTextArea = new JTextArea();
		displayJTextArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(displayJTextArea);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(jsp, BorderLayout.CENTER);
//		jmb = new JMenuBar();
//		JMenu setJMenu = new JMenu("����");
//		JMenuItem setPortJMenuItem = new JMenuItem("�˿�����");
//		setPortJMenuItem.addActionListener(new Monitor());
//		setJMenu.add(setPortJMenuItem);
//		jmb.add(setJMenu);
//		this.setJMenuBar(jmb);
		this.setBounds(400, 150, 500, 350);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void connect() {
		try {
			serverSocket = new ServerSocket(port);          //��¼�������˵�һЩ��Ϣ
			displayJTextArea.append("������:" + InetAddress.getLocalHost().getHostName() + "\n");
			displayJTextArea.append("������ַ:" + InetAddress.getLocalHost().getHostAddress() + "\n");
			displayJTextArea.append("���������˿�:" + port + "\n");
		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "�˶˿��ѱ�ռ�ã��޷�������", "��ʾ", JOptionPane.WARNING_MESSAGE);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
		}
		
		while (true) {
			try {
				socket = serverSocket.accept();               //�ȴ��ͻ��˵�����
				Date now = new Date();
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd E kk:mm:ss");
				displayJTextArea.append("\n" + f.format(now) + "\n");
				displayJTextArea.append(socket.getInetAddress().getHostAddress()
						+ " is connected" + "\n");
				Server_Thread st = new Server_Thread(socket, threadList, displayJTextArea);       //�ͻ������ϣ�������Ӧ���߳�
				st.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args) {
		ChatServer cs = new ChatServer();
		String str = JOptionPane.showInputDialog(null, "��������Ҫ�����Ķ˿ڣ��˿�ӦΪ��1024-65535��������", "�˿�����", JOptionPane.PLAIN_MESSAGE);
		try {
			port = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "�˿����ò��ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		}
		cs.connect();
	}
	
//	class Monitor implements ActionListener {
//		
//		public void actionPerformed(ActionEvent event) {
//			String str = JOptionPane.showInputDialog(null, "��������Ҫ�����Ķ˿ڣ��˿�ӦΪ��1024-65535��������", "�˿�����", JOptionPane.PLAIN_MESSAGE);
//			try {
//				port = Integer.parseInt(str);
//			} catch (NumberFormatException e) {
//				JOptionPane.showMessageDialog(null, "�˿����ò��ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
//			}
//			connect();
//		}
//	}
	
}
