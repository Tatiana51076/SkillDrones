import spriteUrl from "../../assets/sprites/sprite.svg";

interface SpriteIconProps {
  name: string;
  width?: number;
  height?: number;
  className?: string;
}

export const SpriteIcon: React.FC<SpriteIconProps> = ({
  name,
  width = 24,
  height = 24,
  className,
}) => {
  return (
    <div
      className={className}
      style={{
        width: width,
        height: height,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <svg width={width} height={height} aria-hidden="true">
        <use xlinkHref={`${spriteUrl}#${name}`}></use>
      </svg>
    </div>
  );
};
