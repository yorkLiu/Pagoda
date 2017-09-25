package com.ly.suma.api;

public class MyControl {
	public static Boolean isNull(String s) {
		if (s == null || "".equals(s)) {
			return true;
		} else {
			return false;
		}
	}
}
