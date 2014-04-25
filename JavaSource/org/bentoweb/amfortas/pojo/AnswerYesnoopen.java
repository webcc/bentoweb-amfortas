package org.bentoweb.amfortas.pojo;


import java.util.HashMap;

public class AnswerYesnoopen extends AnswerType
{
	private String optionOther=null;
	private HashMap answerMap=null;
	
	public AnswerYesnoopen() {
		this.answerMap = new HashMap();
	}
	
	public String getAnswer(String key) {
		return this.answerMap.get(key).toString();
	}
	
	public void setAnswer(String key, String value) {
		this.answerMap.put(key, value);
	}
	
	public void setOptionother(String optionOther) {
		this.optionOther=optionOther;
	}
	
	public String getOptionother() {
		return this.optionOther;
	}
}