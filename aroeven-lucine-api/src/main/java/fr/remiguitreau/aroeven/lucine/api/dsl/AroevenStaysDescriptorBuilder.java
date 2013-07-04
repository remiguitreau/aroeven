package fr.remiguitreau.aroeven.lucine.api.dsl;

import org.apache.commons.lang3.Validate;

import fr.remiguitreau.aroeven.lucine.api.AroevenSite;
import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.StaySeason;

public class AroevenStaysDescriptorBuilder {

	public static AroevenStaysDescriptorBuilder newStayDescriptor() {		
		return new AroevenStaysDescriptorBuilder();
	}

	// ------------------------------------------------------
	
	private AroevenSite aroevenSite = null;
	
	private StaySeason season = null;
	
	private int year = -1;

	public AroevenStaysDescriptorBuilder organizedBy(AroevenSite anAroevenSite) {
		this.aroevenSite = anAroevenSite;
		return this;
	}

	public AroevenStaysDescriptorBuilder onSeason(StaySeason aSeason) {
		this.season = aSeason;
		return this;
	}

	public AroevenStaysDescriptorBuilder fromYear(int aYear) {
		this.year = aYear;
		return this;
	}

	public AroevenStaysDescriptor build() {
		Validate.notNull(aroevenSite, "AroevenSite must be set for stays descriptor...");
		Validate.notNull(season, "Season must be set for stays descriptor...");
		Validate.isTrue(year > 0, "Year must be > 0 for stays descriptor...");
		return new AroevenStaysDescriptor(aroevenSite, season, year);
	}
	
}
