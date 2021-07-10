package com.mycomp.notesApp.utils;

import java.util.List;

import com.mycomp.notesApp.domain.TAGS;

/**
 * Common Utils to be used across the App
 * 
 * @author Rahil
 *
 */
public class CommonUtils {
	public static boolean isStringNullorEmpty(String inputStr) {
		return inputStr == null || inputStr.trim()
		                                   .isEmpty();
	}
	public static boolean isListEmpty(List inputList) {
		return inputList == null || inputList.isEmpty();
	}

	public static boolean hasValidTag(String inputTag){
        for (TAGS tag : TAGS.values()) {
            if(tag.name().equals(inputTag))
                return true;
        }
        return false;
    }
}
