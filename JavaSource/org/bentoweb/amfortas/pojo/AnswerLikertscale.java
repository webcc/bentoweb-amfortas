package org.bentoweb.amfortas.pojo;


import java.util.HashMap;;

public class AnswerLikertscale extends AnswerType
{
    private HashMap answerMap=null;
    
	public AnswerLikertscale() {
		this.answerMap = new HashMap();
	}
	
	public void setAnswer(String level, String answer) {
		this.answerMap.put(level, answer);
	}
	
	public String getAnswer(String level) {
		return this.answerMap.get(level).toString();
	}
}