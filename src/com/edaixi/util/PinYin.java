package com.edaixi.util;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import com.edaixi.util.HanziToPinyin3.Token;

public class PinYin {
	// 汉字返回拼音，字母原样返回，都转换为小写
	@SuppressLint("DefaultLocale")
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin3.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		if(input.contains("重庆")){
			return "c";
		}else {
			return sb.toString().toLowerCase();
		}
	}
}
