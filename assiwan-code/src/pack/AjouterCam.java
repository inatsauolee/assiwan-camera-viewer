package pack;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;

public class AjouterCam extends JFrame implements ActionListener, MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;
	private boolean off=true;
	private int id;
	
	////Les attributs de la forme:
	private JLayeredPane ajoutPane = new JLayeredPane();
	private JButton quiter, aide, sauvegarder, annuler;
	private JLabel ajoutBck;
	private final ImageIcon sauvegarderGif, annulerGif;
	private JPanel moveSpace=new JPanel();
	
	//Les attributs de corps:
	private JButton onoff=new JButton();
	private JComboBox<String> modele, taille, coleur;
	private JTextField nom=new JTextField();
	private Font pc=new Font("Diavlo Bold", 0, 18);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AjouterCam(int id){
		
		// Traitement de la forme:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(300, 50, 500, 632);
		setLayout(new BorderLayout());
		ajoutPane.setBounds(0, 0, 500, 632);
		add(ajoutPane, BorderLayout.CENTER);
		this.id=id;
		//Traitement de corps:
			
			//** List des modèles:
		 Vector modeleList=new Vector();
		 modeleList.add("Aucune Caméra..");
		 List<Webcam> list = Webcam.getWebcams();
	     for (int i = 0; i < list.size(); i++) {
	          try {
	               Webcam cam = list.get(i);
	               String s=cam.getName();
	               String ch="";
	               for(int h=0; h<s.length()-2; h++){
	            	   ch+=s.charAt(h);
	               }
	               modeleList.add(ch);
	            } catch (Exception e) {
					Message m=new Message(2, "Err à: "+i);
					m.setVisible(true);
	              }
	        }
	     modele=new JComboBox(modeleList);
		 modele.setFont(pc);
		 modele.setBounds(120, 186, 260, 33);					
		 ajoutPane.add(modele);
		 
			//** Taille de sortie:
		taille=new JComboBox();
		taille.setModel(new DefaultComboBoxModel (new String[] { "390 x 250", "585 x 375", "780 x 500"}));
		taille.setFont(pc);
		taille.setBounds(220, 369, 120, 26);					
		ajoutPane.add(taille);
		
			//** List des modes de coleur:
		coleur=new JComboBox();
		coleur.setModel(new DefaultComboBoxModel (new String[] { "RBG", "Gray", "HLS"}));
		coleur.setFont(pc);
		coleur.setBounds(220, 408, 120, 26);					
		ajoutPane.add(coleur);
		
			//** Nom d'affichage:
		nom=new JTextField();
		nom.setBounds(120, 263, 260, 33);
		nom.setFont(pc);
		ajoutPane.add(nom);
		
			//** Activer désactiver le mode de détection:
		onoff = new JButton();
		onoff.setBounds(260, 512, 94, 34);
		Menu.decoration_boutton(onoff, "off.png", "null", "null");
		ajoutPane.add(onoff);
		onoff.addActionListener(this);
		
		// Les Bouttons:

			// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(467, 15, 25, 25);
		Menu.decoration_boutton(quiter, "bigblackExit.png", "bigblackExitHover.png", "bigblackExitPressed.png");
		ajoutPane.add(quiter);
		quiter.addActionListener(this);
		
			// **Boutton aide:
		aide = new JButton();
		aide.setBounds(441, 15, 25, 25);
		Menu.decoration_boutton(aide, "blackAide.png", "blackAideHover.png", "blackAidePressed.png");
		ajoutPane.add(aide);
		aide.addActionListener(this);
		
			// **Boutton Sauvegarder:
		sauvegarder = new JButton();
		sauvegarder.setBounds(140, 561, 112, 70);
		sauvegarder.setVisible(true);
		Menu.decoration_boutton(sauvegarder, "ajoutBtn.png", "null", "sauvegarderPressed.png");
		//Gif image:
		sauvegarderGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/sauvegarderHover.gif")));
		sauvegarder.setRolloverIcon(sauvegarderGif);
		ajoutPane.add(sauvegarder);
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
		ajoutPane.add(annuler);
		//Listeners:
		annuler.addMouseListener(this);
		annuler.addActionListener(this);
		
		// Les Labels:
		
		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(60, 0, 380, 54);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		ajoutPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);
		
		// **L'arrière plan de la fenêtre:
		ajoutBck = new JLabel();
		ajoutBck.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/ajoutBck.png")));
		ajoutBck.setBounds(0, 0, 273, 150);
		this.add(ajoutBck);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == quiter || e.getSource() == annuler) {
			hide();
		}
		
		if (e.getSource() == aide) {
			Message aide=new Message(0, "");
			aide.setVisible(true);
		}
		
		if (e.getSource() == sauvegarder) {
			if(nom.getText().isEmpty()){
				Message msg = new Message(2, "Champ non rempli !!");
				msg.setVisible(true);
			}
			else if(modele.getSelectedItem().equals("Aucune Caméra..")){
				Message msg = new Message(2, "Caméra non sélectionnée!");
				msg.setVisible(true);
			}
			else{
				// Modification des paramètre à la base de données:
				String modeleS=modele.getSelectedItem().toString();
				int port= modele.getSelectedIndex()-1;
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
				String req="UPDATE window SET modele='"+modeleS
												+"', port='"+port
												+"', name='"+nameS
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
					Message msg = new Message(2, "Ajoutée avec Succès !");
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
	public void mouseMoved(MouseEvent arg0) {
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
}
