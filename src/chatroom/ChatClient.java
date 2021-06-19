package chatroom;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatClient extends JFrame {

	private JLabel clientIDLabel, sessionObjectLabel, currentClientLabel;
	public JTextArea displayTextArea;
	public List currentClientList;
	public JTextField clientNameTextField;
	private JTextPane messageJTextPane;
	public JButton loginButton, logoutJButton;
	public JButton sendJButton;
	public JButton sendFileicon;
	public JButton sendicon;
	JMenuItem setIPJMenuItem, setPortJMenuItem, aboutJMenuItem;
	JFileChooser jdir;

	private Socket socket;

	private BufferedReader reader;
	private PrintWriter writer;

	private StringBuffer toClientName = null;

	public static String ip = "127.0.0.1";
	public static int port = 6666;

	public ChatClient() {
		this.setTitle("局域网聊天程序的设计与实现");
		Container container = this.getContentPane();
		GridBagLayout layout = new GridBagLayout();//网格包布局
		GridBagConstraints constraints = new GridBagConstraints();//创建GridBagConstraints实例
		constraints.fill = GridBagConstraints.BOTH;          //组件随着所给区域可以进行扩展
		container.setLayout(layout);
		
		createJMenuBar();

		//客户名，登陆，退出
		constraints.weightx = 8.0;//行的权重
		constraints.weighty = 1.0;//列的权重
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;//指定此组件是其列或行中最后一个
		JPanel topPanel = new JPanel();
		clientIDLabel = new JLabel("客户名:", SwingConstants.CENTER);
		topPanel.add(clientIDLabel);
		clientNameTextField = new JTextField(20);
		topPanel.add(clientNameTextField);
		loginButton = new JButton("登陆");
		topPanel.add(loginButton);
		logoutJButton = new JButton("退出");
		logoutJButton.setEnabled(false);
		topPanel.add(logoutJButton);
		layout.setConstraints(topPanel, constraints);
		container.add(topPanel);

		//会话对象
		constraints.weightx = 8.0;
		constraints.weighty = 1.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sessionObjectLabel = new JLabel("会话对象:");
		layout.setConstraints(sessionObjectLabel, constraints);
		container.add(sessionObjectLabel);

		//文本域
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
		currentClientJPanel.setBackground(Color.GREEN);
		currentClientLabel = new JLabel("当前在线好友:");
		currentClientJPanel.add(currentClientLabel, BorderLayout.NORTH);
		currentClientList = new List();
		currentClientJPanel.add(currentClientList, BorderLayout.CENTER);
		layout.setConstraints(currentClientJPanel, constraints);
		container.add(currentClientJPanel);

		currentClientList.setMultipleMode(true);        //设置列表是多选模式

		constraints.weightx = 1.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sendicon = new JButton("表情");
		/*sendicon.setBackground(Color.pink);
		sendicon.setOpaque(true);
		sendicon.setBorderPainted(false);
		*/
		container.add(sendicon);

		constraints.weightx = 1.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sendFileicon = new JButton("发送文件");
		/*sendFileicon.setBackground(Color.cyan);
		sendFileicon.setOpaque(true);
		sendFileicon.setBorderPainted(false);
		//颜色
		*/
		container.add(sendFileicon);


		constraints.weightx = 7.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.RELATIVE;
		messageJTextPane = new JTextPane();

		//JScrollPane messageScrollPane = new JScrollPane(messageTextField);
		layout.setConstraints(messageJTextPane, constraints);
		container.add(messageJTextPane);




		constraints.weightx = 1.0;
		constraints.weighty = 3.0;
		// constraints.gridheight = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		sendJButton = new JButton("发送");
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
		
		setMonitorForSetIPJMenuItem();             //为设置ip项设置时间监听
		setMonitorForSetPortJMenuItem();		//为设置端口项设置事件监听
		setMonitorForAboutJMenuItem();			//为帮助项设置事件监听
		setMonitorForLogoutJButton();			//为登出项设置事件监听
		setMonitorForCurrentClientList();		//为当前用户列表设置事件监听
		setMonitorForSentJButton();       //为发送按钮就行事件监听
		loginButton.addActionListener(new Monitor()); //为登陆按钮设置监听
		
		messageJTextPane.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				//当键盘输入回车键当时候发送消息
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		sendFileicon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					String name =Transfer();
					String i = ioToBase64(name);
					base64ToIo(i,name);
					//System.out.println(i);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}

			}
		});

	}
	
	public void createJMenuBar() {
		JMenuBar jmb = new JMenuBar();
		JMenu setJMenu = new JMenu("设置");
		setIPJMenuItem = new JMenuItem("设置IP");
		
		setPortJMenuItem = new JMenuItem("设置端口");
		setJMenu.add(setIPJMenuItem);
		setJMenu.add(setPortJMenuItem);
		jmb.add(setJMenu);
		JMenu helpJMenu = new JMenu("帮助");
		helpJMenu.setMnemonic('H');
		aboutJMenuItem = new JMenuItem("帮助");
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
						"请输入你要连接的服务器IP(格式：XXX.XXX.XXX.XXX)", "设置IP",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	public void setMonitorForSetPortJMenuItem() {
		setPortJMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				String str = JOptionPane.showInputDialog(null,
						"请输入你要连接服务器 的端口，端口应为（1024-65535）的整数", "端口设置",
						JOptionPane.PLAIN_MESSAGE);
				try {
					port = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "端口设置不成功", "提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	public void setMonitorForAboutJMenuItem() {
		aboutJMenuItem.addActionListener(new ActionListener() {

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
	}
	
	public void setMonitorForLogoutJButton() {
		logoutJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				writer.println("logout:");
				currentClientList.removeAll(); // 当用户离开时，清空当前在线客户列表
				clientNameTextField.setText("");
				clientNameTextField.setEnabled(true);
				loginButton.setEnabled(true);
				logoutJButton.setEnabled(false);
				toClientName = null;
				sessionObjectLabel.setText("会话对象:");
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
				sessionObjectLabel.setText("会话对象:" + toClientName);
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
	//实现回车键发送消息
	public void sendMessage() {
		if (toClientName != null && !toClientName.equals("")) {
			String msg = messageJTextPane.getText();
			if (!msg.equals("")) {
				writer.println(toClientName + "#" + msg);
				messageJTextPane.setText("");
			} else {
				JOptionPane.showMessageDialog(null,
						"你没有输入如何消息，请输入内容后再发送！", "提示",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"你没有选择要会话的对象，请先选择当前在线的客户进行会话，", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public String Transfer(){//文件选择
		String p = null;
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			jdir = new JFileChooser();

//设置选择路径模式
			jdir.setFileSelectionMode(JFileChooser.FILES_ONLY);

				//FileNameExtensionFilter filter = new FileNameExtensionFilter("指定文件类型",p);
				//jdir.setFileFilter(filter);

//过滤文件类型


//设置对话框标题
			jdir.setDialogTitle("请选择文件路径");

			if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {//用户点击了确定
				p = jdir.getSelectedFile().getAbsolutePath();//取得路径选择
				System.out.println(p);
				
			}
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace();
		} catch (InstantiationException instantiationException) {
			instantiationException.printStackTrace();
		} catch (IllegalAccessException illegalAccessException) {
			illegalAccessException.printStackTrace();
		} catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
			unsupportedLookAndFeelException.printStackTrace();
		}
		return p;
	}


	public String ioToBase64(String f) throws IOException {//编码64
		String fileName = f; //源文件
		String strBase64 = null;
		try {
			InputStream in = new FileInputStream(fileName);
			// in.available()返回文件的字节长度
			byte[] bytes = new byte[in.available()];
			// 将文件中的内容读入到数组中
			in.read(bytes);
			strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
			in.close();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println(strBase64);
		return strBase64;
	}


	public void base64ToIo(String strBase64,String name) throws IOException {//解码
		String string = strBase64;
		String Name = name;
		File tempFile = new File(Name.trim());
		String fileName2 = "/Users/qinmenghui/Chatroom/src/File/";
		String fileName3 = tempFile.getName();
		String fileName=fileName2+fileName3;//生成的新文件
		try {
			// 解码，然后将字节转换为文件
			byte[] bytes = new BASE64Decoder().decodeBuffer(string);   //将字符串转换为byte数组
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			byte[] buffer = new byte[1024];
			FileOutputStream out = new FileOutputStream(fileName);
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread); //文件写操作
			}
			System.out.println("文件存放完成");
			Client_Thread.addDate();
			Client_Thread.cc.displayTextArea.append("文件传输成功！");
//            AsrMain o=new AsrMain(fileName);
//            System.out.println(o.getms());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}



	private class Monitor implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (ip != null && port != -1) {
				try {
					String loginName = clientNameTextField.getText();
					if (loginName.trim().equals("")) {
						JOptionPane.showMessageDialog(null,
								"用户名不能为空，请先输入客户名在登录！", "提示",
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
					writer.println("login:" + loginName);    			//登陆消息前加标标志，表示这是个登陆消息
					sendJButton.setEnabled(true);
				} catch (ConnectException e1) {
					JOptionPane.showMessageDialog(null, "服务器没有开启，请先开启服务器！",
							"警告", JOptionPane.ERROR_MESSAGE);
				} catch (UnknownHostException e1) {
					displayTextArea.append(e1.getMessage());
					displayTextArea.setCaretPosition(displayTextArea.getText().length());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(null, "请先设置有效的IP和端口");
			}
		}
	}


	public static void main(String[] args) {
		new ChatClient();
	}

}
