import type { FC, ReactNode } from "react";
import styles from "./Layout.module.scss";

interface ILayoutProps {
  children: ReactNode;
}

export const Layout: FC<ILayoutProps> = ({ children }) => {
  return <div className={styles.layout}>{children}</div>;
};
