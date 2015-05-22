package fr.dz.roundish.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Todo representation
 */
@XmlRootElement
public class Todo {

    private Long id;
    private String title;
    private String description;
    private boolean done;

    /*
     * GETTERS & SETTERS
     */

    public Long getId() {
	return id;
    }

    public void setId(final Long id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(final String description) {
	this.description = description;
    }

    public boolean isDone() {
	return done;
    }

    public void setDone(final boolean done) {
	this.done = done;
    }
}
