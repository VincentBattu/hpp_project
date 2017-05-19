package model;

import org.joda.time.DateTime;

/**
 * Supertype afin de définis un type générique pour les objets de notre modèle.
 * @author Vincent
 *
 */
public interface Entity {
	
	public DateTime getLastMAJDate();
	public String getDate();
	public int getNbDays();
	public void incrementNbDays();
}