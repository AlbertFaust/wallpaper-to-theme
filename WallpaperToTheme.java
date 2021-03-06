import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.io.IOException;
import java.lang.*;

class WallpaperToTheme {
    public static void main(String[] args){
    
		BufferedImaged bi = new BufferedImaged();
		bi.do_job();
	}
}

class BufferedImaged {
		
		public void do_job(){
			BufferedImage image = null;
			try{
				image = ImageIO.read(new File("test.jpg"));
			} catch (IOException e) {
				System.out.println("error with reading file");
			}
			int w = image.getWidth();
			int h = image.getHeight();
			int i = 0;
		    	int pixels = w*h; //total num of pixels in image
			int[] dataBuffInt = image.getRGB(0,0,w,h,null,0,w);
			
		    RGB[] colours = new RGB[pixels]; //array for RGB of each pixel
		    
		    for (i=0; i<pixels; i++){ //grabbing RGB values from image
				Color c = new Color(dataBuffInt[i]);
				colours[i] = new RGB(c.getRed(),c.getGreen(),c.getBlue());
		    }
		    
		    Kmeans km = new Kmeans(colours, pixels); //creating instance of kmeans
		    km.calcingDistances();
        }
}

class RGB { //class to store RGB values
    private int red;
    private int green;
    private int blue;
    
    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getR() { return red; }
    public int getG() { return green; }
    public int getB() { return blue; }

    public void setRed(int r) { red = r; }
    public void setGreen(int g) { green = g; }
    public void setBlue(int b) { blue = b; }

}

class Kmeans {//class to gather dominant colours
	
	int[] closest;//relates closest centroid to each pixel
	RGB colours[];//colours of each pixel
	int pixels = 0;

	public Kmeans(RGB colours[], int pixels){//overloaded constructor
		this.colours = colours;
		this.pixels = pixels;
		closest = new int[pixels];
		this.colours = colours;
	}

	public void calcingDistances(){
		RGB centroids[] = {new RGB(255,0,0),new RGB(0,255,0),new RGB(0,0,255)};//x2,y2,z2
        int iterations = 0;
		while (iterations != 100){
        RGB total_centroids[] = {new RGB(0,0,0),new RGB(0,0,0),new RGB(0,0,0)};
        int sum[] = new int[3];
        for (int i=0; i<pixels; i++){
			RGB rgb = colours[i];
			closest[i] = calcDist(rgb,centroids);
		}
		for (int i=0; i<pixels; i++){ //just printing rgb values of image
	    	RGB rgb = colours[i];
	        //System.out.println(i + "\t" + rgb.getR() + "\t" + rgb.getG() + "\t" + rgb.getB() + "\t" + closest[i]);
	        for (int j=0; j<3; j++){
                if (closest[i] == j){
                    total_centroids[j].setRed(total_centroids[j].getR()+rgb.getR());
                    total_centroids[j].setGreen(total_centroids[j].getG()+rgb.getG());
                    total_centroids[j].setBlue(total_centroids[j].getB()+rgb.getB());
                    sum[j] = sum[j]+1;
                }
            }
        }
        for (int k=0; k<3; k++){
            centroids[k].setRed(total_centroids[k].getR()/sum[k]);
            centroids[k].setGreen(total_centroids[k].getG()/sum[k]);
            centroids[k].setBlue(total_centroids[k].getB()/sum[k]);
            //System.out.println("Sum of Centroid "+k+" = "+sum[k]);
            //System.out.println("Total value of Centroid "+k+" = "+total_centroids[k].getR()+" "+total_centroids[k].getG()+" "+total_centroids[k].getB());
            System.out.println("New Centroid "+k+" "+centroids[k].getR()+" "+centroids[k].getG()+" "+centroids[k].getB());
        }
        iterations = iterations+1;
	}
    }
	
	public static int calcDist(RGB rgb,RGB centroids[]){
		double distance = 100000;//max distance from a centroid to an RGB val of a pixel
		double newDist = 0; //temp value for ordering clusters
		
		int returnVal = 4;//error testing
		int x1=rgb.getR();
		int y1=rgb.getG();
		int z1=rgb.getB();
		for(int i = 0; i < 3; i++){
			int x2 = centroids[i].getR();
			int y2 = centroids[i].getG();
			int z2 = centroids[i].getB();
			newDist = Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1))+((z2-z1)*(z2-z1)));
			if(newDist < distance){
				distance = newDist;
				returnVal = i;
			}
		}
		return returnVal;
	}
}
