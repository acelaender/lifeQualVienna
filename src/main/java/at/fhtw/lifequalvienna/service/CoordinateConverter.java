package at.fhtw.lifequalvienna.service;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

public class CoordinateConverter {
    private static final CRSFactory crsFactory = new CRSFactory();
    private static final CoordinateReferenceSystem epsg31256 = crsFactory.createFromName("EPSG:31256");
    private static final CoordinateReferenceSystem wgs84 = crsFactory.createFromName("EPSG:4326");

    public static ProjCoordinate toWgs84(double x, double y) {
        ProjCoordinate src = new ProjCoordinate(x, y);
        ProjCoordinate dst = new ProjCoordinate();
        var transform = new org.locationtech.proj4j.BasicCoordinateTransform(epsg31256, wgs84);
        transform.transform(src, dst);
        return dst;
    }
}
