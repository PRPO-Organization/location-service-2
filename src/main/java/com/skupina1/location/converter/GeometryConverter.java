package com.skupina1.location.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = false)
public class GeometryConverter implements AttributeConverter<Point, PGobject> {

    private static final WKTReader wktReader = new WKTReader();
    private static final WKTWriter wktWriter = new WKTWriter();
    private static final WKBReader wkbReader = new WKBReader();

    @Override
    public PGobject convertToDatabaseColumn(Point point) {
        if (point == null) {
            return null;
        }

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("geometry");

            // Ensure SRID is set (default to 4326 for GPS coordinates)
            if (point.getSRID() == 0) {
                point.setSRID(4326);
            }

            // Write as EWKT
            String wkt = wktWriter.write(point);
            String ewkt = "SRID=" + point.getSRID() + ";" + wkt;

            pgObject.setValue(ewkt);
            return pgObject;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to convert Point to PGobject", e);
        }
    }

    @Override
    public Point convertToEntityAttribute(PGobject pgObject) {
        if (pgObject == null || pgObject.getValue() == null) {
            return null;
        }

        try {
            String value = pgObject.getValue();

            // Check if it's WKB (hex string) or EWKT (text)
            if (isHexString(value)) {
                // Parse as WKB
                return parseWKB(value);
            } else {
                // Parse as EWKT
                return parseEWKT(value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse geometry: " + pgObject.getValue(), e);
        }
    }

    private Point parseEWKT(String value) throws ParseException {
        int srid = 0;
        String wkt = value;

        // Parse EWKT: "SRID=4326;POINT(14.505751 46.056946)"
        if (value.startsWith("SRID=")) {
            int semicolonIndex = value.indexOf(';');
            srid = Integer.parseInt(value.substring(5, semicolonIndex));
            wkt = value.substring(semicolonIndex + 1);
        }

        Geometry geometry = wktReader.read(wkt);

        if (!(geometry instanceof Point point)) {
            throw new IllegalArgumentException("Expected Point, got " + geometry.getGeometryType());
        }

        point.setSRID(srid);
        return point;
    }

    private Point parseWKB(String hexString) throws ParseException {
        // Convert hex string to byte array
        byte[] wkb = hexToBytes(hexString);

        // Parse WKB
        Geometry geometry = wkbReader.read(wkb);

        if (!(geometry instanceof Point point)) {
            throw new IllegalArgumentException("Expected Point, got " + geometry.getGeometryType());
        }

        return point;
    }

    private boolean isHexString(String value) {
        // WKB starts with 01 or 00 (byte order marker)
        // Check if it's a hex string (only contains 0-9, A-F)
        return value != null &&
                value.length() > 0 &&
                value.matches("^[0-9A-Fa-f]+$");
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}