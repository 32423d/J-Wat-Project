package eecs1021;

//import firmata libraries and java libraries
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.util.TimerTask;

    //class for running the wateringPump method
    public class wateringPump extends TimerTask{
    private final SSD1306 display;
    private final Pin pin;
    private final Pin pump;

    public wateringPump(SSD1306 display, Pin pin, Pin pump){

        this.display = display;
        this.pin = pin;
        this.pump = pump;
    }

    @Override

    public void run(){

        String value = String.valueOf(pin.getValue());
        System.out.println("The moisture is " + value);
        display.getCanvas().setTextsize(1);
        display.getCanvas().drawString(0,0,"The moisture is " + value);

        String status;

        //If statement to display if the pump is on or off on the OLED screen
        if (pump.getValue() == 1)

        {
            status = "ON";

        }
        else

        {
            status = "OFF";

        }
        //displays the string on the OLED screen
        display.getCanvas().drawString(0,15,"Pump is : " + status);
        display.display();
        display.getCanvas().clear();

        //if statement to turn the pump off or on if it reaches a certain threshold for the moisture values
        if (pin.getValue() >= 560) {

            //try and catch checks for any inputs that create errors that prevent the loop from running
            try {
                pump.setValue(1);

            } catch (Exception ex) {
                System.out.println("pump is not working.");

            }
            System.out.println("Pump is ON");

        }
        else{

            //try and catch checks for any inputs that create errors that prevent the loop from running
            try {

                pump.setValue(0);

            }
            catch (Exception ex) {
                System.out.println("pump is not working");
            }
            System.out.println("Pump is OFF");

        }
    }
}