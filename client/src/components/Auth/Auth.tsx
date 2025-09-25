import { useState } from "react";
import { LoginForm } from "./LoginForm";
import { RegisterForm } from "./RegisterForm";
import { RegisterSuccess } from "./RegisterSuccess";
import styles from "./Auth.module.scss";

export type AuthView = "login" | "register" | "success";

export const Auth = () => {
  const [currentView, setCurrentView] = useState<AuthView>("login");

  const renderCurrentView = () => {
    switch (currentView) {
      case "login":
        return <LoginForm onSwitchView={setCurrentView} />;
      case "register":
        return <RegisterForm onSwitchView={setCurrentView} />;
      case "success":
        return <RegisterSuccess onSwitchView={setCurrentView} />;
      default:
        return <LoginForm onSwitchView={setCurrentView} />;
    }
  };

  return <div className={styles.authContainer}>{renderCurrentView()}</div>;
};
