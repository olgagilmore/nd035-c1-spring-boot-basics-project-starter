package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NoteTabPage {

    @FindBy(id="nav-notes-tab")
    private WebElement navNoteTab;

    @FindBy(id="add-note")
    private WebElement addButton;

    @FindBy(id="edit-note")
    private List<WebElement> editButton;

    @FindBy(id="delete-note")
    private List<WebElement> deleteButton;

    @FindBy(id="notetitle")
    private List<WebElement> titleList;

    @FindBy(id="notedescription")
    private List<WebElement> descriptionList;

    @FindBy(id="note-title")
    private WebElement inputTitle;

    @FindBy(id="note-description")
    private WebElement inputDescription;

    @FindBy(id="noteSubmit")
    private WebElement submitButton;

    @FindBy(id="note-modal-submit")
    private WebElement submitModalButton;

    public NoteTabPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }
    public int getNotesSize() {
        return this.titleList.size();
    }

    public List<String> getDetail(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        navNoteTab.click();
        wait.until(ExpectedConditions.elementToBeClickable(addButton));
        List<String> detail = new ArrayList<>(List.of(titleList.get(0).getText(),
                descriptionList.get(0).getText()));
        return detail;



    }

    public void changeToNoteNavigationTab(WebDriver driver){
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
    }

    public void clickButtonAddNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(this.addButton)).click();
    }
    public void createOrEditNote(WebDriver driver, String title, String description) {
        String textInsideNoteTitle = inputTitle.getText();

        if (textInsideNoteTitle.length() >= 0) {
            inputTitle.clear();
            inputDescription.clear();
        }
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(inputTitle)).sendKeys(title);
        wait.until(ExpectedConditions.elementToBeClickable(inputDescription)).sendKeys(description);
        wait.until(ExpectedConditions.elementToBeClickable(this.submitModalButton)).click();

    }

    public void addNote(WebDriver driver, String title, String description, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 60);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        } catch (TimeoutException ex) {
            System.out.println("Timeout Exception");
            nav.click();
            wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(inputTitle)).sendKeys(title);
        wait.until(ExpectedConditions.elementToBeClickable(inputDescription)).sendKeys(description);

        wait.until(ExpectedConditions.elementToBeClickable(this.submitModalButton)).click();

        //wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();
    }

    public void editNote(WebDriver driver, String title, String description){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();

        wait.until(ExpectedConditions.elementToBeClickable((editButton.get(0)))).click();

        wait.until(ExpectedConditions.elementToBeClickable(inputTitle));
        inputTitle.clear();
        inputTitle.sendKeys(title);

        wait.until(ExpectedConditions.elementToBeClickable(inputDescription));
        inputTitle.clear();
        inputTitle.sendKeys(description);

        wait.until(ExpectedConditions.elementToBeClickable(this.submitModalButton)).click();
    }

    public void deleteNote(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.elementToBeClickable(navNoteTab)).click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton.get(0))).click();
    }

}
