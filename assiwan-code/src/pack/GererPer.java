package pack;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GererPer extends JFrame implements ActionListener,
		MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Point start_drag;
	private Point start_loc;

	// Les attributs de la forme:
	private JLayeredPane gestionPerPane = new JLayeredPane();
	private JButton quiter, aide, annuler;
	private JLabel gestionPerBck;
	private final ImageIcon annulerGif;
	private JPanel moveSpace = new JPanel();

	// Les attributs de corps:
	private JPanel gestionPerPanel;
	private JButton supprimer, ajouter;
	private JList lsm;

	public GererPer() throws IOException{

		// Traitement de la forme:

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(320, 140, 634, 474);
		setLayout(new BorderLayout());
		gestionPerPane.setBounds(0, 0, 634, 474);
		add(gestionPerPane, BorderLayout.CENTER);

		// Traitement de corps:

		// Le Panel:
		gestionPerPanel = new JPanel();
		gestionPerPanel.setBounds(17, 87, 600, 300);
		gestionPerPanel.setOpaque(false);
		gestionPerPanel.setVisible(true);
		gestionPerPane.add(gestionPerPanel, new Integer(0), 0);
		
		// Boutton Supprimer:
		supprimer = new JButton();
		supprimer.setBounds(500, 144, 105, 38);
		supprimer.setVisible(true);
		Menu.decoration_boutton(supprimer, "supprimer.png", "null",
				"supprimerPressed.png");
		gestionPerPane.add(supprimer);
		supprimer.addActionListener(this);
		
		// Boutton Ajouter comme connu:
		ajouter = new JButton();
		ajouter.setBounds(500, 100, 105, 38);
		ajouter.setVisible(true);
		Menu.decoration_boutton(ajouter, "ajouterLite.png", "null", "ajouterLitePressed.png");
		gestionPerPane.add(ajouter);
		ajouter.addActionListener(this);
		
		// Remplir la liste des personnes:
		int a=0;
		ResultSet rs2 = null;
		Statement statement = null;
		try{
			statement = Menu.connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			rs2 = statement.executeQuery("SELECT * FROM personnes");
			while (rs2.next()) {
				a++;
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
			Message msg = new Message(2, "Erreur de la Base !!");
			msg.setVisible(true);
		}
		String path = (Compare.class.getResource("../personnes")).getFile();
        String pp="";
        if(path.contains("%20")){
        pp=path.replace("%20", " ");
        }
		File folder = new File(pp);
        File[] listOfFiles = folder.listFiles();
        DefaultListModel listModel = new DefaultListModel();
        
        int count = 0;
        if(listOfFiles.length!=0){
        	for (int i = 0; i < a; i++)
            {
                System.out.println("check path"+listOfFiles[i]);
                String name = listOfFiles[i].toString();
                
                ImageIcon ii = new ImageIcon(ImageIO.read(listOfFiles[i]));
                listModel.add(count++, ii);
                
            }
        }
        lsm=new JList(listModel);
        lsm.setVisibleRowCount(2);
        lsm.setVisible(true);
        JScrollPane p=new JScrollPane(lsm);
        p.setBounds(50, 160, 400, 201);

        gestionPerPane.add(p);

		// Les Bouttons de la forme:

		// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(593, 25, 25, 25);
		Menu.decoration_boutton(quiter, "quiter.png", "quiterHover.png",
				"quiterPressed.png");
		gestionPerPane.add(quiter);
		quiter.addActionListener(this);

		// **Boutton aide:
		aide = new JButton();
		aide.setBounds(566, 25, 25, 25);
		Menu.decoration_boutton(aide, "aide.png", "aideHover.png",
				"aidePressed.png");
		gestionPerPane.add(aide);
		aide.addActionListener(this);

		// **Boutton Annuler:
		annuler = new JButton();
		annuler.setBounds(417, 403, 112, 70);
		annuler.setVisible(true);
		Menu.decoration_boutton(annuler, "ajoutBtn.png", "null",
				"annulerPressed.png");
		// Gif image:
		annulerGif = new ImageIcon(
				(Menu.class.getResource("../images/"+ Menu.theme+"/annulerHover.gif")));
		annuler.setRolloverIcon(annulerGif);
		gestionPerPane.add(annuler);
		// Listeners:
		annuler.addMouseListener(this);
		annuler.addActionListener(this);

		// Les Labels:

		// **Panel de changement de location de fenêtre:
		moveSpace.setBounds(70, 13, 500, 50);
		moveSpace.setOpaque(false);
		moveSpace.setVisible(true);
		gestionPerPane.add(moveSpace);
		moveSpace.addMouseListener(this);
		moveSpace.addMouseMotionListener(this);

		// **L'arrière plan de la fenêtre:
		gestionPerBck = new JLabel();
		gestionPerBck.setIcon(new ImageIcon(Menu.class
				.getResource("../images/"+ Menu.theme+"/gererBck.png")));
		gestionPerBck.setBounds(0, 0, 273, 150);
		this.add(gestionPerBck);
		
		// Initialisation via la base de données:
		ResultSet rs=null;
		Statement st=null;
		try {
		    st = Menu.connection.createStatement();
			st.setQueryTimeout(30); // set timeout to 30 sec.
			rs = st.executeQuery("SELECT * FROM personnes");
			while (rs.next()) {
				// Initialisation des paramètres de détection:
				
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

		if (e.getSource() == quiter || e.getSource() == annuler) {
			hide();
		}

		if (e.getSource() == aide) {
			Message aide = new Message(0, "");
			aide.setVisible(true);
		}

		if (e.getSource() == supprimer) {
			int s=1;
			Statement statement = null;
			String req = "DELETE FROM personnes WHERE id="+s;
			try {
				statement = Menu.connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.
				statement.executeQuery(req);
				Message msg = new Message(2, "Supprimée aavec succés !");
				msg.setVisible(true);
			} catch (SQLException n) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println(n.getMessage());
				Message msg = new Message(2, "Erreur à la base !!");
				msg.setVisible(true);
			}
			hide();
			try {
				new GererPer().setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (e.getSource() == ajouter) {
			
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
