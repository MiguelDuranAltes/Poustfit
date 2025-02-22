import { defineStore } from "pinia";
import accountRepository from '@/communications_backend/repository/AccountRepository'
import authRepository from '@/communications_backend/repository/AuthRepository'

export const useAuthStore = defineStore(
    "authStore",
    {
        state: () => ({
            username: "",
        }),
        getters: {
            isAuthenticated: (state) => !!state.username,
        },
        actions: {
            async register(user){
                await authRepository.registerAccount(user);
                const currentUser = await authRepository.getCurrentUser();
                this.username = currentUser.username;
            },

            async login(userCredentials) {
                await authRepository.authenticate(userCredentials);
                const currentUser = await authRepository.getCurrentUser();
                this.username = currentUser.username;
            },

            async logout() {
                await accountRepository.logout();
                this.username = "";
            },

            async isAuthenticationChecked(){
                try {
                    const currentUser = await authRepository.getCurrentUser();
                    this.username = currentUser.username;
                } catch {
                    // Hay que cerrar sesion, ya que o bien no hay token o el token es invalido o ha expirado
                    await this.logout();
                }
            }
        }
    }
)
