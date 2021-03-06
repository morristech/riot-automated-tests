package pom_android;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

/**
 * Settings page of Riot. </br>Opened from the context menu on the rooms list.
 * @author jeangb
 *
 */
public class RiotSettingsPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	
	public RiotSettingsPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/vector_settings_page", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ACTION BAR.
	 */
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement actionBarBackButton;
	@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/action_bar']/android.widget.TextView")
	public MobileElement actionBarSettingsTextView;
	
	
	/*
	 * MAIN SETTINGS VIEW.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/vector_settings_page")
	public MobileElement settingsFrameLayout;
	@AndroidFindBy(id="android:id/list")
	public MobileElement settingsListView;
	
	/**
	 * Check or uncheck contact permission.
	 * @param checked
	 */
	public void checkContactsPermissionIfNecessary(Boolean checked){
		MobileElement checkBox=getSettingsItemByName("Contacts permission").findElementById("android:id/checkbox");
		String status=checkBox.getText();
		if(checked && "OFF".equals(status) || false==checked && "ON".equals(status)){
			checkBox.click();
		}
	}
	
	/**
	 * From the settings view, hit the profile picture item, and change the avatar by taking a new picture.</br>
	 * @throws InterruptedException 
	 */
	public void changeAvatarFromSettings() throws InterruptedException{
		getSettingsItemByName("Profile Picture").click();
		RiotCameraPageObjects cameraPreview = new RiotCameraPageObjects(driver);
		cameraPreview.triggerCameraButton.click();//take a photo
		waitUntilDisplayed(driver,"im.vector.alpha:id/medias_picker_preview_layout", true, 5);
		cameraPreview.confirmPickingPictureButton.click();
	}
	
	/**
	 * From the settings view, erase the display name and set a new one.</br>
	 * It doesn't click on the save button.
	 * @param newDisplayName
	 */
	public void changeDisplayNameFromSettings(String newDisplayName){
		getSettingsItemByName("Display Name").click();
		driver.findElementById("android:id/edit").clear();
		driver.findElementById("android:id/edit").setValue(newDisplayName+"\n");
		driver.findElementById("android:id/button1").click();
	}
	
	/**
	 * Return sections visible on the screen of the list.</br>
	 * Sections are TextViews, can be: USER SETTINGS, LOCAL CONTACTS, NOTIFICATIONS...
	 */
	@AndroidFindBy(xpath="android.widget.ListView[@resource-id='android:id/list']/android.widget.TextView")
	public List<MobileElement> sectionsList;
	
	public MobileElement getSettingsItemByName(String settingsName){
		MobileElement settingsItemLayout=settingsListView.findElementByXPath("//android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.TextView[@text='"+settingsName+"']/../..");
		if(null==settingsItemLayout){
			System.out.println("No settings item found for: "+settingsName);
		}
		return settingsItemLayout;
	}
	
}
