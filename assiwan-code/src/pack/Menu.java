package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.opencv.core.Core;

public class Menu extends JFrame implements ActionListener, MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;
	static boolean generalDetection;
	static String theme = "yellow";

	// Les Paneaux:
	public static JPanel shadow;
	private JPanel cam2;
	private JPanel cam3;
	private JPanel cam4;
	private JPanel moveSpace = new JPanel();
	private JPanel cam1 = new JPanel();
	private JLayeredPane MenuPane = new JLayeredPane();

	// Les Bouttons:
	private final JButton ajouter, gerer, enlever, parametres, fullScr,
			acceuil;
	private final JButton quiter, aide, reduire;

	// Les Labels:
	private JLabel Bck, CameraName;
	private final ImageIcon ajouterGif, gererGif, enleverGif, fullScrGif,
			parametresGif;
	private boolean inclk;
    static boolean refresh=false;
	private static int id = 0;

	// Les Déclarations de camera:
	public static Window a, b, c, d;
	static Connection connection = null;
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public void connect() {
		// load the sqlite-JDBC driver using the current class loader:
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Message err = new Message(2, "Data base non trouvée !");
			err.setVisible(true);
		}
		try {
			// Ouvrir la base de données:
			connection = DriverManager.getConnection("jdbc:sqlite:"+Menu.class.getResource("../SQLiteDB/Data.db");
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur à la Base !!");
			msg.setVisible(true);
		}
	}

	public Menu() {

		// Traitement de forme générale:
		connect();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(200, 15, 850, 700);
		setLayout(new BorderLayout());
		MenuPane.setBounds(0, 0, 850, 700);
		add(MenuPane, BorderLayout.CENTER);
		MenuPane.addMouseListener(this);

		// Initialisation via la base de données:

		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Statement statement = null;
		// Initialisation des caméras:
		try {
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs1 = statement.executeQuery("SELECT * FROM window");
			while (rs1.next()) {
				boolean isFull, isDetected;
				if (rs1.getInt("id") == 1) {
					if (rs1.getString("isDetected").equals("on"))
						isDetected = true;
					else
						isDetected = false;
					if (rs1.getString("isFull").equals("on"))
						isFull = true;
					else
						isFull = false;
					a = new Window(1, rs1.getString("name"),
							rs1.getInt("port"), rs1.getInt("color"),
							rs1.getInt("x"), rs1.getInt("y"), isDetected,
							isFull, false);
				} else if (rs1.getInt("id") == 2) {
					if (rs1.getString("isDetected").equals("on"))
						isDetected = true;
					else
						isDetected = false;
					if (rs1.getString("isFull").equals("on"))
						isFull = true;
					else
						isFull = false;
					b = new Window(2, rs1.getString("name"),
							rs1.getInt("port"), rs1.getInt("color"),
							rs1.getInt("x"), rs1.getInt("y"), isDetected,
							isFull, false);
				} else if (rs1.getInt("id") == 3) {
					if (rs1.getString("isDetected").equals("on"))
						isDetected = true;
					else
						isDetected = false;
					if (rs1.getString("isFull").equals("on"))
						isFull = true;
					else
						isFull = false;
					c = new Window(3, rs1.getString("name"),
							rs1.getInt("port"), rs1.getInt("color"),
							rs1.getInt("x"), rs1.getInt("y"), isDetected,
							isFull, false);
				} else {
					if (rs1.getString("isDetected").equals("on"))
						isDetected = true;
					else
						isDetected = false;
					if (rs1.getString("isFull").equals("on"))
						isFull = true;
					else
						isFull = false;
					d = new Window(4, rs1.getString("name"),
							rs1.getInt("port"), rs1.getInt("color"),
							rs1.getInt("x"), rs1.getInt("y"), isDetected,
							isFull, false);
				}
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
		
		//Initialisation des paramètres:
		try{
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs2 = statement.executeQuery("SELECT * FROM parametres");
			while (rs2.next()) {
				theme = rs2.getString("theme");
				if (rs2.getString("detection").equals("on"))
					generalDetection = true;
				else
					generalDetection = false;
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
		
		// Les Paneaux:

		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(80, 0, 670, 60);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		MenuPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);

		// **Panel Shadow:
		shadow = new JPanel();
		shadow.setBounds(30, 98, 390, 251);
		shadow.setOpaque(false);
		shadow.setVisible(false);
		JLabel shadowLabel = new JLabel();
		shadowLabel.setIcon(new ImageIcon((Menu.class.getResource("../images/"
				+ theme + "/shadow.png"))));
		shadow.add(shadowLabel);
		MenuPane.add(shadow, new Integer(1), 0);

		// **Corps de l'application:
		// #Camera N°1:
		cam1 = new JPanel();
		cam1.add(a);
		cam1.setBounds(30, 98, 390, 250);
		cam1.setOpaque(false);
		cam1.setVisible(true);
		MenuPane.add(cam1, new Integer(0), 0);
		a.addMouseListener(this);

		// #Camera N°2:
		cam2 = new JPanel();
		cam2.add(b);
		cam2.setBounds(431 + (1 / 2), 98, 390, 250);
		cam2.setOpaque(false);
		cam2.setVisible(true);
		MenuPane.add(cam2, new Integer(0), 0);
		b.addMouseListener(this);

		// #Camera N°3:
		cam3 = new JPanel();
		cam3.add(c);
		cam3.setBounds(30, 360, 390, 250);
		cam3.setOpaque(false);
		cam3.setVisible(true);
		MenuPane.add(cam3, new Integer(0), 0);
		c.addMouseListener(this);

		// #Camera N°4:
		cam4 = new JPanel();
		cam4.add(d);
		cam4.setBounds(431 + (1 / 2), 360, 390, 250);
		cam4.setOpaque(false);
		cam4.setVisible(true);
		MenuPane.add(cam4, new Integer(0), 0);
		d.addMouseListener(this);
		startCams();

		// Les Bouttons:

		// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(809, 22, 25, 25);
		decoration_boutton(quiter, "quiter.png", "quiterHover.png",
				"quiterPressed.png");
		MenuPane.add(quiter);
		quiter.addActionListener(this);

		// **Boutton réduire:
		reduire = new JButton();
		reduire.setBounds(783, 22, 25, 25);
		decoration_boutton(reduire, "reduire.png", "reduireHover.png",
				"reduirePressed.png");
		MenuPane.add(reduire);
		reduire.addActionListener(this);

		// **Boutton aide:
		aide = new JButton();
		aide.setBounds(757, 22, 25, 25);
		decoration_boutton(aide, "aide.png", "aideHover.png", "aidePressed.png");
		MenuPane.add(aide);
		aide.addActionListener(this);

		// **Boutton acceuil:
		acceuil = new JButton();
		acceuil.setBounds(14, 10, 46, 46);
		decoration_boutton(acceuil, "acceuil.png", "null", "acceuilPressed.png");
		MenuPane.add(acceuil);
		acceuil.addActionListener(this);

		// **Boutton Ajouter une camera:
		ajouter = new JButton();
		ajouter.setBounds(145, 614, 112, 85);
		decoration_boutton(ajouter, "boutton.png", "null",
				"ajouterCamPressed.png");
		// Gif image:
		ajouterGif = new ImageIcon((Menu.class.getResource("../images/" + theme
				+ "/ajouterCamHover.gif")));
		ajouter.setRolloverIcon(ajouterGif);
		// Listeners:
		ajouter.addMouseListener(this);
		ajouter.addActionListener(this);
		MenuPane.add(ajouter);

		// **Boutton Gérer une camera:
		gerer = new JButton("Gérer");
		gerer.setBounds(259, 614, 116, 85);
		decoration_boutton(gerer, "boutton.png", "null", "gererCamPressed.png");
		// Gif image:
		gererGif = new ImageIcon((Menu.class.getResource("../images/" + theme
				+ "/gererCamHover.gif")));
		gerer.setRolloverIcon(gererGif);
		// Listeners:
		gerer.addMouseListener(this);
		gerer.addActionListener(this);
		MenuPane.add(gerer);

		// **Boutton Enlever une camera:
		enlever = new JButton("Enlever");
		enlever.setBounds(369, 614, 116, 85);
		decoration_boutton(enlever, "boutton.png", "null",
				"enleverCamPressed.png");
		// Gif image:
		enleverGif = new ImageIcon((Menu.class.getResource("../images/" + theme
				+ "/enleverCamHover.gif")));
		enlever.setRolloverIcon(enleverGif);
		// Listeners:
		enlever.addMouseListener(this);
		enlever.addActionListener(this);
		MenuPane.add(enlever);

		// **Boutton Paramètres de detection:
		parametres = new JButton("Detecter");
		parametres.setBounds(481, 614, 116, 85);
		decoration_boutton(parametres, "boutton.png", "null",
				"bouttonPressed.png");
		// Gif image:
		parametresGif = new ImageIcon((Menu.class.getResource("../images/"
				+ theme + "/bouttonHover.gif")));
		parametres.setRolloverIcon(parametresGif);
		// Listeners:
		parametres.addMouseListener(this);
		parametres.addActionListener(this);
		MenuPane.add(parametres);

		// **Boutton Aller au mode fullscreen:
		fullScr = new JButton("fullScr");
		fullScr.setBounds(593, 614, 112, 85);
		decoration_boutton(fullScr, "boutton.png", "null",
				"fullScrnPressed.png");
		// Gif image:
		fullScrGif = new ImageIcon((Menu.class.getResource("../images/" + theme
				+ "/fullScrnHover.gif")));
		fullScr.setRolloverIcon(fullScrGif);
		// Listeners:
		fullScr.addMouseListener(this);
		fullScr.addActionListener(this);
		MenuPane.add(fullScr);

		// Les Labels:

		// **le nam de Camera:
		CameraName = new JLabel("Camera sans nom");
		CameraName.setHorizontalAlignment(SwingConstants.CENTER);
		CameraName.setVerticalAlignment(SwingConstants.CENTER);
		CameraName.setFont(new Font("Diavlo Bold", 0, 18));
		CameraName.setBounds(80, -8, 230, 40);
		CameraName.setForeground(new Color(31, 33, 43));
		shadowLabel.add(CameraName);

		// **L'arrière plan de l'application:
		Bck = new JLabel();
		Bck.setIcon(new ImageIcon(Menu.class.getResource("../images/" + theme
				+ "/bck.png")));
		Bck.setBounds(0, 0, 850, 700);
		this.add(Bck);
		
		
		
	}

	public void killCams() {
		a.kill();
		b.kill();
		c.kill();
		d.kill();
	}

	public void startCams() {
		if (a.isfull()) {
			a.start();
			a.setVisible(true);
		} else {
			a.kill();
			a.setVisible(false);
		}
		if (b.isfull()) {
			b.start();
			b.setVisible(true);
		} else {
			b.kill();
			b.setVisible(false);
		}
		if (c.isfull()) {
			c.start();
			c.setVisible(true);
		} else {
			c.kill();
			c.setVisible(false);
		}
		if (d.isfull()) {
			d.start();
			d.setVisible(true);
		} else {
			d.kill();
			d.setVisible(false);
		}
	}

	public static void decoration_boutton(final JButton boutton, String img1,
			String img2, String img3) {
		boutton.setBorder(null);
		boutton.setBorderPainted(false);
		boutton.setContentAreaFilled(false);
		boutton.setOpaque(false);
		if (img1 != "null")
			boutton.setIcon(new ImageIcon((Menu.class.getResource("../images/"
					+ theme + "/" + img1))));
		if (img2 != "null")
			boutton.setRolloverIcon(new ImageIcon((Menu.class
					.getResource("../images/" + theme + "/" + img2))));
		if (img3 != "null")
			boutton.setPressedIcon(new ImageIcon((Menu.class
					.getResource("../images/" + theme + "/" + img3))));
		boutton.setContentAreaFilled(false);
	}

	public void openUrl(String url) throws IOException, URISyntaxException {
		if (java.awt.Desktop.isDesktopSupported()) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
				java.net.URI uri = new java.net.URI(url);
				desktop.browse(uri);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == quiter) {
			System.exit(0);
		}

		if (e.getSource() == reduire) {
			setState(JFrame.ICONIFIED);
		}

		if (e.getSource() == aide) {
			Message aide = new Message(0, "");
			aide.setVisible(true);
		}

		if (e.getSource() == acceuil) {
			String url = Menu.class.getResource("../helpSite/index.html")
					.toString();
			try {
				openUrl(url);
			} catch (IOException | URISyntaxException e1) {
				// TODO Auto-generated catch block
				Message msg = new Message(2, "Erreur d'accès !!");
				msg.setVisible(true);
				e1.printStackTrace();
			}
		}

		if (e.getSource() == ajouter) {
			ResultSet rs;
			Statement statement = null;
			int Id = 0;
			try {
				statement = Menu.connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.
				rs = statement.executeQuery("select * from window");
				while (rs.next()) {
					if (rs.getString("isFull").equals("off")) {
						Id = rs.getInt("id");
						break;
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				Message m = new Message(2, "Erreur à la base !!");
				m.setVisible(true);
				e1.printStackTrace();
			}
			if (Id == 0) {
				Message m = new Message(2, "Pas de fenêtre vacante!");
				m.setVisible(true);
			} else {
				AjouterCam ajout = new AjouterCam(Id);
				ajout.pack();
				ajout.setVisible(true);
			}
		}

		if (e.getSource() == gerer) {
			if (shadow.isVisible() || id != 0) {
				ModifierCam modif = new ModifierCam(id);
				modif.pack();
				modif.setVisible(true);
			} else {
				Message alert = new Message(2, "Sélectionner une camera !!");
				alert.setVisible(true);
			}
			shadow.setVisible(false);
			unClicked();
		}

		if (e.getSource() == enlever) {
			if (inclk && (shadow.isVisible() && id != 0)) {
				Message alert = new Message(1, "Êtes-vous sûres ?");
				alert.setVisible(true);
			} else {
				Message alert = new Message(2, "Sélectionner une camera !!");
				alert.setVisible(true);
				refresh();
			}
			shadow.setVisible(false);
			unClicked();
			inclk=false;
		}

		if (e.getSource() == parametres) {
			Parametres param = new Parametres();
			param.pack();
			param.setVisible(true);
		}

		if (e.getSource() == fullScr) {
			if (shadow.isVisible() || id != 0) {
				FullScreen winFullScrn;
				winFullScrn = new FullScreen(id);
				winFullScrn.pack();
				winFullScrn.setVisible(true);
			} else {
				Message alert = new Message(2, "Sélectionner une camera !!");
				alert.setVisible(true);
			}
			shadow.setVisible(false);
			unClicked();
		}
	}

	public void refresh() {
		// TODO Auto-generated method stub
		killCams();
		this.dispose();
		new Menu().setVisible(true);
	}

	public static void enlever() {
		Statement statement = null;
		String lettre = "A";
		if (id == 2)
			lettre = "B";
		else if (id == 3)
			lettre = "C";
		else
			lettre = "D";
		String req = "UPDATE window SET modele='Par défaut', name='Camera "
				+ lettre + "', port=200, x=390,"
				+ " y=250, color=0, isDetected='off', isFull='off' WHERE id="
				+ id;
		try {
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			statement.executeUpdate(req);
			Message msg = new Message(2, "Enlevée aavec succés !");
			msg.setVisible(true);
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur à la base !!");
			msg.setVisible(true);
		}

	}

	public void mouseClicked(MouseEvent e) {

		if (e.getComponent() == a && a.isfull()) {
			unClicked();
			a.setClicked(true);
			a.start();
			id = 1;
			CameraName.setText(a.getName());
			shadow.setBounds(cam1.getBounds());
			shadow.setVisible(true);
			inclk=true;
		}

		if (e.getComponent() == b && b.isfull()) {
			unClicked();
			b.setClicked(true);
			b.start();
			id = 2;
			CameraName.setText(b.getName());
			shadow.setBounds(cam2.getBounds());
			shadow.setVisible(true);
			inclk=true;
		}

		if (e.getComponent() == c && c.isfull()) {
			unClicked();
			c.setClicked(true);
			c.start();
			id = 3;
			CameraName.setText(c.getName());
			shadow.setBounds(cam3.getBounds());
			shadow.setVisible(true);
			inclk=true;
		}

		if (e.getComponent() == d && d.isfull()) {
			unClicked();
			d.setClicked(true);
			d.start();
			id = 4;
			CameraName.setText(d.getName());
			shadow.setBounds(cam4.getBounds());
			shadow.setVisible(true);
			inclk=true;
		}

		if (e.getComponent() == MenuPane) {
			shadow.setVisible(false);
			unClicked();
			inclk=false;
		}
	}

	private void unClicked() {
		// TODO Auto-generated method stub
		a.setClicked(false);
		b.setClicked(false);
		c.setClicked(false);
		d.setClicked(false);
		shadow.setVisible(false);
	}

	Point getScreenLocation(MouseEvent e) {
		Point cursor = e.getPoint();
		Point target_location = this.getLocationOnScreen();
		return new Point((int) (target_location.getX() + cursor.getX()),
				(int) (target_location.getY() + cursor.getY()));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.start_drag = this.getScreenLocation(e);
		this.start_loc = this.getLocation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point current = this.getScreenLocation(e);
		Point offset = new Point(
				(int) current.getX() - (int) start_drag.getX(),
				(int) current.getY() - (int) start_drag.getY());
		Point new_location = new Point(
				(int) (this.start_loc.getX() + offset.getX()),
				(int) (this.start_loc.getY() + offset.getY()));
		this.setLocation(new_location);
	}

	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		ajouterGif.getImage().flush();
		gererGif.getImage().flush();
		enleverGif.getImage().flush();
		parametresGif.getImage().flush();
		fullScrGif.getImage().flush();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	public static void main(String[] args) {
		Menu win = new Menu();
		win.pack();
		win.setVisible(true);
		win.setBounds(200, 15, 850, 700);
	}

}
