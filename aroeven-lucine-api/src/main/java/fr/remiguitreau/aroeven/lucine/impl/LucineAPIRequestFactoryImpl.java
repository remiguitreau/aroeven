package fr.remiguitreau.aroeven.lucine.impl;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import fr.remiguitreau.aroeven.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeven.lucine.api.LucineAPIRequestFactory;
import fr.remiguitreau.aroeven.lucine.api.LucineAccessDescriptor;
import fr.remiguitreau.aroeven.lucine.api.TownWithGetService;

public class LucineAPIRequestFactoryImpl implements LucineAPIRequestFactory {

    private static final String LUCINE_ACTIONS_API_SUFFIX = "/inscription/actions.php";

    public final static String ID_ORGA_NAME = "id_organisateur";

    public final static String SEASON_NAME = "saison";

    public final static String STAY_YEAR_NAME = "annee_sejour";

    private static final String XLS_STAYS_FILE_ACTION = "submit_xls";

    private static final String ACTION_NAME = "action";

    @Override
    public HttpMethod buildRetrieveStaysMethod(final LucineAccessDescriptor lucineAccessDescriptor,
            final AroevenStaysDescriptor aroevenStaysDescriptor) {
        try {
            final PostMethod method = new PostMethod(buildLucineAccessUrl(lucineAccessDescriptor));
            method.addParameter(ID_ORGA_NAME, String.valueOf(aroevenStaysDescriptor.aroevenSite.code));
            method.addParameter(SEASON_NAME, aroevenStaysDescriptor.season.code);
            method.addParameter(STAY_YEAR_NAME, String.valueOf(aroevenStaysDescriptor.year));
            method.addParameter(ACTION_NAME, "Inscription_ListesRechercheResultat");
            method.addParameter("filtrer", "1");
            method.addParameter("id_sejour", "");
            method.addParameter("intitule", "");
            method.addParameter(XLS_STAYS_FILE_ACTION, "Liste format  XLS");
            appendDefaultParameters(method);
            appendResultColumns(method);
            return method;
        } catch (final Exception ex) {
            throw new LucineRequestBuildException(
                    "Error while creating lucine retrieve stays version request", ex);
        }
    }

    // -------------------------------------------------------------------------

    private void appendDefaultParameters(final PostMethod method) {
        method.addParameter("intitule", "");
        method.addParameter("jour_debut", "");
        method.addParameter("mois_debut", "");
        method.addParameter("annee_debut", "");
        method.addParameter("jour_fin", "");
        method.addParameter("mois_fin", "");
        method.addParameter("annee_fin", "");
        method.addParameter("jour_debut_sejour", "");
        method.addParameter("mois_debut_sejour", "");
        method.addParameter("annee_debut_sejour", "");
        method.addParameter("jour_fin_sejour", "");
        method.addParameter("mois_fin_sejour", "");
        method.addParameter("annee_fin_sejour", "");
        for (final TownWithGetService town : TownWithGetService.values()) {
            method.addParameter("check_depart_" + town.code, "false");
        }
        for (final TownWithGetService town : TownWithGetService.values()) {
            method.addParameter("check_retour_" + town.code, "false");
        }
    }

    private void appendResultColumns(final PostMethod method) {
        method.addParameter("check_date_naissance", "true");
        method.addParameter("check_depart", "true");
        method.addParameter("check_adresse_responsable", "true");
        method.addParameter("check_organisateur", "true");
        method.addParameter("check_responsable", "true");
        method.addParameter("check_retour", "true");
        method.addParameter("check_mail_responsable", "true");
        method.addParameter("check_sejour", "true");
        method.addParameter("check_tel_responsable", "true");
        method.addParameter("check_horaire_depart", "true");
        method.addParameter("check_client", "true");
        method.addParameter("check_option", "true");
        method.addParameter("check_educateur", "true");
        method.addParameter("check_horaire_retour", "true");
        method.addParameter("check_fiche_sanitaire", "true");
        method.addParameter("check_contact", "true");
    }

    private String buildLucineAccessUrl(final LucineAccessDescriptor lucineAccessDescriptor) {
        return lucineAccessDescriptor.url + LUCINE_ACTIONS_API_SUFFIX;
    }

}
