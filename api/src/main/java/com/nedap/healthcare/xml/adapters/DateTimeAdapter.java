package com.nedap.healthcare.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.joda.time.DateTime;

/**
 * 
 * @author wout.slakhorst
 * Datetime xml mapping adapter
 */
public class DateTimeAdapter extends XmlAdapter<String, DateTime> {
	public DateTime unmarshal(String v) throws Exception {
		return new DateTime(v);
	}

	public String marshal(DateTime v) throws Exception {
		return v.toString();
	}
}