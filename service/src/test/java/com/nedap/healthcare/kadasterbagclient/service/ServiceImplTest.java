package com.nedap.healthcare.kadasterbagclient.service;

import junit.framework.Assert;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.AntwoordberichtAPDADO;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.ApplicatieException;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.IBagVsRaadplegenDatumADOV20090901;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOAdres.Vraag;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_apd.v20090901.VraagberichtAPDADOID;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.APD;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMNaamAdres;
import nl.kadaster.schemas.bag_verstrekkingen.bevragingen_selecties.v20090901.NUMPostcodeAdres;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.log.Log;

/**
 * Testing {@link ServiceImpl}.
 * 
 * @author Srdjan Radulovic
 */
public class ServiceImplTest {

    private static IBagVsRaadplegenDatumADOV20090901 client = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        ServiceImpl.main(null);

        // ServiceImpl endpoint = new ServiceImpl();
        // JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        // svrFactory.setServiceClass(IBagVsRaadplegenDatumADOV20090901.class);
        // svrFactory.setAddress("http://localhost:9000/service");
        // svrFactory.setServiceBean(endpoint);
        // svrFactory.create();

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(IBagVsRaadplegenDatumADOV20090901.class);
        factory.setAddress("http://localhost:9000/serviceTest");
        client = (IBagVsRaadplegenDatumADOV20090901) factory.create();

    }

    /**
     * Testing method
     * {@link ServiceImpl#zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(VraagberichtAPDADOAdres)}.
     */
    @Test
    public void testZoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum() throws ApplicatieException {

        // data preparing
        VraagberichtAPDADOAdres address = new VraagberichtAPDADOAdres();
        Vraag vraag = new Vraag();
        NUMNaamAdres naa1 = new NUMNaamAdres();
        naa1.setWoonplaatsId("postalCode1");
        vraag.setNUMNaamAdres(naa1);

        NUMPostcodeAdres npa = new NUMPostcodeAdres();
        npa.setPostcode("postcode1");
        npa.setHuisnummer(1);
        vraag.setNUMPostcodeAdres(npa);

        APD apd1 = new APD();
        apd1.setPeildatum("20090105");
        apd1.setGegVarPeildatum(true);

        vraag.setAPD(apd1);

        address.setVraag(vraag);

        // calling method
        AntwoordberichtAPDADO result = client
                .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(address);
        Log.info("result [good] : " + result.toString());

        // asserting
        Assert.assertNotNull(result);
    }

    /**
     * Testing method
     * {@link ServiceImpl#zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(VraagberichtAPDADOAdres)}.
     * Method should return null because of wrong value for address - productcode parameter.
     */
    @Test
    public void testZoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatumFailForWrongProductcode()
            throws ApplicatieException {

        // data preparing
        VraagberichtAPDADOAdres address = new VraagberichtAPDADOAdres();
        Vraag vraag = new Vraag();
        APD apd1 = new APD();
        apd1.setProductcode("productcode0");
        vraag.setAPD(apd1);
        address.setVraag(vraag);

        NUMPostcodeAdres npa = new NUMPostcodeAdres();
        npa.setPostcode("postcode0");
        npa.setHuisnummer(0);
        vraag.setNUMPostcodeAdres(npa);

        // calling method
        AntwoordberichtAPDADO result = client
                .zoekenAdresseerbaarObjectByPostcodeHuisnummerAndActueelOrPeildatum(address);
        Log.info("result [bad] : " + result);

        // asserting
        Assert.assertNull(result);
    }

    /**
     * Testing method {@link ServiceImpl#opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum(VraagberichtAPDADOID)}.
     */
    @Test
    public void testOpvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum() throws ApplicatieException {

        // data preparing
        VraagberichtAPDADOID id = new VraagberichtAPDADOID();
        VraagberichtAPDADOID.Vraag vraag = new VraagberichtAPDADOID.Vraag();
        APD apd1 = new APD();
        apd1.setProductcode("postcode2");
        vraag.setAPD(apd1);
        id.setVraag(vraag);

        // calling method
        AntwoordberichtAPDADO result = client.opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum(id);

        // asserting
        Assert.assertNotNull(result);
    }

    /**
     * Testing method {@link ServiceImpl#opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum(VraagberichtAPDADOID)}.
     * Method should return null because of wrong value for id - productcode parameter.
     */
    @Test
    public void testOpvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatumFailForProductcode()
            throws ApplicatieException {

        // data preparing
        VraagberichtAPDADOID id = new VraagberichtAPDADOID();
        VraagberichtAPDADOID.Vraag vraag = new VraagberichtAPDADOID.Vraag();
        APD apd1 = new APD();
        apd1.setProductcode("productcode0");
        vraag.setAPD(apd1);
        id.setVraag(vraag);

        // calling method
        AntwoordberichtAPDADO result = client.opvragenAdresseerbaarObjectByAdoIdAndActueelOrPeildatum(id);

        // asserting
        Assert.assertNull(result);
    }

}
