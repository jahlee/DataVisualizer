import java.awt.*;
import java.util.List;
import javax.swing.*;


public class DataVisualizer extends JPanel {

    // use depending on year
    // private final static int REG_YEAR = 365;
    private final static int LEAP_YEAR = 366;

    private List<List<Object>> data;
    
    private int width;  // width of the panel
    private int height; // height of the panel
    
    private int xAxisLength;
    private int yAxisLength;
    
    private float heightIntervals;

    private Color backColor;
    private Color data1Color;
    private Color data2Color;
    private Color data3Color;
    
    private Coordinate origin;
    private Coordinate xAxisEnd;
    private Coordinate yAxisEnd;


    public DataVisualizer(Color[] chosenColors)
    {
        // adjusts screen size depending on dimensions of user's screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) (screenSize.getWidth() * 0.9);
        height = (int) (screenSize.getHeight() * 0.9);
        origin = new Coordinate((float) (width * 0.05), (float) (height * 0.9));
        xAxisEnd = new Coordinate((float) (width * 0.95), (float) (height * 0.9));
        yAxisEnd = new Coordinate((float) (width * 0.05), (float) (height * 0.1));

        xAxisLength = Math.abs(xAxisEnd.getX() - origin.getX());
        yAxisLength = Math.abs(yAxisEnd.getY() - origin.getY());

        heightIntervals = 1;   // set default to 1 value per interval

        // set default colors
        if (chosenColors == null) {
            backColor = Color.WHITE;
            data1Color = Color.RED;
            data2Color = Color.blue;
            data3Color = Color.green;
        }
        else {
            backColor = chosenColors[0];
            data1Color = chosenColors[1];
            data2Color = chosenColors[2];
            data3Color = chosenColors[3];
        }
    }

    // since extending JPanel, must override the paintComponent method
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        this.setBackground(backColor);
        if (backColor == Color.BLACK)
            g.setColor(Color.WHITE);    // if background is black, then the axes lines have to be white
                                        // otherwise the axes will be black by default


        // yDistances will be the NUMBER OF PIXELS between each interval
        // this.heightIntervals will be the VALUE OF EACH INTERVAL
        // inputting max between 3 totals to find what the highest y value should be
        float yDistances = setHeightIntervals(
                Math.max(Math.max(Integer.valueOf((String) data.get(6).get(5)),
                Integer.valueOf((String) data.get(6).get(6))),
                Integer.valueOf((String) data.get(6).get(7))));

        // width is constant, so no need to change


