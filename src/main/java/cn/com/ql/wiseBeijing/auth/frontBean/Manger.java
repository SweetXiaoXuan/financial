package cn.com.ql.wiseBeijing.auth.frontBean;

import javax.ws.rs.FormParam;
public class Manger {
	private int id;
	@FormParam("username")
	private String username;
	@FormParam("password")
	private String password;
	@FormParam("editorcode")
	private String editorcode;
	@FormParam("editor")
	private String editor;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEditorcode() {
		return editorcode;
	}
	public void setEditorcode(String editorcode) {
		this.editorcode = editorcode;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
}
