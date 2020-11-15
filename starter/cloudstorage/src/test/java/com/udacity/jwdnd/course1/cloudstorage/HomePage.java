package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logoutButton")
    private WebElement logoutButton;

    @FindBy(xpath = "//div[@id='logoutDiv']//h1")
    private WebElement name;

    private WebDriver driver;

    public HomePage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout(){
        logoutButton.click();
    }

    public String getName(){
        return name.getText();
    }
}
