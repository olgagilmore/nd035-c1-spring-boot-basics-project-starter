package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class CredentialTabPage {

    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredential;

    @FindBy(id = "add-credential")
    private WebElement addButton;

    @FindBy(id = "edit-credentialbtn")
    private List<WebElement>  editButtonList;

    @FindBy(id = "delete-credentialbtn")
    private List<WebElement> deleteButtonList;

    @FindBy(id = "credUrl")
    private List<WebElement> urlList;

    @FindBy(id = "credUsername")
    private List<WebElement> usernameList;

    @FindBy(id = "credPassword")
    private List<WebElement> passwordList;

    @FindBy(id = "credential-url")
    private WebElement inputUrl;

    @FindBy(id = "credential-username")
    private WebElement inputUsername;

    @FindBy(id = "credential-password")
    private WebElement inputPassword;

    @FindBy(id="credentialSubmit")
    private WebElement saveButton;

    @FindBy(id = "close-button")
    private WebElement closeButton;

    @FindBy(id = "credential-modal-submit")
    private WebElement submitModalButton;


    public CredentialTabPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);

    }

    public WebElement getPassword() {
        return inputPassword;
    }

    public List<String> getDetail(WebDriver driver, int index) {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOf(navCredential)).click();
        //navCredential.click();
        //wait.until(ExpectedConditions.visibilityOf(addButton));
        List<String> details = new ArrayList<>(List.of(urlList.get(index).getText(),
                usernameList.get(index).getText(),
                passwordList.get(index).getText()));

        return details;
    }

    public void close(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
    }

    public void addCredential(WebDriver driver, String url, String username, String password, WebElement nav) {
        WebDriverWait wait = new WebDriverWait(driver, 60);

        try{
            wait.until(ExpectedConditions.visibilityOf(navCredential)).click();
        }catch(TimeoutException ex){
            System.out.println("Time out Exception");
            nav.click();
            wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(addButton)).click();
        //addButton.click();


        wait.until(ExpectedConditions.visibilityOf(inputUrl)).sendKeys(url);
        wait.until(ExpectedConditions.visibilityOf(inputUsername)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(inputPassword)).sendKeys(password);

        //wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(submitModalButton)).click();

        //wait.until(ExpectedConditions.visibilityOf(navCredential)).click();
    }

    public void editCredential(WebDriver driver, Credential credential,
                               int index, String old_password,
                               String url, String username, String password) {

        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.visibilityOf(navCredential)).click();

        //editButtonList.get(index).click();
        wait.until(ExpectedConditions.elementToBeClickable(editButtonList.get(index))).click();

       /* Assertions.assertEquals(credential.getUrl(),inputUrl.getText());
        Assertions.assertEquals(credential.getUsername(),inputUsername.getText());
        Assertions.assertEquals(old_password,inputPassword.getText());*/

        wait.until(ExpectedConditions.visibilityOf(inputUrl));
        inputUrl.clear();
        inputUrl.sendKeys(url);

        wait.until(ExpectedConditions.visibilityOf(inputUsername));
        inputUsername.clear();
        inputUsername.sendKeys(username);

        wait.until(ExpectedConditions.visibilityOf(inputPassword));
        inputPassword.clear();
        inputPassword.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(submitModalButton)).click();
    }

    public void deleteCredential(WebDriver driver, int index){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(navCredential)).click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButtonList.get(index))).click();
    }

    public int getCredentialSize() {
        return urlList.size();
    }

}
