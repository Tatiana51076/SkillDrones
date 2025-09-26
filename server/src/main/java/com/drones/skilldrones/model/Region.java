package com.drones.skilldrones.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Geometry;
import java.time.LocalDateTime;

@Entity
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private Double areaKm2;
    
    @Column(columnDefinition = "geometry(Geometry,4326)")
    private Geometry geometry;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public Region() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // ДОБАВЬТЕ ЭТИ СЕТТЕРЫ:
    public void setRegionId(Long regionId) { this.regionId = regionId; }
    public void setName(String name) { this.name = name; }
    public void setAreaKm2(Double areaKm2) { this.areaKm2 = areaKm2; }
    public void setGeometry(Geometry geometry) { this.geometry = geometry; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // И ДОБАВЬТЕ ГЕТТЕРЫ:
    public Long getRegionId() { return regionId; }
    public String getName() { return name; }
    public Double getAreaKm2() { return areaKm2; }
    public Geometry getGeometry() { return geometry; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
