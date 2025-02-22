import HTTP from "@/communications_backend/common/http";

export default {

    async logout() {
        return (await HTTP.post(`/my-account/sign-out`)).data;
    },

    async getAccountInfo() {
        return (await HTTP.get(`/my-account/info`)).data;
    },

    async getMyFavorites(page, size) {
        const response = await HTTP.get(`/my-account/likes`, { params: { page, size } });
        return response.data;
    },

    async getMyPosts(page, size) {
        const response = await HTTP.get(`/my-account/posts`, { params: { page, size } });
        return response.data;
    },

    async saveImage(file) {
        const formData = new FormData();
        formData.append("file", file);
        const response = await HTTP.post(`/my-account/imagen`, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        return response.data;
    },

    async getImage() {
        return (await HTTP.get(`/my-account/imagen`)).data
    },
};
