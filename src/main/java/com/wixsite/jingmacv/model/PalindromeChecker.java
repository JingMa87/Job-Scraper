package com.wixsite.jingmacv.model;

public class PalindromeChecker {

	public static String isPalindrome(String word) {
		if (word == null) return "No";
		
		int length = word.length();
		for (int i = 0; i < length / 2; i++) {
			if (word.charAt(i) != word.charAt(length - 1 - i))
				return "No";
		}
		return "Yes";
	}
}
