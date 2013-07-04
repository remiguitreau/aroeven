package fr.remiguitreau.aroeven.lucine.api;

public enum AroevenSite {
	BESANCON(30005),

	BORDEAUX(1), CAEN(70002), CLERMONT(10000), DIJON(30003), FOEVEN(10), GRENOBLE(
			60002), LILLE(9), LYON(60001), NANCY(30004), NANTES(50000), ORLEANS(
			70004), PARIS(70003), POITIERS(2), REIMS(30002), RENNES(50001), ROUEN(
			70001), STRASBOURG(30001), TOULOUSE(3), VERSAILLES(20);

	public final int code;

	private AroevenSite(final int code) {
		this.code = code;
	}

}
