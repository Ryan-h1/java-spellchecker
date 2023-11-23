import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FinishedGUI  {

    //frame and panels defined for cardlayout
    private static JFrame frame = new JFrame("Spellchecker");
    private static JPanel panelCont = new JPanel();
    private static JPanel panelMain = new JPanel();
    private static JPanel panelLanding = new JPanel();
    private static CardLayout cl = new CardLayout();

    // objects defined for main page
    private static JButton button; 
    private static JButton button2;
    private static PopupFactory pf;
    private static JButton close;
    private static JPanel poppanel;
    private static JRadioButton engUS;
    private static JRadioButton engUK;
    private static JRadioButton engCa;
    private static JLabel label1;
    private static JLabel label2;
    private static Popup pop;
    final static JFileChooser fileChooser = new JFileChooser();

    //colours and objects defined for landing page
    private static final Color MENU_BG_COLOR = Color.decode("#800080");
    private static final Color MARGIN_COLOR = Color.decode("#E3E2D4");
    private static final Color TEXT_AREA_BG_COLOR = Color.WHITE;
    private static final Font MENU_FONT = new Font("SansSerif", Font.PLAIN, 20);
    private static final int MENU_BAR_HEIGHT = 89; // Adjusted menu bar height
    private static JTextArea textArea;
    private static String filePath;
    private static Popup p;

    public static void main(String[] args) {
            SwingUtilities.invokeLater(FinishedGUI::create);
        }

    public static void create(){

    
        panelCont.setLayout(cl); //set the layout for the container panel


        //now add the main gui stuff to panelMain
        poppanel = new JPanel(new BorderLayout());
        poppanel.setSize(800, 500);
        label1 = new JLabel("<html><div style='text-align: center;'>Welcome to our Spellchecker application! To begin, you can choose to upload an existing .txt or .html file or create your own here.</div></html>");
        Font newLabelFont = new Font(label1.getFont().getName(), label1.getFont().getStyle(), 20);
        label1.setFont(newLabelFont);
        label1.setForeground(Color.WHITE);
        close = new JButton("Begin!");
        Font newCloseFont = new Font(close.getFont().getName(), close.getFont().getStyle(), 20);
        close.setPreferredSize(new Dimension(150, 75));
        close.setMaximumSize(new Dimension(150, 75));
        close.setFont(newCloseFont);
        close.setForeground(new Color(0x993399));
        //close.addActionListener(this);
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               pop.hide();
            }
         });
        poppanel.add(label1, BorderLayout.CENTER); // Position label at the top
        poppanel.add(close, BorderLayout.SOUTH); // Position button at the bottom
        poppanel.setBackground(new Color(0x993399));
        poppanel.setPreferredSize(new Dimension(800, 500));
        pf = PopupFactory.getSharedInstance();
        pop = pf.getPopup(frame,poppanel,300,100);

        button = new JButton();
        button.setBounds(150,100,400,300);
        //button.addActionListener(this);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               System.out.println("create file");
               cl.show(panelCont,"landing");
            }
         });
        button.setText("New File");
        Font newButtonFont=new Font(button.getFont().getName(),button.getFont().getStyle(),20);
        button.setFont(newButtonFont);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setForeground(new Color(0xffffff));
        button.setBackground(new Color(0x993399));

        button2 = new JButton();
        button2.setBounds(880,100,400,300);
        //button2.addActionListener(this);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getPath());
                    // You can perform operations on the selected file here
            }
            }
         });
        
        button2.setText("Upload File");
        button2.setFont(newButtonFont);
        button2.setFocusable(false);
        button2.setBorderPainted(false);
        button2.setOpaque(true);
        button2.setForeground(new Color(0xffffff));
        button2.setBackground(new Color(0x993399));

        engUS = new JRadioButton("English (United States)");
        engUK = new JRadioButton("English (United Kingdom)");
        engCa = new JRadioButton("English (Canada)");
        //engUS.addActionListener(this);
        engUS.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               System.out.println("USA");
            }
         });
        //engUK.addActionListener(this);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               System.out.println("UK");
            }
         });
        //engCa.addActionListener(this);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               System.out.println("CANADA");
            }
         });
        engUS.setForeground(new Color(0x993399));
        engUK.setForeground(new Color(0x993399));
        engCa.setForeground(new Color(0x993399));
        ButtonGroup group = new ButtonGroup();
        group.add(engUS);
        group.add(engUK);
        group.add(engCa);
        engUS.setBounds(597, 500, 200, 30);
        engUK.setBounds(597, 550, 200, 30);
        engCa.setBounds(597, 600, 200, 30);

        panelMain.setBackground(new Color(0xebebe0));
        panelMain.setLayout(null);
        panelMain.add(button);
        panelMain.add(button2);
        panelMain.add(engUS);
        panelMain.add(engUK);
        panelMain.add(engCa);
        panelMain.setBounds(0,0,1594,1030);

        //now add spellchecker gui stuff to panel landing
        //Be very careful here this code is for making sure the text is white
        //don't ask me how it works i used chatgtp
        //i might need to look for a simpler way to do it
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            } catch (Exception e) {
            // If Nimbus is not available, fall back to the default L&F.
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    
            }
            //this is where the code for making the text white ends
    
    
    
    
            //deinfing the jframe
            //frame = new JFrame("Spell Checker");
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.setSize(1594, 1030); // Including menu bar
    
            // Menu bar setup
            JMenuBar menuBar = new JMenuBar();
            menuBar.setPreferredSize(new Dimension(frame.getWidth(), MENU_BAR_HEIGHT));
            menuBar.setBackground(MENU_BG_COLOR);
            menuBar.setLayout(new GridLayout(1, 6, 0, 0)); // 6 items, no gaps in grid layout
    
    
    
            // Menu items
            String[] FileItems = {"Open", "Save", "Save As", "New"};
            String[] SettingItems = {"View User Dictionary", "Exit Checker", "Add Word To Dictionary"};
            String[] MetricsItems = {"Number of Spelling Errors", "Number of Corrections", "Metrics Related to Document"};
            String[] HelpItems = {"More Stuff"};
           
            menuBar.add(createMenu("File", FileItems, createFileActionListener()));
            menuBar.add(createMenu("Settings", SettingItems, createSettingActionListener()));
            menuBar.add(createMenu("Spell Check", createSpellCheckActionListener()));
            menuBar.add(createMenu("Metrics", MetricsItems, createMetricsActionListener()));
            menuBar.add(createMenu("Save", createSaveActionListener()));
            menuBar.add(createMenu("Help", HelpItems, createHelpActionListener()));
            
    
            // Text editor setup
            textArea = new JTextArea();
            textArea.setBackground(TEXT_AREA_BG_COLOR);
            textArea.setFont(MENU_FONT);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(50, 50,50,50));
            
            textArea.setBorder(border);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(new MatteBorder(10, 345, 10, 345, MARGIN_COLOR)); // top, left, bottom, right
    
    
            // Adding components to panel finally after initializing 
            panelLanding.setLayout(null);
            panelLanding.setBounds(0,0,1594,1030);
            panelLanding.add(menuBar, BorderLayout.NORTH);
            panelLanding.add(scrollPane, BorderLayout.CENTER);

            //add panelMain and panelLanding to panelCont
            panelCont.add(panelMain, "main");
            panelCont.add(panelLanding,"landing");
            cl.show(panelCont, "main");

            //add panels to frame
            frame.setLayout(null);
            frame.setSize(1594,1030);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Spellchecker");
            frame.add(panelCont);
            pop.show();
            frame.setVisible(true);

            //frame.add(menuBar, BorderLayout.NORTH);
            //frame.add(scrollPane, BorderLayout.CENTER);
            //frame.setLocationRelativeTo(null);
            //frame.setVisible(true);
    
        }
        private static void openFile(JTextArea textArea) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
    
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selected = fileChooser.getSelectedFile();
                filePath = selected.getAbsolutePath();
                try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
    
                    // Assuming 'textArea' is the JTextArea component
                    textArea.setText(content.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private static ActionListener createSaveActionListener() {
            // TODO Auto-generated method stub
            return new ActionListener() {
                public void actionPerformed(ActionEvent e ) {
                    JMenu source = (JMenu) e.getSource();
                    
                    System.out.println("Save menu clicked: "+ source.getText());
                    System.out.println(textArea.getText());
                    
                    if (filePath!= null) {
                        try (FileWriter fileWriter = new FileWriter(filePath)) {
                            fileWriter.write(textArea.getText());
                            System.out.println("File saved successfully at: " + filePath);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            System.err.println("Error saving the file.");
                        }
                    } else {
                        System.out.println("Please select a file before saving.");
                    }
                    
                }
            };
        }
    
        private static ActionListener createSpellCheckActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JMenuItem source = (JMenuItem) e.getSource();
                    System.out.println("SpellCheck menu clicked: " + source.getText());
                }
            };
        }
    
        private static ActionListener createHelpActionListener() {
            // TODO Auto-generated method stub
            return new ActionListener() {
                public void actionPerformed(ActionEvent e ) {
                    JMenuItem source = (JMenuItem) e.getSource();
                    System.out.println("Help menu item clicked: "+ source.getText());
                    PopupFactory pf = PopupFactory.getSharedInstance();
                    JPanel p2 = new JPanel();
                    
                    JLabel label = new JLabel("<html><font color = 'white'>Welcome to our spellchecker ! <br><br>"
                            + "We're so happy you're here <br><br>"
                            + "There are 5 major tabs for navigating through the application. <br><br>"
                            + "Settings, File Spell Check, Metrics, Save <br><br>"
                            + "Feel free to look around!!</font></html> ");
                    label.setPreferredSize(new Dimension(250,250));
                    p2.add(label);
                    p2.setBackground(MENU_BG_COLOR);
                    p2.setPreferredSize(new Dimension(400,350));
                    //ImageIcon x = new ImageIcon("close");
                    JButton close = new JButton("close");
                    close.setBounds(500, 240, 20, 30);
                    close.addActionListener(new ActionListener() {
    
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // TODO Auto-generated method stub
                            p.hide();
                        }
                        
                    });
                    p2.setLayout(new FlowLayout(FlowLayout.CENTER));
                    p2.add(close);
                    
                    // Create the Popup with your JPanel
                    p = pf.getPopup(frame, p2, 520, 250);
                    
                    p2.setSize(200, 200);
                    // Show the Popup
                    p.show();
                    // Show the Popup
                    
                }
            };
        }
        
        private static ActionListener createMetricsActionListener() {
            // TODO Auto-generated method stub
            return new ActionListener() {
                //@Override
                public void actionPerformed(ActionEvent e ) {
                    JMenuItem source = (JMenuItem) e.getSource();
                    System.out.println("Metrics menu item clicked: "+ source.getText());
                    PopupFactory pf = PopupFactory.getSharedInstance();
                    JPanel p2 = new JPanel();
                    if (source.getText().equals("Number of Spelling Errors")) {
                        JLabel label = new JLabel("<html><font color = 'white'>Number of Spelling Errors: 0</font></html> ");
                        //set size of label
                        label.setPreferredSize(new Dimension(200,150));
                        // add label to panel
                        p2.add(label);
                        // set backgroung of panel
                        p2.setBackground(MENU_BG_COLOR);
                        // set dimensions of panel
                        p2.setPreferredSize(new Dimension(375,200));
                        // add close button
                        JButton close = new JButton("close");
                        // set button dimensions and location (x,y,width,length)
                        close.setBounds(500, 240, 20, 30);
                        // add actionlistener to button
                        close.addActionListener(new ActionListener() {
    
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                p.hide();
                            }
                            
                        });
                        // centers p2
                        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
                        p2.add(close);
                        
                        // Create the Popup with your JPanel
                        p = pf.getPopup(frame, p2, 520, 250);
                        
                        p2.setSize(200, 200);
                        // Show the Popup
                        p.show();
                    }
                    if (source.getText().equals("Number of Corrections")) {
                        JLabel label = new JLabel("<html><font color = 'white'>Number of Corrections: 0</font></html> ");
                        label.setPreferredSize(new Dimension(200,150));
                        p2.add(label);
                        p2.setBackground(MENU_BG_COLOR);
                        p2.setPreferredSize(new Dimension(375,200));
                        //ImageIcon x = new ImageIcon("close");
                        JButton close = new JButton("close");
                        close.setBounds(500, 240, 20, 30);
                        close.addActionListener(new ActionListener() {
    
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                p.hide();
                            }
                            
                        });
                        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
                        p2.add(close);
                        
                        // Create the Popup with your JPanel
                        p = pf.getPopup(frame, p2, 520, 250);
                        
                        p2.setSize(200, 200);
                        // Show the Popup
                        p.show();
                    } 

                    if (source.getText().equals("Metrics Related to Document")) {
                        JLabel label = new JLabel("<html><font color = 'white'>Metrics Related to Document: 0</font></html> ");
                        label.setPreferredSize(new Dimension(200,150));
                        p2.add(label);
                        p2.setBackground(MENU_BG_COLOR);
                        p2.setPreferredSize(new Dimension(375,200));
                        //ImageIcon x = new ImageIcon("close");
                        JButton close = new JButton("close");
                        close.setBounds(500, 240, 20, 30);
                        close.addActionListener(new ActionListener() {
    
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                p.hide();
                            }
                            
                        });
                        p2.setLayout(new FlowLayout(FlowLayout.CENTER));
                        p2.add(close);
                        
                        // Create the Popup with your JPanel
                        p = pf.getPopup(frame, p2, 520, 250);
                        
                        p2.setSize(200, 200);
                        // Show the Popup
                        p.show();
                    }
                    
                    
                    // Show the Popup
                    
                    
                }
            };
        }
    
        private static ActionListener createSettingActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e ) {
                    JMenuItem source = (JMenuItem) e.getSource();
                    System.out.println("Settings menu item clicked: "+ source.getText());
                }
            };
            // TODO Auto-generated method stub
            
        }
        
    
        private static ActionListener createFileActionListener() {
            return new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add your code here for the "File" menu actions
                    JMenuItem source = (JMenuItem) e.getSource();
                    System.out.println("File menu item clicked: "+ source.getText());
                    if (source.getText().equals("Open")){
                        openFile(textArea);
                        
                    }
                    if (source.getText().equals("New")) {
                        textArea.setText("");
                    }
                    if (source.getText().equals("Save As")) {
                        saveAsFile(textArea);
                    }
                    if (source.getText().equals("Save")) {
                        try (FileWriter fileWriter = new FileWriter(filePath)) {
                            fileWriter.write(textArea.getText());
                            System.out.println("File saved successfully at: " + filePath);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            System.err.println("Error saving the file.");
                        }
                    }
                    
                }
            };
        }
    
        private static void saveAsFile(JTextArea textArea) {
            
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(null);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                try (FileWriter fileWriter = new FileWriter(selectedFile)){
                    fileWriter.write(textArea.getText());
                    
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        //initializes the menu item
        //i think this might have been the problem ana but im not sure
        //i think you need you to deinfe all the colors before add to the jframe
        //this is a function that defines a menu item
        private static JMenu createMenu(String title, ActionListener action) {
            JMenu menuItem = new JMenu(title);
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Call the actionPerformed method of the provided ActionListener
                    action.actionPerformed(new ActionEvent(menuItem, ActionEvent.ACTION_PERFORMED, title));
                }
            });
            menuItem.setHorizontalAlignment(SwingConstants.CENTER);
            menuItem.setForeground(Color.WHITE); // This sets the text color to white
            menuItem.setBackground(MENU_BG_COLOR);
            menuItem.setFont(MENU_FONT);
            menuItem.setOpaque(true); // This is necessary for the background and foreground colors to show
            menuItem.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around the text
            
            return menuItem;
        }
       
        
        private static JMenu createMenu(String title, String[] items, ActionListener action) {
            JMenu menu = new JMenu(title);
            menu.setHorizontalAlignment(SwingConstants.CENTER);
            menu.setForeground(Color.WHITE); // This sets the text color to white
            menu.setBackground(MENU_BG_COLOR);
            menu.setFont(MENU_FONT);
            menu.setOpaque(true); // This is necessary for the background and foreground colors to show
            menu.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around the text
            for (int i = 0; i < items.length; i++) {
                JMenuItem item = new JMenuItem(items[i]);
                item.addActionListener(action);
                menu.add(item);
                
                
            }
            return menu;
        }    

      

    /*@Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == button){
            //do something when create file is clicked
            System.out.println("create file");
            cl.show(panelCont,"landing");
        }
        else if(e.getSource() == button2){
            //do something when upload file is clicked
            int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getPath());
                    // You can perform operations on the selected file here
            }
        }
        else if(e.getSource() == engUS){
            System.out.println("us");
        }
        else if(e.getSource() == engUK){
            System.out.println("uk");
        }
        else if(e.getSource() == engCa){
            System.out.println("OH CANADA");
        }
        else if(e.getSource() == close){
            p.hide();
        }

    }*/
}




    


