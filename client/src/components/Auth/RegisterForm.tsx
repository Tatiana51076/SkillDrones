import { CustomInput } from "../CustomInput";
import { Button } from "../Button";
import styles from "./Auth.module.scss";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../app/hooks";
import {
  clearError,
  registerUser,
  selectAuthError,
  selectAuthLoading,
} from "../../app/authSlice";
import type { AuthView } from "./Auth";

const registerSchema = z
  .object({
    email: z
      .string()
      .min(1, "поле обязательно к заполнению")
      .email("Введите корректный email"),
    username: z.string().min(1, "поле обязательно к заполнению"),
    surname: z.string().min(1, "поле обязательно к заполнению"),
    password: z
      .string()
      .min(8, "Пароль должен содержать минимум 8 символов")
      .regex(/[A-Z]/, "Пароль должен содержать хотя бы одну заглавную букву")
      .regex(/[0-9]/, "Пароль должен содержать хотя бы одну цифру"),
    confirmPassword: z.string().min(1, "поле обязательно к заполнению"),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Пароли не совпадают",
    path: ["confirmPassword"],
  });

type RegisterFormData = z.infer<typeof registerSchema>;

interface RegisterFormProps {
  onSwitchView: (view: AuthView) => void;
}

export const RegisterForm = ({ onSwitchView }: RegisterFormProps) => {
  const dispatch = useAppDispatch();
  const isLoading = useAppSelector(selectAuthLoading);
  const error = useAppSelector(selectAuthError);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    mode: "onBlur",
  });

  useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const handleSwitchToLogin = () => {
    onSwitchView("login");
  };

  const handleRegister = async (formData: RegisterFormData) => {
    const result = await dispatch(
      registerUser({
        email: formData.email,
        password: formData.password,
        name: formData.username,
        surname: formData.surname,
      })
    );

    if (registerUser.fulfilled.match(result)) {
      onSwitchView("success");
      console.log("Регистрация успешна");
    }

    if (registerUser.rejected.match(result)) {
      console.log("Что то пошло не так");
    }
  };

  return (
    <form className={styles.authForm} onSubmit={handleSubmit(handleRegister)}>
      <h2 className={styles.authForm__heading}>Регистрация</h2>
      <div className={styles.authForm__wrapper}>
        <CustomInput
          type="email"
          placeholder="Email"
          iconName="icon-email"
          errorMessage={errors.email?.message}
          {...register("email")}
        />

        <CustomInput
          type="text"
          placeholder="Имя"
          iconName="icon-user"
          errorMessage={errors.username?.message}
          {...register("username")}
        />

        <CustomInput
          type="text"
          placeholder="Фамилия"
          iconName="icon-user"
          errorMessage={errors.surname?.message}
          {...register("surname")}
        />

        <CustomInput
          type="password"
          placeholder="Пароль"
          iconName="icon-password"
          errorMessage={errors.password?.message}
          {...register("password")}
        />

        <CustomInput
          type="password"
          placeholder="Подтвердите пароль"
          iconName="icon-password"
          errorMessage={errors.confirmPassword?.message}
          {...register("confirmPassword")}
        />
      </div>

      {error && <span className={styles.authForm__errorText}>{error}</span>}

      <Button type="submit" isLoading={isLoading}>
        Зарегистрироваться
      </Button>

      <button
        type="button"
        className={styles.authForm__switchButton}
        onClick={handleSwitchToLogin}
      >
        У меня есть пароль
      </button>
    </form>
  );
};
