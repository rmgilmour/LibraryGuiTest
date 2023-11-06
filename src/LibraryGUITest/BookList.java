package LibraryGUITest;/*
 * File Name: 	LibraryTest.java
 * Author:     	Roslyn Gilmour
 * Course:		CEN-3024C
 * Professor:	Mary Walauskis
 * Description:	This program will provide a Switch menu to obtain the
 *              user's menu choice.
 *              The first choice is to read a text file.  The readFile
 *              method will call the readFile method to read the file.
 *              This method will then convert the contents to objects
 *              and store the object values in an array for later use.
 *              The user can then select to either list the array
 *              contents via the listFile method, or remove a line item
 * 				from the array and return the list.
 * Date:		10/8/23
 */


/**
 * No return class to encapsulate the data into getters and setters.
 */
public class BookList{
    private String barcode;
    private String title;
    private String author;
    private String status;
    private String dueDate;

    /**
     * Method to set the arrayList of items
     */
    public BookList() {
        setBarcode(barcode);
        setTitle(title);
        setAuthor(author);
        setStatus(status);
        setDueDate(dueDate);
    }

    /**
     * Method to get the barcode.
     * @return      Returns the barcode.
     */
    public String getBarcode() {
        return barcode;
    }
    /**
     * Method to set the barcode content.
     * @param barcode    Parameter to hold the content of
     *                  the barcode and create an instance
     *                  of barcode.
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    /**
     * Method to get the title content.
     * @return          Returns the title content.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Method to set the title content.
     * @param title     Parameter to hold the content of
     *                  the title and create an instance
     *                  of title.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Method to get the author content.
     * @return          Returns the content of author.
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Method to set the author content.
     * @param author        Parameter to hold the content of
     *                      the author and create an instance
     *                      of author.
     */
    public void setAuthor(String author) { this.author = author;}
    /**
     * Method to get the status content.
     *
     * @return Returns the content of status.
     */
    public String getStatus() { return status; }
    /**
     * Method to set the status content.
     * @param status        Parameter to hold the content of
     *                      the status and create an instance
     *                      of status.
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Method to get the dueDate content.
     * @return          Returns the content of the item's dueDate.
     */
    public String getDueDate() { return dueDate; }
    /**
     * Method to set the dueDate content.
     * @param dueDate        Parameter to hold the due date of
     *                      the item and create an instance
     *                      of dueDate.
     */
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String toString() {
        return barcode + "," + title + "," + author + "," + status + "," + dueDate;
    }

    /**
     * Void method to set the bookList
     * @param barcode       Parameter to hold the due date
     * @param title         Parameter to hold the title
     * @param author        Parameter to hold the author
     * @param status        Parameter to hold the status
     * @param dueDate       Parameter to hold the dueDate
     */
    public void editBookList(String barcode, String title, String author, String status, String dueDate) {
        this.barcode = barcode;
        this.title = title;
        this.author = author;
        this.status = status;
        this.dueDate = dueDate;
    }

} // end LibraryGUITest.BookList class
