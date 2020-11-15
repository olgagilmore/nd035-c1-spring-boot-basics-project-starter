package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private String baseUrl;

	private String username = "aUser";
	private String password = "randomPassword";
	private String firstName = "Ann";
	private String lastName = "Smart";

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;
	private CredentialTabPage credentialTabPage;
	private NoteTabPage noteTabPage;

	@Autowired
	private CredentialService credentialService;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@BeforeEach
	public void beforeEach() {
		this.baseUrl = "http://localhost:" + port;
		//this.driver = new ChromeDriver();
		this.loginPage = new LoginPage(driver);
		this.signupPage = new SignupPage(driver);
		this.homePage = new HomePage(driver);
		this.noteTabPage = new NoteTabPage(driver);
		this.credentialTabPage = new CredentialTabPage(driver);
		this.noteTabPage = new NoteTabPage(driver);
	}

	//@AfterEach
	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testValidLoginLogout() {
		driver.get(baseUrl + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, username, password);

		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		//LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		//loginPage.login("tester", password);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		//WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("error-msg")));

		//Assertions.assertEquals("Invalid username or password", driver.findElement(By.id("error-msg")).getText());

		driver.get(baseUrl +"/home");
		//HomePage homePage = new HomePage(driver);
		homePage.logout();

		wait.until(ExpectedConditions.titleContains("Login"));

		Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());
		//wait.until(webDriver -> webDriver.findElement(By.id("logout-msg")));
		Assertions.assertEquals("You have been logged out", driver.findElement(By.id("logout-msg")).getText());

	}

	@Test
	@Order(2)
	public void testInvalidLogin() {
		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("username", "password");

		//loginPage.login("tester", password);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("error-msg")));
		Assertions.assertEquals("Invalid username or password", driver.findElement(By.id("error-msg")).getText());
	}

	@Test
	//@Order(4)
	public void testNoteCRUDfunctions() {
		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		//WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("error-msg")));

		//Assertions.assertEquals("Invalid username or password", driver.findElement(By.id("error-msg")).getText());

		driver.get(baseUrl +"/home");
		//HomePage homePage = new HomePage(driver);
		WebElement nav = driver.findElement(By.id("nav-notes-tab"));
		nav.click();
		noteTabPage.addNote(driver, "This is my title", "This is Description",nav);

		driver.findElement(By.id("home-link")).click();

		driver.get(this.baseUrl + "/home");

		List<String> detail = noteTabPage.getDetail(driver);

		Assertions.assertEquals("This is my title", detail.get(0));
		Assertions.assertEquals("This is Description", detail.get(1));

		noteTabPage.editNote(driver, "Edit title", "Edit Description");

		driver.get("http://localhost:" + this.port +  "/home");

		detail = noteTabPage.getDetail(driver);

		Assertions.assertEquals("Edit Description", detail.get(0));
		Assertions.assertEquals("This is Description", detail.get(1));

		noteTabPage.deleteNote(driver);
		driver.get("http://localhost:" + this.port + "/home");

		wait.until(driver -> driver.findElement(By.id("nav-notes-tab"))).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//String noteSize = wait.until(driver -> driver.findElement(By.id("note-size")).getText());
		int noteSize = this.noteTabPage.getNotesSize();
		Assertions.assertEquals(0, noteSize);


	}

	/*@Test
	public void whenNoteCreated_ThenValidateNoteExists() {

		userLoginProcess();

		noteTabPage.changeToNoteNavigationTab(driver);

		noteTabPage.clickButtonAddNote(driver);

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		homePage.createOrEditNote("noteTitle", "noteDescription");

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		noteTabPage.changeToNoteNavigationTab(driver);

		assertEquals("noteTitle", homePage.getTableNoteTitle());

	}*/

	@Test
	@Order(5)
	public void testAddEditDeleteCredentials() throws InterruptedException {
		String url = "www.superduperdrive.com";
		String user = "someUserName";
		String pwd = "somePwd";

		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		//Add credentials
		//credentialTabPage = new CredentialTabPage(driver);
		WebDriverWait wait = new WebDriverWait(driver, 30);

		driver.get(this.baseUrl + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();

		int credNumber= this.credentialTabPage.getCredentialSize();
		Assertions.assertEquals(credNumber, 0);

		WebElement nav = driver.findElement(By.id("nav-credentials-tab"));
		nav.click();
		this.credentialTabPage.addCredential(driver,url, user,pwd, nav);

		driver.findElement(By.id("home-link")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(this.baseUrl + "/home");
		//wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();

		Assertions.assertEquals(this.credentialTabPage.getCredentialSize(), credNumber +1);
		credNumber++;

		driver.get(this.baseUrl + "/home");
		//wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();
		//Check added credentials for the first credentials
		List<String> detail = credentialTabPage.getDetail(driver, 0);


		Assertions.assertEquals(url, detail.get(0));
		Assertions.assertEquals(user, detail.get(1));


		//password is encrypted thus not equal

		Assertions.assertNotEquals(pwd, detail.get(2));

		//test edit functionality
		Credential credential = this.credentialService.getCredentialByCredentialId(1);

		driver.get(this.baseUrl + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();

		this.credentialTabPage.editCredential(driver,credential, 0, pwd, url, "Test","Test123");

		driver.findElement(By.id("home-link")).click();

		//Check edited credentials
		driver.get(this.baseUrl + "/home");
		detail = credentialTabPage.getDetail(driver,0);

		Assertions.assertEquals(url, detail.get(0));
		Assertions.assertEquals("Test", detail.get(1));


		//password is encrypted thus not equal
		Assertions.assertNotEquals("Test123", detail.get(2));

		//Delete credentials
		driver.get(this.baseUrl + "/home");
		wait.until(driver -> driver.findElement(By.id("nav-credentials-tab"))).click();

		this.credentialTabPage.deleteCredential(driver, 0);
		driver.get(this.baseUrl + "/home");
		Assertions.assertEquals(this.credentialTabPage.getCredentialSize(), credNumber -1);
		credNumber--;
		/*wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-credential"))).click();
		driver.get(this.baseUrl + "/result");*/
		//There are no credentials left - urlList,  is empty
		/*Assertions.assertEquals(List.of(driver.findElement((By.id("credUrl")))).size(), 0);
		Assertions.assertEquals(List.of(driver.findElement((By.id("credUsername")))).size(), 0);
		Assertions.assertEquals(List.of(driver.findElement((By.id("credPassword")))).size(), 0);*/

		//homePage = new HomePage(driver);
		homePage.logout();
		wait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals(this.baseUrl + "/login?logout", driver.getCurrentUrl());

	}

}
