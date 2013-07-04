package fr.remiguitreau.aroeven.lucine.api.dsl;

import org.junit.Assert;
import org.junit.Test;

import fr.remiguitreau.aroeven.lucine.api.AroevenSite;
import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.StaySeason;
import fr.remiguitreau.aroeven.lucine.api.dsl.AroevenStaysDescriptorBuilder;

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
