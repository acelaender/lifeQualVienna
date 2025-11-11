package at.fhtw.lifequalvienna.service;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.stereotype.Service;

@Service
public class GeoService {
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

    public static double distance(double lon1, double lat1, double lon2, double lat2) {
        double a = Math.abs(lat2 - lat1);
        double b = Math.abs(lon2 - lon1);

        double res = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return res;
    }

    public static double[] parsePoint(String point) {
        if (point == null || !point.startsWith("POINT")) {
            throw new IllegalArgumentException("Invalid POINT format: " + point);
        }

        // Extract the content inside the parentheses
        String coords = point.substring(point.indexOf('(') + 1, point.indexOf(')'));
        String[] parts = coords.trim().split("\\s+");

        double lon = Double.parseDouble(parts[0]);
        double lat = Double.parseDouble(parts[1]);

        return new double[]{lon, lat};
    }
}
