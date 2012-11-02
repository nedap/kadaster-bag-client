package com.nedap.healthcare.json.adapters;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;

/**
 * Json Serializer for Joda DateTime
 * @author wout.slakhorst
 *
 */
public class DateTimeSerializer  extends JsonSerializer<DateTime> {

	@Override
	public void serialize(DateTime arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		// joda DateTime toString does xml string notation
		arg1.writeString(arg0.toString());
	}

}