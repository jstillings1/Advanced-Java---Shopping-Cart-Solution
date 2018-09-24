/**
* This program demonstrates a solution to the
* Shopping Cart System programming challenge.
* Date 3-20-2018
* CSC 251  - The Shopping Cart System
* @author Jeremiah Stillings
*/

//Import swing components going to be used
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


//Import awt components needed.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


// Import file handling stuff
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.text.DecimalFormat;

// Import our list and array stuff
import java.util.ArrayList;
import java.util.List;


public class ShoppingCart_JStillings 
{

   
    //declare our containers
    private JFrame app; 
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel americaPanel;
    
    //declare our container size
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    //declare our button components
    private JButton add;
    private JButton delete;
    private JButton checkout;
    //declare our label components
    private JLabel leftLabel;
    private JLabel rightLabel;
    private JLabel leftSpacer;
    private JLabel rightSpacer;
    private JLabel spacer1;
    private JLabel spacer2;
    private JLabel centerLabel ;
    private JLabel subtotalLabel;
    private JLabel totalLabel;
    private JLabel taxLabel;
    //declare our fields for live update display of subtotal, tax and total
    private JTextField subtotalDisplay;
    private JTextField taxDisplay;
    private JTextField totalDisplay;
    
    //declare our JLists
    private JList leftList;
    private JList rightList;
    // declare Abtract lists
    List<String> title;
    List<String> price;
    List<String> cart;
    
    // declare scroll panes
    JScrollPane leftScroll;
    JScrollPane rightScroll;
    
    // file chooser
    private JFileChooser fileChooser;
    
    //counters
    private double subtotal,tax, total = 0;
    
    // Listmodel for on the fly updates to JList
     private DefaultListModel listModel;
    /**
     * default constructor
     */
    public ShoppingCart_JStillings() throws IOException
    {
        app = new JFrame("Book Store Shopping Cart");
        // that thing that exits swing clean
        app.addWindowListener(new WindowAdapter() 
        {
            public void windowClosing(WindowEvent windowEvent)
            {
                System.exit(0);
            }        
        });    
        app.setPreferredSize(new Dimension(1024,768));
        app.setLocationRelativeTo(null);
        app.setLayout(new BorderLayout());
        title = new ArrayList<String>();
        price = new ArrayList<String>();
        cart = new ArrayList<String>(5);
        listModel = new DefaultListModel();
        app.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //create panels
        createNorth();
        createWest();
        createEast();
        createSouth();
        createAmerica();
        //add panels to the contentpane of our JFrame;
        app.add(northPanel,BorderLayout.NORTH);
        app.add(westPanel,BorderLayout.WEST);
        app.add(americaPanel,BorderLayout.CENTER);
        app.add(eastPanel,BorderLayout.EAST);
        app.add(southPanel,BorderLayout.SOUTH);
        // show it off        
        app.pack();
        app.setVisible(true);
        // center the jframe
        centerWindow(app);
        
    }
    
    public static void main(String[] args) throws IOException 
    {
        new ShoppingCart_JStillings();
    }
    
