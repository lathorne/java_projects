import java.util.Scanner;
import java.io.*;
import java.lang.*;

/**
 * Created by logan on 9/8/2017.
 */

public class image {

    pixel[][] pixelArray;
    int width;
    int height;

    image(String filename){ //constructor
        // System.out.println(filename);
        load(filename);
    }

    public void load(String filename) {
    try{
        Scanner sc = new Scanner(new File(filename));
        sc.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
        String input = sc.next(); 
        width = sc.nextInt();
        height = sc.nextInt();
        //sc.skip("#*"");?
        pixelArray = new pixel[height][width];
        int maxcolor = sc.nextInt();
        for(int i = 0; i < height; i++){ //goes down rows
            for(int j = 0; j < width; j++){ //goes along columns
                int red = sc.nextInt();
                // if(j<5 && i == 0)
                //     System.out.println("RED: " + red);
                int green = sc.nextInt();  
                // if(j<5 && i == 0)            
                //     System.out.println("GREEN: " + green);
                int blue = sc.nextInt();
                // if(j<5 && i == 0){    
                //     System.out.println("BLUE: " + blue);
                //     System.out.println(red + " " + green + " " + blue);
                // }
                pixel newPixel = new pixel(red, green, blue);
                pixelArray[i][j] = newPixel;
            }
        }
        sc.close();
    //now time to get r,g,b values and place them in array
    }
    catch(FileNotFoundException e){
        System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
    } //file should be loaded here
    //needs to go through ppm file
    // System.out.println(pixelArray[0][0].getRed());
    // System.out.println(pixelArray[0][0].getGreen());
    // System.out.println(pixelArray[0][0].getBlue());    


    }

    public void store(String outputName){
    
        // System.out.println(System.getProperty("user.dir") + "/" + outputName);
        try{
            FileWriter fw = new FileWriter(outputName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("P3" + "\n"); //this isn't writing
            bw.write(width + " " + height + "\n");
            bw.write("255" + "\n");
            // System.out.println(pixelArray[0][0].getRed());
            // System.out.println(pixelArray[0][0].getGreen());
            // System.out.println(pixelArray[0][0].getBlue());    

            // bw.write(Integer.toString(pixelArray[0][0].getRed()) + "\n");
            // bw.write(Integer.toString(pixelArray[0][0].getGreen()) + "\n");
            // bw.write(Integer.toString(pixelArray[0][0].getBlue()) + "\n");
            for(int i = 0; i < height; i++){ //goes down rows
                for(int j = 0; j < width; j++){ //goes along columns
                    bw.write(Integer.toString(pixelArray[i][j].getRed()) + "\n");
                    bw.write(Integer.toString(pixelArray[i][j].getGreen()) + "\n");
                    bw.write(Integer.toString(pixelArray[i][j].getBlue()) + "\n");
                }
            }
            bw.close();
        }
        catch(IOException e){

        }

    }

    public void invert(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                pixelArray[i][j].setRed(255 - pixelArray[i][j].getRed()); //this isn't working
                pixelArray[i][j].setGreen(255 - pixelArray[i][j].getGreen());
                pixelArray[i][j].setBlue(255 - pixelArray[i][j].getBlue());

            }
        }
    }


    public void greyscale(){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int average = (pixelArray[i][j].getRed()+pixelArray[i][j].getGreen()+pixelArray[i][j].getBlue())/3;
                pixelArray[i][j].setRed(average);
                pixelArray[i][j].setGreen(average);
                pixelArray[i][j].setBlue(average);
            }
        }
    }

    public void emboss(){
       
        for(int i = height - 1; i > 0; i--){
            for(int j = width - 1; j > 0; j--){
                int redDiff = pixelArray[i][j].getRed() - pixelArray[i-1][j-1].getRed();
                int greenDiff = pixelArray[i][j].getGreen() - pixelArray[i-1][j-1].getGreen();
                int blueDiff = pixelArray[i][j].getBlue() - pixelArray[i-1][j-1].getBlue();  
                
                int absRed = Math.abs(redDiff);
                int absGreen = Math.abs(greenDiff);
                int absBlue = Math.abs(blueDiff);

                int maxDiff = Math.max(Math.max(absRed,absGreen),absBlue);
                int max;

                if(maxDiff == absRed){ //prioritize the difference in order of R, G, B
                    max = redDiff + 128;
                }else if(maxDiff == absGreen){
                    max = greenDiff + 128;
                }else{
                    max = blueDiff + 128;
                }

                if(max > 255){ //readjust to keep in bounds of max and min pixel values
                    max = 255;
                }
                if(max < 0){
                    max = 0;
                }
                pixelArray[i][j].setRed(max);
                pixelArray[i][j].setGreen(max);
                pixelArray[i][j].setBlue(max);

            }
        }
        for(int i = 0; i < height; i++){ //for c-1 < 0
            pixelArray[i][0].setRed(128);
            pixelArray[i][0].setGreen(128);
            pixelArray[i][0].setBlue(128);
        }
        for(int j = 0; j < width; j++){ // for r-1 < 0
            pixelArray[0][j].setRed(128);
            pixelArray[0][j].setGreen(128);
            pixelArray[0][j].setBlue(128);
        }
    }


    public void motionblur(int blurvalue){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int divisor = 0; 
                int toAvg = 0;
                //average red value
                for(int k = 0; k < blurvalue; k++){
                    if(j+k < width){
                        divisor++;
                        toAvg += pixelArray[i][j+k].getRed();
                    }
                }
                pixelArray[i][j].setRed(toAvg/divisor);
                //average green value
                divisor = 0;
                toAvg = 0;
                for(int k = 0; k < blurvalue; k++){
                    if(j+k < width){
                        divisor++;
                        toAvg += pixelArray[i][j+k].getGreen();
                    }
                }
                pixelArray[i][j].setGreen(toAvg/divisor);                
                //average blue value
                divisor = 0;
                toAvg = 0;
                for(int k = 0; k < blurvalue; k++){
                    if(j+k < width){
                        divisor++;
                        toAvg += pixelArray[i][j+k].getBlue();
                    }
                }
                pixelArray[i][j].setBlue(toAvg/divisor);  

            }
        }       
    }

}
