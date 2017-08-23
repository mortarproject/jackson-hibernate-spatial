package org.mortar.jackson.module;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public abstract class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {
	private static final int DEFAULT_GEOMETRY_SRID = 4326;

	protected GeometryFactory factory = new GeometryFactory(new PrecisionModel(),
			GeometryDeserializer.DEFAULT_GEOMETRY_SRID);

	protected Point point(ArrayNode nodes) {
		Coordinate[] coordinates = toCoordinateArray(nodes);
		Point result = factory.createPoint(coordinates[0]);
		return result;
	}

	protected MultiPoint multiPoint(ArrayNode nodes) {
		Coordinate[] coordinates = toCoordinateArray(nodes);
		MultiPoint result = factory.createMultiPoint(coordinates);
		return result;
	}

	protected LineString lineString(ArrayNode nodes) {
		Coordinate[] coordinates = toCoordinateArray(nodes);
		LineString result = factory.createLineString(coordinates);
		return result;
	}

	protected MultiLineString multiLineString(ArrayNode nodes) {
		LineString[] lineStrings = new LineString[nodes.size()];
		for (int i = 0; i < lineStrings.length; ++i) {
			lineStrings[i] = lineString((ArrayNode) nodes.get(i));
		}
		MultiLineString result = factory.createMultiLineString(lineStrings);
		return result;
	}

	protected Polygon polygon(ArrayNode nodes) {
		LinearRing outerRing = toLinearRing((ArrayNode) nodes.get(0));
		LinearRing[] innerRings = new LinearRing[nodes.size() - 1];
		for (int i = 0; i < innerRings.length; ++i) {
			innerRings[i] = toLinearRing((ArrayNode) nodes.get(i + 1));
		}
		Polygon result = factory.createPolygon(outerRing, innerRings);
		return result;
	}

	protected MultiPolygon multiPolygon(ArrayNode nodes) {
		Polygon[] polygons = new Polygon[nodes.size()];
		for (int i = 0; i < polygons.length; ++i) {
			polygons[i] = polygon((ArrayNode) nodes.get(i));
		}
		MultiPolygon result = factory.createMultiPolygon(polygons);
		return result;
	}

	protected GeometryCollection geometryCollection(ArrayNode nodes) {
		Geometry[] geometries = new Geometry[nodes.size()];
		for (int i = 0; i < geometries.length; ++i) {
			geometries[i] = internalGeometry(nodes.get(i));
		}
		GeometryCollection result = factory.createGeometryCollection(geometries);
		return result;
	}

	protected LinearRing toLinearRing(ArrayNode nodes) {
		Coordinate[] coordinates = toCoordinateArray(nodes);
		LinearRing result = factory.createLinearRing(coordinates);
		return result;
	}

	private Coordinate[] toCoordinateArray(ArrayNode nodes) {
		Coordinate[] result = new Coordinate[nodes.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = toCoordinate(nodes.get(i));
		}
		return result;
	}

	private Coordinate toCoordinate(JsonNode coord) {
		double x = 0, y = 0, z = 0;

		// Iterator<JsonNode> ite = node.elements();
		// while (ite.hasNext()) {
		// JsonNode coord = ite.next();
		JsonNode xNode = ((JsonNode) coord.get("x"));
		JsonNode yNode = ((JsonNode) coord.get("y"));
		JsonNode zNode = ((JsonNode) coord.get("z"));
		x = xNode != null ? xNode.asDouble() : 0;
		y = yNode != null ? yNode.asDouble() : 0;
		z = zNode != null ? zNode.asDouble() : 0;
		// }
		Coordinate result = new Coordinate(x, y, z);
		return result;
	}

	private Geometry internalGeometry(JsonNode node) {
		Geometry result = null;
		String type = node.get("type").textValue();
		ArrayNode coordinates = (ArrayNode) node.get(GeometrySerializer.COORDS_FIELD_NAME);

		switch (type) {

		case "Point":
			result = point(coordinates);
			break;

		case "MultiPoint":
			result = multiPoint(coordinates);
			break;

		case "LineString":
			result = lineString(coordinates);
			break;

		case "MultiLineString":
			result = multiLineString(coordinates);
			break;

		case "Polygon":
			result = polygon(coordinates);
			break;

		case "MultiPolygon":
			result = multiPolygon(coordinates);
			break;

		case "GeometryCollection":
			result = geometryCollection((ArrayNode) node.get("geometries"));
		}

		return result;

	}
}