    /** shameless copy paste of jFrame Center Code*/
    public static void centerWindow(Window frame) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
    frame.setLocation(x, y);
}

    /**
     * create north panel
     */
    private void createNorth() 
    {
        northPanel = new JPanel();
        centerLabel = new JLabel("Welcome to the Book Store");
        centerLabel.setFont(new Font("Garamond", Font.BOLD, 36));
        centerLabel.setForeground(Color.GREEN);
        centerLabel.setBorder(BorderFactory.createEmptyBorder(100,0,0,0));
        
        northPanel.add(centerLabel);
    }

    /**
     * create west panel
     * @throws IOException 
     */
    private void createWest() throws IOException 
    {
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayout(3, 1));
        westPanel.setPreferredSize(new Dimension(300,300));
        westPanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
        leftLabel = new JLabel("Available Books");
        leftLabel.setFont(new Font("Garamond", Font.BOLD, 16));
        leftLabel.setForeground(Color.RED);
        leftLabel.setBorder(BorderFactory.createEmptyBorder(100,50,0,50));
        leftSpacer = new JLabel("");
        leftSpacer.setFont(new Font("Garamond", Font.BOLD, 16));
        
        //the magic call- reads text file into a list and casts the list into
        //an array to display in the JList.
        readFile();
        leftScroll = new JScrollPane();
        leftList = new JList(getTitle());
        leftScroll.setViewportView(leftList);
        westPanel.add(leftLabel);
        westPanel.add(leftScroll);
        //needed to control the padding
        westPanel.add(leftSpacer);
        ListSelectionListener listSelectionListener = new ListSelectionListener() 
        {
            public void valueChanged(ListSelectionEvent listSelectionEvent) 
            {
                add.setEnabled(true);
                delete.setEnabled(true);
            }
         };
        leftList.addListSelectionListener(listSelectionListener);
            
        
    }

    /**
     * create East Panel
     */
    private void createEast() 
    {
        eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(3, 1));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
        eastPanel.setPreferredSize(new Dimension(300,300));
        rightLabel = new JLabel("Shopping Cart");
        rightLabel.setFont(new Font("Garamond", Font.BOLD, 16));
        rightLabel.setForeground(Color.RED);
        rightLabel.setBorder(BorderFactory.createEmptyBorder(100,50,0,50));
        rightSpacer = new JLabel("");
        rightSpacer.setFont(new Font("Garamond", Font.BOLD, 16));
        rightScroll = new JScrollPane();
        rightList = new JList(listModel);
        rightScroll.setViewportView(rightList);
        eastPanel.add(rightLabel);
        eastPanel.add(rightScroll);
        eastPanel.add(rightSpacer);
    }

    
    /**
     * create south panel
     */
    private void createSouth() 
    {
        southPanel = new JPanel();
    
        subtotalLabel = new JLabel("SubTotal");
        totalLabel = new JLabel("Total");
        taxLabel = new JLabel("Tax");
        subtotalLabel.setFont(new Font("Garamond", Font.BOLD, 36));
        subtotalLabel.setForeground(Color.GREEN);
        totalLabel.setFont(new Font("Garamond", Font.BOLD, 36));
        totalLabel.setForeground(Color.GREEN);
        taxLabel.setFont(new Font("Garamond", Font.BOLD, 36));
        taxLabel.setForeground(Color.GREEN);
        
        subtotalDisplay = new JTextField(5);
        taxDisplay = new JTextField(5);
        totalDisplay = new JTextField(5);
        subtotalDisplay.setFont(new Font("Garamond", Font.BOLD, 36));
        taxDisplay.setFont(new Font("Garamond", Font.BOLD, 36));
        totalDisplay.setFont(new Font("Garamond", Font.BOLD, 36));
        
        subtotalDisplay.setText(String.valueOf(subtotal));
        taxDisplay.setText(String.valueOf(tax));
        totalDisplay.setText(String.valueOf(total));
        southPanel.add(subtotalLabel);
        southPanel.add(subtotalDisplay);
        southPanel.add(taxLabel);
        southPanel.add(taxDisplay);
        southPanel.add(totalLabel);
        southPanel.add(totalDisplay);
        
    }

    /**
     * create center panel
     */
    private void createAmerica() 
    {
       americaPanel = new JPanel();
       americaPanel.setLayout(new GridLayout(5, 1));
       add = new JButton("Add Selection to Cart");
       delete = new JButton("Delete Selection from Cart");
       add.setEnabled(false);
       delete.setEnabled(false);
       checkout = new JButton("Check Out");
       add.setFont(new Font("Garamond", Font.BOLD, 24));
       delete.setFont(new Font("Garamond", Font.BOLD, 24));
       checkout.setFont(new Font("Garamond", Font.BOLD, 24));
       americaPanel.setBorder(BorderFactory.createEmptyBorder(200,50,200,50));
       spacer1 = new JLabel("");
       spacer1.setFont(new Font("Garamond", Font.BOLD, 24));
       spacer2 = new JLabel("");
       spacer2.setFont(new Font("Garamond", Font.BOLD, 24));
      
       //Register an action listener with the buttons
       add.addActionListener(new addButtonListener());
       delete.addActionListener(new deleteButtonListener());
       checkout.addActionListener(new checkoutButtonListener());
       //add buttons to the panel
       americaPanel.add(add);
       americaPanel.add(spacer1);
       americaPanel.add(delete);
       americaPanel.add(spacer2);
       americaPanel.add(checkout);
       
       
       
    }

    /**
     * select, read, parse, store the file
     * @throws IOException 
     */
    private void readFile() throws IOException 
    {
        // declare a String to work with
        String line = "";
        // declare our string array to split the file on ,
        String[] lineTitle;
       //Create a file picker
        fileChooser = new JFileChooser(".");
        // Open the file.
        File selectedFile = new File(fileChooser.toString());
        String filename = "";            // To hold the name and path of the file
        int fileChooserStatus;      /* Indicates status of the open dialog
                                     * box */
        //Diplay and open dialog box
        fileChooserStatus = fileChooser.showOpenDialog
            (app);

        if (fileChooserStatus == JFileChooser.APPROVE_OPTION)
        {
            //Get a reference to the selected file
            selectedFile = fileChooser.getSelectedFile();
        }
        Scanner inputFile = new Scanner(selectedFile);

        // Read lines from the file until no more are left.
        while (inputFile.hasNext())
        {
          //read the line
          line = inputFile.nextLine();
          // Split it
          lineTitle = line.split(",");
          // put the parts into a mutable list
          title.add(lineTitle[0]);
          price.add(lineTitle[1]);
          
        }

        // Close the file.
        inputFile.close();
    }

    /**
     * gets the array from the list in memory for the inventory
     * @return  String Array
     */
    private String[] getTitle() 
    {
     
        String [] titles = title.toArray(new String[title.size()]);
        
        return titles;
    }

    /**
     * get the array from the list in memory for the cart
     * @return  the book title
     */
    private String getCart() 
    {
        String ladenCart = null;
        if (cart.size() >= 1)
        {
            ladenCart = cart.get(listModel.size());
        }
        return ladenCart;
        
    }
    
    /**
     * This adds the selected book to the carts ArrayList
     * @param loc 
     */
    private void addCart(int loc)
    {
        if(loc >= 0)
        {
            String [] cartAdder = new String[title.size()];

            cartAdder[0]= title.get(loc);
            cart.add(cartAdder[0]);
        }
    }
    
    /**
     * This removes the item from the cart ArrayList and the Listmodel and
     * refreshes the Jlist
     * @param loc 
     */
    private void removeCart(int loc)
    {
        if ( cart.size() > 0 && loc >= 0)
        {
            cart.remove(loc);
            listModel.remove(loc);
        }
    }
    
    /**
     * This adds the selected books price to the running subtotal.
     * also calcs tax and total for live update.
     * @param loc 
     */
    private void tally(int loc)
    {
        if( loc != -1)
        {
            String totaltemp, taxtemp, subtemp = "";
            DecimalFormat df = new DecimalFormat("#.00"); 
            subtotal = subtotal + Double.parseDouble(price.get(loc));
            tax = subtotal *.06;
            total = subtotal + tax;
            subtemp = String.valueOf(df.format(subtotal));
            taxtemp = String.valueOf(df.format(tax));
            totaltemp = String.valueOf(df.format(total));
            subtotalDisplay.setText(subtemp);
            taxDisplay.setText(taxtemp);
            totalDisplay.setText(totaltemp);
        }
    }
    
    /** this removes the price from subtotal of the book being deleted in
     * the cart. retallys tax and total.
     * @param loc 
     */
    private void reverseTally(int loc)
    {
        
        String totaltemp, taxtemp, subtemp = "";
        DecimalFormat df = new DecimalFormat("#.00"); 
        if(cart.size() > 0 && loc >= 0)
        {
            // get title deleted
            String book = cart.get(loc);
            int count =0;
            String [] cartRemover = title.toArray(new String[title.size()]);
            for ( int i= 0; i <title.size(); i ++)
            { 
                if (cartRemover[i].equals(book))
                {
                    subtotal = subtotal - Double.parseDouble(price.get(count));
                }
                count ++;
            }



            tax = subtotal *.06;
            total = subtotal + tax;
            subtemp = String.valueOf(df.format(subtotal));
            taxtemp = String.valueOf(df.format(tax));
            totaltemp = String.valueOf(df.format(total));
            subtotalDisplay.setText(subtemp);
            taxDisplay.setText(taxtemp);
            totalDisplay.setText(totaltemp);
        }
    }
   

    /**
     * what to do when add is pressed
     */
    private  class addButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            addCart(leftList.getSelectedIndex());
            tally(leftList.getSelectedIndex());
            listModel.add(listModel.size(),getCart());
         
            
        }
    }

    /**
     * what to do when delete is pressed
     */
    private  class deleteButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            reverseTally(rightList.getSelectedIndex() );
            removeCart(rightList.getSelectedIndex());
            
            
        }
    }

    /**
     * what to do when check out is pressed
     */
    private class checkoutButtonListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            JOptionPane.showMessageDialog(null, "Please Pay : " + totalDisplay.getText()); 
        }
    }

  

}








