import { type InputHTMLAttributes, forwardRef } from "react";
import styles from "./CustomInput.module.scss";
import { SpriteIcon } from "../SpriteIcon/SpriteIcon";

interface CustomInputProps extends InputHTMLAttributes<HTMLInputElement> {
  errorMessage?: string;
  iconName?: string;
  iconWidth?: number;
  iconHeight?: number;
  isDark?: boolean;
}

export const CustomInput = forwardRef<HTMLInputElement, CustomInputProps>(
  (
    {
      isDark = false,
      errorMessage,
      iconName,
      iconWidth = 24,
      iconHeight = 24,
      className = "",
      ...props
    },
    ref
  ) => {
    const hasError = !!errorMessage;
    const hasIcon = !!iconName;

    return (
      <div
        className={[
          styles.customInput,
          hasError && styles["customInput--error"],
          isDark && styles["customInput--dark"],
          className,
        ]
          .filter(Boolean)
          .join(" ")}
      >
        <div className={styles.customInput__wrapper}>
          <input ref={ref} className={styles.customInput__field} {...props} />
          {hasIcon && (
            <SpriteIcon
              className={styles.customInput__icon}
              name={iconName}
              width={iconWidth}
              height={iconHeight}
            />
          )}
        </div>
        {hasError && (
          <span className={styles.customInput__errorMessage}>
            {errorMessage}
          </span>
        )}
      </div>
    );
  }
);

CustomInput.displayName = "CustomInput";
