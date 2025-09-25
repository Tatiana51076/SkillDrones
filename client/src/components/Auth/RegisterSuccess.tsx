import { Button } from "../Button";
import styles from "./Auth.module.scss";
import type { AuthView } from "./Auth";

interface RegisterSuccessProps {
  onSwitchView: (view: AuthView) => void;
}

export const RegisterSuccess = ({ onSwitchView }: RegisterSuccessProps) => {
  const handleSwitchToLogin = () => {
    onSwitchView("login");
  };

  return (
    <div className={styles.authForm}>
      <h2 className={styles.authForm__heading}>Регистрация завершена</h2>
      <p className={styles.authForm__successText}>
        Используйте вашу электронную почту для входа
      </p>
      <Button onClick={handleSwitchToLogin}>Войти</Button>
    </div>
  );
};
