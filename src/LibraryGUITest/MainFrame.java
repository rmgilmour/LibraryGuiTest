package LibraryGUITest;
/* File Name: 	LibraryTest.java
        * Author:     	Roslyn Gilmour
        * Course:		CEN-3024C
        * Professor:	Mary Walauskis
        * Description:	This program will provide a GUI menu to obtain the
        *               user's menu choice.
        *               Choice Options:   Load file:
        *                                       Load a comma delimited file,
        *                                       and read the file into an array.
        *                                 List the file:
        *                                       Display the file details in a
        *                                       table.
        *                                 Add an item:
        *                                       Add a new book to the data file,
        *                                       and update the table display.
        *                                 Delete an item:
        *                                       Delete a book from the data file,
        *                                       and update the table display.
        *                                 Check out an item:
        *                                       Update the item's status to
        *                                       "Checked out", and update the
        *                                       due date to reflect a date 30
        *                                       days from the current date.
        *                                   Check in an item:
        *                                       Update the item's status to
        *                                       "In", and update the due date
        *                                       to reflect null.
        * Date:		10/29/23
        */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class MainFrame extends JFrame {

    // Table labels and fields
    private JPanel mainPanel;
    private JPanel titlePanel;
    private JLabel selectFile;
    private JButton btnSelectFile;
    private JTextField tfFileResult;
    private JTabbedPane tabbedPane1;
    private JPanel listPanel;
    private JButton btnList;
    private JPanel addPanel;
    private JLabel addIdLabel;
    private JTextField tfAddID;
    private JLabel addTitleLabel;
    private JTextField tfAddTitle;
    private JLabel addStatusLabel;
    private JTextField tfAddStatus;
    private JLabel addDateLabel;
    private JTextField tfAddDate;
    private JLabel addAuthorLabel;
    private JTextField tfAddAuthor;
    private JButton btnAddSubmit;
    private JPanel deletePanel;
    private JLabel deleteIdLabel;
    private JTextField tfDeleteID;
    private JButton btnDeleteItem;
    private JLabel deleteTitleLabel;
    private JTextField tfDeleteTitle;
    private JButton btnDeleteTitle;
    private JPanel outPanel;
    private JLabel outBarcodeLabel;
    private JTextField tfBarcode;
    private JButton btnCheckOut;
    private JPanel inPanel;
    private JLabel checkInLabel;
    private JTextField tfCheckIn;
    private JButton btnCheckIn;
    private JTextField textField1;
    private JPanel tablePanel;
    private JTable tableList;

    File selectedFile;
    String filepath;

    static ArrayList<BookList> booksArray = new ArrayList<>();

    // Object array to hold the rows content
    Object[][] data = {};

    // String array to hold hte columns content
    String[] columns = {"Barcode", "Title", "Author", "Status", "Due Date"};

    /*
    *Method to get the main panel.
    * @return returns the mainPanel.
    */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Main method to create the GUI
    public MainFrame() {
        setContentPane(mainPanel);
        setTitle("Library Management System");
        setSize(900, 750);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        createTable();

        // Button to choose a file to upload.
        btnSelectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if(returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    filepath = selectedFile.getPath();
                    Path path = Paths.get(filepath);
                    Path filename = path.getFileName();
                    tfFileResult.setText("" + filename);

                    // read the file and add to booksArray
                    String line;
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(filepath));

                        while ((line = br.readLine()) != null) {
                            String[] items = line.split(",");
                            BookList newList = getBookList(items);
                            booksArray.add(newList);
                        } // end while

                    } catch(FileNotFoundException open) {
                        textField1.setText("Unable to open.");
                    } catch (IOException read) {
                        textField1.setText("Unable to read file.");
                    }

                    textField1.setText("File Read.\n" + "Number of books in " + filename + ": " + booksArray.size());
                }

            }
        }); // end btnSelectFile

        // Action Listener for the List data button to list all data
        btnList.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                listItems(filepath);
            }
        });

        // Action Listener to add an item to the list
        btnAddSubmit.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String code = tfAddID.getText();
                String title = tfAddTitle.getText();
                String author = tfAddAuthor.getText();
                String status = tfAddStatus.getText();
                String date = tfAddDate.getText();

                BookList newBook = new BookList();
                newBook.editBookList(code, title, author, status, date);
                booksArray.add(newBook);

                int sz = booksArray.size();
                UpdateArray(sz, filepath);
                textField1.setText("File Updated.   " + "Number of books in list:  " + booksArray.size());
                tfAddID.setText("");
                tfAddTitle.setText("");
                tfAddAuthor.setText("");
                tfAddStatus.setText("");
                tfAddDate.setText("");

                refreshTable(filepath);
            }
        });

        //Action Listener to delete an item
        btnDeleteItem.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                String delByID;
                String delByTitle;
                if (tfDeleteID.getText() != null) {
                    delByID = tfDeleteID.getText();
                    booksArray.removeIf(book -> Objects.equals(book.getBarcode(), delByID));
                    tfDeleteID.setText("");
                } else {
                    delByTitle = tfDeleteTitle.getText();
                    booksArray.removeIf(book -> Objects.equals(book.getTitle(), delByTitle));
                    tfDeleteTitle.setText("");
                }

                int sz = booksArray.size();
                UpdateArray(sz, filepath);
                textField1.setText("File Updated.   " + "Number of books in list:  " + booksArray.size());
                refreshTable(filepath);
            }
        });

        //Action Listener to check an item out and update the status
        btnCheckOut.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String outStatus = tfBarcode.getText();
                checkOut(outStatus);
                tfBarcode.setText("");
            }
        });

        //Action Listener to check an item in and update the status
        btnCheckIn.addActionListener(new ActionListener() {
            /**
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String inStatus = tfCheckIn.getText();
                checkIn(inStatus);
                tfCheckIn.setText("");
            }
        });

    } // end mainFrame


    private void createTable() {

        tableList.setModel(new DefaultTableModel(data, columns));
        TableColumnModel columns = tableList.getColumnModel();
        columns.getColumn(1).setMinWidth(250);
        columns.getColumn(2).setMinWidth(150);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRenderer);
        columns.getColumn(3).setCellRenderer(centerRenderer);
        columns.getColumn(4).setCellRenderer(centerRenderer);

    }

    private static BookList getBookList(String[] items) {
        String barcode = items[0];
        String title = items[1];
        String author = items[2];
        String status = items[3];
        String dueDate = items[4];

        BookList newList = new BookList();
        newList.setBarcode(barcode);
        newList.setTitle(title);
        newList.setAuthor(author);
        newList.setStatus(status);
        newList.setDueDate(dueDate);
        return newList;
    }

    // Method to list the items in the file
    // to the table.
    public void listItems(String filepath) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            DefaultTableModel model = (DefaultTableModel)tableList.getModel();
            model.setColumnIdentifiers(columns);

            Object[] tableRows = br.lines().toArray();

            for(int i=0; i<tableRows.length; i++){
                String rows = tableRows[i].toString().trim();
                String[] dataRow = rows.split(",");
                model.addRow(dataRow);
            }

        } catch(FileNotFoundException open) {
            textField1.setText("Unable to open.");
        }

    } // end listItems

    // Method to checkout an item, update the array,
    // and update the table content
    public void checkOut(String outStatus) {

        BookList book = new BookList();

        LocalDate date = LocalDate.now();
        LocalDate newDate = date.plusDays(30);
        String toDate = String.valueOf(newDate);

        int i;
        for (i=0; i<booksArray.size(); i++) {
            String[] array = String.valueOf(booksArray.get(i)).split(",");
            for(String a : array) {
                if (Objects.equals(a, outStatus)) {
                    book.setBarcode(outStatus);
                    book.setTitle(String.valueOf(array[1]));
                    book.setAuthor(String.valueOf(array[2]));
                    book.setStatus("Out");
                    book.setDueDate(toDate);

                    String cde = book.getBarcode();
                    String tle = book.getTitle();
                    String auth = book.getAuthor();
                    String stat = book.getStatus();
                    String dte = book.getDueDate();

                    booksArray.removeIf(obj -> Objects.equals(obj.getBarcode(), outStatus));

                    BookList newBook = new BookList();
                    newBook.editBookList(cde,tle,auth,stat,dte);
                    booksArray.add(newBook);

                    int sz = booksArray.size();
                    UpdateArray(sz, filepath);
                }
            }
        }
        textField1.setText("Title: " + book.getTitle() + "   Status: Checked Out.  Due Date: " + book.getDueDate());
        refreshTable(filepath);
    }

    // Method to checkin an item, update the array
    // and update the table content.
    public void checkIn(String inStatus) {

        BookList book = new BookList();

        int i;
        for (i=0; i<booksArray.size(); i++) {
            String[] array = String.valueOf(booksArray.get(i)).split(",");
            for(String a : array) {
                if (Objects.equals(a, inStatus)) {
                    book.setBarcode(inStatus);
                    book.setTitle(String.valueOf(array[1]));
                    book.setAuthor(String.valueOf(array[2]));
                    book.setStatus("In");
                    book.setDueDate("null");

                    String cde = book.getBarcode();
                    String tle = book.getTitle();
                    String auth = book.getAuthor();
                    String stat = book.getStatus();
                    String dte = book.getDueDate();

                    booksArray.removeIf(obj -> Objects.equals(obj.getBarcode(), inStatus));

                    BookList newBook = new LibraryGUITest.BookList();
                    newBook.editBookList(cde,tle,auth,stat,dte);
                    booksArray.add(newBook);

                    int sz = booksArray.size();
                    UpdateArray(sz, filepath);
                }
            }
        }
        textField1.setText("Title: " + book.getTitle() + "   Status: Checked In");
        refreshTable(filepath);
    }

    // Method to update the array.
    private static void UpdateArray(int sz, String filepath) {

        try {
            FileWriter fw = new FileWriter(filepath);
            Writer output = new BufferedWriter(fw);
            for (int i=0; i<sz; i++) {
                output.write(booksArray.get(i).toString() + ("\n"));
            }
            output.close();
            fw.close();

        } catch (IOException fos) {
            System.out.println("fos error.");
        }
    }

    // Method to refresh the table content
    public void refreshTable(String filepath){

        tableList.setModel(new DefaultTableModel(null, columns));

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            DefaultTableModel model = (DefaultTableModel)tableList.getModel();
            model.setColumnIdentifiers(columns);

            Object[] tableRows = br.lines().toArray();

            for(int i=0; i<tableRows.length; i++){
                String rows = tableRows[i].toString().trim();
                String[] dataRow = rows.split(",");
                model.addRow(dataRow);
            }

        } catch(FileNotFoundException open) {
            textField1.setText("Unable to open.");
        }


    }


    // Main method.
    public static void main(String[] args) {
        MainFrame myFrame = new MainFrame();
    } // end main

} // End class MainFrame.java



