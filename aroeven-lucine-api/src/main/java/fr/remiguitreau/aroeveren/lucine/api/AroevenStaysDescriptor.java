package fr.remiguitreau.aroeveren.lucine.api;

public class AroevenStaysDescriptor {

	public final AroevenSite aroevenSite;
	
	public final StaySeason season;
	
	/**
	 * Year at format 'yyyy'
	 */
	public final int year;
	
	public AroevenStaysDescriptor(final AroevenSite aroevenSite,final StaySeason season,final int year) {
		this.aroevenSite = aroevenSite;
		this.season = season;
		this.year = year;		
	}
		
}
