package org.bentoweb.amfortas.pojo;

import java.util.HashMap;


public class AnswerYesno extends AnswerType
{
	private HashMap answerMap=null;
	
	public AnswerYesno() {
		this.answerMap = new HashMap();
	}
	
	public String getAnswer(String key) {
		return this.answerMap.get(key).toString();
	}
	
	public void setAnswer(String key, String value) {
		this.answerMap.put(key, value);
	}
}