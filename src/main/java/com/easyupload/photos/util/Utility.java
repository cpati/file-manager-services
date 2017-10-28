package com.easyupload.photos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	public static String dateFormatter(Date date) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(date);
	}
}
