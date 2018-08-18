package projects.ecommunicator.utility;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Property {
	private static ResourceBundle school;
	private static ResourceBundle font;
	private static ResourceBundle images;

	public static String getString(String propertyFileName, String key) {

		String value = "";
		try {
			value = getResourceBundle(propertyFileName).getString(key);
		} catch (MissingResourceException e) {
			System.out.println(
				"java.util.MissingResourceException: Couldn't find value for: "
					+ key);
		}
		return value;
	}

	private static ResourceBundle getResourceBundle(String propertyFileName) {

		if ((PropertyFileNames.SCHOOL).equals(propertyFileName)) {
			if (school == null) {
				school = ResourceBundle.getBundle(PropertyFileNames.SCHOOL);
			}
			return school;
		} else if ((PropertyFileNames.FONT).equals(propertyFileName)) {
			if (font == null) {
				font = ResourceBundle.getBundle(PropertyFileNames.FONT);
			}
			return font;
		} else if ((PropertyFileNames.IMAGES).equals(propertyFileName)) {
			if (images == null) {
				images = ResourceBundle.getBundle(PropertyFileNames.IMAGES);
			}
			return images;
		}
		return null;
	}

}
