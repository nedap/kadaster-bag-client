package com.nedap.healthcare.kadasterbagclient.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.PointType;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO.Antwoord;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO.Antwoord.Producten;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO.Antwoord.Vraag;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOID;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_producten_apd.v20090901.ADOProduct;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.APD;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMPostcodeAdres;
import nl.kadaster.schemas.imbag.apd.v20090901.GerelateerdeAdressen;
import nl.kadaster.schemas.imbag.apd.v20090901.Nummeraanduiding;
import nl.kadaster.schemas.imbag.apd.v20090901.OpenbareRuimte;
import nl.kadaster.schemas.imbag.apd.v20090901.Verblijfsobject;
import nl.kadaster.schemas.imbag.apd.v20090901.Woonplaats;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.PuntOfVlak;
import nl.kadaster.schemas.imbag.imbag_types.v20090901.Tijdvakgeldigheid;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.mortbay.log.Log;

public class ServiceImpl implements IBagVsRaadplegenDatumADOV20090901 {

    private static final Logger LOG = Logger.getLogger(ServiceImpl.class.getName());
    private Map<String, AntwoordberichtAPDADO> data = null;
    private static Server server;
    private static ServiceImpl endpoint;

    @Override
    public nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum(
            final VraagberichtAPDADOID request1) throws ApplicatieException {
        LOG.info("Executing operation opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum");
        try {
            prepareMockData();

            nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO _return = null;
            if (data.containsKey(request1.getVraag().getAPD().getProductcode())) {
                _return = data.get(request1.getVraag().getAPD().getProductcode());
            }

            destroyMockData();

            return _return;
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(
            final VraagberichtAPDADOAdres request2) throws ApplicatieException {
        LOG.info("Executing operation zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum");
        try {
            prepareMockData();

            nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO _return = null;
            Log.info("searching for : " + request2);
            Log.info("data : " + data);
            if (data.containsKey(request2.getVraag().getNUMPostcodeAdres().getPostcode())) {
                Log.info("found result - " + request2.getVraag().getNUMPostcodeAdres().getPostcode());
                _return = data.get(request2.getVraag().getNUMPostcodeAdres().getPostcode());
            }

            destroyMockData();
            Log.info("returning : " + _return);
            return _return;
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public void prepareMockData() {
        Log.info("prepareMockData start");

        // creating AntwoordberichtAPDADO
        final AntwoordberichtAPDADO a1 = new AntwoordberichtAPDADO();
        a1.setAntwoord(createUniqueAntwoord("1"));
        final AntwoordberichtAPDADO a2 = new AntwoordberichtAPDADO();
        a2.setAntwoord(createUniqueAntwoord("2"));
        final AntwoordberichtAPDADO a3 = new AntwoordberichtAPDADO();
        a3.setAntwoord(createUniqueAntwoord("3"));
        final AntwoordberichtAPDADO a4 = new AntwoordberichtAPDADO();
        a3.setAntwoord(createUniqueAntwoord("4"));

        if (data == null) {
            data = new HashMap<String, AntwoordberichtAPDADO>();
        }

        data.put(a1.getAntwoord().getVraag().getNUMPostcodeAdres().getPostcode(), a1);
        data.put(a2.getAntwoord().getVraag().getNUMPostcodeAdres().getPostcode(), a2);
        data.put(a3.getAntwoord().getVraag().getNUMPostcodeAdres().getPostcode(), a3);
        data.put(a4.getAntwoord().getVraag().getNUMPostcodeAdres().getPostcode(), a4);

        Log.info("prepareMockData end");
    }

    public void destroyMockData() {
        data.clear();
    }

    private Antwoord createUniqueAntwoord(final String unique) {
        final Antwoord antwoord = new Antwoord();
        antwoord.setVraag(createUniqueVraag(unique));
        antwoord.setProducten(createUniqueProducten(unique));

        Log.info("created Antwoord : " + antwoord);
        return antwoord;
    }

    private Producten createUniqueProducten(final String unique) {

        final Tijdvakgeldigheid tij = new Tijdvakgeldigheid();
        tij.setBegindatumTijdvakGeldigheid("20070502120000");
        tij.setEinddatumTijdvakGeldigheid("22991231120000");
        final Nummeraanduiding numm = new Nummeraanduiding();
        numm.setTijdvakgeldigheid(tij);
        if (unique.length() > 0) {
            numm.setHuisnummer(Integer.valueOf(unique));
            numm.setHuisnummertoevoeging("a" + unique);
        } else {
            numm.setHuisnummer(0);
            numm.setHuisnummertoevoeging("a");
        }
        numm.setPostcode("7513KC");

        // city and street data
        final Woonplaats woonplaats = new Woonplaats();
        woonplaats.setWoonplaatsNaam("city" + unique);
        final OpenbareRuimte or = new OpenbareRuimte();
        or.setOpenbareRuimteNaam("street" + unique);
        or.setGerelateerdeWoonplaats(woonplaats);
        numm.setGerelateerdeWoonplaats(woonplaats);
        numm.setGerelateerdeOpenbareRuimte(or);

        // geo positioning data
        final DirectPositionType dpt = new DirectPositionType();
        dpt.getValue().add(87232.211);// value for x
        dpt.getValue().add(469408.512);// value for y
        final PointType pointType = new PointType();
        pointType.setPos(dpt);
        final PuntOfVlak punt = new PuntOfVlak();
        punt.setPoint(pointType);

        final GerelateerdeAdressen ga = new GerelateerdeAdressen();
        ga.setHoofdadres(numm);
        final Verblijfsobject ver = new Verblijfsobject();
        ver.setGerelateerdeAdressen(ga);
        ver.setVerblijfsobjectGeometrie(punt);

        final ADOProduct adop = new ADOProduct();
        adop.setVerblijfsobject(ver);

        final Producten result = new Producten();
        result.getADOProduct().add(adop);

        return result;
    }

    private Vraag createUniqueVraag(final String unique) {
        final Vraag v = new Vraag();
        v.setNUMPostcodeAdres(createUniqueNUMPostcodeAdres(unique));
        v.setAPD(createUniqueAPD(unique));

        return v;
    }

    private NUMPostcodeAdres createUniqueNUMPostcodeAdres(final String unique) {
        final NUMPostcodeAdres result = new NUMPostcodeAdres();

        result.setPostcode("postcode" + unique);
        result.setHuisnummer(Integer.parseInt(unique));
        result.setHuisletter("huisletter" + unique);
        result.setHuisnummertoevoeging("huisnummertoevoeging" + unique);

        return result;
    }

    private APD createUniqueAPD(final String unique) {
        final APD result = new APD();

        result.setActueelDatum("actueelDatum" + unique);
        result.setPeildatum("peildatum" + unique);
        result.setProductcode("productcode" + unique);
        result.setGegVarActueel(true);
        result.setGegVarPeildatum(true);

        return result;
    }

    public static void main(final String args[]) throws Exception {
        if (endpoint == null) {
            endpoint = new ServiceImpl();
            final JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            svrFactory.setServiceClass(IBagVsRaadplegenDatumADOV20090901.class);
            svrFactory.setAddress("http://localhost:9000/serviceTest");
            svrFactory.setServiceBean(endpoint);
            try {
                server = svrFactory.create();
                System.out.println("Server started...");
            } catch (final RuntimeException re) {
                // service already started
            }
        }
        System.out.println("Server ready...");
    }

    public static void destroy() {
        System.out.println("Server about to be destroyed...");
        if (server != null) {
            server.getEndpoint().clear();
            server.stop();
            server.destroy();
        }
        endpoint = null;
        System.out.println("Server destroyed...");
    }

}
