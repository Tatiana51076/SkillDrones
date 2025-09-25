import { CustomInput } from "../CustomInput";
import { Button } from "../Button";
import styles from "./Auth.module.scss";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useAppDispatch, useAppSelector } from "../../app/hooks";
import {
  loginUser,
  selectAuthError,
  selectAuthLoading,
} from "../../app/authSlice";
import type { AuthView } from "./Auth";

const loginSchema = z.object({
  email: z
    .string()
    .min(1, "Email обязателен")
    .email("Введите корректный email"),
  password: z.string().min(1, "Пароль обязателен"),
});

type LoginFormData = z.infer<typeof loginSchema>;

interface LoginFormProps {
  onSwitchView: (view: AuthView) => void;
}

export const LoginForm = ({ onSwitchView }: LoginFormProps) => {
  const dispatch = useAppDispatch();
  const isLoading = useAppSelector(selectAuthLoading);
  const error = useAppSelector(selectAuthError);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    mode: "onBlur",
  });

  const handleSwitchToRegister = () => {
    onSwitchView("register");
  };

  const handleLogin = async (formData: LoginFormData) => {
    const result = await dispatch(loginUser(formData));

    if (loginUser.fulfilled.match(result)) {
      console.log("Успешный вход");
    }

    if (loginUser.rejected.match(result)) {
      console.log("Что то пошло не так");
    }
  };

  return (
    <form className={styles.authForm} onSubmit={handleSubmit(handleLogin)}>
      <h2 className={styles.authForm__heading}>Вход</h2>
      <div className={styles.authForm__wrapper}>
        <CustomInput
          type="email"
          placeholder="Email"
          iconName="icon-email"
          errorMessage={errors.email?.message}
          {...register("email")}
        />
        <CustomInput
          type="password"
          placeholder="Пароль"
          iconName="icon-password"
          errorMessage={errors.password?.message}
          {...register("password")}
        />
      </div>

      {error && <span className={styles.authForm__errorText}>{error}</span>}
      <Button type="submit" isLoading={isLoading}>
        Войти
      </Button>
      <button
        type="button"
        className={styles.authForm__switchButton}
        onClick={handleSwitchToRegister}
      >
        Регистрация
      </button>
    </form>
  );
};
