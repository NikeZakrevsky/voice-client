import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JTextField TextField;
	private static JTextField TextField2;
	private static String str;
	private static GUI GUIThread;
	private static boolean Muted = false;
	private static JTree tree;
	private static JTextPane textPane;
	private static JTextPane textPane_1;
	private static boolean MutedSound = false;

	public MainWindow() throws UnknownHostException, IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {

		// super("Chat");
		setFont(new Font("Dialog", Font.PLAIN, 12));
		// setResizable(false);
		createGUI();
	}

	public int disconnect() {
		new Thread(new PlaySound("disconnected.wav")).start();
		MicrophoneReader.stopSock();
		PlayVoice.stopFlag();
		InputMessage.stopSock();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			MicrophoneReader.sendQuitMessage();
			setTextNickName(new ArrayList<String>());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

	public void createGUI() throws UnknownHostException, IOException {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(null);

		JMenuBar mainMenuBar = new JMenuBar();
		setJMenuBar(mainMenuBar);

		JMenu mnNewMenu = new JMenu("Connections");
		mnNewMenu.setBounds(10, 0, 107, 22);
		mainMenuBar.add(mnNewMenu);

		setPreferredSize(new Dimension(441, 480));

		JMenuItem connectToServer = new JMenuItem("Connect To Server");
		connectToServer.setIcon(new ImageIcon(getClass().getResource(
				"16x16_connect.png")));
		mnNewMenu.add(connectToServer);

		JMenuItem disconnectFromServer = new JMenuItem("Disconnect From Server");
		disconnectFromServer.setIcon(new ImageIcon(getClass().getResource(
				"16x16_disconnect.png")));
		mnNewMenu.add(disconnectFromServer);
		mnNewMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit");
		mnNewMenu.add(exitItem);

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		TextField = new JTextField();
		TextField.setBounds(10, 372, 408, 25);
		panel.add(TextField);
		TextField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {

				if (arg0.getKeyCode() == 10) {
					str = TextField.getText();
					TextField.setText(null);
					try {
						System.out.println(str);
						InputMessage.SendMessage(str);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			public void keyReleased(KeyEvent arg0) {

			}

			public void keyTyped(KeyEvent arg0) {

			}
		});

		TextField2 = new JTextField();
		TextField2.setBounds(10, 380, 215, 30);

		getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(0, 0, 428, 39);
		panel.add(panel_1);
		panel_1.setLayout(null);

		final JToggleButton tglbtnNewToggleButton = new JToggleButton("");
		tglbtnNewToggleButton.setBounds(50, 5, 30, 30);
		panel_1.add(tglbtnNewToggleButton);

		tglbtnNewToggleButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent mEvt) {
				tglbtnNewToggleButton.setBorderPainted(true);

			}

			public void mouseExited(MouseEvent mEvt) {
				tglbtnNewToggleButton.setBorderPainted(false);
				tglbtnNewToggleButton.setFocusPainted(false);

			}

		});
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Muted = !Muted;
				tglbtnNewToggleButton.setBorderPainted(true);
				tglbtnNewToggleButton.setFocusPainted(true);
			}
		});
		tglbtnNewToggleButton.setBackground(SystemColor.control);
		tglbtnNewToggleButton.setIcon(new ImageIcon(getClass().getResource(
				"24x24_input_muted.png")));
		tglbtnNewToggleButton.setBounds(10, 5, 30, 30);
		tglbtnNewToggleButton.setBorderPainted(false);
		tglbtnNewToggleButton.setFocusPainted(false);

		final JToggleButton toggleButton = new JToggleButton("");
		toggleButton.setIcon(new ImageIcon(getClass().getResource(
				"24x24_output_muted.png")));
		toggleButton.setBorderPainted(false);
		toggleButton.setBackground(SystemColor.menu);
		toggleButton.setBounds(50, 5, 30, 30);

		toggleButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent mEvt) {
				toggleButton.setBorderPainted(true);

			}

			public void mouseExited(MouseEvent mEvt) {
				toggleButton.setBorderPainted(false);
				toggleButton.setFocusPainted(false);

			}

		});
		toggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MutedSound = !MutedSound;
				toggleButton.setBorderPainted(true);
				toggleButton.setFocusPainted(true);
			}
		});

		panel_1.add(toggleButton);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 50, 199, 317);
		panel.add(panel_2);
		panel_2.setLayout(null);

		tree = new JTree();
		tree.setVisibleRowCount(10);
		tree.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Users") {
			private static final long serialVersionUID = 1L;

			{
			}
		}));
		tree.setBounds(1, 1, 196, 313);
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree
				.getCellRenderer();
		Icon closedIcon = new ImageIcon(getClass().getResource(
				"Qj3nb_croper_ru.png"));
		Icon openIcon = new ImageIcon(getClass().getResource(
				"Qj3nb_croper_ru.png"));
		Icon leafIcon = new ImageIcon(getClass().getResource(
				"AiiMj_croper_ru.png"));
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);
		renderer.setLeafIcon(leafIcon);
		panel_2.add(tree);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_3.setBounds(219, 50, 199, 317);
		panel.add(panel_3);
		panel_3.setLayout(null);

		textPane_1 = new JTextPane();
		panel_3.add(textPane_1);

		JScrollPane scrollPane = new JScrollPane(textPane_1);
		scrollPane.setBounds(1, 1, 199, 317);
		panel_3.add(scrollPane);
		textPane = new JTextPane();

		JScrollPane scrollBar = new JScrollPane(textPane);
		panel_3.getRootPane().add(scrollBar);

		connectToServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IP.main1();
			}
		});
		disconnectFromServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				disconnect();
			}
		});

	}

	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		GUIThread = new GUI();
		GUIThread.start();
	}

	public static void setText(String s) {
		String gh = "";
		for (int i = 0; i < s.length(); i++) {
			if ((int) s.charAt(i) != 0) {
				gh += s.charAt(i);
				System.out.println("azaza");
			}
		}
		textPane_1.setText(textPane_1.getText() + gh + "\r\n");
	}

	public static void setTextNickName(List<String> nick) {
		System.out.println("Client2:" + nick);
		DefaultMutableTreeNode Tree = new DefaultMutableTreeNode("Users") {
			private static final long serialVersionUID = 1L;
			{
			}
		};
		tree.setModel(new DefaultTreeModel(Tree));
		if (!nick.isEmpty()) {

			for (String s : nick) {
				if (!s.equals("")) {
					Tree.add(new DefaultMutableTreeNode(s));
				}
			}
		}
	}

	public static boolean isMute() {
		return Muted;
	}

	public static boolean isMuteSound() {
		return MutedSound;
	}
}

class GUI extends Thread {
	public GUI() throws UnknownHostException, IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		JFrame.setDefaultLookAndFeelDecorated(true);
		MainWindow frame = new MainWindow();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}