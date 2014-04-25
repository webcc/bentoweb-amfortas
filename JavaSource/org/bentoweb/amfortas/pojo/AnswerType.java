package org.bentoweb.amfortas.pojo;

public abstract class  AnswerType
{
	private String question=null;
	private String expresult=null;
	// 
	public void setQuestion(String question) {
		this.question=question;
	}
	public String getQuestion() {
		return this.question;
	}
	public void setExpResult(String expResult) {
		this.expresult = expResult;
	}
	public String getExpResult() {
		return this.expresult;
	}
}