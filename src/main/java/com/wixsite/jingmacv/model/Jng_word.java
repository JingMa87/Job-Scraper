package com.wixsite.jingmacv.model;

public class Jng_word {
	
	private int word_id;
	private String wrd_word;
	private String wrd_is_palin;
	
	public Jng_word(int word_id, String wrd_word, String wrd_is_palin) {
		this.word_id = word_id;
		this.wrd_word = wrd_word;
		this.wrd_is_palin = wrd_is_palin;
	}

	public int getWord_id() {
		return word_id;
	}

	public void setWords_id(int word_id) {
		this.word_id = word_id;
	}

	public String getWrd_word() {
		return wrd_word;
	}

	public void setWrd_word(String wrd_word) {
		this.wrd_word = wrd_word;
	}

	public String getWrd_is_palin() {
		return wrd_is_palin;
	}

	public void setWrd_is_palin(String wrd_is_palin) {
		this.wrd_is_palin = wrd_is_palin;
	}	
}
