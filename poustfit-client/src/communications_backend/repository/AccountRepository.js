import HTTP from "@/communications_backend/common/http";

export default {

    async logout() {
        return (await HTTP.post(`/my-account/sign-out`)).data;
    },

    async getAccountInfo() {
        return (await HTTP.get(`/my-account/info`)).data;
    }
};
