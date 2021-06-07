package chatroom;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Server_Thread extends Thread {
	
	private String loginName;

	private Socket socket;
	
	private Map<String,Server_Thread> threadList;
	
	private JTextArea displayTextArea;
	
	BufferedReader reader;
	
	PrintWriter writer;
	
	public Server_Thread(Socket socket, Map threadList, JTextArea displayTextArea) {
		this.socket = socket;
		this.threadList = threadList;
		this.displayTextArea = displayTextArea;
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			String msg = null;
			try {
				msg = reader.readLine();
				//���������������ж���Ϣ���������ǵ�½���ǳ�����������������
				if (msg.startsWith("login")) {
					loginName = msg.substring(msg.indexOf(':') + 1).trim();
					if (! threadList.containsKey(loginName)) {
						writer.println(loginName + ": ��¼�ɹ�!");
						threadList.put(loginName, this);
						displayTextArea.append(loginName + " ���������� \n");
						displayTextArea.setCaretPosition(displayTextArea.getText().length());        //���ù�������ʾ�ڵͶ�
						
						Iterator<Entry<String, Server_Thread>> it = threadList.entrySet().iterator();       // ֪ͨ���������û� ���û�������
						while (it.hasNext()) {
							Map.Entry entry = (Map.Entry) it.next();
							Server_Thread st = (Server_Thread) entry.getValue();
							if (st != this) {
								st.writer.println("�û�����:" + loginName);               // �������û�֪ͨ�����߳�
								writer.println("�û�����:" + st.loginName);            // �����߳�֪ͨ�������û�
							}
						}
					} else {
//						JOptionPane.showMessageDialog(null, loginName + " �Ѵ��ڣ���ѡ���������û�����¼", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
						writer.println(loginName + " �Ѵ��ڣ���ѡ���������û�����¼");
					}
				} else if(msg.startsWith("logout")){
					logout();
					break;
				} else {
					String [] toClientNames;
					toClientNames = msg.substring(msg.indexOf('#') + 1).split("#");
					String message = toClientNames[toClientNames.length - 1];       //���һ���ָ�����Ҫ���͸��ͻ�����Ϣ
					for (int i = 0; i < toClientNames.length - 1; i++) {
						Server_Thread st = threadList.get(toClientNames[i].trim());
//						if(st != this) {
							st.writer.println(loginName + ": " + message);
//						}
					}
					writer.println(loginName + ": " + message);    //���Լ�Ҳ������Ϣ
				}
			} catch (IOException e) {
				logout();
				break;
			}
		}
	}
	
	public void logout() {
		Iterator it = threadList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Server_Thread st = (Server_Thread) entry.getValue();
			if (st != this && st.isAlive()) {
				st.writer.println("�û�����:" + loginName);
			}
		}
		threadList.remove(loginName);
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
		Date now = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd E kk:mm:ss");
		displayTextArea.append("\n" + f.format(now) + "\n");
		displayTextArea.append("�û�����:" + loginName + "\n");
		displayTextArea.setCaretPosition(displayTextArea.getText().length());
	}
}
