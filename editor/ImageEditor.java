import java.util.Scanner;

/**
 * Created by logan on 9/8/2017.
 */

public class ImageEditor {

    public static void main(String[] args){
        //System.out.println("Hello, World");
        if(args.length < 3){
            System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
            return;
        }
        String inputFile = args[0];
        String outputFile = args[1];
        String command = args[2];
        int blur = 0;
        if(command.equals("motionblur")){
            if(args.length != 4){
                System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                return;
            }
            blur = Integer.parseInt(args[3]);
            if(blur <= 0){
                System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                return; 
            }
        }

        // System.out.println(inputFile + " " + outputFile + " " + command);

        image edit = new image(inputFile);
        switch(command) {
            case "invert":
                if(args.length != 3){
                    System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                    return; 
                }
                // System.out.println("INVERT");
                edit.invert();
                break;
            case "grayscale":
                if(args.length != 3){
                    System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                    return; 
                }
                // System.out.println("GRAYSCALE");
                edit.greyscale();
                break;
            case "emboss":
                if(args.length != 3){
                    System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                    return; 
                }
                // System.out.println("EMBOSS");
                edit.emboss();
                break;
            case "motionblur":
                // System.out.println("BLUR");
                edit.motionblur(blur);
                break;
            default:
                System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
                return;
        }

        edit.store(outputFile);
    }

}