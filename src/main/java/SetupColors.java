import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Arrays;

public class SetupColors
{
    // Colors
    Color backgroundColor;
    Color data1Color;
    Color data2Color;
    Color data3Color;

    // Names of colors in string format
    String backgroundName;
    String data1Name;
    String data2Name;
    String data3Name;

    // Title of each section/necessary value
    JLabel backgroundLabel;
    JLabel data1Label;
    JLabel data2Label;
    JLabel data3Label;

    // Select white or black for background
    ButtonGroup backgroundSelection;
    JRadioButton white;
    JRadioButton black;

    // Map of basic colors in string format to its value in color
    HashMap<String, Color> basicColors;

    // Combobox used to retrieve inputted color of data values
    JComboBox data1Colors;
    JComboBox data2Colors;
    JComboBox data3Colors;

    JFrame frame;
    JButton submit;

    // Different panels for each section:
    JPanel backColorPanel;  // background
    JPanel lineColorPanel;  // line colors
    JPanel submitPanel;     // submit button

    // Confirmation pane to make sure input is correct
    JOptionPane confirm;

    /**
     * Constructor that will run the SetupColors app when called
     */
    public SetupColors()
    {
        frame = new JFrame();
        // Since using panels, don't need to set frame size!

        backColorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backColorPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));

        lineColorPanel = new JPanel(new GridLayout(0,2));
        lineColorPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        submitPanel = new JPanel();

        frame.add(backColorPanel, BorderLayout.NORTH);
        frame.add(lineColorPanel, BorderLayout.CENTER);
        frame.add(submitPanel, BorderLayout.SOUTH);

        frame.pack();


        backgroundLabel = new JLabel("Color of Background:");
        data1Label = new JLabel("Color for Dyle:");
        data2Label = new JLabel("Color for Hyun:");
        data3Label = new JLabel("Color for Josh:");


        white = new JRadioButton("white");
        black = new JRadioButton("black");

        backgroundSelection = new ButtonGroup();
        backgroundSelection.add(white);
        backgroundSelection.add(black);
        white.setSelected(true);    // default to white

        backColorPanel.add(backgroundLabel);
        backColorPanel.add(white);
        backColorPanel.add(black);


        basicColors = new HashMap<>();
        basicColors.put("red", Color.RED);
        basicColors.put("orange", Color.ORANGE);
        basicColors.put("yellow", Color.YELLOW);
        basicColors.put("green", Color.GREEN);
        basicColors.put("blue", Color.BLUE);
        basicColors.put("magenta", Color.MAGENTA);
        basicColors.put("cyan", Color.CYAN);
        basicColors.put("purple", new Color(102, 0, 153));
        basicColors.put("white", Color.WHITE);
        basicColors.put("black", Color.BLACK);
        basicColors.put("random", new Color(150, 150, 150));    // temp random color

        String allC[] = new String[11]; // need to create an array of the colors to input into combo box
        basicColors.keySet().toArray(allC);
        Arrays.sort(allC);  // keep the order of colors consistent each execution

        data1Colors = new JComboBox(allC);
        data2Colors = new JComboBox(allC);
        data3Colors = new JComboBox(allC);


        lineColorPanel.add(data1Label);
        lineColorPanel.add(data1Colors);

        lineColorPanel.add(data2Label);
        lineColorPanel.add(data2Colors);

        lineColorPanel.add(data3Label);
        lineColorPanel.add(data3Colors);

        submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitButtonPressed();
            }
        } );

        /*
        PURELY FOR ADDITIONAL INFORMATION/FORMATTING
        -------------------------------------------------------------------------------
        currently, the actionListener above is in anonymous class form
        however, using "lambda" would look like:

        submit.addActionListener(e -> {
            try {
                submitButtonPressed();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        });

        basically, lambda replaces the entire new class declaration + method header
        --> lambda only works if you are touching one method
        */

        submitPanel.add(submit);

        // to confirm that the selected colors is correct
        confirm = new JOptionPane();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Setup colors");
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * ActionListener will come here when button is clicked
     * Will set colors of background and 3 data values, and will exit if the user confirms the data is correct
     */
    public void submitButtonPressed(){
        if (white.isSelected())
        {
            backgroundName = "white";
        }
        else
        {
            backgroundName = "black";
        }

        backgroundColor = basicColors.get(backgroundName);


        data1Name = (String) data1Colors.getSelectedItem();
        if (data1Name == "random")
            data1Color = randomColor();
        else
            data1Color = basicColors.get(data1Name);

        data2Name = (String) data2Colors.getSelectedItem();
        if (data2Name == "random")
            data2Color = randomColor();
        else
            data2Color = basicColors.get(data2Name);

        data3Name = (String) data3Colors.getSelectedItem();
        if (data3Name == "random")
            data3Color = randomColor();
        else
            data3Color = basicColors.get(data3Name);


        // confirmation
        Object[] options = {"Yes, continue",
                "No, go back"};

        int n = JOptionPane.showOptionDialog(frame,
                "The background will be: " + backgroundName +
                        "\nData 1 will be: " + data1Name +
                        "\nData 2 will be: " + data2Name +
                        "\nData 3 will be: " + data3Name +
                        "\nIs this ok?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,      //do not use a custom Icon
                options,        //the titles of buttons
                options[0]);    //default button title

        if (n!=0)   // if choose to go back
            return;
        frame.dispose();
    }

    /**
     * Gets the colors that are chosen
     * @return new array of colors that includes values of colors chosen
     */
    public Color[] getColors()
    {
        return new Color[] {backgroundColor, data1Color, data2Color, data3Color};
    }

    /**
     * Used to get a random color for a data line
     * @return random color generated
     */
    public Color randomColor()
    {
        Random rand = new Random();
        float r, g, b;
        Color randomColor;

        // both color statements taken from https://stackoverflow.com/questions/4246351/creating-random-colour-in-java
        // will return bright colors if background is black
        if (backgroundColor == Color.BLACK) {
            r = (float) (rand.nextFloat() / 2f + 0.5);
            g = (float) (rand.nextFloat() / 2f + 0.5);
            b = (float) (rand.nextFloat() / 2f + 0.5);
            randomColor = new Color(r, g, b);
        }
        // else will return rainbow pastel color
        else
        {
            float hue = rand.nextFloat();
            float saturation = 0.9f;  //1.0 for brilliant, 0.0 for dull
            float luminance = 1.0f;   //1.0 for brighter, 0.0 for black
            randomColor = Color.getHSBColor(hue, saturation, luminance);
        }
        return randomColor;
    }

    /**
     * used in main to make sure that a color is chosen before proceeding into the program
     * @return true if the frame is not closed (confirmation is not clicked as yes)
     */
    public boolean isRunning()
    {
        return frame.isVisible() == true;
    }


    public static void main(String[] args)
    {
        new SetupColors();
    }
}
