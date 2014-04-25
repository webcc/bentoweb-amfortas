package org.bentoweb.amfortas.pojo;

import java.util.HashMap;


public class AnswerMultiple extends AnswerType
{
    private HashMap answerMap=null;
    
	public AnswerMultiple() {
		this.answerMap = new HashMap();
	}
	
	public void setAnswer(String value, String answer) {
		this.answerMap.put(value, answer);
	}
	
	public String getAnswer(String key) {
		return this.answerMap.get(key).toString();
	}
}