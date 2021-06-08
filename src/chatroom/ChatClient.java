package chatroom;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class ChatClient extends JFrame {

	private JLabel clientIDLabel, sessionObjectLabel, currentClientLabel;
	public JTextArea displayTextArea;
	public List currentClientList;
	public JTextField clientNameTextField;
	private JTextField messageJTextField;
	public JButton loginButton, logoutJButton;
	public JButton sendJButton;
	
	JMenuItem setIPJMenuItem, setPortJMenuItem, aboutJMenuItem;

	private Socket socket;

	private BufferedReader reader;
	private PrintWriter writer;

	private StringBuffer toClientName = null;

	public String ip;
	public int port = -1;

	public ChatClient() {
		this.setTitle("�������������������ʵ��");
		Container container = this.getContentPane();
		GridBagLayout layout = new GridBagLayout();//���������
		GridBagConstraints constraints = new GridBagConstraints();//����GridBagConstraintsʵ��
		constraints.fill = GridBagConstraints.BOTH;          //�����������������Խ�����չ
		container.setLayout(layout);
		
		createJMenuBar();

		//�ͻ�������½���˳�
		constraints.weightx = 8.0;//�е�Ȩ��
		constraints.weighty = 1.0;//�е�Ȩ��
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;//ָ������������л��������һ��
		JPanel topPanel = new JPanel();
		clientIDLabel = new JLabel("�ͻ���:", SwingConstants.CENTER);
		topPanel.add(clientIDLabel);
		clientNameTextField = new JTextField(20);
		topPanel.add(clientNameTextField);
		loginButton = new JButton("��½");
		topPanel.add(loginButton);
		logoutJButton = new JButton("�˳�");
		logoutJButton.setEnabled(false);
		topPanel.add(logoutJButton);
		layout.setConstraints(topPanel, constraints);
		container.add(topPanel);

		//�Ự����
		constraints.weightx = 8.0;
		constraints.weighty = 1.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sessionObjectLabel = new JLabel("�Ự����:");
		layout.setConstraints(sessionObjectLabel, constraints);
		container.add(sessionObjectLabel);

		//�ı���
		constraints.weightx = 7.0;
		constraints.weighty = 30.0;
		// constraints.gridheight = 5;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		displayTextArea = new JTextArea();
		displayTextArea.setEditable(false);
		JScrollPane displayScrollPane = new JScrollPane(displayTextArea);
		layout.setConstraints(displayScrollPane, constraints);
		container.add(displayScrollPane);

		constraints.weightx = 1.0;
		constraints.weighty = 30.0;
		// constraints.gridheight = 5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		JPanel currentClientJPanel = new JPanel(new BorderLayout());
		currentClientLabel = new JLabel("��ǰ���ߺ���:");
		currentClientJPanel.add(currentClientLabel, BorderLayout.NORTH);
		currentClientList = new List();
		currentClientJPanel.add(currentClientList, BorderLayout.CENTER);
		layout.setConstraints(currentClientJPanel, constraints);
		container.add(currentClientJPanel);

		currentClientList.setMultipleMode(true);        //�����б��Ƕ�ѡģʽ

		constraints.weightx = 7.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		messageJTextField = new JTextField();

		//JScrollPane messageScrollPane = new JScrollPane(messageTextField);
		layout.setConstraints(messageJTextField, constraints);
		container.add(messageJTextField);


		constraints.weightx = 1.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sendJButton = new JButton("����");
		sendJButton.setEnabled(false);
		layout.setConstraints(sendJButton, constraints);
		container.add(sendJButton);

		this.setBounds(400, 100, 600, 600);
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		});
		
		setMonitorForSetIPJMenuItem();             //Ϊ����ip������ʱ�����
		setMonitorForSetPortJMenuItem();		//Ϊ���ö˿��������¼�����
		setMonitorForAboutJMenuItem();			//Ϊ�����������¼�����
		setMonitorForLogoutJButton();			//Ϊ�ǳ��������¼�����
		setMonitorForCurrentClientList();		//Ϊ��ǰ�û��б������¼�����
		setMonitorForSentJButton();       //Ϊ���Ͱ�ť�����¼�����
		loginButton.addActionListener(new Monitor()); //Ϊ��½��ť���ü���
		
		messageJTextField.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				//����������س�����ʱ������Ϣ
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		
	}
	
	public void createJMenuBar() {
		JMenuBar jmb = new JMenuBar();
		JMenu setJMenu = new JMenu("����");
		setIPJMenuItem = new JMenuItem("����IP");
		
		setPortJMenuItem = new JMenuItem("���ö˿�");
		setJMenu.add(setIPJMenuItem);
		setJMenu.add(setPortJMenuItem);
		jmb.add(setJMenu);
		JMenu helpJMenu = new JMenu("����");
		helpJMenu.setMnemonic('H');
		aboutJMenuItem = new JMenuItem("����");
		aboutJMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK));
		helpJMenu.add(aboutJMenuItem);
		jmb.add(helpJMenu);
		this.setJMenuBar(jmb);
	}
	
	public void setMonitorForSetIPJMenuItem() {
		setIPJMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ip = JOptionPane.showInputDialog(null,
						"��������Ҫ���ӵķ�����IP(��ʽ��XXX.XXX.XXX.XXX)", "����IP",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	public void setMonitorForSetPortJMenuItem() {
		setPortJMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				String str = JOptionPane.showInputDialog(null,
						"��������Ҫ���ӷ����� �Ķ˿ڣ��˿�ӦΪ��1024-65535��������", "�˿�����",
						JOptionPane.PLAIN_MESSAGE);
				try {
					port = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "�˿����ò��ɹ�", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	public void setMonitorForAboutJMenuItem() {
		aboutJMenuItem.addActionListener(new ActionListener() {

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
	}
	
	public void setMonitorForLogoutJButton() {
		logoutJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				writer.println("logout:");
				currentClientList.removeAll(); // ���û��뿪ʱ����յ�ǰ���߿ͻ��б�
				clientNameTextField.setText("");
				clientNameTextField.setEnabled(true);
				loginButton.setEnabled(true);
				logoutJButton.setEnabled(false);
				toClientName = null;
				sessionObjectLabel.setText("�Ự����:");
			}
		});
	}
	
	public void setMonitorForCurrentClientList() {
		currentClientList.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				String[] Names = (String[]) currentClientList
						.getSelectedObjects();
				if (Names.length == 0) {
					toClientName = null;
					return;
				}
				toClientName = new StringBuffer();
				for (int i = 0; i < Names.length; i++) {
					toClientName.append("#" + Names[i]);
				}
				sessionObjectLabel.setText("�Ự����:" + toClientName);
			}
		});
	}
	
	public void setMonitorForSentJButton() {
		sendJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
	}
	//ʵ�ֻس���������Ϣ
	public void sendMessage() {
		if (toClientName != null && !toClientName.equals("")) {
			String msg = messageJTextField.getText();
			if (!msg.equals("")) {
				writer.println(toClientName + "#" + msg);
				messageJTextField.setText("");
			} else {
				JOptionPane.showMessageDialog(null,
						"��û�����������Ϣ�����������ݺ��ٷ��ͣ�", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"��û��ѡ��Ҫ�Ự�Ķ�������ѡ��ǰ���ߵĿͻ����лỰ��", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private class Monitor implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (ip != null && port != -1) {
				try {
					String loginName = clientNameTextField.getText();
					if (loginName.trim().equals("")) {
						JOptionPane.showMessageDialog(null,
								"�û�������Ϊ�գ���������ͻ����ڵ�¼��", "��ʾ",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					socket = new Socket(ip, port);
					Client_Thread ct = new Client_Thread(socket,
							ChatClient.this);
					ct.start();
					reader = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					writer = new PrintWriter(socket.getOutputStream(), true);
					writer.println("login:" + loginName);    			//��½��Ϣǰ�ӱ��־����ʾ���Ǹ���½��Ϣ
					sendJButton.setEnabled(true);
				} catch (ConnectException e1) {
					JOptionPane.showMessageDialog(null, "������û�п��������ȿ�����������",
							"����", JOptionPane.ERROR_MESSAGE);
				} catch (UnknownHostException e1) {
					displayTextArea.append(e1.getMessage());
					displayTextArea.setCaretPosition(displayTextArea.getText()
							.length());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "����������Ч��IP�Ͷ˿�");
			}
		}
	}

	public static void main(String[] args) {
		new ChatClient();
	}

}
