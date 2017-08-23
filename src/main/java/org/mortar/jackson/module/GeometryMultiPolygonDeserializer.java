package org.mortar.jackson.module;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vividsolutions.jts.geom.MultiPolygon;

public class GeometryMultiPolygonDeserializer extends GeometryDeserializer<MultiPolygon> {

	@Override
	public MultiPolygon deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		ArrayNode coordinates = (ArrayNode) node.get(GeometrySerializer.COORDS_FIELD_NAME);
		return coordinates != null ? multiPolygon(coordinates) : null;
	}

}