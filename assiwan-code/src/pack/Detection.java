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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Detection extends JFrame implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;
	private boolean alarmeB, ajoutB=true;
	private boolean smsB, emailB, alarmPlayed=false;
	private String message="Inconnu", activeFor, alarmeFor, alarmeSound;

	// Les attributs de la forme:
	private JLayeredPane detectionPane = new JLayeredPane();
	private JButton quiter, aide, annuler;
	private JLabel detectionBck;
	private final ImageIcon annulerGif;
	private JPanel moveSpace = new JPanel();

	// Les attributs de corps:
	private JPanel detectionPanel;
	private JButton arreter, ajouter;
	private JLabel persoName;
	private Font pc1 = new Font("Diavlo Bold", 0, 35);
	private Font pc2 = new Font("Diavlo Bold", 0, 23);
	private Font pc3 = new Font("Diavlo Bold", 0, 20);
	private Alarm a;
	private String email="moha.elouastani@gmail.com";

	public Detection(String nomCam, Mat face) throws IOException{

		// Traitement de la forme:

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(280, 100, 634, 474);
		setLayout(new BorderLayout());
		detectionPane.setBounds(0, 0, 634, 474);
		add(detectionPane, BorderLayout.CENTER);

		// Traitement de corps:

		// Le Panel:
		detectionPanel = new JPanel();
		detectionPanel.setBounds(17, 87, 600, 300);
		detectionPanel.setOpaque(false);
		detectionPanel.setVisible(true);
		detectionPane.add(detectionPanel, new Integer(0), 0);
		
		// Les labels:
			//** Photo:
		Mat faceResize=new Mat();
		Imgproc.resize(face, faceResize, new Size(99, 99));
		ImageIcon image = new ImageIcon(
				Window.Mat2bufferedImage(faceResize));
		JLabel facePic=new JLabel();
		facePic.setBounds(64, 190, 106, 106);
		facePic.setIcon(image);
		facePic.setHorizontalAlignment(SwingConstants.CENTER);
		facePic.setVerticalAlignment(SwingConstants.CENTER);
		detectionPane.add(facePic);
		 	//** Nom de personne détectée:
		persoName=new JLabel();
		persoName.setBounds(167, 150, 400, 40);
		persoName.setFont(pc1);
		persoName.setForeground(Color.green);
		persoName.setHorizontalAlignment(SwingConstants.CENTER);
		persoName.setVerticalAlignment(SwingConstants.CENTER);
		detectionPane.add(persoName);
			//** Nom de caméra:
		JLabel camName=new JLabel();
		camName.setText(nomCam);
		camName.setBounds(295, 203, 200, 30);
		camName.setFont(pc2);
		camName.setForeground(Color.WHITE);
		detectionPane.add(camName);
			//** Date de détection:
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date d = new Date();
		JLabel date=new JLabel(dateFormat.format(d));
		date.setBounds(385, 235, 150, 30);
		date.setFont(pc3);
		date.setForeground(Color.WHITE);
		detectionPane.add(date);
			//** Heur de détection:
		JLabel heur=new JLabel(timeFormat.format(d));
		heur.setBounds(385, 263, 150, 30);
		heur.setFont(pc3);
		heur.setForeground(Color.WHITE);
		detectionPane.add(heur);
		
		
		// Boutton arrêter l'alarme:
		arreter = new JButton();
		arreter.setBounds(110, 395, 105, 38);
		arreter.setVisible(true);
		Menu.decoration_boutton(arreter, "arreter.png", "null",
				"arreterPressed.png");
		detectionPane.add(arreter);
		arreter.addActionListener(this);
		
		// Boutton Ajouter comme connu:
		ajouter = new JButton();
		ajouter.setBounds(364, 395, 105, 38);
		ajouter.setVisible(true);
		Menu.decoration_boutton(ajouter, "ajouterLite.png", "null", "ajouterLitePressed.png");
		detectionPane.add(ajouter);
		ajouter.addActionListener(this);

		// Les Bouttons de la forme:

		// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(593, 25, 25, 25);
		Menu.decoration_boutton(quiter, "quiter.png", "quiterHover.png",
				"quiterPressed.png");
		detectionPane.add(quiter);
		quiter.addActionListener(this);

		// **Boutton aide:
		aide = new JButton();
		aide.setBounds(566, 25, 25, 25);
		Menu.decoration_boutton(aide, "aide.png", "aideHover.png",
				"aidePressed.png");
		detectionPane.add(aide);
		aide.addActionListener(this);

		// **Boutton Annuler:
		annuler = new JButton();
		annuler.setBounds(490, 403, 112, 70);
		annuler.setVisible(true);
		Menu.decoration_boutton(annuler, "ajoutBtn.png", "null",
				"annulerPressed.png");
		// Gif image:
		annulerGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/annulerHover.gif")));
		annuler.setRolloverIcon(annulerGif);
		detectionPane.add(annuler);
		// Listeners:
		annuler.addMouseListener(this);
		annuler.addActionListener(this);

		// Les Labels:

		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(70, 13, 500, 50);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		detectionPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);

		// **L'arrière plan de la fenêtre:
		detectionBck = new JLabel();
		detectionBck.setIcon(new ImageIcon(Menu.class
				.getResource("../images/"+ Menu.theme+"/detectionBck.png")));
		detectionBck.setBounds(0, 0, 273, 150);
		this.add(detectionBck);
		// Initialisation via la base de données:
		ResultSet rs=null;
		Statement statement=null;
		try {
		    statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs = statement.executeQuery("select * from parametres");
			while (rs.next()) {
				// Initialisation des paramètres de détection:
				if(rs.getString("activerSMS").equals("on")) smsB=true;
				else smsB=false;
				if(rs.getString("activerEmail").equals("on")) emailB=true;
				else emailB=false;
				activeFor=rs.getString("activeFor");
				email=rs.getString("email");
				// Initialisation d'alarme:
				if(rs.getString("activerAlarme").equals("on")) alarmeB=true;
				else alarmeB=false;
				alarmeFor=rs.getString("alarmeFor");
				alarmeSound=rs.getString("alarme");
				a = new Alarm(alarmeSound);
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
	    
		// Lançer la détection:
		/*try {
			Compare c=new Compare(face);
			message=c.isSimillar();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg=new Message(2, "Erreur de détection !");
			msg.setVisible(true);
		}*/
	    Detect();
	    if(!alarmPlayed) 
			Menu.decoration_boutton(arreter, "arreterPressed.png", "null",
					"null");
	    if(message!=null){
	    	ajoutB=false;
			Menu.decoration_boutton(ajouter, "ajouterLitePressed.png", "null",
					"null");
	    }

		// Envoyer Mail:
		//EnvoyerMail mail=new EnvoyerMail(email, message, dateFormat.format(d), timeFormat.format(d));
	}

	public void Detect(){
		// Personne détectée:
		if(message==null){
			persoName.setText("Inconnu");
			persoName.setForeground(Color.red);
		}
		else{
			persoName.setText(message);
		}
		
		// Pour l'alarme:
		if(alarmeB){
			if(alarmeFor.equals("Tous le monde")){
				a.play();
				alarmPlayed=true;
			}
			else {
				if(message==null){
					a.play();
					alarmPlayed=true;
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == quiter || e.getSource() == annuler) {
			hide();
			if(alarmPlayed) a.stop();
			Window.detect=true;
		}

		if (e.getSource() == aide) {
			Message aide = new Message(0, "");
			aide.setVisible(true);
		}

		if (e.getSource() == arreter) {
			if(alarmPlayed){
				a.stop();
				Menu.decoration_boutton(arreter, "arreterPressed.png", "null",
						"null");
				
				alarmPlayed=false;
			}
		}

		if (e.getSource() == ajouter) {
			if(ajoutB){
				
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
		annulerGif.getImage().flush();
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
