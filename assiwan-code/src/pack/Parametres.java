package pack;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Parametres extends JFrame implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;
	private String themeC;
	private boolean alarmB, smsB, emailB, detectB, testB = true;

	// Les attributs de la forme:
	private JLayeredPane paramPane = new JLayeredPane();
	private JButton quiter, aide, sauvegarder;
	private JButton themes, paramDetect, alarme, faces;
	private JLabel ajoutBck;
	private final ImageIcon sauvegarderGif, themesGif, paramDetectGif,
			alarmeGif, facesGif;
	private JPanel moveSpace = new JPanel();

	// Les attributs de corps:
	private JPanel themePanel, alarmePanel, detectPanel, facesPanel;
	private JButton theme0, theme1, theme2;
	private JButton alarmeOn, smsOn, emailOn, detectOn, tester, gerer;
	private JTextField email, tel;
	private JComboBox<String> activePour, alarmeSound, detectePour;
	private Font pc = new Font("Diavlo Bold", 0, 18);
	private Alarm a;

	@SuppressWarnings({ "rawtypes", "unchecked"})
	public Parametres() {

		// Traitement de la forme:

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(280, 100, 634, 474);
		setLayout(new BorderLayout());
		paramPane.setBounds(0, 0, 634, 474);
		add(paramPane, BorderLayout.CENTER);

		// Traitement de corps:

		// **Paramètres de thème:

		themePanel = new JPanel();
		themePanel.setBounds(17, 87, 600, 300);
		themePanel.setOpaque(false);
		themePanel.setVisible(false);
		JLabel themeLabel = new JLabel();
		themeLabel.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/themePanel.png"))));
		themePanel.add(themeLabel);
		paramPane.add(themePanel, new Integer(0), 0);
		// Les Thèmes:
		// # theme 0:
		theme0 = new JButton();
		theme0.setBounds(30, 120, 170, 140);
		theme0.setVisible(true);
	    Menu.decoration_boutton(theme0, "themeYellow.png", "themeYellowHover.png", "null");
		themeLabel.add(theme0);
		theme0.addActionListener(this);
		// # theme 1:
		theme1 = new JButton();
		theme1.setBounds(210, 120, 170, 140);
		theme1.setVisible(true);
	    Menu.decoration_boutton(theme1, "themeBlue.png", "themeBlueHover.png", "null");
		themeLabel.add(theme1);
		theme1.addActionListener(this);
		// # theme 2:
		theme2 = new JButton();
		theme2.setBounds(390, 120, 170, 140);
		theme2.setVisible(true);
	    Menu.decoration_boutton(theme2, "themeRed.png", "themeRedHover.png", "null");
		themeLabel.add(theme2);
		theme2.addActionListener(this);

		// **Paramètres de l'alarme:

		alarmePanel = new JPanel();
		alarmePanel.setBounds(17, 87, 600, 300);
		alarmePanel.setOpaque(false);
		alarmePanel.setVisible(false);
		JLabel alarmeLabel = new JLabel();
		alarmeLabel.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/alarmePanel.png"))));
		alarmePanel.add(alarmeLabel);
		paramPane.add(alarmePanel, new Integer(0), 0);
		// Activer l'alarme:
		alarmeOn = new JButton();
		alarmeOn.setBounds(300, 107, 94, 34);
		alarmeOn.setVisible(true);
		Menu.decoration_boutton(alarmeOn, "off.png", "off.png", "null");
		alarmeLabel.add(alarmeOn);
		alarmeOn.addActionListener(this);
		// Activé pour:
		activePour = new JComboBox();
		activePour.setModel(new DefaultComboBoxModel(new String[] {
				"Tous le monde", "Les inconnus seulement" }));
		activePour.setFont(pc);
		activePour.setBounds(303, 163, 240, 26);
		alarmeLabel.add(activePour);
		// Choisir le son d'alarme:
		alarmeSound = new JComboBox();
		alarmeSound.setModel(new DefaultComboBoxModel(new String[] {
				"Alarme 1", "Alarme 2", "Alarme 3" }));
		alarmeSound.setFont(pc);
		alarmeSound.setBounds(303, 210, 110, 26);
		alarmeLabel.add(alarmeSound);
		// Tester:
		tester = new JButton();
		tester.setBounds(430, 204, 105, 38);
		tester.setVisible(true);
		Menu.decoration_boutton(tester, "tester.png", "null",
				"testerPressed.png");
		alarmeLabel.add(tester);
		tester.addActionListener(this);

		// **Gestion des personnes:

		facesPanel = new JPanel();
		facesPanel.setBounds(17, 87, 600, 300);
		facesPanel.setOpaque(false);
		facesPanel.setVisible(false);
		JLabel facesLabel = new JLabel();
		facesLabel.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/facesPanel.png"))));
		facesPanel.add(facesLabel);
		paramPane.add(facesPanel, new Integer(0), 0);
		// Activer l'alarme:
		detectOn = new JButton();
		detectOn.setBounds(253, 127, 94, 34);
		detectOn.setVisible(true);
		Menu.decoration_boutton(detectOn, "off.png", "off.png", "null");
		facesLabel.add(detectOn);
		detectOn.addActionListener(this);
		// Gérer:
		gerer = new JButton();
		gerer.setBounds(330, 174, 105, 38);
		gerer.setVisible(true);
		Menu.decoration_boutton(gerer, "gerer.png", "null", "gererPressed.png");
		facesLabel.add(gerer);
		gerer.addActionListener(this);

		// **Paramètres de détection:

		detectPanel = new JPanel();
		detectPanel.setBounds(17, 125, 600, 300);
		detectPanel.setOpaque(false);
		detectPanel.setVisible(true);
		JLabel detectLabel = new JLabel();
		detectLabel.setIcon(new ImageIcon((Menu.class
				.getResource("../images/"+ Menu.theme+"/detectPanel.png"))));
		detectPanel.add(detectLabel);
		paramPane.add(detectPanel, new Integer(0), 0);
		// Activer le message d'alerte par SMS:
		smsOn = new JButton();
		smsOn.setBounds(300, 105, 94, 34);
		smsOn.setVisible(true);
		Menu.decoration_boutton(smsOn, "off.png", "off.png", "null");
		detectLabel.add(smsOn);
		smsOn.addActionListener(this);
		// N° de telephone:
		tel = new JTextField();
		tel.setBounds(303, 145, 200, 28);
		tel.setFont(pc);
		detectLabel.add(tel);
		// Activer le message d'alerte par Email:
		emailOn = new JButton();
		emailOn.setBounds(300, 178, 94, 34);
		emailOn.setVisible(true);
		Menu.decoration_boutton(emailOn, "off.png", "off.png", "null");
		detectLabel.add(emailOn);
		emailOn.addActionListener(this);
		// Email:
		email = new JTextField();
		email.setBounds(303, 216, 240, 28);
		email.setFont(pc);
		detectLabel.add(email);
		// Activé pour:
		detectePour = new JComboBox();
		detectePour.setModel(new DefaultComboBoxModel(new String[] {
				"Tous le monde", "Les inconnus seulement" }));
		detectePour.setFont(pc);
		detectePour.setBounds(303, 252, 240, 26);
		detectLabel.add(detectePour);

		// Les Bouttons:

		// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(593, 25, 25, 25);
		Menu.decoration_boutton(quiter, "quiter.png", "quiterHover.png",
				"quiterPressed.png");
		paramPane.add(quiter);
		quiter.addActionListener(this);

		// **Boutton aide:
		aide = new JButton();
		aide.setBounds(566, 25, 25, 25);
		Menu.decoration_boutton(aide, "aide.png", "aideHover.png",
				"aidePressed.png");
		paramPane.add(aide);
		aide.addActionListener(this);

		// **Boutton Thèmes:
		themes = new JButton();
		themes.setBounds(442, 62, 112, 90);
		themes.setVisible(true);
		Menu.decoration_boutton(themes, "theme.png", "null", "themePressed.png");
		// Gif image:
		themesGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/themeHover.gif")));
		themes.setRolloverIcon(themesGif);
		paramPane.add(themes);
		// Listeners:
		themes.addMouseListener(this);
		themes.addActionListener(this);

		// **Boutton Thèmes:
		alarme = new JButton();
		alarme.setBounds(323, 62, 112, 90);
		alarme.setVisible(true);
		Menu.decoration_boutton(alarme, "alarme.png", "null",
				"alarmePressed.png");
		// Gif image:
		alarmeGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/alarmeHover.gif")));
		alarme.setRolloverIcon(alarmeGif);
		paramPane.add(alarme);
		// Listeners:
		alarme.addMouseListener(this);
		alarme.addActionListener(this);

		// **Boutton Personnes:
		faces = new JButton();
		faces.setBounds(204, 62, 112, 90);
		faces.setVisible(true);
		Menu.decoration_boutton(faces, "personnes.png", "null",
				"personnesPressed.png");
		// Gif image:
		paramDetectGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/personnesHover.gif")));
		faces.setRolloverIcon(paramDetectGif);
		paramPane.add(faces);
		// Listeners:
		faces.addMouseListener(this);
		faces.addActionListener(this);

		// **Boutton Détection:
		paramDetect = new JButton();
		paramDetect.setBounds(85, 62, 112, 90);
		paramDetect.setVisible(true);
		Menu.decoration_boutton(paramDetect, "detection.png", "null",
				"detectionPressed.png");
		// Gif image:
		facesGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/detectionHover.gif")));
		paramDetect.setRolloverIcon(facesGif);
		paramPane.add(paramDetect);
		// Listeners:
		paramDetect.addMouseListener(this);
		paramDetect.addActionListener(this);

		// **Boutton Sauvegarder:
		sauvegarder = new JButton();
		sauvegarder.setBounds(510, 413, 90, 60);
		sauvegarder.setVisible(true);
		Menu.decoration_boutton(sauvegarder, "paramBoutton.png", "null",
				"paramBouttonPressed.png");
		// Gif image:
		sauvegarderGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/paramBouttonHover.gif")));
		sauvegarder.setRolloverIcon(sauvegarderGif);
		paramPane.add(sauvegarder);
		// Listeners:
		sauvegarder.addMouseListener(this);
		sauvegarder.addActionListener(this);

		// Les Labels:

		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(70, 13, 500, 50);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		paramPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);

		// **L'arrière plan de la fenêtre:
		ajoutBck = new JLabel();
		ajoutBck.setIcon(new ImageIcon(Menu.class
				.getResource("../images/"+ Menu.theme+"/paramBck.png")));
		ajoutBck.setBounds(0, 0, 273, 150);
		this.add(ajoutBck);

		// Initialisation via la base de données:
		ResultSet rs=null;
		Statement statement=null;
		try {
		    statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs = statement.executeQuery("select * from parametres");
			while (rs.next()) {
				// Initialisation des paramètres de détection:
				tel.setText(rs.getString("tel"));
				email.setText(rs.getString("email"));
				if (rs.getString("activeFor").equals("Tous le monde"))
					detectePour.setSelectedIndex(0);
				else
					detectePour.setSelectedIndex(1);
				if (rs.getString("activerSMS").equals("on")) {
					smsB = true;
					tel.setEditable(true);
					Menu.decoration_boutton(smsOn, "on.png", "on.png", "null");
				} else {
					smsB = false;
					tel.setEditable(false);
					Menu.decoration_boutton(smsOn, "off.png", "off.png", "null");
				}
				if (rs.getString("activerEmail").equals("on")) {
					emailB = true;
					email.setEditable(true);
					Menu.decoration_boutton(emailOn, "on.png", "on.png", "null");
				} else {
					emailB = false;
					email.setEditable(false);
					Menu.decoration_boutton(emailOn, "off.png", "off.png",
							"null");
				}

				// Initialisation d'alarme:
				alarmeSound.setSelectedItem(rs.getString("alarme"));
				a = new Alarm(alarmeSound.getSelectedItem().toString());
				if (rs.getString("alarmeFor").equals("Tous le monde"))
					activePour.setSelectedIndex(0);
				else
					activePour.setSelectedIndex(1);
				if (rs.getString("activerAlarme").equals("on")) {
					alarmB = true;
					activePour.setEditable(true);
					alarmeSound.setEditable(true);
					Menu.decoration_boutton(tester, "tester.png", "null",
							"testerPressed.png");
					Menu.decoration_boutton(alarmeOn, "on.png", "on.png",
							"null");
				} else {
					alarmB = false;
					activePour.setEditable(false);
					alarmeSound.setEditable(false);
					Menu.decoration_boutton(tester, "testerPressed.png",
							"null", "null");
					Menu.decoration_boutton(alarmeOn, "off.png", "off.png",
							"null");
				}

				// Initialisation des paramaètres de gestion des personnes:
				if (rs.getString("detection").equals("on")) {
					detectB = true;
					Menu.decoration_boutton(gerer, "gerer.png", "null",
							"gererPressed.png");
					Menu.decoration_boutton(detectOn, "on.png", "on.png",
							"null");
				} else {
					detectB = false;
					Menu.decoration_boutton(gerer, "gererPressed.png", "null",
							"null");
					Menu.decoration_boutton(detectOn, "off.png", "off.png",
							"null");
				}

				// Thèmes:
				if (rs.getString("theme").equals("yellow")) {
					themeC = "yellow";
					Menu.decoration_boutton(theme0, "themeYellowChoosed.png", "null", "null");
					Menu.decoration_boutton(theme1, "themeBlue.png", "themeBlueHover.png", "null");
					Menu.decoration_boutton(theme2, "themeRed.png", "themeRedHover.png", "null");
				} else if (rs.getString("theme").equals("blue")) {
					themeC = "blue";
					Menu.decoration_boutton(theme1, "themeBlueChoosed.png", "null", "null");
					Menu.decoration_boutton(theme0, "themeYellow.png", "themeYellowHover.png", "null");
					Menu.decoration_boutton(theme2, "themeRed.png", "themeRedHover.png", "null");
				} else if (rs.getString("theme").equals("red")) {
					themeC = "red";
					Menu.decoration_boutton(theme2, "themeRedChoosed.png", "null", "null");
					Menu.decoration_boutton(theme1, "themeBlue.png", "themeBlueHover.png", "null");
					Menu.decoration_boutton(theme0, "themeYellow.png", "themeYellowHover.png", "null");
				}
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == quiter) {
			hide();
		}

		if (e.getSource() == aide) {
			Message aide = new Message(0, "");
			aide.setVisible(true);
			processEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			new Menu().setVisible(true);
		}

		if (e.getSource() == sauvegarder) {
			if(tel.getText().isEmpty() || email.getText().isEmpty()){
				Message msg = new Message(2, "Champ non rempli !!");
				msg.setVisible(true);
			}
			else if(!email.getText().endsWith("@gmail.com") && !email.getText().endsWith("@hotmail.com")){
				Message msg = new Message(2, "Email incorrect !!");
				msg.setVisible(true);
			}
			else if(tel.getText().length()!=10 || (!tel.getText().startsWith("06") && !tel.getText().startsWith("05"))){
				Message msg = new Message(2, "N° de Tel incorrect !!");
				msg.setVisible(true);
			}
			else{
				// Modification des paramètre à la base de données:
				String themeS=themeC;
				String alarmeS;
				if(alarmB) alarmeS="on";
				else alarmeS="off";
				String smsS;
				if(smsB) smsS="on";
				else smsS="off";
				String emailS;
				if(emailB) emailS="on";
				else emailS="off";
				String detectS;
				if(detectB) detectS="on";
				else detectS="off";
				String req="UPDATE parametres SET theme='"+themeS
												+"', activerAlarme='"+alarmeS
												+"', alarmeFor='"+activePour.getSelectedItem().toString()
												+"', alarme='"+alarmeSound.getSelectedItem().toString()
												+"', activerSMS='"+smsS
												+"', tel='"+tel.getText().toString()
												+"', activerEmail='"+emailS
												+"', email='"+email.getText().toString()
												+"', activeFor='"+detectePour.getSelectedItem().toString()
												+"', detection='"+detectS+"' WHERE id=0";
				try {
					// Modification des paramètres de détection:
					Statement st=Menu.connection.createStatement();
					st.setQueryTimeout(30); // set timeout to 30 sec.
					st.executeUpdate(req);
					Message msg = new Message(2, "Modifiés avec Succès !");
					msg.setVisible(true);
					hide();
				} catch (SQLException s) {
					// if the error message is "out of memory",
					// it probably means no database file is found
					Message msg = new Message(2, "Erreur à la base !!");
					msg.setVisible(true);
					System.err.println(s.getMessage());
				}
			}
		}

		if (e.getSource() == themes) {
			themePanel.setVisible(true);
			alarmePanel.setVisible(false);
			detectPanel.setVisible(false);
			facesPanel.setVisible(false);
		}

		if (e.getSource() == alarme) {
			alarmePanel.setVisible(true);
			themePanel.setVisible(false);
			detectPanel.setVisible(false);
			facesPanel.setVisible(false);
		}

		if (e.getSource() == paramDetect) {
			alarmePanel.setVisible(false);
			themePanel.setVisible(false);
			detectPanel.setVisible(true);
			facesPanel.setVisible(false);
		}

		if (e.getSource() == faces) {
			alarmePanel.setVisible(false);
			themePanel.setVisible(false);
			detectPanel.setVisible(false);
			facesPanel.setVisible(true);
		}

		if (e.getSource() == theme0) {
			themeC = "yellow";
			Menu.decoration_boutton(theme0, "themeYellowChoosed.png", "null", "null");
			Menu.decoration_boutton(theme1, "themeBlue.png", "themeBlueHover.png", "null");
			Menu.decoration_boutton(theme2, "themeRed.png", "themeRedHover.png", "null");
		}

		if (e.getSource() == theme1) {
			themeC = "blue";
			Menu.decoration_boutton(theme1, "themeBlueChoosed.png", "null", "null");
			Menu.decoration_boutton(theme0, "themeYellow.png", "themeYellowHover.png", "null");
			Menu.decoration_boutton(theme2, "themeRed.png", "themeRedHover.png", "null");
		}

		if (e.getSource() == theme2) {
			themeC = "red";
			Menu.decoration_boutton(theme2, "themeRedChoosed.png", "null", "null");
			Menu.decoration_boutton(theme1, "themeBlue.png", "themeBlueHover.png", "null");
			Menu.decoration_boutton(theme0, "themeYellow.png", "themeYellowHover.png", "null");
		}

		if (e.getSource() == alarmeOn) {
			if (alarmB) {
				alarmB = false;
				activePour.setEditable(false);
				alarmeSound.setEditable(false);
				Menu.decoration_boutton(tester, "testerPressed.png", "null",
						"null");
				Menu.decoration_boutton(alarmeOn, "off.png", "off.png", "null");
			} else {
				alarmB = true;
				activePour.setEditable(true);
				alarmeSound.setEditable(true);
				Menu.decoration_boutton(tester, "tester.png", "null",
						"testerPressed.png");
				Menu.decoration_boutton(alarmeOn, "on.png", "on.png", "null");
			}
		}

		if (e.getSource() == smsOn) {
			if (smsB) {
				smsB = false;
				tel.setEditable(false);
				Menu.decoration_boutton(smsOn, "off.png", "off.png", "null");
			} else {
				smsB = true;
				tel.setEditable(true);
				Menu.decoration_boutton(smsOn, "on.png", "on.png", "null");
			}
		}

		if (e.getSource() == emailOn) {
			if (emailB) {
				emailB = false;
				email.setEditable(false);
				Menu.decoration_boutton(emailOn, "off.png", "off.png", "null");
			} else {
				emailB = true;
				email.setEditable(true);
				Menu.decoration_boutton(emailOn, "on.png", "on.png", "null");
			}
		}

		if (e.getSource() == detectOn) {
			if (detectB) {
				detectB = false;
				Menu.decoration_boutton(gerer, "gererPressed.png", "null",
						"null");
				Menu.decoration_boutton(detectOn, "off.png", "off.png", "null");
			} else {
				detectB = true;
				gerer.enable(true);
				Menu.decoration_boutton(gerer, "gerer.png", "null",
						"gererPressed.png");
				Menu.decoration_boutton(detectOn, "on.png", "on.png", "null");
			}

		}

		if (e.getSource() == tester) {
			a.setSound(alarmeSound.getSelectedItem().toString());
			if (testB) {
				testB = false;
				a.play();
				Menu.decoration_boutton(tester, "arreter.png", "null",
						"arreterPressed.png");
			} else {
				testB = true;
				a.stop();
				Menu.decoration_boutton(tester, "tester.png", "null",
						"testerPressed.png");
			}
		}

		if (e.getSource() == gerer) {
			GererPer ger;
			try {
				ger = new GererPer();
				ger.setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
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

	Point getScreenLocation(MouseEvent e) {
		Point cursor = e.getPoint();
		Point target_location = this.getLocationOnScreen();
		return new Point((int) (target_location.getX() + cursor.getX()),
				(int) (target_location.getY() + cursor.getY()));
	}

	public void mouseEntered(MouseEvent e) {
		sauvegarderGif.getImage().flush();
		themesGif.getImage().flush();
		paramDetectGif.getImage().flush();
		alarmeGif.getImage().flush();
		facesGif.getImage().flush();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
