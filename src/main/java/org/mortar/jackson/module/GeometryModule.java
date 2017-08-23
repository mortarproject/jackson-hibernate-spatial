package org.mortar.jackson.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometryModule extends SimpleModule {

	private static final long serialVersionUID = -5152128875283428325L;

	public GeometryModule() {
		addSerializer(Geometry.class, new GeometrySerializer());

		addDeserializer(Point.class, new GeometryPointDeserializer());
		addDeserializer(MultiPoint.class, new GeometryMultiPointDeserializer());
		addDeserializer(LineString.class, new GeometryLineStringDeserializer());
		addDeserializer(MultiLineString.class, new GeometryMultiLineStringDeserializer());
		addDeserializer(Polygon.class, new GeometryPolygonDeserializer());
		addDeserializer(MultiPolygon.class, new GeometryMultiPolygonDeserializer());
		addDeserializer(GeometryCollection.class, new GeometryGeometryCollectionDeserializer());
	}

}
