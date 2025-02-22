import HTTP from "@/communications_backend/common/http";

export default {

    async registerAccount(user) {
        return (await HTTP.post(`/auth/register`, user)).data;
    },

    async authenticate(userCredentials) {
        return (await HTTP.post(`/auth/authenticate`, userCredentials)).data;
    },

    async getCurrentUser() {
        return (await HTTP.get(`/auth/current-user`)).data;
    }
};