        drawAxes(g, yDistances);
        drawLines(g, yDistances);
        drawKey(g);
        drawLeaderboards(g);

    }

    /**
     * Draws the axes
     * @param g graphics from the paintComponent method
     * @param yDistances number of pixels for each interval
     */
    public void drawAxes(Graphics g, float yDistances)
    {
        // create 10 horizontal lines along y-axis
        for (int i = 0; i < 11; i++)
        {
            // horizontal interval lines that run perpendicular to the y-axis will have x coords from originX to xAxisEndX, and y coords will depend on which interval value it holds
            g.drawLine(origin.getX(), (int) (origin.getY() - (yDistances * i)), xAxisEnd.getX(), (int) (origin.getY() - (yDistances * i)));
            g.drawString(String.format("%d", (int) (i*heightIntervals)), origin.getX() - 20, (int) (origin.getY() - (yDistances * i)));
        }

        int days = 0;    // number of days will be used to create accurate x-axis tick marks

        // create 12 vertical ticks along x-axis
        for (int i = 0; i < 12; i++)
        {
            // vertical ticks on x-axis will have y coords from originY - 10 to originY + 10, and x coords will be distributed into the 12 months
            days+=Months.values()[i].getDay();   // days represents the total number of days that have passed in the year

            // replace LEAP_YEAR with REG_YEAR depending on year
            g.drawLine(origin.getX() + ((days * xAxisLength)/LEAP_YEAR),
                    origin.getY() - 10,
                    origin.getX() + ((days * xAxisLength)/LEAP_YEAR),
                    origin.getY() + 10);
            g.drawString(String.format("%s %d", Months.values()[i], Months.values()[i].getDay()),
                    origin.getX() * 3 / 4 + (days * xAxisLength)/LEAP_YEAR,  origin.getY() + 30);
            // 3/4 of originX to compensate for length of _Month_ _day_
        }
    }

    /**
     * Draws the data lines
     * @param g graphics from the paintComponent method
     * @param yDistances number of pixels for each interval
     */
    public void drawLines(Graphics g, float yDistances)
    {
        // y values represent the last saved value of y from the previous iteration
        int y1 = origin.getY();
        int y2 = origin.getY();
        int y3 = origin.getY();

        for (int i = 0; i < data.size(); i++) {
            List row = data.get(i);

            if (!row.get(1).equals("-")) {
                g.setColor(data1Color);
                g.drawLine(origin.getX() + (width - 100) * (i - 1) / LEAP_YEAR, y1,
                        origin.getX() + (width - 100) * i / LEAP_YEAR, (int) (y1 - (yDistances / heightIntervals * Integer.valueOf((String) row.get(1)))));
                y1 -= (yDistances / heightIntervals * Integer.valueOf((String) row.get(1)));
            }
            else
                break;  // no more data to read

            if (!row.get(2).equals("-")) {
                g.setColor(data2Color);
                g.drawLine(origin.getX() + (width - 100) * (i - 1) / LEAP_YEAR, y2,
                        origin.getX() + (width - 100) * i / LEAP_YEAR, (int) (y2 - (yDistances / heightIntervals * Integer.valueOf((String) row.get(2)))));
                y2 -= (yDistances / heightIntervals * Integer.valueOf((String) row.get(2)));
            }

            if (!row.get(3).equals("-")) {
                g.setColor(data3Color);
                g.drawLine(origin.getX() + (width - 100) * (i - 1) / LEAP_YEAR, y3,
                        origin.getX() + (width - 100) * i / LEAP_YEAR, (int) (y3 - (yDistances / heightIntervals * Integer.valueOf((String) row.get(3)))));
                y3 -= (yDistances / heightIntervals * Integer.valueOf((String) row.get(3)));
            }
        }
    }

    /**
     * Draws a key showing which color corresponds to which person
     * @param g graphics from the paintComponent method
     */
    public void drawKey(Graphics g)
    {
        int bottom = yAxisEnd.getY() - 15;
        int vertMiddle = (origin.getX() + xAxisLength) / 4;
        int horizMiddle = (bottom - 10) / 2;
        int leftEdge = (origin.getX() + xAxisLength) / 4 - 150;
        Color outline;  // draws outline of the key box as well as the color boxes

        if (backColor == Color.BLACK)
            outline = Color.WHITE;
        else
            outline = Color.BLACK;
        g.setColor(outline);


        // drawRect: (x,y) is top left corner
        // drawString: (x,y) is bottom left corner
        // fillRect: (x,y) is top left corner

        g.drawRect(leftEdge, 10, 300, bottom);
        // underline string adapted from from: http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Displayunderlinedtext.htm
        g.drawString("Key", vertMiddle - (getFontMetrics(getFont()).stringWidth("Key") / 2), 30);
        g.drawLine(vertMiddle - (getFontMetrics(getFont()).stringWidth("Key") / 2), 32, vertMiddle + (getFontMetrics(getFont()).stringWidth("Key") / 2), 32);

        g.drawString("Dyle", leftEdge + 45, horizMiddle + 30);
        g.drawString("Hyun", leftEdge + 135, horizMiddle + 30);
        g.drawString("Josh", leftEdge + 225, horizMiddle + 30);

        g.setColor(data1Color);
        g.fillRect(leftEdge + 25, horizMiddle + 20, 10, 10);
        g.setColor(outline);
        g.drawRect(leftEdge + 25, horizMiddle + 20, 10, 10);

        g.setColor(data2Color);
        g.fillRect(leftEdge + 115, horizMiddle + 20, 10, 10);
        g.setColor(outline);
        g.drawRect(leftEdge + 115, horizMiddle + 20, 10, 10);

        g.setColor(data3Color);
        g.fillRect(leftEdge + 205, horizMiddle + 20, 10, 10);
        g.setColor(outline);
        g.drawRect(leftEdge + 205, horizMiddle + 20, 10, 10);
    }

    /**
     * Draws a 'leaderboard' showing the total values of each person
     * @param g graphics from the paintComponent method
     */
    public void drawLeaderboards(Graphics g)
    {
        int bottom = yAxisEnd.getY() - 15;
        int vertMiddle = (origin.getX() + xAxisLength) * 3 / 4;
        int horizMiddle = (bottom - 10) / 2;
        int leftEdge = (origin.getX() + xAxisLength) * 3 / 4 - 150;

        if (backColor == Color.BLACK)
            g.setColor(Color.WHITE);
        else
            g.setColor(Color.BLACK);

        g.drawRect(leftEdge, 10, 300, bottom);
        g.drawString("Leaderboards", vertMiddle - (getFontMetrics(getFont()).stringWidth("Leaderboards") / 2), 30);
        g.drawLine(vertMiddle - (getFontMetrics(getFont()).stringWidth("Leaderboards") / 2), 32, vertMiddle + (getFontMetrics(getFont()).stringWidth("Leaderboards") / 2), 32);

        g.setColor(data1Color);
        g.drawString("Dyle: " + data.get(6).get(5), leftEdge + 25, horizMiddle + 30);
        g.setColor(data2Color);
        g.drawString("Hyun: " + data.get(6).get(6), leftEdge + 115, horizMiddle + 30);
        g.setColor(data3Color);
        g.drawString("Josh: " + data.get(6).get(7), leftEdge + 205, horizMiddle + 30);
    }

    /**
     * gets the data retrieved from the google spreadsheet
     * @param data values of the spreadsheet
     */
    public void importData(List<List<Object>> data)
    {
        this.data = data;
    }

    public int getWidth()   { return width; }

    public int getHeight()  { return height; }

    /**
     *
     * @param num the highest total value between the 3 data values
     * @return number of pixels for each interval (split into 10 intervals)
     */
    public float setHeightIntervals(float num)
    {
        num = Math.round((num+5)/10.0) * 10;    // will round max to the next/nearest number divisible by 10 to make intervals nicer
        this.heightIntervals = num / 10;
        return yAxisLength / 10;  //value of each interval
    }
}
