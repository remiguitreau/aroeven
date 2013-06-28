package fr.remiguitreau.aroeveren.lucine.api;

public enum StaySeason {
	SUMMER("ete", "Eté"), WINTER("hiver", "Hiver"), BAFA("bafa", "Bafa"), OTHER(
			"autre", "Autre");

	public final String code;
	public final String humanName;

	private StaySeason(final String value, final String humanName) {
		this.code = value;
		this.humanName = humanName;
	}
}
