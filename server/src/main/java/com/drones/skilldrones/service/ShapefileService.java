package com.drones.skilldrones.service;

import com.drones.skilldrones.model.Region;
import org.geotools.data.shapefile.ShapefileDataStore; // ИЗМЕНИТЕ ИМПОРТ
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShapefileService {
    
    public List<Region> loadShapefile(String shapefilePath) {
        List<Region> regions = new ArrayList<>();
        
        try {
            File file = new File(shapefilePath);
            URL fileURL = file.toURI().toURL();
            
            // ИСПРАВЛЕННАЯ СТРОКА - используем ShapefileDataStore
            ShapefileDataStore store = new ShapefileDataStore(fileURL);
            store.setCharset(StandardCharsets.UTF_8); // Теперь этот метод существует
            
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection collection = featureSource.getFeatures();
            
            try (SimpleFeatureIterator iterator = collection.features()) {
                WKTReader wktReader = new WKTReader();
                
                while (iterator.hasNext()) {
                    var feature = iterator.next();
                    
                    Region region = new Region();
                    
                    String name = getAttributeValue(feature, "name");
                    region.setName(name);
                    
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    if (geometry != null) {
                        String wkt = geometry.toText();
                        Geometry normalizedGeometry = wktReader.read(wkt);
                        region.setGeometry(normalizedGeometry);
                        
                        double area = calculateAreaKm2(normalizedGeometry);
                        region.setAreaKm2(area);
                    }
                    
                    regions.add(region);
                }
            }
            
            store.dispose();
            
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки шейп-файла: " + e.getMessage(), e);
        }
        
        return regions;
    }
    
    private String getAttributeValue(org.opengis.feature.simple.SimpleFeature feature, String attributeName) {
        try {
            Object value = feature.getAttribute(attributeName);
            return value != null ? value.toString() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
    
    private double calculateAreaKm2(Geometry geometry) {
        try {
            double area = geometry.getArea() * 111 * 111;
            return Math.round(area * 100) / 100.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
