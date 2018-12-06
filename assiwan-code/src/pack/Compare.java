package pack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

import org.opencv.core.Mat;

public class Compare {
	// We use instances of Color to make things simpler.
	private Color[][] signature;
	// The base size of the images.
	private static final int baseSize = 100;
	private String Name;
	private double tempDist=0;
	
	public Compare(Mat mat) throws IOException{
		
		// Calculate the signature vector for the reference.
		BufferedImage img;
		img=Window.Mat2bufferedImage(mat);
		RenderedImage ref = img;
		signature = calcSignature(ref);
		File folder = new File("D:/personnes");

		System.err.println(folder);
		// List all the image files in that directory.
		File[] others = folder.listFiles();
		System.err.println(others.length);
		double[] distances = new double[others.length];

		System.err.println(distances.length);
		RenderedImage[] rothers = new RenderedImage[others.length];

		for (int o = 0; o < others.length; o++) {
			rothers[o] = ImageIO.read(others[o]);
			distances[o] = calcDistance(rothers[o]);
		}
		File tempF=null;
		// Sort those vectors *together*.
		for (int p1 = 0; p1 < others.length - 1; p1++)
			for (int p2 = p1 + 1; p2 < others.length; p2++) {
				if (distances[p1] < distances[p2]) {
				    tempDist = distances[p1];
					distances[p1] = distances[p2];
					distances[p2] = tempDist;
					RenderedImage tempR = rothers[p1];
					rothers[p1] = rothers[p2];
					rothers[p2] = tempR;
				    tempF = others[p1];
					others[p1] = others[p2];
					others[p2] = tempF;
				}
			}
		String s=tempF.getName();
		Name="";
        for(int h=0; h<s.length()-4; h++){
           Name+=s.charAt(h);
        }
	}
	
	public String isSimillar(){
		if(tempDist>1300){
			return null;
		}
		else return Name;
	}
	
	/*
	 * 148 * This method averages the pixel values around a central point and
	 * return the 149 * average as an instance of Color. The point coordinates
	 * are proportional to 150 * the image. 151
	 */
	private Color averageAround(RenderedImage i, double px, double py) {
		// Get an iterator for the image.
		RandomIter iterator = RandomIterFactory.create(i, null);
		// Get memory for a pixel and for the accumulator.
		double[] pixel = new double[3];
		double[] accum = new double[3];
		// The size of the sampling area.
		int sampleSize = 10;
		int numPixels = 0;
		// Sample the pixels.
		for (double x = px * baseSize - sampleSize; x < px * baseSize
				+ sampleSize; x++) {
			for (double y = py * baseSize - sampleSize; y < py * baseSize
					+ sampleSize; y++) {
				iterator.getPixel((int) x, (int) y, pixel);
				accum[0] += pixel[0];
				accum[1] += pixel[1];
				accum[2] += pixel[2];
				numPixels++;
			}
		}
		// Average the accumulated values.
		accum[0] /= numPixels;
		accum[1] /= numPixels;
		accum[2] /= numPixels;
		return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
	}
	
	/*
	 * 131 * This method calculates and returns signature vectors for the input
	 * image. 132
	 */
	private Color[][] calcSignature(RenderedImage i) {
		// Get memory for the signature.
		Color[][] sig = new Color[5][5];
		// For each of the 25 signature values average the pixels around it.
		// Note that the coordinate of the central pixel is in proportions.
		float[] prop = new float[] { 1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f,
				9f / 10f };
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 5; y++)
				sig[x][y] = averageAround(i, prop[x], prop[y]);
		return sig;
	}
	
	/*
	 * 182 * This method calculates the distance between the signatures of an
	 * image and 183 * the reference one. The signatures for the image passed as
	 * the parameter are 184 * calculated inside the method. 185
	 */
	private double calcDistance(RenderedImage other) {
		// Calculate the signature for that image.
		Color[][] sigOther = calcSignature(other);
		// There are several ways to calculate distances between two vectors,
		// we will calculate the sum of the distances between the RGB values of
		// pixels in the same positions.
		double dist = 0;
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 5; y++) {
				int r1 = signature[x][y].getRed();
				int g1 = signature[x][y].getGreen();
				int b1 = signature[x][y].getBlue();
				int r2 = sigOther[x][y].getRed();
				int g2 = sigOther[x][y].getGreen();
				int b2 = sigOther[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
						* (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		return dist;
	}
/*	public static void main(String [] args) throws IOException{
		Compare e=new Compare(Mat );
		System.err.print(e.isSimillar());
	}*/
}
