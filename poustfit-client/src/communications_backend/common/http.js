import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'


const HTTP = axios.create({
    baseURL: "http://localhost:8080/poustfit-server",
    withCredentials: true,
});

const onUnauthorized = async () => {
    const authStore = useAuthStore();
    await authStore.logout();
};

const onResponseSuccess = (response) => {
    return response;
}

const onResponseFailure = async (err) => {
    if (err.response) {
        const status = err.response.status;

        // Excepto cuando estemos haciendo login
        if (!err.config?.url?.includes("current-user")) {
            if (status === 401 || status === 403) {

                await onUnauthorized();
            }
        }
    }
    return Promise.reject(err);
};

HTTP.interceptors.response.use(onResponseSuccess, onResponseFailure);

export default HTTP;
