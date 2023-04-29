package eecs1021;

//import firmata libraries and java libraries
import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.io.IOException;
import java.util.Timer;

//main class for pump and initializing board
public class Main {

    //initializes the port that the moisture sensor will be using
    static final int A1 = 15;
    static final byte I2cconnection = 0x3C;//initializes the old screen

    //main metheod
    public static void main(String[] args) throws InterruptedException, IOException{

        //initializes connection of my board to the COM3 port
        var myBoard = new FirmataDevice("COM3");
        myBoard.start(); //starts board
        myBoard.ensureInitializationIsDone(); //checks to see if the board is initialized, if board it not checked if it is initialized then
                                              //the board does not run

        //initializes the board
        var mois = myBoard.getPin(15);
        double moisRead = mois.getValue(); //initializes the moisture readings
        //OLED

        //initializes the OLED screen
        I2CDevice i2cObject = myBoard.getI2CDevice((byte) 0x3C);
        SSD1306 theOledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
        theOledObject.init();

        //initializes the water pump
        var waterPump = myBoard.getPin(2);
        waterPump.setMode(Pin.Mode.OUTPUT);
        var task = new wateringPump(theOledObject,mois,waterPump);
        new Timer().schedule(task, 0, 1000);

        //try and catch checks for any inputs that create errors that prevent the loop from running
        try {
            // If the soil is dry turn on pump
            if (moisRead >= 560){
                waterPump.setValue(1); //water pump is activated


            }
            //water pump is deactivated
            else {
                waterPump.setValue(0);

            }
        } catch (Exception ex) {
            //displays error if pump is not working
            System.out.println("Pump error.");

        }
        //water pump is deactivated
        waterPump.setValue(0);

    }
}