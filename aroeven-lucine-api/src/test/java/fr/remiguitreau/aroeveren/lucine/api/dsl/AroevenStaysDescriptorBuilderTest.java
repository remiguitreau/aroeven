package fr.remiguitreau.aroeveren.lucine.api.dsl;

import org.junit.Assert;
import org.junit.Test;

import fr.remiguitreau.aroeveren.lucine.api.AroevenSite;
import fr.remiguitreau.aroeveren.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeveren.lucine.api.StaySeason;

public class AroevenStaysDescriptorBuilderTest {

	@Test
	public void test_build_Aroever_stays_descriptor() {
		final AroevenStaysDescriptor aroevenStaysDescriptor = AroevenStaysDescriptorBuilder
				.newStayDescriptor().organizedBy(AroevenSite.CLERMONT)
				.onSeason(StaySeason.SUMMER).fromYear(2041).build();

		Assert.assertEquals(AroevenSite.CLERMONT,
				aroevenStaysDescriptor.aroevenSite);
		Assert.assertEquals(StaySeason.SUMMER,
				aroevenStaysDescriptor.season);
		Assert.assertEquals(2041, aroevenStaysDescriptor.year);
	}

}
