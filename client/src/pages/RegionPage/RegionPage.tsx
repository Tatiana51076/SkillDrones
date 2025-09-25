// pages/RegionPage.tsx
import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import RegionMap from "../../components/SimpleMap/RegionMap";

const RegionPage: React.FC = () => {
  const { regionId } = useParams<{ regionId: string }>();
  const navigate = useNavigate();

  if (!regionId) {
    return <div>Регион не указан</div>;
  }

  const handleBackClick = () => {
    navigate(-1); // Возврат назад или navigate('/') для возврата на главную
  };

  return (
    <div style={{ padding: "20px" }}>
      <button
        onClick={handleBackClick}
        style={{
          marginBottom: "20px",
          padding: "10px 20px",
          backgroundColor: "#007bff",
          color: "white",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
      >
        ← Назад к карте России
      </button>

      <h1>Регион: {regionId}</h1>

      <div style={{ marginTop: "30px" }}>
        <RegionMap regionId={regionId} width="100%" height="600px" />
      </div>

      {/* Дополнительная информация о регионе */}
      <div style={{ marginTop: "30px" }}>
        <h2>Информация о регионе</h2>
        <p>Здесь может быть дополнительная информация о регионе {regionId}</p>
        {/* Можно добавить статистику, данные и т.д. */}
      </div>
    </div>
  );
};

export default RegionPage;
