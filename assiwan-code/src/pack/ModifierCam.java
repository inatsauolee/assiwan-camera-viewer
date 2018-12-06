package pack;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

public class ModifierCam extends JFrame implements ActionListener, MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;
	private int id;
	
	////Les attributs de la forme:
	private JPanel moveSpace=new JPanel();
	private JLayeredPane modifPane = new JLayeredPane();
	private JButton quiter, aide, sauvegarder, annuler;
	private JLabel modifBck;
	private final ImageIcon sauvegarderGif, annulerGif;
	
	//Les attributs de corps:
	private JButton onoff=new JButton();
	private JComboBox<String> taille, coleur;
	private JTextField nom=new JTextField();

	private Font pc=new Font("Diavlo Bold", 0, 18);
	private boolean off=true;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModifierCam(int id){
		
		// Traitement de la forme:
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(300, 50, 500, 632);
		setLayout(new BorderLayout());
		modifPane.setBounds(0, 0, 500, 632);
		add(modifPane, BorderLayout.CENTER);
		this.id=id;
		
		//Traitement de corps:
		
			//** Taille de sortie:
		taille=new JComboBox();
		taille.setModel(new DefaultComboBoxModel (new String[] { "390 x 250", "585 x 375", "780 x 500" }));
		taille.setFont(pc);
		taille.setBounds(220, 324, 120, 26);					
		modifPane.add(taille);
		
			//** List des modes de coleur:
		coleur=new JComboBox();
		coleur.setModel(new DefaultComboBoxModel (new String[] { "RBG", "Gray", "HLS" }));
		coleur.setFont(pc);
		coleur.setBounds(220, 363, 120, 26);					
		modifPane.add(coleur);
		
			//** Nom d'affichage:
		nom=new JTextField();
		nom.setBounds(120, 210, 260, 33);
		nom.setFont(pc);
		modifPane.add(nom);
		
			//** Activer désactiver le mode de détection:
		onoff = new JButton();
		onoff.setBounds(260, 461, 94, 34);
		Menu.decoration_boutton(onoff, "off.png", "null", "null");
		modifPane.add(onoff);
		onoff.addActionListener(this);
		
		// Les Bouttons:

			// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(467, 15, 25, 25);
		Menu.decoration_boutton(quiter, "bigblackExit.png", "bigblackExitHover.png", "bigblackExitPressed.png");
		modifPane.add(quiter);
		quiter.addActionListener(this);
		
			// **Boutton aide:
		aide = new JButton();
		aide.setBounds(441, 15, 25, 25);
		Menu.decoration_boutton(aide, "blackAide.png", "blackAideHover.png", "blackAidePressed.png");
		modifPane.add(aide);
		aide.addActionListener(this);
		
			// **Boutton Sauvegarder:
		sauvegarder = new JButton();
		sauvegarder.setBounds(140, 561, 112, 70);
		sauvegarder.setVisible(true);
		Menu.decoration_boutton(sauvegarder, "ajoutBtn.png", "null", "sauvegarderPressed.png");
		//Gif image:
		sauvegarderGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/sauvegarderHover.gif")));
		sauvegarder.setRolloverIcon(sauvegarderGif);
		modifPane.add(sauvegarder);
		//Listeners:
		sauvegarder.addMouseListener(this);
		sauvegarder.addActionListener(this);
		
			// **Boutton Annuler:
		annuler = new JButton();
		annuler.setBounds(258, 561, 112, 70);
		annuler.setVisible(true);
		Menu.decoration_boutton(annuler, "ajoutBtn.png", "null", "annulerPressed.png");
		//Gif image:
		annulerGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/annulerHover.gif")));
		annuler.setRolloverIcon(annulerGif);
		modifPane.add(annuler);
		//Listeners:
		annuler.addMouseListener(this);
		annuler.addActionListener(this);
		
		// Les Labels:
		
		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(60, 0, 380, 54);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		modifPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);

		// **L'arrière plan de la fenêtre:
		modifBck = new JLabel();
		modifBck.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/modifBck.png")));
		modifBck.setBounds(0, 0, 273, 150);
		this.add(modifBck);
		
		// Initialisation via DB:
		
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs = statement.executeQuery("SELECT * FROM window WHERE id=" + id);
			while (rs.next()) {
				int colorI, tailleI;
				if(rs.getInt("x")==390) tailleI=0;
				else if(rs.getInt("x")==585) tailleI=1;
				else tailleI=2;
				if(rs.getInt("color")==0) colorI=0;
				else if(rs.getInt("color")==10) colorI=1;
				else colorI=2;
				if (rs.getString("isDetected").equals("on")){
					off = false;
					onoff.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/on.png")));
				}
				else{
					off = true;
					onoff.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/off.png")));
				}
				nom.setText(rs.getString("name"));
				coleur.setSelectedIndex(colorI);
				taille.setSelectedIndex(tailleI);
			}
		} catch (SQLException s) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(s.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == quiter || e.getSource() == annuler) {
			hide();
		}
		
		if (e.getSource() == aide) {
			Message aide=new Message(2, "");
			aide.setVisible(true);
		}
		
		if (e.getSource() == sauvegarder) {
			if(nom.getText().isEmpty()){
				Message msg = new Message(2, "Champ non rempli !!");
				msg.setVisible(true);
			}
			else{
				// Modification des paramètre à la base de données:
				String nameS=nom.getText().toString();
				int x, y, colorInt;
				if(taille.getSelectedIndex()==0){
					x=390; y=250;
				}
				else if(taille.getSelectedIndex()==1){
					x=585; y=375;
				}
				else{
					x=780; y=500;
				}
				if(coleur.getSelectedIndex()==0) colorInt=0;
				else if(coleur.getSelectedIndex()==1) colorInt=10;
				else colorInt=72;
				String detectS;
				if(off) detectS="off";
				else detectS="on";
					
				Statement st=null;
				String req="UPDATE window SET  name='"+nameS
												+"', x='"+x
												+"', y='"+y
												+"', color='"+colorInt
												+"', isDetected='"+detectS
												+"', isFull='on' WHERE id="+this.id;
				try {
					// Modification des paramètres de détection:
				    st=Menu.connection.createStatement();
					st.setQueryTimeout(30); // set timeout to 30 sec.
					st.executeUpdate(req);
					Message msg = new Message(2, "Modifiée avec Succès !");
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
		
		if (e.getSource() == onoff) {
			if(off){
				onoff.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/on.png")));
				off=false;
			}
			else{
				onoff.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/off.png")));
				off=true;
			}
		}
	}
	public void mouseEntered(MouseEvent e) {
		annulerGif.getImage().flush();
		sauvegarderGif.getImage().flush();
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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		this.start_drag = this.getScreenLocation(e);
		this.start_loc = this.getLocation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Point current =this.getScreenLocation(e) ;
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

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
