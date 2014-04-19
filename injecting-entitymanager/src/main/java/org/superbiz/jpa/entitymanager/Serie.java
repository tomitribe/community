package org.superbiz.jpa.entitymanager;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Serie {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String originalChannel;
    private String title;
    private String format;
    
    public Serie() {
    	super();
    }
    
	public Serie(String originalChannel, String title, String format) {
		super();
		this.originalChannel = originalChannel;
		this.title = title;
		this.format = format;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOriginalChannel() {
		return originalChannel;
	}
	public void setOriginalChannel(String originalChannel) {
		this.originalChannel = originalChannel;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
}
