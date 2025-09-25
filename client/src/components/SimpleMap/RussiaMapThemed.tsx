import React, { useEffect, useRef } from "react";
import * as am5 from "@amcharts/amcharts5";
import * as am5map from "@amcharts/amcharts5/map";
import am5geodata_russiaLow from "@amcharts/amcharts5-geodata/russiaLow";
import am5geodata_russiaCrimeaLow from "@amcharts/amcharts5-geodata/russiaCrimeaLow";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";
import type { FeatureCollection, Feature } from "geojson";
import * as d3geo from "d3-geo";
import am5geodata_lang_EN from "@amcharts/amcharts5-geodata/lang/EN";

interface RegionData {
  id: string;
  name: string;
  value?: number;
}

interface RussiaMapProps {
  width?: string;
  height?: string;
  onRegionClick?: (region: RegionData) => void;
  selectedRegionId?: string; // ID региона для отображения
  mapData?: FeatureCollection; // Кастомные геоданные
}

const RussiaMapRegion: React.FC<RussiaMapProps> = ({
  width = "100%",
  height = "600px",
  onRegionClick,
  selectedRegionId,
  mapData,
}) => {
  const chartRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!chartRef.current) return;

    const root = am5.Root.new(chartRef.current);
    root.setThemes([am5themes_Animated.new(root)]);

    // Настройка проекции в зависимости от того, отображаем весь регион или конкретный
    const customProjection = d3geo
      .geoTransverseMercator()
      .scale(selectedRegionId ? 2500 : 1500) // Увеличиваем масштаб для отдельного региона
      .rotate([-93, 0, 0])
      .center(selectedRegionId ? [0, 55] : [0, 65]) // Корректируем центр для отдельного региона
      .translate([0, 0]);

    const chart = root.container.children.push(
      am5map.MapChart.new(root, {
        panX: "none",
        panY: "none",
        wheelY: "none",
        pinchZoom: false,
        projection: customProjection,
        zoomLevel: selectedRegionId ? 1.5 : 0.9, // Увеличиваем зум для отдельного региона
        rotationX: -90,
        centerX: 0,
        centerY: selectedRegionId ? 0 : 10,
      })
    );

    // Функция для получения геоданных нужного региона
    const getRegionGeoJSON = (regionId: string): FeatureCollection => {
      const combinedGeoJSON: FeatureCollection = {
        type: "FeatureCollection",
        features: [
          ...(am5geodata_russiaLow as FeatureCollection).features,
          ...(am5geodata_russiaCrimeaLow as FeatureCollection).features,
        ],
      };

      // Ищем регион по ID
      const regionFeature = combinedGeoJSON.features.find(
        (feature: Feature) =>
          feature.properties?.id === regionId || feature.id === regionId
      );

      // Если нашли регион, возвращаем FeatureCollection только с ним
      if (regionFeature) {
        return {
          type: "FeatureCollection",
          features: [regionFeature],
        };
      }

      // Если регион не найден, возвращаем пустую коллекцию
      return {
        type: "FeatureCollection",
        features: [],
      };
    };

    // Используем кастомные данные или генерируем нужные
    let geoJSON: FeatureCollection;
    if (mapData) {
      geoJSON = mapData;
    } else if (selectedRegionId) {
      geoJSON = getRegionGeoJSON(selectedRegionId);
    } else {
      geoJSON = {
        type: "FeatureCollection",
        features: [
          ...(am5geodata_russiaLow as FeatureCollection).features,
          ...(am5geodata_russiaCrimeaLow as FeatureCollection).features,
        ],
      };
    }

    const polygonSeries = chart.series.push(
      am5map.MapPolygonSeries.new(root, {
        geoJSON: geoJSON,
        geodataNames: am5geodata_lang_EN,
      })
    );

    // Настройка внешнего вида
    polygonSeries.mapPolygons.template.setAll({
      fill: am5.color(selectedRegionId ? "#ff6b6b" : "#67b7dc"), // Разный цвет для выделенного региона
      fillOpacity: 0.8,
      stroke: am5.color("#ffffff"),
      strokeWidth: selectedRegionId ? 2 : 0.5, // Более толстая граница для отдельного региона
      interactive: !selectedRegionId, // Делаем интерактивным только при отображении всей карты
    });

    if (!selectedRegionId) {
      polygonSeries.mapPolygons.template.states.create("hover", {
        fill: am5.color("#5c9ec9"),
        fillOpacity: 1,
      });

      polygonSeries.mapPolygons.template.events.on("click", (ev) => {
        const dataItem = ev.target.dataItem;
        if (dataItem) {
          const dataContext: unknown = dataItem.dataContext;
          if (isRegionData(dataContext) && onRegionClick) {
            onRegionClick(dataContext);
          }
        }
      });

      polygonSeries.mapPolygons.template.set("tooltipText", "{name}");
      polygonSeries.set(
        "tooltip",
        am5.Tooltip.new(root, {
          themeTags: ["map"],
        })
      );
    }

    return () => {
      root.dispose();
    };
  }, [onRegionClick, selectedRegionId, mapData]);

  function isRegionData(data: unknown): data is RegionData {
    return (
      typeof data === "object" &&
      data !== null &&
      "id" in data &&
      "name" in data &&
      typeof (data as RegionData).id === "string" &&
      typeof (data as RegionData).name === "string"
    );
  }

  return (
    <div
      ref={chartRef}
      style={{
        width,
        height,
        backgroundColor: "#f0f2f5",
      }}
    />
  );
};

export default RussiaMapRegion;
