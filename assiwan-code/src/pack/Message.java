package pack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Message extends JDialog implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	private JLayeredPane MessagePane = new JLayeredPane();
	private int id;
	private JButton quiter, oui, non, ok;
	private JLabel message, titre, MessageBck;
	private final ImageIcon  okGif, ouiGif, nonGif;
	
	public Message(int id, String text){
		// Traitement de la forme:
		setUndecorated(true);
		setLocationRelativeTo(null);
		setBounds(500, 160, 273, 150);
		setLayout(new BorderLayout());
		MessagePane.setBounds(0, 0, 273, 150);
		add(MessagePane, BorderLayout.CENTER);
		
		// Les Bouttons:

			// **Boutton quiter:
		quiter = new JButton();
		quiter.setBounds(247, 9, 20, 20);
		Menu.decoration_boutton(quiter, "blackExit.png", "blackExitHover.png", "blackExitPressed.png");
		MessagePane.add(quiter);
		quiter.addActionListener(this);
		
			// **Boutton Oui:
		oui = new JButton();
		oui.setBounds(64, 94, 75, 55);
		oui.setVisible(false);
		Menu.decoration_boutton(oui, "msgBoutton.png", "null", "ouiBouttonPressed.png");
		//Gif image:
		ouiGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/ouiBouttonHover.gif")));
        oui.setRolloverIcon(ouiGif);
		MessagePane.add(oui);
		//Listeners:
		oui.addMouseListener(this);
		oui.addActionListener(this);
		
			// **Boutton Non:
		non = new JButton();
		non.setBounds(155, 94, 75, 55);
		non.setVisible(false);
		Menu.decoration_boutton(non, "msgBoutton.png", "null", "nonBouttonPressed.png");
		//Gif image:
		nonGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/nonBouttonHover.gif")));
        non.setRolloverIcon(nonGif);
		MessagePane.add(non);
		//Listeners:
		non.addMouseListener(this);
		non.addActionListener(this);
		
			// **Boutton OK:
		ok = new JButton();
		ok.setBounds(99, 94, 75, 55);
		ok.setVisible(false);
		Menu.decoration_boutton(ok, "msgBoutton.png", "null", "okBouttonPressed.png");
		//Gif image:
		okGif = new ImageIcon((Menu.class.getResource("../images/"+ Menu.theme+"/okBouttonHover.gif")));
        ok.setRolloverIcon(okGif);
		MessagePane.add(ok);
		//Listeners:
		ok.addMouseListener(this);
		ok.addActionListener(this);
		
		// Les Labels:Diavlo_BOLD_II
		
		// **les Message:
		message = new JLabel(text);
		message.setFont(new Font("Diavlo Bold", 0, 18));
		message.setBounds(16 + (1/2), 50, 240, 80);
		message.setForeground(Color.WHITE);
		this.add(message);
		
		// **Le titre de message:
		titre = new JLabel();
		titre.setBounds(0, 0, 170, 36);
		this.add(titre);

		// **L'arrière plan de message:
		MessageBck = new JLabel();
		MessageBck.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/MessageBck.png")));
		MessageBck.setBounds(0, 0, 273, 150);
		this.add(MessageBck);
		
		// Initialisation:
		this.id=id;
		/*
		 * pour l'id:
		 * 0: message d'aide.
		 * 1: message de vérification.
		 * 2: message d'alerte.
		 */
		switch(this.id){
		case 0: ok.setVisible(true);
				titre.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/aidetitle.png")));
				message.setBounds(0, 40, 270, 90);
				message.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/aideMsg.png")));
				break;
		case 1: oui.setVisible(true); non.setVisible(true);
				titre.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/verifier.png")));
				break;
		case 2: ok.setVisible(true); 
				titre.setIcon(new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/alerte.png")));
				break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == quiter || e.getSource() == non || e.getSource() == ok) {
			hide();
		}
		
		if (e.getSource() == oui) {
			Menu.enlever();
			hide();
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		ouiGif.getImage().flush();
		nonGif.getImage().flush();
		okGif.getImage().flush();
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
