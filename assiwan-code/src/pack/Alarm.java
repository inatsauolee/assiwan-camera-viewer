package pack;

import java.net.URL;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.format.AudioFormat;
import javax.media.pim.PlugInManager;

public class Alarm {
	private String Sound;
	private Player player;
	public Alarm(String s){
		this.Sound=s;
	}
	
	public void play() {
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		PlugInManager.addPlugIn(
			"com.sun.media.codec.audio.mp3.JavaDecoder",
			new Format[]{input1, input2},
			new Format[]{output},
			PlugInManager.CODEC
		);
		try{
			URL url=Menu.class.getResource("../alarme/"+this.Sound+".mp3");
		    player = Manager.createPlayer(new MediaLocator(url));
			player.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
			Message e=new Message(2, "Erreur de lire l'alarme !");
			e.setVisible(true);
		}
	}
	
	public void stop(){
		try{
			player.stop();
		}
		catch(Exception ex){
			ex.printStackTrace();
			Message e=new Message(2, "Erreur de lire l'alarme !");
			e.setVisible(true);
		}
	}
	
	public void setSound(String s){
		this.Sound=s;
	}
}