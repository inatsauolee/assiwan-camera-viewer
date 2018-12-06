package pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Window extends JLabel implements Runnable, ActionListener {

	private static final long serialVersionUID = 1L;
	private boolean isfull, isDetected, isClicked;
	private int N = 0, colorCode, x, y;
	public int id;
	private String name;
	private Thread t;
	private Mat outerBox, frame = new Mat();
	private VideoCapture cam;
	private Rect rect_Crop=null;
	private JButton reessayer=new JButton();
	private boolean isStarted=false;
	static boolean detect=true;

	public Window(int id, String name, int num, int color, int x, int y, boolean i, boolean j, boolean k) {
		setSize(340, 290);
		setVisible(true);
		this.id = id;
		this.name = name;
		this.N = num;
		this.colorCode = color;
		this.x = x;
		this.y = y;
		this.isDetected = i;
		this.isfull = j;
		this.isClicked = k;
		this.cam = new VideoCapture(this.N);
	}

	public int getId(){
		return this.id;
	}
	public boolean isfull(){
		return this.isfull;
	}

	public void setfull(boolean x){
		this.isfull=x;
	}
	
	public boolean isDetected() {
		return this.isDetected;
	}

	public void setDetected(boolean y) {
		this.isDetected=y;
	}
	
	public boolean isClicked() {
		return this.isClicked;
	}
	public void setClicked(boolean z) {
		this.isClicked=z;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getN() {
		return this.N;
	}

	public void setN(int number) {
		this.N = number;
		this.cam = new VideoCapture(this.N);
	}

	public int getColor() {
		return this.colorCode;
	}

	public void setColor(int number) {
		this.colorCode = number;
	}

	public int getTailleX() {
		return this.x;
	}

	public int getTailleY() {
		return this.y;
	}

	public void setTaille(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void run() {
		outerBox = new Mat();
		frame = new Mat();
		System.out.println("ready " + name);
		try {
			while (true) {
					System.out.println("Running " + name);
					if (cam.read(frame) && this.isfull()) {
						Mat resizeimage = new Mat();
						Size sz = new Size(x, y);
						outerBox = new Mat(frame.size(), CvType.CV_8UC1);
						try{
							Imgproc.cvtColor(frame, outerBox, colorCode);
							Imgproc.GaussianBlur(outerBox, outerBox, new Size(3, 3), 0);
							Imgproc.resize(outerBox, resizeimage, sz);
							if(isDetected && Menu.generalDetection) faceDetect(resizeimage);
							ImageIcon image = new ImageIcon(
									Mat2bufferedImage(resizeimage));
							this.setIcon(image);
							this.repaint();
							reessayer.setVisible(false);
						} catch (Exception e) {
							error();
							break;
						}
					}
				// Let the thread sleep for a while.
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			System.err.println("Thread " + name + " interrupted.");
			error();
		}
		System.out.println("Thread " + name + " exiting.");
	}

	private void error() {
		// TODO Auto-generated method stub
		ImageIcon img = new ImageIcon(Menu.class.getResource("../images/"+ Menu.theme+"/error.png"));
		this.setIcon(img);
		reessayer=new  JButton();
		reessayer.setBounds(150, 160, 90, 70);
		Menu.decoration_boutton(reessayer, "reessayer.png", "reessayerHover.png", "reessayerPressed.png");
		reessayer.addActionListener(this);
		add(reessayer);
	}

	public void start() {
		System.out.println("Starting " + name);
		t = new Thread(this);
		t.start();
		isStarted=true;
	}

	@SuppressWarnings("deprecation")
	public void kill() {
		if(isStarted){
			this.t.stop();
			isStarted=false;
		}
	}

	public void faceDetect(Mat f) {
		CascadeClassifier faceDetectorClassifier = new CascadeClassifier(
				"C:/Users/Inatsauo Lee/workspace/MonProjet/src/Haar Cascade/haarcascade_frontalface_alt.xml");
		MatOfRect faceDetections = new MatOfRect();
		faceDetectorClassifier.detectMultiScale(f, faceDetections); // detectMultiScale will perform the detection
		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(f, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top right
					new Scalar(0, 255, 0)); // RGB colour
			rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);
			if(detect){
				Mat face = new Mat(f,rect_Crop);
				cropFaces(f, "face0");
				try{
					Detection det=new Detection(this.getName(), face);
					det.setVisible(true);
				}catch(Exception e){
					Message msg=new Message(2, "Erreur !!");
					msg.setVisible(true);
				}
			}
		}
		if(faceDetections.toArray().length!=0){
			System.err.println("Détect "+faceDetections.toArray().length+" personnes !!");
			detect=false;
		}
	}
   
	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}

	public void cropFaces(Mat f, String s){
		  Mat face = new Mat(f,rect_Crop);
		  Imgcodecs.imwrite("src/faces/"+s+".jpg",face);
		//return face;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reessayer)
			start();
	}
}