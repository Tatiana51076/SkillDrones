import React from "react";
import styles from "./Button.module.scss";

export type ButtonVariant = "primary" | "secondary";
export type ButtonSize = "small" | "medium" | "large";

export interface ButtonProps {
  children?: React.ReactNode;
  variant?: ButtonVariant;
  size?: ButtonSize;
  isDisabled?: boolean;
  type?: "button" | "submit" | "reset";
  onClick?: (event: React.MouseEvent<HTMLButtonElement>) => void;
  className?: string;
  icon?: React.ReactNode;
  fullWidth?: boolean;
  isLoading?: boolean;
}

export const Button: React.FC<ButtonProps> = ({
  children,
  variant = "primary",
  size = "medium",
  isDisabled = false,
  type = "button",
  onClick,
  className,
  icon,
  fullWidth = false,
  isLoading = false,
  ...rest
}) => {
  const buttonClasses = [
    styles.button,
    styles[`button--${variant}`],
    styles[`button--${size}`],
    fullWidth && styles["button--fullWidth"],
    isLoading && styles["button--loading"],
    className,
  ]
    .filter(Boolean)
    .join(" ");

  const disabled = isDisabled || isLoading;

  return (
    <button
      type={type}
      className={buttonClasses}
      disabled={disabled}
      onClick={onClick}
      {...rest}
    >
      {isLoading && <span className={styles.button__loader}>🌀</span>}

      {icon && !isLoading && (
        <span className={styles.button__icon}>{icon}</span>
      )}

      {children && <span className={styles.button__text}>{children}</span>}
    </button>
  );
};
