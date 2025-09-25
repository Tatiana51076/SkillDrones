// Типы для GeoJSON данных
export interface CRS {
  type: string;
  properties: {
    name: string;
  };
}

export interface RegionProperties {
  region: string;
  federal_district: string;
  population: number;
  [key: string]: any; // для дополнительных свойств
}

export interface Geometry {
  type: 'MultiPolygon' | 'Polygon' | 'Point' | 'LineString';
  coordinates: number[][][][] | number[][][] | number[][] | number[];
}

export interface Feature {
  type: 'Feature';
  properties: RegionProperties;
  geometry: Geometry;
}

export interface GeoJSONData {
  type: 'FeatureCollection';
  crs: CRS;
  features: Feature[];
}

// Типы для компонента карты
export interface TooltipState {
  show: boolean;
  content: string;
  x: number;
  y: number;
}

export interface RegionStats {
  name: string;
  population: number;
  district: string;
}