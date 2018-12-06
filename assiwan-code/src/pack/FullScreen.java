package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class FullScreen extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static int i = 1, inC = 1, nextC;
	private static boolean isPaused = false;
	private Window myWindow;
	private final int myWidth, myHeight;

	// Les Paneaux:
	private JLayeredPane FullScrnPane = new JLayeredPane();
	private JPanel shadow, head, picture, nextBar, exitBar, horloge;
	private JLabel headLabel;

	// Les Bouttons:
	private final JButton in, out, left, right, top, bottom, center, pause;
	private final JButton next, pervious, exit;

	public FullScreen(int id) {
		// Traitement de forme générale:
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		Menu.shadow.setVisible(false);
		add(FullScrnPane, BorderLayout.CENTER);
		FullScrnPane.addMouseListener(this);

		// Initialisations de la taille:
		
		nextC=id;
		myWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		myHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		// Initialisation via la base de données:

		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs = statement.executeQuery("SELECT * FROM window WHERE id=" + id);
			while (rs.next()) {
				boolean isFull, isDetected;
				if (rs.getString("isDetected").equals("on"))
					isDetected = true;
				else
					isDetected = false;
				if (rs.getString("isFull").equals("on"))
					isFull = true;
				else
					isFull = false;
				myWindow = new Window(1, rs.getString("name"),
						rs.getInt("port"), rs.getInt("color"), myWidth,
						myHeight, isDetected, isFull, false);
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}

		// **Panel Head:
		head = new JPanel();
		head.setBounds((int) ((myWidth - 390) / 2), 15, 390, 37);
		head.setVisible(false);
		head.setBackground(new Color(0, 0, 0, 50));
		// Nom de camera:
		headLabel = new JLabel(myWindow.getName());
		headLabel.setForeground(Color.WHITE);
		headLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headLabel.setVerticalAlignment(SwingConstants.CENTER);
		headLabel.setFont(new Font("Diavlo Bold", 0, 18));
		headLabel.setBounds(80, -8, 390, 40);
		head.add(headLabel);
		FullScrnPane.add(head, new Integer(1), 0);

		// **Panel Exit:
		exitBar = new JPanel();
		exitBar.setBounds((int) (myWidth - 50), 10, 40, 50);
		exitBar.setOpaque(false);
		exitBar.setVisible(false);
		FullScrnPane.add(exitBar, new Integer(1), 0);

		// **Panel Shadow:
		shadow = new JPanel();
		shadow.setBounds((int) ((myWidth - 110) / 2), 350, 110, 130);
		shadow.setOpaque(false);
		shadow.setVisible(false);
		JLabel cercle = new JLabel();
		cercle.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/cercle.png"))));
		shadow.add(cercle);
		FullScrnPane.add(shadow, new Integer(1), 0);

		// **Panel NextBar:
		nextBar = new JPanel();
		nextBar.setBounds((int) ((myWidth - 180) / 2), 450, 180, 60);
		nextBar.setOpaque(false);
		nextBar.setVisible(false);
		JLabel nextLabel = new JLabel();
		nextLabel.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/nextLabel.png"))));
		nextBar.add(nextLabel);
		FullScrnPane.add(nextBar, new Integer(1), 0);

		// **Panel Picture:
		picture = new JPanel();
		picture.setBounds(0, 0, myWidth, myHeight);
		myWindow.start();
		myWindow.addMouseListener(this);
		picture.add(myWindow);
		picture.setVisible(true);
		FullScrnPane.add(picture, new Integer(0), 0);

		// **Panel Horloge:
		horloge = new JPanel();
		horloge.setBounds(565, myHeight - 40, 180, 36);
		horloge.setBackground(new Color(0, 0, 0, 50));
		horloge.setVisible(false);
		SimpleDateFormat a = new SimpleDateFormat();
		JLabel h = new JLabel(a.toString());
		horloge.add(h);
		FullScrnPane.add(horloge, new Integer(0), 0);

		// **Les Bouttons de Panel Shadow:
		// Zoom in:
		in = new JButton();
		in.setBounds(75, 70, 28, 28);
		Menu.decoration_boutton(in, "in.png", "inHover.png", "inPressed.png");
		cercle.add(in);
		in.addActionListener(this);
		// Zoom out:
		out = new JButton();
		out.setBounds(7, 70, 28, 28);
		Menu.decoration_boutton(out, "out.png", "outHover.png",
				"outPressed.png");
		cercle.add(out);
		out.addActionListener(this);
		// Pause/Play:
		pause = new JButton();
		pause.setBounds(41, 85, 28, 28);
		Menu.decoration_boutton(pause, "pause.png", "pauseHover.png",
				"pausePressed.png");
		cercle.add(pause);
		pause.addActionListener(this);
		// Left:
		left = new JButton();
		left.setBounds(20, 31, 22, 22);
		Menu.decoration_boutton(left, "flech.png", "flechHover.png",
				"flechPressed.png");
		cercle.add(left);
		left.addActionListener(this);
		// Right:
		right = new JButton();
		right.setBounds(68, 31, 22, 22);
		Menu.decoration_boutton(right, "flech.png", "flechHover.png",
				"flechPressed.png");
		cercle.add(right);
		right.addActionListener(this);
		// Top:
		top = new JButton();
		top.setBounds(44, 8, 22, 22);
		Menu.decoration_boutton(top, "flech.png", "flechHover.png",
				"flechPressed.png");
		cercle.add(top);
		top.addActionListener(this);
		// Bottom:
		bottom = new JButton();
		bottom.setBounds(44, 55, 22, 22);
		Menu.decoration_boutton(bottom, "flech.png", "flechHover.png",
				"flechPressed.png");
		cercle.add(bottom);
		bottom.addActionListener(this);
		// Center:
		center = new JButton();
		center.setBounds(44, 31, 25, 25);
		Menu.decoration_boutton(center, "center.png", "centerPressed.png",
				"centerPressed.png");
		cercle.add(center);
		center.addActionListener(this);

		// **Les Bouttons de Panel Next & Exit:
		// Pervious:
		pervious = new JButton();
		pervious.setBounds(24, 0, 50, 50);
		Menu.decoration_boutton(pervious, "pervious.png",
				"perviousPressed.png", "perviousPressed.png");
		nextLabel.add(pervious);
		pervious.addActionListener(this);
		// Next:
		next = new JButton();
		next.setBounds(106, 0, 50, 50);
		Menu.decoration_boutton(next, "next.png", "nextHover.png",
				"nextPressed.png");
		nextLabel.add(next);
		next.addActionListener(this);
		// Exit:
		exit = new JButton();
		exit.setBounds(0, 0, 40, 40);
		Menu.decoration_boutton(exit, "exit.png", "exitHover.png",
				"exitPressed.png");
		exitBar.add(exit);
		exit.addActionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (i % 2 == 1) {
			head.setVisible(true);
			exitBar.setVisible(true);
			shadow.setVisible(true);
			nextBar.setVisible(true);
		} else {
			head.setVisible(false);
			exitBar.setVisible(false);
			nextBar.setVisible(false);
			shadow.setVisible(false);
		}
		i++;
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exit) {
			hide();
			myWindow.kill();
		}

		if (e.getSource() == pervious) {

		}

		if (e.getSource() == next) {
			ResultSet rs = null;
			Statement statement = null;
			nextC++;
			if(nextC==5) nextC=1;
			try {
				statement = Menu.connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.
				rs = statement.executeQuery("SELECT * FROM window");
				while (rs.next()) {
					if(rs.getString("isFull").equals("on")){
						nextC=rs.getInt("id");
						break;
					}
				}
			} catch (SQLException s) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println(s.getMessage());
				Message msg = new Message(2, "Erreur de la Base !!");
				msg.setVisible(true);
			}
			System.err.println(nextC);
			this.dispose();
			new FullScreen(nextC).setVisible(true);
		}

		if (e.getSource() == center) {
			myWindow.setTaille(myWidth, myHeight);
			inC = 1;
		}

		if (e.getSource() == in) {
			if (inC == 1) {
				myWindow.setTaille((int) ((1.5) * myWidth),
						(int) ((1.5) * myHeight));
				inC++;
			} else if (inC == 2) {
				myWindow.setTaille(2 * myWidth, 2 * myHeight);
				inC++;
			} else if (inC == 3) {
				myWindow.setTaille((int) ((2.5) * myWidth),
						(int) ((2.5) * myHeight));
				inC++;
			}
		}

		if (e.getSource() == out) {
			if (inC == 2) {
				myWindow.setTaille(myWidth, myHeight);
				inC--;
			} else if (inC == 3) {
				myWindow.setTaille((int) ((1.5) * myWidth),
						(int) ((1.5) * myHeight));
				inC--;
			} else if (inC == 4) {
				myWindow.setTaille(2 * myWidth, 2 * this.myHeight);
				inC--;
			}
		}

		if (e.getSource() == top) {
		}

		if (e.getSource() == bottom) {
			// myWindow.setTaille(x, y);
		}

		if (e.getSource() == left) {
			// myWindow.setTaille(x, y);
		}

		if (e.getSource() == right) {
			// myWindow.setTaille(x, y);
		}

		if (e.getSource() == pause) {
			if (!isPaused) {
				myWindow.kill();
				Menu.decoration_boutton(pause, "play.png", "playHover.png",
						"playPressed.png");
				isPaused = true;
			} else {
				myWindow.start();
				Menu.decoration_boutton(pause, "pause.png", "pauseHover.png",
						"pausePressed.png");
				isPaused = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
